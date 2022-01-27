package com.hina122526.tally.db;

public class TypeBean {
    int id;
    String typename;
    int imageId; //沒有選到的圖片ID
    int simageId; //選到的圖片ID
    int kind; //收入-1 支出-0

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getImgeid() {
        return imageId;
    }

    public void setImgeid(int imgeid) {
        this.imageId = imgeid;
    }

    public int getSimgeid() {
        return simageId;
    }

    public void setSimgeid(int simgeid) {
        this.simageId = simgeid;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public TypeBean(String id, String typename, String imageid, String simgid, String kind1, String s) {

    }

    public TypeBean(int id, String typename, int imageId, int simageId, int kind) {
        this.id = id;
        this.typename = typename;
        this.imageId = imageId;
        this.simageId = simageId;
        this.kind = kind;
    }
}
