package org.code4seoul.team5.data.crawler.domain.gmap;

import lombok.Data;

/**
 * Created by papillon212 on 15. 3. 22..
 */
@Data
public class Geometry {

    private Bounds bounds;

    private Coordinates location;

    private String locationType;

    private Bounds viewport;
}
