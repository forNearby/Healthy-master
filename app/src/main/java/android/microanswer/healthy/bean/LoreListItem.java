package android.microanswer.healthy.bean;

import java.io.Serializable;

/**
 * 健康知识列表条目
 * Created by Micro on 2016/6/21.
 */
@Deprecated
public class LoreListItem implements Serializable {
    private int count;//访问数
    private String description;//简介
    private int fcount;//收藏数
    private long id;//id编码
    private String img;//图片地址
    private String keywords;//关键字
    private int loreclass;//分类
    private int rcount;//评论数
    private long time;//发布时间
    private String title;//标题
    private String message;//内容

    @Override
    public String toString() {
        return "LoreListItem{" +
                "count=" + count +
                ", description='" + description + '\'' +
                ", fcount=" + fcount +
                ", id=" + id +
                ", img='" + img + '\'' +
                ", keywords='" + keywords + '\'' +
                ", loreclass=" + loreclass +
                ", rcount=" + rcount +
                ", time=" + time +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getLoreclass() {
        return loreclass;
    }

    public void setLoreclass(int loreclass) {
        this.loreclass = loreclass;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
