package com.example.paint;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class SimplePaint extends View
{
    List<Paint> paintList;
    List<Path> pathList;
    Paint currentPaint;
    Path currentPath;
    ColorDrawable currentColor;
    StyleType style = StyleType.line;

    float auxLxStart = 0, auxLxEnd = 0, auxLyStart = 0, auxLyEnd = 0;

    public SimplePaint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paintList = new ArrayList<>();
        pathList = new ArrayList<>();

        currentColor = new ColorDrawable();
        currentColor.setColor(Color.BLACK);

        startPaint();
    }

    public void startPaint() {
        currentPaint = new Paint();
        currentPath = new Path();

        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeWidth(20);
        currentPaint.setColor(currentColor.getColor());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int idx = 0; idx < paintList.size(); idx++)
            canvas.drawPath(pathList.get(idx), paintList.get(idx));

        canvas.drawPath(currentPath, currentPaint);

        switch (style) {
            case line:
                canvas.drawPath(currentPath, currentPaint);
                break;
            case circle:
            case square:
                break;
        }

        canvas.drawPath(currentPath, currentPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ly, lx;

        lx = event.getX();
        ly = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                currentPath.moveTo(lx, ly);
                currentPath.lineTo(lx, ly);
                auxLxStart = lx;
                auxLyEnd = ly;
                break;
            case MotionEvent.ACTION_MOVE:
                auxLxEnd = lx;
                auxLyEnd= ly;
                double dMoving = Math.sqrt(Math.pow(auxLxEnd - auxLxStart, 2) + Math.pow(auxLyEnd - auxLyStart, 2));

                if (style == StyleType.line)
                {
                    currentPath.lineTo(lx, ly);
                }
                else if (style == StyleType.circle)
                {
                    float radius = (float) (dMoving / 2);
                    float x = (auxLxStart + auxLxEnd) / 2;
                    float y = (auxLyStart + auxLyEnd) / 2;
                    currentPath.reset();
                    currentPath.addCircle(x, y, radius, Path.Direction.CW);
                }
                else if (style == StyleType.square)
                {
                    currentPath.reset();
                    currentPath.addRect(auxLxStart,auxLyStart,auxLxEnd,auxLyEnd, Path.Direction.CCW);
                }
                break;
            case MotionEvent.ACTION_UP:
                auxLxEnd = lx;
                auxLyEnd= ly;

                if (style == StyleType.line)
                {
                    currentPath.lineTo(lx, ly);
                }
                else if (style == StyleType.circle)
                {
                    double dFinal = Math.sqrt(Math.pow(auxLxEnd - auxLxStart, 2) + Math.pow(auxLyEnd - auxLyStart, 2));
                    float radius = (float) (dFinal / 2);
                    float x = (auxLxStart + auxLxEnd) / 2;
                    float y = (auxLyStart + auxLyEnd) / 2;
                    currentPath.addCircle(x, y, radius, Path.Direction.CW);
                }
                else if (style == StyleType.square)
                {
                    currentPath.addRect(auxLxStart,auxLyStart,auxLxEnd,auxLyEnd, Path.Direction.CCW);
                }

                paintList.add(currentPaint);
                pathList.add(currentPath);
                startPaint();
                break;
            default:
                break;
        }

        invalidate();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setColor(Color color) {
        currentColor.setColor(color.toArgb());
        currentPaint.setColor(color.toArgb());
    }

    public void backDraw() {
        if (paintList.isEmpty())
            return;

        paintList.remove(paintList.size() - 1);
        pathList.remove(pathList.size() - 1);

        invalidate();
    }

    public void removeDraw() {
        if (paintList.isEmpty())
            return;

        paintList.clear();
        pathList.clear();

        invalidate();
    }

    public void setStyleType(StyleType style) {
        this.style = style;
    }
}

enum StyleType {
    line,
    circle,
    square,
}