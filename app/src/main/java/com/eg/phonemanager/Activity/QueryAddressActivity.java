package com.eg.phonemanager.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eg.phonemanager.R;

public class QueryAddressActivity extends AppCompatActivity {

    private EditText et_phone_adress;
    private Button bt_query_address;
    private TextView tv_show_phone_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);
        initUI();
    }

    private void initUI() {
        et_phone_adress = (EditText) findViewById(R.id.et_phone_address);
        bt_query_address = (Button) findViewById(R.id.bt_query_address);
        tv_show_phone_address = (TextView) findViewById(R.id.tv_show_phone_address);
        bt_query_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
