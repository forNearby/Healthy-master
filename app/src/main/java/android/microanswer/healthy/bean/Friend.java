package android.microanswer.healthy.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * 好友类,用于展示用户自己以外的其他好友的信息
 * 由 Micro 创建于 2016/7/20.
 */

public class Friend implements Serializable, Comparable<Friend> {
    private String account;
    private String avatar;
    private long birth;
    private String city;
    private String domain;
    private String email;
    private int gender;
    private String home;
    private int id;
    private int integral;
    private String phone;
    private String province;
    private String qq;
    private String signature;
    private long time;
    private String title;
    private String weibo;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    private String letter;//账号首字母

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "account='" + account + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birth=" + birth +
                ", city='" + city + '\'' +
                ", domain='" + domain + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", home='" + home + '\'' +
                ", id=" + id +
                ", integral=" + integral +
                ", phone='" + phone + '\'' +
                ", province='" + province + '\'' +
                ", qq='" + qq + '\'' +
                ", signature='" + signature + '\'' +
                ", time=" + time +
                ", title='" + title + '\'' +
                ", weibo='" + weibo + '\'' +
                ", letter='" + letter + '\'' +
                '}';
    }

    @Override
    public int compareTo(Friend friend) {
        if (letter != null && friend != null && friend.getLetter() != null) {
            return -1 * friend.getLetter().compareTo(getLetter());
        }
        return 0;
    }

}
