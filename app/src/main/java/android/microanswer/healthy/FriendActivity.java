package android.microanswer.healthy;

import android.content.DialogInterface;
import android.content.Intent;
import android.microanswer.healthy.application.Healthy;
import android.microanswer.healthy.bean.Friend;
import android.microanswer.healthy.view.ItemView2;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.tauth.Tencent;

import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 由 Micro 创建于 2016/7/25.
 */

public class FriendActivity extends BaseActivity implements View.OnClickListener {

    private Friend friend;

    private TextView signature;
    private TextView headview_title;
    private TextView headview_sex;
    private TextView headview_age;
    private TextView headview_city;
    private CircleImageView headview_img;

    private ItemView2 tel;
    private ItemView2 qq;
    private ItemView2 weibo;
    private ItemView2 integral;
    private ItemView2 doman;
    private ItemView2 id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        suitToolBar(R.id.activity_friend_toolbar);
        setToolBarBackEnable();
        friend = (Friend) getIntent().getSerializableExtra("data");


        headview_title = (TextView) findViewById(R.id.activity_friend_headview_title);
        headview_age = (TextView) findViewById(R.id.activity_friend_headview_age);
        headview_city = (TextView) findViewById(R.id.activity_friend_headview_city);
        headview_img = (CircleImageView) findViewById(R.id.activity_friend_headview_img);
        headview_sex = (TextView) findViewById(R.id.activity_friend_headview_sex);
        signature = (TextView) findViewById(R.id.activity_friend_signature);

        tel = (ItemView2) findViewById(R.id.activity_friend_tel);
        qq = (ItemView2) findViewById(R.id.activity_friend_qq);
        weibo = (ItemView2) findViewById(R.id.activity_friend_weibo);
        integral = (ItemView2) findViewById(R.id.activity_friend_integral);
        doman = (ItemView2) findViewById(R.id.activity_friend_home);
        id = (ItemView2) findViewById(R.id.activity_friend_id);
        signature.setText((friend.getSignature() + "").replace("null", "未设置"));

        tel.getContentView(TextView.class).setText((friend.getPhone() + "").replace("null", "未设置"));
        tel.setOnClickListener(this);
        qq.getContentView(TextView.class).setText((friend.getQq() + "").replace("null", "未设置"));
        qq.setOnClickListener(this);
        weibo.getContentView(TextView.class).setText((friend.getWeibo() + "").replace("null", "未设置"));
        integral.getContentView(TextView.class).setText((friend.getIntegral() + "").replace("null", "未设置"));
        doman.getContentView(TextView.class).setText((friend.getDomain() == null ? "null" : ("www.tngou.net/my/" + friend.getDomain())).replace("null", "未设置"));
        id.getContentView(TextView.class).setText((friend.getId() + "").replace("null", "未设置"));

        ImageLoader.getInstance().displayImage("http://tnfs.tngou.net/image" + friend.getAvatar(), headview_img);
        headview_title.setText((friend.getAccount() + "").replace("null", "无名氏"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(friend.getBirth());
        int yearstart = calendar.get(Calendar.YEAR);
        headview_age.setText((year - yearstart) + "岁");
        headview_city.setText(((friend.getProvince() == null ? "" : friend.getProvince()) + " " + (friend.getCity() == null ? "null" : friend.getProvince())).replace("null", "未设置"));
        headview_sex.setText(friend.getGender() == 1 ? "男" : friend.getGender() == 0 ? "女" : "保密");

//        alertDialog(friend.getAccount(),friend.toString()).show();

    }


    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == tel) {
            final String trim = tel.getContentView(TextView.class).getText().toString().trim();
            if (trim.length() >= 8) {
                alertDialog("拨打电话", trim + "\n是一个电话号码，是否拨打？", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setData(Uri.parse("tel:" + trim));
                        intent.setAction(Intent.ACTION_CALL);
                        startActivity(intent);
                    }
                }).show();
            }
        } else if (view == qq) {
            final String qqnumber = qq.getContentView(TextView.class).getText().toString().trim();
            if (qqnumber.matches("^\\d{5,12}$")) {
                alertDialog("发起QQ会话", "要向" + qqnumber + "发起会话吗？", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int i1 = Healthy.tencent.startWPAConversation(FriendActivity.this, qqnumber, "");
                        if (i1 != 0) {
                            toast("会话发起失败", POSOTION_TOP);
                        }
                    }
                }).show();
            }
        }
    }
}
