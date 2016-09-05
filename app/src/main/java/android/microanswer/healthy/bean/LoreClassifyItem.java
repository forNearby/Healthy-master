package android.microanswer.healthy.bean;

import java.io.Serializable;

/**
 * Created by Micro on 2016/6/23.
 */
@Deprecated
public class LoreClassifyItem implements Serializable {
    private String description;
    private int id;
    private String keywords;
    private String name;
    private String title;
    private int seq;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "LoreClassifyItem{" +
                "description='" + description + '\'' +
                ", id=" + id +
                ", keywords='" + keywords + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", seq=" + seq +
                '}';
    }
}
