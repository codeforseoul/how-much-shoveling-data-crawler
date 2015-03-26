package org.code4seoul.team5.data.crawler;

import lombok.extern.slf4j.Slf4j;
import org.code4seoul.team5.data.crawler.repository.QueryPeriodRepository;
import org.code4seoul.team5.data.crawler.service.ConstructionInfoGenerator;
import org.joda.time.DateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.inject.Inject;

/**
 * Created by papillon212 on 15. 3. 20..
 */
@Slf4j
@SpringBootApplication
public class Starter implements CommandLineRunner {

    @Inject
    private QueryPeriodRepository queryPeriodRepository;

    @Inject
    private ConstructionInfoGenerator constructionInfoGenerator;

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Starter.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        DateTime startDate = DateTime.parse("20141231");
        String from = startDate.toString("yyyyMMdd");
        String to = startDate.minusWeeks(4).toString("yyyyMMdd");

        while (queryPeriodRepository.findOne(from + "-" + to) != null && from.indexOf("2014") > -1) {
            startDate = startDate.minusDays(29);
            from = startDate.toString("yyyyMMdd");
            to = startDate.minusWeeks(4).toString("yyyyMMdd");
        }

        if (from.indexOf("2014") > -1) {
            constructionInfoGenerator.crawlG2B(from, to);
        }
    }
}
