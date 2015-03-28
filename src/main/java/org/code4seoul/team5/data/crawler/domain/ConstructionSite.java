package org.code4seoul.team5.data.crawler.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created by papillon212 on 15. 3. 26..
 */
@Data
@AllArgsConstructor
public class ConstructionSite {

    @Id
    private String contractNo;

    private String name;

    private String sourceLink;

    private String startedAt;

    private String duration;

    private String ownerName;

    private String ownerType;

    private String builderName;

    private int accuracy;

    private String address;

    private long totalAmount;

    private double lat;

    private double lng;

    private String province;
}
