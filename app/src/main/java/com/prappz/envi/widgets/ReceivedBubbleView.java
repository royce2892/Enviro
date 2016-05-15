package com.prappz.envi.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.prappz.envi.R;

/**
 * Created by royce on 05-05-2016.
 */
public class ReceivedBubbleView extends LinearLayout {

    private Paint mPointedBackgroundPaint;

    private int tailOffsetSide = 20;
    private int tailOffsetTop = 20;
    private int tailOffsetBottom = 50;

    private final int cStrokeWidth = 6;
    private final int tailStrokeWidth = 4;

    private BubbleType mType = BubbleType.TOP;

    public ReceivedBubbleView(Context context) {
        super(context);
        init();
    }

    public ReceivedBubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReceivedBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ReceivedBubbleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mPointedBackgroundPaint = new Paint();
        mPointedBackgroundPaint.setColor(getResources().getColor(R.color.green_overlay));
        mPointedBackgroundPaint.setStyle(Paint.Style.FILL);
        mPointedBackgroundPaint.setAntiAlias(true);
//        mPointedBackgroundPaint.setDither(true);
//        mPointedBackgroundPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPointedBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPointedBackgroundPaint.setPathEffect(new CornerPathEffect(5));

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int densityFactor=metrics.densityDpi/160;
        tailOffsetTop = densityFactor * 10;
        tailOffsetSide = densityFactor * 10;
        tailOffsetBottom = densityFactor * 25;
    }

    public void setType(BubbleType type){
        mType = type;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
    }

    private void drawBackground(Canvas canvas) {
        switch(mType){
            case TOP:
                drawTopBubbleBackground(canvas);
                break;
            case MIDDLE:
                drawMiddleBubbleBackground(canvas);
                break;
            case BOTTOM:
                break;
        }
    }

    private void drawTopBubbleBackground(Canvas canvas){
        int width = getWidth();
        int bubbleWidth = width - tailOffsetSide;
        int height = getHeight();

        //Top Line
        Point a = new Point(tailOffsetSide, 0);
        Point b = new Point(width, 0);
        // Tail points
        Point e = new Point(tailOffsetSide, tailOffsetTop);
        Point f = new Point(0, tailOffsetTop);
        Point g = new Point(tailOffsetSide, tailOffsetBottom);
        //Botom line
        Point c = new Point(width, height);
        Point d = new Point(tailOffsetSide, height);

        Path cPath = new Path();
        cPath.moveTo(e.x, e.y);
        cPath.lineTo(a.x, a.y);
        cPath.lineTo(b.x, b.y);
        cPath.lineTo(c.x, c.y);
        cPath.lineTo(d.x, d.y);
        cPath.lineTo(g.x, g.y);

        mPointedBackgroundPaint.setStrokeWidth(cStrokeWidth);
        canvas.drawPath(cPath, mPointedBackgroundPaint);

        //Draw tail
        Path tailPath = new Path();
        tailPath.moveTo(e.x, e.y);
        tailPath.lineTo(f.x, f.y);
        tailPath.lineTo(g.x, g.y);

        mPointedBackgroundPaint.setStrokeWidth(tailStrokeWidth);
        canvas.drawPath(tailPath, mPointedBackgroundPaint);
    }

    private void drawMiddleBubbleBackground(Canvas canvas){
        int width = getWidth();
        int height = getHeight();

        Point a = new Point(tailOffsetSide, 0);
        Point b = new Point(width, 0);
        Point c = new Point(width, height);
        Point d = new Point(tailOffsetSide, height);

        Path path = new Path();
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(d.x, d.y);
        path.lineTo(a.x, a.y);

        mPointedBackgroundPaint.setStrokeWidth(cStrokeWidth);
        canvas.drawPath(path, mPointedBackgroundPaint);
    }
}