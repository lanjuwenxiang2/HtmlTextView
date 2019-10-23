package com.ljwx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ljwx.htmltextview.R;

public final class HtmlTextView extends TextView {
    private int defualtColor = getCurrentTextColor();
    private float defualtSize = getTextSize();
    private int mNorColor;
    private float mNorSize;

    private String mLeftString = "", mCenterString = "", mRightString = "";
    private int mColorLeft = defualtColor, mColorCenter = defualtColor, mColorRight = defualtColor;
    private float mSizeLeft, mSizeCenter, mSizeRight;
    //底部margin
    private float mMarBtmLeft, mMarBtmCenter, mMarBtmRight;
    private boolean mBoldLeft, mBoldCenter, mBoldRight;
    //中间文本的左右margin
    private float mCenterTvMarginLeft, mCenterTvMarginRight;
    //模式
    private int mGravityMode;
    private int mBaseLineMode;
    private Paint mPaintLeft = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintCenter = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintRight = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMaxPaint;
    private Float mMaxBaseLine;
    //drawable宽高
    private float mDrawableLeftWidth, mDrawableRightWidth, mDrawableLeftHeight, mDrawableRightHeight;
    private float mDrawableLeftPadding, mDrawableRightPadding;
    private Drawable[] drawables;
    private float mLeftWidth, mCenterWidth, mRightWidth;
    private int mParentWidth, mViewWidth, mContentWidth;
    //字体大小自适应
    private boolean mAutoSize = false;
    private float mAutoSizeRatio = 0.01f;

    public HtmlTextView(Context context) {
        super(context, null);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.HtmlTextView);
        mNorColor = attr.getColor(R.styleable.HtmlTextView_htvTvNorColor, defualtColor);
        mNorSize = attr.getDimension(R.styleable.HtmlTextView_htvTvNorSize, defualtSize);
        mLeftString = attr.getString(R.styleable.HtmlTextView_htvLeftString);
        mCenterString = attr.getString(R.styleable.HtmlTextView_htvCenterString);
        mRightString = attr.getString(R.styleable.HtmlTextView_htvRightString);
        mColorLeft = attr.getColor(R.styleable.HtmlTextView_htvLeftColor, mNorColor);
        mColorCenter = attr.getColor(R.styleable.HtmlTextView_htvCenterColor, mNorColor);
        mColorRight = attr.getColor(R.styleable.HtmlTextView_htvRightColor, mNorColor);
        mSizeLeft = attr.getDimension(R.styleable.HtmlTextView_htvLeftSize, mNorSize);
        mSizeCenter = attr.getDimension(R.styleable.HtmlTextView_htvCenterSize, mNorSize);
        mSizeRight = attr.getDimension(R.styleable.HtmlTextView_htvRightSize, mNorSize);
        mBoldLeft = attr.getBoolean(R.styleable.HtmlTextView_htvLeftBold, false);
        mBoldCenter = attr.getBoolean(R.styleable.HtmlTextView_htvCenterBold, false);
        mBoldRight = attr.getBoolean(R.styleable.HtmlTextView_htvRightBold, false);

        mMarBtmLeft = attr.getDimension(R.styleable.HtmlTextView_htvLeftMarginBottom, 0f);
        mMarBtmCenter = attr.getDimension(R.styleable.HtmlTextView_htvCenterMarginBottom, 0f);
        mMarBtmRight = attr.getDimension(R.styleable.HtmlTextView_htvRightMarginBottom, 0f);
        mCenterTvMarginLeft = attr.getDimension(R.styleable.HtmlTextView_htvCenterMarginLeft, 0f);
        mCenterTvMarginRight = attr.getDimension(R.styleable.HtmlTextView_htvCenterMarginRight, 0f);

        mGravityMode = attr.getInt(R.styleable.HtmlTextView_htvGravityMode, 0);
        mBaseLineMode = attr.getInt(R.styleable.HtmlTextView_htvBaseLineMode, 0);

        mDrawableLeftWidth = attr.getDimension(R.styleable.HtmlTextView_htvDrawLeftWidth, 0f);
        mDrawableLeftHeight = attr.getDimension(R.styleable.HtmlTextView_htvDrawLeftHeight, 0f);
        mDrawableLeftPadding = attr.getDimension(R.styleable.HtmlTextView_htvDrawLeftPadding, 0f);
        mDrawableRightWidth = attr.getDimension(R.styleable.HtmlTextView_htvDrawRightWidth, 0f);
        mDrawableRightHeight = attr.getDimension(R.styleable.HtmlTextView_htvDrawRightHeight, 0f);
        mDrawableRightPadding = attr.getDimension(R.styleable.HtmlTextView_htvDrawRightPadding, 0f);

        mAutoSize = attr.getBoolean(R.styleable.HtmlTextView_htvAutoSize, false);
        mAutoSizeRatio = attr.getDimension(R.styleable.HtmlTextView_htvAutoSizeRatio, 0.01f);

        attr.recycle();
        init();
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaintLeft.setFakeBoldText(mBoldLeft);
        mPaintLeft.setTextSize(mSizeLeft);
        mPaintLeft.setColor(mColorLeft);
        mPaintLeft.setTypeface(getTypeface());

        mPaintCenter.setFakeBoldText(mBoldCenter);
        mPaintCenter.setTextSize(mSizeCenter);
        mPaintCenter.setColor(mColorCenter);
        mPaintCenter.setTypeface(getTypeface());

        mPaintRight.setFakeBoldText(mBoldRight);
        mPaintRight.setTextSize(mSizeRight);
        mPaintRight.setColor(mColorRight);
        mPaintRight.setTypeface(getTypeface());
        initMaxPaint();
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

    /**
     * 计算最大高度
     */
    private void initMaxPaint() {
        mMaxPaint = mPaintLeft;
        mMaxPaint = mPaintCenter.getTextSize() >= mPaintLeft.getTextSize() ? mPaintCenter : mPaintLeft;
        mMaxPaint = mPaintRight.getTextSize() >= mMaxPaint.getTextSize() ? mPaintRight : mMaxPaint;
        if (mMaxPaint == null) {
            mMaxPaint = getPaint();
        }
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        String txt = getText().toString();
        if (MeasureSpec.AT_MOST == heightMode || MeasureSpec.UNSPECIFIED == heightMode) {
            float ptm = (float) (getPaddingTop() + getPaddingBottom() + 0.5);
            if (!TextUtils.isEmpty(txt)) {
//                height = (int) (getPaint().descent() - getPaint().ascent() + ptm);
                height = getMeasuredHeight();
            } else {
                height = (int) (mMaxPaint.descent() - mMaxPaint.ascent() + ptm);
            }
        }

        if (MeasureSpec.AT_MOST == widthMode) {
            if (!TextUtils.isEmpty(txt)) {
//                width = (int) (getPaint().measureText(getText().toString()) + plr);
                width = getMeasuredWidth();
            } else {
                width = contentWidth();
            }
        }
        height = (int) Math.max(height, Math.max(mDrawableLeftHeight, mDrawableRightHeight));
        setMeasuredDimension(width, height);
        contentWidth();
    }

    /**
     * 计算文本宽度
     */
    private int contentWidth() {
        float plr = (float) (getPaddingLeft() + getPaddingRight() + 0.5);
        float w1 = 0, w2 = 0, w3 = 0;
        if (!TextUtils.isEmpty(mLeftString)) {
            w1 = mPaintLeft.measureText(mLeftString);
        }
        if (!TextUtils.isEmpty(mCenterString)) {
            w2 = mPaintCenter.measureText(mCenterString);
        }
        if (!TextUtils.isEmpty(mRightString)) {
            w3 = mPaintRight.measureText(mRightString);
        }
        mContentWidth = (int) (w1 + w2 + w3 + plr
                + mCenterTvMarginLeft + mCenterTvMarginRight
                + mDrawableLeftWidth + mDrawableRightWidth);
        return mContentWidth;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth();
    }

    /**
     * 布局宽度
     */
    private void viewWidth() {
        mParentWidth = 0;
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            mParentWidth = parent.getWidth();
        }
        mViewWidth = Math.max(getWidth(), getMeasuredWidth());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTv(canvas);
        setDrawable(canvas);
    }

    /**
     * 绘制文字
     */
    private void drawTv(Canvas canvas) {
        if (TextUtils.isEmpty(getText())) {
            autoSize();
            mMaxBaseLine = (getHeight() / 2) + (Math.abs(mMaxPaint.ascent() + mMaxPaint.descent()) / 2);
            this.drawLeft(canvas);
            this.drawCenter(canvas);
            this.drawRight(canvas);
        }

    }

    /**
     * 自适应字体大小
     */
    private void autoSize() {
        if (mAutoSize) {
            //等比例缩放
            float s1 = mPaintLeft.getTextSize() * mAutoSizeRatio;
            float s2 = mPaintCenter.getTextSize() * mAutoSizeRatio;
            float s3 = mPaintRight.getTextSize() * mAutoSizeRatio;
            for (int i = 0; i < 50; i++) {
                if (mContentWidth > mParentWidth || mContentWidth > mViewWidth) {
                    mPaintLeft.setTextSize(mPaintLeft.getTextSize() - s1);
                    mPaintCenter.setTextSize(mPaintCenter.getTextSize() - s2);
                    mPaintRight.setTextSize(mPaintRight.getTextSize() - s3);
                } else {
                    break;
                }
                //重新计算
                contentWidth();
            }
            requestLayout();
        }
    }

    /**
     * 左边文字
     */
    private final void drawLeft(Canvas canvas) {
        if (!TextUtils.isEmpty(mLeftString)) {
            float xStartPoint = getPaddingLeft() + mDrawableLeftWidth + mDrawableLeftPadding;
            canvas.drawText(mLeftString, xStartPoint, countBaseLine(mPaintLeft) - mMarBtmLeft, mPaintLeft);
            mLeftWidth = xStartPoint + mPaintLeft.measureText(mLeftString);
        }
    }

    private final void drawCenter(Canvas canvas) {
        if (!TextUtils.isEmpty(mCenterString)) {
            float xStartPoint = mCenterTvMarginLeft + mLeftWidth;
            //填满
            if (mGravityMode != 0) {
                int midx = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
                xStartPoint = midx - mPaintCenter.measureText(mCenterString) / 2;
                //填满没有right时
                if (TextUtils.isEmpty(mRightString)) {
                    xStartPoint = getWidth() - getPaddingRight() - mPaintCenter.measureText(mCenterString)
                            - mDrawableRightWidth - mDrawableRightPadding;
                }
            }
            canvas.drawText(mCenterString, xStartPoint, countBaseLine(mPaintCenter) - mMarBtmCenter, mPaintCenter);
            mCenterWidth = mCenterTvMarginLeft + mCenterTvMarginRight + mPaintCenter.measureText(mCenterString);
        }
    }

    private final void drawRight(Canvas canvas) {
        if (!TextUtils.isEmpty(mRightString)) {
            float xStartPoint = mLeftWidth + mCenterWidth;
            //填满
            if (mGravityMode != 0) {
                xStartPoint = (float) (this.getWidth() - this.getPaddingRight()) - mPaintRight.measureText(mRightString) - mDrawableRightWidth - mDrawableRightPadding;
            }
            canvas.drawText(mRightString, xStartPoint, countBaseLine(mPaintRight) - mMarBtmRight, mPaintRight);
            mRightWidth = mPaintRight.measureText(mRightString);
        }
    }

    /**
     * 基线对齐
     */
    private final float countBaseLine(Paint paint) {
        float tvLine = (getHeight() / 2) + Math.abs(paint.ascent() + paint.descent()) / 2;
        return mBaseLineMode == 0 ? mMaxBaseLine : tvLine;
    }

    /**
     * drawable图片宽高
     */
    private void setDrawable(Canvas canvas) {
        if (drawables[0] != null) {
            Drawable left = drawables[0];
            int paddingLeft = getPaddingLeft();
            //固定宽高
            if (mDrawableLeftWidth > 0 && mDrawableLeftHeight > 0) {
                int topPoint = (int) (getHeight() / 2 - mDrawableLeftHeight / 2);
                left.setBounds(paddingLeft, topPoint, (int) mDrawableLeftWidth + paddingLeft, (int) mDrawableLeftHeight + topPoint);
            } else {
                //原生宽高
                int topPoint = getHeight() / 2 - left.getIntrinsicHeight() / 2;
                drawables[0].setBounds(paddingLeft, topPoint, left.getIntrinsicWidth() + paddingLeft, left.getIntrinsicHeight() + topPoint);
            }

            left.draw(canvas);
        }
        if (drawables[2] != null) {
            Drawable right = drawables[2];
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            //固定宽高
            if (mDrawableRightWidth > 0 && mDrawableRightHeight > 0) {
                float rightStartPoint = mLeftWidth + mCenterWidth + mRightWidth + mDrawableRightPadding;
                //空间是否足够,紧凑模式
                int leftPoint = (int) ((getWidth() >= rightStartPoint + mDrawableRightWidth && mGravityMode == 0) ? (rightStartPoint) : (getWidth() - mDrawableRightWidth));
                int topPoint = (int) (getHeight() / 2 - mDrawableRightHeight / 2);
                right.setBounds(leftPoint - paddingRight, topPoint, (int) mDrawableRightWidth + leftPoint - paddingRight, (int) mDrawableRightHeight + topPoint);
            } else {
                //原生宽高
                int topPoint = getHeight() / 2 - right.getIntrinsicHeight() / 2;
                right.setBounds(paddingLeft - paddingRight, topPoint, right.getIntrinsicWidth() + paddingLeft - paddingRight, right.getIntrinsicHeight() + topPoint);
            }
            right.draw(canvas);
        }
    }

    public String getLeftString() {
        return mLeftString;
    }

    public void setLeftString(String mTv1String) {
        this.mLeftString = mTv1String;
        requestLayout();
    }

    public String getCenterString() {
        return mCenterString;
    }

    public void setCenterString(String mTv2String) {
        this.mCenterString = mTv2String;
        requestLayout();
    }

    public String getRightString() {
        return mRightString;
    }

    public void setRightString(String mTv3String) {
        this.mRightString = mTv3String;
        requestLayout();
    }

    public float getSizeLeft() {
        return mSizeLeft;
    }

    public void setSizeLeft(float mTv1Size) {
        mPaintLeft.setTextSize(mTv1Size);
        requestLayout();
    }

    public float getSizeCenter() {
        return mSizeCenter;
    }

    public void setSizeCenter(float centerSize) {
        mPaintCenter.setTextSize(centerSize);
        requestLayout();
    }

    public float getSizeRight() {
        return mSizeRight;
    }

    public void setSizeRight(float rightSize) {
        mPaintRight.setTextSize(rightSize);
        requestLayout();
    }
}
