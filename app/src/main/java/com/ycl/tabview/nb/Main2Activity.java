package com.ycl.tabview.nb;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.ycl.tabview.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends FragmentActivity implements FragmentMy.CallBackValue,FragmentMain.CallBackValuemain,FragmentShequ.She,FragmentShop.Shopggg{

    public static final int TAB_HOME = 0;
    public static final int TAB_PROJECTS = 1;
    public static final int TAB_STUDYS = 2;
    public static final int TAB_SHOP = 3;
    public static final int TAB_USER_CENTER = 4;

    @Bind(R.id.viewpager)
    public ContainerViewPager viewPager;

    @Bind(R.id.radio_main)
    public RadioButton radioMain;

    @Bind(R.id.radio_projects)
    public RadioButton radioProjects;

    @Bind(R.id.radio_studys)
    public RadioButton radioStudys;

    @Bind(R.id.radio_shop)
    public RadioButton radioshop;

    @Bind(R.id.radio_user_center)
    public RadioButton radioUserCenter;

    FragmentMain fragmentMain;
    FragmentShequ fragmentShequ;
    FragmentShop fragmentShop;
    FragmentMy fragmentMy;
    private FragmentViewPagerAdapter adapter;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ButterKnife.bind(this);

        initView();
        addPageChangeListener();



    }

    private void initView() {

        fragments = new ArrayList<Fragment>();

        fragmentMain = FragmentMain.newInstance("http://3.1budai.com/mobile/catalog.php");

        FragmentHuodong fragmentHuodong = new FragmentHuodong();

        fragmentShequ = new FragmentShequ();

        fragmentShop = new FragmentShop();

        fragmentMy = new FragmentMy();

        fragments.add(fragmentMain);
        fragments.add(fragmentHuodong);
        fragments.add(fragmentShequ);
        fragments.add(fragmentShop);
        fragments.add(fragmentMy);

        this.viewPager.setOffscreenPageLimit(0);

        adapter = new FragmentViewPagerAdapter(this.getSupportFragmentManager(), viewPager, fragments);

    }

    private void addPageChangeListener() {
        viewPager.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int id) {
                switch (id) {
                    case TAB_HOME:
                        radioMain.setChecked(true);
                        break;
                    case TAB_PROJECTS:
                        radioProjects.setChecked(true);
                        break;
                    case TAB_STUDYS:
                        Log.e("++2++","执行了");
                        radioStudys.setChecked(true);
                        break;
                    case TAB_SHOP:
                        radioshop.setChecked(true);
                        break;
                    case TAB_USER_CENTER:
                        radioUserCenter.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @OnClick({R.id.radio_main, R.id.radio_projects, R.id.radio_studys,R.id.radio_shop, R.id.radio_user_center})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.radio_main:
                viewPager.setCurrentItem(TAB_HOME, false);
                fragmentMain.ScrollToTop();
                break;
            case R.id.radio_projects:
                viewPager.setCurrentItem(TAB_PROJECTS, false);
                break;
            case R.id.radio_studys:
                Log.e("++++","执行了");
                viewPager.setCurrentItem(TAB_STUDYS, false);
                fragmentShequ.ScrollToTop();
                break;
            case R.id.radio_shop:
                viewPager.setCurrentItem(TAB_SHOP, false);
                fragmentShop.ScrollToTop();
                break;
            case R.id.radio_user_center:
                viewPager.setCurrentItem(TAB_USER_CENTER, false);
                fragmentMy.ScrollToTop();
                break;
        }
    }

    long firstClickTime = 0;
    long secondClickTime = 0;

    public void doubleClick(View view) {

        if (firstClickTime > 0) {
            secondClickTime = SystemClock.uptimeMillis();
            if (secondClickTime - firstClickTime < 500) {
//                LogUtils.d("***************   double click  ******************");
                fragmentMain.ScrollToTop();
            }
            firstClickTime = 0;
            return;
        }

        firstClickTime = SystemClock.uptimeMillis();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    firstClickTime = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void SendMessageValue(final int i) {
        switch (i){
            case 1:
                finish();
                Intent intent1 = new Intent(Main2Activity.this,Main1Activity.class);
                startActivity(intent1);
                break;
            case 2:
                finish();
                Intent intent2 = new Intent(Main2Activity.this,Main22Activity.class);
                startActivity(intent2);
                break;
            case 3:
                finish();
                Intent intent3 = new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(intent3);
                break;
            case 4:
                finish();
                Intent intent4 = new Intent(Main2Activity.this,Main4Activity.class);
                startActivity(intent4);
                break;
        }
    }

    @Override
    public void SendMessageValuemain(final int i) {
        switch (i){
            case 1:
                finish();
                Intent intent1 = new Intent(Main2Activity.this,Main1Activity.class);
                startActivity(intent1);
                break;
            case 2:
                finish();
                Intent intent2 = new Intent(Main2Activity.this,Main22Activity.class);
                startActivity(intent2);
                break;
            case 3:
                finish();
                Intent intent3 = new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(intent3);
                break;
            case 4:
                finish();
                Intent intent4 = new Intent(Main2Activity.this,Main4Activity.class);
                startActivity(intent4);
                break;
        }
    }

    @Override
    public void Sendshe(int i) {
        switch (i){
            case 1:
                finish();
                Intent intent1 = new Intent(Main2Activity.this,Main1Activity.class);
                startActivity(intent1);
                break;
            case 2:
                finish();
                Intent intent2 = new Intent(Main2Activity.this,Main22Activity.class);
                startActivity(intent2);
                break;
            case 3:
                finish();
                Intent intent3 = new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(intent3);
                break;
            case 4:
                finish();
                Intent intent4 = new Intent(Main2Activity.this,Main4Activity.class);
                startActivity(intent4);
                break;
        }
    }

    @Override
    public void Sendshop(int i) {
        switch (i){
            case 1:
                finish();
                Intent intent1 = new Intent(Main2Activity.this,Main1Activity.class);
                startActivity(intent1);
                break;
            case 2:
                finish();
                Intent intent2 = new Intent(Main2Activity.this,Main22Activity.class);
                startActivity(intent2);
                break;
            case 3:
                finish();
                Intent intent3 = new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(intent3);
                break;
            case 4:
                finish();
                Intent intent4 = new Intent(Main2Activity.this,Main4Activity.class);
                startActivity(intent4);
                break;
        }
    }
}
