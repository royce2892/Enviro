package com.prappz.envi.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.prappz.envi.R;
import com.prappz.envi.application.MyApplication;

/**
 * Created by royce on 05-05-2016.
 */
public class SentBubbleView extends LinearLayout {

    private Paint mPaint;

    private int tailOffsetSide = 20;
    private int tailOffsetTop = 20;
    private int tailOffsetBottom = 50;

    private final int cStrokeWidth = 6;
    private final int tailStrokeWidth = 4;

    private BubbleType mType = BubbleType.TOP;

    public SentBubbleView(Context context) {
        super(context);
        init();
    }

    public SentBubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SentBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SentBubbleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.ripple));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
//        mPointedBackgroundPaint.setDither(true);
//        mPointedBackgroundPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPointedBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPointedBackgroundPaint.setPathEffect(new CornerPathEffect(5));

//        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
//        int densityFactor=metrics.densityDpi/160;
        Context c = MyApplication.getInstance();
        tailOffsetTop = dpToPixel(c, 10);
        tailOffsetSide = dpToPixel(c, 10);
        tailOffsetBottom = dpToPixel(c, 25);
    }

    public static int dpToPixel(Context c, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                c.getResources().getDisplayMetrics());
    }

    public void setType(BubbleType type) {
        mType = type;
    }

    public void setColorId(int colorId) {
        mPaint.setColor(getResources().getColor(colorId));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
    }

    private void drawBackground(Canvas canvas) {
        switch (mType) {
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

    private void drawTopBubbleBackground(Canvas canvas) {
        int width = getWidth();
        int bubbleWidth = width - tailOffsetSide;
        int height = getHeight();

        //Top Line
        Point a = new Point(0, 0);
        Point b = new Point(bubbleWidth, 0);
        //Tail points
        Point c = new Point(bubbleWidth, tailOffsetTop);
        Point d = new Point(width, tailOffsetTop);
        Point e = new Point(bubbleWidth, tailOffsetBottom);
        //Bottom Line
        Point f = new Point(bubbleWidth, height);
        Point g = new Point(0, height);

        Path cPath = new Path();
        cPath.moveTo(b.x, b.y);
        cPath.lineTo(a.x, a.y);
        cPath.lineTo(g.x, g.y);
        cPath.lineTo(f.x, f.y);

        mPaint.setStrokeWidth(cStrokeWidth);
        canvas.drawPath(cPath, mPaint);

        //Draw tail
        Path tailPath = new Path();
        tailPath.moveTo(b.x, b.y);
        tailPath.lineTo(c.x, c.y);
        tailPath.lineTo(d.x, d.y);
        tailPath.lineTo(e.x, e.y);
        tailPath.lineTo(f.x, f.y);

        mPaint.setStrokeWidth(tailStrokeWidth);
        canvas.drawPath(tailPath, mPaint);
    }

    private void drawMiddleBubbleBackground(Canvas canvas) {
        int width = getWidth();
        int bubbleWidth = width - tailOffsetSide;
        int height = getHeight();

        Point a = new Point(0, 0);
        Point b = new Point(bubbleWidth, 0);
        Point c = new Point(bubbleWidth, height);
        Point d = new Point(0, height);

        Path path = new Path();
        path.moveTo(b.x, b.y);
        path.lineTo(a.x, a.y);
        path.lineTo(d.x, d.y);
        path.lineTo(c.x, c.y);
        mPaint.setStrokeWidth(cStrokeWidth);
        canvas.drawPath(path, mPaint);

        Path pathSide = new Path();
        pathSide.moveTo(b.x, b.y);
        pathSide.lineTo(c.x, c.y);
        mPaint.setStrokeWidth(tailStrokeWidth);
        canvas.drawPath(pathSide, mPaint);
    }


}
