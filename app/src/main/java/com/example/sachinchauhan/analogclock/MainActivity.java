package com.example.sachinchauhan.analogclock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.sql.Time;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ClockView(this));
        getSupportActionBar().hide();

        final Handler handler = new Handler();
        final Boolean mStopHandler=false;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setContentView(new ClockView(getApplicationContext()));
                if(!mStopHandler)
                    handler.postDelayed(this,1000);
            }
        };
       handler.post(runnable);
    }
    public class ClockView extends View {
        Context mContext;
        Paint paint=null;

        public ClockView(Context context) {
            super(context);
            mContext=context;
            paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);

            canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
            paint.setStyle(Paint.Style.FILL);
            Date date = new Date(System.currentTimeMillis());
               int seconds = date.getSeconds();
               int minutes = date.getMinutes();
               int hours = date.getHours();
                float center_x = getWidth() / 2;
                float center_y = getHeight() / 2;
                float radius = center_x;

                Log.i("--Hour: "+hours,"--Minute: "+minutes+"--Second: "+seconds);
               drawClock(canvas, center_x, center_y, radius);
               drawSecondHand(canvas, center_x, center_y, radius, seconds);
               drawMinuteHand(canvas, center_x, center_y, radius, minutes);
               drawHourHand(canvas, center_x, center_y, radius, hours);
            //inner centre
            paint.setColor(mContext.getResources().getColor(R.color.innerRingColor));
            canvas.drawCircle(center_x,center_y,20,paint);
        }

        public void drawClock(Canvas canvas,float center_x,float center_y,float radius){

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mContext.getResources().getColor(R.color.backgroundColor));
            canvas.drawPaint(paint);

            paint.setColor(mContext.getResources().getColor(R.color.tickerColor));
            paint.setTextSize(120);
            canvas.drawText("Ticker!",center_x-170,160,paint);

            //outer ring--black
            paint.setColor(mContext.getResources().getColor(R.color.clockBoundaryColor));
            canvas.drawCircle(center_x,center_y,radius-10,paint);
            //inside triangle part
            paint.setColor(mContext.getResources().getColor(R.color.triangleColor));
            canvas.drawCircle(center_x,center_y,radius-27,paint);

            final RectF oval = new RectF();
            oval.set(center_x - radius+27,
                    center_y - radius+27,
                    center_x + radius-27,
                    center_y + radius-27);

            //lower half--cyan
            paint.setColor(mContext.getResources().getColor(R.color.lowerHalfColor));
            canvas.drawArc(oval, 45,175, false, paint);
            //upper half--light green
            paint.setColor(mContext.getResources().getColor(R.color.upperHalfColor));
            canvas.drawArc(oval, 45,-175, false, paint);

            //outer centre
            paint.setColor(mContext.getResources().getColor(R.color.outerRingColor));
            canvas.drawCircle(center_x,center_y,40,paint);

        }

        public void drawSecondHand(Canvas canvas,float center_x,float center_y,float radius,int seconds){
            paint.setColor(mContext.getResources().getColor(R.color.handColor));

            double angle=6*seconds*Math.PI/180;
            float length=radius-60;
            float back_length=120;
            float start_x=center_x;
            float start_y=center_y;
            float end_x= (float) (center_x+length*Math.sin(angle)) ;
            float end_y= (float) (center_y-length*Math.cos(angle));

            paint.setStrokeWidth(6);
            canvas.drawLine(start_x,start_y,end_x,end_y,paint);
            //back line
            end_x= (float) (center_x-back_length*Math.sin(angle)) ;
            end_y= (float) (center_y+back_length*Math.cos(angle));
            // paint.setStrokeWidth(10);
            canvas.drawLine(start_x,start_y,end_x,end_y,paint);
        }

        public void drawMinuteHand(Canvas canvas,float center_x,float center_y,float radius,int minutes){
            paint.setColor(mContext.getResources().getColor(R.color.handColor));

            double angle=minutes*6*Math.PI/180;
            float length=radius-90;
            float back_length=90;
            float start_x=center_x;
            float start_y=center_y;
            float end_x= (float) (center_x+length*Math.sin(angle));
            float end_y= (float) (center_y-length*Math.cos(angle));
            paint.setStrokeWidth(10);
            canvas.drawLine(start_x,start_y,end_x,end_y,paint);

            end_x= (float) (center_x-back_length*Math.sin(angle)) ;
            end_y= (float) (center_y+back_length*Math.cos(angle));

            canvas.drawLine(start_x,start_y,end_x,end_y,paint);

        }

        public void drawHourHand(Canvas canvas,float center_x,float center_y,float radius,int hours){
            paint.setColor(mContext.getResources().getColor(R.color.handColor));

            double angle=hours*30*Math.PI/180;
            float length=radius-160;
            float back_length=70;
            float start_x=center_x;
            float start_y=center_y;
            float end_x= (float) (center_x+length*Math.sin(angle));
            float end_y= (float) (center_y-length*Math.cos(angle));
            paint.setStrokeWidth(10);
            canvas.drawLine(start_x,start_y,end_x,end_y,paint);

            end_x= (float) (center_x-back_length*Math.sin(angle)) ;
            end_y= (float) (center_y+back_length*Math.cos(angle));

            canvas.drawLine(start_x,start_y,end_x,end_y,paint);

        }
    }
}