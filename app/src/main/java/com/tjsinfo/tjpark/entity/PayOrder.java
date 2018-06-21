package com.tjsinfo.tjpark.entity;

import java.io.Serializable;

/**
 * Created by panning on 2018/1/15.
 */

//准备提交订单
public class PayOrder  implements Serializable {
    private String customer_id;
    private String plate_number;
    private String plate_id;
    private String place_id;
    private String place_name;
    private String reservation_time;
    private String reservation_fee;
    private String payMode;
    private String realMoney;

    public String getCustomer_id() {
        return customer_id;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public String getPlate_id() {
        return plate_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getReservation_time() {
        return reservation_time;
    }

    public String getReservation_fee() {
        return reservation_fee;
    }

    public String getPayMode() {
        return payMode;
    }

    public String getRealMoney() {
        return realMoney;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public void setPlate_id(String plate_id) {
        this.plate_id = plate_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public void setReservation_time(String reservation_time) {
        this.reservation_time = reservation_time;
    }

    public void setReservation_fee(String reservation_fee) {
        this.reservation_fee = reservation_fee;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public void setRealMoney(String realMoney) {
        this.realMoney = realMoney;
    }
}
