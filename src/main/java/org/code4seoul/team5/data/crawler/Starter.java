package org.code4seoul.team5.data.crawler;

import lombok.extern.slf4j.Slf4j;
import org.code4seoul.team5.data.crawler.service.ConstructionInfoGenerator;
import org.joda.time.DateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by papillon212 on 15. 3. 20..
 */
@Slf4j
@SpringBootApplication
public class Starter {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(Starter.class, args);

        applicationContext.getBean(ConstructionInfoGenerator.class).crawlG2B(DateTime.parse("2015-01-01"), 12);

//        String startDate = DateTime.parse("2015-01-01").toString("yyyyMMdd");
//        String endDate = DateTime.now().toString("yyyyMMdd");
//        int totalCnt = applicationContext.getBean(ConstructionInfoGenerator.class).totalCnt(startDate, endDate);
//
//        log.debug("totalCnt: {}", totalCnt);
    }
}
