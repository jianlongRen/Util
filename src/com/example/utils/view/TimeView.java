package com.example.utils.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.MainActivity.BackgroundCallback;

public class TimeView extends TextView {
    private Time mTime;
    private BackgroundCallback<View, Void, Void> mCallback;

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        Toast.makeText(getContext(), "开始", Toast.LENGTH_SHORT).show();
        if (null == mTime) {
            mTime = new Time();
            mTime.setName("Thread: TimeView.Time");
            mTime.start();
        }

        super.onAttachedToWindow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        setBackgroundColor(randomColor());
        mCallback.callback(this, null, null);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        Toast.makeText(getContext(), "停止", Toast.LENGTH_SHORT).show();
        if (null != mTime) {
            mTime.cancle();
        }
        super.onDetachedFromWindow();

    }

    public void setCallback(BackgroundCallback<View, Void, Void> callback) {
        mCallback = (BackgroundCallback<View, Void, Void>) callback;
    }

    private class Time extends Thread {

        private boolean isRun = false;
        private String oldText = "";

        @Override
        public synchronized void start() {
            isRun = true;
            super.start();
        }

        public synchronized void cancle() {
            isRun = false;
        }

        @Override
        public void run() {
            short pause = 0;
            while (isRun) {
                final String time = time();
                if (!TextUtils.equals(oldText, time)) {
                    oldText = time;
                    post(new Runnable() {

                        @Override
                        public void run() {
                            setText(time);
                        }
                    });
                }
                if (((++pause) % 5 == 0)) {
                    post(new Runnable() {

                        @Override
                        public void run() {
                            setBackgroundColor(randomColor());
                        }
                    });
                }
                sleep();
            }
        }

        private String time() {
            return SimpleDateFormat.getDateTimeInstance().format(new Date());
        }

        private void sleep() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int randomColor() {
        Next next = new Next();
        return Color.rgb(next.next(), next.next(), next.next());
    }

    private class Next {
        final Random mRandom = new Random();
        final int min = 0x0072;
        final int max = 0x0001 << 8;

        public int next() {
            int next = mRandom.nextInt(max);
            if (next < min) {
                next = next();
            }
            return next;
        }
    };

}
