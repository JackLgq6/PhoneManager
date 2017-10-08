package com.eg.phonemanager.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eg.phonemanager.R;

/**
 * Created by jack on 17-9-9.
 */

class Setup1Activity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    public void nextPage(View view) {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();

        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }
}
