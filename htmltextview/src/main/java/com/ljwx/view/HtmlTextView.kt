package com.ljwx.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.Log
import com.ljwx.htmltextview.R

class HtmlTextView : AppCompatTextView {

    var defualtColor = Color.parseColor("#ff333333")
    var defualtSize = textSize

    var tv1: String? = null
    var tv2: String? = null
    var tv3: String? = null
    private val mPaint1 by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }
    private val mPaint2 by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }
    private val mPaint3 by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }
    //确定最大字体
    private val mBigP by lazy {
        var p =
                if (mPaint1.textSize >= mPaint2.textSize) {
                    if (mPaint1.textSize >= mPaint3.textSize) {
                        mPaint1
                    } else {
                        mPaint3
                    }
                } else {
                    if (mPaint2.textSize >= mPaint3.textSize) {
                        mPaint2
                    } else {
                        mPaint3
                    }
                }
        p
    }
    private val mBigLine by lazy {
        height / 2 + Math.abs(mBigP.ascent() + mBigP.descent()) / 2
    }
    var marginL = 0f
    var marginR = 0f
    var mGravityMode = 0
    var mTv2Offset = 0f
    var mLineMode = 0
    private var mDrawaLeft: Drawable? = null
    var mLeftWidth = 60
    var mLeftHeight = 60

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.HtmlTextView)
        tv1 = attr.getString(R.styleable.HtmlTextView_htvTv1String)
        tv2 = attr.getString(R.styleable.HtmlTextView_htvTv2String)
        tv3 = attr.getString(R.styleable.HtmlTextView_htvTv3String)
        mPaint1.color = attr.getColor(R.styleable.HtmlTextView_htvTv1Color, defualtColor)
        mPaint2.color = attr.getColor(R.styleable.HtmlTextView_htvTv2Color, defualtColor)
        mPaint3.color = attr.getColor(R.styleable.HtmlTextView_htvTv3Color, defualtColor)
        mPaint1.textSize = attr.getDimension(R.styleable.HtmlTextView_htvTv1Size, defualtSize)
        mPaint2.textSize = attr.getDimension(R.styleable.HtmlTextView_htvTv2Size, defualtSize)
        mPaint3.textSize = attr.getDimension(R.styleable.HtmlTextView_htvTv3Size, defualtSize)
        var b1 = attr.getBoolean(R.styleable.HtmlTextView_htvTv1Bold, false)
        var b2 = attr.getBoolean(R.styleable.HtmlTextView_htvTv2Bold, false)
        var b3 = attr.getBoolean(R.styleable.HtmlTextView_htvTv3Bold, false)
//        mPaint2.isFakeBoldText = true
        mPaint1.typeface = if (b1) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        mPaint2.typeface = if (b2) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        mPaint3.typeface = if (b3) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        marginL = attr.getDimension(R.styleable.HtmlTextView_htvTv2MarginL, 0f)
        marginR = attr.getDimension(R.styleable.HtmlTextView_htvTv2MarginR, 0f)
        mGravityMode = attr.getInt(R.styleable.HtmlTextView_htvGravityMode, 0)
        mTv2Offset = attr.getDimension(R.styleable.HtmlTextView_htvTv2Offset, 0f)
        mLineMode = attr.getInt(R.styleable.HtmlTextView_htvBaseLineMode, 0)
        mDrawaLeft = attr.getDrawable(R.styleable.HtmlTextView_htvDrawLeft)
        attr.recycle()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var heigthMode = MeasureSpec.getMode(heightMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)

        var height = MeasureSpec.getSize(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)

        val txt = text.toString()
        if (MeasureSpec.AT_MOST == heigthMode) {
            height = if (txt.isNotEmpty()) {
                (paint.descent() - paint.ascent()).toInt()
            } else {
                (mBigP.descent() - mBigP.ascent()).toInt() + paddingTop + paddingBottom
            }
        }

        if (MeasureSpec.AT_MOST == widthMode) {
            width = if (txt.isNotEmpty()) {
                paint.measureText(text.toString()).toInt()
            } else {
                var m1 = 0f
                var m2 = 0f
                var m3 = 0f
                tv1?.let {
                    m1 = mPaint1.measureText(it)
                }
                tv2?.let {
                    m2 = mPaint2.measureText(it)
                }
                tv3?.let {
                    m3 = mPaint3.measureText(it)
                }
                (m1 + m2 + m3 + marginL + marginR).toInt() + paddingLeft + paddingRight
            }
        }

        if (mDrawaLeft != null) {
            mDrawaLeft?.setBounds(0, 0, mLeftWidth, mLeftHeight)

        }
        drawImage()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawTv(canvas)
    }

    private fun drawImage() {
        setCompoundDrawables(mDrawaLeft, null, null, null)
    }

    private fun drawTv(canvas: Canvas) {
        if (text.toString().isEmpty()) {
            drawTv1(canvas)
            drawTv2(canvas)
            drawTv3(canvas)
        }
    }

    private fun drawTv1(canvas: Canvas) {
        if (tv1 != null) {
            canvas.drawText(tv1, paddingLeft.toFloat(), countBaseLine(mPaint1), mPaint1)
        }
    }

    private fun drawTv2(canvas: Canvas) {
        if (tv1 != null && tv2 != null) {
            var lx2 = paddingLeft + mPaint1.measureText(tv1) + marginL
            var midx = (width - paddingLeft - paddingRight) / 2
            //位置是否均分
            if (mGravityMode != 0) {
                lx2 = midx - (mPaint2.measureText(tv2) / 2)
                //偏移
                if (mTv2Offset != 0f) {
                    lx2 = midx - mTv2Offset
                }
                //只有两段内容
                if (tv3 == null) {
                    lx2 = width - paddingRight - mPaint2.measureText(tv2)
                }
            }
            canvas.drawText(tv2, lx2, countBaseLine(mPaint2), mPaint2)
        }
    }

    private fun drawTv3(canvas: Canvas) {
        if (tv1 != null && tv2 != null && tv3 != null) {
            var lx3 = paddingLeft + marginL + marginR + mPaint1.measureText(tv1) + mPaint2.measureText(tv2)
            if (mGravityMode != 0) {
                lx3 = width - paddingRight - mPaint3.measureText(tv3)
            }
            canvas.drawText(tv3, lx3, countBaseLine(mPaint3), mPaint3)
            Log.e("ljwx-3", "draw3")
        }
    }

    private fun countBaseLine(paint: Paint): Float {
        //本身的基线
        var tvLine = height / 2 + Math.abs(paint.ascent() + paint.descent()) / 2
        return if (mLineMode == 0) mBigLine else tvLine
    }

}
