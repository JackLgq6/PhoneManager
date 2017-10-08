package com.eg.phonemanager.Activity;

import com.eg.phonemanager.R;
import com.eg.phonemanager.utils.ConstantValue;
import com.eg.phonemanager.utils.SpUtil;
import com.eg.phonemanager.view.SettingItemView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by jack on 17-9-3.
 */

public class SettingActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUpdate();
    }

    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        //是否选中,根据上一次存储的结果去做决定
        siv_update.setChecked(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果之前是选中的,点击过后,变成未选中
                //如果之前是未选中的,点击过后,变成选中
                //获取之前的选中状态
                boolean isChecked = siv_update.isChecked();
                //将原有状态取反
                siv_update.setChecked(!isChecked);
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !isChecked);
            }
        });
    }
}
