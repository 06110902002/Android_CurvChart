package com.example.liuxiaobing.drawquxian.bezierpath;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.liuxiaobing.drawquxian.R;

/**
 * Created by liuxiaobing
 * Date on 2018/11/8
 * Copyright 2013 - 2018 QianTuo Inc. All Rights Reserved
 * Desc: 本类实现贝赛尔曲线 的DEMO
 */

public class BezierPathActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bezier);
    }
}
