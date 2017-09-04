package com.baway.mouthevent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 类的描述：
 * 时间：  2017/9/4.19:07
 * 姓名：chenlong
 */

public class CicleView extends View{

    private float circleX;
    private float circleY;
    private final float radius;
    private final Paint paint;
    private final int DRAG = 1;//拖拽
    private final int ZOOM = 2;//缩放
    private final int IDLE = 3;//空闲状态
    private int mode = IDLE;
    private double startDis;//起始时，两个手指的距离
    private float resultDis;

    public CicleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CicleView);
        //获取属性值
        circleX = typedArray.getDimension(R.styleable.CicleView_circleX, 50);
        circleY = typedArray.getDimension(R.styleable.CicleView_circleY, 50);
        radius = typedArray.getDimension(R.styleable.CicleView_radius, 50);
        typedArray.recycle();

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(circleX,circleY,radius+resultDis,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
              mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                Log.e("CircleView", "ZOOM");
                //获取两个点的x y
                float px1 = event.getX(0);
                float px2 = event.getX(1);
                float py1 = event.getY(0);
                float py2 = event.getY(1);
                float distanceX = px2 - px1;
                float distanceY = py2 - py1;
                startDis = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    circleX = event.getX();
                    circleY = event.getY();
                    invalidate();
                } else {
                    float mpx1 = event.getX(0);
                    float mpx2 = event.getX(1);
                    float mpy1 = event.getY(0);
                    float mpy2 = event.getY(1);
                    float mdistanceX = mpx2 - mpx1;
                    float mdistanceY = mpy2 - mpy1;
                    double endDis = Math.sqrt(mdistanceX * mdistanceX + mdistanceY * mdistanceY);
                    resultDis = (float) (endDis - startDis);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mode = IDLE;
                break;
        }
        return true;
    }
}
