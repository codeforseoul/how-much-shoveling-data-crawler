package org.code4seoul.team5.data.crawler.domain.daum;

import lombok.Data;

import java.util.List;

/**
 * Created by papillon212 on 15. 3. 22..
 */
@Data
public class Channel {

    private int totalCount;

    private List<Item> item;
}
