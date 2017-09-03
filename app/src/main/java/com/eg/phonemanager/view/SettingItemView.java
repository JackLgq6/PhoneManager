package com.eg.phonemanager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eg.phonemanager.R;

/**
 * Created by jack on 17-9-3.
 */
public class SettingItemView extends RelativeLayout {

    private TextView tv_setting_des;
    private CheckBox cb_box;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml--->view	将设置界面的一个条目转换成view对象,直接添加到了当前SettingItemView对应的view中
        View.inflate(context, R.layout.setting_item_view, this);

        //等同于以下两行代码
		/*View view = View.inflate(context, R.layout.setting_item_view, null);
		this.addView(view);*/
        TextView tv_setting_title = (TextView) findViewById(R.id.tv_setting_title);
        tv_setting_des = (TextView) findViewById(R.id.tv_setting_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        tv_setting_title.setText("自动更新设置");
    }

    /**
     * 判断是否开启的方法
     * @return	返回当前SettingItemView是否选中状态	true开启(checkBox返回true)
     * false关闭(checkBox返回false)
     */
    public boolean isChecked() {
        return cb_box.isChecked();
    }

    /**
     * @rpaam isChecked 是否作为开启的变量,由点击过程中去做传递
     */
    public void setChecked(boolean isChecked) {
        //当前条目在选择的过程中,cb_box选中状态也在跟随(isCheck)变化
        cb_box.setChecked(isChecked); //如果不设置cb_box的状态变化，选中一次后就没有没变化，isChecked永远为true
        if (isChecked) {
            tv_setting_des.setText("自动更新已开启");
        } else {
            tv_setting_des.setText("自动更新已关闭");
        }
    }
}
