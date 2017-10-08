package com.eg.phonemanager.Activity;

import com.eg.phonemanager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by qiangge-pc on 2017/10/6.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<HashMap<String, String>> mContactList = new ArrayList<HashMap<String, String>>();
    ContactListActivity contactListActivity;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View contactView;
        TextView tv_name;
        TextView tv_phone;

        public ViewHolder(View view) {
            super(view);
            contactView = view;
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        }
    }

    public ContactAdapter(List<HashMap<String, String>> contactList, Activity activity) {
        this.mContactList = contactList;
        this.contactListActivity = (ContactListActivity) activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .recyclerview_contact_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.contactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                HashMap<String, String> hashMap = mContactList.get(position);
                String phone = hashMap.get("phone");
                Intent intent = new Intent();
                intent.putExtra("phone", phone);
                contactListActivity.setResult(0, intent);
                contactListActivity.finish();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> hashMap = mContactList.get(position);
        holder.tv_name.setText(hashMap.get("name"));
        holder.tv_phone.setText(hashMap.get("phone"));
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }


}
