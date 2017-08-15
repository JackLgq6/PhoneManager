package com.eg.phonemanager;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private int mLocalVersionCode;
    private static final int VERSION_UPDATE =20;
    private static final int ERROR = 22;

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
                String versionCode = jsonObject.getString("versionCode");
                String versionName = jsonObject.getString("versionName");
                String versionDescription = jsonObject.getString("versionDescription");
                mLocalVersionCode = getLocalVersionCode();
                if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                    msg.what = VERSION_UPDATE;
                }
                Log.i(TAG, "versionCode: " + versionCode);
                Log.i(TAG, "versionName: " + versionName);
                Log.i(TAG, "versionDescription: " + versionDescription);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            msg.what = ERROR;
        } finally {
            handler.sendMessage(msg);
        }
    }

    private void showUpdateDialog() {

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
