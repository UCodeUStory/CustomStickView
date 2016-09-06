package com.example.qiyue.customstickview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by qiyue on 2016/9/4.
 * 当拖动的时候改变原位置圆半径
 *
 * 改变贝塞尔曲线辅助点y
 *
 *
 */
public class StickView extends View {
    private Paint mPaint;
    private int width;
    private int height;
    float centerX = 0;
    float centerY = 0;
    /**
     * 当前圆心
     */
    float r1 = 100;
    /**
     * 圆心坐标
     */
    int circleX = 0;
    int circleY = 0;

    int leftX = -98;
    int leftY = 0;
    int rightX = 98;
    int rightY = 0;
    /**
     * 初始参考点
     */
    int qutoY = 80;

    int circleX2 = 0;
    int circleY2 = 0;

    float r2 = 100;

    float qutoX3 = 100;
    float qutoY3 = 0;
    /**
     * 拖拽速度
     */
    int speed = 8;
    /**
     * 固定圆减少速度
     */
    int decreaseSpeed = 3;
    boolean isMaxRange = false;
    Paint p = new Paint();
    public StickView(Context context) {
        super(context);
        init(context);
    }

    public StickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.RED);
        p.setStrokeWidth(5);

    }

    /**
     * x不让改变，只变y
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width / 2, height / 2);
        if (isMaxRange){
            canvas.drawCircle(circleX2, circleY2, r2, mPaint);
        }else {
            canvas.drawLine(0, -height / 2, 0, height / 2, mPaint);
            canvas.drawLine(-width / 2, 0, width / 2, 0, mPaint);
            Path path = new Path();
            path.moveTo(rightX, rightY);
            canvas.drawCircle(circleX, circleY, r1, mPaint);
            canvas.drawCircle(circleX2, circleY2, r2, mPaint);
            /**
             * 这里注意起笔点和落笔点的顺序，否则链接就会产生不一样效果
             */
            /** 右曲线
             */
            path.quadTo(0, qutoY, qutoX3, qutoY3);
            path.lineTo(-qutoX3, qutoY3);
            path.moveTo(-qutoX3, qutoY3);
            // canvas.drawPath(path, p);

            /** 左曲线
             */

            path.quadTo(0, qutoY, leftX, leftY);
            path.lineTo(rightX, rightY);

            canvas.drawPath(path, p);
        }


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        centerX = w/2;
        centerY = h/2;
    }
    /**
     * 辅助点坐标
     */
    float mSupX = 0;
    float mSupY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                mSupX = event.getX();
                mSupY = event.getY();
                float distance = ((mSupX-centerX)*(mSupX-centerX)+(mSupY-centerY)*(mSupY-centerY));
                Log.i("test",distance+"");
                if (distance<(100*100)){
                    Log.i("test","可以移动");
                    r1 = r1 - decreaseSpeed;
                    leftX = leftX + decreaseSpeed;
                    rightX = rightX - decreaseSpeed;
                    qutoY++;
                    circleY2 = circleY2 + speed;
                    qutoY3 = qutoY3 + speed;
                    if (r1<=0){
                        isMaxRange = true;
                    }
                    centerY = centerY + speed;
                    Log.i("test","centerY"+centerY);
                    invalidate();
                }
                /**
                 * 跟踪想要的点，然后确定要好图形，这也算一个工具类
                 */
                Log.i("test","mSupX="+mSupX+"mSupY="+mSupY);

                invalidate();
        }
        return true;
    }
}
