package jyq.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

import jyq.customview.R;
import jyq.customview.util.ScreenUtils;


/**
 * 自定义表盘
 */
public class MyWatchView extends View {
    private Drawable mExampleDrawable;

    private float mRadius; // 表盘半径
    private float mPadding; // 表盘边距
    private int mBackgroundColor; // 表盘底色
    private int mDialColor; // 刻度颜色
    private int mDialBoldColor; // 深色刻度颜色
    private int mDialTextColor; // 刻度字体颜色
    private float mDialTextSize; // 刻度字体大小
    private float mPointerRadius; // 指针圆角
    private float mPointerTailLength; // 指针尾长

    private float mHourPointerLength; // 时针长度
    private float mMinPointerLength; // 分针长度
    private float mSecPointerLength; // 秒针长度

    private float mHourPointerWidth; // 时针宽度
    private float mMinPointerWidth; // 分针宽度
    private float mSecPointerWidth; // 秒针宽度

    private int mHourPointerColor; // 时针颜色
    private int mMinPointerColor; // 分针颜色
    private int mSecPointerColor; // 秒针颜色


    private TextPaint mTextPaint; // 刻度数字
    private TextPaint mDialPaint; //刻度
    private TextPaint mDialBoldPaint; //加粗刻度
    private Paint mPanelPaint; //表盘背景
    private Paint mPanelCenterPaint; //表盘中心点
    private Paint mPointerPaint; //表盘指针
    
    private float mDialHeight;
    private float mDialBoldHeight;

    public MyWatchView(Context context) {
        super(context);
        init(null, 0);
    }

    public MyWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyWatchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        
        //paint panel
        canvas.drawCircle(0, 0, mRadius, mPanelPaint);

        //paint Dail
        for (int i=0; i<60; i++){
            if(i%5==0){ // 整点
                canvas.drawLine(0, -mRadius + mPadding, 0, -mRadius + mPadding + mDialBoldHeight, mDialBoldPaint);
                
                // 绘制文字
                String text = ((i / 5) == 0 ? 12 : (i / 5)) + "";
                Rect textBound = new Rect();
                mTextPaint.getTextBounds(text, 0, text.length(), textBound);
                
                canvas.save();
                final int textHeight = textBound.bottom - textBound.top;
                canvas.translate(0, -mRadius + ScreenUtils.dpToPx(getContext(), 5f) + mDialBoldHeight + textHeight);
                
                canvas.rotate(-6 * i);
                canvas.drawText(text, -(textBound.right - textBound.left) / 2, textBound.bottom + textHeight/2, mTextPaint);
                canvas.restore();
            }else{
                canvas.drawLine(0, -mRadius + mPadding, 0, -mRadius + mPadding + mDialHeight, mDialPaint);
            }
            canvas.rotate(6);
        }
        
        
        
        //paint pointers
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); //时
        int minute = calendar.get(Calendar.MINUTE); //分
        int second = calendar.get(Calendar.SECOND); //秒
        int angleHour = (hour % 12) * 360 / 12; //时针转过的角度
        int angleMinute = minute * 360 / 60; //分针转过的角度
        int angleSecond = second * 360 / 60; //秒针转过的角度
        
        // hour
        canvas.save();
        canvas.rotate(angleHour); //旋转到时针的角度
        RectF rectFHour = new RectF(-mHourPointerWidth / 2, -mHourPointerLength, mHourPointerWidth / 2, mPointerTailLength);
        mPointerPaint.setColor(mHourPointerColor);
        mPointerPaint.setStrokeWidth(mHourPointerWidth);
        canvas.drawRoundRect(rectFHour, mPointerRadius, mPointerRadius, mPointerPaint); //绘制时针
        canvas.restore();
        
        //min
        canvas.save();
        canvas.rotate(angleMinute);
        final RectF rectFMin = new RectF(-mMinPointerWidth / 2, -mMinPointerLength, mMinPointerWidth / 2, mPointerTailLength);
        mPointerPaint.setColor(mMinPointerColor);
        mPointerPaint.setStrokeWidth(mMinPointerWidth);
        canvas.drawRoundRect(rectFMin, mPointerRadius, mPointerRadius, mPointerPaint);
        canvas.restore();
        
        //sec
        canvas.save();
        canvas.rotate(angleSecond);
        final RectF rectFSec = new RectF(-mSecPointerWidth / 2, -mSecPointerLength, mSecPointerWidth / 2, mPointerTailLength);
        mPointerPaint.setColor(mSecPointerColor);
        mPointerPaint.setStrokeWidth(mSecPointerWidth);
        canvas.drawRoundRect(rectFSec, mPointerRadius, mPointerRadius, mPointerPaint);
        canvas.restore();
        
        //paint center point
        canvas.drawCircle(0,0,mHourPointerWidth,mPanelCenterPaint);
        
        
        //refresh
        postInvalidateDelayed(500);
    }




    private void init(AttributeSet attrs, int defStyle) {
        obtainAttrs(attrs, defStyle);
        
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(mDialTextSize);
        mTextPaint.setColor(mDialTextColor);
        mTextPaint.setStyle(Paint.Style.FILL);

        mDialPaint = new TextPaint();
        mDialPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mDialPaint.setStrokeWidth(ScreenUtils.dpToPx(getContext(), 1f));
        mDialPaint.setColor(mDialColor);

        mDialBoldPaint = new TextPaint();
        mDialBoldPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mDialBoldPaint.setStrokeWidth(ScreenUtils.dpToPx(getContext(), 1.5f));
        mDialBoldPaint.setColor(mDialBoldColor);

        mPanelPaint = new Paint();
        mPanelPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPanelPaint.setStyle(Paint.Style.FILL);
        mPanelPaint.setColor(mBackgroundColor);

        mPanelCenterPaint = new Paint();
        mPanelCenterPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPanelCenterPaint.setStyle(Paint.Style.FILL);
        mPanelCenterPaint.setColor(mBackgroundColor);

        mPointerPaint = new Paint();
        mPointerPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPointerPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    private void obtainAttrs(AttributeSet attrs, int defStyle) {
        TypedArray a = null;
        try {
            a = getContext().obtainStyledAttributes(attrs, R.styleable.MyWatchView, defStyle, 0);

            /** example code **/
            if (a.hasValue(R.styleable.MyWatchView_exampleDrawable)) {
                mExampleDrawable = a.getDrawable(R.styleable.MyWatchView_exampleDrawable);
                mExampleDrawable.setCallback(this);
            }
            /** end **/

            mBackgroundColor = a.getColor(R.styleable.MyWatchView_wv_backgroudColor, Color.WHITE);
            mDialColor = a.getColor(R.styleable.MyWatchView_wv_dialColor, Color.argb(125, 0, 0, 0));
            mDialBoldColor = a.getColor(R.styleable.MyWatchView_wv_dialBoldColor, Color.argb(225, 0, 0, 0));
            mDialTextColor = a.getColor(R.styleable.MyWatchView_wv_dialTextColor, Color.BLACK);
            mHourPointerColor = a.getColor(R.styleable.MyWatchView_wv_hourPointColor, Color.BLACK);
            mMinPointerColor = a.getColor(R.styleable.MyWatchView_wv_minPointColor, Color.BLACK);
            mSecPointerColor = a.getColor(R.styleable.MyWatchView_wv_secPointColor, Color.RED);
            mDialTextSize = a.getDimension(R.styleable.MyWatchView_wv_dialTextSize, ScreenUtils.dpToPx(getContext(), 16f));
        } catch (Exception e) {
            
            e.printStackTrace();
            mBackgroundColor = Color.WHITE;
            mDialColor = Color.argb(125, 0, 0, 0);
            mDialBoldColor = Color.argb(225, 0, 0, 0);
            mDialTextColor = Color.BLACK;
            mHourPointerColor = Color.BLACK;
            mMinPointerColor = Color.BLACK;
            mSecPointerColor = Color.RED;
            mDialTextSize = ScreenUtils.dpToPx(getContext(), 16f);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }

        mPointerRadius = ScreenUtils.dpToPx(getContext(), 1f);
        mHourPointerWidth = ScreenUtils.dpToPx(getContext(), 5f);
        mMinPointerWidth = ScreenUtils.dpToPx(getContext(), 3f);
        mSecPointerWidth = ScreenUtils.dpToPx(getContext(), 2f);
        mPadding =  ScreenUtils.dpToPx(getContext(), 5f);
        mDialHeight =  ScreenUtils.dpToPx(getContext(), 5f);
        mDialBoldHeight =  ScreenUtils.dpToPx(getContext(), 8f);
    }



    /**
     * 处理Wrapcontent
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = ScreenUtils.dpToPxInt(this.getContext(), 300); //设定一个最小值dp
        int width =  ScreenUtils.dpToPxInt(this.getContext(), 300); //设定一个最小值dp

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        if (widthMode == MeasureSpec.EXACTLY  && heightMode == MeasureSpec.EXACTLY ) {
            height = heightSize;
            width = widthSize;
        }else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(heightSize, height);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(widthSize, width);
        }

        final int radius = Math.min(height, width);
        setMeasuredDimension(radius, radius);

    }

    /**
     * 在这里获得尺寸信息，
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = w - paddingLeft - paddingRight;
        int contentHeight = h - paddingTop - paddingBottom;

        mRadius = Math.min(contentHeight,contentWidth)/2;
        mSecPointerLength = mRadius - mPadding;
        mMinPointerLength = mRadius * 7/10;
        mHourPointerLength = mRadius * 6/10;
        mPointerTailLength = mRadius/6;
    }

    
    
}
