package org.code4seoul.team5.data.crawler.repository;

import org.code4seoul.team5.data.crawler.domain.ConstructionSite;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by papillon212 on 15. 3. 26..
 */
public interface ConstructionSiteRepository extends MongoRepository<ConstructionSite, String> {

    public List<ConstructionSite> findByLat(double lat);
}
