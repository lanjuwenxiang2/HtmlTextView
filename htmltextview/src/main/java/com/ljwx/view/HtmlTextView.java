package com.ljwx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.ljwx.htmltextview.R;

public final class HtmlTextView extends AppCompatTextView {
    private int defualtColor = getCurrentTextColor();
    private float defualtSize = getTextSize();
    private int mNorColor;
    private float mNorSize;
    private String mTvString, mSplitString;
    private String mTv1String, mTv2String, mTv3String;
    private int mTv1Color = defualtColor, mTv2Color = defualtColor, mTv3Color = defualtColor;
    private float mTv1Size, mTv2Size, mTv3Size;
    private float tv1MarBtm, tv2MarBtm, tv3MarBtm;
    private boolean tv1Bold, tv2Bold, tv3Bold;
    private float mTv2MarginLeft, mTv2MarginRight;
    private int mGravityMode;
    private float mTv2Offset;
    private int mLineMode;
    private Paint mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMaxPaint;
    private Float mMaxBaseLine;
    private float mDrawableLeftWidth, mDrawableRightWidth, mDrawableLeftHeight, mDrawableRightHeight;
    private Drawable[] drawables;
    float mDrawRightX = 0;

    public HtmlTextView(Context context) {
        super(context, null);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.HtmlTextView);
        mNorColor = attr.getColor(R.styleable.HtmlTextView_htvTvNorColor, defualtColor);
        mNorSize = attr.getDimension(R.styleable.HtmlTextView_htvTvNorSize, defualtSize);
        mTvString = attr.getString(R.styleable.HtmlTextView_htvTvString);
        mSplitString = attr.getString(R.styleable.HtmlTextView_htvTvSplitString);
        mTv1String = attr.getString(R.styleable.HtmlTextView_htvLeftString);
        mTv2String = attr.getString(R.styleable.HtmlTextView_htvCenterString);
        mTv3String = attr.getString(R.styleable.HtmlTextView_htvRightString);
        mTv1Color = attr.getColor(R.styleable.HtmlTextView_htvLeftColor, mNorColor);
        mTv2Color = attr.getColor(R.styleable.HtmlTextView_htvCenterColor, mNorColor);
        mTv3Color = attr.getColor(R.styleable.HtmlTextView_htvRightColor, mNorColor);
        mTv1Size = attr.getDimension(R.styleable.HtmlTextView_htvLeftSize, mNorSize);
        mTv2Size = attr.getDimension(R.styleable.HtmlTextView_htvCenterSize, mNorSize);
        mTv3Size = attr.getDimension(R.styleable.HtmlTextView_htvRightSize, mNorSize);
        tv1Bold = attr.getBoolean(R.styleable.HtmlTextView_htvLeftBold, false);
        tv2Bold = attr.getBoolean(R.styleable.HtmlTextView_htvCenterBold, false);
        tv3Bold = attr.getBoolean(R.styleable.HtmlTextView_htvRightBold, false);

        tv1MarBtm = attr.getDimension(R.styleable.HtmlTextView_htvLeftMarginBottom, 0f);
        tv2MarBtm = attr.getDimension(R.styleable.HtmlTextView_htvCenterMarginBottom, 0f);
        tv3MarBtm = attr.getDimension(R.styleable.HtmlTextView_htvRightMarginBottom, 0f);
        mTv2MarginLeft = attr.getDimension(R.styleable.HtmlTextView_htvCenterMarginLeft, 0f);
        mTv2MarginRight = attr.getDimension(R.styleable.HtmlTextView_htvCenterMarginRight, 0f);

        mGravityMode = attr.getInt(R.styleable.HtmlTextView_htvGravityType, 0);
        mTv2Offset = attr.getDimension(R.styleable.HtmlTextView_htvCenterOffset, 0f);
        mLineMode = attr.getInt(R.styleable.HtmlTextView_htvBaseLineType, 0);

        mDrawableLeftWidth = attr.getDimension(R.styleable.HtmlTextView_htvDrawLeftWidth, 0f);
        mDrawableLeftHeight = attr.getDimension(R.styleable.HtmlTextView_htvDrawLeftHeight, 0f);
        mDrawableRightWidth = attr.getDimension(R.styleable.HtmlTextView_htvDrawRightWidth, 0f);
        mDrawableRightHeight = attr.getDimension(R.styleable.HtmlTextView_htvDrawRightHeight, 0f);

        attr.recycle();
        init();
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        if (!TextUtils.isEmpty(mTv1String)) {
            mPaint1.setFakeBoldText(tv1Bold);
            mPaint1.setTextSize(mTv1Size);
            mPaint1.setColor(mTv1Color);
            mMaxPaint = mPaint1;
        }
        if (!TextUtils.isEmpty(mTv2String) && !TextUtils.isEmpty(mTv1String)) {
            mPaint2.setFakeBoldText(tv2Bold);
            mPaint2.setTextSize(mTv2Size);
            mPaint2.setColor(mTv2Color);
            mMaxPaint = mPaint2.getTextSize() >= mPaint1.getTextSize() ? mPaint2 : mPaint1;
        }
        if (!TextUtils.isEmpty(mTv3String) && !TextUtils.isEmpty(mTv1String) && !TextUtils.isEmpty(mTv3String)) {
            mPaint3.setFakeBoldText(tv3Bold);
            mPaint3.setTextSize(mTv3Size);
            mPaint3.setColor(mTv3Color);
            mMaxPaint = mPaint3.getTextSize() >= mMaxPaint.getTextSize() ? mPaint3 : mMaxPaint;
        }
        if (mMaxPaint == null) {
            mMaxPaint = getPaint();
        }
        if (mTvString != null && mSplitString != null) {
            if (mTvString.contains(mSplitString)) {
                String[] split = mTvString.split(mSplitString);
                if (split.length == 1) {
                    mTv1String = split[0];
                    mTv2String = mSplitString;
                }
                if (split.length == 2) {
                    mTv1String = split[0];
                    mTv2String = mSplitString;
                    mTv3String = split[1];
                }
            }
        }
        initDrawable();
    }

    private void initDrawable() {
        drawables = getCompoundDrawables();
        setCompoundDrawables(null, drawables[1], null, drawables[3]);
        if (drawables[0] != null) {
            mDrawableLeftWidth = (mDrawableLeftWidth > 0) ? (mDrawableLeftWidth) : (drawables[0].getIntrinsicWidth());
            mDrawableLeftHeight = (mDrawableLeftHeight > 0) ? (mDrawableLeftHeight) : (drawables[0].getIntrinsicHeight());
        }
        if (drawables[2] != null) {
            mDrawableRightWidth = (mDrawableRightWidth > 0) ? (mDrawableRightWidth) : (drawables[2].getIntrinsicWidth());
            mDrawableRightHeight = (mDrawableRightHeight > 0) ? (mDrawableRightHeight) : (drawables[2].getIntrinsicHeight());
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
//                height = (int) (getPaint().descent() - getPaint().ascent() + ptm);
                height = getMeasuredHeight();
            } else {
                height = (int) (mMaxPaint.descent() - mMaxPaint.ascent() + ptm);
            }
        }

        if (MeasureSpec.AT_MOST == widthMode) {
            float plr = (float) (getPaddingLeft() + getPaddingRight() + 0.5);
            if (!TextUtils.isEmpty(txt)) {
//                width = (int) (getPaint().measureText(getText().toString()) + plr);
                width = getMeasuredWidth();
            } else {
                float w1 = 0, w2 = 0, w3 = 0;
                if (!TextUtils.isEmpty(mTv1String)) {
                    w1 = mPaint1.measureText(mTv1String);
                }
                if (!TextUtils.isEmpty(mTv2String)) {
                    w2 = mPaint2.measureText(mTv2String);
                }
                if (!TextUtils.isEmpty(mTv3String)) {
                    w3 = mPaint3.measureText(mTv3String);
                }
                width = (int) (w1 + w2 + w3 + plr + mTv2MarginLeft + mTv2MarginRight
                        + mDrawableLeftWidth + mDrawableRightWidth);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("ljwx", "onlayout");
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTv(canvas);
        setDrawable(canvas);
        Log.e("ljwx", "onDraw");
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
        if (mTv1String != null) {
            float x = getPaddingLeft() + mDrawableLeftWidth;
            mDrawRightX = x + mPaint1.measureText(mTv1String);
            canvas.drawText(mTv1String, x, countBaseLine(mPaint1) - tv1MarBtm, mPaint1);
        }

    }

    private final void drawTv2(Canvas canvas) {
        if (mTv1String != null && mTv2String != null) {
            float lx2 = getPaddingLeft() + mPaint1.measureText(mTv1String) + mTv2MarginLeft + mDrawableLeftWidth;
            int midx = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
            if (mGravityMode != 0) {
                lx2 = midx - mPaint2.measureText(mTv2String) / 2;
                if (mTv2Offset != 0) {
                    lx2 = midx - mTv2Offset;
                }

                if (this.mTv3String == null) {
                    lx2 = getWidth() - getPaddingRight() - mPaint2.measureText(mTv2String);
                }
            }
            mDrawRightX = lx2 + mPaint2.measureText(mTv2String);
            canvas.drawText(mTv2String, lx2, countBaseLine(mPaint2) - tv2MarBtm, mPaint2);
        }

    }

    private final void drawTv3(Canvas canvas) {
        if (mTv1String != null && mTv2String != null && mTv3String != null) {
            float lx3 = getPaddingLeft() + mTv2MarginLeft + mTv2MarginRight +
                    mPaint1.measureText(mTv1String) + mPaint2.measureText(mTv2String) + mDrawableLeftWidth;
            if (mGravityMode != 0) {
                lx3 = (float) (this.getWidth() - this.getPaddingRight()) - mPaint3.measureText(mTv3String);
            }
            mDrawRightX = lx3 + mPaint3.measureText(mTv3String);
            canvas.drawText(mTv3String, lx3, countBaseLine(mPaint3) - tv3MarBtm, mPaint3);
        }
    }

    private final float countBaseLine(Paint paint) {
        float tvLine = (getHeight() / 2) + Math.abs(paint.ascent() + paint.descent()) / 2;
        return mLineMode == 0 ? mMaxBaseLine : tvLine;
    }

    private void setDrawable(Canvas canvas) {
        if (drawables[0] != null) {
            Drawable left = drawables[0];
            int x = getPaddingLeft();
            int y = getHeight() / 2 - left.getIntrinsicHeight() / 2;
            drawables[0].setBounds(x, y, left.getIntrinsicWidth() + x, left.getIntrinsicHeight() + y);
            if (mDrawableLeftWidth > 0 && mDrawableLeftHeight > 0) {
                int y2 = (int) (getHeight() / 2 - mDrawableLeftHeight / 2);
                left.setBounds(x, y2, (int) mDrawableLeftWidth + x, (int) mDrawableLeftHeight + y2);
                left.draw(canvas);
            } else {
                left.draw(canvas);
            }
        }
        if (drawables[2] != null) {
            Drawable right = drawables[2];
            int x = getPaddingLeft();
            int y = getHeight() / 2 - right.getIntrinsicHeight() / 2;
            drawables[0].setBounds(x, y, right.getIntrinsicWidth() + x, right.getIntrinsicHeight() + y);
            if (mDrawableRightWidth > 0 && mDrawableRightHeight > 0) {
                int x2 = (int) ((getWidth() >= mDrawRightX + mDrawableRightWidth) ? (mDrawRightX) : (getWidth() - mDrawableRightWidth));
                int y2 = (int) (getHeight() / 2 - mDrawableRightHeight / 2);
                right.setBounds(x2, y2, (int) mDrawableRightWidth + x2, (int) mDrawableRightHeight + y2);
                right.draw(canvas);
            } else {
                right.draw(canvas);
            }
        }
    }

    public String getTv1String() {
        return mTv1String;
    }

    public void setTv1String(String mTv1String) {
        this.mTv1String = mTv1String;
        invalidate();
    }

    public String getTv2String() {
        return mTv2String;
    }

    public void setTv2String(String mTv2String) {
        this.mTv2String = mTv2String;
        invalidate();
    }

    public String getTv3String() {
        return mTv3String;
    }

    public void setTv3String(String mTv3String) {
        this.mTv3String = mTv3String;
        invalidate();
    }

    public float getTv1Size() {
        return mTv1Size;
    }

    public void setTv1Size(float mTv1Size) {
        mPaint1.setTextSize(mTv1Size);
        requestLayout();
    }

    public float getTv2Size() {
        return mTv2Size;
    }

    public void setTv2Size(float mTv2Size) {
        mPaint2.setTextSize(mTv2Size);
        requestLayout();
    }

    public float getTv3Size() {
        return mTv3Size;
    }

    public void setTv3Size(float mTv3Size) {
        mPaint3.setTextSize(mTv3Size);
        requestLayout();
    }
}
