package android.microanswer.healthy.tools;

import android.microanswer.healthy.LoginActivity;
import android.microanswer.healthy.bean.AskClassifyItem;
import android.microanswer.healthy.bean.AskListItem;
import android.microanswer.healthy.bean.AskNews;
import android.microanswer.healthy.bean.BookClassifyItem;
import android.microanswer.healthy.bean.BookListItem;
import android.microanswer.healthy.bean.CookClassify;
import android.microanswer.healthy.bean.CookListItem;
import android.microanswer.healthy.bean.FoodClassify;
import android.microanswer.healthy.bean.FoodListItem;
import android.microanswer.healthy.bean.Friend;
import android.microanswer.healthy.bean.InfoClassifyItem;
import android.microanswer.healthy.bean.InfoListItem;
import android.microanswer.healthy.bean.LoreClassifyItem;
import android.microanswer.healthy.bean.LoreListItem;
import android.microanswer.healthy.bean.LoreNews;
import android.microanswer.healthy.bean.User;
import android.microanswer.healthy.database.DataManager;
import android.microanswer.healthy.exception.JavaBeanDataLoadException;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据工具类
 * Created by Micro on 2016/6/23.
 */

public class JavaBeanTools {
    private static final String error_description = "数据加载出错";
    private static final String TAG = "JavaBeanTools";

    /**
     * 健康咨询
     */
    public static final class Info {

        /**
         * 获取健康咨询分类
         *
         * @param loadListener
         * @return
         * @throws JavaBeanDataLoadException
         */
        @Deprecated
        public static final ArrayList<InfoClassifyItem> getInfoClassifyData(LoadListener loadListener) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/info/classify";
            try {
                String res = InternetServiceTool.request(url);
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getBoolean("status")) {
                    JSONArray ja = jsonObject.getJSONArray("tngou");
                    int count = ja.length();
                    ArrayList<InfoClassifyItem> data = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        JSONObject j2 = ja.getJSONObject(i);
                        InfoClassifyItem infoClassifyItem = new InfoClassifyItem();
                        infoClassifyItem.setDescription(j2.getString("description"));
                        infoClassifyItem.setId(j2.getInt("id"));
                        infoClassifyItem.setKeywords(j2.getString("keywords"));
                        infoClassifyItem.setName(j2.getString("name"));
                        infoClassifyItem.setSeq(j2.getInt("seq"));
                        infoClassifyItem.setTitle(j2.getString("title"));


                        data.add(infoClassifyItem);
                        if (loadListener != null) {
                            loadListener.onItemLoadOk(infoClassifyItem);
                        }

                    }
                    return data;
                } else {
                    res = jsonObject.getString("msg");
                    throw new Exception(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
        }

        /**
         * 获取健康咨询分类
         * @return
         */
        public static final List<InfoClassifyItem> getInfoClassifyData() {
            String url = "http://www.tngou.net/api/info/classify";
            String request = InternetServiceTool.request(url);
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(url);
            if (jsonObject.getBoolean("status")) {
                com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                return JSON.parseArray(tngou.toJSONString(), InfoClassifyItem.class);
            }
            return null;
        }

        /**
         * 获取咨询列表
         *
         * @param count 请求页数，默认page=1
         * @param page 每页返回的条数，默认rows=20
         * @param id 分类ID，默认返回的是全部。这里的ID就是指分类的ID
         * @return
         * @throws JavaBeanDataLoadException
         */
        @Deprecated
        public static final ArrayList<InfoListItem> getInfoListData(int count, int page, int id, LoadListener loadListener) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/info/list?rows=" + count + "&page=" + page + "&id=" + id;
            try {
                String res = InternetServiceTool.request(url);
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getBoolean("status")) {
                    ArrayList<InfoListItem> listdata = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("tngou");
                    int a = jsonArray.length();
                    for (int i = 0; i < a; i++) {
                        JSONObject j1 = jsonArray.getJSONObject(i);
                        InfoListItem infoListItem = new InfoListItem();
                        infoListItem.setTitle(j1.getString("title"));
                        infoListItem.setKeywords(j1.getString("keywords"));
                        infoListItem.setId(j1.getInt("id"));
                        infoListItem.setCount(j1.getInt("count"));
                        infoListItem.setDescription(j1.getString("description"));
                        infoListItem.setFcount(j1.getInt("fcount"));
                        infoListItem.setImg(j1.getString("img"));
                        infoListItem.setInfoclass(j1.getInt("infoclass"));
//                        try {
//                            infoListItem.setMessage(j1.getString("message"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        infoListItem.setRcount(j1.getInt("rcount"));
                        infoListItem.setTime(j1.getLong("time"));
                        listdata.add(infoListItem);
                        if (loadListener != null) {
                            loadListener.onItemLoadOk(infoListItem);
                        }
                    }
                    return listdata;
                } else {
                    res = jsonObject.getString("msg");
                    throw new Exception(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
        }

        /**
         * @param page 否	int	请求页数，默认page=1
         * @param rows 否	int	每页返回的条数，默认rows=20
         * @param id   否	int	分类ID，默认返回的是全部。这里的ID就是指分类的ID
         * @return
         */
        public static final List<InfoListItem> getInfoListData(int page, int rows, int id) {

            String url = "http://www.tngou.net/api/info/list?page=" + page + "&rows=" + rows + "&id=" + id;

            String result = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getBooleanValue("status")) {
                com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                return JSON.parseArray(tngou.toJSONString(), InfoListItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 获取最新咨询
         *
         * @param count    返回最新关键词的条数，默认rows=20
         * @param classify 分类ID，取得该 分类下的最新数据
         * @param id       #当前最新的热点热词关键词id
         */
        @Deprecated
        public static final ArrayList<InfoListItem> getInfoNews(int count, int classify, int id, LoadListener loadListener) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/info/news?rows="+count+"&classify="+classify+"&id="+id;

            try {

                String res = InternetServiceTool.request(url);
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getBoolean("status")) {
                    ArrayList<InfoListItem> data = new ArrayList<>();
                    JSONArray ja = jsonObject.getJSONArray("tngou");
                    int ll = ja.length();
                    for (int i = 0; i < ll; i++) {
                        JSONObject a = ja.getJSONObject(i);
                        InfoListItem infoListItem = new InfoListItem();
                        infoListItem.setTime(a.getLong("time"));
                        infoListItem.setRcount(a.getInt("rcount"));
                        infoListItem.setMessage(a.getString("message"));
                        infoListItem.setInfoclass(a.getInt("infoclass"));
                        infoListItem.setCount(a.getInt("count"));
                        infoListItem.setDescription(a.getString("description"));
                        infoListItem.setFcount(a.getInt("fcount"));
                        infoListItem.setId(a.getInt("id"));
                        infoListItem.setImg(a.getString("img"));
                        infoListItem.setTitle(a.getString("title"));
                        infoListItem.setKeywords(a.getString("keywords"));
                        data.add(infoListItem);
                        if (loadListener != null) {
                            loadListener.onItemLoadOk(infoListItem);
                        }
                    }
                    return data;
                } else {
                    throw new Exception(jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
        }

        /**
         * @param rows     否	int	返回最新关键词的条数，默认rows=20
         * @param classify 否	int	分类ID，取得该 分类下的最新数据ify
         * @param id       是	long	当前最新的热点热词关键词id
         * @return
         */
        public static final List<InfoListItem> getInfoNews(int rows, int classify, int id) {
            String url = "http://www.tngou.net/api/info/news?rows=" + rows + "&classify=" + classify + "&id=" + id;
            String request = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(request);
            if (jsonObject.getBooleanValue("status")) {
                com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                return JSON.parseArray(tngou.toJSONString(), InfoListItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 获取指定咨询
         *
         * @param id
         * @return
         */
        @Deprecated
        public static final InfoListItem getInfoOld(int id) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/info/show?id=" + id;
            try {
                String res = InternetServiceTool.request(url);

                JSONObject a = new JSONObject(res);
                if (a.getBoolean("status")) {
                    InfoListItem infoListItem = new InfoListItem();
                    infoListItem.setTime(a.getLong("time"));
                    infoListItem.setRcount(a.getInt("rcount"));
                    infoListItem.setMessage(a.getString("message"));
                    infoListItem.setInfoclass(a.getInt("infoclass"));
                    infoListItem.setCount(a.getInt("count"));
                    infoListItem.setDescription(a.getString("description"));
                    infoListItem.setFcount(a.getInt("fcount"));
                    infoListItem.setId(a.getInt("id"));
                    infoListItem.setImg(a.getString("img"));
                    infoListItem.setTitle(a.getString("title"));
                    infoListItem.setKeywords(a.getString("keywords"));
                    infoListItem.setUrl(a.getString("url"));
                    return infoListItem;
                } else {
                    throw new Exception(a.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }


        }

        /**
         * 健康资讯信息，通过健康资讯的id，取得数据详情。
         *
         * @param id 是	long	新闻资讯id
         * @return
         */
        public static final InfoListItem getInfo(int id) {
            String url = "http://www.tngou.net/api/info/show?id=" + id;
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                return JSON.parseObject(res, InfoListItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }


    /**
     * 健康知识
     */
    public static class Lore {

        /**
         * 获取健康知识分类
         *
         * @return
         */
        @Deprecated
        public static final ArrayList<LoreClassifyItem> getLoreClassifyData(LoadListener loadListener) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/lore/classify";
            try {
                String res = InternetServiceTool.request(url);
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getBoolean("status")) {
                    ArrayList<LoreClassifyItem> listdata = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("tngou");

                    int count = jsonArray.length();

                    for (int i = 0; i < count; i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        LoreClassifyItem loreClassifyItem = new LoreClassifyItem();
                        loreClassifyItem.setTitle(jsonObject1.getString("title"));
                        loreClassifyItem.setDescription(jsonObject1.getString("description"));
                        loreClassifyItem.setKeywords(jsonObject1.getString("keywords"));
                        loreClassifyItem.setId(jsonObject1.getInt("id"));
                        loreClassifyItem.setSeq(jsonObject1.getInt("seq"));
                        loreClassifyItem.setName(jsonObject1.getString("name"));
                        listdata.add(loreClassifyItem);
                        if (loadListener != null) {
                            loadListener.onItemLoadOk(loreClassifyItem);
                        }
                    }
                } else {
                    throw new Exception(jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
            return null;
        }

        /**
         * 获取健康知识分类
         *
         * @return
         */
        public static final List<LoreClassifyItem> getLoreClassifyData() {
            String url = "http://www.tngou.net/api/lore/classify";
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                return JSON.parseArray(tngou.toJSONString(), LoreClassifyItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



        /**
         * 获取健康知识列表数据
         *
         * @param count       数据条数
         * @param page        页数
         * @param loreClassID 健康知识分类id
         * @return
         */
        @Deprecated
        public static final ArrayList<LoreListItem> getLoreListData(int count, int page, int loreClassID, LoadListener loadListener) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/lore/list?rows=" + count + "&page=" + page + "&id=" + loreClassID;
            try {
                ArrayList<LoreListItem> listdata = new ArrayList<>();
                String res = InternetServiceTool.request(url);
                Log.i("HealDataLoader  ", "健康知识加载结果：" + res);
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("tngou");
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        LoreListItem loreListItem = new LoreListItem();
                        loreListItem.setDescription(jsonObject1.getString("description"));
                        loreListItem.setKeywords(jsonObject1.getString("keywords"));
                        loreListItem.setTitle(jsonObject1.getString("title"));
                        loreListItem.setCount(jsonObject1.getInt("count"));
                        loreListItem.setFcount(jsonObject1.getInt("fcount"));
                        loreListItem.setRcount(jsonObject1.getInt("rcount"));
                        loreListItem.setImg(jsonObject1.getString("img"));
                        loreListItem.setId(jsonObject1.getLong("id"));
                        loreListItem.setTime(jsonObject1.getLong("time"));
                        loreListItem.setLoreclass(jsonObject1.getInt("loreclass"));
                        listdata.add(loreListItem);
                        if (null != loadListener) {
                            loadListener.onItemLoadOk(loreListItem);
                        }
                    }
                    return listdata;
                } else {
                    throw new Exception(jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
        }

        /**
         * @param page 否	int	请求页数，默认page=1
         * @param rows 否	int	每页返回的条数，默认rows=20
         * @param id   否	int	分类ID，默认返回的是全部。这里的ID就是指分类的ID
         */
        public static final List<LoreListItem> getLoreListData(int page, int rows, int id) {
            String url = "http://www.tngou.net/api/lore/list?page=" + page + "&rows=" + rows + "&id=" + id;
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                return JSON.parseArray(tngou.toJSONString(), LoreListItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        /**
         * 获取最新健康知识
         *
         * @param count    最新关键词的条数，默认rows=20
         * @param classify 分类ID，取得该分类下的最新数据
         * @param id       #当前最新的知识的id
         * @return
         */
        @Deprecated
        public static final LoreNews getLoreNews(int count, int classify, int id, LoadListener loadListener) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/lore/news?rows=" + count + "&classify=" + classify + "&id=" + id;

            try {
                String res = InternetServiceTool.request(url);
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getBoolean("status")) {
                    LoreNews loreNews = new LoreNews();
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    ArrayList<LoreListItem> list = new ArrayList<>();
                    int countl = jsonArray.length();
                    for (int i = 0; i < countl; i++) {
                        JSONObject j1 = jsonArray.getJSONObject(i);
                        LoreListItem l = new LoreListItem();
                        l.setLoreclass(j1.getInt("loreclass"));
                        l.setTime(j1.getLong("time"));
                        l.setId(j1.getInt("id"));
                        l.setImg(j1.getString("img"));
                        l.setCount(j1.getInt("count"));
                        l.setDescription(j1.getString("description"));
                        l.setFcount(j1.getInt("fcount"));
                        l.setRcount(j1.getInt("rcount"));
                        l.setKeywords(j1.getString("keywords"));
                        l.setTitle(j1.getString("title"));
                        l.setMessage(j1.getString("message"));
                        list.add(l);
                        if (null != loadListener) {
                            loadListener.onItemLoadOk(l);
                        }
                    }
                    loreNews.setList(list);
                    loreNews.setPage(jsonObject.getInt("page"));
                    loreNews.setSize(jsonObject.getInt("size"));
                    loreNews.setTotal(jsonObject.getInt("total"));
                    loreNews.setTotalpage(jsonObject.getInt("totalpage"));
                    return loreNews;
                } else {
                    throw new Exception(jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
        }

        /**
         * @param rows     否	int	返回最新关键词的条数，默认rows=20s
         * @param classify 否	int	分类ID，取得该分类下的最新数据ssify
         * @param id       是	long	当前最新的知识的id
         * @return
         */
        public static final LoreNews getLoreNew(int rows, int classify, int id) {
            String url = "http://www.tngou.net/api/lore/news?rows=" + rows + "&classify=" + classify + "&id=" + id;
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                return JSON.parseObject(res, LoreNews.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 获取一条具体的健康知识内容
         *
         * @param id
         * @return
         */
        @Deprecated
        public LoreListItem getLore(int id) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/lore/show?id=" + id;

            try {
                String res = InternetServiceTool.request(url);
                JSONObject j1 = new JSONObject(res);
                if (j1.getBoolean("status")) {
                    LoreListItem l = new LoreListItem();
                    l.setLoreclass(j1.getInt("loreclass"));
                    l.setTime(j1.getLong("time"));
                    l.setId(j1.getInt("id"));
                    l.setImg(j1.getString("img"));
                    l.setCount(j1.getInt("count"));
                    l.setDescription(j1.getString("description"));
                    l.setFcount(j1.getInt("fcount"));
                    l.setRcount(j1.getInt("rcount"));
                    l.setKeywords(j1.getString("keywords"));
                    l.setTitle(j1.getString("title"));
                    l.setMessage(j1.getString("message"));
                    return l;
                } else {
                    throw new Exception(j1.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
        }

        /**
         * 获取一条具体的健康知识内容
         *
         * @param id 是	long	健康知识的id
         * @return
         */
        public static final LoreListItem getLoreShow(int id) {
            String url = "http://www.tngou.net/api/lore/show?id=" + id;
            return request(url, LoreListItem.class);
        }

    }


    /**
     * 健康问答
     */
    public static final class Ask {

        /**
         * 获取问答分类
         *
         * @return
         */
        @Deprecated
        public static final ArrayList<AskClassifyItem> getAskClassifyData(LoadListener loadListener) throws JavaBeanDataLoadException {

            String url = "http://www.tngou.net/api/ask/classify";
            try {
                String res = InternetServiceTool.request(url);
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getBoolean("status")) {
                    ArrayList<AskClassifyItem> list = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("tngou");
                    int count = jsonArray.length();
                    for (int i = 0; i < count; i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        AskClassifyItem askClassifyItem = new AskClassifyItem();
                        askClassifyItem.setTitle(jsonObject1.getString("title"));
                        askClassifyItem.setKeywords(jsonObject1.getString("keywords"));
                        askClassifyItem.setDescription(jsonObject1.getString("description"));
                        askClassifyItem.setId(jsonObject1.getInt("id"));
                        askClassifyItem.setSeq(jsonObject1.getInt("seq"));
                        list.add(askClassifyItem);
                        if (null != loadListener) {
                            loadListener.onItemLoadOk(askClassifyItem);
                        }
                    }
                    return list;
                } else {
                    throw new Exception(jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
        }

        /**
         * 健康问答分类API接口，取得健康问答的分类，该分类可以用去取得问答详情。
         *
         * @return
         */
        public static final List<AskClassifyItem> getAskClassifyData() {
            String url = "http://www.tngou.net/api/ask/classify";
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                return JSON.parseArray(tngou.toJSONString(), AskClassifyItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 获取问答列表数据
         *
         * @param count 条数
         * @param id    分类id
         * @param page  页数
         * @return
         * @throws JavaBeanDataLoadException
         */
        @Deprecated
        public static final ArrayList<AskListItem> getAskListData(int count, int id, int page, LoadListener loadListener) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/ask/list?rows=" + count + "&id=" + id + "&page=" + page;

            try {

                String res = InternetServiceTool.request(url);
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getBoolean("status")) {
                    ArrayList<AskListItem> listdata = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("tngou");
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        AskListItem askListItem = new AskListItem();
                        askListItem.setTitle(jsonObject1.getString("title"));
                        askListItem.setKeywords(jsonObject1.getString("keywords"));
                        askListItem.setAskclass(jsonObject1.getInt("askclass"));
                        askListItem.setCount(jsonObject1.getInt("count"));
                        askListItem.setDescription(jsonObject1.getString("description"));
                        askListItem.setFcount(jsonObject1.getInt("fcount"));
                        askListItem.setId(jsonObject1.getInt("id"));
                        askListItem.setImg(jsonObject1.getString("img"));
                        askListItem.setMessage(jsonObject1.getString("message"));
                        askListItem.setRcount(jsonObject1.getInt("rcount"));
                        askListItem.setTime(jsonObject1.getLong("time"));
                        listdata.add(askListItem);
                        if (null != loadListener) {
                            loadListener.onItemLoadOk(askListItem);
                        }
                    }
                    return listdata;
                } else {
                    throw new Exception(jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
        }

        /**
         * @param page 否	int	请求页数，默认page=1
         * @param rows 否	int	每页返回的条数，默认rows=20
         * @param id   否	int	分类ID，默认返回的是全部。这里的ID就是指分类的ID
         * @return
         */
        public static final List<AskListItem> getAskListData(int page, int rows, int id) {
            String url = "http://www.tngou.net/api/ask/list?page=" + page + "&rows=" + rows + "&id=" + id;
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                return JSON.parseArray(tngou.toJSONString(), AskListItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 最新健康问答,通过当前最新的ID，取得最新的问答列表。通过该方法可以做到数据的不重复。但改接口也并非一定要用！
         *
         * @param count    返回最新关键词的条数，默认rows=20
         * @param classify 分类ID，取得该分类下的最新数据
         * @param id       #当前最新的热点热词关键词id
         */
        @Deprecated
        public static final AskNews getAskNews(int count, int classify, int id, LoadListener loadListener) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/ask/news?rows=" + count + "&classify=" + classify + "&id =" + id;
            try {
                String res = InternetServiceTool.request(url);
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getBoolean("status")) {
                    AskNews askNews = new AskNews();
                    ArrayList<AskListItem> listdata = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        AskListItem askListItem = new AskListItem();
                        askListItem.setTitle(jsonObject1.getString("title"));
                        askListItem.setKeywords(jsonObject1.getString("keywords"));
                        askListItem.setAskclass(jsonObject1.getInt("askclass"));
                        askListItem.setCount(jsonObject1.getInt("count"));
                        askListItem.setDescription(jsonObject1.getString("description"));
                        askListItem.setFcount(jsonObject1.getInt("fcount"));
                        askListItem.setId(jsonObject1.getInt("id"));
                        askListItem.setImg(jsonObject1.getString("img"));
                        askListItem.setMessage(jsonObject1.getString("message"));
                        askListItem.setRcount(jsonObject1.getInt("rcount"));
                        askListItem.setTime(jsonObject1.getLong("time"));
                        listdata.add(askListItem);
                        if (null != loadListener) {
                            loadListener.onItemLoadOk(askListItem);
                        }
                    }
                    return askNews;
                } else {
                    throw new Exception(jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
        }


        /**
         * @param rows     否	int	返回最新关键词的条数，默认rows=20s
         * @param classify 否	int	分类ID，取得该分类下的最新数据ssify
         * @param id       是	long	当前最新的热点热词关键词id
         * @return
         */
        public static final AskNews getAskNews(int rows, int classify, int id) {
            String url = "http://www.tngou.net/api/ask/news?rows=" + rows + "&classify=" + classify + "&id =" + id;
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                return JSON.parseObject(res, AskNews.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        /**
         * 健康问答信息，通过健康问答的id，取得数据详情。
         *
         * @param id 健康资讯的id
         * @return
         */
        @Deprecated
        public static final AskListItem getAsk(int id) throws JavaBeanDataLoadException {
            String url = "thttp://www.tngou.net/api/ask/show?id=" + id;

            try {
                String res = InternetServiceTool.request(url);
                JSONObject jsonObject1 = new JSONObject(res);

                if (jsonObject1.getBoolean("status")) {
                    AskListItem askListItem = new AskListItem();
                    askListItem.setTitle(jsonObject1.getString("title"));
                    askListItem.setKeywords(jsonObject1.getString("keywords"));
                    askListItem.setAskclass(jsonObject1.getInt("askclass"));
                    askListItem.setCount(jsonObject1.getInt("count"));
                    askListItem.setDescription(jsonObject1.getString("description"));
                    askListItem.setFcount(jsonObject1.getInt("fcount"));
                    askListItem.setId(jsonObject1.getInt("id"));
                    askListItem.setImg(jsonObject1.getString("img"));
                    askListItem.setMessage(jsonObject1.getString("message"));
                    askListItem.setRcount(jsonObject1.getInt("rcount"));
                    askListItem.setTime(jsonObject1.getLong("time"));
                    return askListItem;
                } else {
                    throw new Exception(jsonObject1.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }

        }

        /**
         * @param id 是	long	健康资讯的id
         * @return
         */
        public static final AskListItem getAskShow(int id) {
            String url = "http://www.tngou.net/api/ask/show?id=" + id;
            return request(url, AskListItem.class);
        }
    }


    /**
     * 健康图书类
     */
    public static class Book {
        /**
         * 获取健康图书分类
         *
         * @param loadListener
         * @return
         * @throws JavaBeanDataLoadException
         */
        @Deprecated
        public static final ArrayList<BookClassifyItem> getBookClassifyData(LoadListener loadListener) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/book/classify";
            try {
                String res = InternetServiceTool.request(url);
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getBoolean("status")) {
                    ArrayList<BookClassifyItem> listdata = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("tngou");

                    int count = jsonArray.length();

                    for (int i = 0; i < count; i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        BookClassifyItem bookClassifyItem = new BookClassifyItem();
                        bookClassifyItem.setTitle(jsonObject1.getString("title"));
                        bookClassifyItem.setDescription(jsonObject1.getString("description"));
                        bookClassifyItem.setKeywords(jsonObject1.getString("keywords"));
                        bookClassifyItem.setId(jsonObject1.getInt("id"));
                        bookClassifyItem.setSeq(jsonObject1.getInt("seq"));
                        bookClassifyItem.setName(jsonObject1.getString("name"));
                        listdata.add(bookClassifyItem);
                        if (loadListener != null) {
                            loadListener.onItemLoadOk(bookClassifyItem);
                        }
                    }
                } else {
                    throw new Exception(jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
            return null;
        }

        /**
         * 健康图书分类API接口，取得不同类型的图书，方便后期查询列表
         *
         * @return
         */
        public static final List<BookClassifyItem> getBookClassifyData() {
            String url = "http://www.tngou.net/api/book/classify";
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                return JSON.parseArray(tngou.toJSONString(), BookClassifyItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 获取健康图书列表
         *
         * @param count        每页返回的条数，默认rows=20
         * @param page         请求页数，默认page=1
         * @param id           分类ID，默认返回的是全部。这里的ID就是指分类的ID
         * @param loadListener
         * @return
         * @throws JavaBeanDataLoadException
         */
        @Deprecated
        public static final ArrayList<BookListItem> getBookListData(int count, int page, int id, LoadListener loadListener) throws JavaBeanDataLoadException {

            String url = null;
            if (id != 0) {
                url = "http://www.tngou.net/api/book/list?rows=" + count + "&page=" + page + "&id=" + id;
            } else {
                url = "http://www.tngou.net/api/book/list?rows=" + count + "&page=" + page;
            }
            try {
                ArrayList<BookListItem> listdata = new ArrayList<>();
                String res = InternetServiceTool.request(url);
                Log.i("获取健康图书列表", res);
                JSONObject jsonObject = new JSONObject(res);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        BookListItem bookListItem = new BookListItem();
                        bookListItem.setAuthor(jsonObject1.getString("author"));
                        bookListItem.setBookclass(jsonObject1.getInt("bookclass"));
                        bookListItem.setName(jsonObject1.getString("name"));
                        bookListItem.setCount(jsonObject1.getInt("count"));
                        bookListItem.setFcount(jsonObject1.getInt("fcount"));
                        bookListItem.setRcount(jsonObject1.getInt("rcount"));
                        bookListItem.setId(jsonObject1.getInt("id"));
                        bookListItem.setImg(jsonObject1.getString("img"));
                        bookListItem.setSummary(jsonObject1.getString("summary"));
                        bookListItem.setTime(jsonObject1.getLong("time"));
                        listdata.add(bookListItem);
                        if (null != loadListener) {
                            loadListener.onItemLoadOk(bookListItem);
                        }
                    }
                    return listdata;
                } else {
                    throw new Exception(jsonObject.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
        }

        /**
         * @param page 否	int	请求页数，默认page=1
         * @param rows 否	int	每页返回的条数，默认rows=20
         * @param id   否	int	分类ID，默认返回的是全部。这里的ID就是指分类的ID
         */
        public static final List<BookListItem> getBookList(int page, int rows, int id) {
            String url = "http://www.tngou.net/api/book/list?page=" + page + "&rows=" + rows + "&id=" + id;
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                com.alibaba.fastjson.JSONArray list = jsonObject.getJSONArray("list");
                return JSON.parseArray(list.toJSONString(), BookListItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        /**
         * 健康图书信息，通过健康图书的id，取得数据详情。
         *
         * @param id #图书ID
         * @return
         */
        @Deprecated
        public static final BookListItem getBookOld(int id) throws JavaBeanDataLoadException {
            String url = "http://www.tngou.net/api/book/show?id=" + id;
            try {
                String res = InternetServiceTool.request(url);
                JSONObject jsonObject1 = new JSONObject(res);
                if (jsonObject1.getBoolean("status")) {
                    BookListItem bookListItem = new BookListItem();
                    bookListItem.setAuthor(jsonObject1.getString("author"));
                    bookListItem.setBookclass(jsonObject1.getInt("bookclass"));
                    bookListItem.setName(jsonObject1.getString("name"));
                    bookListItem.setCount(jsonObject1.getInt("count"));
                    bookListItem.setFcount(jsonObject1.getInt("fcount"));
                    bookListItem.setRcount(jsonObject1.getInt("rcount"));
                    bookListItem.setId(jsonObject1.getInt("id"));
                    bookListItem.setImg(jsonObject1.getString("img"));
                    bookListItem.setSummary(jsonObject1.getString("summary"));
                    bookListItem.setTime(jsonObject1.getLong("time"));
                    return bookListItem;
                } else {
                    throw new Exception(jsonObject1.getString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JavaBeanDataLoadException(e, url, error_description);
            }
        }

        /**
         * 健康图书信息，通过健康图书的id，取得数据详情。
         *
         * @param id 是	int	图书ID
         * @return
         */
        public static final BookListItem getBook(int id) {
            String url = "http://www.tngou.net/api/book/show?id=" + id;
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                return JSON.parseObject(res, BookListItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }


    public static class Cook {
        /**
         * 获取健康菜谱分类
         *
         * @return
         */
        public static final List<CookClassify> getCookClassifyData() {
            String url = "http://www.tngou.net/api/cook/classify";
            try {
                String request = InternetServiceTool.request(url);
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(request);
                if (jsonObject.getBooleanValue("status")) {
                    com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                    return JSON.parseArray(tngou.toJSONString(), CookClassify.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * @param rows 否	int	每页返回的条数，默认rows=20
         * @param page 否	int	请求页数，默认page=1
         * @param id   否	int	分类ID，默认返回的是全部。这里的ID就是指分类
         * @return 菜谱列表API接口-天狗菜谱信息分类列表接口
         */
        public static final List<CookListItem> getCookList(int rows, int page, int id) {
            String url = "http://www.tngou.net/api/cook/list?rows=" + rows + "&page=" + page + "&id=" + id;
            String request = InternetServiceTool.request(url);
//            Log.i(TAG, "健康菜谱请求结果:" + request);
            try {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(request);
                if (jsonObject.getBooleanValue("status")) {
                    com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                    int count = tngou.size();
                    for (int i = 0; i < count; i++) {
                        tngou.getJSONObject(i).put("cookclass", id);
                    }

                    return JSON.parseArray(tngou.toJSONString(), CookListItem.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * @param id 是	long	菜谱id
         * @return
         */
        public static final CookListItem getCookShow(int id) {
            String url = "http://www.tngou.net/api/cook/show?id=" + id;
            String res = InternetServiceTool.request(url);
            try {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                return JSON.parseObject(res, CookListItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * @param name 是	string	食品名称
         * @return
         */
        public static final CookListItem getCookName(String name) {
            String url = "http://www.tngou.net/api/cook/name?name=" + name;
            String res = InternetServiceTool.request(url);
            try {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                return JSON.parseObject(res, CookListItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public static class Food {
        /**
         * @param id 否	int	分类ID，取得分类下的分类。默认为0,返回一级分类。
         * @return
         */
        public static final List<FoodClassify> getFoodClassify2(int id) {
            String url = "http://www.tngou.net/api/food/classify?id=" + id;
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getBooleanValue("status")) {
                com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                return JSON.parseArray(tngou.toJSONString(), FoodClassify.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * @param page 否	int	请求页数，默认page=1
         * @param rows 否	int	每页返回的条数，默认rows=20
         * @param id   否	int	分类ID，默认返回的是全部。这里的ID就是指分类的ID
         * @return
         */
        public static final List<FoodListItem> getFoodList(int rows, int page, int id) {
            String url = "http://www.tngou.net/api/food/list?rows=" + rows + "&page=" + page + "&id=" + id;
            String res = InternetServiceTool.request(url);
            try {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
                if (jsonObject.getBooleanValue("status")) {
                    com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                    int count = tngou.size();
                    for (int i = 0; i < count; i++) {
                        tngou.getJSONObject(i).put("foodclass", id);
                    }
                    return JSON.parseArray(tngou.toJSONString(), FoodListItem.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * @param id 是	long	食品id
         * @return
         */
        public static final FoodListItem getFoodShow(int id) {
            String url = "http://www.tngou.net/api/food/show?id=" + id;
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if(jsonObject.getBooleanValue("status")){
                return JSON.parseObject(res,FoodListItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * @param name 是	string	食品名称
         * @return
         */
        public static final FoodListItem getFoodName(String name) {
            String url = "http://www.tngou.net/api/food/name?name=" + name;
            String res = InternetServiceTool.request(url);
            try {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
            if(jsonObject.getBooleanValue("status")){
                return JSON.parseObject(res,FoodListItem.class);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    /**
     * 发送指定请求,返回指定对象
     *
     * @param url   请求地址
     * @param clazz 返回对象类型
     * @return
     */
    private static <T> T request(String url, Class<T> clazz) {
        String res = InternetServiceTool.request(url);
        try {
            return JSON.parseObject(res, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 天狗用户接口
     */
    public static final class UserInterface {

        /**
         * 登陆
         *
         * @param client_id     是	string	OAuth2客户ID
         * @param client_secret 是	string	安全密文ret
         * @param name          是	string	邮件/账号 也就是email与account的一个
         * @param password      是	string	密码
         * @return
         */
        public static final User login(String client_id, String client_secret, String name, String password) {
            String url = "http://www.tngou.net/api/oauth2/login?client_id=" + client_id + "&client_secret=" + client_secret + "&name=" + name + "&password=" + password;
            try {
                String request = InternetServiceTool.request(url);
                Log.i("登陆", request);
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(request);
                if (jsonObject.getBooleanValue("status")) {
                    User.getUser().setAccess_token(jsonObject.getString("access_token"));
                    User.getUser().setRefresh_token(jsonObject.getString("refresh_token"));
                    User.getUser().setId(jsonObject.getInteger("id"));
                    return User.getUser();
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        /**
         * 获取我的关注好友列表
         *
         * @return
         */
        public static final List<Friend> myHeart(int rows, int page, String name, String access_token) {
            String url = "http://www.tngou.net/api/my/heart?rows=" + rows + "&page=" + page + "&name=" + name + "&access_token=" + access_token;
            String request = InternetServiceTool.request(url);
            Log.i("获取关注好友", "" + request);
            try {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(request);
                if (jsonObject.getBooleanValue("status")) {
                    com.alibaba.fastjson.JSONArray tngou = jsonObject.getJSONArray("tngou");
                    return JSON.parseArray(tngou.toJSONString(), Friend.class);
                } else {
                    if (login(LoginActivity.client_id, LoginActivity.client_secret, User.getUser().getAccount(), User.getUser().getPassword()) != null) {
                        return myHeart(rows, page, name, access_token);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 数据记载过程中的回调
     */
    @Deprecated
    public static interface LoadListener {
        /**
         * 传入加载成功的对象
         *
         * @param obj
         */
        void onItemLoadOk(Object obj);
    }

}
