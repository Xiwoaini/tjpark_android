package com.tjsinfo.tjpark.entity;

import java.io.Serializable;

/**
 * Created by panning on 2018/3/5.
 */

public class ParkYuYue implements Serializable {

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

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getShare_id() {
        return share_id;
    }

    public void setShare_id(String share_id) {
        this.share_id = share_id;
    }

    public String getDetail_type() {
        return detail_type;
    }

    public void setDetail_type(String detail_type) {
        this.detail_type = detail_type;
    }

    public String getPark_time() {
        return park_time;
    }

    public void setPark_time(String park_time) {
        this.park_time = park_time;
    }

    private String customer_id = "";
    private String plate_number = "";
    private String plate_id = "";
    private String place_id = "";
    private String place_name = "";
    private String reservation_time = "";
    private String reservation_fee = "";
    private String payMode = "";
    private String record_id = "";
    private String fee = "";
    private String share_id = "";
    private String detail_type = "";
    private String park_time = "";
}
