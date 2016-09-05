package android.microanswer.healthy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.microanswer.healthy.application.Healthy;
import android.microanswer.healthy.bean.Collected;
import android.microanswer.healthy.bean.User;
import android.microanswer.healthy.tools.InternetServiceTool;
import android.microanswer.healthy.view.ItemView;
import android.microanswer.healthy.view.ItemView2;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.sql.Date;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 48fe7ed6e993241f335882ddlb27c0d7
 * Created by Micro on 2016/6/12.
 */

public class UserCenterActivity extends BaseActivity implements View.OnClickListener {
    private ActionBar actionBar;

    private ItemView2 headview;
    private ItemView2 userlikeview;
    private ItemView2 userfriend;
    private ItemView2 usernicknameview;
    private ItemView2 sexview;
    private ItemView2 birthdayview;
    private ItemView2 telview;
    private ItemView2 qqview;
    private ItemView2 sinaview;
    private ItemView2 singlnatureview;
    private ItemView2 cityview;

    private CircleImageView headdview;

    private CollapsingToolbarLayout collapsingToolbarLayout;


    private void initview() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_usercenter_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(User.getUser().getAccount());
        }
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_usercenter_collapsingToolBarLayout);
        try{collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);}catch(Exception e){e.printStackTrace();}//设置展开字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后字体颜色;
        headview = (ItemView2) findViewById(R.id.activity_usercenter_itemview_head);
        headview.setOnClickListener(this);
        headdview = (CircleImageView) headview.findViewById(R.id.activity_usercenter_imageview_head);
        userlikeview = (ItemView2) findViewById(R.id.activity_usercenter_itemview_userlike);
        userlikeview.setOnClickListener(this);
        userfriend = (ItemView2) findViewById(R.id.activity_usercenter_itemview_userfriend);
        userfriend.setOnClickListener(this);
        usernicknameview = (ItemView2) findViewById(R.id.activity_usercenter_itemview_usernickname);
        sexview = (ItemView2) findViewById(R.id.activity_usercenter_itemview_usersex);
        birthdayview = (ItemView2) findViewById(R.id.activity_usercenter_itemview_userbrithday);
        telview = (ItemView2) findViewById(R.id.activity_usercenter_itemview_tel);
        qqview = (ItemView2) findViewById(R.id.activity_usercenter_itemview_qq);
        sinaview = (ItemView2) findViewById(R.id.activity_usercenter_itemview_sina);
        singlnatureview = (ItemView2) findViewById(R.id.activity_usercenter_itemview_singnauter);
        cityview = (ItemView2) findViewById(R.id.activity_usercenter_itemview_city);
    }


    private void initdata() {
        User user = User.getUser();
//        TODO 加载头像
//        Log.i("UserCenterActivity", user + "" + usernicknameview);

        ((TextView) usernicknameview.findViewById(R.id.activity_usercenter_textview_nickname)).setText(user.getAccount());
        if (user.getGender() == 1) {
            ((RadioButton) sexview.findViewById(R.id.activity_usercenter_radiobutton_boy)).setChecked(true);
            ((RadioButton) sexview.findViewById(R.id.activity_usercenter_radiobutton_gril)).setChecked(false);
        } else {
            ((RadioButton) sexview.findViewById(R.id.activity_usercenter_radiobutton_gril)).setChecked(true);
            ((RadioButton) sexview.findViewById(R.id.activity_usercenter_radiobutton_boy)).setChecked(false);
        }
        if (user.getGender() == 0) {
            //性别保密
        }

        ImageLoader.getInstance().displayImage(Healthy.IMAGE_URL + user.getAvatar(), headdview);

        Date userBirth = user.getBirth();
        ((TextView) birthdayview.findViewById(R.id.activity_usercenter_TextView_birthday)).setText(userBirth == null ? "未设置" : userBirth.toLocaleString());
        ((TextView) telview.findViewById(R.id.activity_usercenter_TextView_tel)).setText(user.getPhone());
        ((TextView) qqview.findViewById(R.id.activity_usercenter_TextView_qq)).setText(user.getQq());
        ((TextView) sinaview.findViewById(R.id.activity_usercenter_TextView_sina)).setText(user.getWeibo());
        ((TextView) singlnatureview.findViewById(R.id.activity_usercenter_TextView_singnauter)).setText(user.getSignature());
        ((TextView) cityview.findViewById(R.id.activity_usercenter_TextView_city)).setText(user.getCity());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);
        initview();
        initdata();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }

    public void onuttonclick(View v) {
        if (v.getId() == R.id.activity_usercenter_button_exitlogin) {
            loginOut();
            onBackPressed();
        }
    }


    private void loginOut() {
        File f = getAppInternalWorkDir();
        File userfile = new File(f.getAbsolutePath() + "/" + LoginActivity.userObjectFileName);
        if (userfile.exists()) {
            userfile.delete();
        }
        User.setUser(null);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.activity_usercenter_itemview_userlike) {
            jumpTo(CollectedActivity.class, true);
        } else if (id == R.id.activity_usercenter_itemview_userfriend) {
            jumpTo(FriendsActivity.class, true);
        } else if (id == R.id.activity_usercenter_itemview_head) {
            //弹出框，用户选择拍照设置或者从文件系统拿图片
            showPhotoGetDialog();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GET_HEADIMG && resultCode == RESULT_OK) {
//            data.setClass(this, HeadPhotoCutActivity.class);
            data.setAction("com.android.camera.action.CROP");
            data.setDataAndType(data.getData(), "image/png");
            data.putExtra("crop", "true");
            data.putExtra("aspectX", 1);// 裁剪框比例
            data.putExtra("aspectY", 1);
            data.putExtra("outputX", 300);// 输出图片大小
            data.putExtra("outputY", 300);
            data.putExtra("scale", true);//黑边
            data.putExtra("scaleUpIfNeeded", true);//黑边
            data.putExtra("return-data", true);
            data.putExtra("noFaceDetection", true); //关闭人脸检测
            data.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
            data.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
            startActivityForResult(data, REQUEST_CUT_HEADIMG, true);
        } else if (requestCode == REQUEST_CUT_HEADIMG && resultCode == RESULT_OK) {
            if (photofile.exists() && photofile.canRead()) {
                runOnOtherThread(new BaseOtherThread() {

                    private ProgressDialog dialogg = null;

                    @Override
                    void onOtherThreadRunEnd(Message msg) {
                        if (dialogg != null) {
                            dialogg.dismiss();
                        }

                        if (msg.arg1 == 1) {
                            toast("上传成功", POSOTION_TOP);
                            ImageLoader.getInstance().displayImage(msg.obj + "", headdview);
                        } else {
                            errorDialog("" + msg.obj, UserCenterActivity.this);
                        }
                    }

                    @Override
                    public Map getTaskParams() {
                        dialogg = ProgressDialog.show(UserCenterActivity.this, null, "上传中...");
                        return null;
                    }

                    @Override
                    public Message run(Map params) {
                        Message msg = new Message();
                        try {
                            msg.obj = InternetServiceTool.upLoadPhoto(photofile);
                            JSONObject jsonObject = JSON.parseObject(msg.obj + "");
                            i(jsonObject.toString());
                            if (jsonObject.getString("state").equalsIgnoreCase("SUCCESS")) {
                                String changeHeadurl = "http://www.tngou.net/api/user/portrait?access_token=" + User.getUser().getAccess_token() + "&url=" + jsonObject.getString("url");
                                String request = InternetServiceTool.request(changeHeadurl);
                                JSONObject jsonObject1 = JSON.parseObject(request);
                                if (jsonObject1.getBooleanValue("status")) {
                                    String avatar = jsonObject1.getString("avatar");
                                    User.getUser().setAvatar(avatar);
                                    saveUserInFile();
                                    msg.obj = Healthy.IMAGE_URL + avatar;
                                    msg.arg1 = 1;
                                } else {
                                    msg.obj = "头像上传成功，但是刷新不成功";
                                    msg.arg1 = 0;
                                }
                            } else {
                                msg.obj = "头像上传失败";
                                msg.arg1 = 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            msg.arg1 = 0;
                            msg.obj = e.toString();
                        }
                        return msg;
                    }
                }, 45443);
            }
        }
    }

    public static final int REQUEST_GET_HEADIMG = 1;
    public static final int REQUEST_CUT_HEADIMG = 2;
    private File photofile;

    /**
     * 弹窗提示用户选择图片的方式
     */
    private void showPhotoGetDialog() {
        if (photofile == null) {
            photofile = new File(getAppInternalWorkDir(), "headphoto.png");
        }
        alertMenu(null, new String[]{"拍摄照片", "选取照片"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                if (i == 0) {
                    //TODO 拍照

                } else if (i == 1) {
                    //选取
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");

                    startActivityForResult(intent, REQUEST_GET_HEADIMG);
                }
            }
        }).show();
    }
}
