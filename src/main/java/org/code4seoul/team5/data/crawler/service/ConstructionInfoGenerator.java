package org.code4seoul.team5.data.crawler.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.code4seoul.team5.data.crawler.domain.daum.Item;
import org.code4seoul.team5.data.crawler.domain.daum.Response;
import org.code4seoul.team5.data.crawler.domain.g2b.Construction;
import org.code4seoul.team5.data.crawler.domain.g2b.ConstructionList;
import org.code4seoul.team5.data.crawler.domain.g2b.Group;
import org.code4seoul.team5.data.crawler.domain.gmap.Coordinates;
import org.code4seoul.team5.data.crawler.domain.gmap.Geocode;
import org.code4seoul.team5.data.crawler.domain.gmap.Results;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by papillon212 on 15. 3. 21..
 */
@Slf4j
@Service
public class ConstructionInfoGenerator {

    @Value("${g2b.construction.api.key}")
    private String g2bApiKey;

    @Value("${daum.api.key}")
    private String daumApiKey;

    @Value("${json.output.dir}")
    private String outputDir;

    @Inject
    private G2BService g2BService;

    @Inject
    private GoogleMapService googleMapService;

    @Inject
    private DaumService daumService;

    private Splitter officeSplitter = Splitter.onPattern("\\s+");

    private Joiner addrJoiner = Joiner.on(" ");

    public int totalCnt(String startDate, String endDate) {
        log.debug("startDate: {}, endDate: {}", startDate, endDate);

        ConstructionList.Body body = g2BService.fetchConstructionContracts(
                startDate,
                endDate,
                1,
                1,
                g2bApiKey).getBody();

        return body.getTotalCount();
    }

    public void crawlG2B(DateTime from, int durationWeeks) {
        String startDate = from.toString("yyyyMMdd");
        String endDate = from.plusWeeks(durationWeeks).toString("yyyyMMdd");

        int totalCount = totalCnt(startDate, endDate);

        log.debug("totalCount: {}", totalCount);

        ConstructionList.Body body = g2BService.fetchConstructionContracts(
                startDate,
                endDate,
                totalCount,
                1,
                g2bApiKey).getBody();

        List<Construction> constructions = body.getConstructions();

        log.debug("load fully");

        Map<Group, List<Construction>> groupListMap = constructions.parallelStream()
                .map(construction -> {
                    log.debug("construction: {}", construction);

                    construction.setGroup(Group.NO_ADDR);

                    switch (construction.getOfficeType()) {
                        case 국가기관: {
                            String officeName = construction.getOffice();
                            if (officeName.indexOf("국방부") > -1 || officeName.indexOf("부대") > -1 || officeName.indexOf("국군") > -1) {
                                construction.setGroup(Group.MILITARY);
                            } else {
                                construction.setGroup(Group.NO_ADDR);
                            }
                            break;
                        }
                        case 임의기관:
                        case 정부투자기관: {
                            construction.setGroup(Group.NO_ADDR);
                            break;
                        }
                        case 지방공기업:
                        case 교육기관:
                        case 공기업:
                        case 기타기관: {
//                            findCoordinates(construction, Iterables.getLast(officeSplitter.split(construction.getOffice())));
                            break;
                        }
                        case 기타공공기관:
                        case 준정부기관:
                        case 지자체: {
//                            String officeName = Iterables.getLast(officeSplitter.split(construction.getOffice()));
//                            String firstName = "", secondName = "";
//                            int cnt = 0;
//                            for (String name : officeSplitter.split(construction.getName())) {
//                                if (cnt++ == 0) {
//                                    firstName = name;
//                                } else {
//                                    secondName = name;
//                                    break;
//                                }
//                            }
//
//                            findCoordinates(construction, addrJoiner.join(officeName, firstName, secondName));
//
//                            if (construction.getGroup() == Group.NO_ADDR) {
//                                findCoordinates(construction, addrJoiner.join(officeName, firstName));
//                            }

                            break;
                        }
                    }
                    return construction;
                })
                .collect(Collectors.groupingBy(Construction::getGroup));

        for (Group group : groupListMap.keySet()) {
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(groupListMap.get(group));

            try {
                File outputFile = new File(outputDir, startDate + "-" + endDate + "-" + group.name() + ".json");
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }
                Files.write(json, outputFile, Charset.forName("UTF-8"));
            } catch (IOException e) {
                log.error("failed to write", e);
            }
        }
    }

    public void findCoordinates(String jsonFile) {
        try {
            Gson gson = new GsonBuilder().create();

            List<Construction> constructions = gson.fromJson(Files.newReader(new File(jsonFile), Charset.forName("UTF-8")), new TypeToken<List<Construction>>() {}.getType());
        } catch (FileNotFoundException e) {
            log.error("failed to load json", e);
        }
    }

    private void findCoordinates(Construction construction, String addr) {
        Geocode geocode = null;
        try {
            Response response = null;//daumService.findCoordinates(addr, daumApiKey);
            geocode = null;

            if (response != null && response.getChannel().getTotalCount() == 1) {
                Item item = Iterables.getFirst(response.getChannel().getItem(), null);
                log.debug("addr: {}, daum item: {}", addr, item);
                construction.setLat(item.getLat());
                construction.setLng(item.getLng());
                construction.setGroup(Group.NORMAL);
            } else {
                geocode = googleMapService.findCoordinates(addr);

                if (geocode != null && "OK".equals(geocode.getStatus()) && geocode.getResults().size() == 1) {
                    log.debug("addr: {}, geocode: {}", addr, geocode);

                    Results results = geocode.getResults().get(0);
                    Coordinates location = results.getGeometry().getLocation();
                    construction.setLat(location.getLat());
                    construction.setLng(location.getLng());
                    construction.setGroup(Group.NORMAL);
                }
            }
        } catch (Exception e) {
            log.error("failed to find coordinates", e);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //do nothing
        }

        if (construction.getGroup() == null || construction.getGroup() == Group.NO_ADDR) {
            if (geocode != null && "OK".equals(geocode.getStatus()) && geocode.getResults().size() > 1) {
                construction.setGroup(Group.MULTI_ADDR);
            } else {
                construction.setGroup(Group.NO_ADDR);
            }
        }
    }
}
