package com.hw.widget.arcbutton;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hw.widget.R;

import java.util.ArrayList;

/**
 * {此处写描述信息}
 * <p>
 * <p>
 * 作者：郝乐涛 on 2018/1/16 15:13
 * 邮箱：364320703@qq.com
 */

public class HwArcButtonGroup extends RelativeLayout implements View.OnClickListener {
    protected ImageView ibtnAddButtomGroupAdd;
    protected LinearLayout llAddButtomGroupAdd;
    protected ImageButton ibtnAddButtomGroupClose;
    protected RelativeLayout rlAddButtomGroup;
    protected View rootView;
    protected View vAddButtonGroup;
    protected View vAddButtonGroupB;
    private ArrayList<View> views = new ArrayList<View>();

    private final int maxAngleAll = -65;
    private final int minAngle = -16;

    private ArrayList<HwArcDataTypeBean> imageIds = new ArrayList<>();

    public void setBackgroundView(View backgroundView) {
        this.backgroundView = backgroundView;
    }

    private View backgroundView;

    public boolean isOpenState() {
        return openState;
    }

    private boolean openState = false;


    private HwArcButtonItemClick mpAddButtonItemClick;

    public HwArcButtonGroup(Context context) {
        super(context);
        init(context);
    }

    public HwArcButtonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.hw_widget_view_arc_button_group, this);
        initView(this);
        addViews();
    }

    public void setImageIds(ArrayList<HwArcDataTypeBean> imageIds) {
        this.imageIds = imageIds;
        addViews();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_arc_buttom_group_add) {
            setBackground();
            openButtonAnimator();
        } else if (view.getId() == R.id.ibtn_arc_buttom_group_close) {
            closeButtonAnimator();
        } else {
            for (int i = 0; i < views.size(); i++) {
                if (view == views.get(i)) {
                    closeButtonAnimator();
                    if (mpAddButtonItemClick != null) {
                        mpAddButtonItemClick.onArcButtonItemClick(view, i);
                    }
                    return;
                }
            }

        }
    }

    private void setBackground() {
        if (backgroundView != null) {
//            backgroundView = llAddButtomGroupAdd;
            backgroundView.setDrawingCacheBackgroundColor(Color.WHITE);
            backgroundView.setDrawingCacheEnabled(true);
            backgroundView.buildDrawingCache();
            final Bitmap bitmap = backgroundView.getDrawingCache();

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    blur(bitmap);
                    backgroundView.destroyDrawingCache();
                    bitmap.recycle();
                }
            }.start();

        }
    }

    public void setMpAddButtonItemClick(HwArcButtonItemClick mpAddButtonItemClick) {
        this.mpAddButtonItemClick = mpAddButtonItemClick;
    }

    private void initView(View rootView) {
        ibtnAddButtomGroupAdd = (ImageView) rootView.findViewById(R.id.ibtn_arc_buttom_group_add);
        llAddButtomGroupAdd = (LinearLayout) rootView.findViewById(R.id.ll_arc_buttom_group_add);
        llAddButtomGroupAdd.setOnClickListener(HwArcButtonGroup.this);
        ibtnAddButtomGroupClose = (ImageButton) rootView.findViewById(R.id.ibtn_arc_buttom_group_close);
        ibtnAddButtomGroupClose.setOnClickListener(HwArcButtonGroup.this);
        rlAddButtomGroup = (RelativeLayout) rootView.findViewById(R.id.rl_arc_buttom_group);
        vAddButtonGroup = (View) rootView.findViewById(R.id.v_arc_button_group);
        vAddButtonGroupB = (View) rootView.findViewById(R.id.v_arc_button_group_b);


    }

    private void addViews() {
        removeViews();
        for (int i = imageIds.size() - 1; i >= 0; i--) {
            addImageButton(i, imageIds.get(i));
        }

    }

    private void removeViews() {
        if (rlAddButtomGroup != null) {
            for (int i = 0; i < views.size(); i++) {
                rlAddButtomGroup.removeView(views.get(i));
            }
        }
    }

    private void addImageButton(int position, HwArcDataTypeBean mpAddDataTypeBean) {
        if (rlAddButtomGroup != null) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.hw_widget_view_arc_button_item, null);
            ImageView imageView = (ImageView) v.findViewById(R.id.iv_arc_button_item);
            TextView textView = (TextView) v.findViewById(R.id.tv_arc_button_item);
            imageView.setImageResource(mpAddDataTypeBean.getResId());
            textView.setText(mpAddDataTypeBean.getName());
            LayoutParams params = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, HwArcUtils.dip2px(getContext(), 43));
            params.setMargins(0, 0, HwArcUtils.dip2px(getContext(), 37), HwArcUtils.dip2px(getContext(), 37));
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            v.setLayoutParams(params);
            v.setTag(position);
            v.setOnClickListener(this);
            rlAddButtomGroup.addView(v, 0);
            views.add(v);
        }
    }

    public void openButtonAnimator() {
        if (openState) {
            return;
        }
        openState = true;
        int duration = 500;
        ObjectAnimator rotation = HwArcUtils.rotation(
                0, -45, ibtnAddButtomGroupAdd, duration, false);
        ObjectAnimator alpha = HwArcUtils.alpha(
                1f, 0f, llAddButtomGroupAdd, duration, false);
        ObjectAnimator rotationClose = HwArcUtils.rotation(
                45, 0, ibtnAddButtomGroupClose, duration, false);
        ObjectAnimator alphaClose = HwArcUtils.alpha(
                0f, 1f, ibtnAddButtomGroupClose, duration, false);

        int move = HwArcUtils.dip2px(getContext(), 230);

        ValueAnimator[] valueAnimators = new ValueAnimator[views.size() * 2 + 6];
        valueAnimators[views.size() * 2 + 0] = rotation;
        valueAnimators[views.size() * 2 + 1] = alpha;
        valueAnimators[views.size() * 2 + 2] = rotationClose;
        valueAnimators[views.size() * 2 + 3] = alphaClose;

        ValueAnimator va = ValueAnimator.ofInt(0, 40);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int alpha = (Integer) valueAnimator.getAnimatedValue();
                vAddButtonGroup.setAlpha(alpha / 100f);
            }
        });
        valueAnimators[views.size() * 2 + 4] = va;

        ValueAnimator vaB = ValueAnimator.ofInt(0, 100);
        vaB.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int alpha = (Integer) valueAnimator.getAnimatedValue();
                vAddButtonGroupB.setAlpha(alpha / 100f);
            }
        });
        valueAnimators[views.size() * 2 + 5] = vaB;

        for (int i = views.size(); i > 0; i--) {
            valueAnimators[i - 1] = startRotate(views.get(i - 1), 500, move, views.size(), i, false);
            TextView textView = (TextView) views.get(i - 1).findViewById(R.id.tv_arc_button_item);
            valueAnimators[views.size() + i - 1] = HwArcUtils.alpha(0, 1, textView, 500, false);
        }

        HwArcUtils.animatorSetTogether(duration, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                llAddButtomGroupAdd.setEnabled(false);
                ibtnAddButtomGroupClose.setEnabled(false);
                ibtnAddButtomGroupClose.setVisibility(View.VISIBLE);

                for (int i = 0; i < views.size(); i++) {
                    views.get(i).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                llAddButtomGroupAdd.setEnabled(true);
                ibtnAddButtomGroupClose.setEnabled(true);
                llAddButtomGroupAdd.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                llAddButtomGroupAdd.setEnabled(true);
                ibtnAddButtomGroupClose.setEnabled(true);
                ibtnAddButtomGroupClose.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }, valueAnimators);


    }


    public void closeButtonAnimator() {
        if (!openState) {
            return;
        }
        openState = false;
        int duration = 500;
        ObjectAnimator rotation = HwArcUtils.rotation(
                -45, 0, ibtnAddButtomGroupAdd, duration, false);
        ObjectAnimator alpha = HwArcUtils.alpha(
                0f, 1f, llAddButtomGroupAdd, duration, false);
        ObjectAnimator rotationClose = HwArcUtils.rotation(
                0, 45, ibtnAddButtomGroupClose, duration, false);
        ObjectAnimator alphaClose = HwArcUtils.alpha(
                1f, 0f, ibtnAddButtomGroupClose, duration, false);


        int move = HwArcUtils.dip2px(getContext(), 230);

        ValueAnimator[] valueAnimators = new ValueAnimator[views.size() * 2 + 6];
        valueAnimators[views.size() * 2 + 0] = rotation;
        valueAnimators[views.size() * 2 + 1] = alpha;
        valueAnimators[views.size() * 2 + 2] = rotationClose;
        valueAnimators[views.size() * 2 + 3] = alphaClose;


        ValueAnimator va = ValueAnimator.ofInt(40, 0);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int alpha = (Integer) valueAnimator.getAnimatedValue();
                String alphaS;
                if (alpha < 10) {
                    alphaS = "0" + alpha;
                } else {
                    alphaS = "" + alpha;
                }
                vAddButtonGroup.setAlpha(alpha / 100f);
//                vAddButtonGroupB.setAlpha(alpha / 100f);
//                vAddButtonGroup.setBackgroundColor(Color.parseColor("#" + alphaS + "000000"));
            }
        });
        valueAnimators[views.size() * 2 + 4] = va;

        ValueAnimator vaB = ValueAnimator.ofInt(100, 0);
        vaB.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int alpha = (Integer) valueAnimator.getAnimatedValue();
                vAddButtonGroupB.setAlpha(alpha / 100f);
            }
        });
        valueAnimators[views.size() * 2 + 5] = vaB;

        for (int i = views.size(); i > 0; i--) {
            valueAnimators[i - 1] = startRotate(views.get(i - 1), 500, move, views.size(), i, true);
            TextView textView = (TextView) views.get(i - 1).findViewById(R.id.tv_arc_button_item);
            valueAnimators[views.size() + i - 1] = HwArcUtils.alpha(1, 0, textView, 500, false);
        }

        HwArcUtils.animatorSetTogether(duration, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                llAddButtomGroupAdd.setEnabled(false);
                ibtnAddButtomGroupClose.setEnabled(false);
                llAddButtomGroupAdd.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                llAddButtomGroupAdd.setEnabled(true);
                ibtnAddButtomGroupClose.setEnabled(true);
                ibtnAddButtomGroupClose.setVisibility(View.GONE);
                for (int i = 0; i < views.size(); i++) {
                    views.get(i).setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                llAddButtomGroupAdd.setEnabled(true);
                ibtnAddButtomGroupClose.setEnabled(true);
                llAddButtomGroupAdd.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }, valueAnimators);

    }

    private RectF rectFRotate;

    private ValueAnimator startRotate(final View view, int duration, int radius, int size, int postion, boolean back) {


        int angle = maxAngleAll / size;
        angle = angle < minAngle ? minAngle : angle;
        //获取控件当前位置

        if (rectFRotate == null) {
            int[] startLoc = new int[2];
            startLoc[0] = (int) view.getX();
            startLoc[1] = (int) view.getY();
            rectFRotate = new RectF(startLoc[0] - radius * 2, startLoc[1] - radius, startLoc[0], startLoc[1] + radius);//椭圆大小需自己调整

        }
        Path path = new Path();
        if (back) {

            path.addArc(rectFRotate, angle * postion, angle * postion * -1);
        } else {
            path.addArc(rectFRotate, 0, angle * postion);

        }

        //pathMeasure用来计算显示坐标
        final PathMeasure pathMeasure = new PathMeasure(path, false);

        Log.e("aaa", "startRotate: " + pathMeasure.getLength());
        //属性动画加载
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());

        //设置动画时长
        valueAnimator.setDuration(duration);

        //加入差值器
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());


        //添加监听
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取当前位置
                float value = (float) animation.getAnimatedValue();
                //boolean getPosTan(float distance, float[] pos, float[] tan) ：
                //传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标
                float[] mCurrentPosition = new float[2];
                pathMeasure.getPosTan(value, mCurrentPosition, null);
                //打印当前坐标
//                KLog.i(mCurrentPosition[0]+"    "+mCurrentPosition[1]);
                //设置视图坐标
                view.setX(mCurrentPosition[0]);
                view.setY(mCurrentPosition[1]);
            }
        });

        return valueAnimator;
    }

    public static interface HwArcButtonItemClick {
        public void onArcButtonItemClick(View v, int position);
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_WHAT_1010) {
                Bitmap bitmap = (Bitmap) msg.obj;
                if (Build.VERSION.SDK_INT < 16) {//16level以前使用这个方法，在16中被废弃
                    vAddButtonGroupB.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
                } else {
                    vAddButtonGroupB.setBackground(new BitmapDrawable(getResources(), bitmap));
                }
            }
        }
    };

    private static final int MESSAGE_WHAT_1010 = 1010;

    private void blur(Bitmap bkg) {
        float scaleFactor = 4;//缩放图片，缩放之后模糊效果更好
        float radius = 1;

        Matrix matrix = new Matrix();
        matrix.postScale(1f / scaleFactor, 1f / scaleFactor); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bkg, 0, 0, bkg.getWidth(), bkg.getHeight(), matrix, true);

        Bitmap bitmap = doBlur(resizeBmp, (int) radius, true);//进行高斯模糊操作

        Message message = new Message();
        message.obj = bitmap;
        message.what = MESSAGE_WHAT_1010;
        handler.sendMessage(message);


    }

    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}

