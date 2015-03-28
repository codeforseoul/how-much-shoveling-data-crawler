package org.code4seoul.team5.data.crawler.domain;

/**
 * Created by papillon212 on 15. 3. 28..
 */
public enum Province {
//    Seoul(new String[]{"서울", "Seoul"}),
//    Incheon(new String[]{"인천광역시", "Incheon"}),
//    Gyeonggi_do(new String[]{"경기도", "Gyeonggi-do"}),
//    Gangwon_do(new String[]{"강원도", "Gangwon-do"}),
//    Chungcheongnam_do(new String[]{"충청남도", "Chungcheongnam-do"}),
//    Chungcheongbuk_do(new String[]{"충청북도", "Chungcheongbuk-do"}),
//    Daejeon(new String[]{"대전광역시", "Daejeon"}),
    Gyeongsangbuk_do(new String[]{"경상북도", "Gyeongsangbuk-do"}),
    Daegu(new String[]{"대구광역시", "Daegu"}),
    Ulsan(new String[]{"울산광역시", "Ulsan"}),
    Busan(new String[]{"부산광역시", "Busan"}),
    Gyeongsangnam_do(new String[]{"경상남도", "Gyeongsangnam-do"}),
    Jeollabuk_do(new String[]{"전라북도", "Jeollabuk-do"}),
    Gwangju(new String[]{"광주광역시", "Gwangju"}),
    Jeollanam_do(new String[]{"전라남도", "Jeollanam-do"}),
    Jeju_do(new String[]{"제주도", "Jeju-do}"});

    /**
     * korean, english
     */
    private String[] keywords;

    private String korean;

    private Province(String[] keywords) {
        this.keywords = keywords;
        this.korean = keywords[0];
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String getKorean() {
        return korean;
    }

    public static String match(String address) {
        if (address == null) return null;

        for (Province province : values()) {
            for (String keyword : province.getKeywords()) {
                if (address.indexOf(keyword) > -1) {
                    return province.korean;
                }
            }
        }
        return null;
    }
}
