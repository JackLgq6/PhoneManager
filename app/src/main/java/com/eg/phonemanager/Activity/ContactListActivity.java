package com.eg.phonemanager.Activity;

import com.eg.phonemanager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ContactListActivity extends AppCompatActivity {

    private List<HashMap<String, String>> mContactList = new ArrayList<HashMap<String, String>>();
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            recyclerView.setAdapter(contactAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initData();
        initUI();
    }

    private void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        contactAdapter = new ContactAdapter(mContactList, ContactListActivity.this);
    }

    private void initData() {
        //因为读取系统联系人,可能是一个耗时操作,放置到子线程中处理
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone
                    .CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.DISPLAY_NAME));
                        String number = cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.NUMBER));
                        hashMap.put("name", name);
                        hashMap.put("phone", number);
                        mContactList.add(hashMap);
                    }
                    mHandler.sendEmptyMessage(0);
                    contactAdapter.notifyDataSetChanged();
                    //发送一个空的消息,告知主线程可以去使用子线程已经填充好的数据集合
                    cursor.close();
                }
            }
        }).start();
    }
}
