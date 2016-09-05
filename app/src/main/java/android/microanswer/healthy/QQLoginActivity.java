package android.microanswer.healthy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * QQ第三方登录界面
 * 由 Micro 创建于 2016/7/30.
 */

public class QQLoginActivity extends BaseActivity {
    public static final int LOGINOK = 11112;

    private final String url = "http://www.tngou.net/api/oauth2/open/qq";
    private WebView webView;
    WebViewClient mc = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            super.shouldOverrideUrlLoading(view, request);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:window.handler.show(document.body.innerText);");
            super.onPageFinished(view, url);
        }
    };

    private class LoginWatcher {
        @JavascriptInterface
        public void show(String data) {
            if (data.startsWith("{") && data.endsWith("}")) {
                JSONObject jsonObject = JSON.parseObject(data);
                if (jsonObject.getBooleanValue("status")) {
                    Intent intent = new Intent();
                    intent.putExtra("code", jsonObject.getString("code"));
                    setResult(LOGINOK, intent);
                    finish();
                }
            }
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqlogin);
        suitToolBar(R.id.activity_qqlogin_toolbar);
        setToolBarBackEnable();

        webView = (WebView) findViewById(R.id.activity_qqlogin_webview);

        WebSettings settings = webView.getSettings();
        settings.setSaveFormData(true);
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setSupportZoom(true);
        webView.setWebViewClient(mc);
        webView.setWebChromeClient(new WebChromeClient());

        webView.addJavascriptInterface(new LoginWatcher(), "handler");

        String address = url + "?client_id=" + client_id + "&redirect_uri=" + redirect_uri;
        webView.loadUrl(address);
    }


    @Override
    protected boolean onHomeButtonClick() {
        onBackPressed();
        return true;
    }
}
