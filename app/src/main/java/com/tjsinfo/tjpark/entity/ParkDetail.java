package com.tjsinfo.tjpark.entity;

import java.io.Serializable;

/**
 * Created by panning on 2018/1/15.
 */

//封装了所有类型停车场的属性
public class ParkDetail  implements Serializable {

    private String lable;
    private String start_time;
    private String end_time;
    private String type;
    private String period_type;
    private String open_time;
    private String s_time;
    private String e_time;
    private String t_fee;
    private String fee_time;
    private String park_time;
    private String fee;
    private String num;
    private String place_id;
    private String customer_id;
    private String park_num;
    private String phone;
    private String create_time;
    private String park_fee;
    private String status;
    private String share_status;
    private String contacts_name;
    private String realMoney;

    public String getLable() {
        return lable;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getType() {
        return type;
    }

    public String getPeriod_type() {
        return period_type;
    }

    public String getOpen_time() {
        return open_time;
    }

    public String getS_time() {
        return s_time;
    }

    public String getE_time() {
        return e_time;
    }

    public String getT_fee() {
        return t_fee;
    }

    public String getFee_time() {
        return fee_time;
    }

    public String getPark_time() {
        return park_time;
    }

    public String getFee() {
        return fee;
    }

    public String getNum() {
        return num;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getPark_num() {
        return park_num;
    }

    public String getPhone() {
        return phone;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getPark_fee() {
        return park_fee;
    }

    public String getStatus() {
        return status;
    }

    public String getShare_status() {
        return share_status;
    }

    public String getContacts_name() {
        return contacts_name;
    }

    public String getRealMoney() {
        return realMoney;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPeriod_type(String period_type) {
        this.period_type = period_type;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public void setS_time(String s_time) {
        this.s_time = s_time;
    }

    public void setE_time(String e_time) {
        this.e_time = e_time;
    }

    public void setT_fee(String t_fee) {
        this.t_fee = t_fee;
    }

    public void setFee_time(String fee_time) {
        this.fee_time = fee_time;
    }

    public void setPark_time(String park_time) {
        this.park_time = park_time;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public void setPark_num(String park_num) {
        this.park_num = park_num;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setPark_fee(String park_fee) {
        this.park_fee = park_fee;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setShare_status(String share_status) {
        this.share_status = share_status;
    }

    public void setContacts_name(String contacts_name) {
        this.contacts_name = contacts_name;
    }

    public void setRealMoney(String realMoney) {
        this.realMoney = realMoney;
    }
}
