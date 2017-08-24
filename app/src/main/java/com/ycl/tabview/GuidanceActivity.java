package com.ycl.tabview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ycl.tabview.nb.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GuidanceActivity extends AppCompatActivity {
    private ViewPager gdvp;
    private GuidanceAdapter gda;
    private ImageView[] gdiv;

    String s = "3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);


        SharedPreferences spf = getSharedPreferences("main",
                Context.MODE_PRIVATE);
        Boolean isFrist = true;

        String banbei = spf.getString("banbei", "0");
        if (!s.equals(banbei)){
            isFrist = true;
        }else {
            isFrist = false;
        }

        if (isFrist) {
            // 初始化控件
            initView();
            // 设置监听
            setPagerViewListener();
            // 设置adapter
            setUpAdapter();
            // 数据源
            setData();

        } else {
            Intent gdi = new Intent(this, WelcomeActivity.class);
            startActivity(gdi);
            finish();
        }

    }


    private void setData() {
        List<View> list = new ArrayList<View>();
        View gdone = LayoutInflater.from(this).inflate(R.layout.guidance_one,
                null);
        View gdtwo = LayoutInflater.from(this).inflate(R.layout.guidance_two,
                null);
        View gdthree = LayoutInflater.from(this).inflate(
                R.layout.guidance_three, null);
        View gdfour = LayoutInflater.from(this).inflate(R.layout.guidance_four,
                null);

        list.add(gdone);
        list.add(gdtwo);
        list.add(gdthree);
        list.add(gdfour);

        gda.setDataList(list);
        gda.notifyDataSetChanged();

    }

    private void setUpAdapter() {
        gda = new GuidanceAdapter();
        gdvp.setAdapter(gda);
    }

    private void setPagerViewListener() {
        gdvp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position > 2) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent gdin = new Intent(GuidanceActivity.this,
                                    MainActivity.class);
                            SharedPreferences sp = getSharedPreferences("main",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor ed = sp.edit();
                            //ed.putBoolean("isFrist", false);
                            ed.putString("banbei",s);
                            ed.commit();

                            startActivity(gdin);
                            finish();
                        }
                    }, 1500);
                }

                for (int i = 0; i < gdiv.length; i++) {
                    gdiv[i].setImageResource(R.drawable.adware_style_default);
                }
                gdiv[position]
                        .setImageResource(R.drawable.adware_style_selected);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initView() {
        gdvp = (ViewPager) findViewById(R.id.guidance_viewpager);

        gdiv = new ImageView[4];
        gdiv[0] = (ImageView) findViewById(R.id.guidance_point_one);
        gdiv[1] = (ImageView) findViewById(R.id.guidance_point_two);
        gdiv[2] = (ImageView) findViewById(R.id.guidance_point_three);
        gdiv[3] = (ImageView) findViewById(R.id.guidance_point_four);

        gdiv[0].setImageResource(R.drawable.adware_style_selected);

    }
}
