package com.eg.phonemanager.Activity;

import com.eg.phonemanager.R;
import com.eg.phonemanager.utils.ConstantValue;
import com.eg.phonemanager.utils.SpUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import static com.eg.phonemanager.R.id.tv_phone;

public class SetupOverActivity extends AppCompatActivity {

    private TextView tv_reset_setup;
    private TextView tv_show_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if (setup_over) {
            //密码输入成功,并且四个导航界面设置完成----->停留在设置完成功能列表界面
            setContentView(R.layout.activity_setup_over);
            initUI();
        } else {
            //密码输入成功,四个导航界面没有设置完成----->跳转到导航界面第1个
            Intent intent = new Intent(SetupOverActivity.this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initUI() {
        tv_show_phone = (TextView) findViewById(R.id.tv_show_phone);
        //设置联系人号码
        String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE_NUMBER, "");
        tv_show_phone.setText(phone);
        tv_reset_setup = (TextView) findViewById(R.id.tv_reset_setup);
        //重新设置条目被点击
        tv_reset_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupOverActivity.this, Setup1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
