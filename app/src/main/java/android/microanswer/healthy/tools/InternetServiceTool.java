package android.microanswer.healthy.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static android.R.attr.data;

/**
 * 该类里面是一些简单的从网络获取的信息
 *
 * @author Micro
 */
public class InternetServiceTool {

    private static final String TAG = "InternetServiceTool";

    /**
     * 天气
     *
     * @author Micro
     */
    public static final class Weather {

        /**
         * 获取当前的天气预报信息<br/>
         * {@linkplain @http://apistore.baidu.com/apiworks/servicedetail/478.html}
         *
         * @param cityName 城市名字【国内的支持中英文，国外支持英文】
         * @return
         */
        public static final String get(String cityName) {
            String httpUrl = "http://apis.baidu.com/heweather/weather/free";
            String httpArg = "city=" + cityName;
            return baiduApiRequest(httpUrl, httpArg, "GET");
        }

    }

    /**
     * 历史上的今天
     *
     * @author Micro
     */
    public static class HistoryToday {

        /**
         * 获取指定日期在历史上发生的大事件
         */
        public static String get(int month, int day, int page, int count) {
            String httpArg = "";
            if (count > 50) {
                count = 50;
            }
            httpArg = "yue=" + month + "&ri=" + day + "&type=1&page=1&rows=" + count + "&dtype=JOSN&format=false";
            return baiduApiRequest("http://apis.baidu.com/avatardata/historytoday/lookup", httpArg, "GET");
        }
    }

    private static final String baiduApiRequest(String httpUrl, String httpArg, String requestMethod) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("apikey", ApiKey.BAIDU_APIKEY);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
//				sbf.append(System.lineSeparator());
            }
            result = sbf.toString();
        } catch (Exception e) {
            result = "error:" + e.toString();
        } finally {
            try {
                reader.close();
            } catch (Exception e2) {
                result = "error:" + e2.toString();
            }
        }
        return result;
    }


    /**
     * 【POST】请求
     * 请求一个网址，返回一个结果
     * 一个参数以数组表示，数组第一个是参数名，第二个是参数值
     *
     * @param urla    网址
     * @param parmars 参数
     * @return
     */
    public static final String request(String urla, ArrayList<String[]> parmars) {
        String result = "";
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urla);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("GET");
            if (parmars != null)
                for (String[] ps : parmars) {
                    httpURLConnection.addRequestProperty(ps[0], ps[1]);
                }
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            StringBuffer sbf = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sbf.append(line);
            }
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            result = "error-->[" + e.toString() + "]";
        } finally {
            try {
                if (inputStreamReader != null)
                    inputStreamReader.close();
            } catch (Exception e2) {
                e2.printStackTrace();
                result = "error-->[" + e2.toString() + "]";
            }
        }
        return result;
    }

    @Deprecated
    public static final String requestPost(String urlm, Map parma) {
        String result = null;
        URL url = null;
        HttpURLConnection urlConnection = null;
        InputStreamReader inputstreamreader = null;
        BufferedReader buffreader = null;
        try {
            url = new URL(urlm);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            if (parma != null) {
                Set<Map.Entry> entries = parma.entrySet();
                for (Map.Entry entry : entries) {
                    urlConnection.addRequestProperty(entry.getKey()+"", ""+entry.getValue());
                }
            }

            urlConnection.connect();
            inputstreamreader = new InputStreamReader(urlConnection.getInputStream());
            buffreader = new BufferedReader(inputstreamreader);

            StringBuilder sbu = new StringBuilder();
            String temp = null;

            while ((temp = buffreader.readLine()) != null) {
                sbu.append(temp);
            }
            result = sbu.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffreader != null) {
                    buffreader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 请求一个网址，返回一个结果
     *
     * @param urla
     * @return 文字的结果
     */
    public static final String request(String urla) {
        String result = "";
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urla);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(30000);
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            StringBuffer sbf = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sbf.append(line);
            }
            result = sbf.toString();
        } catch (Exception e) {
            result = "error-->[" + e.toString() + "]";
        } finally {
            try {
                if (inputStreamReader != null)
                    inputStreamReader.close();
            } catch (Exception e2) {
                result = "error-->[" + e2.toString() + "]";
            }
        }
        return result;
    }



    /**
     * 上传头像图片到Tngou服务器
     *
     * @param photofile
     *            要上传的头像图片
     * @return 头像图片上传完成后服务器返回的信息，上传异常将返回Null
     */
    public static String upLoadPhoto(File photofile) {
        String murl = "http://www.tngou.net/tnfs/action/controller?action=uploadimage&path=avatar";
        return upLoadPhoto(murl, photofile);
    }


    public static String upLoadPhoto(String murl, File photofile) {

        String serverPath;

        if (murl.contains("=")) {
            serverPath = murl.substring(murl.lastIndexOf("=") + 1);
        } else {
            serverPath = photofile.getName();
        }

        String end = "\r\n";
        String flagTag = "--";
        String boundary = "----" + UUID.randomUUID().toString();
        try {
            URL url = new URL(murl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // 先构建非文件部分上部
            StringBuilder sbu = new StringBuilder();
            sbu.append(flagTag + boundary + end);
            sbu.append("Content-Disposition: form-data; name=\"path\"" + end);
            sbu.append(end);
            sbu.append(serverPath + end);
            sbu.append(flagTag + boundary + end);
            sbu.append(
                    "Content-Disposition: form-data; name=\"upfile\"; filename=\"" + photofile.getName() + "\"" + end);
            sbu.append("Content-Type: image/*" + end);
            sbu.append(end);

            // 非文件部分下部
            StringBuffer sbu2 = new StringBuffer();
            sbu2.append(end + flagTag + boundary + flagTag + end);

            con.setRequestProperty("Content-Length", String.valueOf(
                    sbu.toString().getBytes().length + sbu2.toString().getBytes().length + photofile.length()));

            FileInputStream fin = new FileInputStream(photofile);
            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sbu.toString().getBytes("UTF-8"));// 上传模拟表单上部
            byte data[] = new byte[1024];
            int datasize = 0;
            while ((datasize = fin.read(data)) != -1) {// 上传图片数据
                outputStream.write(data, 0, datasize);
            }
            outputStream.write(sbu2.toString().getBytes("UTF-8"));// 上传模拟表单下部

            outputStream.flush();
            fin.close();

            // 获取服务器响应
            InputStream is = con.getInputStream();

            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bfrd = new BufferedReader(inputStreamReader);
            StringBuffer sbf = new StringBuffer();
            String temp = null;
            while ((temp = bfrd.readLine()) != null) {
                sbf.append(temp);
            }
            bfrd.close();
            outputStream.close();
            return sbf.toString();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }


}