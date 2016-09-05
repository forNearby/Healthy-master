package android.microanswer.healthy.bean;

import java.io.Serializable;

/**
 * Created by Micro on 2016/6/22.
 */

@Deprecated
public class AskClassifyItem implements Serializable {
    private int id;//分类id
    private String name;//分类名字
    private String title;//分类标题
    private String keywords;//分类关键字
    private String description;//分类简介
    private int seq;//分类的排序

    @Override
    public String toString() {
        return "AskClassifyItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", keywords='" + keywords + '\'' +
                ", description='" + description + '\'' +
                ", seq=" + seq +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
