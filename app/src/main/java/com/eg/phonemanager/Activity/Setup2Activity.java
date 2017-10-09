package com.eg.phonemanager.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.eg.phonemanager.R;
import com.eg.phonemanager.utils.ConstantValue;
import com.eg.phonemanager.utils.SpUtil;
import com.eg.phonemanager.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView siv_sim_bound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUI();
    }

    private void initUI() {
        siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        //1,回显(读取已有的绑定状态,用作显示,sp中是否存储了sim卡的序列号)
        String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        //2.判断序列号是否为空
        if (TextUtils.isEmpty(sim_number)) {
            siv_sim_bound.setChecked(false);
        } else {
            siv_sim_bound.setChecked(true);
        }
        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3.获取原有的状态
                boolean isChecked = siv_sim_bound.isChecked();
                //4.将原有状态取反设置给当前条目
                siv_sim_bound.setChecked(!isChecked);
                if (!isChecked) {
                    //4.存储，获取sim卡序列号
                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = manager.getSimSerialNumber();
                    SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);
                } else {
                    //5.将存储sim卡序列号的节点从sp中删除
                    SpUtil.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
                }
            }
        });
    }

    @Override
    public void nextPage() {
        String serialNumber = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (!TextUtils.isEmpty(serialNumber)) {
            Intent intent = new Intent(this, Setup3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            Toast.makeText(this, "请绑定sim卡", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void prePage() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

}
