package android.microanswer.healthy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.microanswer.healthy.adapter.MoreLoreListViewAdapter;
import android.microanswer.healthy.bean.LoreClassifyItem;
import android.microanswer.healthy.bean.LoreListItem;
import android.microanswer.healthy.database.DataManager;
import android.microanswer.healthy.tools.BaseTools;
import android.microanswer.healthy.tools.JavaBeanTools;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更多健康知识界面<br/>
 * 由 Micro 创建于 2016/8/16.
 */

public class MoreLoreActivity extends BaseActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private ListView listView;
    private int page = 1;
    private int classifyId = -1;
    private MoreLoreListViewAdapter adapter;

    private static List<LoreClassifyItem> menudata;

    private DataManager dataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morelore);
        suitToolBar(R.id.activity_morelore_toolbar);
        setToolBarBackEnable();
        dataManager = new DataManager(this);
        listView = (ListView) findViewById(R.id.activity_morelore_listview);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        listView.addFooterView(View.inflate(this, R.layout.listview_foot, null));
        adapter = new MoreLoreListViewAdapter();
        listView.setAdapter(adapter);
        requestMenu(false);
    }


    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "选择分类").setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            return true;
        }


        if (item.getItemId() == 1) {
            requestMenu(true);
            return true;
        }
        return false;
    }

    private void requestMenu(final boolean showmenu) {
        if (menudata != null && menudata.size() > 0) {//已有数据，直接弹窗
            if (showmenu) {
                showmenu();
                return;
            }
            classifyId = menudata.get(0).getId();
            requestData(1, classifyId);
            return;
        }

        menudata = dataManager.getLoreClassifyItems();
        if (menudata != null && menudata.size() > 0) {//冲数据库拿数据
            if (showmenu) {
                showmenu();
                return;
            }
            classifyId = menudata.get(0).getId();
            requestData(1, classifyId);
            return;
        }


        runOnOtherThread(new BaseOtherThread() {
            private ProgressDialog dialog;

            @Override
            void onOtherThreadRunEnd(Message msg) {

                if (dialog != null) {
                    dialog.dismiss();
                }

                if (msg.obj != null) {
                    menudata = (List<LoreClassifyItem>) msg.obj;
                    if (menudata.size() > 0) {
                        dataManager.putLoreClassifyItems(menudata);
                        if (showmenu) {
                            showmenu();
                            return;
                        }
                        classifyId = menudata.get(0).getId();
                        requestData(1, classifyId);
                    } else {
                        errorDialog("分类数据加载失败", MoreLoreActivity.this);
                    }
                } else {
                    errorDialog("分类数据加载失败", MoreLoreActivity.this);
                }

            }

            @Override
            public Map getTaskParams() {
                dialog = ProgressDialog.show(MoreLoreActivity.this, null, "加载中...");
                return null;
            }

            @Override
            public Message run(Map params) {
                Message msg = new Message();
                msg.obj = JavaBeanTools.Lore.getLoreClassifyData();
                return msg;
            }
        }, 23521);
    }


    /**
     * 显示弹窗用户选择分类
     */
    private void showmenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return menudata.size();
            }

            @Override
            public Object getItem(int i) {
                return menudata.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView tv = null;
                if (view == null) {
                    tv = new TextView(MoreLoreActivity.this);
                    int plr = BaseTools.Dp2Px(MoreLoreActivity.this, 20f);
                    int ptb = BaseTools.Dp2Px(MoreLoreActivity.this, 10f);
                    tv.setPadding(plr, ptb, plr, ptb);
                    view = tv;
                } else {
                    tv = (TextView) view;
                }
                tv.setText(menudata.get(i).getTitle());
                return view;
            }
        }, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (menudata.get(i).getId() == classifyId) {
                    return;//点击的条目正是当前显示的条目
                } else {
                    classifyId = menudata.get(i).getId();
                    adapter.clear();
                    requestData(1, classifyId);
                }
            }
        });
        builder.show();

    }

    private boolean isloadding = false;

    private void requestData(final int pagea, final int classifyIDd) {
        if (isloadding) return;
        isloadding = true;
        runOnOtherThread(new BaseOtherThread() {
            private ProgressDialog dialog;

            @Override
            void onOtherThreadRunEnd(Message msg) {
                if (msg.obj != null) {
                    adapter.addDatas((List<LoreListItem>) msg.obj);
                } else {
                    //加载失败
                }

                if (dialog != null) {
                    dialog.dismiss();
                }
                isloadding = false;
            }

            @Override
            public Map getTaskParams() {
                HashMap<String, Object> data = new HashMap<String, Object>();
                data.put("page", pagea);
                data.put("classifyId", classifyIDd);
                dialog = ProgressDialog.show(MoreLoreActivity.this, null, "加载中...");
                return data;
            }

            @Override
            public Message run(Map params) {
                Message msg = new Message();
                msg.obj = JavaBeanTools.Lore.getLoreListData((Integer) params.get("page"), 30, (Integer) params.get("classifyId"));
                if (msg.obj != null)
                    dataManager.putLoreListItems((List<LoreListItem>) msg.obj);
                return msg;
            }
        }, 39457);
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        // 当不滚动时
        if (i == SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                //加载更多功能的代码
                requestData(++page, classifyId);
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        LoreListItem item = (LoreListItem) adapter.getItem(i);
        Intent intent = new Intent(this, LoreInfoAskActivity.class);
        intent.putExtra("data", item);
        startActivity(intent, false);
    }
}
