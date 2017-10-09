package com.eg.phonemanager.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eg.phonemanager.R;
import com.eg.phonemanager.utils.ConstantValue;
import com.eg.phonemanager.utils.SpUtil;

public class Setup3Activity extends BaseSetupActivity {

    private EditText et_phone_number;
    private Button bt_select_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        initUI();
    }

    private void initUI() {
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE_NUMBER, "");
        et_phone_number.setText(phone);
        bt_select_number = (Button) findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            //1.返回到当前界面的时候,接受结果的方法
            String phone = data.getStringExtra("phone");
            //2,将特殊字符过滤(中划线转换成空字符串)
            phone = phone.replace("-", "").replace(" ", "").trim();
            et_phone_number.setText(phone);
            //3.存储联系人至sp中
            SpUtil.putString(this, ConstantValue.CONTACT_PHONE_NUMBER, phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void nextPage() {
        String phone = et_phone_number.getText().toString();
        if (!TextUtils.isEmpty(phone)) {
            Intent intent = new Intent(this, Setup4Activity.class);
            startActivity(intent);
            finish();
            //如果现在是输入电话号码,则需要去保存
            SpUtil.putString(this, ConstantValue.CONTACT_PHONE_NUMBER, phone);
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            Toast.makeText(this, "请输入电话号码", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void prePage() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

}
