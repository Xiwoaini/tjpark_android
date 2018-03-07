package tjpark.tjsinfo.com.tjpark.entity;

import java.io.Serializable;

/**
 * Created by panning on 2018/3/6.
 */

public class GreenPark implements Serializable {
    private String id= "";
    private String place_id= "";
    private String place_name= "";
    private String customer_id= "";
    private String park_num= "";
    private String create_time= "";
    private String electricity_fee= "";
    private String park_fee= "";
    private String start_time= "";
    private String end_time= "";
    private String status= "";
    private String share_status= "";
    private String contacts_name= "";
    private String model= "";
    private String label= "";
    private String distance= "";
    private String address= "";

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public String getDistance() {
        return distance;
    }

    public String getAddress() {
        return address;
    }

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

    public String getCreate_time() {
        return create_time;
    }

    public String getElectricity_fee() {
        return electricity_fee;
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

    public String getModel() {
        return model;
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

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setElectricity_fee(String electricity_fee) {
        this.electricity_fee = electricity_fee;
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

    public void setModel(String model) {
        this.model = model;
    }
}
