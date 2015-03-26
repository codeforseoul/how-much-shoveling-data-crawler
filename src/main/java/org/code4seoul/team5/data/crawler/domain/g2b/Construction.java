package org.code4seoul.team5.data.crawler.domain.g2b;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by papillon212 on 15. 3. 21..
 */
@Data
@Root(name = "item", strict = false)
public class Construction {

    @Element(name = "확정계약번호", required = false)
    private String contractNo;

    @Element(name = "계약건명", required = false)
    private String name;

    @Element(name = "계약기관명", required = false)
    private String ownerName;

    @Element(name = "계약기관소관구분", required = false)
    private OwnerType ownerType;

    @Element(name = "계약체결일자", required = false)
    private String startedAt;

    @Element(name = "계약기간", required = false)
    private String duration;

    @Element(name = "업체정보", required = false)
    private String company;

    @Element(name = "총계약금액", required = false)
    private long totalAmount;

    @Element(name = "상세링크", required = false)
    private String link;

    @Element(required = false)
    private Group group;

    @Element(required = false)
    private double lat;

    @Element(required = false)
    private double lng;

    @Element(required = false)
    private String address;

    @Element(required = false)
    private int accuracy;
}
