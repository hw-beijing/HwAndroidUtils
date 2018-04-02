package com.hw.tools.split_joint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hw.tools.R;

/**
 * {此处写描述信息}
 * <p>
 * <p>
 * 作者：郝乐涛 on 2018/3/30 18:37
 * 邮箱：haoletao@miao.cn
 */
public class HwImgSplitJointView {
    public static Bitmap createScreenShotBitmap(Context context, View view, int layoutID, int layoutHeight) {

//        Point point = getRealScreenSize(context);
        Point point = new Point();
        point.x = view.getWidth();
        point.y = view.getHeight();
        View v = LayoutInflater.from(context).inflate(R.layout.hw_tools_view_split_joint, null);
        ImageView iv = v.findViewById(R.id.iv_top);
        ViewStub viewStub = v.findViewById(R.id.vsb_bottom);
        viewStub.setLayoutResource(layoutID);
        viewStub.inflate();
        //整体布局
        iv.setLayoutParams(new LinearLayout.LayoutParams(point.x, point.y));

        view.setDrawingCacheEnabled(true);
        view.destroyDrawingCache();
        iv.setImageBitmap(view.getDrawingCache());
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
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        return result;
    }

   

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
