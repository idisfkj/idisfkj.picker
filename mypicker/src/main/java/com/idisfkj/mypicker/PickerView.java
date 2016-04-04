/*
 * Copyright (c) 2016. The Android Open Source Project
 * Created by idisfkj
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idisfkj.mypicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by idisfkj on 16/4/3.
 * Email : idisfkj@qq.com.
 */
public class PickerView<T extends Object> extends View{
    private Paint mPaint;
    private List<T> dataList;
    private Timer mTimer;
    private int screenWidth;
    private int screenHeight;
    private int maxTextSize = 80;
    private int minTextSize = 40;
    private boolean isReady = false;

    private float moveLength = 0;
    private int maxAlpha = 255;
    private int minAlpha = 120;
    private int position = -1;

    private static final float MARGIN = 3.0f;

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
        dataList = new ArrayList<>();
        mTimer = new Timer();
    }

    /**
     * set the default centered text
     * @param position position in the data
     */
    public void setSelected(int position) {
        this.position = position;
    }

    public void setData(List<T> list) {
        dataList = list;
        // if not set the default centered text,show the default centered text in the data
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

    private void onDrawView(Canvas canvas) {
        float scal = getParabola((screenHeight / 4.0f), moveLength);
        float x = screenWidth / 2.0f;
        float y = screenHeight / 2.0f + moveLength;
        float size = (maxTextSize - minTextSize) * scal + minTextSize;
        Paint.FontMetricsInt pfm = mPaint.getFontMetricsInt();
        float baseLine = y - (pfm.top + pfm.bottom) / 2;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((maxAlpha - minAlpha) * scal + minAlpha));
        canvas.drawText(String.valueOf(dataList.get(position)), x, baseLine, mPaint);

        for (int i = 1; position - i >= 0; i++) {
            drawOtherView(canvas, i, -1);
        }
        for (int i = 1; position + i < dataList.size(); i++) {
            drawOtherView(canvas, i, 1);
        }
    }

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
        canvas.drawText(String.valueOf(dataList.get(position + direction * i)), x, baseLine, mPaint);
    }

    /**
     * parabola
     * @param zero
     * @param offsetY
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
            moveFootTOHead();
            moveLength = moveLength - MARGIN * minTextSize;
        } else if (moveLength < -MARGIN * minTextSize / 2) {
            moveHeadToFoot();
            moveLength = moveLength + MARGIN * minTextSize;
        }
        eventY = event.getY();
        invalidate();
    }


    private void moveHeadToFoot() {
        T head = dataList.get(0);
        dataList.remove(0);
        dataList.add(head);
    }

    private void moveFootTOHead() {
        T foot = dataList.get(dataList.size() - 1);
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
        mTimer.schedule(myTimeTask, 0, 100);
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
            if (Math.abs(moveLength) < MARGIN * minTextSize / 2) {
                moveLength = 0;
                if (myTimeTask != null) {
                    myTimeTask.cancel();
                    myTimeTask = null;
                    completeSelector();
                }
            } else {
                if (moveLength < 0) {
                    moveHeadToFoot();
                } else {
                    moveFootTOHead();
                }
                moveLength = moveLength - moveLength / Math.abs(moveLength) * MARGIN * minTextSize / 2;
            }
            invalidate();
        }
    };

    private void completeSelector() {
        if (mListener != null)
            mListener.onSelector(String.valueOf(dataList.get(position)));
    }

    public void setOnSelectorListener(OnSelectorListener listener) {
        mListener = listener;
    }

    public interface OnSelectorListener {
        void onSelector(String text);
    }
}
