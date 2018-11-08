package com.example.liuxiaobing.drawquxian;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


import java.util.ArrayList;

/**********************************************************
 * @文件名称：LineGraphicView.java
 * @文件作者：rzq
 * @创建时间：2015年5月27日 下午3:05:19
 * @文件描述：自定义简单曲线图
 * @修改历史：2015年5月27日创建初始版本
 **********************************************************/
public class LineGraphicView extends View {
    /**
     * 公共部分
     */
    private  int CIRCLE_SIZE = 10;

    private static enum Linestyle {
        Line, Curve
    }

    private Context mContext;
    private Paint mPaint;
    private Paint indexLinePaint;
    private Paint midCirclePaint;
    private Resources res;
    //private DisplayMetrics dm;

    /**
     * data
     */
    private Linestyle mStyle = Linestyle.Curve;

    private int canvasHeight;
    private int canvasWidth;
    private int bheight = 0;
    private int blwidh;
    private boolean isMeasure = true;
    /**
     * Y轴最大值
     */
    private int maxValue;
    /**
     * Y轴间距值
     */
    private int averageValue;
    private int marginTop = 20;
    private int marginBottom = 40;

    /**
     * 曲线上总点数
     */
    private Point[] mPoints;
    /**
     * 纵坐标值
     */
    private ArrayList<Double> yRawData;
    /**
     * 横坐标值
     */
    private ArrayList<String> xRawDatas;
    private ArrayList<Integer> xList = new ArrayList<Integer>();// 记录每个x的值
    private int spacingHeight;
    private boolean startDrawIndexLine = false;
    private float curTouchPosX = 50.0f;
    private float offsetY = 0.0f;
    private float xyAxisFontSize = 0.0f;
    private float indexTrigleEdgeLenght = 0.0f;
    private Path indexRectPath = null;
    private String daySumTips = null;
    private int curValueOfXData = -1;
    private float mAnimProgress;

    public LineGraphicView(Context context) {
        this(context, null);
    }

    public LineGraphicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView() {
        this.res = mContext.getResources();
        this.mPaint = new Paint();
        mPaint.setStyle(Style.FILL);
        mPaint.setStrokeWidth(1);
        marginBottom = dip2px(20);
        marginTop = dip2px(140);
        offsetY = dip2px(140);
        xyAxisFontSize = sp2px(10);
        CIRCLE_SIZE = dip2px(5);
        indexTrigleEdgeLenght = dip2px(8);
        indexRectPath = new Path();
        daySumTips = res.getString(R.string.daySumTips);

        mPaint.setTextSize(xyAxisFontSize);

        indexLinePaint = new Paint();
        indexLinePaint.setStyle(Style.FILL);
        indexLinePaint.setStrokeWidth(dip2px(1));
        indexLinePaint.setColor(res.getColor(R.color.color_E9A79B));

        midCirclePaint = new Paint();
        midCirclePaint.setStyle(Style.FILL);
        midCirclePaint.setStrokeWidth(dip2px(2));
        midCirclePaint.setColor(res.getColor(R.color.color_E9A79B));    //初始为大圆圈的颜色
        post(new Runnable() {
            @Override
            public void run() {
                showAnimator();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (isMeasure) {
            this.canvasHeight = getHeight();
            this.canvasWidth = getWidth();
            if (bheight == 0)
                bheight = (int) (canvasHeight - marginBottom - dip2px(140));
            blwidh = dip2px(30);
            isMeasure = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        mPaint.setColor(res.getColor(R.color.color_9F9F9F));
        mPaint.setStrokeWidth(1);
        drawAllXLine(canvas);
        drawAllYLine(canvas);
        mPoints = getPoints();

        mPaint.setColor(res.getColor(R.color.color_1f96f2));
        mPaint.setStrokeWidth(dip2px(1.5f));
        mPaint.setStyle(Style.STROKE);
        if (mStyle == Linestyle.Curve) {
            drawScrollLine(canvas);
        } else {
            drawLine(canvas);
        }

        //画点
        mPaint.setStyle(Style.FILL);
        for (int i = 0; i < mPoints.length; i++) {
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, CIRCLE_SIZE / 4 , mPaint);
        }
        drawTopLine(canvas);
        drawIndexLine(canvas,curTouchPosX);
        drawMidCircle(canvas,curTouchPosX);

    }

    /**
     * 画所有横向表格，包括X轴
     */
    private void drawAllXLine(Canvas canvas) {
        mPaint.setColor(res.getColor(R.color.color_9F9F9F));
        for (int i = 0; i < spacingHeight + 1; i++) {
            canvas.drawLine(blwidh,
                    bheight - (bheight / spacingHeight) * i + marginTop,
                    (canvasWidth - blwidh) + (canvasWidth - blwidh) / xRawDatas.size(),
                    bheight - (bheight / spacingHeight) * i + marginTop, mPaint);// Y坐标
            canvas.drawText(String.valueOf(averageValue * i),
                    blwidh / 2,
                    bheight - (bheight / spacingHeight) * i + marginTop,mPaint);
        }
    }

    /**
     * 画所有纵向表格，包括Y轴
     */
    private void drawAllYLine(Canvas canvas) {
        for (int i = 0; i < xRawDatas.size(); i++) {
            xList.add(blwidh + (canvasWidth - blwidh) / xRawDatas.size() * i);  //曲线数据对应的x 轴刻度

            canvas.drawLine(blwidh + (canvasWidth - blwidh) / xRawDatas.size() * i,
                    marginTop,
                    blwidh + (canvasWidth - blwidh) / xRawDatas.size() * i,
                    bheight + marginTop, mPaint);
            if (i % 5 == 0) {

               mPaint.setColor(res.getColor(R.color.color_949494));
                canvas.drawText(xRawDatas.get(i),
                        blwidh + (canvasWidth - blwidh) / xRawDatas.size() * i,
                        bheight + dip2px(15) + (int)offsetY,mPaint);
            }


        }
    }

    private void drawScrollLine(Canvas canvas) {
        drawAima(canvas);
        Point startp = new Point();
        Point endp = new Point();
        Paint linePaint = new Paint();
        linePaint.setStyle(Style.FILL);
        linePaint.setStrokeWidth((float) 2.3);
        linePaint.setColor(Color.parseColor("#def0fe"));//设置曲线的颜色的
        linePaint.setAlpha(125);
        linePaint.setPathEffect(null);

        Path path2 = new Path();
        path2.moveTo(mPoints[0].x, mPoints[0].y);
        Point lastPoint = new Point();
        lastPoint.x = mPoints[mPoints.length - 1].x;
        lastPoint.y = mPoints[mPoints.length - 1].y;

        for (int i = 0; i < mPoints.length - 1; i++) {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaint);
        }

        for (int i = 0; i < mPoints.length; i++) {
            if (i != 0) {

                Point prePoint = new Point();
                Point nowPoint = new Point();

                prePoint.x = mPoints[i - 1].x;
                prePoint.y = mPoints[i - 1].y;

                nowPoint.x = mPoints[i].x;
                nowPoint.y = mPoints[i].y;

                path2.cubicTo((nowPoint.x + prePoint.x) / 2, prePoint.y,
                        (nowPoint.x + prePoint.x) / 2, nowPoint.y,
                        nowPoint.x, nowPoint.y);

            }

        }

        //下面3个稳点非常重要，目的是为了形成一个封闭的路径
        path2.lineTo(lastPoint.x, bheight + marginTop);          //移到最后一个点
        path2.lineTo(mPoints[0].x, bheight + marginTop);        //再移到与最后一个点同x座标的 但y座标为0处
        path2.lineTo(mPoints[0].x, mPoints[0].y);                  //再回到原点
        canvas.drawPath(path2, linePaint);
    }

    private void drawLine(Canvas canvas) {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++) {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
        }
    }



    private void drawText(String text, int x, int y, Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(dip2px(12));
        p.setColor(Color.BLUE);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x, y, p);
    }

    private Point[] getPoints() {

        Point[] points = new Point[yRawData.size()];
        for (int i = 0; i < yRawData.size(); i++) {

            int ph = bheight - (int) (bheight * (yRawData.get(i) / maxValue));

            points[i] = new Point(xList.get(i), ph + marginTop);
        }
        return points;
    }

    public void setData(ArrayList<Double> yRawData, ArrayList<String> xRawData, int maxValue, int averageValue) {
        this.maxValue = maxValue;
        this.averageValue = averageValue;
        this.mPoints = new Point[yRawData.size()];
        this.xRawDatas = xRawData;
        this.yRawData = yRawData;
        this.spacingHeight = maxValue / averageValue;
    }

    public void setTotalvalue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setPjvalue(int averageValue) {
        this.averageValue = averageValue;
    }

    public void setMargint(int marginTop) {
        this.marginTop = marginTop;
    }

    public void setMarginb(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public void setMstyle(Linestyle mStyle) {
        this.mStyle = mStyle;
    }

    public void setBheight(int bheight) {
        this.bheight = bheight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        float scale = this.mContext.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
    /** sp转换px */
    public int sp2px(int spValue) {
        float fontScale = this.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        curTouchPosX = event.getX();
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                startDrawIndexLine = true;
                break;

            case MotionEvent.ACTION_MOVE:
                startDrawIndexLine = true;
                break;

            case MotionEvent.ACTION_UP:
                startDrawIndexLine = false;
                break;
        }
        postInvalidate();
        return true;
    }

    private void drawIndexLine(Canvas canvas,float x){
        if(x < (blwidh - 5) || x > (canvasWidth - blwidh + (canvasWidth - blwidh) / xRawDatas.size() + 2)) return;
        if(startDrawIndexLine){

            canvas.drawLine(x, 0 + indexTrigleEdgeLenght - indexLinePaint.getStrokeWidth(), x, bheight + marginTop , indexLinePaint);
            float tt = getValueByPosX(x);
            //System.out.println("330-----------:"+tt);

            //画指示3角形
            indexLinePaint.setColor(Color.WHITE);
            indexLinePaint.setStrokeWidth(dip2px(1) * 1.5f);
            canvas.drawLine(x + indexTrigleEdgeLenght,
                    indexLinePaint.getStrokeWidth(),
                    x - indexTrigleEdgeLenght,
                    indexLinePaint.getStrokeWidth(), indexLinePaint);

            indexLinePaint.setStrokeWidth(dip2px(1));
            indexLinePaint.setColor(res.getColor(R.color.color_E9A79B));
            canvas.drawLine(x - indexTrigleEdgeLenght,
                    indexLinePaint.getStrokeWidth(),
                        x,
                    indexLinePaint.getStrokeWidth() +  indexTrigleEdgeLenght *  0.866f, indexLinePaint);

            canvas.drawLine(x,
                    indexLinePaint.getStrokeWidth() +  indexTrigleEdgeLenght * 0.866f,
                    x + indexTrigleEdgeLenght,
                    indexLinePaint.getStrokeWidth(), indexLinePaint);

            drawDetailMaker(canvas,x,tt);

        }
    }

    private void drawDetailMaker(Canvas canvas,float x,float value){

        if(value <= -1) return;
        indexLinePaint.setColor(res.getColor(R.color.color_F26341));
        canvas.drawRect(x - 6 * indexTrigleEdgeLenght ,
                indexTrigleEdgeLenght * 2,
                x + 6 * indexTrigleEdgeLenght,
                indexTrigleEdgeLenght * 10,indexLinePaint);

        indexLinePaint.setColor(Color.WHITE);
        float baseline = (indexTrigleEdgeLenght * 8 - (indexLinePaint.descent() - indexLinePaint.ascent())) / 2 - indexLinePaint.ascent();
        String tmp = null;
        if((curValueOfXData + 1) < 10){
            tmp = "0"+ (curValueOfXData + 1);
        }

        String date = String.format("%s 年%s 月%s 日","2018","11",tmp);
        String valueStr = String.format("%f",value);

        int textWidth = (int) indexLinePaint.measureText(date);
        int textWidth2 = (int) indexLinePaint.measureText(daySumTips);
        int textWidth3 = (int) indexLinePaint.measureText(valueStr);

        indexLinePaint.setTextSize(xyAxisFontSize);
        canvas.drawText(date,x - textWidth / 2,baseline,indexLinePaint);
        canvas.drawText(daySumTips,x - textWidth2 / 2,baseline * 1.5f,indexLinePaint);
        canvas.drawText(valueStr,x - textWidth3 / 2,baseline * 2.0f,indexLinePaint);
    }

    private void drawTopLine(Canvas canvas){
        indexLinePaint.setColor(res.getColor(R.color.color_E9A79B));
        indexLinePaint.setStrokeWidth(dip2px(1));
        canvas.drawLine(blwidh,
                indexLinePaint.getStrokeWidth(),
                (canvasWidth - blwidh) + (canvasWidth - blwidh) / xRawDatas.size() ,
                indexLinePaint.getStrokeWidth() , indexLinePaint);
    }

    private void drawMidCircle(Canvas canvas,float x){
        float value = getValueByPosX(x);
        if(startDrawIndexLine && value != -1){
            for (int i = 0; i < mPoints.length; i++) {
                if(Math.abs(x - mPoints[i].x) <= 4){
                    midCirclePaint.setColor(res.getColor(R.color.color_E9A79B));
                    canvas.drawCircle(mPoints[i].x, mPoints[i].y, CIRCLE_SIZE * 1.5f , midCirclePaint);
                    midCirclePaint.setColor(Color.WHITE);
                    canvas.drawCircle(mPoints[i].x, mPoints[i].y, CIRCLE_SIZE , midCirclePaint);
                    midCirclePaint.setColor(res.getColor(R.color.color_F26341));
                    canvas.drawCircle(mPoints[i].x, mPoints[i].y, CIRCLE_SIZE / 1.5f , midCirclePaint);
                    break;
                }

            }
        }
    }


    private void drawAima(Canvas canvas){
        canvas.clipRect(new RectF(
                blwidh,
                indexTrigleEdgeLenght - indexLinePaint.getStrokeWidth(),
                (getRight() - getPaddingRight()) * mAnimProgress,
                bheight + dip2px(15) + (int)offsetY)
        );

    }

    private float getValueByPosX(float x){

        float dayUnit = (canvasWidth - 2 * blwidh) / (xRawDatas.size());
        curValueOfXData = (int) ((x - blwidh)  / dayUnit);
        if(curValueOfXData < yRawData.size() && curValueOfXData >= 0){

            return yRawData.get(curValueOfXData).floatValue();
        }
        return -1;

    }

    /**
     * 添加动画实现曲线，原理是使用 画布裁剪
     */
    public void showAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(6000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }


}