package com.tjsinfo.tjpark.entity;

import java.io.Serializable;

/**
 * Created by panning on 2018/1/15.
 */
//订单实体类
public class Order implements Serializable {
    private String id;
    private String place_id;
    private String place_name;
    private String place_number;
    private String create_time;
    private String in_time;
    private String park_fee;
    private String real_park_fee;
    private String reservation_in_id;
    private String park_time;
    private String park_type;
    private String status;
    private String park_id;
    private String out_time;
    private String reservation_park_fee;

    public String getId() {
        return id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getPlace_number() {
        return place_number;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getIn_time() {
        return in_time;
    }

    public String getPark_fee() {
        return park_fee;
    }

    public String getReal_park_fee() {
        return real_park_fee;
    }

    public String getReservation_in_id() {
        return reservation_in_id;
    }

    public String getPark_time() {
        return park_time;
    }

    public String getPark_type() {
        return park_type;
    }

    public String getStatus() {
        return status;
    }

    public String getPark_id() {
        return park_id;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public void setPlace_number(String place_number) {
        this.place_number = place_number;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public void setPark_fee(String park_fee) {
        this.park_fee = park_fee;
    }

    public void setReal_park_fee(String real_park_fee) {
        this.real_park_fee = real_park_fee;
    }

    public void setReservation_in_id(String reservation_in_id) {
        this.reservation_in_id = reservation_in_id;
    }

    public void setPark_time(String park_time) {
        this.park_time = park_time;
    }

    public void setPark_type(String park_type) {
        this.park_type = park_type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPark_id(String park_id) {
        this.park_id = park_id;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public void setReservation_park_fee(String reservation_park_fee) {
        this.reservation_park_fee = reservation_park_fee;
    }

    public String getReservation_park_fee() {
        return reservation_park_fee;
    }
}
