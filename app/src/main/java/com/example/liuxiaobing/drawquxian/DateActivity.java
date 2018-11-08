package com.example.liuxiaobing.drawquxian;

import android.app.Activity;
import android.drm.DrmStore;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.NumberPicker;

/**
 * Created by liuxiaobing
 * Date on 2018/11/7
 * Copyright 2013 - 2018 QianTuo Inc. All Rights Reserved
 * Desc:
 */

public class DateActivity extends Activity {

    private NumberPicker yearPicker;
    private NumberPicker monthPicker;
    private final String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11","12"};
    private final String[] years = {"2013", "2014", "2015", "2016", "2017", "2018"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        yearPicker = findViewById(R.id.year);
        monthPicker = findViewById(R.id.month);

        //设置需要显示的内容数组
        yearPicker.setDisplayedValues(years);
        //设置最大最小值
        yearPicker.setMinValue(2013);
        yearPicker.setMaxValue(Integer.valueOf((years[years.length -1])));
        //设置默认的位置
        yearPicker.setValue(1);
        yearPicker.setWrapSelectorWheel(false);

        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //得到选择结果
                System.out.println("43-------------:"+newVal );
            }
        });

        monthPicker.setDisplayedValues(months);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(1);
        monthPicker.setWrapSelectorWheel(false);

        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //得到选择结果
                System.out.println("59-------------:"+newVal );
            }
        });

        monthPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE){
                    System.out.println("65------stop:"+view.getValue());
                }
            }
        });
    }
}
