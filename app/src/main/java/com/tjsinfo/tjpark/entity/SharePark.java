package com.tjsinfo.tjpark.entity;

import java.io.Serializable;

/**
 * Created by panning on 2018/1/15.
 */

public class SharePark  implements Serializable {
    private String place_id;
    private String place_name;

    public String getPlace_id() {
        return place_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }
}
