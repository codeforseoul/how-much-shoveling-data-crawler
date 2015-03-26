package org.code4seoul.team5.data.crawler.repository;

import org.code4seoul.team5.data.crawler.domain.QueryPeriod;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by papillon212 on 15. 3. 26..
 */
public interface QueryPeriodRepository extends MongoRepository<QueryPeriod, String> {

}
