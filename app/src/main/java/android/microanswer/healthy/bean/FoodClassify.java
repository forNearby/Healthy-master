package android.microanswer.healthy.bean;

import java.io.Serializable;

/**
 * 食物分类
 * 由 Micro 创建于 2016/7/6.
 */
@Deprecated
public class FoodClassify implements Serializable {
    private String description;
    private int foodclass;
    private int id;
    private String keywords;
    private String name;
    private int seq;
    private String title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFoodclass() {
        return foodclass;
    }

    public void setFoodclass(int foodclass) {
        this.foodclass = foodclass;
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
