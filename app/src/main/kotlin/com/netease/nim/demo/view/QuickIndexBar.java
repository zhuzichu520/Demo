package com.netease.nim.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.netease.nim.demo.R;

public class QuickIndexBar extends View {
    private OnTouchingLetterChangedListener listener;

    private String[] letters;

    private Paint mPaint;

    private float offset;

    private boolean hit;

    private int normalColor;

    private int touchColor;

    private Drawable hintDrawable;

    public QuickIndexBar(Context paramContext) {
        this(paramContext, null);
    }

    public QuickIndexBar(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public QuickIndexBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.mPaint = new Paint();
        this.offset = 0.0F;
        this.hit = false;
        this.touchColor = Color.WHITE;

        hintDrawable = ContextCompat.getDrawable(paramContext, R.drawable.nim_contact_letter_view_hit_point);
        if (hintDrawable != null)
            hintDrawable.setBounds(0, 0, hintDrawable.getIntrinsicWidth(), hintDrawable.getIntrinsicHeight());

        TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.QuickIndexBar);
        this.normalColor = typedArray.getColor(R.styleable.QuickIndexBar_QuickIndexBar_textColor, Color.GRAY);
        typedArray.recycle();

        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(normalColor);

        letters = paramContext.getResources().getStringArray(R.array.letter_list);
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.listener = onTouchingLetterChangedListener;
    }

    public void setLetters(String[] letters) {
        this.letters = letters;
    }

    public void setNormalColor(int color) {
        this.normalColor = color;
        mPaint.setColor(normalColor);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hit = true;
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.contact_letter_idx_bg));
                mPaint.setColor(touchColor);
                onHit(event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                onHit(event.getY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onCancel();
                break;
        }
        invalidate();
        return true;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float letterHeight = ((float) getHeight()) / letters.length;
        float width = getWidth();
        float textSize = letterHeight * 2 / 3;
        mPaint.setTextSize(textSize);
        for (int i = 0; i < letters.length; ++i) {
            float halfWidth = width / 2;
            float letterPosY = letterHeight * i + textSize;
            canvas.drawText(letters[i], halfWidth, letterPosY, mPaint);
        }
        if (hit) {
            int halfWidth = getWidth() / 2;
            int halfDrawWidth = hintDrawable.getIntrinsicWidth() / 2;
            float translateX = halfWidth - halfDrawWidth;
            int halfDrawHeight = hintDrawable.getIntrinsicHeight() / 2;
            float translateY = offset - halfDrawHeight;
            canvas.save();
            canvas.translate(translateX, translateY);
            hintDrawable.draw(canvas);
            canvas.restore();
        }
    }

    private void onHit(float offset) {
        this.offset = offset;
        if (hit && listener != null) {
            int index = (int) (offset / getHeight() * letters.length);
            index = Math.max(index, 0);
            index = Math.min(index, letters.length - 1);
            String str = letters[index];
            listener.onHit(str);
        }
    }

    private void onCancel() {
        hit = false;
        setBackgroundColor(Color.TRANSPARENT);
        mPaint.setColor(this.normalColor);
        refreshDrawableState();

        if (listener != null) {
            listener.onCancel();
        }
    }

    public interface OnTouchingLetterChangedListener {
        void onHit(String letter);

        void onCancel();
    }

}