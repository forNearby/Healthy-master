package android.microanswer.healthy.bean;

import java.io.Serializable;

/**
 * 食物详情
 * 由 Micro 创建于 2016/7/6.
 */
@Deprecated
public class FoodListItem implements Serializable {
    private int count;
    private String description;
    private String disease;
    private int fcount;
    private String food;
    private int id;
    private String img;
    private String keywords;
    private String message;
    private String name;
    private int rcount;
    private String summary;
    private String symptom;
    private String url;
    private int foodclass;


    public int getFoodclass() {
        return foodclass;
    }

    public void setFoodclass(int foodclass) {
        this.foodclass = foodclass;
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

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRcount() {
        return rcount;
    }

    public void setRcount(int rcount) {
        this.rcount = rcount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        FoodListItem foodListItem = (FoodListItem) obj;
        return foodListItem.getId() == getId();
    }

    @Override
    public String toString() {
        return "FoodListItem{" +
                "count=" + count +
                ", description='" + description + '\'' +
                ", disease='" + disease + '\'' +
                ", fcount=" + fcount +
                ", food='" + food + '\'' +
                ", id=" + id +
                ", img='" + img + '\'' +
                ", keywords='" + keywords + '\'' +
                ", message='" + message + '\'' +
                ", name='" + name + '\'' +
                ", rcount=" + rcount +
                ", summary='" + summary + '\'' +
                ", symptom='" + symptom + '\'' +
                ", url='" + url + '\'' +
                ", foodclass=" + foodclass +
                '}';
    }
}
