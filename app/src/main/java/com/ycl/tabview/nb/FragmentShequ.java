package com.ycl.tabview.nb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ycl.tabview.R;
import com.ycl.tabview.SimpleCaptureActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentShequ extends Fragment {

    View mainView = null;

    @Bind(R.id.web_shequ)
    WebView mWeb;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 5:
                    webViewGoBack();
                    break;
            }

        }
    };
    private void webViewGoBack() {
        mWeb.goBack();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.fragment_shequ, container, false);
        }
        ButterKnife.bind(this, mainView);
        WebSettings settings = mWeb.getSettings();


        settings.setJavaScriptEnabled(true);
        mWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWeb.getSettings().setSupportZoom(true);  //支持放大缩小
        mWeb.getSettings().setBuiltInZoomControls(true);
        mWeb.loadUrl("http://3.1budai.com/mobile/catalog.php");
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWeb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWeb.clearHistory();
        mWeb.clearFormData();
        mWeb.clearCache(true);

        mWeb.setWebViewClient(new WebViewClient());
        mWeb.addJavascriptInterface(new JavaScriptInterface(), "JSInterface");

        mWeb.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && mWeb.canGoBack()) {
                    handler.sendEmptyMessage(5);
                    return true;
                }
                return false;
            }

        });

        return mainView;
    }

    public void ScrollToTop() {
        mWeb.loadUrl("http://3.1budai.com/mobile/catalog.php");

    }

    public class JavaScriptInterface {

        @JavascriptInterface
        public void showToast() {

            Intent intent = new Intent(getContext(), SimpleCaptureActivity.class);
            startActivity(intent);
        }
    }
}
