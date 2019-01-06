package com.ljwx.myview

import android.content.Context
import android.graphics.*
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.ljwx.htmltextview.R

class HtmlTextView : AppCompatTextView {

    var defualtColor = Color.parseColor("#ff333333")
    var defualtSize = dp2px(12f)

    var tv1: String? = null
    var tv2: String? = null
    var tv3: String? = null
    val mPaint1 = Paint(Paint.ANTI_ALIAS_FLAG)
    val mPaint2 = Paint(Paint.ANTI_ALIAS_FLAG)
    val mPaint3 = Paint(Paint.ANTI_ALIAS_FLAG)
    var marginL = 0f
    var marginR = 0f

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.HtmlTextView)
        tv1 = attr.getString(R.styleable.HtmlTextView_tv1)
        tv2 = attr.getString(R.styleable.HtmlTextView_tv2)
        tv3 = attr.getString(R.styleable.HtmlTextView_tv3)
        mPaint1.color = attr.getColor(R.styleable.HtmlTextView_tv1Color, defualtColor)
        mPaint2.color = attr.getColor(R.styleable.HtmlTextView_tv2Color, defualtColor)
        mPaint3.color = attr.getColor(R.styleable.HtmlTextView_tv3Color, defualtColor)
        mPaint1.textSize = attr.getDimension(R.styleable.HtmlTextView_tv1Size, defualtSize)
        mPaint2.textSize = attr.getDimension(R.styleable.HtmlTextView_tv2Size, defualtSize)
        mPaint3.textSize = attr.getDimension(R.styleable.HtmlTextView_tv3Size, defualtSize)
        var b1 = attr.getBoolean(R.styleable.HtmlTextView_tv1Bold, false)
        var b2 = attr.getBoolean(R.styleable.HtmlTextView_tv2Bold, false)
        var b3 = attr.getBoolean(R.styleable.HtmlTextView_tv3Bold, false)
        mPaint1.typeface = if (b1) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        mPaint2.typeface = if (b2) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        mPaint3.typeface = if (b3) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
//        mPaint2.isFakeBoldText = true
        marginL = attr.getDimension(R.styleable.HtmlTextView_tv2MarginL,0f)
        marginR = attr.getDimension(R.styleable.HtmlTextView_tv2MarginR,0f)
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
                val s = Math.max(mPaint1.textSize, Math.max(mPaint2.textSize, mPaint3.textSize))
                val p = Paint()
                p.textSize = s
                (p.descent() - p.ascent()).toInt() + paddingTop + paddingBottom
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

        setMeasuredDimension(width, height);
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawTv(canvas)
    }

    fun drawTv(canvas: Canvas) {
        if (text.toString().isEmpty()) {
            if (tv1!!.isNotEmpty()) {
                var y = height / 2 + Math.abs(mPaint1.ascent() + mPaint1.descent()) / 2
                if (tv1 != null) {
                    canvas.drawText(tv1, paddingLeft.toFloat(), y, mPaint1)
                }
                if (tv1 != null && tv2 != null) {
                    canvas.drawText(tv2, paddingLeft + mPaint1.measureText(tv1) + marginL, y, mPaint2)
                }
                if (tv1 != null && tv2 != null && tv3 != null) {
                    canvas.drawText(tv3, paddingLeft + mPaint1.measureText(tv1) + mPaint2.measureText(tv2)
                            + marginL + marginR, y, mPaint3)
                }
            }
        }
    }


    fun dp2px(dp: Float): Float {
        val scale = resources.displayMetrics.density
        return dp * scale + 0.5f
    }
}