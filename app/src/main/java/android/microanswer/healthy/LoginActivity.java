package android.microanswer.healthy;

import android.content.Intent;
import android.microanswer.healthy.bean.User;
import android.microanswer.healthy.tools.InternetServiceTool;
import android.microanswer.healthy.view.SimpleViewFocusHinter;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener, CheckBox.OnCheckedChangeListener, View.OnFocusChangeListener {
    private static final String loginurl = "http://www.tngou.net/api/oauth2/login";//用户登陆
    private static final String userurl = "http://www.tngou.net/api/user";//获取用户信息
    public static final int REQUEST_LOGIN = 4;

    private EditText etaccount, et_pwd;
    private TextView forgotpwd, noaccount;
    private Button submit;
    private ImageView qq, sina;
    private CheckBox seepwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
    }

    private void initview() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        seepwd = (CheckBox) findViewById(R.id.activity_login_checkbox_seepwd);
        seepwd.setOnCheckedChangeListener(this);
        etaccount = (EditText) findViewById(R.id.activity_login_edittext_acount);
        etaccount.setOnFocusChangeListener(this);
        et_pwd = (EditText) findViewById(R.id.activity_login_edittext_password1);
        et_pwd.setOnFocusChangeListener(this);
        forgotpwd = (TextView) findViewById(R.id.activity_login_textview_forgotpwd);
        forgotpwd.setOnClickListener(this);
        noaccount = (TextView) findViewById(R.id.activity_login_textview_noaccount);
        noaccount.setOnClickListener(this);
        noaccount.setOnFocusChangeListener(this);
        submit = (Button) findViewById(R.id.activity_login_submit);
        submit.setOnClickListener(this);
        submit.setOnFocusChangeListener(this);
        qq = (ImageView) findViewById(R.id.activity_login_qq);
        qq.setOnClickListener(this);
        sina = (ImageView) findViewById(R.id.activity_login_sina);
        sina.setOnClickListener(this);
    }

    private String getRightUrl(String account, String pwd) {
        return loginurl + "?client_id=" + client_id + "&client_secret=" + client_secret + "&name=" + account + "&password=" + pwd;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * 字符串判断空
     *
     * @param ss
     * @return
     */
    private boolean n(String ss) {
        return TextUtils.isEmpty(ss);
    }


    private final int LOGIN_REQUEST_THREAD = 3;
    private final int GET_USER_INFO_THREAD = 4;

    @Override
    public void onClick(View v) {
        v.requestFocus();
        switch (v.getId()) {
            case R.id.activity_login_submit:
                String name = etaccount.getText().toString().trim();
                if (n(name)) {
                    toast(getString(R.string.acounthint), POSOTION_TOP);
                    return;
                }
                final String pwd = et_pwd.getText().toString().trim();
                if (n(pwd)) {
                    toast(getString(R.string.pwdhint), POSOTION_TOP);
                    return;
                }
                submit.setText(getString(R.string.logining));
                submit.setEnabled(false);
                final String url = getRightUrl(name, pwd);
                runOnOtherThread(new OtherThreadTask<String, Object>() {
                    @Override
                    public Map<String, Object> getTaskParams() {
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("url", url);
                        param.put("user", User.getUser());
                        return param;
                    }

                    @Override
                    public Message run(Map<String, Object> params) {
                        String murl = (String) params.get("url");
                        User user = (User) params.get("user");
                        String respones = "{}";
                        try {
                            respones = InternetServiceTool.request(murl);
                            try {
                                respones = respones == null ? "{}" : respones;
                                JSONObject jo = new JSONObject(respones);
                                if (jo.getBoolean("status")) {
                                    user.setAccess_token(jo.getString("access_token"));
                                    user.setRefresh_token(jo.getString("refresh_token"));
                                    user.setId(jo.getInt("id"));
                                    user.setPassword(pwd);
                                } else {
                                    respones = jo.getString("msg");
                                }
                            } catch (Exception e) {
//                                e.printStackTrace();
                                respones = getString(R.string.login_error) + "\n[服务器连接失败]";
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Map<String, Object> datas = new HashMap<String, Object>();
                        datas.put("res", respones);
                        datas.put("user", user);
                        Message message = new Message();
                        message.obj = datas;
                        return message;
                    }
                }, LOGIN_REQUEST_THREAD);


                break;
            case R.id.activity_login_qq://QQ登陆
//                int login = Healthy.tencent.login(this, "all", miuiListener);
//                toast(login + "", POSOTION_TOP);
                jumpForResultTo(QQLoginActivity.class, false, 1111);
                break;
            case R.id.activity_login_sina://新浪登陆
                break;
            case R.id.activity_login_textview_noaccount:
                jumpForResultTo(RegActivity.class, true, RegActivity.REG_ACCOUNT);
                break;
            case R.id.activity_login_textview_forgotpwd:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RegActivity.REG_ACCOUNT) {
            String[] name_pwds = data.getStringArrayExtra("name_pwd");

            etaccount.setText(name_pwds[0]);
            et_pwd.setText(name_pwds[1]);
            User.getUser().setPassword(name_pwds[1]);
            toast("注册成功，正在登录", POSOTION_TOP);
            requestuserinfo();
        } else if (resultCode == QQLoginActivity.LOGINOK) {
            String code = data.getStringExtra("code");

            final String accesstokenurl = "http://www.tngou.net/api/oauth2/accesstoken?code=" + code + "&client_id=" + client_id + "&client_secret=" + client_secret;

            runOnOtherThread(new BaseOtherThread() {
                @Override
                void onOtherThreadRunEnd(Message msg) {
                    requestuserinfo();
                }

                @Override
                public Map getTaskParams() {
                    Map<String, String> url = new HashMap<String, String>();
                    url.put("url", accesstokenurl);
                    return url;
                }

                @Override
                public Message run(Map params) {

                    String url = (String) params.get("url");

                    String request = InternetServiceTool.request(url);
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(request);
                    if (jsonObject.getBooleanValue("status")) {
                        User.getUser().setId(jsonObject.getInteger("uid"));
                        User.getUser().setAccess_token(jsonObject.getString("access_token"));
                        User.getUser().setRefresh_token(jsonObject.getString("refresh_token"));
                    }
                    return null;
                }
            }, 765);
            

        }

    }

    private void requestuserinfo() {
        runOnOtherThread(new OtherThreadTask<String, String>() {

            @Override
            public Map<String, String> getTaskParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("url", userurl + "?access_token=" + User.getUser().getAccess_token());
                return param;
            }

            @Override
            public Message run(Map<String, String> params) {
                String uurl = params.get("url");
                String res = "";
                ObjectOutputStream objectOutputStream = null;
                try {
                    res = InternetServiceTool.request(uurl);
                    JSONObject jsondata = new JSONObject(res);

                    if (jsondata.getBoolean("status")) {
                        User user = User.getUser();
                        try {
                            if (jsondata.has("id"))
                            user.setId(jsondata.getInt("id"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("account"))
                            user.setAccount(jsondata.getString("account"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("avatar"))
                                user.setAvatar(jsondata.getString("avatar"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("birth"))
                            user.setBirth(new Date(jsondata.getLong("birth")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("city"))
                            user.setCity(jsondata.getString("city"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("domain"))
                            user.setDomain(jsondata.getString("domain"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("email"))
                            user.setEmail(jsondata.getString("email"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("gender"))
                            user.setGender(jsondata.getInt("gender"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("home"))
                            user.setHome(jsondata.getString("home"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("integral"))
                            user.setIntegral(jsondata.getInt("integral"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("isemail"))
                            user.setIsemail(jsondata.getInt("isemail"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("isphone"))
                            user.setIsphone(jsondata.getInt("isphone"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("phone"))
                            user.setPhone(jsondata.getString("phone"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("province"))
                            user.setProvince(jsondata.getString("province"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("qq"))
                            user.setQq(jsondata.getString("qq"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("qqid"))
                                user.setQqid(jsondata.getString("qqid"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("signature"))
                            user.setSignature(jsondata.getString("signature"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("time"))
                            user.setTime(jsondata.getLong("time"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("title"))
                            user.setTitle(jsondata.getString("title"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("weibo"))
                            user.setWeibo(jsondata.getString("weibo"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (jsondata.has("weiboid"))
                            user.setWeiboid(jsondata.getInt("weiboid"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        File appInternalWorkDir = getAppInternalWorkDir();
                        if (appInternalWorkDir != null) {
                            objectOutputStream = new ObjectOutputStream(new FileOutputStream(appInternalWorkDir.getAbsolutePath() + "/" + userObjectFileName));
                            objectOutputStream.writeObject(user);
                            objectOutputStream.flush();
                        } else {
                            Log.i("将登录信息写入文件", "文件目录创建失败");
                        }
//                        Log.e("LoginActivity", user.toString());
                    } else {
                        try {
                            res = jsondata.getString("msg") + "\n请重试";
                        } catch (Exception e) {
                            res = "我们这边出了一点小问题，请重试";
                        }
                    }


                } catch (Exception e) {
                            e.printStackTrace();
                    res = "服务器连接失败,请重试";
                } finally {
                    try {
                        if (objectOutputStream != null)
                            objectOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Message msg = new Message();
                msg.obj = res;
                return msg;
            }
        }, GET_USER_INFO_THREAD);
    }

    @Override
    public synchronized void onOtherThreadRunEnd(int id, Message msg) {
        super.onOtherThreadRunEnd(id, msg);
        if (id == LOGIN_REQUEST_THREAD) {
            Map<String, Object> datas = (Map<String, Object>) msg.obj;
            submit.setText(getString(R.string.login));
            submit.setEnabled(true);
            if (n(User.getUser().getAccess_token())) {
                toast(datas.get("res").toString(), POSOTION_TOP);//登录失败
            } else {//登录成功
                requestuserinfo();
            }
        } else if (id == GET_USER_INFO_THREAD) {
            String ress = msg.obj.toString();
            if (ress.contains("请重试")) {
                toast(ress, POSOTION_TOP);
            } else {
                setResult(REQUEST_LOGIN);
                finish();
            }

        }
    }

    public void finish() {
        SimpleViewFocusHinter.dismassMaked();
        super.finish();
    }

    private HideReturnsTransformationMethod hideReturnsTransformationMethod = new HideReturnsTransformationMethod();
    private PasswordTransformationMethod passwordTransformationMethod = PasswordTransformationMethod.getInstance();

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            et_pwd.setTransformationMethod(passwordTransformationMethod);
        } else {
            et_pwd.setTransformationMethod(hideReturnsTransformationMethod);
        }

        et_pwd.setSelection(et_pwd.length());

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof EditText) {
            if (hasFocus) {
                SimpleViewFocusHinter.makeSimpleViewFocusHinter(this, v, "" + (v).getContentDescription().toString()).show();
            } else {
                SimpleViewFocusHinter.dismassMaked();
            }
        } else {
            SimpleViewFocusHinter.dismassMaked();
        }
    }


    /**
     * qq登陆结果Bean
     */
    public static class QQResult implements Serializable {

        /**
         * ret : 0
         * openid : EBFBF391BECFD988EF505C999D10B448
         * access_token : 2218772A44E0338F827B273235CCEF8B
         * pay_token : 3D10A4E2C60DAABFCDEF7543D4B8B73D
         * expires_in : 7776000
         * pf : desktop_m_qq-10000144-android-2002-
         * pfkey : 06b2ee67bb7869f3695aa29d692f6344
         * msg :
         * login_cost : 319
         * query_authority_cost : 349
         * authority_cost : 0
         */

        private int ret;
        private String openid;
        private String access_token;
        private String pay_token;
        private int expires_in;
        private String pf;
        private String pfkey;
        private String msg;
        private int login_cost;
        private int query_authority_cost;
        private int authority_cost;

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getPay_token() {
            return pay_token;
        }

        public void setPay_token(String pay_token) {
            this.pay_token = pay_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getPf() {
            return pf;
        }

        public void setPf(String pf) {
            this.pf = pf;
        }

        public String getPfkey() {
            return pfkey;
        }

        public void setPfkey(String pfkey) {
            this.pfkey = pfkey;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getLogin_cost() {
            return login_cost;
        }

        public void setLogin_cost(int login_cost) {
            this.login_cost = login_cost;
        }

        public int getQuery_authority_cost() {
            return query_authority_cost;
        }

        public void setQuery_authority_cost(int query_authority_cost) {
            this.query_authority_cost = query_authority_cost;
        }

        public int getAuthority_cost() {
            return authority_cost;
        }

        public void setAuthority_cost(int authority_cost) {
            this.authority_cost = authority_cost;
        }
    }
}
