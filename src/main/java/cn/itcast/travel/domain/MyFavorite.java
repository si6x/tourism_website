package cn.itcast.travel.domain;

import java.io.Serializable;

/**
 * @author Si6x
 */
public class MyFavorite implements Serializable {
    private int rid;  //表中对应其他表的线路rid
    private String date;  //收藏日期
    private int uid;   //用户id

    public MyFavorite() {
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
