package com.hw.tools.split_joint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hw.tools.R;

import java.lang.reflect.Method;

/**
 * {此处写描述信息}
 * <p>
 * <p>
 * 作者：郝乐涛 on 2018/3/30 18:37
 * 邮箱：haoletao@miao.cn
 */
public class HwImgSplitJointView {
    public static Bitmap createScreenShotBitmap(Context context, Bitmap bitmap, int layoutID, int layoutHeight) {
        Point point = getRealScreenSize(context);
        View v = LayoutInflater.from(context).inflate(R.layout.hw_view_split_joint, null);
        ImageView iv = v.findViewById(R.id.iv_top);
        ViewStub viewStub = v.findViewById(R.id.vsb_bottom);
        viewStub.setInflatedId(layoutID);
        viewStub.inflate();
        //整体布局
        iv.setLayoutParams(new LinearLayout.LayoutParams(point.x, point.y));
        iv.setImageBitmap(bitmap);
        point.y = point.y + dip2px(context, layoutHeight);
        v.measure(View.MeasureSpec.makeMeasureSpec(point.x, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(point.y,
                        View.MeasureSpec.EXACTLY));

        v.layout(0, 0, point.x, point.y);

        Bitmap result = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(result);
        c.drawColor(Color.WHITE);
        // Draw view to canvas
        v.draw(c);


        return result;
    }

    /**
     * 获取屏幕分辨率
     */
    private static Point getRealScreenSize(Context context) {
        Point screenSize = null;
        try {
            screenSize = new Point();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                defaultDisplay.getRealSize(screenSize);
            } else {
                try {
                    Method mGetRawW = Display.class.getMethod("getRawWidth");
                    Method mGetRawH = Display.class.getMethod("getRawHeight");
                    screenSize.set(
                            (Integer) mGetRawW.invoke(defaultDisplay),
                            (Integer) mGetRawH.invoke(defaultDisplay)
                    );
                } catch (Exception e) {
                    screenSize.set(defaultDisplay.getWidth(), defaultDisplay.getHeight());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenSize;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
