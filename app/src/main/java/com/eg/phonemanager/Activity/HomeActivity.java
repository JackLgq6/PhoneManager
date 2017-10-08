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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eg.phonemanager.R;
import com.eg.phonemanager.service.DownloadService;
import com.eg.phonemanager.utils.ConstantValue;
import com.eg.phonemanager.utils.SpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private GridView gv_home;
    private String[] mTitleStrs;
    private int[] mDrawableIds;

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
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_CONTACTS);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(HomeActivity.this, permissions, 1);
        }
        if (SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false)) {
            checkVersionCode();
        }
        initUI();
        initData();
    }

    private void initData() {
        mTitleStrs = new String[] {"手机防盗","通信卫士","软件管理","进程管理","流量统计",
            "手机杀毒","缓存清理","高级工具","设置中心"};
        mDrawableIds = new int[] {
                R.drawable.home_safe,R.drawable.home_callmsgsafe,
                R.drawable.home_apps,R.drawable.home_taskmanager,
                R.drawable.home_netmanager,R.drawable.home_trojan,
                R.drawable.home_sysoptimize,R.drawable.home_tools,
                R.drawable.home_settings};
        gv_home.setAdapter(new MyAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showDialog();
                        break;
                    case 8:
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;

                }
            }
        });
    }

    private void showDialog() {
        //判断本地是否有存储密码
        String pwd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PWD, "");
        if (TextUtils.isEmpty(pwd)) {
            showPwdSetDialog();
        } else {
            showPwdConfirmDialog();
        }
    }

    private void showPwdSetDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_set_pwd, null);
        dialog.setView(view);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        final EditText et_set_pwd = (EditText) view.findViewById(R.id.et_set_pwd);
        final EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_set_pwd.getText().toString();
                String confirm_pwd = et_confirm_pwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(HomeActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirm_pwd)) {
                    Toast.makeText(HomeActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(confirm_pwd)) {
                    if (pwd.equals(confirm_pwd)) {
                        Log.i(TAG, "pwd: " + pwd);
                        Log.i(TAG, "confirm_pwd: " + confirm_pwd);
                        SpUtil.putString(HomeActivity.this, ConstantValue.MOBILE_SAFE_PWD, pwd);
                        Intent intent = new Intent(HomeActivity.this, Setup1Activity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showPwdConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_confirm_pwd, null);
        dialog.setView(view);
        dialog.show();
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        final EditText et_confirm_pwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String confirm_pwd = et_confirm_pwd.getText().toString();
                if (TextUtils.isEmpty(confirm_pwd)) {
                    Toast.makeText(HomeActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(confirm_pwd)) {
                    String mobile_safe_pwd = SpUtil.getString(HomeActivity.this, ConstantValue.MOBILE_SAFE_PWD, "");
                    if (confirm_pwd.equals(mobile_safe_pwd)) {
                        boolean setup_over = SpUtil.getBoolean(HomeActivity.this, ConstantValue
                            .SETUP_OVER, false);
                        if (setup_over) {
                            Intent intent = new Intent(HomeActivity.this, SetupOverActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(HomeActivity.this, Setup1Activity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    } else {
                        Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTitleStrs.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStrs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //MyApplication.getContext() 空指针，为啥？
            View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_title.setText(mTitleStrs[position]);
            iv_icon.setImageResource(mDrawableIds[position]);
            return view;
        }
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
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
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "Denied permissions will not be able to use the application",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
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
                String url = "http://10.0.2.2:8080/PhoneManager.apk";
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
