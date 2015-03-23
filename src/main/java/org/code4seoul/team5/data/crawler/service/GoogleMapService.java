package org.code4seoul.team5.data.crawler.service;

import org.code4seoul.team5.data.crawler.domain.gmap.Geocode;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by papillon212 on 15. 3. 22..
 */
public interface GoogleMapService {

    @GET("/maps/api/geocode/json")
    public Geocode findCoordinates(@Query("address") String address);
}
