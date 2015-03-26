package org.code4seoul.team5.data.crawler.domain;

import org.springframework.data.annotation.Id;

/**
 * Created by papillon212 on 15. 3. 27..
 */
public class QueryPeriod {
    @Id
    private String id;

    private String startedAt;

    private String endedAt;
}
