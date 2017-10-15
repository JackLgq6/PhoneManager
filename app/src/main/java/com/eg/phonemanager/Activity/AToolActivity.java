package com.eg.phonemanager.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eg.phonemanager.R;

public class AToolActivity extends AppCompatActivity {

    private TextView tv_query_phone_address;
    private TextView tv_backup_message;
    private TextView tv_select_often_number;
    private TextView tv_app_lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);
        initUI();
        setOnClickListener();
    }

    private void setOnClickListener() {
        tv_query_phone_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AToolActivity.this, QueryAddressActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        tv_query_phone_address = (TextView) findViewById(R.id.tv_query_phone_address);
        tv_backup_message = (TextView) findViewById(R.id.tv_backup_message);
        tv_select_often_number = (TextView) findViewById(R.id.tv_select_often_number);
        tv_app_lock = (TextView) findViewById(R.id.tv_app_lock);
    }
}
