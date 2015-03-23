package org.code4seoul.team5.data.crawler.domain.daum;

import lombok.Data;

/**
 * Created by papillon212 on 15. 3. 22..
 */
@Data
public class Item {
    private String title;
    private String mountain;
    private String localName_1;
    private String localName_2;
    private String localName_3;
    private String mainAddress;
    private String subAddress;
    private String buildingAddress;
    private String isNewAddress;
    private String newAddress;
    private double lng;
    private double lat;
}
