package org.code4seoul.team5.data.crawler;

import lombok.extern.slf4j.Slf4j;
import org.code4seoul.team5.data.crawler.domain.QueryPeriod;
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
        DateTime startDate = DateTime.parse("2014-12-31");
        String from = startDate.minusDays(2).toString("yyyyMMdd");
        String to = startDate.toString("yyyyMMdd");

        boolean isNoUpdate = true;
        while (from.indexOf("2014") > -1) {
            if (queryPeriodRepository.findOne(from + "-" + to) == null ) {
                isNoUpdate = false;
                break;
            } else {
                startDate = startDate.minusDays(3);
                from = startDate.minusDays(2).toString("yyyyMMdd");
                to = startDate.toString("yyyyMMdd");
            }
        }

        if (!isNoUpdate) {
            queryPeriodRepository.save(new QueryPeriod(from + "-" + to, from, to));
            constructionInfoGenerator.crawlG2B(from, to);
        }
    }
}
