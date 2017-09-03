package com.eg.phonemanager.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by jack on 17-9-3.
 */

public class FocusTextView extends TextView{

    //使用在通过java代码创建控件
    public FocusTextView(Context context) {
        super(context);
    }

    //由系统调用(带属性+上下文环境构造方法)
    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //由系统调用(带属性+上下文环境构造方法+布局文件中定义样式文件构造方法)
    public FocusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //重写获取焦点的方法,由系统调用,调用的时候默认就能获取焦点
    @Override
    public boolean isFocused() {
        return true;
    }
}
