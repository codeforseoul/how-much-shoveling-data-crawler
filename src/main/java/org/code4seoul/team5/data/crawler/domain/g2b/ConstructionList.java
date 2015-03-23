package org.code4seoul.team5.data.crawler.domain.g2b;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by papillon212 on 15. 3. 22..
 */
@Data
@Root(name = "response", strict = false)
public class ConstructionList {

    @Element
    private Body body;

    @Data
    @Root(strict = false)
    public static class Body {

        @ElementList(name = "items", required = false)
        private List<Construction> constructions;

        @Element(name = "totalCount", required = false)
        private int totalCount;
    }

}
