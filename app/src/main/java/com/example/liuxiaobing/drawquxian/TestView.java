package com.example.liuxiaobing.drawquxian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liuxiaobing
 * Date on 2018/11/7
 * Copyright 2013 - 2018 QianTuo Inc. All Rights Reserved
 * Desc:
 */

public class TestView extends View {

    private Paint mPaint;

    public TestView(Context context) {
        super(context);
        initView();
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){

        mPaint = new Paint();
        mPaint.setTextSize(20);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.CYAN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.RED);
        mPaint.setTextSize(30);
        String string="HeDan";
        mPaint.setStrokeWidth(30);
        canvas.drawText(string,100,100,mPaint);

    }
}
