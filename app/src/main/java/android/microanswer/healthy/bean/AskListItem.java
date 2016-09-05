package android.microanswer.healthy.bean;

import java.io.Serializable;

/**
 * Created by Micro on 2016/6/22.
 */
@Deprecated
public class AskListItem implements Serializable {
    private long id;//id编码
    private String title;//标题
    private int askclass;//分类
    private String img;//图片
    private String description;//描述
    private String keywords;//关键字
    private String message;//内容
    private int count;//访问次数
    private int fcount;//收藏数
    private int rcount;//评论读数
    private long time;//发布时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAskclass() {
        return askclass;
    }

    public void setAskclass(int askclass) {
        this.askclass = askclass;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public int getRcount() {
        return rcount;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "AskListItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", askclass=" + askclass +
                ", img='" + img + '\'' +
                ", description='" + description + '\'' +
                ", keywords='" + keywords + '\'' +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", fcount=" + fcount +
                ", rcount=" + rcount +
                ", time=" + time +
                '}';
    }
}
