package android.microanswer.healthy.bean;

import java.io.Serializable;
import java.sql.Date;

/**
 * 用户Bean
 * Created by Micro on 2016/6/14.
 */

public class User implements Serializable{

    private int id;
    private String account;    //       登录帐号
    private String email;    //       邮箱
    private String password;    //           密码
    private String domain = "";    //           用户主页域名
    private String title = "";        //   个性标题
    private String avatar;    //       头像地址
    private String home;    //      个人主页
    private String phone;    //       联系电话 手机，电话等
    private String qq;    //      QQ号
    private String weibo;    //       微博名称
    private int gender;    //      性别，1：男 0：女
    private java.sql.Date birth;    //      出生年月
    private String province;    //       省份
    private String city;    //      城市
    private String signature;   //个性签名
    private int integral;    //      用户积分
    private int isemail;    //      是否显示邮箱 1：显示 0：不显示
    private int isphone;    //     是否显示联系电话 1：显示 0：不显示
    private int role;    //     用户权限，0：普通用户，1：管理员；-1:未激活；-2：黑名单
    private String access_token;    //     钥匙，做认证
    private String refresh_token;    //      预备认证
    private String qqid;//    QQ ID
    private long weiboid;    //      微博ID
    private long time;//注册时间


    private static User user;

    private User() {
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public static User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getIsemail() {
        return isemail;
    }

    public void setIsemail(int isemail) {
        this.isemail = isemail;
    }

    public int getIsphone() {
        return isphone;
    }

    public void setIsphone(int isphone) {
        this.isphone = isphone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getQqid() {
        return qqid;
    }

    public void setQqid(String qqid) {
        this.qqid = qqid;
    }

    public long getWeiboid() {
        return weiboid;
    }

    public void setWeiboid(long weiboid) {
        this.weiboid = weiboid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", domain='" + domain + '\'' +
                ", title='" + title + '\'' +
                ", avatar='" + avatar + '\'' +
                ", home='" + home + '\'' +
                ", phone='" + phone + '\'' +
                ", qq='" + qq + '\'' +
                ", weibo='" + weibo + '\'' +
                ", gender=" + gender +
                ", birth=" + birth +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", signature='" + signature + '\'' +
                ", integral=" + integral +
                ", isemail=" + isemail +
                ", isphone=" + isphone +
                ", role=" + role +
                ", access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", qqid='" + qqid + '\'' +
                ", weiboid=" + weiboid +
                ", time=" + time +
                '}';
    }
}
