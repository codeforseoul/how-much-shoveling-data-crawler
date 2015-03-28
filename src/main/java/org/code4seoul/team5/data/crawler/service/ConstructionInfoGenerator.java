package org.code4seoul.team5.data.crawler.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.code4seoul.team5.data.crawler.domain.ConstructionSite;
import org.code4seoul.team5.data.crawler.domain.Province;
import org.code4seoul.team5.data.crawler.domain.daum.Item;
import org.code4seoul.team5.data.crawler.domain.daum.Response;
import org.code4seoul.team5.data.crawler.domain.g2b.Construction;
import org.code4seoul.team5.data.crawler.domain.g2b.ConstructionList;
import org.code4seoul.team5.data.crawler.domain.g2b.Group;
import org.code4seoul.team5.data.crawler.domain.gmap.Coordinates;
import org.code4seoul.team5.data.crawler.domain.gmap.Geocode;
import org.code4seoul.team5.data.crawler.domain.gmap.Results;
import org.code4seoul.team5.data.crawler.repository.ConstructionSiteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

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

    @Value("${google.api.key}")
    private String googleApiKey;

    @Value("${json.output.dir}")
    private String outputDir;

    @Inject
    private G2BService g2BService;

    @Inject
    private GoogleMapService googleMapService;

    @Inject
    private DaumService daumService;

    private Splitter ownerSplitter = Splitter.onPattern("\\s+");

    private Joiner addrJoiner = Joiner.on(" ");

    @Inject
    private ConstructionSiteRepository constructionSiteRepository;

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

    public void crawlG2B(String startDate, String endDate) {
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

        constructions.forEach(construction -> {
            log.debug("construction: {}", construction);

            construction.setGroup(Group.NO_ADDR);

            switch (construction.getOwnerType()) {
                case 국가기관: {
                    String officeName = construction.getOwnerName();
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
                    findCoordinates(construction, Iterables.getLast(ownerSplitter.split(construction.getOwnerName())));
                    break;
                }
                case 기타공공기관:
                case 준정부기관:
                case 지자체: {
                    String ownerName = Iterables.getLast(ownerSplitter.split(construction.getOwnerName()));
                    String firstName = "", secondName = "";
                    int cnt = 0;
                    for (String name : ownerSplitter.split(construction.getName())) {
                        if (cnt++ == 0) {
                            firstName = name;
                        } else {
                            secondName = name;
                            break;
                        }
                    }

                    findCoordinates(construction, addrJoiner.join(ownerName, firstName, secondName));

                    if (construction.getGroup() == Group.NO_ADDR) {
                        findCoordinates(construction, addrJoiner.join(ownerName, firstName));
                    }

                    break;
                }
            }
            if (construction.getGroup() == Group.NORMAL) {
                try {
                    constructionSiteRepository.save(new ConstructionSite(
                            construction.getContractNo(),
                            construction.getName(),
                            construction.getLink(),
                            construction.getStartedAt(),
                            construction.getDuration(),
                            construction.getOwnerName(),
                            construction.getOwnerType().toString(),
                            construction.getCompany(),
                            construction.getAccuracy(),
                            construction.getAddress(),
                            construction.getTotalAmount(),
                            construction.getLat(),
                            construction.getLng(),
                            Province.match(construction.getAddress())
                    ));
                } catch (Exception e) {
                    log.error("skip exception");
                }
            }
        });
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
                geocode = googleMapService.findCoordinates(addr/*, googleApiKey*/);

                if (geocode != null && "OK".equals(geocode.getStatus()) && geocode.getResults().size() == 1) {
                    log.debug("addr: {}, geocode: {}", addr, geocode);

                    Results results = geocode.getResults().get(0);
                    Coordinates location = results.getGeometry().getLocation();
                    construction.setLat(location.getLat());
                    construction.setLng(location.getLng());
                    construction.setGroup(Group.NORMAL);

                    String formattedAddress = results.getFormattedAddress();

                    construction.setAddress(formattedAddress);
                    final int[] accuracy = {0};
                    ownerSplitter.split(addr).forEach(part -> {
                        accuracy[0] += formattedAddress.indexOf(part) > -1 ? 1 : 0;
                    });

                    construction.setAccuracy(accuracy[0]);
                }
            }
        } catch (Exception e) {
            log.error("failed to find coordinates", e);
        }

        if (construction.getGroup() == null || construction.getGroup() == Group.NO_ADDR) {
            if (geocode != null && "OK".equals(geocode.getStatus()) && geocode.getResults().size() > 1) {
                construction.setGroup(Group.MULTI_ADDR);
            } else {
                construction.setGroup(Group.NO_ADDR);
            }
        }

        try {
            Thread.sleep(construction.getGroup() == Group.NORMAL ? 2000 : 2500);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

}
