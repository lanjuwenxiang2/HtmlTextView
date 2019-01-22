package com.ljwx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.ljwx.htmltextview.R;

public class ClickAreaImageView extends AppCompatImageView {

    private float[] mTag1Point = {0, 0, 0, 0};
    private float[] mTag2Point = {0, 0, 0, 0};
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean mDrawArea = true;

    public ClickAreaImageView(Context context) {
        super(context, null);
    }

    public ClickAreaImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClickAreaImageView);
        mTag1Point[0] = array.getDimension(R.styleable.ClickAreaImageView_tag1_left, 0);
        mTag1Point[1] = array.getDimension(R.styleable.ClickAreaImageView_tag1_top, 0);
        mTag1Point[2] = array.getDimension(R.styleable.ClickAreaImageView_tag1_right, 0);
        mTag1Point[3] = array.getDimension(R.styleable.ClickAreaImageView_tag1_bottom, 0);

        mTag2Point[0] = array.getDimension(R.styleable.ClickAreaImageView_tag2_left, 0);
        mTag2Point[1] = array.getDimension(R.styleable.ClickAreaImageView_tag2_top, 0);
        mTag2Point[2] = array.getDimension(R.styleable.ClickAreaImageView_tag2_right, 0);
        mTag2Point[3] = array.getDimension(R.styleable.ClickAreaImageView_tag2_bottom, 0);
        mDrawArea = array.getBoolean(R.styleable.ClickAreaImageView_drawArea, true);
        array.recycle();
        init();
    }

    public ClickAreaImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawArea) {
            canvas.drawRect(mTag1Point[0], mTag1Point[1], mTag1Point[2], mTag1Point[3], mPaint);
            canvas.drawRect(mTag2Point[0], mTag2Point[1], mTag2Point[2], mTag2Point[3], mPaint);
        }
    }

    boolean isTag1 = false;
    boolean isTag2 = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        canCall(event);
        return true;
    }

    float xD1 = 0, yD1 = 0, xU1 = 0, yU1 = 0;
    float xD2 = 0, yD2 = 0, xU2 = 0, yU2 = 0;

    private void canCall(MotionEvent event) {
        isTag1 = false;
        isTag2 = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xD1 = event.getX();
                xD2 = event.getX();
                yD1 = event.getY();
                yD2 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xU1 = event.getX();
                xU2 = event.getX();
                yU1 = event.getY();
                yU2 = event.getY();

                boolean xdb1 = xD1 >= mTag1Point[0] && xD1 <= mTag1Point[2];
                boolean xub1 = xU1 >= mTag1Point[0] && xU1 <= mTag1Point[2];
                boolean ydb1 = yD1 >= mTag1Point[1] && yD1 <= mTag1Point[3];
                boolean yub1 = yU1 >= mTag1Point[1] && yU1 <= mTag1Point[3];
                if (xdb1 && xub1 && ydb1 && yub1) {
                    isTag1 = true;
                    if (mTag1Listener != null) {
                        mTag1Listener.call();
                    }
                }

                boolean xdb2 = xD2 >= mTag2Point[0] && xD2 <= mTag2Point[2];
                boolean xub2 = xU2 >= mTag2Point[0] && xU2 <= mTag2Point[2];
                boolean ydb2 = yD2 >= mTag2Point[1] && yD2 <= mTag2Point[3];
                boolean yub2 = yU2 >= mTag2Point[1] && yU2 <= mTag2Point[3];
                if (xdb2 && xub2 && ydb2 && yub2) {
                    isTag2 = true;
                    if (mTag2Listener != null) {
                        mTag2Listener.call();
                    }
                }
                break;
        }
    }

    public interface TouchAreaListener {
        void call();
    }

    TouchAreaListener mTag1Listener;
    TouchAreaListener mTag2Listener;

    public void setOnClickListenerTag1(TouchAreaListener listener) {
        mTag1Listener = listener;
    }

    public void setOnClickListenerTag2(TouchAreaListener listener) {
        mTag2Listener = listener;
    }
}
