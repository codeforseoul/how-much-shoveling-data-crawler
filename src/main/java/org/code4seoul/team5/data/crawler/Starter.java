package org.code4seoul.team5.data.crawler;

import lombok.extern.slf4j.Slf4j;
import org.code4seoul.team5.data.crawler.service.ConstructionInfoGenerator;
import org.joda.time.DateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;

/**
 * Created by papillon212 on 15. 3. 20..
 */
@Slf4j
@SpringBootApplication
public class Starter implements CommandLineRunner {

    @Inject
    private ConstructionInfoGenerator constructionInfoGenerator;

    @Override
    public void run(String... args) throws Exception {
        String startDate = DateTime.parse("2015-01-01").toString("yyyyMMdd");
        String endDate = DateTime.parse("2015-01-2").toString("yyyyMMdd");

        constructionInfoGenerator.crawlG2B(startDate, endDate);
//        log.debug("total cnt: {}", constructionInfoGenerator.totalCnt(startDate, endDate));
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Starter.class, args);
    }
}
