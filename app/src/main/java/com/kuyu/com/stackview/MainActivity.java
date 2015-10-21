package com.kuyu.com.stackview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.StackView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private StackView sk_show;
    private Button btn_prev, btn_next, btn_autoplay, btn_autostop;
    private int[] imgIds = new int[]{R.drawable.imgv1, R.drawable.imgv2, R.drawable.imgv3,
            R.drawable.imgv4, R.drawable.imgv5, R.drawable.imgv6};
    private Timer timer;
    private TimerTask task;
    private final int UPDATE_ID = 101;
    private int iCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListener();
        initDate();
    }

    /**
     * 初始化空间
     */
    private void initViews() {
        sk_show = (StackView) findViewById(R.id.sk_show);
        btn_prev = (Button) findViewById(R.id.btn_prev);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_autoplay = (Button) findViewById(R.id.btn_autoPlay);
        btn_autostop = (Button) findViewById(R.id.btn_autostop);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        btn_prev.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_autoplay.setOnClickListener(this);
        btn_autostop.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < imgIds.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>() {
            };
            item.put("img", imgIds[i]);
            listItems.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.stackview_cell, new
                String[]{"img"}, new int[]{R.id.img_show}); //用适配器将每个图片设置到stackView里面去
        sk_show.setAdapter(adapter);
        btn_autostop.setClickable(false);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prev:
                sk_show.showPrevious();
                break;

            case R.id.btn_next:
                sk_show.showNext();
                break;

            case R.id.btn_autoPlay:
                btn_autoplay.setClickable(false);
                btn_autostop.setClickable(true);
                startPlay();
                break;

            case R.id.btn_autostop:
                btn_autostop.setClickable(false);
                btn_autoplay.setClickable(true);
                stopPlay();
                break;
        }
    }

    /**
     * 开始自动播放
     */
    private void startPlay() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.arg1 = UPDATE_ID;
                handler.sendMessage(msg);
                iCount++;
                if(iCount > 20){
                    timer.cancel();
                    task.cancel();
                    iCount = 0;
                    btn_autoplay.setClickable(true);
                }
            }
        };
        timer.schedule(task,0, 1000);
    }

    /**
     * 停止自动播放
     */
    private void stopPlay(){
        timer.cancel();
        task.cancel();
    }

    /**
     * 更新ui
     */
    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case UPDATE_ID:
                    sk_show.showNext();
                    break;
            }
        }
    };
}
