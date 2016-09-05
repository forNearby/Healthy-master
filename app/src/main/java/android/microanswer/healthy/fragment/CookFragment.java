package android.microanswer.healthy.fragment;

import android.app.ProgressDialog;
import android.microanswer.healthy.BaseActivity;
import android.microanswer.healthy.R;
import android.microanswer.healthy.adapter.CookRecyclerViewAdapter;
import android.microanswer.healthy.bean.CookClassify;
import android.microanswer.healthy.bean.CookListItem;
import android.microanswer.healthy.database.DataManager;
import android.microanswer.healthy.tools.BaseTools;
import android.microanswer.healthy.tools.JavaBeanTools;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 由 Micro 创建于 2016/7/17.
 */

public class CookFragment extends Fragment implements TabLayout.OnTabSelectedListener, CookRecyclerViewAdapter.OnClickListener {
    private static final String TAG = "CookFragment";

    private static final int WHAT_LOAD_MORE = 2;//加载更多内容
    private static final int WHAT_LOAD_MORE_OK = 3;//更多内容加载完成
    private static final int WHAT_LOAD_COOKCLASSIFY = 5;//加载菜谱分类
    private static final int WHAT_LOAD_COOKCLASSIFY_OK = 6;//菜谱分类加载完成
    private static final int WHAT_LOAD_COOKCLASSIFY_FAILL = 7;//菜谱分类加载是失败
    private static final int WHAT_LOAD_CLASSIFYDATA = 8;//加载分类下对应的数据列表
    private static final int WHAT_LOAD_CLASSIFYDATA_OK = 9;
    private static final int WHAT_LOAD_CLASSIFYDATA_FAILL = 10;

    public static final int PAGE_COUNT = 30;//每页显示的数量

    private ProgressDialog dialog;//加载弹窗


    private View rootview;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private DataManager dataManager;
    private CookRecyclerViewAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    private boolean isLoadingMore = false;//标记是否处于加载更多的状态中

    private List<CookClassify> cookClassifies;//菜谱分类列表

    private Handler childHandler;//子线程HAndler
    private Handler mainHandler = new Handler() {//主线程Handler
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_LOAD_COOKCLASSIFY_OK://菜谱分类加载成功
                    List<CookClassify> data = (List<CookClassify>) msg.obj;
                    if (data != null) {
                        for (CookClassify cookclassify : data) {
                            tabLayout.addTab(tabLayout.newTab().setText(cookclassify.getTitle()));
                        }
                    }
                    if (dialog != null)
                        dialog.dismiss();
                    break;
                case WHAT_LOAD_COOKCLASSIFY_FAILL:
                    if (dialog != null)
                        dialog.dismiss();
                    BaseActivity.errorDialog("加载未成功,请稍后重试!", getActivity());
                    break;
                case WHAT_LOAD_CLASSIFYDATA_FAILL:
                    if (dialog != null)
                        dialog.dismiss();
                    BaseActivity.errorDialog("加载未成功,请稍后重试!", getActivity());
                    break;
                case WHAT_LOAD_CLASSIFYDATA_OK:
                    List<CookListItem> cooklistitems = (List<CookListItem>) msg.obj;
                    int classify = msg.arg1;
                    adapter.setClassifyData(classify, cooklistitems);
                    adapter.setCurrentClassify(classify);
                    if (dialog != null)
                        dialog.dismiss();
                    break;
                case WHAT_LOAD_MORE_OK:
                    List<CookListItem> cooklistitems1 = (List<CookListItem>) msg.obj;
                    int classify2 = msg.arg1;
                    adapter.appendClassifyData(classify2, cooklistitems1);
                    isLoadingMore = false;
                    break;
            }
        }
    };
    private Thread thread = new Thread() {
        @Override
        public void run() {
            Log.i(TAG, "子线程已开启");
            Looper.prepare();
            childHandler = new Handler() {
                @Override
                public void handleMessage(Message gmsg) {
                    switch (gmsg.what) {
                        case WHAT_LOAD_COOKCLASSIFY://加载菜谱分类
                            List<CookClassify> cookClassifyData = JavaBeanTools.Cook.getCookClassifyData();
                            if (cookClassifyData != null) {
                                cookClassifies = cookClassifyData;
                                int i = dataManager.putCookClassifys(cookClassifyData);
                                Log.i(TAG, "从网络获取到的菜谱分类写入了" + i + "条到数据库");
                                Message msg = mainHandler.obtainMessage();
                                msg.what = WHAT_LOAD_COOKCLASSIFY_OK;
                                msg.obj = cookClassifyData;
                                msg.sendToTarget();
                            } else {
                                Message msg = mainHandler.obtainMessage();
                                msg.what = WHAT_LOAD_COOKCLASSIFY_FAILL;
                                msg.sendToTarget();
                            }
                            break;
                        case WHAT_LOAD_CLASSIFYDATA://加载分类下的对应数据列表
                            int classify = gmsg.arg1;
                            List<CookListItem> cookList = JavaBeanTools.Cook.getCookList(PAGE_COUNT, 1, classify);
                            if (cookList != null) {
                                int i = dataManager.putCookListItems(cookList);
                                Log.i(TAG, "从网诺获取到的分类为:" + classify + "的菜谱列表数据写入到数据库" + i + "条");
                                Message msg = mainHandler.obtainMessage();
                                msg.what = WHAT_LOAD_CLASSIFYDATA_OK;
                                msg.arg1 = classify;
                                msg.obj = cookList;
                                msg.sendToTarget();
                            } else {
                                mainHandler.sendEmptyMessage(WHAT_LOAD_CLASSIFYDATA_FAILL);
                            }
                            break;
                        case WHAT_LOAD_MORE://加载更多数据
                            int currentClassify = adapter.getCurrentClassify();
                            List<CookListItem> cookList1 = JavaBeanTools.Cook.getCookList(PAGE_COUNT, adapter.getCurrentClassifyPage() + 1, currentClassify);
                            if (cookList1 != null) {
                                int i = dataManager.putCookListItems(cookList1);
                                Log.i(TAG, "追加从网诺获取到的分类为:" + currentClassify + "的菜谱列表数据写入到数据库" + i + "条");
                                Message msg = mainHandler.obtainMessage();
                                msg.what = WHAT_LOAD_MORE_OK;
                                msg.obj = cookList1;
                                msg.arg1 = currentClassify;
                                msg.sendToTarget();
                            } else {
                                isLoadingMore = false;
                            }
                            break;
                    }
                }
            };
            Looper.loop();
        }

    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (childHandler == null) {
            thread.start();
        }
        if (rootview == null) {
            rootview = inflater.inflate(R.layout.fragment_cook, null);
        }
        if (tabLayout == null) {
            tabLayout = (TabLayout) rootview.findViewById(R.id.fragment_cook_tablayout);
        }
        if (dataManager == null) {
            dataManager = new DataManager(getActivity());
        }

        if (recyclerView == null) {
            recyclerView = (RecyclerView) rootview.findViewById(R.id.fragment_cook_recyclerview);
        }

        initRecyclerView();

        if (tabLayout.getTabCount() <= 5) {
            ArrayList<CookClassify> cookClassifys = dataManager.getCookClassifys();
            if (cookClassifys == null || cookClassifys.size() == 0) {
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("加载中...");
                dialog.show();
                childHandler.sendEmptyMessage(WHAT_LOAD_COOKCLASSIFY);//给子线程发一条消息,让子线程加载菜谱分类
            } else {
                this.cookClassifies = cookClassifys;
                for (CookClassify cookclassify : cookClassifys) {
                    tabLayout.addTab(tabLayout.newTab().setText(cookclassify.getTitle()));
                }
                onTabSelected(tabLayout.getTabAt(0));
            }
        }
        recyclerView.addOnScrollListener(new RecyclerviewScroller());
        tabLayout.addOnTabSelectedListener(this);

        return rootview;
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        if (recyclerView.getLayoutManager() == null) {
//            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
//            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            gridLayoutManager = new GridLayoutManager(getActivity(),3);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        if (recyclerView.getAdapter() == null) {
            adapter = new CookRecyclerViewAdapter(getActivity());
            adapter.setOnClickListener(this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int id = cookClassifies.get(tab.getPosition()).getId();
        if (adapter.getClassifyData(id) == null) {

            if (BaseTools.isNetworkAvailable(getActivity())) {//网络良好，从网络加载数据
                Message msg = childHandler.obtainMessage();
                msg.what = WHAT_LOAD_CLASSIFYDATA;
                msg.arg1 = id;
                msg.sendToTarget();
                if (tab.getPosition() == 0) {//tab等于0的时候是软件打开的时候,不要弹出窗口
                    return;
                }
                dialog = new ProgressDialog(getActivity());
//                dialog.setTitle("正在加载[" + cookClassifies.get(tab.getPosition()).getName() + "]的列表数据");
                dialog.setMessage("加载中...");
                dialog.show();
            } else {//网络不好，从数据库加载数据
                ArrayList<CookListItem> cookListItems = dataManager.getCookListItems(PAGE_COUNT, 1, id);
                if (cookListItems != null && cookListItems.size() == PAGE_COUNT) {
                    adapter.setClassifyData(id, cookListItems);
                    adapter.setCurrentClassify(id);
                }
            }
//
//            ArrayList<CookListItem> cookListItems = dataManager.getCookListItems(PAGE_COUNT, 1, id);
//
//            if (cookListItems == null || cookListItems.size() != PAGE_COUNT) {
//                Message msg = childHandler.obtainMessage();
//                msg.what = WHAT_LOAD_CLASSIFYDATA;
//                msg.arg1 = id;
//                msg.sendToTarget();
//                if (tab.getPosition() == 0) {//tab等于0的时候是软件打开的时候,不要弹出窗口
//                    return;
//                }
//                dialog = new ProgressDialog(getActivity());
////                dialog.setTitle("正在加载[" + cookClassifies.get(tab.getPosition()).getName() + "]的列表数据");
//                dialog.setMessage("加载中...");
//                dialog.show();
//            } else {
//                adapter.setClassifyData(id, cookListItems);
//                adapter.setCurrentClassify(id);
//            }
        } else {
            adapter.setCurrentClassify(id);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(Object obj) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(obj);
        }
    }


    private class RecyclerviewScroller extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (isSlideToBottom(recyclerView)) {//滑动到底部自动加载更多
                if (isLoadingMore) {
                    return;
                }
                isLoadingMore = true;

                if (BaseTools.isNetworkAvailable(getActivity())) {//网络良好，从网络加载数据
                    childHandler.sendEmptyMessage(WHAT_LOAD_MORE);//通知子线程加载更多
                } else {
                    ArrayList<CookListItem> cookListItems = dataManager.getCookListItems(PAGE_COUNT, adapter.getCurrentClassifyPage() + 1, adapter.getCurrentClassify());
                    if (cookListItems != null && cookListItems.size() == PAGE_COUNT) {
                        adapter.appendClassifyData(adapter.getCurrentClassify(), cookListItems);
                    }
                }


//                ArrayList<CookListItem> cookListItems = dataManager.getCookListItems(PAGE_COUNT, adapter.getCurrentClassifyPage() + 1, adapter.getCurrentClassify());
//                if (cookListItems == null || cookListItems.size() != PAGE_COUNT) {
//                    childHandler.sendEmptyMessage(WHAT_LOAD_MORE);//通知子线程加载更多
//                } else {
//                    adapter.appendClassifyData(adapter.getCurrentClassify(), cookListItems);
//                }
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        childHandler.getLooper().quit();
    }

    private boolean isSlideToBottom(RecyclerView recyclerView) {
        return recyclerView != null && recyclerView.getAdapter() != null && getActivity() != null && recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= (recyclerView.computeVerticalScrollRange() - BaseTools.Dp2Px(getActivity(), 100));
    }

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(Object obj);
    }
}
