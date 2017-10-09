package com.eg.phonemanager.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by gqliu on 17-10-9.
 */

public abstract class BaseSetupActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            /**
             * 监听手势滑动事件
             * @param e1 表示滑动的起点
             * @param e2 表示滑动终点
             * @param velocityX 表示水平滑动速度
             * @param velocityY 表示垂直滑动速度
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //判断纵向的滑动幅度是否过大，如果过大，不允许切换界面
                if (Math.abs(e2.getRawY() - e1.getRawY()) > 400) {
                    Toast.makeText(BaseSetupActivity.this, "不能这样划哦，亲", Toast.LENGTH_SHORT).show();
                    return true;
                }

                //判断滑动的是否过慢
                if (Math.abs(velocityX ) < 100) {
                    Toast.makeText(BaseSetupActivity.this, "亲，滑动的太慢了哦", Toast.LENGTH_SHORT).show();
                    return true;
                }
                //向左划,下一页
                if (e1.getRawX() - e2.getRawX() > 200) {
                    nextPage();
                }
                //向右划,上一页
                if (e2.getRawX() - e1.getRawX() > 200) {
                    prePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public abstract void nextPage();
    public abstract void prePage();

    public void showNextPage(View view) {
        nextPage();
    }

    public void showPrePage(View view) {
        prePage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //委托手势识别器处理触摸事件
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
