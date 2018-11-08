package com.example.liuxiaobing.drawquxian;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    LineGraphicView tu;
    ArrayList<Double> yList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tu = (LineGraphicView) findViewById(R.id.line_graphic);

        yList = new ArrayList<>();
        yList.add(100.00);
        yList.add(250.0);
        yList.add(300.0);
        yList.add(540.0);
        yList.add(1000.0);

        ArrayList<String> xRawDatas = new ArrayList<String>();
       for(int i = 1; i <= 31; i++){
           xRawDatas.add(i+"");
       }
        tu.setData(yList, xRawDatas, 1000, 1000 / yList.size());

        //testShowOnlyYearMonth();
    }


    private void testShowOnlyYearMonth(){

        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(new ContextThemeWrapper(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), null, yy, mm, dd) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                LinearLayout mSpinners = (LinearLayout) findViewById(getContext().getResources().getIdentifier("android:id/pickers", null, null));
                if (mSpinners != null) {
                    NumberPicker mMonthSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/month", null, null));
                    NumberPicker mYearSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/year", null, null));
                    mSpinners.removeAllViews();
                    if (mMonthSpinner != null) {
                        mSpinners.addView(mMonthSpinner);
                    }
                    if (mYearSpinner != null) {
                        mSpinners.addView(mYearSpinner);
                    }
                }
                View dayPickerView = findViewById(getContext().getResources().getIdentifier("android:id/day", null, null));
                if(dayPickerView != null){
                    dayPickerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                super.onDateChanged(view, year, month, day);
                setTitle("请选择信用卡有效期");
            }
        };

        dlg.show();





    }
}
