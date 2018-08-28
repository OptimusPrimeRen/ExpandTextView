package com.rentee.expandtextview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * author: rentee
 * date: 2018/8/20
 * description：可展开收起TextView
 */
public class ExpandableTextView extends AppCompatTextView {
    public static final byte STATE_UNKNOWN = 0; //未知状态
    public static final byte STATE_NOT_OVERFLOW = 1; //文本行数不超过限定行数
    public static final byte STATE_COLLAPSE = 2; //文本行数超过限定行数,处于折叠状态
    public static final byte STATE_EXPAND = 3; //文本行数超过限定行数,处于展开状态

    public static final int ANIM_DURATION = 300; //展开收起的动画时间
    public static final int MAX_LINE_COUNT = 3; //收起时的最大行数

    private TimeInterpolator mInterpolator;
    private boolean mIsAnimating; //是否正在执行动画
    private int mCollapsedHeight; //收起时的高度
    private int mExpandedHeight; //展开时的高度
    private byte mState = STATE_UNKNOWN;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInterpolator = new AccelerateDecelerateInterpolator();
    }

    /**
     * 初始化state
     *
     * @param defaultState     默认展开/关闭
     * @param textStateInitListener 初始化完成后，textview state的状态
     */
    public void initTextState(final byte defaultState, final TextStateInitListener textStateInitListener) {
        setMaxLines(Integer.MAX_VALUE);
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this); //这个回调会调用多次，获取完行数记得注销监听
                if (getLineCount() <= ExpandableTextView.MAX_LINE_COUNT) { //没有超过限定行数
                    mState = STATE_NOT_OVERFLOW;
                } else {
                    mState = defaultState;  // 默认展开收起
                }
                resetState(mState);
                textStateInitListener.initFinish(mState);
                return true;
            }
        });
    }

    /**
     * 置位文字状态，不含动画
     */
    public void resetState(byte state) {
        switch (state) {
            case STATE_NOT_OVERFLOW:
                mState = STATE_NOT_OVERFLOW;
                break;
            case STATE_COLLAPSE:
                mState = STATE_COLLAPSE;
                expandAndCalHeight();
                collapseAndCalHeight();
                break;
            case STATE_EXPAND:
                mState = STATE_EXPAND;
                collapseAndCalHeight();
                expandAndCalHeight();
                break;
            default:
                break;
        }
    }

    /**
     * 在展开/收起之间进行状态切换，包含动画
     */
    public void toggle() {
        if (mState == STATE_EXPAND) {
            mState = STATE_COLLAPSE;
            collapseWithAnim();
        } else if (mState == STATE_COLLAPSE) {
            mState = STATE_EXPAND;
            expandWithAnim();
        }
    }


    /**
     * 收起并记录高度
     */
    private void collapseAndCalHeight() {
        setMaxLines(MAX_LINE_COUNT);
        measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mCollapsedHeight = getMeasuredHeight();
    }

    /**
     * 展开并记录高度
     */
    private void expandAndCalHeight() {
        setMaxLines(Integer.MAX_VALUE);
        measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mExpandedHeight = getMeasuredHeight();
    }

    /**
     * 收起并展示动画
     */
    private void collapseWithAnim() {
        if (mIsAnimating) {
            return;
        }

        mIsAnimating = true;
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(mExpandedHeight, mCollapsedHeight);
        valueAnimator.addUpdateListener(mAnimatorUpdateListener);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setMaxLines(MAX_LINE_COUNT);
                changeHeightWrapContent();
                mIsAnimating = false;
            }
        });
        valueAnimator.setInterpolator(mInterpolator);
        valueAnimator.setDuration(ANIM_DURATION).start();
    }

    /**
     * 展开并展示动画
     */
    private void expandWithAnim() {
        if (mIsAnimating) {
            return;
        }

        mIsAnimating = true;
        setMaxLines(Integer.MAX_VALUE);
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(mCollapsedHeight, mExpandedHeight);
        valueAnimator.addUpdateListener(mAnimatorUpdateListener);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                changeHeightWrapContent();
                mIsAnimating = false;
            }
        });
        valueAnimator.setInterpolator(mInterpolator);
        valueAnimator.setDuration(ANIM_DURATION).start();
    }

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            changeHeight((int) animation.getAnimatedValue());
        }
    };

    private void changeHeight(int value) {
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = value;
        setLayoutParams(layoutParams);
    }

    private void changeHeightWrapContent() {
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        setLayoutParams(layoutParams);
        invalidate();
    }

    public byte getState() {
        return mState;
    }

    public boolean isAnimating() {
        return mIsAnimating;
    }

    public interface TextStateInitListener {
        void initFinish(byte state);
    }

}
