package com.hw.utils.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
/**
 * {此处写描述信息}
 * <p>
 * <p>
 * 作者：郝乐涛 on
 * 邮箱：364320703@qq.com
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private ArrayList<Button> buttons = new ArrayList<>();
    private LinearLayout mLinearLayout;
    private LinearLayout.LayoutParams mLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initView());
    }

    private ViewGroup initView() {
        // 创建LinearLayout对象
        mLinearLayout = new LinearLayout(this);

        // 建立布局样式宽和高，对应xml布局中：
        // android:layout_width="fill_parent"
        // android:layout_height="fill_parent"
        mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        // 设置方向，对应xml布局中：
        // android:orientation="vertical"
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);

        mLayoutParams = new LinearLayout.LayoutParams(-1, -2);
        mLayoutParams.setMargins(20, 20, 20, 20);

        addView("弧形按钮菜单");

        return mLinearLayout;
    }

    private void addView(String text) {
        Button button = new Button(this);
        button.setTextColor(Color.RED);
        button.setOnClickListener(this);
        button.setText(text);
        mLinearLayout.addView(button, mLayoutParams);
        buttons.add(button);
    }

    @Override
    public void onClick(View v) {
        if (v == buttons.get(0)) {
            startActivity(new Intent(this, HwArcButtonActivity.class));
        }


    }
}
