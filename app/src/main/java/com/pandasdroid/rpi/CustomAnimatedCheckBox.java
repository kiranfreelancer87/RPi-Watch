package com.pandasdroid.rpi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class CustomAnimatedCheckBox extends View {
    private boolean isChecked = false;
    private Paint checkMarkPaint;
    private RectF checkboxRect;
    private int borderColor = Color.BLACK;
    private int checkColor = Color.GREEN;
    private int borderThickness = 4;
    private float checkSize = 0.3f; // Fraction of checkbox size
    private float animProgress = 0;
    private CheckboxAnimation anim;

    public CustomAnimatedCheckBox(Context context) {
        super(context);
        init();
    }

    public CustomAnimatedCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomAnimatedCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        checkMarkPaint = new Paint();
        checkMarkPaint.setColor(checkColor);
        checkMarkPaint.setStrokeWidth(6);
        checkMarkPaint.setStyle(Paint.Style.STROKE);
        checkMarkPaint.setAntiAlias(true);
        checkboxRect = new RectF();
        anim = new CheckboxAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int minSize = Math.min(width, height);
        checkboxRect.set(0, 0, minSize, minSize);

        Paint borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderThickness);
        canvas.drawRoundRect(checkboxRect, 10, 10, borderPaint);

        float startX = minSize * 0.25f;
        float startY = minSize * 0.5f;
        float midX = minSize * 0.5f;
        float midY = minSize * 0.75f;
        float endX = minSize * (0.5f + checkSize);
        float endY = minSize * (0.5f + checkSize);

        if (animProgress > 0) {
            // Draw an animated checkmark
            float animatedMidX = midX - (midX - startX) * animProgress;
            float animatedMidY = midY - (midY - startY) * animProgress;
            float animatedEndX = midX + (endX - midX) * animProgress;
            float animatedEndY = midY + (endY - midY) * animProgress;
            canvas.drawLine(startX, startY, animatedMidX, animatedMidY, checkMarkPaint);
            canvas.drawLine(animatedMidX, animatedMidY, animatedEndX, animatedEndY, checkMarkPaint);
        } else if (isChecked) {
            // Draw the full checkmark when the checkbox is checked
            canvas.drawLine(startX, startY, midX, midY, checkMarkPaint);
            canvas.drawLine(midX, midY, endX, endY, checkMarkPaint);
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        if (checked != isChecked) {
            isChecked = checked;
            if (isChecked) {
                startAnimation();
            }
            invalidate();
        }
    }

    public void setBorderColor(int color) {
        borderColor = color;
        invalidate();
    }

    public void setCheckColor(int color) {
        checkColor = color;
        checkMarkPaint.setColor(color);
        invalidate();
    }

    public void setBorderThickness(int thickness) {
        borderThickness = thickness;
        invalidate();
    }

    public void setCheckSize(float size) {
        checkSize = size;
        invalidate();
    }

    private void startAnimation() {
        anim.reset();
        anim.setDuration(500);
        startAnimation(anim);
    }

    private class CheckboxAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            animProgress = interpolatedTime;
            invalidate();
        }
    }
}
