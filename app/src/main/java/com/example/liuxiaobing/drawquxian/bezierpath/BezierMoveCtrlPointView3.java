package com.example.liuxiaobing.drawquxian.bezierpath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxiaobing
 * Date on 2018/11/8
 * Copyright 2013 - 2018 QianTuo Inc. All Rights Reserved
 * Desc:
 */

public class BezierMoveCtrlPointView3 extends View {
    private float startX,startY;
    private float endX,endY ;
    private float contorlX1 = 90,contorlY1 = 60;//默认值
    private float contorlX2 = 400,contorlY2 = 60;//默认值
    private Paint paint;
    private float t;
    private Path path;
    private List<PointF> points;//存储曲线上运动的点
    public BezierMoveCtrlPointView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        startX = 60;
        startY = 350;
        endX = 450;
        endY = 350;
        path = new Path();
        points = new ArrayList<>();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(t >= 1.0f){
            t = 1.0f;
        }
        //绘制线 起点-控制点1  控制点1-控制点2  控制点2-终点
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor("#e5e5e5"));
        canvas.drawLine(startX,startY,contorlX1,contorlY1,paint);
        canvas.drawLine(contorlX1,contorlY1,contorlX2,contorlY2,paint);
        canvas.drawLine(contorlX2,contorlY2,endX,endY,paint);
        //绘制轨迹直线
        float p3x = (1 - t) * startX+ t* contorlX1;
        float p3y = (1 - t) *startY + t * contorlY1;
        float p4x = (1 - t) * contorlX1 + t * contorlX2;
        float p4y = (1 - t) * contorlY1 + t * contorlY2;
        float p5x = (1 - t) * contorlX1+ t* contorlX2;
        float p5y = (1 - t) *contorlY1 + t * contorlY2;
        float p6x = (1 - t) * contorlX2 + t * endX;
        float p6y = (1 - t) * contorlY2 + t * endY;

        float pc7x = (1 - t) * p3x + t * p4x;
        float pc7y = (1 - t) * p3y + t * p4y;

        float pc8x = (1 - t) * p5x + t * p6x;
        float pc8y = (1 - t) * p5y + t * p6y;
        paint.setColor(Color.GREEN);
        //绘制曲线运动轨迹
        canvas.drawLine(p3x, p3y, p4x, p4y, paint);
        canvas.drawLine(p5x, p5y, p6x, p6y, paint);
        paint.setColor(Color.BLUE);
        //绘制辅助线
        canvas.drawLine(pc7x, pc7y, pc8x, pc8y, paint);

        float p9x = (1 - t) * pc7x + t * pc8x;
        float p9y = (1 - t) * pc7y + t * pc8y;
        points.add(new PointF(p9x, p9y));
        paint.setColor(Color.RED);
        PointF ps, pe;
        for (int i = 1; i < points.size(); i++) {//其实这个二阶曲线就是由无数个点构成,然后把前后2个点构成一条直线
            ps = points.get(i - 1);
            pe = points.get(i);
            canvas.drawLine(ps.x, ps.y, pe.x, pe.y, paint);//绘制贝塞尔在时间0到1内曲线运动轨迹
        }
        paint.setColor(Color.parseColor("#e5e5e5"));
        paint.setStrokeWidth(6);
        paint.setColor(Color.BLUE);
        canvas.drawPoint(contorlX1,contorlY1,paint);
        canvas.drawPoint(contorlX2,contorlY2,paint);
        path.reset();
        if(t==1.0f){
            path.moveTo(startX,startY);
            path.cubicTo(contorlX1,contorlY1,contorlX2,contorlY2,endX,endY);
            canvas.drawPath(path,paint);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                t+=0.01;
                invalidate();
            }
        },50);
    }
}