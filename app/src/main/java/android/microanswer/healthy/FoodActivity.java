package android.microanswer.healthy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.microanswer.healthy.application.Healthy;
import android.microanswer.healthy.bean.Collected;
import android.microanswer.healthy.bean.FoodListItem;
import android.microanswer.healthy.bean.User;
import android.microanswer.healthy.tools.InternetServiceTool;
import android.microanswer.healthy.tools.JavaBeanTools;
import android.microanswer.healthy.view.HtmlView;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 由 Micro 创建于 2016/8/16.
 */

public class FoodActivity extends BaseActivity implements View.OnClickListener {

    private FoodListItem foodListItem;
    private ImageView food_img;
    private TextView food_title;
    private TextView food_desc;
    private TextView like$dislike;
    private HtmlView food_summery;
    private HtmlView food_message;
    private View loadview;

    private static SparseArray<FoodListItem> cachedata;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        suitToolBar(R.id.activity_food_toolbar);
        setToolBarBackEnable();

        if (cachedata == null) {
            cachedata = new SparseArray<>();
        }


        food_title = (TextView) findViewById(R.id.activity_food_title);
        food_img = (ImageView) findViewById(R.id.activity_food_img);
        food_desc = (TextView) findViewById(R.id.activity_food_desc);
        food_message = (HtmlView) findViewById(R.id.activity_food_message);
        like$dislike = (TextView) findViewById(R.id.activity_food_like$dislike);
        like$dislike.setOnClickListener(this);
        like$dislike.setEnabled(false);
        food_summery = (HtmlView) findViewById(R.id.activity_food_summery);
        food_message = (HtmlView) findViewById(R.id.activity_food_message);
        loadview = findViewById(R.id.activity_food_loadview);


        Serializable data = getIntent().getSerializableExtra("data");

        if (data instanceof FoodListItem) {
            foodListItem = (FoodListItem) data;
            ImageLoader.getInstance().displayImage(Healthy.IMAGE_URL + foodListItem.getImg(), food_img, new DisplayImageOptions.Builder()
                    .displayer(new BitmapDisplayer() {
                        @Override
                        public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                            Bitmap b = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                            Canvas c = new Canvas(b);
                            c.drawColor(Color.LTGRAY);


                            c.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(4, 4, b.getWidth() - 4, b.getHeight() - 4), null);
                            imageAware.setImageBitmap(b);
                        }
                    }).build());
            food_title.setText(foodListItem.getName());
            food_desc.setText(foodListItem.getDescription());
            food_summery.setHtml(foodListItem.getSummary());
        } else if (data instanceof Collected) {
            Collected d = (Collected) data;
            foodListItem = new FoodListItem();
            foodListItem.setId(d.getOid());
            dialog = ProgressDialog.show(this, null, "加载中...");
        }


        if (cachedata.get(foodListItem.getId()) != null) {
            foodListItem = cachedata.get(foodListItem.getId());
            loadview.setVisibility(View.GONE);
            onFoodListItemDataOk();
            if (dialog != null) {
                dialog.dismiss();
            }
            onFoodListItemDataOk();
        } else {
            requestFoodListItem();
        }
        requestIsLike();
    }


    private void onFoodListItemDataOk() {
        ImageLoader.getInstance().displayImage(Healthy.IMAGE_URL + foodListItem.getImg(), food_img);
        food_title.setText(foodListItem.getName());
        food_desc.setText(foodListItem.getDescription());
        food_summery.setHtml(foodListItem.getSummary());
        food_message.setHtml(foodListItem.getMessage());
    }


    private void disLike() {
        runOnOtherThread(new BaseOtherThread() {
            @Override
            public void onOtherThreadRunEnd(Message msg) {
                like$dislike.setEnabled(true);
                if (msg.arg1 == LIKE) {
                    like$dislike.setTag(LIKE);
                    like$dislike.setText("取消收藏");
                } else if (msg.arg1 == UNLIKE) {
                    like$dislike.setText("收藏");
                    like$dislike.setTag(UNLIKE);
                    if (msg.obj != null) {
                        toast(msg.obj + "", POSOTION_BOTTOM);
                    }
                }

            }

            @Override
            public Map getTaskParams() {
                like$dislike.setEnabled(false);
                HashMap<String, Object> params = new HashMap<>();
                params.put("id", foodListItem.getId());
                params.put("type", "food");
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
                like$dislike.setEnabled(true);
                if (msg.arg1 == LIKE) {
                    like$dislike.setTag(LIKE);
                    like$dislike.setText("取消收藏");
                } else if (msg.arg1 == UNLIKE) {
                    like$dislike.setText("收藏");
                    like$dislike.setTag(UNLIKE);
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
                    parp.put("id", foodListItem.getId());
                    parp.put("type", "food");
                    parp.put("title", URLEncoder.encode(foodListItem.getName(), "UTF-8"));
                    parp.put("access_token", User.getUser().getAccess_token());
                    parp.put("keyword", isTextEmpty(keyWord) ? foodListItem.getName() : keyWord);
                    like$dislike.setEnabled(false);
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
                like$dislike.setTag(msg.arg1);
                like$dislike.setEnabled(true);
                if (msg.arg1 == 0) {
                    like$dislike.setText("收藏");
                    if (msg.obj != null) {
                        toast(msg.obj + "", POSOTION_TOP);
                    }
                } else {
                    like$dislike.setText("取消收藏");
                }
            }

            @Override
            public Map getTaskParams() {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("id", foodListItem.getId());
                params.put("access_token", User.getUser().getAccess_token());
                return params;
            }

            @Override
            public Message run(Map params) {
                String url = "http://www.tngou.net/api/favorite?id=" + params.get("id") + "&type=food&access_token=" + params.get("access_token");
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
        }, 29386);
    }

    /**
     * 请求健康食材详细数据
     */
    private void requestFoodListItem() {
        runOnOtherThread(new BaseOtherThread() {
            @Override
            void onOtherThreadRunEnd(Message msg) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                loadview.setVisibility(View.GONE);
                if (msg.obj != null) {
                    foodListItem = (FoodListItem) msg.obj;
                    cachedata.put(foodListItem.getId(), foodListItem);
                    onFoodListItemDataOk();
                } else {
                    errorDialog("加载失败", FoodActivity.this);
                }
            }

            @Override
            public Map getTaskParams() {
                HashMap<String, Integer> object = new HashMap<String, Integer>();
                object.put("id", foodListItem.getId());
                return object;
            }

            @Override
            public Message run(Map params) {
                Message msg = new Message();
                msg.obj = JavaBeanTools.Food.getFoodShow((Integer) params.get("id"));
                return msg;
            }
        }, 39453);


    }

    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return super.onHomeButtonClick();
    }

    public static final int LIKE = 1;
    private static final int UNLIKE = 0;

    @Override
    public void onClick(View view) {
        if (like$dislike.getTag() != null)
            if (((int) like$dislike.getTag()) == LIKE) {//取消收藏
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
}
