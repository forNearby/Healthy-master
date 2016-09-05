package android.microanswer.healthy.bean;

import java.io.Serializable;

/**
 * 由 Micro 创建于 2016/8/4.
 */

public class Collected implements Serializable {

    /**
     * id : 867
     * oid : 6632
     * otype : info
     * tag : 改行做起了婴儿洗澡店
     * time : 1470301918000
     * title : 改行做起了婴儿洗澡店
     * url : http://www.tngou.net/info/show/6632
     * user : 1569
     */

    private int id;
    private int oid;
    private String otype;
    private String tag;
    private long time;
    private String title;
    private String url;
    private int user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Collected{" +
                "id=" + id +
                ", oid=" + oid +
                ", otype='" + otype + '\'' +
                ", tag='" + tag + '\'' +
                ", time=" + time +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", user=" + user +
                '}';
    }
}
