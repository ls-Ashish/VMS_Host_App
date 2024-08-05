package com.leegosolutions.vms_host_app.utility.camera;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.core.content.ContextCompat;

import com.leegosolutions.vms_host_app.R;

public class CS_ScanningOverlayView extends ViewGroup {
    private float left, top, endY;
    private int rectWidth, rectHeight;
    private int frames;
    private boolean revAnimation;
    private int lineColor, lineWidth;

    public CS_ScanningOverlayView(Context context) {
        super(context);
    }

    public CS_ScanningOverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CS_ScanningOverlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CS_ScannerOverlay,
                0, 0);
        rectWidth = a.getInteger(R.styleable.CS_ScannerOverlay_square_width, 230);
        rectHeight = a.getInteger(R.styleable.CS_ScannerOverlay_square_height, 230);
        lineColor = a.getColor(R.styleable.CS_ScannerOverlay_line_color, ContextCompat.getColor(context, R.color.scanner_line));
        lineWidth = a.getInteger(R.styleable.CS_ScannerOverlay_line_width, 4);
        frames = a.getInteger(R.styleable.CS_ScannerOverlay_line_speed, 4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        left = (w - dpToPx(rectWidth)) / 2;
        top = (h - dpToPx(rectHeight)) / 2;
        endY = top;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw transparent rect
        int cornerRadius = 20;
        Paint eraser = new Paint();
        eraser.setAntiAlias(true);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        // Border
        Paint strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        //strokePaint.setStyle(Paint.Style.valueOf());
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStrokeWidth(10);


        RectF rect = new RectF(left, top, dpToPx(rectWidth) + left, dpToPx(rectHeight) + top);
        canvas.drawRoundRect(rect, (float) cornerRadius, (float) cornerRadius, eraser);
        canvas.drawRoundRect(rect, (float) cornerRadius, (float) cornerRadius, strokePaint);

        // draw horizontal line
        Paint line = new Paint();
        line.setColor(lineColor);
        line.setStrokeWidth(Float.valueOf(lineWidth));

        // draw the line to product animation
        if (endY >= top + dpToPx(rectHeight - 10) + frames) { // -10 added as the scanning line was reaching the bottom border
            revAnimation = true;
        } else if (endY == top + frames) {
            revAnimation = false;
        }

        // check if the line has reached to bottom
        if (revAnimation) {
            endY -= frames;
        } else {
            endY += frames;
        }
        canvas.drawLine(left + 10, endY, left + dpToPx(rectWidth) - 10, endY, line);
        invalidate();
    }
}