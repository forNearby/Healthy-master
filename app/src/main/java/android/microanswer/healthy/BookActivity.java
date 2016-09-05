package android.microanswer.healthy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.TimeUnit;
import android.microanswer.healthy.application.Healthy;
import android.microanswer.healthy.bean.BookListItem;
import android.microanswer.healthy.bean.Collected;
import android.microanswer.healthy.bean.User;
import android.microanswer.healthy.tools.InternetServiceTool;
import android.microanswer.healthy.view.BookPageDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 由 Micro 创建于 2016/8/9.
 */

public class BookActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "BookActivity";

    private BookListItem bookListItem;

    private static SparseArray<BookListItem> cachedata;

    private TextView title;
    private TextView time;
    private TextView count;
    private TextView fcount;
    private TextView rcount;
    private TextView writer;
    private TextView summary;
    private TextView like$dislike;
    private ImageView bookimg;

    private LinearLayout activity_book_mulucontent;

    private ProgressDialog pd;//从收藏界面跳转到费界面会使用该弹出框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        suitToolBar(R.id.activity_book_toolbar);
        setToolBarBackEnable();

        if (cachedata == null) {
            cachedata = new SparseArray<>();
        }

        bookimg = (ImageView) findViewById(R.id.activity_book_img);
        time = (TextView) findViewById(R.id.activity_book_time);
        count = (TextView) findViewById(R.id.activity_book_count);
        fcount = (TextView) findViewById(R.id.activity_book_fcount);
        rcount = (TextView) findViewById(R.id.activity_book_rcount);
        writer = (TextView) findViewById(R.id.activity_book_writer);
        summary = (TextView) findViewById(R.id.activity_book_summary);
        like$dislike = (TextView) findViewById(R.id.activity_book_like$dislike);
        title = (TextView) findViewById(R.id.activity_book_title);
        activity_book_mulucontent = (LinearLayout) findViewById(R.id.activity_book_mulucontent);
        like$dislike.setEnabled(false);
        like$dislike.setOnClickListener(this);
//        bookListItem = (BookListItem) getIntent().getSerializableExtra("data");

        Serializable data = getIntent().getSerializableExtra("data");
        if (data instanceof BookListItem) {
            this.bookListItem = (BookListItem) data;
            title.setText(bookListItem.getName());
            time.setText("发布时间：" + DateUtils.formatDateTime(this, bookListItem.getTime(), -1));
            count.setText("访问量：" + bookListItem.getCount());
            fcount.setText("阅读量：" + bookListItem.getFcount());
            rcount.setText("评论量：" + bookListItem.getRcount());
            writer.setText("作者：" + bookListItem.getAuthor());
            summary.setText(Html.fromHtml(bookListItem.getSummary()));
            ImageLoader.getInstance().displayImage(Healthy.IMAGE_URL + bookListItem.getImg(), bookimg);
            requestLike();
            if (cachedata.get(bookListItem.getId()) == null) {
                requestBook();
            } else {
                bookListItem = cachedata.get(bookListItem.getId());
                onBookGeted();
            }
        } else if (data instanceof Collected) {
            Collected c = (Collected) data;
            this.bookListItem = new BookListItem();
            this.bookListItem.setId(c.getOid());
            requestLike();
            pd = new ProgressDialog(this);
            pd.setMessage("加载中...");
            pd.setCancelable(false);
            pd.show();
            if (cachedata.get(bookListItem.getId()) == null) {
                requestBook();
            } else {
                bookListItem = cachedata.get(bookListItem.getId());
                onBookGeted();
            }
//            requestBook();
        }
    }


    private void onBookGeted() {

        cachedata.put(bookListItem.getId(), bookListItem);//加入缓存豪华套餐

        if (pd != null) {
            pd.dismiss();
        }



        title.setText(bookListItem.getName());
        time.setText("发布时间：" + DateUtils.formatDateTime(this, bookListItem.getTime(), -1));
        count.setText("访问量：" + bookListItem.getCount());
        fcount.setText("阅读量：" + bookListItem.getFcount());
        rcount.setText("评论量：" + bookListItem.getRcount());
        writer.setText("作者：" + bookListItem.getAuthor());
        summary.setText(Html.fromHtml(bookListItem.getSummary()));
        ImageLoader.getInstance().displayImage(Healthy.IMAGE_URL + bookListItem.getImg(), bookimg);
        activity_book_mulucontent.removeAllViews();
        for (int i = 0; i < bookListItem.getList().size(); i++) {
            BookListItem.BookPage bkg = bookListItem.getList().get(i);
            TextView tv = new TextView(this);
            tv.setTag(i);
            tv.setOnClickListener(this);
            tv.setPadding(title.getPaddingLeft(), title.getPaddingBottom(), title.getPaddingRight(), title.getPaddingBottom());
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setText(bkg.getTitle());
            tv.setClickable(true);
            tv.setBackgroundResource(android.R.drawable.list_selector_background);
            tv.setTextColor(Color.BLACK);
            activity_book_mulucontent.addView(tv);
        }
    }


    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return super.onHomeButtonClick();
    }

    @Override
    protected void onDestroy() {
        bookListItem = null;
        shutDownAllOtherThread();
        super.onDestroy();
    }

    /**
     * 该方法用于请求详细图书数据
     */
    private void requestBook() {
        runOnOtherThread(new BaseOtherThread() {
            @Override
            public void onOtherThreadRunEnd(Message msg) {

                if (msg != null) {
                    bookListItem = (BookListItem) msg.obj;
                    onBookGeted();
                } else {
                    //TODO 加载失败
                    toast("加载失败", POSOTION_BOTTOM);
                }

            }

            @Override
            public Map getTaskParams() {
                HashMap<String, Integer> integerHashMap = new HashMap<String, Integer>();
                integerHashMap.put("id", bookListItem.getId());
                return integerHashMap;
            }

            @Override
            public Message run(Map params) {
                String url = "http://www.tngou.net/api/book/show?id=" + params.get("id");
                try {
                    String request = InternetServiceTool.request(url);
                    JSONObject jsonObject = JSON.parseObject(request);
                    if (jsonObject.getBooleanValue("status")) {
                        BookListItem bookListItem = JSON.parseObject(request, BookListItem.class);

                        Collections.sort(bookListItem.getList());

                        Message msg = new Message();
                        msg.obj = bookListItem;
                        return msg;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, 49302);
    }

    /**
     * 请求是否已经收藏
     */
    private void requestLike() {
        runOnOtherThread(new BaseOtherThread() {
            @Override
           public void onOtherThreadRunEnd(Message msg) {
                like$dislike.setEnabled(true);
                if (msg.arg1 == LIKE) {
                    like$dislike.setText("取消收藏");
                    like$dislike.setTag(LIKE);
                } else if (msg.arg1 == UNLIKE) {
                    like$dislike.setText("收藏");
                    like$dislike.setTag(UNLIKE);
                    if (msg.obj != null) {
                        like$dislike.setEnabled(false);
                        Toast.makeText(BookActivity.this, msg.obj + "", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public Map getTaskParams() {
                HashMap<String, Object> par = new HashMap<String, Object>();
                par.put("id", bookListItem.getId());
                par.put("type", "book");
                return par;
            }

            @Override
            public Message run(Map params) {
                String url = "http://www.tngou.net/api/favorite?access_token=" + User.getUser().getAccess_token() + "&id=" + params.get("id") + "&type=" + params.get("type");

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
                    msg.obj = e.toString();
                }
                return msg;
            }
        }, 22334);
    }

    /**
     * 收藏
     */
    private void like(final String keyWord) {
        runOnOtherThread(new BaseOtherThread() {
            @Override
         public   void onOtherThreadRunEnd(Message msg) {
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
                HashMap<String, Object> parp = new HashMap<>();
                try {
                    parp.put("id", bookListItem.getId());
                    parp.put("type", "book");
                    parp.put("title", URLEncoder.encode(bookListItem.getName(), "UTF-8"));
                    parp.put("access_token", User.getUser().getAccess_token());
                    parp.put("keyword", isTextEmpty(keyWord) ? bookListItem.getName() : keyWord);
                    like$dislike.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return parp;
            }

            @Override
            public Message run(Map params) {
                String url = "http://www.tngou.net/api/favorite/add?id=" + params.get("id") + "&type=" + params.get("type") + "&title=" + params.get("title") + "&access_token=" + params.get("access_token") + "&keyword=" + params.get("keyword");

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
        }, 34573);
    }

    /**
     * 删除收藏
     */
    private void disLike() {
        runOnOtherThread(new BaseOtherThread() {
            @Override
          public  void onOtherThreadRunEnd(Message msg) {
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
                params.put("id", bookListItem.getId());
                params.put("type", "book");
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
        }, 5322);

    }



    @Override
    public void onClick(View view) {
        if (like$dislike == view) {
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
            return;
        }

        if (view instanceof TextView && view.getTag() != null) {
            new BookPageDialog(this, bookListItem, (int) view.getTag()).show();
        }
    }


    private static final int LIKE = 1;
    private static final int UNLIKE = 0;
}
