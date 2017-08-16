package com.eg.phonemanager.Activity;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.eg.phonemanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int VERSION_UPDATE =20;
    private static final int ERROR = 22;
    private int mLocalVersionCode;
    private String mVersionCode;
    private String mVersionName;
    private String mVersionDescription;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VERSION_UPDATE:
                    showUpdateDialog();
                    break;
                case ERROR:
                    Toast.makeText(HomeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkVersionCode();

    }

    private void checkVersionCode() {
        Thread t = new Thread() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/update.json").build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private void parseJSONWithJSONObject(String jsonData) {
        Message msg = handler.obtainMessage();
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                mVersionCode = jsonObject.getString("versionCode");
                mVersionName = jsonObject.getString("versionName");
                mVersionDescription = jsonObject.getString("versionDescription");
                mLocalVersionCode = getLocalVersionCode();
                if (mLocalVersionCode < Integer.parseInt(mVersionCode)) {
                    msg.what = VERSION_UPDATE;
                }
                Log.i(TAG, "versionCode: " + mVersionCode);
                Log.i(TAG, "versionName: " + mVersionName);
                Log.i(TAG, "versionDescription: " + mVersionDescription);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            msg.what = ERROR;
        } finally {
            handler.sendMessage(msg);
        }
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("版本更新");
        builder.setMessage(mVersionDescription);
        builder.setPositiveButton("前往下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "开始下载……");
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private int getLocalVersionCode() {
        PackageManager pm = this.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
