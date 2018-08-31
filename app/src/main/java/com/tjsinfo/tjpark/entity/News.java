package com.tjsinfo.tjpark.entity;

/**
 * Created by panning on 2018/7/6.
 */

public class News {
        private String id;
    private String imgs;
    private String title;
    private String releasetime;
    private String updatetime;
    private String name;
    private String contail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleasetime() {
        return releasetime;
    }

    public void setReleasetime(String releasetime) {
        this.releasetime = releasetime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContail() {
        return contail;
    }

    public void setContail(String contail) {
        this.contail = contail;
    }
}
