package com.example.liuxiaobing.drawquxian.bezierpath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxiaobing
 * Date on 2018/11/8
 * Copyright 2013 - 2018 QianTuo Inc. All Rights Reserved
 * Desc: 本例实现 二阶贝赛尔曲线轨迹
 *
 *  二阶曲线轨迹公式：
 *  f(t) = (1-t) * (1-t) P0 + 2t*(1-t)P1 + t * t P2；
 *
 *  其中 t 取值范围 [0,1]
 *
 *  p0 起点，p1 是控制点，p2终点
 *
 */

public class BezierMoveCtrlPointView2 extends View {
    private float startX,startY;
    private float endX,endY ;
    private float contorlX = 200,contorlY = 60;//默认值
    private Paint paint;
    private float t;
    private Path path;
    private List<PointF> points;//存储曲线上运动的点
    public BezierMoveCtrlPointView2(Context context, AttributeSet attrs) {
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
        if(t>=1.0f){
            t=1.0f;
        }
        //绘制2根线  起点-控制点  控制点-终点
        paint.setColor(Color.parseColor("#e5e5e5"));
        canvas.drawLine(startX,startY,contorlX,contorlY,paint);
        canvas.drawLine(contorlX,contorlY,endX,endY,paint);
        //
        float p3x = (1 - t) * startX+ t* contorlX;  //将 start  与control看作是 一阶贝赛尔曲线 l1
        float p3y = (1 - t) *startY + t * contorlY;

        float p4x = (1 - t) * contorlX + t * endX;  //将 end  与control看作是 一阶贝赛尔曲线  l2
        float p4y = (1 - t) * contorlY + t * endY;

        paint.setColor(Color.GREEN);
        //这是绘制辅助线说明轨迹的运动情况 轨迹的运动情况
        canvas.drawLine(p3x, p3y, p4x, p4y, paint); // 辅助线
        //绘制曲线轨迹
        float p5x = (1 - t) * p3x + t * p4x;    //再将一个点在 辅助线上的运动轨迹 画出来，就是一个二阶贝赛尔曲线了
        float p5y = (1 - t) * p3y + t * p4y;

        points.add(new PointF(p5x, p5y));
        paint.setColor(Color.RED);
        PointF ps, pe;
        for (int i = 1; i < points.size(); i++) {//其实这个二阶曲线就是由无数个点构成,然后把前后2个点构成一条直线
            ps = points.get(i - 1);
            pe = points.get(i);
            canvas.drawLine(ps.x, ps.y, pe.x, pe.y, paint);//绘制贝塞尔在时间0到1内运动轨迹
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                t+=0.01;
                invalidate();
            }
        },50);
        if(t==1.0f){//绘制曲线轨迹曲线
            path.reset();//记得一定要重置path
            path.moveTo(startX,startY);
            path.quadTo(contorlX,contorlY,endX,endY);
            canvas.drawPath(path,paint);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                contorlX = event.getX();
                contorlY = event.getY();
                t = 0;
                points.clear();
                invalidate();
                break;
        }
        return true;
    }
}
