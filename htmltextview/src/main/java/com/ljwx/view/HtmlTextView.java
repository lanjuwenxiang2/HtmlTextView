package com.ljwx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.ljwx.htmltextview.R;

public final class HtmlTextView extends AppCompatTextView {
    private int defualtColor = Color.parseColor("#ff333333");
    private float defualtSize = getTextSize();
    private String mTv1, mTv2, mTv3;
    private int mTv1Color, mTv2Color, mTv3Color;
    private float mTv1Size, mTv2Size, mTv3Size;
    private float tv1MarBtm, tv2MarBtm, tv3MarBtm;
    private boolean tv1Bold, tv2Bold, tv3Bold;
    private float mMarginL, mMarginR;
    private int mGravityMode;
    private float mTv2Offset;
    private int mLineMode;
    private Paint mPaint1, mPaint2, mPaint3;
    private Paint mMaxPaint;
    private Float mMaxBaseLine;

    public HtmlTextView(Context context) {
        super(context, null);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.HtmlTextView);
        mTv1 = attr.getString(R.styleable.HtmlTextView_htvTv1String);
        mTv2 = attr.getString(R.styleable.HtmlTextView_htvTv2String);
        mTv3 = attr.getString(R.styleable.HtmlTextView_htvTv3String);
        mTv1Color = attr.getColor(R.styleable.HtmlTextView_htvTv1Color, defualtColor);
        mTv2Color = attr.getColor(R.styleable.HtmlTextView_htvTv2Color, defualtColor);
        mTv3Color = attr.getColor(R.styleable.HtmlTextView_htvTv3Color, defualtColor);
        mTv1Size = attr.getDimension(R.styleable.HtmlTextView_htvTv1Size, defualtSize);
        mTv2Size = attr.getDimension(R.styleable.HtmlTextView_htvTv2Size, defualtSize);
        mTv3Size = attr.getDimension(R.styleable.HtmlTextView_htvTv3Size, defualtSize);
        tv1Bold = attr.getBoolean(R.styleable.HtmlTextView_htvTv1Bold, false);
        tv2Bold = attr.getBoolean(R.styleable.HtmlTextView_htvTv2Bold, false);
        tv3Bold = attr.getBoolean(R.styleable.HtmlTextView_htvTv3Bold, false);
        tv1MarBtm = attr.getDimension(R.styleable.HtmlTextView_htvTv1MarginBottom, 0f);
        tv2MarBtm = attr.getDimension(R.styleable.HtmlTextView_htvTv2MarginBottom, 0f);
        tv3MarBtm = attr.getDimension(R.styleable.HtmlTextView_htvTv3MarginBottom, 0f);
        mMarginL = attr.getDimension(R.styleable.HtmlTextView_htvTv2MarginL, 0f);
        mMarginR = attr.getDimension(R.styleable.HtmlTextView_htvTv2MarginR, 0f);
        mGravityMode = attr.getInt(R.styleable.HtmlTextView_htvGravityMode, 0);
        mTv2Offset = attr.getDimension(R.styleable.HtmlTextView_htvTv2Offset, 0f);
        mLineMode = attr.getInt(R.styleable.HtmlTextView_htvBaseLineMode, 0);
        initPaint();
        attr.recycle();
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaint() {
        if (!TextUtils.isEmpty(mTv1)) {
            mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint1.setFakeBoldText(tv1Bold);
            mPaint1.setTextSize(mTv1Size);
            mPaint1.setColor(mTv1Color);
            mMaxPaint = mPaint1;
        }
        if (!TextUtils.isEmpty(mTv2) && !TextUtils.isEmpty(mTv1)) {
            mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint2.setFakeBoldText(tv2Bold);
            mPaint2.setTextSize(mTv2Size);
            mPaint2.setColor(mTv2Color);
            mMaxPaint = mPaint2.getTextSize() >= mPaint1.getTextSize() ? mPaint2 : mPaint1;
        }
        if (!TextUtils.isEmpty(mTv3) && !TextUtils.isEmpty(mTv1) && !TextUtils.isEmpty(mTv3)) {
            mPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint3.setFakeBoldText(tv3Bold);
            mPaint3.setTextSize(mTv3Size);
            mPaint3.setColor(mTv3Color);
            mMaxPaint = mPaint3.getTextSize() >= mMaxPaint.getTextSize() ? mPaint3 : mMaxPaint;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        String txt = getText().toString();
        if (MeasureSpec.AT_MOST == heightMode) {
            float ptm = (float) (getPaddingTop() + getPaddingBottom() + 0.5);
            if (!TextUtils.isEmpty(txt)) {
                height = (int) (getPaint().descent() - getPaint().ascent() + ptm);
            } else {
                height = (int) (mMaxPaint.descent() - mMaxPaint.ascent() + ptm);
            }
        }

        if (MeasureSpec.AT_MOST == widthMode) {
            float plr = (float) (getPaddingLeft() + getPaddingRight() + 0.5);
            if (!TextUtils.isEmpty(txt)) {
                width = (int) (getPaint().measureText(getText().toString()) + plr);
            } else {
                float w1 = 0, w2 = 0, w3 = 0;
                if (!TextUtils.isEmpty(mTv1)) {
                    w1 = mPaint1.measureText(mTv1);
                }
                if (!TextUtils.isEmpty(mTv2)) {
                    w2 = mPaint2.measureText(mTv2);
                }
                if (!TextUtils.isEmpty(mTv3)) {
                    w3 = mPaint3.measureText(mTv3);
                }
                width = (int) (w1 + w2 + w3 + plr + mMarginL + mMarginR);
            }
        }
        setMeasuredDimension(width, height);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTv(canvas);
    }

    private void drawTv(Canvas canvas) {
        if (TextUtils.isEmpty(getText())) {
            mMaxBaseLine = (getHeight() / 2) + (Math.abs(mMaxPaint.ascent() + mMaxPaint.descent()) / 2);
            this.drawTv1(canvas);
            this.drawTv2(canvas);
            this.drawTv3(canvas);
        }

    }

    private final void drawTv1(Canvas canvas) {
        if (mTv1 != null) {
            canvas.drawText(mTv1, (float) getPaddingLeft(), countBaseLine(mPaint1) - tv1MarBtm, mPaint1);
        }

    }

    private final void drawTv2(Canvas canvas) {
        if (mTv1 != null && mTv2 != null) {
            float lx2 = getPaddingLeft() + mPaint1.measureText(mTv1) + mMarginL;
            int midx = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
            if (mGravityMode != 0) {
                lx2 = midx - mPaint2.measureText(mTv2) / 2;
                if (mTv2Offset != 0) {
                    lx2 = midx - mTv2Offset;
                }

                if (this.mTv3 == null) {
                    lx2 = getWidth() - getPaddingRight() - mPaint2.measureText(mTv2);
                }
            }

            canvas.drawText(mTv2, lx2, countBaseLine(mPaint2) - tv2MarBtm, mPaint2);
        }

    }

    private final void drawTv3(Canvas canvas) {
        if (mTv1 != null && mTv2 != null && mTv3 != null) {
            float lx3 = getPaddingLeft() + mMarginL + mMarginR + mPaint1.measureText(mTv1) + mPaint2.measureText(mTv2);
            if (mGravityMode != 0) {
                lx3 = (float) (this.getWidth() - this.getPaddingRight()) - mPaint3.measureText(mTv3);
            }
            canvas.drawText(mTv3, lx3, countBaseLine(mPaint3) - tv3MarBtm, mPaint3);
        }

    }

    private final float countBaseLine(Paint paint) {
        float tvLine = (getHeight() / 2) + Math.abs(paint.ascent() + paint.descent()) / 2;
        return mLineMode == 0 ? mMaxBaseLine : tvLine;
    }

}
