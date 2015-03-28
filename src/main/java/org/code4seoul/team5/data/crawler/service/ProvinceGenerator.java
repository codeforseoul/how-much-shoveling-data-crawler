package org.code4seoul.team5.data.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.code4seoul.team5.data.crawler.domain.ConstructionSite;
import org.code4seoul.team5.data.crawler.domain.Province;
import org.code4seoul.team5.data.crawler.repository.ConstructionSiteRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by papillon212 on 15. 3. 28..
 */
@Slf4j
@Service
public class ProvinceGenerator {

    @Inject
    private ConstructionSiteRepository constructionSiteRepository;

    public void generate() {
        for (Province province : Province.values()) {
            for (String keyword : province.getKeywords()) {
                List<ConstructionSite> constructionSites = constructionSiteRepository.findByAddressLike(keyword);
                int size = constructionSites.size();
                log.debug("keyword: {}, result cnt: {}", keyword, size);
                if (size > 0) {
                    for (ConstructionSite constructionSite : constructionSites) {
//                        log.debug(constructionSite.toString());
                        constructionSite.setProvince(province.getKorean());
                    }
                    constructionSiteRepository.save(constructionSites);
                }
            }
        }
    }
}
