
package android.microanswer.healthy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.microanswer.healthy.bean.User;
import android.microanswer.healthy.tools.InternetServiceTool;
import android.microanswer.healthy.view.SimpleViewFocusHinter;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private final String regUrl = "http://www.tngou.net/api/oauth2/reg";//注册地址

    private int REG_THREAD = 3;
    public static final int REG_ACCOUNT = 3;
    private ActionBar actionBar;
    private CheckBox checkBoxpwd;
    private EditText pwd1, pwd2;
    private EditText email;
    private EditText acount;
    private RadioButton boy, gril, keep;
    private EditText checkcode;
    private Button submit;
    private ImageView checkcodeiv;
    private String cpd = "";//随机生成的验证码

    private SharedPreferences sharedPreferences;//用户保存用户输入的注册信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        sharedPreferences = getSharedPreferences("regactivity_data", MODE_PRIVATE);
        initview();
    }

    private void initview() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_reg_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        checkBoxpwd = (CheckBox) findViewById(R.id.activity_reg_checkbox_seepwd);
        checkBoxpwd.setOnCheckedChangeListener(new onCheckBoxCheckChangedListener());
        pwd1 = (EditText) findViewById(R.id.activity_reg_edittext_password1);
        pwd1.setOnFocusChangeListener(this);
        pwd2 = (EditText) findViewById(R.id.activity_reg_edittext_password2);
        pwd2.setOnFocusChangeListener(this);
        email = (EditText) findViewById(R.id.activity_reg_edittext_email);
        email.setOnFocusChangeListener(this);
        pwd2.setOnFocusChangeListener(this);
        acount = (EditText) findViewById(R.id.activity_reg_edittext_acount);
        acount.setOnFocusChangeListener(this);
        boy = (RadioButton) findViewById(R.id.activity_reg_radiobutton_boy);
        gril = (RadioButton) findViewById(R.id.activity_reg_radiobutton_gril);
        keep = (RadioButton) findViewById(R.id.activity_reg_radiobutton_keep);
        checkcode = (EditText) findViewById(R.id.activity_reg_edittext_checkcode);
        checkcode.setOnFocusChangeListener(this);
        checkcodeiv = (ImageView) findViewById(R.id.activity_reg_imageview_checknumber);
        checkcodeiv.setOnClickListener(this);
        submit = (Button) findViewById(R.id.activity_reg_button_submit);
        submit.setOnFocusChangeListener(this);
        submit.setOnClickListener(this);
        runOnOtherThread(new CheckCodeCreater(), 2);

        acount.setText(sharedPreferences.getString("acount", ""));
        email.setText(sharedPreferences.getString("email", ""));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof EditText) {
            if (hasFocus) {
                SimpleViewFocusHinter.makeSimpleViewFocusHinter(this, v, v.getContentDescription().toString()).show();
            } else {
                SimpleViewFocusHinter.dismassMaked();
            }
        } else {
            SimpleViewFocusHinter.dismassMaked();
        }
    }

    public class CheckCodeCreater implements OtherThreadTask<String, Object> {
        @Override
        public Message run(Map<String, Object> param) {
            SystemClock.sleep(300);
            Bitmap checkcoderbitmap = null;
            String checkcoderpwd = null;
            checkcoderpwd = "" + (int) (Math.random() * 8999 + 1000);
            checkcoderbitmap = getCheckBitmap(checkcodeiv.getHeight(), checkcoderpwd);
            Message msg = new Message();
            Map<String, Object> datas = new HashMap<>();
            datas.put("checkcoderpwd", checkcoderpwd);
            datas.put("checkcoderbitmap", checkcoderbitmap);
            msg.obj = datas;
            return msg;
        }

        @Override
        public Map<String, Object> getTaskParams() {
            return null;
        }
    }


    @Override
    public synchronized void onOtherThreadRunEnd(int id, Message message) {
        super.onOtherThreadRunEnd(id, message);
        if (id == 2) {
            Map<String, Object> datas = (Map<String, Object>) message.obj;
            cpd = datas.get("checkcoderpwd").toString();
            Bitmap bitmap = (Bitmap) datas.get("checkcoderbitmap");
            checkcodeiv.setImageBitmap(bitmap);
        } else if (id == REG_THREAD) {
            submit.setEnabled(true);
            submit.setText(getString(R.string.reg));
            String rees = message.obj.toString();
            if (!n(User.getUser().getAccess_token())) {
                //注册成功
                Intent intent = new Intent();
                intent.putExtra("name_pwd", new String[]{acount.getText().toString().trim(), pwd1.getText().toString().trim()});
                setResult(REG_ACCOUNT,intent);
                finish();
            } else {
                //注册失败
                errorDialog(rees,this);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 通过用户输入的数据建立一个完整的请求
     *
     * @return
     */
    private String makeRightUrl(String semal, String sacount, String pwd, int sex, String checkcode) {
        return regUrl + "?client_id=" + client_id + "&client_secret=" + client_secret + "&email=" + semal + "&account=" + sacount + "&password=" + pwd + "&gender=" + sex;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.activity_reg_button_submit) {
            final String semail = email.getText().toString().trim();
            if (n(semail)) {
                toast(getString(R.string.emailhint), POSOTION_TOP);
                return;
            }
            final String sacount = acount.getText().toString().trim();
            if (n(sacount)) {
                toast(getString(R.string.acounthint), POSOTION_TOP);
                return;
            }
            final String spwd1 = pwd1.getText().toString().trim();
            if (n(spwd1)) {
                toast(getString(R.string.pwdhint), POSOTION_TOP);
                return;
            }
            String spwd2 = pwd2.getText().toString().trim();
            if (n(spwd2)) {
                toast(getString(R.string.pwd2hint), POSOTION_TOP);
                return;
            }
            final int ssex = boy.isChecked() ? 1 : (gril.isChecked() ? 0 : -1);
            String scode = checkcode.getText().toString().trim();
            if (n(scode)) {
                toast(getString(R.string.checkcodehint), POSOTION_TOP);
                return;
            }
            if (!isCheckCodeRight(scode)) {
                toast(getString(R.string.checkcode2), POSOTION_TOP);
                return;
            }

            if (!checkEmail(semail)) {
                toast("邮箱格式不正确，请检查", POSOTION_TOP);
                return;
            }


            if (sacount.length() < 6) {
                toast("账号长度至少为6位", POSOTION_TOP);
                return;
            }

            if (!checkAccount(sacount)) {
                toast("账号不合法，开头不能为数字，只能使用字母下划线和$开头，不能包含中文", POSOTION_TOP);
                return;
            }

            if (!TextUtils.equals(spwd1, spwd2)) {
                toast("两次密码输入不相同", POSOTION_TOP);
                return;
            }

            submit.setText(getString(R.string.reging));
            submit.setEnabled(false);
            runOnOtherThread(new OtherThreadTask() {
                @Override
                public Map getTaskParams() {
                    Map parpa = new HashMap();
                    parpa.put("url", regUrl + "?client_id=" + client_id + "&client_secret=" + client_secret + "&email=" + semail + "&account=" + sacount + "&password=" + spwd1 + "&gender=" + ssex + "");
                    return parpa;
                }

                @Override
                public Message run(Map params) {
                    String regurrl = (String) params.get("url");
                    String rees = "{}";
                    try {
                        rees = InternetServiceTool.request(regurrl);
                        JSONObject jsonObject = new JSONObject(rees);
                        if (jsonObject.getBoolean("status")) {
                            User.getUser().setAccess_token(jsonObject.getString("access_token"));
                            User.getUser().setRefresh_token(jsonObject.getString("refresh_token"));
                            User.getUser().setId(jsonObject.getInt("id"));
                        } else {
                            rees = jsonObject.getString("msg") + "\n请重试";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        rees = "连接服务器失败\n请重试";
                    }
                    Message msg = new Message();
                    msg.obj = rees;
                    return msg;
                }
            }, REG_THREAD);


        } else if (v.getId() == R.id.activity_reg_imageview_checknumber) {
            runOnOtherThread(new CheckCodeCreater(), 2);
        }
    }


    public boolean checkAccount(String account) {
        char c = account.charAt(0);
        if ('0' < c && c < '9') {//账号开头不能为数字
            return false;
        }

        for (int i = 0; i < account.length(); i++) {
            c = account.charAt(i);
            if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9') || c == '_' || c == '$') {
                continue;
            } else {
                return false;
            }
        }

        return true;
    }


    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    /**
     * 验证码判断
     *
     * @param checkcode
     * @return
     */
    private boolean isCheckCodeRight(String checkcode) {
        return checkcode.equals(cpd);
    }

    /**
     * 获取验证码
     *
     * @return
     */
    private Bitmap getCheckBitmap(int height, String checkcode) {
        height = height * 4 / 5;
        int width = (height * 4 / 5) * checkcode.length();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Random random = new Random();
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(height);
        int background_r = random.nextInt(255);
        int background_g = random.nextInt(255);
        int background_b = random.nextInt(255);
        int fground_r = 255 - background_r;
        int fground_g = 255 - background_g;
        int fground_b = 255 - background_b;
        p.setColor(Color.rgb(fground_r, fground_g, fground_b));
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRGB(background_r, background_g, background_b);
        for (int i = 0; i < checkcode.length(); i++) {
            char c = checkcode.charAt(i);
            int ca = canvas.save();
            int flag = random.nextInt(2);
            int d = random.nextInt(45);
            if ((i == checkcode.length() - 1) || (flag == 1 && (i == 1 || i == 2))) {
                d = -d;
            }
            for (int j = 0; j < 6; j++) {
                p.setStrokeWidth(random.nextInt(10) + 5);
                p.setColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                canvas.drawPoint(random.nextInt(width), random.nextInt(height), p);
            }

            for (int j = 0; j < 2; j++) {
                p.setStrokeWidth(random.nextInt(5));
                p.setColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                canvas.drawLine(0, random.nextInt(height), width, random.nextInt(height), p);
            }
            p.setStrokeWidth(3);
            p.setColor(Color.rgb(fground_r, fground_g, fground_b));
            canvas.rotate(d, i * height + 10, height * 2 / 3 - 12);
            canvas.drawText(c + "", i * height * 4 / 5 + random.nextInt(5), height * 4 / 5, p);
            canvas.restoreToCount(ca);
        }
        return bitmap;
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


    class onCheckBoxCheckChangedListener implements CheckBox.OnCheckedChangeListener {
        private HideReturnsTransformationMethod hideReturnsTransformationMethod;
        private PasswordTransformationMethod passwordTransformationMethod;

        onCheckBoxCheckChangedListener() {
            hideReturnsTransformationMethod = new HideReturnsTransformationMethod();
            passwordTransformationMethod = PasswordTransformationMethod.getInstance();
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked) {
                pwd1.setTransformationMethod(passwordTransformationMethod);
                pwd2.setTransformationMethod(passwordTransformationMethod);
            } else {
                pwd1.setTransformationMethod(hideReturnsTransformationMethod);
                pwd2.setTransformationMethod(hideReturnsTransformationMethod);
            }
            pwd1.setSelection(pwd1.length());
            pwd2.setSelection(pwd2.length());
        }
    }

    @Override
    public void finish() {
        super.finish();
        sharedPreferences.edit().putString("acount", acount.getText().toString().trim())
                .putString("email", email.getText().toString())
                .apply();
    }
}
