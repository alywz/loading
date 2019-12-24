package com.demo.alyloading.components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

/**
 * Created by WZ on 2019/12/23.
 * description:自定义loading
 */
public class LoadingView extends View {
    private String TAG=getClass().getSimpleName();
    private Paint mPaint;
    private Path mPath;
    private Path circlePath;
    private Path dst;
    private PathMeasure pathMeasure;
    private float animateValue;
    private float startD,stopD;
    private float radius=80;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this.setLayerType(LAYER_TYPE_SOFTWARE,null);
        mPath = new Path();
        circlePath = new Path();
        dst = new Path();
        pathMeasure = new PathMeasure();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xFF3A85DB);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        circlePath.addCircle(0, 0, radius, Path.Direction.CW);
        mPath.addCircle(0, 0, radius, Path.Direction.CW);

//        //////对号start///////////////////////
//        mPath.moveTo(-radius*3/5,0);
//        mPath.lineTo(-radius/5,radius/2);
//        mPath.lineTo(radius*1/2,-radius/4);
//        //////对号end///////////////////////

        //////画W start///////////////////////
        mPath.moveTo(-radius*2/3,-radius/3);
        mPath.lineTo(-radius/3,radius/3);
        mPath.lineTo(0,-radius/3);
        mPath.lineTo(radius/3,radius/3);
        mPath.lineTo(radius*2/3,-radius/3);
        //////画W end///////////////////////

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 4);
        valueAnimator.setDuration(4000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animateValue = (float) animation.getAnimatedValue();
//                Log.d(TAG,animateValue+"===");
                invalidate();
            }
        });
        valueAnimator.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dst.reset();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        pathMeasure.setPath(mPath,false);
/////////////////绘制进度圈/////////////////////////////////////
//        float length = pathMeasure.getLength();
//        float stop = animateValue * length;
//        Log.d(TAG,Math.abs(animateValue - 0.5)+"");
//        float start = (float) (stop - (0.5 - Math.abs(animateValue - 0.5)) * length);
//        pathMeasure.getSegment(start, stop, dst, true);
////        canvas.rotate(360*animateValue);
//        canvas.drawPath(dst,mPaint);
////////////////绘制进度圈///////////////////////////////////////////////

        //////////绘制内容/////////////////////////
        if(animateValue<1){
            startD=0;
            stopD=pathMeasure.getLength()*animateValue;
            pathMeasure.getSegment(startD,stopD,dst,true);
            canvas.drawPath(dst,mPaint);
        }else {
            pathMeasure.nextContour();
            Log.d(TAG,animateValue+"===============");
            pathMeasure.nextContour();
            canvas.drawPath(circlePath,mPaint);
            startD=0;
            stopD=pathMeasure.getLength()*(animateValue-1);
            pathMeasure.getSegment(startD,stopD,dst,true);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            canvas.drawPath(dst,mPaint);
        }
        /////////绘制内容//////////////////////////
    }
}
