package com.hw.utils.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hw.tools.split_joint.HwImgSplitJointView;

public class HwSplitJointActivity extends Activity implements View.OnClickListener {

    protected ImageView ivImg;
    protected LinearLayout clRoot;
    protected Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.hw_activity_split_joint);
        initView();

    }

    private void initView() {
        ivImg = (ImageView) findViewById(R.id.iv_img);
        clRoot = (LinearLayout) findViewById(R.id.cl_root);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(HwSplitJointActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn) {
            ivImg.setImageBitmap(HwImgSplitJointView.createScreenShotBitmap(this, clRoot, R.layout.hw_view_split_joint, 50));
        }
    }
}
