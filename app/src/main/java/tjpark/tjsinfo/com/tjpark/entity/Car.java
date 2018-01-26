package tjpark.tjsinfo.com.tjpark.entity;

import com.google.gson.JsonElement;

import java.io.Serializable;

/**
 * Created by panning on 2018/1/15.
 */

public class Car  implements Serializable {

    private String id;
    private String place_number;
    private String customer_id;
    private String created_time;
    private String park_id;

    public String getId() {
        return id;
    }

    public String getPlace_number() {
        return place_number;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getPark_id() {
        return park_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlace_number(String place_number) {
        this.place_number = place_number;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public void setPark_id(String park_id) {
        this.park_id = park_id;
    }
}
