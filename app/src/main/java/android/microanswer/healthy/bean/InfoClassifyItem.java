package android.microanswer.healthy.bean;

import java.io.Serializable;

/**
 * 咨询分类
 * Created by MicroAnswer on 2016/6/29.
 */
@Deprecated
public class InfoClassifyItem implements Serializable {
    private String description;
    private int id;
    private String keywords;
    private String name;
    private int seq;
    private String title;
    private String url;



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "InfoClassifyItem{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", keywords='" + keywords + '\'' +
                ", name='" + name + '\'' +
                ", seq=" + seq +
                ", title='" + title + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
