package org.code4seoul.team5.data.crawler.service;

import org.code4seoul.team5.data.crawler.domain.daum.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by papillon212 on 15. 3. 22..
 */
public interface DaumService {

    @GET("/local/geo/addr2coord?output=json")
    public Response findCoordinates(@Query("q") String address, @Query("apikey") String apikey);
}
