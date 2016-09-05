package android.microanswer.healthy.fragment;

import android.content.SharedPreferences;
import android.microanswer.healthy.R;
import android.microanswer.healthy.adapter.HealListViewAdapter;
import android.microanswer.healthy.adapter.RecyclerViewAdapter;
import android.microanswer.healthy.bean.BookListItem;
import android.microanswer.healthy.bean.InfoListItem;
import android.microanswer.healthy.bean.LoreListItem;
import android.microanswer.healthy.database.DataManager;
import android.microanswer.healthy.tools.BaseTools;
import android.microanswer.healthy.tools.JavaBeanTools;
import android.microanswer.healthy.viewbean.ListHeadView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 由 Micro 创建于 2016/8/8.
 */

public class HealthyFragment2 extends Fragment implements Runnable, PtrHandler, AbsListView.OnScrollListener, ListHeadView.OnItemClickListener, HealListViewAdapter.OnItemClickListener {
    private final String TAG = "HealthyFragment2";

    private View root;
    private ListView listview;
    private HealListViewAdapter adapter;

    private int infoPage = 1;

    private SharedPreferences sharedPreferences;

    private PtrFrameLayout ptrFrameLayout;

    private ListHeadView listHeadView;

    private View listfootview;


    private Handler childThreadHandler;//子线程handler，用于加载数据

    private Handler mainHandler;//主线程handler,

    private DataManager dataManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_healthy2, null);
        }

        if (mainHandler == null) {
            mainHandler = new Handler();
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        inflateview();
        initview();
        if (childThreadHandler == null) {
            new Thread(this).start();
        }
        return root;
    }


    private void inflateview() {
        listHeadView = new ListHeadView(getActivity());
        listHeadView.setOnItemClickListener(this);
        adapter = new HealListViewAdapter(getActivity());
        adapter.setOnItemClickListener(this);
        listfootview = View.inflate(getActivity(), R.layout.listview_foot, null);
        dataManager = new DataManager(getActivity());
    }


    private void initview() {
        listview = (ListView) root.findViewById(R.id.fragment_healthy2_listview);
        listview.setOnScrollListener(this);
        listview.addHeaderView(listHeadView);
        listview.addFooterView(listfootview);
        listview.setAdapter(adapter);
        ptrFrameLayout = (PtrFrameLayout) root.findViewById(R.id.fragment_healthy2);
        PtrClassicDefaultHeader ptrClassicDefaultHeader = new PtrClassicDefaultHeader(getActivity());
        ptrFrameLayout.setHeaderView(ptrClassicDefaultHeader);
        ptrFrameLayout.disableWhenHorizontalMove(true);
        ptrFrameLayout.addPtrUIHandler(ptrClassicDefaultHeader);
        ptrFrameLayout.setPtrHandler(this);
    }

    @Override
    public void run() {
        Looper.prepare();
        childThreadHandler = new Handler();
        bangBangData();
        Looper.loop();
    }

    /**
     * 获取或刷新数据[该方法只能在子线程运行]
     */
    private void bangBangData() {
        infoPage = 1;
        final List<InfoListItem> infoListData;
        if (BaseTools.isNetworkAvailable(getActivity())) {
            infoListData = JavaBeanTools.Info.getInfoListData(infoPage, 10, Integer.parseInt(sharedPreferences.getString("main_set_info_data", "-1")));
            if (infoListData != null) {
                dataManager.clearInfo();
                dataManager.putInfoListItems(infoListData);
            }
        } else {
            infoListData = dataManager.getInfoListItems(10, infoPage, Integer.parseInt(sharedPreferences.getString("main_set_info_data", "-1")));
        }

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (infoListData != null)
                    listHeadView.setBannerData(infoListData);
            }
        });
        final List<LoreListItem> loreListData;
        if (BaseTools.isNetworkAvailable(getActivity())) {
            loreListData = JavaBeanTools.Lore.getLoreListData(1, 6, Integer.parseInt(sharedPreferences.getString("main_set_lore_data", "-1")));
            if (loreListData != null) {
                dataManager.clearLore();
                dataManager.putLoreListItems(loreListData);
            }
        } else {
            loreListData = dataManager.getLoreListItems(6, 1, Integer.parseInt(sharedPreferences.getString("main_set_lore_data", "-1")));
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (loreListData != null)
                    listHeadView.setLoreListData(loreListData);
            }
        });
        final List<BookListItem> booklistdata;
        if (BaseTools.isNetworkAvailable(getActivity())) {
            booklistdata = JavaBeanTools.Book.getBookList(1, 9, RecyclerViewAdapter.tranceformInt(Integer.parseInt(sharedPreferences.getString("main_set_book_data", "-1")), true));
            if (booklistdata != null) {
                dataManager.clearBooks();
                dataManager.putBookListItems(booklistdata);
            }
        } else {
            booklistdata = dataManager.getBookListItems(9, 1, -1);
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (loreListData != null)
                    listHeadView.setBookListData(booklistdata);
            }
        });

        final List<InfoListItem> infoListData2;

        if (BaseTools.isNetworkAvailable(getActivity())) {
            infoListData2 = JavaBeanTools.Info.getInfoListData(++infoPage, 10, Integer.parseInt(sharedPreferences.getString("main_set_info_data", "-1")));
            if (infoListData2 != null) {
                dataManager.putInfoListItems(infoListData2);
            }
        } else {
            infoListData2 = dataManager.getInfoListItems(10, ++infoPage, Integer.parseInt(sharedPreferences.getString("main_set_info_data", "-1")));
        }
        final List<List<InfoListItem>> data = new ArrayList<>();
        if (infoListData2 != null && infoListData2.size() == 10) {
            for (int i = 0; i < 5; i++) {
                data.add(infoListData2.subList((i * 2), (i * 2) + 2));
            }
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                adapter.setDate(data);
                ptrFrameLayout.refreshComplete();
            }
        });
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (childThreadHandler != null) {
            childThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    bangBangData();
                }
            });
        }
    }

    private boolean isloading = false;//标记是否正在加载更多...

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        // 当不滚动时
        if (scrollState == SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                //加载更多功能的代码

                if (isloading) {
                    return;
                }
                isloading = true;
                childThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final List<InfoListItem> infoListData2;

                        if (BaseTools.isNetworkAvailable(getActivity())) {
                            infoListData2 = JavaBeanTools.Info.getInfoListData(++infoPage, 10, Integer.parseInt(sharedPreferences.getString("main_set_info_data", "-1")));
                            if (infoListData2 != null) {
                                dataManager.putInfoListItems(infoListData2);
                            }
                        } else {
                            infoListData2 = dataManager.getInfoListItems(10, ++infoPage, Integer.parseInt(sharedPreferences.getString("main_set_info_data", "-1")));
                        }
                        final List<List<InfoListItem>> data = new ArrayList<>();
                        if (infoListData2 != null && infoListData2.size() == 10) {
                            for (int i = 0; i < 5; i++) {
                                data.add(infoListData2.subList((i * 2), (i * 2) + 2));
                            }
                        } else {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                                    listview.setSelection(adapter.getCount() - 1);
                                    isloading = false;
                                }
                            });
                        }
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.appendData(data);
                                isloading = false;
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public void onClick(Object obj) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(obj);
        }
    }

    @Override
    public void onMoreLoreClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onMoreLoreClick();
        }
    }

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static interface OnItemClickListener {
        void onClick(Object obj);

        void onMoreLoreClick();
    }
}
