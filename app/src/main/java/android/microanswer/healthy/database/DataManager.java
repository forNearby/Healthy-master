package android.microanswer.healthy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.microanswer.healthy.bean.AskClassifyItem;
import android.microanswer.healthy.bean.AskListItem;
import android.microanswer.healthy.bean.BookClassifyItem;
import android.microanswer.healthy.bean.BookListItem;
import android.microanswer.healthy.bean.CookClassify;
import android.microanswer.healthy.bean.CookListItem;
import android.microanswer.healthy.bean.FoodClassify;
import android.microanswer.healthy.bean.FoodListItem;
import android.microanswer.healthy.bean.InfoClassifyItem;
import android.microanswer.healthy.bean.InfoListItem;
import android.microanswer.healthy.bean.LoreClassifyItem;
import android.microanswer.healthy.bean.LoreListItem;
import android.microanswer.healthy.tools.BaseTools;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 软件的数据管理类<br/>
 * 该类直接和数据库联系
 * 由 Micro 创建于 2016/7/3.
 */

public final class DataManager {

    public static final int DATABASEVERSION = 1;

    private Context context;
    private DataBaseOpenHelper dboh;

    public DataManager(Context context) {
        this.context = context;
        this.dboh = new DataBaseOpenHelper(this.context, DATABASEVERSION);
    }

    /**
     * 获取健康咨询分类
     *
     * @return
     */
    public ArrayList<InfoClassifyItem> getInfoClassifyItems() {
        ArrayList<InfoClassifyItem> data = new ArrayList<>();
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            Cursor result = readableDatabase.query(DataBaseOpenHelper.TABLE_INFO_CLASSIFY, null, null, null, null, null, null);
            if (result.moveToFirst()) {
                do {
                    InfoClassifyItem infoClassifyItem = new InfoClassifyItem();
                    infoClassifyItem.setName(result.getString(result.getColumnIndex(DataBaseOpenHelper.INFO_CLASSIFY_NAME)));
                    infoClassifyItem.setTitle(result.getString(result.getColumnIndex(DataBaseOpenHelper.INFO_CLASSIFY_TITLE)));
                    infoClassifyItem.setDescription(result.getString(result.getColumnIndex(DataBaseOpenHelper.INFO_CLASSIFY_DESCRIPTION)));
                    infoClassifyItem.setId(result.getInt(result.getColumnIndex(DataBaseOpenHelper.INFO_CLASSIFY_ID)));
                    infoClassifyItem.setKeywords(result.getString(result.getColumnIndex(DataBaseOpenHelper.INFO_CLASSIFY_KEYWORDS)));
                    infoClassifyItem.setSeq(result.getInt(result.getColumnIndex(DataBaseOpenHelper.INFO_CLASSIFY_SEQ)));
                    data.add(infoClassifyItem);
                } while (result.moveToNext());
            }
            result.close();
            readableDatabase.close();
        }
        return data;
    }

    /**
     * 将健康咨询分类写入数据库
     *
     * @param data
     * @return 返回添加成功的条数
     */
    public int putInfoClassifyItems(ArrayList<InfoClassifyItem> data) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int conunt = -1;
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();
            for (InfoClassifyItem item : data) {
                if (exist(item.getId(), DataBaseOpenHelper.TABLE_INFO_CLASSIFY, writableDatabase)) {
                    continue;
                }
                ContentValues values = BaseTools.createContentValues(item);
                long insert = writableDatabase.insert(DataBaseOpenHelper.TABLE_INFO_CLASSIFY, DataBaseOpenHelper.INFO_CLASSIFY_DESCRIPTION, values);
                if (insert != -1) {
                    conunt++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return conunt;
    }

    /**
     * 通过id获取一个InfoListItem
     *
     * @param id
     * @return
     */
    public InfoListItem getInfoListItem(int id) {
        InfoListItem infoListItem = null;
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_INFO, null, DataBaseOpenHelper.INFO_ID, new String[]{id + ""}, null, null, null);
            if (query.moveToFirst()) {
                infoListItem = BaseTools.cursor2Object(InfoListItem.class, query);
                query.close();
                readableDatabase.close();
            }
        }
        return infoListItem;
    }


    /**
     * 获取健康咨询列表
     *
     * @param count 条数
     * @param page  页数
     * @param id    要获取的健康咨询的分类id
     * @return
     */
    public ArrayList<InfoListItem> getInfoListItems(int count, int page, int id) {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {//{"limit " + count + " offset "+ (count * (page - 1))}
            Cursor result = null;
            if (id != -1) {
                result = readableDatabase.query(DataBaseOpenHelper.TABLE_INFO, null, DataBaseOpenHelper.INFO_INFOCLASSIFY + " = ? ", new String[]{id + ""}, null, null, DataBaseOpenHelper.INFO_ID + " DESC", (count * (page - 1)) + "," + count);
            } else {
                result = readableDatabase.query(DataBaseOpenHelper.TABLE_INFO, null, null, null, null, null, DataBaseOpenHelper.INFO_ID + " DESC", (count * (page - 1)) + "," + count);
            }
            ArrayList<InfoListItem> data = new ArrayList<>();
            try {
                if (result.moveToFirst()) {
                    do {
                        InfoListItem infoListItem = BaseTools.cursor2Object(InfoListItem.class, result);
                        data.add(infoListItem);
                    } while (result.moveToNext());
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                result.close();
                readableDatabase.close();
                return null;
            } finally {
                result.close();
                readableDatabase.close();
            }
            return data;
        }
        return null;
    }


    /**
     * 向数据库放入一个健康咨询数据
     *
     * @param item
     * @return
     */
    public long putInfoListItem(InfoListItem item) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        if (exist(item.getId(), DataBaseOpenHelper.TABLE_INFO, writableDatabase)) {
            writableDatabase.close();
            return 0L;
        }

        long l = 0;
        if (writableDatabase.isOpen()) {
            ContentValues contentValues = BaseTools.createContentValues(item);
            l = writableDatabase.insert(DataBaseOpenHelper.TABLE_INFO, DataBaseOpenHelper.INFO_DESCRIPTION, contentValues);
            writableDatabase.close();
        }
        return l;
    }

    /**
     * 向数据库放入多个健康咨询
     *
     * @param items
     * @return
     */
    public int putInfoListItems(List<InfoListItem> items) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int a = 0;
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();
            for (int i = 0; i < items.size(); i++) {
                InfoListItem item = items.get(i);

                if (exist(item.getId(), DataBaseOpenHelper.TABLE_INFO, writableDatabase)) {
                    continue;
                }

                ContentValues contentValues = BaseTools.createContentValues(item);
                long insert = writableDatabase.insert(DataBaseOpenHelper.TABLE_INFO, DataBaseOpenHelper.INFO_DESCRIPTION, contentValues);
                if (insert != -1) {
                    a++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
            return a;
        }
        return -1;
    }

    /**
     * 根据id查看数据库中是否存在自定InfoListItem数据
     *
     * @param infoId
     * @return
     */
    public boolean hasInfoListItem(int infoId) {
        return getInfoListItem(infoId) != null;
    }

    /**
     * 删除所有健康信息数据
     *
     * @return
     */
    public int clearInfo() {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int c = 0;
        if (writableDatabase.isOpen()) {
            c = writableDatabase.delete(DataBaseOpenHelper.TABLE_INFO, null, null);
            writableDatabase.close();
        }
        return c;
    }

    /**
     * 获取健康知识分类
     *
     * @return
     */
    public ArrayList<LoreClassifyItem> getLoreClassifyItems() {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_LORE_CLASSIFY, null, null, null, null, null, null);
            ArrayList<LoreClassifyItem> data = new ArrayList<>();
            if (query.moveToFirst()) {
                do {
                    LoreClassifyItem loreClassifyItem = BaseTools.cursor2Object(LoreClassifyItem.class, query);
                    data.add(loreClassifyItem);
                } while (query.moveToNext());
            }
            query.close();
            readableDatabase.close();
            return data;
        }
        return null;
    }

    /**
     * 将健康知识分类存入数据库
     *
     * @param data
     * @return 写入的数据条数
     */
    public int putLoreClassifyItems(List<LoreClassifyItem> data) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();

        int c = 0;
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();

            for (int i = 0; i < data.size(); i++) {
                LoreClassifyItem item = data.get(i);
                if (exist(item.getId(), DataBaseOpenHelper.TABLE_LORE_CLASSIFY, writableDatabase)) {
                    continue;
                }
                long a = writableDatabase.insert(DataBaseOpenHelper.TABLE_LORE_CLASSIFY, DataBaseOpenHelper.LORE_CLASSIFY_DESCRIPTION, BaseTools.createContentValues(item));
                if (a != -1) {
                    c++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return c;
    }

    /**
     * 获取一个健康知识
     *
     * @param id
     * @return
     */
    public LoreListItem getLoreListItem(int id) {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_LORE, null, DataBaseOpenHelper.LORE_ID, new String[]{id + ""}, null, null, null);
            if (query.moveToFirst()) {
                LoreListItem loreListItem = BaseTools.cursor2Object(LoreListItem.class, query);
                query.close();
                readableDatabase.close();
                return loreListItem;
            }
        }
        return null;
    }

    /**
     * 获取多个健康知识
     *
     * @param count 个数
     * @param page  页数
     * @param id    id号
     * @return
     */
    public ArrayList<LoreListItem> getLoreListItems(int count, int page, int id) {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            Cursor query;
            if (id != -1) {
                query = readableDatabase.query(DataBaseOpenHelper.TABLE_LORE, null, DataBaseOpenHelper.LORE_LORECLASSIFY + " = ? ", new String[]{id + ""}, null, null, DataBaseOpenHelper.LORE_ID + " DESC", (count * (page - 1)) + "," + count);
            } else {
                query = readableDatabase.query(DataBaseOpenHelper.TABLE_LORE, null, null, null, null, null, DataBaseOpenHelper.LORE_ID + " DESC", (count * (page - 1)) + "," + count);
            }
            ArrayList<LoreListItem> data = new ArrayList<>();
            if (query.moveToFirst()) {
                do {
                    LoreListItem loreListItem = BaseTools.cursor2Object(LoreListItem.class, query);
                    data.add(loreListItem);
                } while (query.moveToNext());
            }
            query.close();
            readableDatabase.close();
            return data;
        }
        return null;
    }

    /**
     * 添加一条健康知识到数据库
     *
     * @param item
     * @return
     */
    public long putLoreListItem(LoreListItem item) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        if (exist((int) item.getId(), DataBaseOpenHelper.TABLE_LORE, writableDatabase)) {
            writableDatabase.close();
            return 0L;
        }
        long l = -1;
        if (writableDatabase.isOpen()) {
            ContentValues values = BaseTools.createContentValues(item);
            l = writableDatabase.insert(DataBaseOpenHelper.TABLE_LORE, DataBaseOpenHelper.LORE_DESCRIPTION, values);
            writableDatabase.close();
        }
        return l;
    }

    /**
     * 添加多条将健康知识到数据库
     *
     * @param items
     * @return
     */
    public int putLoreListItems(List<LoreListItem> items) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int c = 0;
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();
            for (LoreListItem item : items) {
                if (exist((int) item.getId(), DataBaseOpenHelper.TABLE_LORE, writableDatabase)) {
                    continue;
                }
                ContentValues contentValues = BaseTools.createContentValues(item);
                long l = writableDatabase.insert(DataBaseOpenHelper.TABLE_LORE, DataBaseOpenHelper.LORE_DESCRIPTION, contentValues);
                if (l != -1) {
                    c++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return c;
    }

    /**
     * @param id
     * @return
     */
    public boolean hasLoreListItem(int id) {
        return getLoreListItem(id) != null;
    }

    /**
     * 删除所有健康知识数据
     *
     * @return
     */
    public int clearLore() {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int c = 0;
        if (writableDatabase.isOpen()) {
            c = writableDatabase.delete(DataBaseOpenHelper.TABLE_LORE, null, null);
            writableDatabase.close();
        }
        return c;
    }


    /**
     * 获取健康问答分类
     *
     * @return
     */
    public ArrayList<AskClassifyItem> getAskClassifyItems() {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {

            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_ASK_CLASSIFY, null, null, null, null, null, null);
            ArrayList<AskClassifyItem> data = new ArrayList<>();
            if (query.moveToFirst()) {

                do {
                    AskClassifyItem askClassifyItem = BaseTools.cursor2Object(AskClassifyItem.class, query);
                    data.add(askClassifyItem);
                } while (query.moveToNext());
            }
            query.close();
            readableDatabase.close();
            return data;
        }
        return null;
    }

    /**
     * 写入健康问答分类到数据库
     *
     * @param datas
     * @return
     */
    public int putAskClassifyItems(List<AskClassifyItem> datas) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int count = 0;
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();
            for (AskClassifyItem item : datas) {
                if (exist(item.getId(), DataBaseOpenHelper.TABLE_ASK_CLASSIFY, writableDatabase)) {
                    continue;
                }
                ContentValues values = BaseTools.createContentValues(item);
                long l = writableDatabase.insert(DataBaseOpenHelper.TABLE_ASK_CLASSIFY, DataBaseOpenHelper.ASK_CLASSIFY_DESCRIPTION, values);
                if (l != -1) {
                    count++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
            return count;
        }
        return -1;
    }

    /**
     * 获取一条健康问答详情
     *
     * @param id
     * @return
     */
    public AskListItem getAskListItem(int id) {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_ASK, null, DataBaseOpenHelper.ASK_ID, new String[]{id + ""}, null, null, null, null);
            if (query.moveToFirst()) {
                AskListItem askListItem = BaseTools.cursor2Object(AskListItem.class, query);
                readableDatabase.close();
                return askListItem;
            }
        }
        return null;
    }


    /**
     * 获取多条健康问答数据
     *
     * @param count
     * @param page
     * @param id
     * @return
     */
    public ArrayList<AskListItem> getAskListItems(int count, int page, int id) {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            ArrayList<AskListItem> data = new ArrayList<>();
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_ASK, null, DataBaseOpenHelper.ASK_ID + " = ? ", new String[]{id + ""}, null, null, DataBaseOpenHelper.ASK_ID + " DESC", (count * (page - 1)) + "," + count);
            if (query.moveToFirst()) {
                do {
                    AskListItem askListItem = BaseTools.cursor2Object(AskListItem.class, query);
                    data.add(askListItem);
                } while (query.moveToNext());
            }
            query.close();
            readableDatabase.close();
            return data;
        }
        return null;
    }

    /**
     * 写入一条健康问答详情
     *
     * @param item
     * @return
     */
    public long putAskListItem(AskListItem item) {
        long l = 0L;
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        if (exist((int) item.getId(), DataBaseOpenHelper.TABLE_ASK, writableDatabase)) {
            writableDatabase.close();
            return 0L;
        }
        if (writableDatabase.isOpen()) {
            ContentValues values = BaseTools.createContentValues(item);
            l = writableDatabase.insert(DataBaseOpenHelper.TABLE_ASK, DataBaseOpenHelper.ASK_DESCRIPTION, values);
            writableDatabase.close();
        }
        return l;
    }

    /**
     * 写入多条健康问答详情
     *
     * @param items
     * @return
     */
    public int putAskListItems(ArrayList<AskListItem> items) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int a = 0;
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();
            for (AskListItem item : items) {
                if (exist((int) item.getId(), DataBaseOpenHelper.TABLE_ASK, writableDatabase)) {
                    continue;
                }
                ContentValues value = BaseTools.createContentValues(item);
                long o = writableDatabase.insert(DataBaseOpenHelper.TABLE_ASK, DataBaseOpenHelper.ASK_DESCRIPTION, value);
                if (o != -1) {
                    a++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return a;
    }

    /**
     * 判断数据库中是否存在某一条健康问答详情
     *
     * @param id
     * @return
     */
    public boolean hasAskListItem(int id) {
        return getAskListItem(id) != null;
    }

    /**
     * 删除所有健康问答数据
     *
     * @return
     */
    public int clearAsk() {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int c = 0;
        if (writableDatabase.isOpen()) {
            c = writableDatabase.delete(DataBaseOpenHelper.TABLE_ASK, null, null);
            writableDatabase.close();
        }
        return c;
    }

    /**
     * 获取健康图书分类
     */
    public ArrayList<BookClassifyItem> getBookClassifyItems() {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_BOOK_CLASSIFY, null, null, null, null, null, null);
            ArrayList<BookClassifyItem> data = new ArrayList<>();

            if (query.moveToFirst()) {
                do {
                    BookClassifyItem bookClassify = BaseTools.cursor2Object(BookClassifyItem.class, query);
                    data.add(bookClassify);
                } while (query.moveToNext());
            }

            query.close();
            readableDatabase.close();
            return data;
        }
        return null;
    }

    /**
     * 写入将健康图书分类
     *
     * @param items
     */
    public int putBookClassifyItems(ArrayList<BookClassifyItem> items) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int count = 0;
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();
            for (BookClassifyItem item : items) {
                if (exist((int) item.getId(), DataBaseOpenHelper.TABLE_BOOK_CLASSIFY, writableDatabase)) {
                    continue;
                }
                ContentValues value = BaseTools.createContentValues(item);
                long l = writableDatabase.insert(DataBaseOpenHelper.TABLE_BOOK_CLASSIFY, DataBaseOpenHelper.BOOK_CLASSIFY_DESCRIPTION, value);
                if (l != -1) {
                    count++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return count;
    }

    /**
     * 获取一条健康图书详情
     *
     * @param id
     * @return
     */
    public BookListItem getBookListItem(int id) {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_BOOK, null, DataBaseOpenHelper.BOOK_ID, new String[]{id + ""}, null, null, null);
            if (query.moveToFirst()) {
                BookListItem bookListItem = BaseTools.cursor2Object(BookListItem.class, query);
                readableDatabase.close();
                return bookListItem;
            }
        }
        return null;
    }

    /**
     * 获取多条健康图书信息
     *
     * @param count
     * @param page
     * @param id
     * @return
     */
    public ArrayList<BookListItem> getBookListItems(int count, int page, int id) {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            Cursor query;
            if (id != -1) {
                query = readableDatabase.query(DataBaseOpenHelper.TABLE_BOOK, null, DataBaseOpenHelper.BOOK_BOOKCLASS + " = ? ", new String[]{id + ""}, null, null, DataBaseOpenHelper.BOOK_ID + " DESC", (count * (page - 1)) + "," + count);
            } else {
                query = readableDatabase.query(DataBaseOpenHelper.TABLE_BOOK, null, null, null, null, null, DataBaseOpenHelper.BOOK_ID + " DESC", (count * (page - 1)) + "," + count);
            }
            ArrayList<BookListItem> data = new ArrayList<>();
            if (query.moveToFirst())
                do {
                    data.add(BaseTools.cursor2Object(BookListItem.class, query));
                } while (query.moveToNext());

            query.close();
            readableDatabase.close();
            return data;
        }
        return null;
    }


    /**
     * 添加一条健康图书
     *
     * @param item
     * @return
     */
    public long putBookListItem(BookListItem item) {
        long l = -1;
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        if (exist((int) item.getId(), DataBaseOpenHelper.TABLE_BOOK, writableDatabase)) {
            writableDatabase.close();
            return 0L;
        }
        if (writableDatabase.isOpen()) {
            ContentValues values = BaseTools.createContentValues(item);

            l = writableDatabase.insert(DataBaseOpenHelper.TABLE_BOOK, DataBaseOpenHelper.BOOK_SUMMARY, values);
            writableDatabase.close();
        }
        return l;
    }

    /**
     * 添加多条健康图书数据
     *
     * @param bookListItems
     * @return
     */
    public int putBookListItems(List<BookListItem> bookListItems) {
        int count = 0;
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();
            for (BookListItem item : bookListItems) {
                if (exist((int) item.getId(), DataBaseOpenHelper.TABLE_BOOK, writableDatabase)) {
                    continue;
                }
                ContentValues values = BaseTools.createContentValues(item);
                long l = writableDatabase.insert(DataBaseOpenHelper.TABLE_BOOK, DataBaseOpenHelper.BOOK_SUMMARY, values);
                if (l != -1) {
                    count++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return count;
    }

    /**
     * 判断数据库中是否存在某一条健康图书数据
     *
     * @param id
     * @return
     */
    public boolean hasBookListItem(int id) {
        return getBookListItem(id) != null;
    }

    /**
     * 删除所有健康图书
     *
     * @return
     */
    public int clearBooks() {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int c = 0;
        if (writableDatabase.isOpen()) {
            c = writableDatabase.delete(DataBaseOpenHelper.TABLE_BOOK, null, null);
            writableDatabase.close();
        }
        return c;
    }

    /**
     * 获取健康食物分类
     *
     * @return
     */
    public ArrayList<FoodClassify> getFoodClassifyItems() {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            ArrayList<FoodClassify> data = new ArrayList<>();
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_FOOD_CLASSIFY, null, null, null, null, null, null);
            if (query.moveToFirst()) {
                do {
                    data.add(BaseTools.cursor2Object(FoodClassify.class, query));
                } while (query.moveToNext());
            }
            query.close();
            readableDatabase.close();
            return data;
        }
        return null;
    }


    /**
     * 写入健康食物分类
     *
     * @param datas
     * @return
     */
    public int putFoodClassifys(List<FoodClassify> datas) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int count = 0;
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();
            for (FoodClassify item : datas) {
                if (exist((int) item.getId(), DataBaseOpenHelper.TABLE_FOOD_CLASSIFY, writableDatabase)) {
                    continue;
                }
                ContentValues contentValues = BaseTools.createContentValues(item);
                long o = writableDatabase.insert(DataBaseOpenHelper.TABLE_FOOD_CLASSIFY, DataBaseOpenHelper.FOOD_CLASSIFY_DESCRIPTION, contentValues);
                if (o != -1) {
                    count++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return count;
    }


    /**
     * 获取一条健康食物详情
     *
     * @param id
     * @return
     */
    public FoodListItem getFoodLiteItem(int id) {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_FOOD, null, DataBaseOpenHelper.FOOD_ID, new String[]{id + ""}, null, null, null);
            if (query.moveToFirst()) {
                FoodListItem foodListItem = BaseTools.cursor2Object(FoodListItem.class, query);
                query.close();
                readableDatabase.close();
                return foodListItem;
            }
        }
        return null;
    }

    /**
     * 获取多条健康食物数据
     *
     * @param count
     * @param page
     * @param id
     * @return
     */
    public ArrayList<FoodListItem> getFoodListItems(int count, int page, int id) {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_FOOD, null, DataBaseOpenHelper.FOOD_CLASS + " = ? ", new String[]{id + ""}, null, null, DataBaseOpenHelper.FOOD_ID + " ASC", (count * (page - 1)) + "," + count);
            ArrayList<FoodListItem> data = new ArrayList<>();
            try {
                if (query.moveToFirst()) {
                    do {
                        FoodListItem foodListItem = BaseTools.cursor2Object(FoodListItem.class, query);
                        data.add(foodListItem);
                    } while (query.moveToNext());
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                query.close();
                readableDatabase.close();
                return null;
            } finally {
                query.close();
                readableDatabase.close();
            }
            return data;
        }
        return null;
    }

    /**
     * 写入一条健康食物
     *
     * @param item
     * @return
     */
    public long putFoodListItem(FoodListItem item) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        if (exist((int) item.getId(), DataBaseOpenHelper.TABLE_FOOD, writableDatabase)) {
            writableDatabase.close();
            return 0L;
        }
        if (writableDatabase.isOpen()) {
            ContentValues values = BaseTools.createContentValues(item);
            long l = writableDatabase.insert(DataBaseOpenHelper.TABLE_FOOD, DataBaseOpenHelper.FOOD_DESCRIPTION, values);
            writableDatabase.close();
            return l;
        }
        return -1;
    }


    /**
     * 写入多条健康食物数据
     *
     * @param items
     * @return
     */
    public int putFoodListItems(List<FoodListItem> items) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int count = 0;
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();
            for (FoodListItem item : items) {
                if (exist((int) item.getId(), DataBaseOpenHelper.TABLE_FOOD, writableDatabase)) {
                    continue;
                }
                ContentValues values = BaseTools.createContentValues(item);
                long il = writableDatabase.insert(DataBaseOpenHelper.TABLE_FOOD, DataBaseOpenHelper.FOOD_DESCRIPTION, values);
                if (il != -1) {
                    count++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return count;
    }

    /**
     * 某条健康食物是否存在数据库
     *
     * @param id
     * @return
     */
    public boolean hasFoodListItem(int id) {
        return getFoodLiteItem(id) != null;
    }

    /**
     * 删除所有健康信息数据
     *
     * @return
     */
    public int clearFood() {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int c = 0;
        if (writableDatabase.isOpen()) {
            c = writableDatabase.delete(DataBaseOpenHelper.TABLE_FOOD, null, null);
            writableDatabase.close();
        }
        return c;
    }

    /**
     * 获取菜谱分类
     *
     * @return
     */
    public ArrayList<CookClassify> getCookClassifys() {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            ArrayList<CookClassify> data = new ArrayList<>();
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_COOK_CLASSIFY, null, null, null, null, null, null);
            if (query.moveToFirst()) {
                do {
                    CookClassify cookClassify = BaseTools.cursor2Object(CookClassify.class, query);
                    data.add(cookClassify);
                } while (query.moveToNext());
            }
            query.close();
            readableDatabase.close();
            return data;
        }


        return null;
    }

    /**
     * 写入菜谱分类数据
     *
     * @param datas
     * @return
     */
    public int putCookClassifys(List<CookClassify> datas) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int count = 0;
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();
            for (CookClassify item : datas) {
                if (exist((int) item.getId(), DataBaseOpenHelper.TABLE_COOK_CLASSIFY, writableDatabase)) {
                    continue;
                }
                ContentValues values = BaseTools.createContentValues(item);
                long o = writableDatabase.insert(DataBaseOpenHelper.TABLE_COOK_CLASSIFY, DataBaseOpenHelper.COOK_CLASSIFY_DESCRIPTION, values);
                if (o != -1) {
                    count++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return count;
    }


    /**
     * 获取一条健康菜谱
     *
     * @param id
     * @return
     */
    public CookListItem getCookListItem(int id) {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        CookListItem cookListItem = null;
        if (readableDatabase.isOpen()) {
            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_COOK, null, DataBaseOpenHelper.COOK_ID, new String[]{id + ""}, null, null, null, null);
            if (query.moveToFirst()) {
                cookListItem = BaseTools.cursor2Object(CookListItem.class, query);
            }
            query.close();
            readableDatabase.close();
        }
        return cookListItem;
    }

    /**
     * 获取多条菜谱
     *
     * @param count
     * @param page
     * @param id
     * @return
     */
    public ArrayList<CookListItem> getCookListItems(int count, int page, int id) {
        SQLiteDatabase readableDatabase = dboh.getReadableDatabase();
        if (readableDatabase.isOpen()) {
            ArrayList<CookListItem> datas = new ArrayList<>();

            Cursor query = readableDatabase.query(DataBaseOpenHelper.TABLE_COOK, null, DataBaseOpenHelper.COOK_CLASS + " = ? ", new String[]{id + ""}, null, null, DataBaseOpenHelper.COOK_ID + " ASC", (count * (page - 1)) + "," + count);
            try {
                if (query.moveToFirst()) {
                    do {
                        CookListItem cookListItem = BaseTools.cursor2Object(CookListItem.class, query);
                        datas.add(cookListItem);
                    } while (query.moveToNext());
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                query.close();
                readableDatabase.close();
                return null;
            } finally {
                query.close();
                readableDatabase.close();
            }
            return datas;
        }
        return null;
    }

    /**
     * 写入一条健康菜谱数据
     *
     * @param item
     * @return
     */
    public long putCookListItem(CookListItem item) {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        if (exist(item.getId(), DataBaseOpenHelper.TABLE_COOK, writableDatabase)) {
            writableDatabase.close();
            return 0L;
        }
        long l = 0L;
        if (writableDatabase.isOpen()) {
            l = writableDatabase.insert(DataBaseOpenHelper.TABLE_COOK, DataBaseOpenHelper.COOK_DESCRIPTION, BaseTools.createContentValues(item));
            writableDatabase.close();
        }
        return l;
    }

    /**
     * 写入多条健康数据
     *
     * @param items
     * @return
     */
    public synchronized int putCookListItems(List<CookListItem> items) {
        int count = 0;

        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        if (writableDatabase.isOpen()) {
            writableDatabase.beginTransaction();
            for (CookListItem item : items) {
                if (exist(item.getId(), DataBaseOpenHelper.TABLE_COOK, writableDatabase)) {
                    continue;
                }
                ContentValues values = BaseTools.createContentValues(item);
                long t = writableDatabase.insert(DataBaseOpenHelper.TABLE_COOK, DataBaseOpenHelper.COOK_DESCRIPTION, values);
                if (t != -1) {
                    count++;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return count;
    }

    /**
     * 判断某一条健康菜谱是否存在
     *
     * @param id
     * @return
     */
    public boolean hasCookListItem(int id) {
        return getCookListItem(id) != null;
    }

    /**
     * 删除所有健康信息数据
     *
     * @return
     */
    public int clearCook() {
        SQLiteDatabase writableDatabase = dboh.getWritableDatabase();
        int c = 0;
        if (writableDatabase.isOpen()) {
            c = writableDatabase.delete(DataBaseOpenHelper.TABLE_COOK, null, null);
            writableDatabase.close();
        }
        return c;
    }
    /**
     * 判断某一张表中是否存在某一条为id的数据
     *
     * @param id
     * @param table
     * @return
     */
    private synchronized boolean exist(int id, String table, SQLiteDatabase sqLiteDatabase) {
        String sql = "select * from " + table + " where id = ?";
        if (sqLiteDatabase.isOpen()) {
            Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{id + ""});
            boolean exist = cursor != null && cursor.getCount() == 1;
            if (cursor != null)
                cursor.close();
            return exist;
        }
        return true;
    }

    /**
     * 数据库打开帮助类
     */
    public static final class DataBaseOpenHelper extends SQLiteOpenHelper {
        /**
         * 本程序的数据库名称
         */
        static final String DATABASE_NAME = "data.db";

        /**
         * 表：资讯分类
         */
        static final String TABLE_INFO_CLASSIFY = "infoclassify";

        /**
         * 表：资讯详情
         */
        static final String TABLE_INFO = "info";

        /**
         * 表：健康知识分类
         */
        static final String TABLE_LORE_CLASSIFY = "loreclassify";

        /**
         * 表:健康知识详情
         */
        static final String TABLE_LORE = "lore";

        /**
         * 表:健康问答分类
         */
        static final String TABLE_ASK_CLASSIFY = "askclassify";

        /**
         * 表:健康问答详情
         */
        static final String TABLE_ASK = "ask";


        /**
         * 表：健康图书分类
         */
        static final String TABLE_BOOK_CLASSIFY = "bookclassify";

        /**
         * 表：健康图书详情
         */
        static final String TABLE_BOOK = "book";


        /**
         * 表：健康食物分类
         */
        static final String TABLE_FOOD_CLASSIFY = "foodclassify";

        /**
         * 表：食物详情
         */
        static final String TABLE_FOOD = "food";

        /**
         * 表:菜谱分类
         */
        static final String TABLE_COOK_CLASSIFY = "cookclassify";

        /**
         * 表:菜谱
         */
        static final String TABLE_COOK = "cook";

        /**
         * 字段：description<br/>
         * 该分类的描述<br/>
         * 存在于表：{@link #TABLE_INFO_CLASSIFY}
         */
        static final String INFO_CLASSIFY_DESCRIPTION = "description";
        /**
         * 字段：id<br/>
         * 该分类的id号<br/>
         * 存在于表：{@link #TABLE_INFO_CLASSIFY}
         */
        static final String INFO_CLASSIFY_ID = "id";
        /**
         * 字段：keywords<br/>
         * 该分类的关键字<br/>
         * 存在于表：{@link #TABLE_INFO_CLASSIFY}
         */
        static final String INFO_CLASSIFY_KEYWORDS = "keywords";

        /**
         * 字段：name<br/>
         * 该分类的名字<br/>
         * 存在于表：{@link #TABLE_INFO_CLASSIFY}
         */
        static final String INFO_CLASSIFY_NAME = "name";

        /**
         * 字段：seq<br/>
         * 该分类的排序位置<br/>
         * 存在于表：{@link #TABLE_INFO_CLASSIFY}
         */
        static final String INFO_CLASSIFY_SEQ = "seq";

        /**
         * 字段：title<br/>
         * 该分类的标题<br/>
         * 存在于表：{@link #TABLE_INFO_CLASSIFY}
         */
        static final String INFO_CLASSIFY_TITLE = "title";


        /**
         * 字段：description<br/>
         * 该资讯的描述<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_DESCRIPTION = INFO_CLASSIFY_DESCRIPTION;
        /**
         * 字段：count<br/>
         * 该资讯的访问人数<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_COUNT = "count";

        /**
         * 字段：id<br/>
         * 该资讯的ID号，#注意,这不是分类的id<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_ID = INFO_CLASSIFY_ID;

        /**
         * 字段：img<br/>
         * 该资讯的图片地址，为了得到该图片，你应该在这个地址前面添加：http://tnfs.tngou.net/image<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_IMG = "img";

        /**
         * 字段：fcount<br/>
         * 该资讯的收藏数量<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_FCOUNT = "fcount";

        /**
         * 字段：rocunt<br/>
         * 该资讯的评论数量<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_RCOUNT = "rcount";

        /**
         * 字段：infoclass<br/>
         * 该资讯的分类<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_INFOCLASSIFY = "infoclass";

        /**
         * 字段：keywords<br/>
         * 该资讯的关键字<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_KEYWORDS = INFO_CLASSIFY_KEYWORDS;

        /**
         * 字段：message<br/>
         * 该资讯的内容<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_MESSAGE = "message";

        /**
         * 字段：time<br/>
         * 该资讯的发布时间<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_TIME = "time";

        /**
         * 字段：title<br/>
         * 该资讯的标题<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_TITLE = INFO_CLASSIFY_TITLE;

        /**
         * 字段：url<br/>
         * 该资讯的网址链接<br/>
         * 存在于表：{@link #TABLE_INFO}
         */
        static final String INFO_URL = "url";


        /**
         * 字段：description<br/>
         * 该健康知识分类的描述<br/>
         * 存在于表：{@link #TABLE_LORE_CLASSIFY}
         */
        static final String LORE_CLASSIFY_DESCRIPTION = INFO_CLASSIFY_DESCRIPTION;

        /**
         * 字段：id<br/>
         * 该健康知识分类的id<br/>
         * 存在表：{@link #TABLE_LORE_CLASSIFY}
         */
        static final String LORE_CLASSIFY_ID = INFO_CLASSIFY_ID;

        /**
         * 字段：keywords<br/>
         * 该健康知识分类的关键字<br/>
         * 存在表：{@link #TABLE_LORE_CLASSIFY}
         */
        static final String LORE_CLASSIFY_KEYWORDS = INFO_CLASSIFY_KEYWORDS;

        /**
         * 字段: name<br/>
         * 该健康知识分类的名字<br/>
         * 存在表：{@link #TABLE_LORE_CLASSIFY}
         */
        static final String LORE_CLASSIFY_NAME = INFO_CLASSIFY_NAME;

        /**
         * 字段:seq<br/>
         * 该健康知识分类的名字<br/>
         * 存在表：{@link #TABLE_LORE_CLASSIFY}
         */
        static final String LORE_CLASSIFY_SEQ = INFO_CLASSIFY_SEQ;

        /**
         * 字段:title<br/>
         * 该健康知识分类的标题<br/>
         * 存在表：{@link #TABLE_LORE_CLASSIFY}
         */
        static final String LORE_CLASSIFY_TITLE = INFO_CLASSIFY_TITLE;


        /**
         * 字段:count<br/>
         * 该健康知识的访问数量<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_COUNT = INFO_COUNT;
        /**
         * 字段:description<br/>
         * 该健康知识的描述<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_DESCRIPTION = INFO_DESCRIPTION;
        /**
         * 字段:fcount<br/>
         * 该健康知识的收藏数量<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_FCOUNT = INFO_FCOUNT;
        /**
         * 字段:id<br/>
         * 该健康知识的ID号<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_ID = INFO_ID;
        /**
         * 字段:img<br/>
         * 该健康知识的图片地址<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_IMG = INFO_IMG;
        /**
         * 字段:keywords<br/>
         * 该健康知识的关键字<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_KEYWORDS = INFO_KEYWORDS;
        /**
         * 字段:loreclass<br/>
         * 该健康知识的分类<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_LORECLASSIFY = "loreclass";
        /**
         * 字段:message<br/>
         * 该健康知识的内容<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_MESSAGE = INFO_MESSAGE;
        /**
         * 字段:rcount<br/>
         * 该健康知识的评论量<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_RCOUNT = INFO_RCOUNT;
        /**
         * 字段:time<br/>
         * 该健康知识的发布时间<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_TIME = INFO_TIME;
        /**
         * 字段:title<br/>
         * 该健康知识的标题<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_TITLE = INFO_TITLE;
        /**
         * 字段:url<br/>
         * 该健康知识的连接地址<br/>
         * 存在于表: {@link #TABLE_LORE}
         */
        static final String LORE_URL = INFO_URL;

        /**
         * 字段:description<br/>
         * 问答分类描述<br/>
         * 存在于表:{@link #TABLE_ASK_CLASSIFY}
         */
        static final String ASK_CLASSIFY_DESCRIPTION = INFO_CLASSIFY_DESCRIPTION;
        /**
         * 字段:id<br/>
         * 问答分类id号<br/>
         * 存在于表:{@link #TABLE_ASK_CLASSIFY}
         */
        static final String ASK_CLASSIFY_ID = INFO_CLASSIFY_ID;
        /**
         * 字段:keywords<br/>
         * 问答分类关键字<br/>
         * 存在于表:{@link #TABLE_ASK_CLASSIFY}
         */
        static final String ASK_CLASSIFY_KEYWORDS = INFO_KEYWORDS;
        /**
         * 字段:name<br/>
         * 问答分类名称<br/>
         * 存在于表:{@link #TABLE_ASK_CLASSIFY}
         */
        static final String ASK_CLASSIFY_NAME = INFO_CLASSIFY_NAME;
        /**
         * 字段:seq<br/>
         * 问答分类排序<br/>
         * 存在于表:{@link #TABLE_ASK_CLASSIFY}
         */
        static final String ASK_CLASSIFY_SEQ = INFO_CLASSIFY_SEQ;
        /**
         * 字段:title<br/>
         * 问答分类标题<br/>
         * 存在于表:{@link #TABLE_ASK_CLASSIFY}
         */
        static final String ASK_CLASSIFY_TITLE = INFO_CLASSIFY_TITLE;

        /**
         * 字段:askclass<br/>
         * 该健康问答的分类<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_ASKCLASSIFY = "askclass";
        /**
         * 字段:count<br/>
         * 该健康问答的访问量<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_COUNT = INFO_COUNT;
        /**
         * 字段:description<br/>
         * 该健康问答的描述<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_DESCRIPTION = INFO_DESCRIPTION;
        /**
         * 字段:fcount<br/>
         * 该健康问答的收藏数量<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_FCOUNT = INFO_FCOUNT;
        /**
         * 字段:id<br/>
         * 该健康问答的id号<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_ID = INFO_ID;
        /**
         * 字段:img<br/>
         * 该健康问答的图片地址<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_IMG = INFO_IMG;
        /**
         * 字段:keywords<br/>
         * 该健康问答的关键字<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_KEYWORDS = INFO_KEYWORDS;
        /**
         * 字段:message<br/>
         * 该健康问答的内容<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_MESSAGE = INFO_MESSAGE;
        /**
         * 字段:rcount<br/>
         * 该健康问答的评论量<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_RCOUNT = INFO_RCOUNT;
        /**
         * 字段:time<br/>
         * 该健康问答的发布时间<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_TIME = INFO_TIME;
        /**
         * 字段:title<br/>
         * 该健康问答的标题<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_TITLE = INFO_TITLE;
        /**
         * 字段:url<br/>
         * 该健康问答的连接地址<br/>
         * 存在于表:{@link #TABLE_ASK}
         */
        static final String ASK_URL = INFO_URL;


        /**
         * 字段：description<br/>
         * 图书分类描述<br/>
         * 存在于表：{@link #TABLE_BOOK_CLASSIFY}
         */
        static final String BOOK_CLASSIFY_DESCRIPTION = INFO_CLASSIFY_DESCRIPTION;
        /**
         * 字段：id<br/>
         * 图书分类id<br/>
         * 存在于表：{@link #TABLE_BOOK_CLASSIFY}
         */
        static final String BOOK_CLASSIFY_ID = INFO_CLASSIFY_ID;
        /**
         * 字段：keywords<br/>
         * 图书分类关键字<br/>
         * 存在于表：{@link #TABLE_BOOK_CLASSIFY}
         */
        static final String BOOK_CLASSIFY_KEYWORDS = INFO_CLASSIFY_KEYWORDS;
        /**
         * 字段：name<br/>
         * 图书分类名字<br/>
         * 存在于表：{@link #TABLE_BOOK_CLASSIFY}
         */
        static final String BOOK_CLASSIFY_NAME = INFO_CLASSIFY_NAME;
        /**
         * 字段：seq<br/>
         * 图书分类排序<br/>
         * 存在于表：{@link #TABLE_BOOK_CLASSIFY}
         */
        static final String BOOK_CLASSIFY_SEQ = INFO_CLASSIFY_SEQ;

        /**
         * 字段：title<br/>
         * 图书分类标题<br/>
         * 存在于表：{@link #TABLE_BOOK_CLASSIFY}
         */
        static final String BOOK_CLASSIFY_TITLE = INFO_CLASSIFY_TITLE;


        /**
         * 字段：id<br/>
         * 图书的id<br/>
         * 存在于表：(@link {@link #TABLE_BOOK})
         */
        static final String BOOK_ID = ASK_ID;
        /**
         * 字段：name<br/>
         * 图书的名字<br/>
         * 存在于表：(@link {@link #TABLE_BOOK})
         */
        static final String BOOK_NAME = "name";
        /**
         * 字段：author<br/>
         * 图书的作者<br/>
         * 存在于表：(@link {@link #TABLE_BOOK})
         */
        static final String BOOK_AUTHOR = "author";
        /**
         * 字段：summary<br/>
         * 图书的简介<br/>
         * 存在于表：(@link {@link #TABLE_BOOK})
         */
        static final String BOOK_SUMMARY = "summary";
        /**
         * 字段：img<br/>
         * 图书的图片<br/>
         * 存在于表：(@link {@link #TABLE_BOOK})
         */
        static final String BOOK_IMG = "img";
        /**
         * 字段：bookclass<br/>
         * 图书的图书分类<br/>
         * 存在于表：(@link {@link #TABLE_BOOK})
         */
        static final String BOOK_BOOKCLASS = "bookclass";
        /**
         * 字段：count<br/>
         * 图书的访问量<br/>
         * 存在于表：(@link {@link #TABLE_BOOK})
         */
        static final String BOOK_COUNT = "count";
        /**
         * 字段：rcount<br/>
         * 图书的评论量<br/>
         * 存在于表：(@link {@link #TABLE_BOOK})
         */
        static final String BOOK_RCOUNT = "rcount";
        /**
         * 字段：fcount<br/>
         * 图书的收藏量<br/>
         * 存在于表：(@link {@link #TABLE_BOOK})
         */
        static final String BOOK_FCOUNT = "fcount";
        /**
         * 字段：time<br/>
         * 图书的发表时间<br/>
         * 存在于表：(@link {@link #TABLE_BOOK})
         */
        static final String BOOK_TIME = "time";
        /**
         * 字段：list,其内容如下<br/>
         * "list":[{"book":1,"id":100,"message":"","seq":0:"title":""},{}]<br/>
         * 图书的章节类容<br/>
         * 存在于表：(@link {@link #TABLE_BOOK})
         */
        static final String BOOK_LIST = "list";

        /**
         * 字段：description<br/>
         * 食物分类描述<br/>
         * 存在于表：{@link #TABLE_FOOD_CLASSIFY}
         */
        static final String FOOD_CLASSIFY_DESCRIPTION = "description";
        /**
         * 字段：foodclass<br/>
         * 食物食物分类分类<br/>
         * 存在于表：{@link #TABLE_FOOD_CLASSIFY}
         */
        @Deprecated
        static final String FOOD_CLASSIFY_FOODCLASS = "foodclass";
        /**
         * 字段：id<br/>
         * 食物分类id<br/>
         * 存在于表：{@link #TABLE_FOOD_CLASSIFY}
         */
        static final String FOOD_CLASSIFY_ID = "id";
        /**
         * 字段：keywords<br/>
         * 食物分类关键字<br/>
         * 存在于表：{@link #TABLE_FOOD_CLASSIFY}
         */
        static final String FOOD_CLASSIFY_KEYWORDS = "keywords";
        /**
         * 字段：name<br/>
         * 食物分类名字<br/>
         * 存在于表：{@link #TABLE_FOOD_CLASSIFY}
         */
        static final String FOOD_CLASSIFY_NAME = "name";
        /**
         * 字段：seq<br/>
         * 食物分类排序<br/>
         * 存在于表：{@link #TABLE_FOOD_CLASSIFY}
         */
        static final String FOOD_CLASSIFY_SEQ = "seq";
        /**
         * 字段：title<br/>
         * 食物分类标题<br/>
         * 存在于表：{@link #TABLE_FOOD_CLASSIFY}
         */
        static final String FOOD_CLASSIFY_TITLE = "title";


        /**
         * 字段：count<br/>
         * 食物访问数<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_COUNT = "count";
        /**
         * 字段：description<br/>
         * 食物描述<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_DESCRIPTION = "description";
        /**
         * 字段：disease<br/>
         * 食物的相关疾病<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_DISEASE = "disease";
        /**
         * 字段：fcount<br/>
         * 食物收藏数<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_FCOUNT = "fcount";
        /**
         * 字段：food<br/>
         * 食物的相关食物<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_FOOD = "food";
        /**
         * 字段：id<br/>
         * 食物的id<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_ID = "id";
        /**
         * 字段：img<br/>
         * 食物图片<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_IMG = "img";
        /**
         * 字段：keywords<br/>
         * 食物的关键字<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_KEYWORDS = "keywords";
        /**
         * 字段：message<br/>
         * 食物详情<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_MESSAGE = "message";
        /**
         * 字段：name<br/>
         * 食物名字<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_NAME = "name";
        /**
         * 字段：rcount<br/>
         * 食物的评论数<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_RCOUNT = "rcount";
        /**
         * 字段：summary<br/>
         * 食物简介<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_SUMMARY = "summary";
        @Deprecated
        static final String FOOD_SYMPTOM = "symptom";
        /**
         * 字段：url<br/>
         * 食物的连接<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_URL = "url";
        /**
         * 字段：foodclass<br/>
         * 食物的所属分类<br/>
         * 存在于表：{@link #TABLE_FOOD}
         */
        static final String FOOD_CLASS = "foodclass";


        @Deprecated
        static final String COOK_CLASSIFY_COOKCLASS = "cookclass";
        /**
         * 字段:description<br/>
         * 菜谱的分类描述<br/>
         * 存在于表:{@link #TABLE_COOK_CLASSIFY}
         */
        static final String COOK_CLASSIFY_DESCRIPTION = "description";
        /**
         * 字段:id<br/>
         * 菜谱的分类id<br/>
         * 存在于表:{@link #TABLE_COOK_CLASSIFY}
         */
        static final String COOK_CLASSIFY_ID = "id";
        /**
         * 字段:keywords<br/>
         * 菜谱的分类关键字<br/>
         * 存在于表:{@link #TABLE_COOK_CLASSIFY}
         */
        static final String COOK_CLASSIFY_KEYWORDS = "keywords";
        /**
         * 字段:name<br/>
         * 菜谱的分类名字<br/>
         * 存在于表:{@link #TABLE_COOK_CLASSIFY}
         */
        static final String COOK_CLASSIFY_NAME = "name";
        /**
         * 字段:seq<br/>
         * 菜谱的分类排序<br/>
         * 存在于表:{@link #TABLE_COOK_CLASSIFY}
         */
        static final String COOK_CLASSIFY_SEQ = "seq";
        /**
         * 字段:title<br/>
         * 菜谱的分类名称<br/>
         * 存在于表:{@link #TABLE_COOK_CLASSIFY}
         */
        static final String COOK_CLASSIFY_TITLE = "title";

        /**
         * 字段:count<br/>
         * 菜谱的访问数<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_COUNT = "count";
        /**
         * 字段:description<br/>
         * 菜谱的描述<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_DESCRIPTION = "description";
        /**
         * 字段:fcount<br/>
         * 菜谱的收藏数<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_FCOUNT = "fcount";
        /**
         * 字段:food<br/>
         * 菜谱的相关食物<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_FOOD = "food";
        /**
         * 字段:id<br/>
         * 菜谱的ID号<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_ID = "id";
        /**
         * 字段:images<br/>
         * 菜谱的图,多张图用逗号隔开<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_IMAGES = "images";
        /**
         * 字段:img<br/>
         * 菜谱的图片<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_IMG = "img";
        /**
         * 字段:keywords<br/>
         * 菜谱的关键字<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_KEYWORDS = "keywords";
        /**
         * 字段:message<br/>
         * 菜谱的内容<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_MESSAGE = "message";
        /**
         * 字段:name<br/>
         * 菜谱的名字<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_NAME = "name";
        /**
         * 字段:rcount<br/>
         * 菜谱的评论数<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_RCOUNT = "rcount";
        /**
         * 字段:url<br/>
         * 菜谱的链接地址<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_URL = "url";
        /**
         * 字段:cookclass<br/>
         * 菜谱的所属分类<br/>
         * 存在于表:{@link #TABLE_COOK}
         */
        static final String COOK_CLASS = "cookclass";


        public DataBaseOpenHelper(Context context, int version) {
            super(context, DATABASE_NAME, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sqlTableInfoClassify = BaseTools.createTable(TABLE_INFO_CLASSIFY,
                    g(INFO_CLASSIFY_DESCRIPTION, FieldType.VARCHAR),
                    g(INFO_CLASSIFY_ID, FieldType.INTEGER),
                    g(INFO_CLASSIFY_KEYWORDS, FieldType.VARCHAR),
                    g(INFO_CLASSIFY_NAME, FieldType.VARCHAR),
                    g(INFO_CLASSIFY_SEQ, FieldType.INTEGER),
                    g(INFO_CLASSIFY_TITLE, FieldType.VARCHAR));
            db.execSQL(sqlTableInfoClassify);//创建资讯分类表

            String sqlTableInfo = BaseTools.createTable(TABLE_INFO,
                    g(INFO_COUNT, FieldType.INTEGER),
                    g(INFO_DESCRIPTION, FieldType.VARCHAR),
                    g(INFO_FCOUNT, FieldType.INTEGER),
                    g(INFO_ID, FieldType.INTEGER),
                    g(INFO_IMG, FieldType.VARCHAR),
                    g(INFO_INFOCLASSIFY, FieldType.INTEGER),
                    g(INFO_KEYWORDS, FieldType.VARCHAR),
                    g(INFO_MESSAGE, FieldType.TEXT),
                    g(INFO_RCOUNT, FieldType.INTEGER),
                    g(INFO_TIME, FieldType.LONG),
                    g(INFO_TITLE, FieldType.VARCHAR),
                    g(INFO_URL, FieldType.VARCHAR));
            db.execSQL(sqlTableInfo);//创建资讯详情表

            String sqlTableLoreClassify = BaseTools.createTable(TABLE_LORE_CLASSIFY,
                    g(LORE_CLASSIFY_DESCRIPTION, FieldType.VARCHAR),
                    g(LORE_CLASSIFY_ID, FieldType.INTEGER),
                    g(LORE_CLASSIFY_KEYWORDS, FieldType.VARCHAR),
                    g(LORE_CLASSIFY_NAME, FieldType.VARCHAR),
                    g(LORE_CLASSIFY_SEQ, FieldType.INTEGER),
                    g(LORE_CLASSIFY_TITLE, FieldType.VARCHAR));
            db.execSQL(sqlTableLoreClassify);//创建健康知识表

            String sqlTableLore = BaseTools.createTable(TABLE_LORE,
                    g(LORE_COUNT, FieldType.INTEGER),
                    g(LORE_DESCRIPTION, FieldType.VARCHAR),
                    g(LORE_FCOUNT, FieldType.INTEGER),
                    g(LORE_ID, FieldType.INTEGER),
                    g(LORE_IMG, FieldType.VARCHAR),
                    g(LORE_KEYWORDS, FieldType.VARCHAR),
                    g(LORE_LORECLASSIFY, FieldType.INTEGER),
                    g(LORE_MESSAGE, FieldType.TEXT),
                    g(LORE_RCOUNT, FieldType.INTEGER),
                    g(LORE_TIME, FieldType.LONG),
                    g(LORE_TITLE, FieldType.VARCHAR),
                    g(LORE_URL, FieldType.VARCHAR));
            db.execSQL(sqlTableLore);//创建健康知识详情表

            String sqlTableAskClassify = BaseTools.createTable(TABLE_ASK_CLASSIFY,
                    g(ASK_CLASSIFY_DESCRIPTION, FieldType.VARCHAR),
                    g(ASK_CLASSIFY_ID, FieldType.INTEGER),
                    g(ASK_CLASSIFY_KEYWORDS, FieldType.VARCHAR),
                    g(ASK_CLASSIFY_NAME, FieldType.VARCHAR),
                    g(ASK_CLASSIFY_SEQ, FieldType.INTEGER),
                    g(ASK_CLASSIFY_TITLE, FieldType.VARCHAR));
            db.execSQL(sqlTableAskClassify);//创建健康问答分类表

            String sqlTableAsk = BaseTools.createTable(TABLE_ASK,
                    g(ASK_ASKCLASSIFY, FieldType.INTEGER),
                    g(ASK_COUNT, FieldType.INTEGER),
                    g(ASK_DESCRIPTION, FieldType.VARCHAR),
                    g(ASK_FCOUNT, FieldType.INTEGER),
                    g(ASK_ID, FieldType.INTEGER),
                    g(ASK_IMG, FieldType.VARCHAR),
                    g(ASK_KEYWORDS, FieldType.VARCHAR),
                    g(ASK_MESSAGE, FieldType.TEXT),
                    g(ASK_RCOUNT, FieldType.INTEGER),
                    g(ASK_TIME, FieldType.LONG),
                    g(ASK_TITLE, FieldType.VARCHAR),
                    g(ASK_URL, FieldType.VARCHAR));
            db.execSQL(sqlTableAsk);//创建健康问答详情表

            String sqlTableBooklClassify = BaseTools.createTable(TABLE_BOOK_CLASSIFY,
                    g(BOOK_CLASSIFY_DESCRIPTION, FieldType.VARCHAR),
                    g(BOOK_CLASSIFY_ID, FieldType.INTEGER),
                    g(BOOK_CLASSIFY_KEYWORDS, FieldType.VARCHAR),
                    g(BOOK_CLASSIFY_NAME, FieldType.VARCHAR),
                    g(BOOK_CLASSIFY_SEQ, FieldType.INTEGER),
                    g(BOOK_CLASSIFY_TITLE, FieldType.VARCHAR));
            db.execSQL(sqlTableBooklClassify);//创建健康图书分类表

            String sqlTableBook = BaseTools.createTable(TABLE_BOOK,
                    g(BOOK_NAME, FieldType.VARCHAR),
                    g(BOOK_AUTHOR, FieldType.VARCHAR),
                    g(BOOK_BOOKCLASS, FieldType.INTEGER),
                    g(BOOK_COUNT, FieldType.INTEGER),
                    g(BOOK_ID, FieldType.INTEGER),
                    g(BOOK_IMG, FieldType.VARCHAR),
                    g(BOOK_LIST, FieldType.BLOB),
                    g(BOOK_RCOUNT, FieldType.INTEGER),
                    g(BOOK_FCOUNT, FieldType.INTEGER),
                    g(BOOK_SUMMARY, FieldType.VARCHAR),
                    g(BOOK_TIME, FieldType.LONG));
            db.execSQL(sqlTableBook);  //创建健康图书详情表

            String sqlTableFoodClassify = BaseTools.createTable(TABLE_FOOD_CLASSIFY,
                    g(FOOD_CLASSIFY_DESCRIPTION, FieldType.VARCHAR),
                    g(FOOD_CLASSIFY_ID, FieldType.INTEGER),
                    g(FOOD_CLASSIFY_KEYWORDS, FieldType.VARCHAR),
                    g(FOOD_CLASSIFY_NAME, FieldType.VARCHAR),
                    g(FOOD_CLASSIFY_SEQ, FieldType.INTEGER),
                    g(FOOD_CLASSIFY_TITLE, FieldType.VARCHAR),
                    g(FOOD_CLASSIFY_FOODCLASS, FieldType.INTEGER));
            db.execSQL(sqlTableFoodClassify);//创建健康食物分类表

            String sqlTableFood = BaseTools.createTable(TABLE_FOOD,
                    g(FOOD_COUNT, FieldType.INTEGER),
                    g(FOOD_DESCRIPTION, FieldType.VARCHAR),
                    g(FOOD_DISEASE, FieldType.VARCHAR),
                    g(FOOD_FCOUNT, FieldType.INTEGER),
                    g(FOOD_FOOD, FieldType.VARCHAR),
                    g(FOOD_ID, FieldType.INTEGER),
                    g(FOOD_IMG, FieldType.VARCHAR),
                    g(FOOD_KEYWORDS, FieldType.VARCHAR),
                    g(FOOD_MESSAGE, FieldType.TEXT),
                    g(FOOD_NAME, FieldType.VARCHAR),
                    g(FOOD_CLASS, FieldType.INTEGER),
                    g(FOOD_RCOUNT, FieldType.INTEGER),
                    g(FOOD_SUMMARY, FieldType.VARCHAR),
                    g(FOOD_SYMPTOM, FieldType.VARCHAR),
                    g(FOOD_URL, FieldType.VARCHAR));
            db.execSQL(sqlTableFood);//创建食物详情表

            String sqlTableCookClassify = BaseTools.createTable(TABLE_COOK_CLASSIFY,
                    g(COOK_CLASSIFY_DESCRIPTION, FieldType.VARCHAR),
                    g(COOK_CLASSIFY_ID, FieldType.INTEGER),
                    g(COOK_CLASSIFY_KEYWORDS, FieldType.VARCHAR),
                    g(COOK_CLASSIFY_NAME, FieldType.VARCHAR),
                    g(COOK_CLASSIFY_TITLE, FieldType.VARCHAR),
                    g(COOK_CLASSIFY_SEQ, FieldType.INTEGER),
                    g(COOK_CLASSIFY_COOKCLASS, FieldType.INTEGER));
            db.execSQL(sqlTableCookClassify);//创建菜谱分类表

            String sqlTableCook = BaseTools.createTable(TABLE_COOK,
                    g(COOK_COUNT, FieldType.INTEGER),
                    g(COOK_FCOUNT, FieldType.INTEGER),
                    g(COOK_NAME, FieldType.VARCHAR),
                    g(COOK_ID, FieldType.INTEGER),
                    g(COOK_RCOUNT, FieldType.INTEGER),
                    g(COOK_IMAGES, FieldType.VARCHAR),
                    g(COOK_CLASS, FieldType.INTEGER),
                    g(COOK_IMG, FieldType.VARCHAR),
                    g(COOK_KEYWORDS, FieldType.VARCHAR),
                    g(COOK_MESSAGE, FieldType.TEXT),
                    g(COOK_FOOD, FieldType.VARCHAR),
                    g(COOK_DESCRIPTION, FieldType.VARCHAR),
                    g(COOK_URL, FieldType.VARCHAR));
            db.execSQL(sqlTableCook);//创建菜谱表

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        /**
         * 在字段和字段类型中添加逗号
         *
         * @param flags
         * @return
         */
        private String g(String... flags) {
            return flags[0] + "," + flags[1];
        }

    }

    /**
     * 数据库数据类型类
     */
    static final class FieldType {
        public static final String VARCHAR = "varchar";
        public static final String NVARCHAR = "nvarchar";
        public static final String TEXT = "text";//值是一个文本字符串，使用数据库编码（UTF-8、UTF-16BE 或 UTF-16LE）存储。
        public static final String INTEGER = "integer";//值是一个带符号的整数，根据值的大小存储在 1、2、3、4、6 或 8 字节中。
        public static final String FLOAT = "float";
        public static final String BOOLEAN = "boolean";
        public static final String CLOB = "clob";
        public static final String BLOB = "blob";//值是一个 blob 数据，完全根据它的输入存储。
        public static final String TIMESTAMP = "timestamp";
        public static final String VARYING = "varying";
        public static final String CHARACTER = "character";
        public static final String REAL = "real";//值是一个浮点值，存储为 8 字节的 IEEE 浮点数字。
        public static final String SMALLINT = "smallint";
        public static final String NULL = "null";//值是一个 NULL 值。
        public static final String NONE = "none";//
        public static final String NUMERIC = "numeric";//NUMERIC列可以使用任何存储类型,它首先试图将插入的数据转换为REAL或INTEGER型的,如果成功则存储为REAL和INTEGER型,否则不加改变的存入.
        public static final String SHORT = "short";
        public static final String DOUBLE = "double ";
        public static final String LONG = "long";
    }
}
