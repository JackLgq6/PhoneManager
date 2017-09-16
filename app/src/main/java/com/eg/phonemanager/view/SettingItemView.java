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

    private static final String TAG = "SettingItemView";
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.eg.phonemanager";
    private TextView tv_setting_des;
    private CheckBox cb_box;
    private String desTitle;
    private String desOn;
    private String desOff;

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
        //通过属性索引获取属性名称&属性值
        /*for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.i(TAG, "name: " + attrs.getAttributeName(i));
            Log.i(TAG, "value: " + attrs.getAttributeValue(i));
        }*/
        initAttrs(attrs);
        tv_setting_title.setText(desTitle);
    }

    /**
     * 返回属性集合中自定义属性的属性值
     * @param attrs	构造方法中维护好的属性集合
     */
    private void initAttrs(AttributeSet attrs) {
        //通过名称空间和属性名称获取属性值
        desTitle = attrs.getAttributeValue(NAMESPACE, "desTitle");
        desOn = attrs.getAttributeValue(NAMESPACE, "desOn");
        desOff = attrs.getAttributeValue(NAMESPACE, "desOff");
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
            tv_setting_des.setText(desOn);
        } else {
            tv_setting_des.setText(desOff);
        }
    }
}
