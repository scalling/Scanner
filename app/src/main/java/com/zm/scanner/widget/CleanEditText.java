package com.zm.scanner.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.zm.scanner.R;
import com.zm.scanner.util.ArmsUtils;

/**
 * @author: zengmei
 * @version: v1.0.0
 * @date: 2018/12/21
 * @description: 带清除功能的EditText
 **/
public class CleanEditText extends android.support.v7.widget.AppCompatEditText
        implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearDrawable;
    private boolean hasFocus;
    private OnFocusListener mFocusListener;

    public CleanEditText(Context context) {
        this(context.getApplicationContext(), null);
    }

    public CleanEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CleanEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.edit_clear);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] == null) return super.onTouchEvent(event);
            boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < (
                    getWidth()
                            - getPaddingRight()));
            if (touchable) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (mFocusListener != null) {
            mFocusListener.onFocusListener(v, hasFocus);
        }
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    //设置是否显示清除按钮
    public void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right,
                getCompoundDrawables()[3]);
        if (visible) {
            setCompoundDrawablePadding(ArmsUtils.dip2px(getContext(), 6));
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (hasFocus) {
            setClearIconVisible(text.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    public void setmKeyListener(OnKeyListener mKeyListener) {
        this.mKeyListener = mKeyListener;
    }

    private OnKeyListener mKeyListener;

    public interface OnKeyListener {
        void onKeyListener(EditText edt, int keyCode, KeyEvent event);
    }

    public interface OnFocusListener {
        void onFocusListener(View v, boolean hasFocus);
    }

    public void setmFocusListener(OnFocusListener mFocusListener) {
        this.mFocusListener = mFocusListener;
    }
}