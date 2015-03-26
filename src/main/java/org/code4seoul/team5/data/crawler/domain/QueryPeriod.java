package org.code4seoul.team5.data.crawler.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created by papillon212 on 15. 3. 27..
 */
@Data
@AllArgsConstructor
public class QueryPeriod {
    @Id
    private String id;

    private String startedAt;

    private String endedAt;
}
