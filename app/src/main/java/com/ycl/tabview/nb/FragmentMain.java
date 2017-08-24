package com.ycl.tabview.nb;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ycl.tabview.BuildConfig;
import com.ycl.tabview.R;
import com.ycl.tabview.SimpleCaptureActivity;
import com.ycl.tabview.network.NationalClient;
import com.ycl.tabview.network.UICallBack;
import com.ycl.tabview.phone.BitmapAndStringUtils;
import com.ycl.tabview.phone.PhoneActivity;
import com.ycl.tabview.phone.PhoneResult;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import okhttp3.Call;

public class FragmentMain extends Fragment {

    View mainView = null;
    CallBackValuemain callBackValuemain;
    WebView mWeb;
    String url = "http://3.1budai.com/mobile/";
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
    private String data;

    private void webViewGoBack() {
        isGoBack = true;
        mWeb.goBack();
    }

    static FragmentMain newInstance(String s){
        FragmentMain myFragment = new FragmentMain();
        Bundle bundle = new Bundle();
        bundle.putString("DATA",s);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    private ImageView iamgeView;
    private LinearLayout root;
    private boolean isFirst = true;
    private boolean isGoBack = false;

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;

    //调用照相机返回图片文件
    private File tempFile;
    // 1: qq, 2: weixin
    private int type;
    private String ids;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValuemain =(CallBackValuemain) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mainView == null) {
            mainView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        ButterKnife.bind(this, mainView);

        //当前界面的布局
        root = (LinearLayout) mainView.findViewById(R.id.main_fragment);

        Bundle bundle = getArguments();
        data = bundle.getString("DATA");

        mWeb = (WebView) mainView.findViewById(R.id.web);

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
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onAnimationStart(Animation animation) {
                            // TODO Auto-generated method stub
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
        if (data != null){
            mWeb.loadUrl(data);
        }else {
            mWeb.loadUrl("http://3.1budai.com/mobile/");
        }
        return mainView;
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//
//        if (!hidden) {
//            mWeb.loadUrl("http://3.1budai.com/mobile/");
//        }
//
//    }




    public class JavaScriptInterface {

        @JavascriptInterface
        public void showToast() {

            Intent intent = new Intent(getContext(), SimpleCaptureActivity.class);
            startActivity(intent);
        }
        @JavascriptInterface
        public  void shouye(){
            callBackValuemain.SendMessageValuemain(1);
        }
        @JavascriptInterface
        public  void fenlei(){
            callBackValuemain.SendMessageValuemain(2);
        }
        
        @JavascriptInterface
        public  void gouwu(){
            callBackValuemain.SendMessageValuemain(3);
        }
        @JavascriptInterface
        public  void geren(){
            callBackValuemain.SendMessageValuemain(4);
        }
        @JavascriptInterface
        public void head_portrait(String id){
            ids = id;
            type = 1;
            uploadHeadImage();
        }

    }

    public void ScrollToTop() {
        isFirst = true;
        mWeb.loadUrl("http://3.1budai.com/mobile/");

    }

    public interface CallBackValuemain{
        public void SendMessageValuemain(int i);
    }

    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);


        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);

                } else {
                    //跳转到调用系统相机
                    gotoCamera();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到相册
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 外部存储权限申请返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCamera();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            }
        }
    }


    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        Log.d("evan", "*****************打开相机********************");
        //创建拍照存储的图片文件
        tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == getActivity().RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == getActivity().RESULT_OK) {
                    Uri uri = intent.getData();

                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == getActivity().RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);


                    String s = BitmapAndStringUtils.convertIconToString(bitMap);
                    Call call = NationalClient.getInstance().uploadAvatar(ids,s);
                    call.enqueue(new UICallBack() {
                        @Override
                        public void onFailureUI(Call call, IOException e) {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponseUI(Call call, String body) {

                            PhoneResult result = new Gson().fromJson(body,PhoneResult.class);
                            if (result.isData()){
                                mWeb.reload();
                            }
                        }
                    });

                }
                break;
        }
    }


    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getContext(), PhoneActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }



}
