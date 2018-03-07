package tjpark.tjsinfo.com.tjpark.entity;

import java.io.Serializable;

/**
 * Created by panning on 2018/1/15.
 */

public class MyShare  implements Serializable {

    private String id;
    private String place_id;
    private String place_name;

    private String customer_id;
    private String park_num;
    private String is_charing_pile;
    private String   is_fast_pile;
    private String phone;
    private String create_time;
    private String  electricity_fee;
    private String park_fee;
    private String start_time;
    private String end_time;
    private String status;
    private String examine_time;
    private String  examine_id;
    private String examine_name;
    private String share_status;
    private String memo;
    private String contacts_name;
    private String model;
    private String buttonName;

    public String getId() {
        return id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getPlace_name() {
        return place_name;
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

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
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

    public String getButtonName() {
        return buttonName;
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

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }


    public String getIs_charing_pile() {
        return is_charing_pile;
    }

    public String getIs_fast_pile() {
        return is_fast_pile;
    }

    public String getElectricity_fee() {
        return electricity_fee;
    }

    public String getExamine_time() {
        return examine_time;
    }

    public String getExamine_id() {
        return examine_id;
    }

    public String getExamine_name() {
        return examine_name;
    }

    public String getMemo() {
        return memo;
    }

    public String getModel() {
        return model;
    }

    public void setIs_charing_pile(String is_charing_pile) {
        this.is_charing_pile = is_charing_pile;
    }

    public void setIs_fast_pile(String is_fast_pile) {
        this.is_fast_pile = is_fast_pile;
    }

    public void setElectricity_fee(String electricity_fee) {
        this.electricity_fee = electricity_fee;
    }

    public void setExamine_time(String examine_time) {
        this.examine_time = examine_time;
    }

    public void setExamine_id(String examine_id) {
        this.examine_id = examine_id;
    }

    public void setExamine_name(String examine_name) {
        this.examine_name = examine_name;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
