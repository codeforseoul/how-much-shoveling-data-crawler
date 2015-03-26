package org.code4seoul.team5.data.crawler;

import lombok.extern.slf4j.Slf4j;
import org.code4seoul.team5.data.crawler.repository.ConstructionSiteRepository;
import org.code4seoul.team5.data.crawler.service.ConstructionInfoGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.inject.Inject;

/**
 * Created by papillon212 on 15. 3. 20..
 */
@Slf4j
@SpringBootApplication
public class Starter {

    @Inject
    private ConstructionSiteRepository constructionSiteRepository;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            log.debug("Usage: java -jar 20150101 20150102");
            System.exit(0);
        }
        String startDate = args[0];
        String endDate = args[1];

        ConfigurableApplicationContext applicationContext = SpringApplication.run(Starter.class, args);
        ConstructionInfoGenerator constructionInfoGenerator = applicationContext.getBean(ConstructionInfoGenerator.class);
        constructionInfoGenerator.crawlG2B(startDate, endDate);
//        log.debug("total cnt: {}", constructionInfoGenerator.totalCnt(startDate, endDate));
    }

}
