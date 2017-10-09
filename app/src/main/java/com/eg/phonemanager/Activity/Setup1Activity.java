package com.eg.phonemanager.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.eg.phonemanager.R;

/**
 * Created by jack on 17-9-9.
 */

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    public void nextPage() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    @Override
    public void prePage() {

    }
}
