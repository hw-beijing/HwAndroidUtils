package com.hw.widget.arcbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class HwArcUtils {
    /**
     * dp 转 px
     */
    public static int dip2px(Context mContext, float dpValue) {
        final float scale =mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static AnimatorSet animatorSetTogether(long duration, Animator.AnimatorListener animatorListener, Animator... items) {
        AnimatorSet animSet = getAnimatorSetTogether(duration, items);
        if (animatorListener != null) {
            animSet.addListener(animatorListener);
        }
        animSet.start();
        return animSet;
    }

    private static AnimatorSet getAnimatorSetTogether(long duration, Animator... items) {
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(duration);
        animSet.setInterpolator(new DecelerateInterpolator());
        //多个动画同时执行
        animSet.playTogether(items);
        return animSet;
    }


    public static ObjectAnimator rotation(int startPosition, int endPosition, View view, long duration, boolean start) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", startPosition, endPosition);
        animator.setDuration(duration);
        if (start) {
            animator.start();
        }
        return animator;
    }

    public static ObjectAnimator alpha(float startPosition, float endPosition, View view, long duration, boolean start) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", startPosition,endPosition);
        animator.setDuration(duration);
        if (start) {
            animator.start();
        }
        return animator;
    }


}
