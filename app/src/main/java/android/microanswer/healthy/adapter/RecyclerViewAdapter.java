package android.microanswer.healthy.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.microanswer.healthy.R;
import android.microanswer.healthy.bean.AskClassifyItem;
import android.microanswer.healthy.bean.BookListItem;
import android.microanswer.healthy.bean.InfoListItem;
import android.microanswer.healthy.bean.LoreListItem;
import android.microanswer.healthy.database.DataManager;
import android.microanswer.healthy.tools.BaseTools;
import android.microanswer.healthy.tools.JavaBeanTools;
import android.microanswer.healthy.viewbean.HealthyItemGroup;
import android.microanswer.healthy.viewbean.HealthyItemItemAsk;
import android.microanswer.healthy.viewbean.HealthyItemItemBooks;
import android.microanswer.healthy.viewbean.HealthyItemItemInfo;
import android.microanswer.healthy.viewbean.HealthyItemItemKnowledge;
import android.microanswer.healthy.viewbean.SmartBannerViewHolder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 由 Micro 创建于 2016/6/30.
 */
@Deprecated
public class RecyclerViewAdapter extends RecyclerView.Adapter implements HealthyItemItemAsk.OnHealItemItemAskClickListener, HealthyItemItemBooks.OnHealthyItemItemBooksClickListener, HealthyItemItemKnowledge.OnHealthyItemKnowledgeClickListener, HealthyItemItemInfo.OnHealthyItemItemInfoClickListener, SmartBannerViewHolder.OnSmartBannerItemClickListener {
    public static final int TYPE_BANNER = 1;
    public static final int TYPE_ITEMGROUP = 2;
    public static final int TYPE_ITEM_KNOWLEDGE = 3;
    public static final int TYPE_ITEM_BOOKS = 4;
    public static final int TYPE_ITEM_ASK = 5;
    public static final int TYPE_ITEM_INFO = 6;


//    public static final int FUNCTION_NORMALLOAD = 1;
//    public static final int FUNCTION_REFRESH = 2;

    private static final int WHAT_ITEMUPDATE = 1;
    private static final int WHAT_REFRESH_END = 2;
    //    private static final int WHAT_START_AUTO_REFRESH = 3;
    private static final int WHAT_ITEM_APPEND = 4;
    private static final int WHAT_ERROR = 5;

    private Context context;

    private DataManager dataManager;

    private OnItemClickListener onItemClickListener;

    private SharedPreferences sharedPreferences;

    private LoadThread loadthread;

    private Runnable onLoadHandlerInited;
    /**
     * 数据格式：<br/>
     * ArrayList<Map<String,Object>><br/>
     * Map<String,Object><br/>
     * ["type":"type-value"]<br/>
     * ["data":"item-data"]<br/>
     */
    private List<Map<String, Object>> data;

    public RecyclerViewAdapter(Context ccontext, Runnable onLoadHandlerInited) {
        this.context = ccontext;
        this.onLoadHandlerInited = onLoadHandlerInited;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        dataManager = new DataManager(context);
        data = generateDisOnlineData();

        if (loadhandler == null) {
            loadthread = new LoadThread();
            loadthread.start();
        }
    }

    /**
     * 数据生成方法，用于没有加载完成数据的时候显示
     *
     * @return
     */
    private ArrayList<Map<String, Object>> generateDisOnlineData() {


        ArrayList<Map<String, Object>> datalist = new ArrayList<>();


        Map<String, Object> banner = new HashMap<>();
        banner.put("type", TYPE_BANNER);
        banner.put("data", null);
        datalist.add(banner);
        //构建Banner

        Map<String, Object> group1 = new HashMap<>();
        group1.put("type", TYPE_ITEMGROUP);
        group1.put("name", "健康知识");
        group1.put("smallname", "Health Knowledge");
        datalist.add(group1);
        //构建健康知识标题

        for (int i = 0; i < 2; i++) {
            Map<String, Object> knowledgedata = new HashMap<>();
            knowledgedata.put("type", TYPE_ITEM_KNOWLEDGE);
            ArrayList<LoreListItem> item_in_item = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                LoreListItem loreListItem = new LoreListItem();
                loreListItem.setDescription("加载中...");
                item_in_item.add(loreListItem);
            }
            knowledgedata.put("data", item_in_item);
            datalist.add(knowledgedata);
        }//构建健康知识数据

        Map<String, Object> group2 = new HashMap<>();
        group2.put("type", TYPE_ITEMGROUP);
        group2.put("name", "健康问答");
        group2.put("smallname", "Health Question & Answer");
        datalist.add(group2);
        //构建健康问答标题

        for (int m = 0; m < 3; m++) {
            Map<String, Object> askdata = new HashMap<>();
            askdata.put("type", TYPE_ITEM_ASK);
            ArrayList<AskClassifyItem> item_in_item = new ArrayList<>();
            for (int l = 0; l < 4; l++) {
                AskClassifyItem askListItem = new AskClassifyItem();
                askListItem.setTitle("加载中..");
                item_in_item.add(askListItem);
            }
            askdata.put("data", item_in_item);
            datalist.add(askdata);
        }

        Map<String, Object> group3 = new HashMap<>();
        group3.put("type", TYPE_ITEMGROUP);
        group3.put("name", "健康图书");
        group3.put("smallname", "Health Books");
        datalist.add(group3);
        //构建健康问答标题

        for (int m = 0; m < 3; m++) {
            Map<String, Object> bookdata = new HashMap<>();
            bookdata.put("type", TYPE_ITEM_BOOKS);
            ArrayList<BookListItem> item_in_item = new ArrayList<>();
            for (int l = 0; l < 3; l++) {
                BookListItem bookListItem = new BookListItem();
                bookListItem.setName("加载中...");
                item_in_item.add(bookListItem);
            }
            bookdata.put("data", item_in_item);
            datalist.add(bookdata);
        }//构建健康问答数据

        Map<String, Object> group4 = new HashMap<>();
        group4.put("type", TYPE_ITEMGROUP);
        group4.put("name", "更多资讯");
        group4.put("smallname", "More Healthy Info");
        datalist.add(group4);
        //构建更多资讯标题


        for (int i = 0; i < 5; i++) {
            Map<String, Object> Infolistdata = new HashMap<>();
            Infolistdata.put("type", TYPE_ITEM_INFO);
            ArrayList<InfoListItem> item_in_item = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                InfoListItem infoListItem = new InfoListItem();
                infoListItem.setTitle("加载中...");
                item_in_item.add(infoListItem);
            }
            Infolistdata.put("data", item_in_item);
            datalist.add(Infolistdata);
        }
        return datalist;
    }

    private int infoPage = 1;
    private boolean isappending = false;

    public void appendHealthyInfo() {
        if (isappending) {//正在添加新内容，不要再添加
            return;
        }

        if (loadhandler == null) {
            loadthread = new LoadThread();
            loadthread.start();
            return;
        }
        if (BaseTools.isNetworkAvailable(context)) {
            loadhandler.post(new Runnable() {
            @Override
            public void run() {
                isappending = true;
                int classifyId = Integer.parseInt(sharedPreferences.getString("main_set_info_data", "-1"));
                List<InfoListItem> infoListData = JavaBeanTools.Info.getInfoListData(infoPage++, 10, classifyId);
                if (infoListData != null) {
                    dataManager.putInfoListItems(infoListData);
                    for (int i = 0; i < 5; i++) {
                        Map<String, Object> infoitemdata = new HashMap<>();
                        infoitemdata.put("type", TYPE_ITEM_INFO);
                        infoitemdata.put("data", infoListData.subList((i * 2), (i * 2) + 2));
                        Message msg = handler.obtainMessage();
                        msg.what = WHAT_ITEM_APPEND;
                        msg.obj = infoitemdata;
                        msg.sendToTarget();
                    }//构建更多资讯数据
                } else {
                    Message msge = Message.obtain(handler);
                    msge.what = WHAT_ERROR;
                    msge.obj = "你的网络好像不怎么好！";
                    msge.sendToTarget();
                }
                isappending = false;
            }
            });
        } else {
            isappending = true;

            List<InfoListItem> infoListItems1 = dataManager.getInfoListItems(10, infoPage++, -1);
            if (infoListItems1 != null && infoListItems1.size() == 10) {
                for (int i = 0; i < 5; i++) {
                    Map<String, Object> d5 = new HashMap<>();
                    d5.put("type", TYPE_ITEM_INFO);
                    d5.put("data", infoListItems1.subList((i * 2), ((i * 2) + 2)));
                    appendData(d5);
                }
                //从数据库加载更多健康咨询
            }

            isappending = false;
        }
    }

    /**
     * 如果trance为true，该方法才有作用，否则原值返回
     *
     * @param in
     * @param trance
     * @return
     */
    public static int tranceformInt(int in, boolean trance) {

        if (!trance) {
            return in;
        }

        if (in == -1) {
            return (int) Math.floor(Math.random() * 7) + 1;
        }
        return in;
    }

    /**
     * 生成在线数据，从网络获取数据
     */
    public void generateOnlineData() {
        if (loadhandler == null) {
            loadthread = new LoadThread();
            loadthread.start();
            return;
        }

        if (data.size() > 20) {
            data = (List<Map<String, Object>>) data.subList(0, 18);
            notifyDataSetChanged();
        }
        loadhandler.post(new Runnable() {
            @Override
            public void run() {
                infoPage = 1;
                try {

                    Log.i("加载网络数据", "开始加载banner");

                    Map<String, Object> banner = new HashMap<>();
                    banner.put("type", TYPE_BANNER);
                    int classifyId = Integer.parseInt(sharedPreferences.getString("main_set_info_data", "-1"));
                    classifyId = tranceformInt(classifyId, false);
                    List<InfoListItem> infoListData1 = JavaBeanTools.Info.getInfoListData(infoPage++, 10, classifyId);
                    if (infoListData1 != null) {
                        //先删除原有的数据，重新写入新数据
                        dataManager.clearInfo();
                        dataManager.putInfoListItems(infoListData1);
                        banner.put("data", infoListData1);
                        Message msg0 = handler.obtainMessage();
                        msg0.what = WHAT_ITEMUPDATE;
                        msg0.obj = banner;
                        msg0.arg1 = 0;
                        msg0.sendToTarget();
                    }
                    //刷新banner数据


                    Log.i("加载网络数据", "开始加载健康知识");
                    int loreClassID = tranceformInt(Integer.parseInt(sharedPreferences.getString("main_set_lore_data", "-1")), false);
                    List<LoreListItem> loreListData1 = JavaBeanTools.Lore.getLoreListData(1, 4, loreClassID);
                    if (loreListData1 != null) {
                        dataManager.clearLore();
                        dataManager.putLoreListItems(loreListData1);
                        for (int i = 0; i < 2; i++) {
                            HashMap<String, Object> items = new HashMap<String, Object>();
                            items.put("type", TYPE_ITEM_KNOWLEDGE);
                            items.put("data", loreListData1.subList((i * 2), (i * 2) + 2));
                            Message msg = handler.obtainMessage();
                            msg.what = WHAT_ITEMUPDATE;
                            msg.obj = items;
                            msg.arg1 = (i + 2);
                            handler.sendMessage(msg);
                        }//请求健康知识数据
                    }
                    Log.i("加载网络数据", "开始加载健康问答");
                    List<AskClassifyItem> askClassifyData = JavaBeanTools.Ask.getAskClassifyData();

                    if (askClassifyData != null) {
                        dataManager.putAskClassifyItems(askClassifyData);
                        for (int i = 0; i < 3; i++) {
                            Map<String, Object> askdata = new HashMap<>();
                            askdata.put("type", TYPE_ITEM_ASK);
                            List<AskClassifyItem> item_in_item = askClassifyData.subList((i * 4), (i * 4) + 4);
                            askdata.put("data", item_in_item);
                            Message msg = handler.obtainMessage();
                            msg.what = WHAT_ITEMUPDATE;
                            msg.arg1 = (i + 5);
                            msg.obj = askdata;
                            handler.sendMessage(msg);
                        }
                    }//请求健康问答数据


                    Log.i("加载网络数据", "开始加载健康图书");
                    int bookclassify = tranceformInt(Integer.parseInt(sharedPreferences.getString("main_set_book_data", "-1")), true);
                    List<BookListItem> itembookData = JavaBeanTools.Book.getBookList(1, 9, bookclassify);
                    if (itembookData != null) {
                        dataManager.clearBooks();
                        dataManager.putBookListItems(itembookData);
                        for (int m = 0; m < 3; m++) {
                            Map<String, Object> bookdata = new HashMap<>();
                            bookdata.put("type", TYPE_ITEM_BOOKS);
                            List<BookListItem> item_in_item = itembookData.subList((m * 3), (m * 3) + 3);
                            bookdata.put("data", item_in_item);
                            Message msg = handler.obtainMessage();
                            msg.what = WHAT_ITEMUPDATE;
                            msg.arg1 = (m + 9);
                            msg.obj = bookdata;
                            handler.sendMessage(msg);
                        }
                    }

                    Log.i("加载网络数据", "加载更多资讯");
                    List<InfoListItem> infoListData = JavaBeanTools.Info.getInfoListData(infoPage++, 10, classifyId);
                    if (infoListData != null) {
                        dataManager.putInfoListItems(infoListData);
                        for (int i = 0; i < 5; i++) {
                            Map<String, Object> infoitemdata = new HashMap<>();
                            infoitemdata.put("type", TYPE_ITEM_INFO);
                            infoitemdata.put("data", infoListData.subList((i * 2), (i * 2) + 2));
                            Message msg = handler.obtainMessage();
                            msg.what = WHAT_ITEMUPDATE;
                            msg.arg1 = (i + 13);
                            msg.obj = infoitemdata;
                            msg.sendToTarget();
                        }//构建更多资讯数据
                        infoPage = 3;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msge = handler.obtainMessage();
                    msge.what = WHAT_ERROR;
                    msge.obj = "你的网络好像不怎么好！";
                    msge.sendToTarget();
                } finally {
                    Log.i("加载网络数据", "网络数据加载完成");
                    handler.sendEmptyMessage(WHAT_REFRESH_END);
                }
            }
        });
    }

    /**
     * 从数据库获取数据
     */
    public void generateDatabaseData() {
        infoPage = 1;

        ArrayList<InfoListItem> infoListItems = dataManager.getInfoListItems(10, infoPage++, -1);
        if (infoListItems != null) {
            Map<String, Object> d1 = new HashMap<>();
            d1.put("type", TYPE_BANNER);
            d1.put("data", infoListItems);
            updateData(0, d1);
            //从数据库加载健康Banner的咨询数据
        }

        ArrayList<LoreListItem> loreListItems = dataManager.getLoreListItems(4, 1, -1);
        if (loreListItems != null && loreListItems.size() == 4) {
            for (int i = 0; i < 2; i++) {
                Map<String, Object> d2 = new HashMap<>();
                d2.put("type", TYPE_ITEM_KNOWLEDGE);
                d2.put("data", loreListItems.subList(i * 2, ((i * 2) + 2)));
                updateData(2 + i, d2);
            }
            //从数据库加载健康知识数据
        }

        ArrayList<AskClassifyItem> askClassifyItems = dataManager.getAskClassifyItems();
        if (askClassifyItems != null && askClassifyItems.size() >= 12) {
            for (int i = 0; i < 3; i++) {
                Map<String, Object> d3 = new HashMap<>();
                d3.put("type", TYPE_ITEM_ASK);
                d3.put("data", askClassifyItems.subList(i * 4, ((i * 4) + 4)));
                updateData(i + 5, d3);
            }
            //从数据库加载健康问答分类
        }


        ArrayList<BookListItem> bookListItems = dataManager.getBookListItems(9, 1, -1);
        if (bookListItems != null && bookListItems.size() == 9) {
            for (int i = 0; i < 3; i++) {
                Map<String, Object> d4 = new HashMap<>();
                d4.put("type", TYPE_ITEM_BOOKS);
                d4.put("data", bookListItems.subList(i * 3, ((i * 3) + 3)));
                updateData(9 + i, d4);
            }
            //从数据库加载健康图书数据
        }


        List<InfoListItem> infoListItems1 = dataManager.getInfoListItems(10, infoPage++, -1);
        if (infoListItems1 != null && infoListItems1.size() == 10) {
            for (int i = 0; i < 5; i++) {
                Map<String, Object> d5 = new HashMap<>();
                d5.put("type", TYPE_ITEM_INFO);
                d5.put("data", infoListItems1.subList((i * 2), ((i * 2) + 2)));
                updateData(i + 13, d5);
            }
            //从数据库加载更多健康咨询
        }
    }

    public void updateData(int index, Map<String, Object> dataitem) {
        Map<String, Object> stringObjectMap = this.data.get(index);
        Set<String> strings = stringObjectMap.keySet();
        for (String key : strings) {
            stringObjectMap.put(key, dataitem.get(key));
        }
//        this.data.set(index, dataitem);
        this.notifyItemChanged(index);
    }

    private SmartBannerViewHolder smartBannerViewHolder;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.i("RecyclerViewAdapter", "onCreateViewHolder,parent=" + parent.getClass().getSimpleName() + ",viewType=" + viewType);
        if (viewType == TYPE_BANNER) {
            if (smartBannerViewHolder == null) {
                View bannerview = View.inflate(context, R.layout.healthy_banner, null);
//            return new BannerViewHolder(bannerview, context, parentFragment);
                smartBannerViewHolder = new SmartBannerViewHolder(bannerview);
            }
            return smartBannerViewHolder;
        } else if (viewType == TYPE_ITEMGROUP) {
            View itemGroupView = View.inflate(context, R.layout.healthy_itemgroup, null);
            return new HealthyItemGroup(itemGroupView);
        } else if (viewType == TYPE_ITEM_ASK) {
            View itemitem = View.inflate(context, R.layout.healthy_itemitem_ask, null);
            HealthyItemItemAsk healthyItemItem = new HealthyItemItemAsk(itemitem);
            return healthyItemItem;
        } else if (viewType == TYPE_ITEM_BOOKS) {
            return new HealthyItemItemBooks(View.inflate(context, R.layout.healthy_itemitem_health_books, null));
        } else if (viewType == TYPE_ITEM_KNOWLEDGE) {
            return new HealthyItemItemKnowledge(View.inflate(context, R.layout.healthy_itemitem_health_knowledge, null));
        } else if (viewType == TYPE_ITEM_INFO) {
            return new HealthyItemItemInfo(View.inflate(context, R.layout.healthy_itemitem_health_knowledge, null), context);
        }
        return null;
    }

    public Map<String, Object> getItemObject(int index) {
        return data.get(index);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Log.i("RecyclerViewAdapter", "Bind数据：" + holder.getClass().getSimpleName() + ",position=" + position);
        Map<String, Object> itemdata = data.get(position);
        int type = (int) itemdata.get("type");
        if (type == TYPE_BANNER) {
            SmartBannerViewHolder bannerViewHolder = (SmartBannerViewHolder) holder;
            bannerViewHolder.setOnSmartBannerItemClickListener(this);
            List<InfoListItem> data = (List<InfoListItem>) itemdata.get("data");
            bannerViewHolder.setData(data);
        } else {
            if (type == TYPE_ITEMGROUP) {
                HealthyItemGroup healthyItemGroup = (HealthyItemGroup) holder;
                String name = itemdata.get("name").toString();
                String smallname = itemdata.get("smallname").toString();
                healthyItemGroup.setName(name);
                healthyItemGroup.setSmallName(smallname);

            } else if (type == TYPE_ITEM_ASK) {
                HealthyItemItemAsk healthyItemItem = (HealthyItemItemAsk) holder;
                healthyItemItem.setOnHealItemItemAskClickListener(this);
                List<AskClassifyItem> item = (List<AskClassifyItem>) itemdata.get("data");
                if (item != null)
                    healthyItemItem.setData(item);

            } else if (type == TYPE_ITEM_BOOKS) {
                HealthyItemItemBooks healthyItemItem = (HealthyItemItemBooks) holder;
                healthyItemItem.setOnHealthyItemItemBooksClickListener(this);
                List<BookListItem> item = (List<BookListItem>) itemdata.get("data");
                if (item != null)
                    healthyItemItem.setData(item);

            } else if (type == TYPE_ITEM_KNOWLEDGE) {
                HealthyItemItemKnowledge healthyItemItem = (HealthyItemItemKnowledge) holder;
                healthyItemItem.setOnHealthyItemKnowledgeClickListener(this);
                List<LoreListItem> item = (List<LoreListItem>) itemdata.get("data");
                if (item != null)
                    healthyItemItem.setData(item);

            } else if (type == TYPE_ITEM_INFO) {
                HealthyItemItemInfo healthyItemItemInfo = (HealthyItemItemInfo) holder;
                healthyItemItemInfo.setOnHealthyItemItemInfoClickListener(this);
                List<InfoListItem> item = (List<InfoListItem>) itemdata.get("data");
                if (item != null)
                    healthyItemItemInfo.setData(item);
            }
        }
    }

    /**
     * 追加数据
     *
     * @param itemdata
     */
    private void appendData(Map<String, Object> itemdata) {
        this.data.add(itemdata);
        notifyItemInserted(this.getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
//        Log.i("RecyclerViewAdapter", "getItemViewType,position=" + position);
        Map<String, Object> itemdata = data.get(position);
        return (int) itemdata.get("type");
    }

    @Override
    public int getItemCount() {
//        Log.i("RecyclerViewAdapter", "getItemCount=" + data.size());
        return data.size();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT_ITEMUPDATE) {
                updateData(msg.arg1, (Map<String, Object>) msg.obj);
            } else if (msg.what == WHAT_REFRESH_END) {
                if (refreshListener != null) {
                    refreshListener.onRefreshEnd();
                }
            } else if (msg.what == WHAT_ITEM_APPEND) {
                appendData((Map<String, Object>) msg.obj);
            } else if (msg.what == WHAT_ERROR) {
                generateDatabaseData();//网络不好得时候加载数据库内容
                Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    };

    Handler loadhandler = null;

    /**
     * 数据加载线程，每次加载数据的时候在该线程完成
     */
    class LoadThread extends Thread {
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            loadhandler = new Handler();
            handler.post(onLoadHandlerInited);
            Looper.loop();
        }
    }

    public RefreshListener getRefreshListener() {
        return refreshListener;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    private RefreshListener refreshListener;

    /**
     * 健康问答点击
     *
     * @param askClassifyItem
     */
    @Override
    public void onClick(AskClassifyItem askClassifyItem) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(askClassifyItem);
        }
    }

    /**
     * 健康图书点击
     *
     * @param item
     */
    @Override
    public void onClick(BookListItem item) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(item);
        }
    }

    /**
     * 健康知识点击
     *
     * @param item
     */
    @Override
    public void onclick(LoreListItem item) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(item);
        }
    }

    /**
     * 健康咨询点击
     *
     * @param item
     */
    @Override
    public void onclick(InfoListItem item) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(item);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onItemClick(InfoListItem item) {
        this.onclick(item);
    }

    public interface RefreshListener {
        void onRefreshEnd();
    }

    public interface OnItemClickListener {
        void onClick(Object item);
    }
}
