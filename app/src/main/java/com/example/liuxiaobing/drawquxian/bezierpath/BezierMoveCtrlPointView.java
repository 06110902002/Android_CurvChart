package com.example.liuxiaobing.drawquxian.bezierpath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liuxiaobing
 * Date on 2018/11/8
 * Copyright 2013 - 2018 QianTuo Inc. All Rights Reserved
 * Desc: 滑动Bezier的控制点，二阶的
 */

public class BezierMoveCtrlPointView extends View{

    private float startX,startY;
    private float endX,endY ;
    private float contorlX = 200,contorlY = 60;//默认值
    private Paint paint;
    private float t;
    private Path path;


    public BezierMoveCtrlPointView(Context context) {
        super(context);
    }

    public BezierMoveCtrlPointView(Context context, @Nullable AttributeSet attrs) {
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
    }

    public BezierMoveCtrlPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        path.moveTo(startX,startY);
        path.quadTo(contorlX,contorlY,endX,endY);
        canvas.drawPath(path,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            contorlX = event.getX();
            contorlY = event.getY();
            invalidate();
        }
        return true;
    }
}
