package android.microanswer.healthy;

import android.content.Intent;
import android.microanswer.healthy.adapter.CollectedListAdapter;
import android.microanswer.healthy.bean.Collected;
import android.microanswer.healthy.bean.LoreListItem;
import android.microanswer.healthy.bean.User;
import android.microanswer.healthy.tools.InternetServiceTool;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏
 * 由 Micro 创建于 2016/8/4.
 */

public class CollectedActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private String TAG = "CollectedActivity";

    private ListView listview;
    private View loadview;
    private View emptyview;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    private CircleRefreshLayout circleRefreshLayout;
    private CollectedListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected);
        suitToolBar(R.id.activity_collected_toolbar);
        setToolBarBackEnable();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.view_collected_item_refreshlayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        listview = (ListView) findViewById(R.id.activity_collectedlistview);
        listview.setOnItemClickListener(this);
        loadview = findViewById(R.id.activity_collected_loadview);
        emptyview = findViewById(R.id.activity_collected_emptyview);
        emptyview.setOnClickListener(this);
        listview.setEmptyView(emptyview);
        adapter = new CollectedListAdapter(this);
        listview.setAdapter(adapter);
        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, 1, "").setIcon(R.mipmap.refresh).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == 1) {
            update();
        }

        return super.onOptionsItemSelected(item);
    }

    private void update() {
        runOnOtherThread(new BaseOtherThread() {
            @Override
           public void onOtherThreadRunEnd(Message msg) {
                if (msg != null)
                    adapter.setData((ArrayList<Collected>) msg.obj);
                else {
                    errorDialog("数据加载失败", CollectedActivity.this).show();
                }
                loadview.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                toast("加载已完成", POSOTION_BOTTOM);
            }

            @Override
            public Map getTaskParams() {
                loadview.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(true);
                adapter.clear();
                return null;
            }

            @Override
            public Message run(Map params) {
                String url = "http://www.tngou.net/api/my/favorite?access_token=" + User.getUser().getAccess_token();
                Message msg = new Message();
                try {
                    String result = InternetServiceTool.request(url);
                    JSONObject jsonObject = JSON.parseObject(result);
                    if (jsonObject.getBooleanValue("status")) {
                        JSONArray tngou = jsonObject.getJSONArray("tngou");
                        List<Collected> collecteds = JSON.parseArray(tngou.toJSONString(), Collected.class);
                        msg.obj = collecteds;
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return msg;
            }
        }, 5042);

    }


    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        update();
    }

    @Override
    public void onRefresh() {
        update();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, LoreInfoAskActivity.class);
        Collected c = (Collected) adapter.getItem(i);
        if (c.getOtype().equals("book")) {
            intent.setClass(this, BookActivity.class);
        } else if (c.getOtype().equals("cook")) {
            intent.setClass(this, CookActivity.class);
        } else if (c.getOtype().equalsIgnoreCase("food")) {
            intent.setClass(this, FoodActivity.class);
        }
        intent.putExtra("data", c);
        startActivity(intent);
    }
}
