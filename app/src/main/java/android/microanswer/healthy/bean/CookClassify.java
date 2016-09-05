package android.microanswer.healthy.bean;

import java.io.Serializable;

/**
 * 由 Micro 创建于 2016/7/12.
 */
@Deprecated
public class CookClassify implements Serializable {
    private int cookclass;
    private String description;
    private int id;
    private String keywords;
    private String name;
    private int seq;
    private String title;

    public int getCookclass() {
        return cookclass;
    }

    public void setCookclass(int cookclass) {
        this.cookclass = cookclass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
