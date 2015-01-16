package com.example.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.R;
import com.example.utils.view.TimeView;

public class MainActivity extends Activity {
    TextView copy;
    private ScaleGestureDetector mGestureDetector;
    private OnScaleGestureListener mGestureListener = new OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            final float scale = detector.getScaleFactor();
            if (Float.isNaN(scale) || Float.isInfinite(scale)) {
                return false;
            }

            if (scale <= 0.95) {
                Toast.makeText(MainActivity.this, "onScale" + scale,
                        Toast.LENGTH_SHORT).show();
            }

            if (scale >= 1.05) {
                Toast.makeText(MainActivity.this, "onScale:" + scale,
                        Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copy = (TextView) findViewById(R.id.copy);
        TimeView view = (TimeView) findViewById(R.id.time);
        view.setCallback(new BackgroundCallback<View, Void, Void>() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void callback(View view, Void f, Void g) {
                copy.setBackground(create((TimeView) view));

            }
        });

        mGestureDetector = new ScaleGestureDetector(this, mGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private Drawable create(TimeView view) {
        Bitmap bm = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bm);

        final Rect rect = new Rect();
        view.getDrawingRect(rect);
        // //////////////////////////////////////
        canvas.save();
        canvas.translate(-view.getScrollX(), -view.getScrollY());
        canvas.clipRect(rect);
        view.draw(canvas);
        canvas.restore();
        // //////////////////////////////////////

        canvas.setBitmap(null);
        Toast.makeText(this, "bitmap: " + bm.getByteCount(), Toast.LENGTH_SHORT)
                .show();
        return new BitmapDrawable(getResources(), bm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public interface BackgroundCallback<E, F, G> {
        void callback(E e, F f, G g);

    }

}
