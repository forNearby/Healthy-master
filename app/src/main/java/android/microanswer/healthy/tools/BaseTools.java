package android.microanswer.healthy.tools;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基本工具类
 * Created by Micro on 2016/6/17.
 */

public class BaseTools {


    /**
     * 获取屏幕对角尺寸
     *
     * @param ctx
     * @return
     */
    public static double getScreenPhysicalSize(Context ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
        return diagonalPixels / (160 * dm.density);
    }

    /**
     * 获取文字宽度
     *
     * @param text
     * @param Size
     * @return
     */
    public static float GetTextWidth(String text, float Size) {
        TextPaint FontPaint = new TextPaint();
        FontPaint.setTextSize(Size);
        return FontPaint.measureText(text);
    }

    /**
     * 判断某字符串是否包含汉字
     *
     * @param sequence
     * @return
     */
    public static boolean checkChinese(String sequence) {
        final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
        boolean result = false;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        result = matcher.find();
        return result;
    }

    /**
     * 使用渐变的方式在ImageView里面显示这个Bitmap
     *
     * @param imageView
     * @param bitmap
     * @param context
     */
    private static void setImageBitmap(ImageView imageView, Bitmap bitmap, Context context) {
        // Use TransitionDrawable to fade in.
        final TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(context.getResources().getColor(android.R.color.transparent)), new BitmapDrawable(context.getResources(), bitmap)});
        //noinspection deprecation
        imageView.setBackgroundDrawable(imageView.getDrawable());
        imageView.setImageDrawable(td);
        td.startTransition(200);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


    /**
     * 判断手机是否有能力处理某个action
     *
     * @param context
     * @param action
     * @return
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * make true current connect service is wifi
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    /**
     * 打印一个Cursor中所有的字段
     *
     * @param cursor
     * @return
     */
    public static String[] printCursorColumNames(String logTag, Cursor cursor) {
        int columnCount = cursor.getColumnCount();
        String[] names = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            names[i] = cursor.getColumnName(i);
        }
        Log.i(logTag, Arrays.toString(names));
        return names;
    }


    /**
     * 从数据库的查询结果中取得一个对象,[#在传入cursor之前,你应该先指定好其中的position]
     *
     * @param cursor
     * @return
     */
    public static <T> T cursor2Object(Class<T> clazz, Cursor cursor) {
        try {
            T t = clazz.newInstance();
//            printCursorColumNames("从数据库", cursor);
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                String fieldName = f.getName();
                Class type = f.getType();
                try {
                    if (type.getSimpleName().equals("String")) {
                        String fieldValue = cursor.getString(cursor.getColumnIndexOrThrow(fieldName));
                        f.set(t, fieldValue);
                    } else if (type.getSimpleName().equals("Integer") || type.getSimpleName().equals("int")) {
                        int fieldValues = cursor.getInt(cursor.getColumnIndexOrThrow(fieldName));
                        f.set(t, fieldValues);
                    } else if (type.getSimpleName().equals("Long") || type.getSimpleName().equals("long")) {
                        long fieldValues = cursor.getLong(cursor.getColumnIndexOrThrow(fieldName));
                        f.set(t, fieldValues);
                    } else if (type.getSimpleName().contains("List")) {
                        f.set(t, byteArray2object(cursor.getBlob(cursor.getColumnIndexOrThrow(fieldName))));
                    }
                } catch (Exception e) {
                    System.err.print("出错字段:" + fieldName + " |----| " + e.toString());
                }
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字节数组反序列化成对象
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> T byteArray2object(byte[] data) {
        if (data == null) {
            return null;
        }

        T t = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(data);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            t = (T) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        return t;
    }

    /**
     * 将对象序列化为字节数组<br/>
     *
     * @param bean 要进行序列化的对象,该对象应该实现Serializable接口
     * @return 序列化结果的字节数组
     */
    public static byte[] object2byteArray(Object bean) {
        byte[] data = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(bean);
            objectOutputStream.flush();
            data = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null)
                    objectOutputStream.close();
                if (byteArrayOutputStream != null)
                    byteArrayOutputStream.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 将对象转变为可以写入数据库的Values
     *
     * @param key  写入数据库时的字段名
     * @param bean 具体内容
     */
    public static ContentValues createBlobContentValues(String key, Object bean) {
        ContentValues values = new ContentValues();
        values.put(key, object2byteArray(bean));
        return values;
    }

    /**
     * 创建一个对应bean的可以用于写入数据库的ContentValues
     *
     * @param bean
     * @return
     */
    public static ContentValues createContentValues(Object bean) {

        ContentValues values = new ContentValues();
        try {
            Class<?> aClass = bean.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();//取得该Bean的所有字段
            for (Field f : declaredFields) {
                f.setAccessible(true);
                String fieldName = f.getName();
                Object fieldValues = f.get(bean);
                if (fieldValues instanceof String) {
                    values.put(fieldName, fieldValues + "");
                } else if (fieldValues instanceof Integer) {
                    values.put(fieldName, (int) fieldValues);
                } else if (fieldValues instanceof Long) {
                    values.put(fieldName, (long) fieldValues);
                } else if (fieldValues instanceof ArrayList) {
                    values.put(fieldName, object2byteArray(fieldValues));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    /**
     * 帮你整理一个可以创建你指定的键的表的可执行的SQL语句
     *
     * @param tableName 要创建的表名
     * @param fields    表中的字段属性（"name,varchar"）
     * @return 该表的可执行语句
     */
    public static String createTable(String tableName, String... fields) {
        StringBuilder baseSql = new StringBuilder("create table " + tableName + "( _id integer primary key autoincrement,");
        String baseSqlEnd = ");";
        for (int i = 0; i < fields.length; i++) {
            String item = fields[i];
            String s[] = item.split(",");
            String s1 = s[0];
            String s2 = s[1];
            baseSql.append(s1).append(" ").append(s2).append(i == fields.length - 1 ? "" : ",");
        }
        return baseSql.append(baseSqlEnd).toString();
    }

    /**
     * 获取当前正在运行的Activiy所在的程序的名称
     *
     * @param context 上下文
     * @author MicroAnswer
     * @return 正在运行的程序名字
     */
    public static String getCurrentRunningActivityName(Context context) {
        String name = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses != null && runningAppProcesses.size() > 0) {
            String runningPackageName = runningAppProcesses.get(0).processName;
            PackageManager packageManager = context.getPackageManager();
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(runningPackageName, 0);
                name = applicationInfo.loadLabel(packageManager).toString();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return name;
    }

    /**
     * 图片高斯模糊
     */
    public static Bitmap doBlur2(Bitmap bitmap) {
        return BlurTool.BoxBlurFilter(bitmap);
    }


    /**
     * 图片高斯模糊
     *
     * @param bitmap           要模糊的图片
     * @param radius           模糊半径
     * @param blurAlphaChannel 是否包含透明度
     * @return 模糊结果
     */
    public static Bitmap doBlur(Bitmap bitmap, int radius, boolean blurAlphaChannel) {
        return BlurTool.stackBlurImage(bitmap, radius, blurAlphaChannel);
    }

    /**
     * 图片高斯模糊
     *
     * @param sentBitmap
     * @param radius
     * @return
     */
    @Deprecated
    public static Bitmap doBlur(Bitmap sentBitmap, int radius) {
        boolean canReuseInBitmap = false;

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
