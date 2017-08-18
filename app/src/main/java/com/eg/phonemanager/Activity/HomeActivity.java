package com.eg.phonemanager.Activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.eg.phonemanager.R;
import com.eg.phonemanager.service.DownloadService;

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
    private DownloadService.DownloadBinder downloadBinder;

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
    
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "service" + service);
            downloadBinder = (DownloadService.DownloadBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        checkVersionCode();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    Log.i(TAG, "responseData: " + responseData);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
       @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Denied permissions will not be able to use the application",
                        Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
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
                String url = "http://10.0.2.2:8080/cm_security_cn.apk";
                downloadBinder.startDownload(url);
//                Log.i(TAG, "开始下载……");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

}
