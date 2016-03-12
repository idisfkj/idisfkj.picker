package com.idisfkj.picker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.idisfkj.picker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by idisfkj on 16/3/9.
 * Email : idisfkj@qq.com.
 */
public class PickerView extends View {
    private Paint mPaint;
    private List<String> dataList;
    private Timer mTimer;
    private int screenWidth;
    private int screenHeight;
    private int maxTextSize = 80;
    private int minTextSize = 40;
    private boolean isReady = false;
    /**
     * 滑动距离
     */
    private float moveLength = 0;
    private int maxAlpha = 255;
    private int minAlpha = 120;
    private int position = -1;
    /**
     * 字体间的间距
     */
    private static final float MARGIN = 3.0f;
    /**
     * 速度
     */
    private static final float SPEED = 2.0f;
    private float eventY;
    private MyTimeTask myTimeTask;
    private OnSelectorListener mListener;

    public PickerView(Context context) {
        super(context);
        init();
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.paintColor));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);
        dataList = new ArrayList<String>();
        mTimer = new Timer();
    }

    /**
     * 设置居中显示的文本
     * @param position dataList中的位置
     */
    public void setSelected(int position) {
        this.position = position;
    }

    /**
     * 添加数据
     * @param list
     */
    public void setData(List<String> list) {
        dataList = list;
        //如果没有自定义居中显示的文本，则默认显示中间的文本
        if (position == -1) {
            position = dataList.size() / 2;
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();
        maxTextSize = (int) (screenHeight / 8.0f);
        minTextSize = (int) (maxTextSize / 2.0f);
        isReady = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isReady)
            onDrawView(canvas);
    }

    /**
     * 绘图
     * @param canvas
     */
    private void onDrawView(Canvas canvas) {
        float scal = getParabola((screenHeight / 4.0f), moveLength);
        //字体居中显示
        float x = screenWidth / 2.0f;
        float y = screenHeight / 2.0f + moveLength;
        float size = (maxTextSize - minTextSize) * scal + minTextSize;
        //获得Paint的属性参数
        Paint.FontMetricsInt pfm = mPaint.getFontMetricsInt();
        //得到居中y
        float baseLine = y - (pfm.top + pfm.bottom) / 2;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((maxAlpha - minAlpha) * scal + minAlpha));
        //画中间位置
        canvas.drawText(dataList.get(position), x, baseLine, mPaint);

        //画上面位置
        for (int i = 1; position - i >= 0; i++) {
            drawOtherView(canvas, i, -1);
        }
        //画下面的位置
        for (int i = 1; position + i < dataList.size(); i++) {
            drawOtherView(canvas, i, 1);
        }
    }

    /**
     * 绘上／下文本
     * @param canvas
     * @param i 距中间文本的个数
     * @param direction 方向 1代表下 -1代表上
     */
    private void drawOtherView(Canvas canvas, int i, int direction) {
        float offsetY = (MARGIN * minTextSize * i + moveLength * direction);
        float scal = getParabola(screenHeight / 4.0f, offsetY);
        float x = screenWidth / 2.0f;
        float y = screenHeight / 2.0f + direction * offsetY;
        float size = (maxTextSize - minTextSize) * scal + minTextSize;
        float alpha = (maxAlpha - minAlpha) * scal + minAlpha;
        Paint.FontMetricsInt pfm = mPaint.getFontMetricsInt();
        float baseLine = (float) (y - (pfm.top + pfm.bottom) / 2.0);
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) alpha);
        canvas.drawText(dataList.get(position + direction * i), x, baseLine, mPaint);
    }

    /**
     * 抛物线
     * @param zero 圆点坐标
     * @param offsetY 偏移量
     * @return y = x^2
     */
    private float getParabola(float zero, float offsetY) {
        float res = (float) (1 - Math.pow(offsetY / zero, 2));
        return res < 0 ? 0 : res;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (myTimeTask != null) {
            myTimeTask.cancel();
            myTimeTask = null;
        }
        eventY = event.getY();
    }

    private void doMove(MotionEvent event) {
        moveLength += event.getY() - eventY;
        if (moveLength > MARGIN * minTextSize / 2) {
            //手指向下滑动滑出边界
            moveFootTOHead();
            moveLength = moveLength - MARGIN * minTextSize;
        } else if (moveLength < -MARGIN * minTextSize / 2) {
            //手指向上滑动滑出边界
            moveHeadToFoot();
            moveLength = moveLength + MARGIN * minTextSize;
        }
        eventY = event.getY();
        invalidate();
    }


    private void moveHeadToFoot() {
        String head = dataList.get(0);
        dataList.remove(0);
        dataList.add(head);
    }

    private void moveFootTOHead() {
        String foot = dataList.get(dataList.size() - 1);
        dataList.remove(dataList.get(dataList.size() - 1));
        dataList.add(0, foot);
    }

    private void doUp(MotionEvent event) {
        if (Math.abs(moveLength) < 0.0001) {
            moveLength = 0;
            return;
        }
        if (myTimeTask != null) {
            myTimeTask.cancel();
            myTimeTask = null;
        }
        myTimeTask = new MyTimeTask(mHandler);
        mTimer.schedule(myTimeTask, 0, 10);
    }

    public class MyTimeTask extends TimerTask {
        private Handler mHandler;

        public MyTimeTask(Handler handler) {
            mHandler = handler;
        }

        @Override
        public void run() {
            mHandler.sendMessage(mHandler.obtainMessage());
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(moveLength) < SPEED) {
                moveLength = 0;
                if (myTimeTask != null) {
                    myTimeTask.cancel();
                    myTimeTask = null;
                    completeSelector();
                }
            } else
                moveLength = moveLength - moveLength / Math.abs(moveLength) * SPEED;
            invalidate();
        }
    };

    /**
     * 完成选择
     */
    private void completeSelector() {
        if (mListener != null)
            mListener.onSelector(dataList.get(position));
    }

    public void setOnSelectorListener(OnSelectorListener listener) {
        mListener = listener;
    }

    public interface OnSelectorListener {
        void onSelector(String text);
    }
}
