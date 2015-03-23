package org.code4seoul.team5.data.crawler.service;

import org.code4seoul.team5.data.crawler.domain.g2b.ConstructionList;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by papillon212 on 15. 3. 21..
 */
public interface G2BService {

    @GET("/openapi/service/rest/CntrctInfoService/getCntrctInfoListFcltyCntrctSttus")
    public ConstructionList fetchConstructionContracts(
            @Query("sDate") String sDate,
            @Query("sDate") String eDate,
            @Query("numOfRows") int numOfRows,
            @Query("pageNo") int pageNo,
            @Query(value = "ServiceKey", encodeValue = false) String serviceKey
            );
}
