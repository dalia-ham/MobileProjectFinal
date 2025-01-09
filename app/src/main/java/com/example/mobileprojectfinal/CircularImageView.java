package com.example.mobileprojectfinal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CircularImageView extends AppCompatImageView {

    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addOval(rect, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
