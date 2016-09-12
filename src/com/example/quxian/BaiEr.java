package com.example.quxian;

import android.content.Context;
import android.graphics.*;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 田益达 on 2016/9/12.
 */
public class BaiEr extends View {

    private String tex = "9";

    private int defaultSize1 = 100;

    private float ftexsize = 60;
    private float stexsize = 60;

//    定点
    private float fx;
    private float fy;
    private float fr;

//    动点
    private float sx = 0;
    private float sy = 0;
    private float sr = 0;

    private float d = (float) Math.sqrt((fx - sx)*(fx - sx)+(fy-sy)*(fy-sy));
    private float lastx;
    private float lasty;

    private float lfsr = 100;
    private float lssr = 100;
    private float lsfd;
    private float lssd;
    void make(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        float cos = (sy - fy)/d;
        float sin = (sx - fx)/d;

        float ax = fx - fr * cos;
        float ay = fy + fr * sin;

        float ox = (sx - fx)/2 + fx;
        float oy = (sy - fy)/2 + fy;

        float bx = fx + fr * cos;
        float by = fy - fr * sin;

        float cx = sx + sr * cos;
        float cy = sy - sr * sin;

        float dx = sx - sr * cos;
        float dy = sy + sr * sin;

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
//        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);

        TextPaint tex1 = new TextPaint();


        tex1.setAntiAlias(true);
        tex1.setStrokeWidth(3);
        tex1.setTextAlign(Paint.Align.CENTER);
        tex1.setStyle(Paint.Style.FILL_AND_STROKE);
//        tex1.setStyle(Paint.Style.STROKE);
        tex1.setColor(Color.WHITE);
        tex1.setTextSize(ftexsize);


        canvas.drawCircle(fx,fy,fr,paint);
        canvas.drawText(tex, fx, fy+ftexsize/2-3, tex1);

        if(sx != 0){
            Path path = new Path();

            path.moveTo(ax,ay);
            path.quadTo(ox,oy,dx,dy);
            path.lineTo(cx,cy);
            path.quadTo(ox,oy,bx,by);
            path.lineTo(ax,ay);
            canvas.drawPath(path,paint);
            tex1.setTextSize(ftexsize);
            canvas.drawText(tex, fx, fy+ftexsize/2-3, tex1);
            canvas.drawCircle(sx,sy,sr,paint);
            tex1.setTextSize(stexsize);
            canvas.drawText(tex, sx, sy+stexsize/2-3, tex1);

        }
    }

    public BaiEr(Context context) {
        super(context);
        setFdata(500,500,100);
    }

    public BaiEr(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFdata(500,500,100);
    }

    public BaiEr(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFdata(500,500,100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        make(canvas);
    }

    private int getSize(int defaultSize, int measureSpec){
        int mySize = defaultSize;

        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);

        switch (mode){
            case MeasureSpec.UNSPECIFIED:
                mySize = defaultSize1;
                break;
            case MeasureSpec.AT_MOST:
                mySize =size;
                break;
            case MeasureSpec.EXACTLY:
                mySize = size;
                break;
        }
        return mySize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getSize(defaultSize1,widthMeasureSpec);
        int height = getSize(defaultSize1,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    void setFdata(float a,float b,float r){
        fx = a;
        fy = b;
        fr = r;
    }

    void setData(float c,float d1,float r){
        sx = c;
        sy = d1;
        sr = r;
        d = (float) Math.sqrt((fx - sx)*(fx - sx)+(fy-sy)*(fy-sy));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        lssd = d;
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastx = x;
                lasty = y;

                if((x-500)*(x-500)*2 > 10000)
                    return false;

                setData(x,y,100);
                lsfd = d;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                lfsr -= (lssd - lsfd)/10;
                lssr += (lssd - lsfd)/10;
                ftexsize -= (lssd - lsfd)/10;
                stexsize += (lssd - lsfd)/10;
                if(lfsr > 0){

                    setFdata(500,500,lfsr);
                    setData(x,y,lssr);
                }
                lsfd = lssd;
                Log.i("....move..x:",""+x);
                Log.i("....move..y:",""+y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                setFdata(500,500,100);
                setData(0,0,0);
                lfsr = 100;
                lssr = 100;
                lsfd = 0;
                lssd = 0;
                ftexsize = 60;
                stexsize = 60;
                invalidate();
                Log.i("....","up");
                break;
        }
        return true;
    }
}
