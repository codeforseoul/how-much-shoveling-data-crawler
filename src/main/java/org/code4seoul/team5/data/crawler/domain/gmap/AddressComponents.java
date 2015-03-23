package org.code4seoul.team5.data.crawler.domain.gmap;

import lombok.Data;

import java.util.List;

/**
 * Created by papillon212 on 15. 3. 22..
 */
@Data
public class AddressComponents {

    private String longName;
    private String shortName;
    private List<String> types;
}
