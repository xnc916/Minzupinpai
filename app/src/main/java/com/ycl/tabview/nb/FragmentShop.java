package com.ycl.tabview.nb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ycl.tabview.R;
import com.ycl.tabview.SimpleCaptureActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FragmentShop extends Fragment {
    View mainView = null;

    @Bind(R.id.web_shop)
    WebView mWeb;

    Shopggg shop;

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

    private ImageView iamgeView;
    private LinearLayout root;
    private boolean isFirst = true;
    private boolean isGoBack = false;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        shop =(Shopggg) getActivity();

    }
    private void webViewGoBack() {
        isGoBack = true;
        mWeb.goBack();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.fragment_fragment_shop, container, false);
        }
        ButterKnife.bind(this, mainView);
        root = (LinearLayout) mainView.findViewById(R.id.shop_fragment);
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        mWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWeb.getSettings().setSupportZoom(true);  //支持放大缩小
        mWeb.getSettings().setBuiltInZoomControls(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWeb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWeb.clearHistory();
        mWeb.clearFormData();
        mWeb.clearCache(true);
        settings.setAppCacheEnabled(true);    //开启H5(APPCache)缓存功能
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(true);    // 可以读取文件缓存(manifest生效)
        settings.setDatabaseEnabled(true);    //webSettings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);  // 开启 DOM storage 功能
        settings.setDisplayZoomControls(false);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH); //提高渲染的优先级

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

        mWeb.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (isFirst)return; //刚进入页面不需要模拟效果，app自己有

                view.setVisibility(View.GONE);//先隐藏webview
                if (newProgress == 100) {

                    //加载完毕，显示webview 隐藏imageview
                    view.setVisibility(View.VISIBLE);
                    if (iamgeView != null)iamgeView.setVisibility(View.GONE);

                    //页面进入效果的动画
                    Animation translate_in = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_in_go);
                    Animation translate_out = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out_go);

                    //页面退出的动画
                    if (isGoBack){
                        translate_in = AnimationUtils.loadAnimation(getContext(), R.anim.translate_in_back);
                        translate_out = AnimationUtils.loadAnimation(getContext(), R.anim.translate_out_back);
                    }
                    translate_in.setFillAfter(true);
                    translate_in.setDetachWallpaper(true);
                    translate_out.setFillAfter(true);
                    translate_out.setDetachWallpaper(true);

                    //开启动画
                    if(null!=iamgeView)iamgeView.startAnimation(translate_out);
                    view.startAnimation(translate_in);

                    //动画结束后，移除imageView
                    translate_out.setAnimationListener(new Animation.AnimationListener(){
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if(null!=iamgeView){
                                root.removeView(iamgeView);
                                iamgeView=null;
                                isGoBack = false;
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationStart(Animation animation) {

                        }
                    });

                }else{
                    //url没加载好之前，隐藏webview，在主布局中，加入imageview显示当前页面快照

                    if(null==iamgeView){
                        iamgeView=new ImageView(getContext());
                        view.setDrawingCacheEnabled(true);
                        Bitmap bitmap=view.getDrawingCache();
                        if(null!=bitmap){
                            Bitmap b=   Bitmap.createBitmap(bitmap);
                            iamgeView.setImageBitmap(b);
                        }
                        root.addView(iamgeView);
                    }
                }

            }
        });
        mWeb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                isFirst = false;
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mWeb.loadUrl("http://3.1budai.com/mobile/flow.php");
        return mainView;
    }

    public void ScrollToTop() {
        isFirst = true;
        mWeb.loadUrl("http://3.1budai.com/mobile/flow.php");

    }

    public class JavaScriptInterface {

        @JavascriptInterface
        public void showToast() {

            Intent intent = new Intent(getContext(), SimpleCaptureActivity.class);
            startActivity(intent);
        }
        @JavascriptInterface
        public  void shouye(){
            shop.Sendshop(1);
        }
        @JavascriptInterface
        public  void fenlei(){
            shop.Sendshop(2);;
        }

        @JavascriptInterface
        public  void gouwu(){
            shop.Sendshop(3);
        }
        @JavascriptInterface
        public  void geren(){
            shop.Sendshop(4);
        }
    }
    public interface Shopggg{
        public void Sendshop(int i);
    }
}
