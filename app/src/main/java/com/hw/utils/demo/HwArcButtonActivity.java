package com.hw.utils.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hw.widget.arcbutton.HwArcButtonGroup;
import com.hw.widget.arcbutton.HwArcDataTypeBean;

import java.util.ArrayList;

/**
 * {此处写描述信息}
 * <p>
 * <p>
 * 作者：郝乐涛 on
 * 邮箱：364320703@qq.com
 */
public class HwArcButtonActivity extends Activity implements HwArcButtonGroup.HwArcButtonItemClick {

    protected RelativeLayout rlMainRoot;
    private HwArcButtonGroup hwArcButtonGroup;
    private ArrayList<HwArcDataTypeBean> hwArcDataTypeBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.hw_activity_arc_button);
        rlMainRoot = (RelativeLayout) findViewById(R.id.rl_main_root);
        hwArcButtonGroup = new HwArcButtonGroup(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        hwArcButtonGroup.setLayoutParams(params);
        rlMainRoot.addView(hwArcButtonGroup);

        hwArcButtonGroup.setImageIds(getAddDataTypes());
        hwArcButtonGroup.setBackgroundView(findViewById(R.id.rl_main));
        hwArcButtonGroup.setMpAddButtonItemClick(this);
    }


    protected ArrayList<HwArcDataTypeBean> getAddDataTypes() {
        String type_code = "类型";
        hwArcDataTypeBeans = new ArrayList<>();
        HwArcDataTypeBean hwArcDataTypeBean;
        hwArcDataTypeBean = new HwArcDataTypeBean();
        hwArcDataTypeBean.setType(type_code);
        hwArcDataTypeBean.setName("按钮1");
        hwArcDataTypeBean.setResId(R.mipmap.mp_arc_button_group_sport);
        hwArcDataTypeBeans.add(hwArcDataTypeBean);

        hwArcDataTypeBean = new HwArcDataTypeBean();
        hwArcDataTypeBean.setType(type_code);
        hwArcDataTypeBean.setName("按钮2");
        hwArcDataTypeBean.setResId(R.mipmap.mp_arc_button_group_gps);
        hwArcDataTypeBeans.add(hwArcDataTypeBean);

        hwArcDataTypeBean = new HwArcDataTypeBean();
        hwArcDataTypeBean.setType(type_code);
        hwArcDataTypeBean.setName("按钮3");
        hwArcDataTypeBean.setResId(R.mipmap.mp_arc_button_group_voice);
        hwArcDataTypeBeans.add(hwArcDataTypeBean);

        hwArcDataTypeBean = new HwArcDataTypeBean();
        hwArcDataTypeBean.setType(type_code);
        hwArcDataTypeBean.setName("按钮4");
        hwArcDataTypeBean.setResId(R.mipmap.mp_arc_button_group_manual);
        hwArcDataTypeBeans.add(hwArcDataTypeBean);

        return hwArcDataTypeBeans;
    }

    @Override
    public void onArcButtonItemClick(View v, int position) {
        Toast.makeText(getApplicationContext(),
                "position :" + position +
                        " name:" + hwArcDataTypeBeans.get(position).getName(), Toast.LENGTH_LONG).show();
    }
}
