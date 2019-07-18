package com.hbandroid.viewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.hbandroid.viewdemo.R;


public class DefineProgressView extends View {

    //当进度条移动至最右端的时候给与一定的居左边距，因为API所计算出来的文字宽度有一定误差
    private static final int TEXT_LEFT_RIGHT_PADDING = 5;

    //绘制进度条和进度文字的画笔
    private Paint mProgressPaint;
    private Paint mTextPaint;


    //进度条的底色和完成进度的颜色
    private int mProgressBackColor;
    private int mProgressForeColor;

    //进度条上方现实的文字
    private String mProgressText;
    //进度文字的颜色
    private int mTextColor;
    //进度文字的字体大小
    private int mTextSize;

    //进度条的起始值，当前值和结束值
    private int currentProgress;


    //进度条的高度
    private int mProgressBarHeight;

    //view的上下内边距
    private int mPaddingTop;
    private int mPaddingBottom;

    //用于测量文字显示区域的宽度和高度
    private Paint.FontMetricsInt mTextFontMetrics;
    private Rect mTextBound;

    //进度条和进度文字显示框的间距
    private int mLine2TextDividerHeight;

    //绘制进度条圆角矩形的圆角
    private int mRectCorn;
    //计算文字显示区域的宽度
    private int textWidth;

    public DefineProgressView(Context context) {
        this(context, null);
    }

    public DefineProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefineProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DefineProgressView);
            mProgressBackColor = attributes.getColor(R.styleable.DefineProgressView_firstColor, Color.RED);
            mProgressForeColor = attributes.getColor(R.styleable.DefineProgressView_secondColor, Color.BLUE);
            mTextColor = attributes.getColor(R.styleable.DefineProgressView_progressTextColor, Color.BLACK);
            currentProgress = attributes.getInt(R.styleable.DefineProgressView_currProgress, 0);
            mProgressText = currentProgress + "%";
            mTextSize = attributes.getDimensionPixelSize(R.styleable.DefineProgressView_progressTextSize, (int) dp(8));
            mProgressBarHeight = attributes.getDimensionPixelSize(R.styleable.DefineProgressView_progressHeight, (int) dp(3));
            mRectCorn = (int) (mProgressBarHeight * 1.0f / 2 + 0.5f);
            mLine2TextDividerHeight = attributes.getDimensionPixelSize(R.styleable.DefineProgressView_progressMarginText, (int) dp(2));
            attributes.recycle();
        }
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = mPaddingTop + mTextFontMetrics.bottom - mTextFontMetrics.top + mLine2TextDividerHeight + mProgressBarHeight + mPaddingBottom;
        setMeasuredDimension(getMeasuredWidth(), measuredHeight);
    }

    private void init() {

        mTextBound = new Rect();

        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(mProgressBarHeight);

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        reCalculationTextSize();

        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();

    }

    private void reCalculationTextSize() {
        mTextPaint.setTextSize(mTextSize);
        mTextFontMetrics = mTextPaint.getFontMetricsInt();
        mTextPaint.getTextBounds(mProgressText, 0, mProgressText.length(), mTextBound);

        textWidth = mTextBound.right - mTextBound.left + TEXT_LEFT_RIGHT_PADDING;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mProgressPaint.setColor(mProgressBackColor);

        //计算开始绘制进度条的距离顶部的间距
        int progressBarMarginTop = mPaddingTop - mTextFontMetrics.top + mTextFontMetrics.bottom + mLine2TextDividerHeight;

        //绘制进度条底部背景
        RectF rectF = new RectF(0, progressBarMarginTop, getMeasuredWidth(), progressBarMarginTop + mProgressBarHeight);
        canvas.drawRoundRect(rectF, mRectCorn, mRectCorn, mProgressPaint);

        mProgressPaint.setColor(mProgressForeColor);
        //绘制已经完成了的进度条
        int currProgress = (int) (getMeasuredWidth() * currentProgress * 1.0f / 100);
        RectF rectProgress = new RectF(0, progressBarMarginTop, currProgress, progressBarMarginTop + mProgressBarHeight);
        canvas.drawRoundRect(rectProgress, mRectCorn, mRectCorn, mProgressPaint);

        //绘制文字，存在textX小0的时候，绘制的文字将会看不见
        int textX = currProgress - textWidth;
        canvas.drawText(mProgressText, textX > 0 ? textX : 0, mPaddingTop - mTextFontMetrics.top, mTextPaint);

    }


    public void resetLevelProgress(int currentProgress) {
        if (currentProgress > 100) {
            currentProgress = currentProgress % 100;
        }
        this.currentProgress = currentProgress;
        mProgressText = currentProgress + "%";
        reCalculationTextSize();
        invalidate();
    }

    public float dp(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
