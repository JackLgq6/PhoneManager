package com.eg.phonemanager.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eg.phonemanager.R;

public class SplashActivity extends AppCompatActivity {

    private TextView tv_version_name;
    private RelativeLayout rl_root;
    private final int ENTER_HOME = 1;
    private final int ERROR = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENTER_HOME:
                    enterHome();
                    break;
                case ERROR:
                    Toast.makeText(SplashActivity.this, "UI异常", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
        setContentView(R.layout.activity_splash);
        initUI();
        initData();
        initAnimation();
        welcome();
    }

    private void welcome() {
        //消息机制
		//mHandler.sendMessageDelayed(msg, 4000);
        //在发送消息4秒后去处理,ENTER_HOME状态码指向的消息
        handler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
    }

    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Add fade animation effect
     */
    private void initAnimation() {
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(3000);
        rl_root.startAnimation(animation);
    }

    private void initData() {
        tv_version_name.setText("当前版本:" + getVersionName());
    }

    private void initUI() {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
    }

    private String getVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
