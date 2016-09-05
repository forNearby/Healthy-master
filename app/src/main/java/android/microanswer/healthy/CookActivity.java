package android.microanswer.healthy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.microanswer.healthy.application.Healthy;
import android.microanswer.healthy.bean.Collected;
import android.microanswer.healthy.bean.CookListItem;
import android.microanswer.healthy.bean.User;
import android.microanswer.healthy.tools.BaseTools;
import android.microanswer.healthy.tools.InternetServiceTool;
import android.microanswer.healthy.tools.JavaBeanTools;
import android.microanswer.healthy.view.HtmlView;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康菜谱,详细信息
 * 由 Micro 创建于 2016/8/12.
 */

public class CookActivity extends BaseActivity implements View.OnClickListener {
    private CookListItem cookListItem;

    private View cook_img_bg;
    private ImageView cook_img;
    private TextView cook_title;
    private TextView cook_desc;
    private TextView cook_like$dislike;
    private LinearLayout key_words_content;
    private HtmlView cook_htmlview;

    private ProgressDialog dialog;
    private static SparseArray<CookListItem> cachedata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);
        suitToolBar(R.id.activity_cook_toolbar);
        setToolBarBackEnable();

        if (cachedata == null) {
            cachedata = new SparseArray<>();
        }


        cook_img_bg = findViewById(R.id.activity_cook_head_relativelayout);
        cook_desc = (TextView) findViewById(R.id.activity_cook_desc);
        cook_img = (ImageView) findViewById(R.id.activity_cook_img);
        cook_title = (TextView) findViewById(R.id.activity_cook_title);
        cook_like$dislike = (TextView) findViewById(R.id.activity_cook_like$dislike);
        cook_like$dislike.setEnabled(false);
        cook_like$dislike.setOnClickListener(this);
        key_words_content = (LinearLayout) findViewById(R.id.activity_cook_keywords);
        cook_htmlview = (HtmlView) findViewById(R.id.activity_cook_htmlview);


        Serializable serializable = getIntent().getSerializableExtra("data");


        if (serializable instanceof CookListItem) {

            cookListItem = (CookListItem) serializable;

            if (cookListItem != null) {
                ImageLoader.getInstance().displayImage(Healthy.IMAGE_URL + cookListItem.getImg(), cook_img, new DisplayImageOptions.Builder()
                        .displayer(new BitmapDisplayer() {
                            @Override
                            public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                                Bitmap b = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                                Canvas c = new Canvas(b);
                                c.drawColor(Color.LTGRAY);
                                c.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(4, 4, b.getWidth() - 4, b.getHeight() - 4), null);
                                imageAware.setImageBitmap(b);
                            }
                        }).
                                build(), new CookImageLoadListener());
                cook_desc.setText(cookListItem.getDescription());
                cook_title.setText(cookListItem.getName());
            }
        } else if (serializable instanceof Collected) {
            Collected collected = (Collected) serializable;
            cookListItem = new CookListItem();
            cookListItem.setId(collected.getOid());
            dialog = ProgressDialog.show(this, null, "加载中...");
        }

        if (cachedata.get(cookListItem.getId()) != null) {
            if (dialog != null) {
                dialog.dismiss();
            }
            cookListItem = cachedata.get(cookListItem.getId());
            onCookListItemRequestOK();
        } else {
            requestCookListItem();
        }



        requestIsLike();
    }


    /**
     * 当数据请求成功的时候回调
     */
    private void onCookListItemRequestOK() {
        key_words_content.removeAllViews();
        cachedata.put(cookListItem.getId(), cookListItem);
        ImageLoader.getInstance().displayImage(Healthy.IMAGE_URL + cookListItem.getImg(), cook_img, new DisplayImageOptions.Builder()
                .displayer(new BitmapDisplayer() {
                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                        Bitmap b = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                        Canvas c = new Canvas(b);
                        c.drawColor(Color.LTGRAY);
                        c.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(4, 4, b.getWidth() - 4, b.getHeight() - 4), null);
                        imageAware.setImageBitmap(b);
                    }
                }).
                        build(), new CookImageLoadListener());
        cook_desc.setText(cookListItem.getDescription());
        cook_title.setText(cookListItem.getName());
        String keywords = cookListItem.getKeywords();
        String[] split = keywords.split(" ");
        for (String aSplit : split) {
            TextView tv = new TextView(this);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundResource(R.drawable.shape_rount_colorprimay);
            int plr = BaseTools.Dp2Px(this, 10f);
            int ptb = BaseTools.Dp2Px(this, 3f);
            tv.setPadding(plr, ptb, plr, ptb);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(plr, ptb, 0, ptb);
            tv.setLayoutParams(p);
            tv.setText(aSplit);
            key_words_content.addView(tv);
        }

        cook_htmlview.setHtml(cookListItem.getMessage());


    }

    private void disLike() {
        runOnOtherThread(new BaseOtherThread() {
            @Override
            public void onOtherThreadRunEnd(Message msg) {
                cook_like$dislike.setEnabled(true);
                if (msg.arg1 == LIKE) {
                    cook_like$dislike.setTag(LIKE);
                    cook_like$dislike.setText("取消收藏");
                } else if (msg.arg1 == UNLIKE) {
                    cook_like$dislike.setText("收藏");
                    cook_like$dislike.setTag(UNLIKE);
                    if (msg.obj != null) {
                        toast(msg.obj + "", POSOTION_BOTTOM);
                    }
                }

            }

            @Override
            public Map getTaskParams() {
                cook_like$dislike.setEnabled(false);
                HashMap<String, Object> params = new HashMap<>();
                params.put("id", cookListItem.getId());
                params.put("type", "cook");
                params.put("access_token", User.getUser().getAccess_token());
                return params;
            }

            @Override
            public Message run(Map params) {
                String url = "http://www.tngou.net/api/favorite/delete?id=" + params.get("id") + "&access_token=" + params.get("access_token") + "&type=" + params.get("type");
                Message msg = new Message();

                try {
                    String request = InternetServiceTool.request(url);
                    JSONObject jsonObject = JSON.parseObject(request);
                    if (jsonObject.getBooleanValue("status")) {
                        msg.arg1 = jsonObject.getIntValue("favorite");
                    } else {
                        msg.arg1 = UNLIKE;
                        msg.obj = jsonObject.getString("msg");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.arg1 = UNLIKE;
                    msg.obj = e;
                }
                return msg;
            }
        }, 51322);

    }


    private void like(final String keyWord) {
        runOnOtherThread(new BaseOtherThread() {
            @Override
            public void onOtherThreadRunEnd(Message msg) {
                cook_like$dislike.setEnabled(true);
                if (msg.arg1 == LIKE) {
                    cook_like$dislike.setTag(LIKE);
                    cook_like$dislike.setText("取消收藏");
                } else if (msg.arg1 == UNLIKE) {
                    cook_like$dislike.setText("收藏");
                    cook_like$dislike.setTag(UNLIKE);
                    if (msg.obj != null) {
                        toast(msg.obj + "", POSOTION_BOTTOM);
                        alertDialog("需要登录", "你还没有登录，是否立即登录？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                jumpTo(LoginActivity.class, true);
                            }
                        }).show();
                    }
                }

            }

            @Override
            public Map getTaskParams() {
                HashMap<String, Object> parp = new HashMap<>();
                try {
                    parp.put("id", cookListItem.getId());
                    parp.put("type", "cook");
                    parp.put("title", URLEncoder.encode(cookListItem.getName(), "UTF-8"));
                    parp.put("access_token", User.getUser().getAccess_token());
                    parp.put("keyword", isTextEmpty(keyWord) ? cookListItem.getName() : keyWord);
                    cook_like$dislike.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return parp;
            }

            @Override
            public Message run(Map params) {
                String url = null;
                try {
                    url = "http://www.tngou.net/api/favorite/add?id=" + params.get("id") + "&type=" + params.get("type") + "&title=" + params.get("title") + "&access_token=" + params.get("access_token") + "&keyword=" + URLEncoder.encode(params.get("keyword") + "", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                try {
                    String request = InternetServiceTool.request(url);

                    JSONObject jsonObject = JSON.parseObject(request);

                    if (jsonObject.getBooleanValue("status")) {
                        msg.arg1 = jsonObject.getIntValue("favorite");
                    } else {
                        msg.obj = jsonObject.getString("msg");
                        msg.arg1 = UNLIKE;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    msg.obj = e.toString();
                    msg.arg1 = UNLIKE;
                }

                return msg;
            }
        }, 34523);
    }


    /**
     * 请求是否是已经收藏的菜谱
     */
    private void requestIsLike() {
        runOnOtherThread(new BaseOtherThread() {
            @Override
            void onOtherThreadRunEnd(Message msg) {
                cook_like$dislike.setTag(msg.arg1);
                cook_like$dislike.setEnabled(true);
                if (msg.arg1 == 0) {
                    cook_like$dislike.setText("收藏");
                    if (msg.obj != null) {
                        toast(msg.obj + "", POSOTION_TOP);
                    }
                } else {
                    cook_like$dislike.setText("取消收藏");
                }
            }

            @Override
            public Map getTaskParams() {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("id", cookListItem.getId());
                params.put("access_token", User.getUser().getAccess_token());
                return params;
            }

            @Override
            public Message run(Map params) {
                String url = "http://www.tngou.net/api/favorite?id=" + params.get("id") + "&type=cook&access_token=" + params.get("access_token");
                Message msg = new Message();
                try {

                    String request = InternetServiceTool.request(url);

                    JSONObject jsonObject = JSON.parseObject(request);

                    if (jsonObject.getBooleanValue("status")) {
                        msg.arg1 = jsonObject.getIntValue("favorite");
                    } else {
                        msg.arg1 = 0;
                        msg.obj = jsonObject.getString("msg");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.arg1 = 0;
                    msg.obj = e;
                }

                return msg;
            }
        }, 29384);
    }


    /**
     * 请求完整的健康菜谱数据
     */
    private void requestCookListItem() {
        runOnOtherThread(new BaseOtherThread() {
            @Override
            void onOtherThreadRunEnd(Message msg) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (msg.obj == null) {
                    errorDialog("加载失败,请稍后再试", CookActivity.this);
                } else {
                    CookActivity.this.cookListItem = (CookListItem) msg.obj;
                    onCookListItemRequestOK();
//                    alertDialog("加载成功", cookListItem.toString()).show();
                }
            }

            @Override
            public Map getTaskParams() {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("id", cookListItem.getId());
                return params;
            }

            @Override
            public Message run(Map params) {
                Message msg = new Message();
                msg.obj = JavaBeanTools.Cook.getCookShow((Integer) params.get("id"));
                return msg;
            }
        }, 50023);
    }

    @Override
    public void onClick(View view) {
        if (cook_like$dislike.getTag() != null)
            if (((int) cook_like$dislike.getTag()) == LIKE) {//取消收藏
                disLike();
            } else {//添加收藏
                AlertDialog.Builder duilder = new AlertDialog.Builder(this);
                duilder.setTitle("添加收藏");
                final EditText et = new EditText(this);
                et.setHint("添加收藏标签");
                duilder.setView(et);
                duilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        like(et.getText().toString());
                    }
                });
                duilder.setNegativeButton("取消", null);
                duilder.show();
            }
    }


    /**
     * 图片加载监听
     */
    private class CookImageLoadListener implements ImageLoadingListener {

        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            runOnOtherThread(new DoCookImgBg(loadedImage), 229);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    }

    /**
     * 模糊背景操作
     */
    private class DoCookImgBg extends BaseOtherThread {

        private Bitmap bitmap;

        private DoCookImgBg(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        void onOtherThreadRunEnd(Message msg) {
            cook_img_bg.setBackgroundDrawable(new BitmapDrawable((Bitmap) msg.obj));
        }

        @Override
        public Map getTaskParams() {
            HashMap<String, Bitmap> map = new HashMap<>();
            map.put("data", this.bitmap);
            return map;
        }

        @Override
        public Message run(Map params) {
            Message msg = new Message();
            msg.obj = BaseTools.doBlur2((Bitmap) params.get("data"));
            return msg;
        }
    }


    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return super.onHomeButtonClick();
    }

    private static final int LIKE = 1;
    private static final int UNLIKE = 0;
}
