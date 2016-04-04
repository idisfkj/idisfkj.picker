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
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by idisfkj on 16/4/3.
 * Email : idisfkj@qq.com.
 */
public class MyPicker<T extends Object> extends PopupWindow implements View.OnClickListener {
    private TextView pickerTitle;
    private Button back;
    private Button OK;
    private PickerView pickerView1;
    private PickerView pickerView2;
    private PickerView pickerView3;

    private T leftText;
    private T middleText;
    private T rightText;
    private List<T> leftList;
    private List<T> middleList;
    private List<T> rightList;
    private int selectedPosition1 = -1;
    private int selectedPosition2 = -1;
    private int selectedPosition3 = -1;
    private SelectedFinishListener mListener;


    public MyPicker(Context context) {
        init(context);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }

    private void init(Context context) {
        View view  = LayoutInflater.from(context).inflate(R.layout.my_picker,null);
        pickerView1 = (PickerView) view.findViewById(R.id.picker_view1);
        pickerView2 = (PickerView) view.findViewById(R.id.picker_view2);
        pickerView3 = (PickerView) view.findViewById(R.id.picker_view3);
        pickerTitle = (TextView) view.findViewById(R.id.picker_title);
        OK = (Button) view.findViewById(R.id.OK);
        back = (Button) view.findViewById(R.id.back);
        OK.setOnClickListener(this);
        back.setOnClickListener(this);

        pickerView1.setOnSelectorListener(new PickerView.OnSelectorListener() {
            @Override
            public void onSelector(String text) {
                leftText = (T) text;
            }
        });
        pickerView2.setOnSelectorListener(new PickerView.OnSelectorListener() {
            @Override
            public void onSelector(String text) {
                middleText = (T) text;
            }
        });
        pickerView3.setOnSelectorListener(new PickerView.OnSelectorListener() {
            @Override
            public void onSelector(String text) {
                rightText = (T) text;
            }
        });
        leftList = new ArrayList<>();
        middleList = new ArrayList<>();
        rightList = new ArrayList<>();

        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable color = new ColorDrawable(0xff000000);
        this.setBackgroundDrawable(color);
        this.update();
    }

    /**
     * default show three
     * @param num
     */
    public void setShowNum(int num){
        switch (num){
            case 1:
                pickerView2.setVisibility(View.GONE);
                pickerView3.setVisibility(View.GONE);
                break;
            case 2:
                pickerView3.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * set title
     * @param text String
     */
    public void setPickerTitle(String text) {
        pickerTitle.setText(text);
    }

    /**
     * Fill in the data
     * @param dataList List
     * @param i Select the picker
     *          1-first  2-second  3-third
     */
    public void setData(List<T> dataList, int i) {
        switch (i) {
            case 1:
                leftList = dataList;
                pickerView1.setData(leftList);
                break;
            case 2:
                middleList = dataList;
                pickerView2.setData(middleList);
                break;
            case 3:
                rightList = dataList;
                pickerView3.setData(rightList);
                break;
        }
    }

    /**
     * set the default centered text,
     * if not set,show centered in the data
     * @param position position in the data
     * @param i  Select the picker
     *          1-first  2-second  3-third
     */
    public void setMiddleText(int position, int i) {
        switch (i) {
            case 1:
                pickerView1.setSelected(position);
                selectedPosition1 = position;
                break;
            case 2:
                pickerView2.setSelected(position);
                selectedPosition2 = position;
                break;
            case 3:
                pickerView3.setSelected(position);
                selectedPosition3 = position;
                break;
        }
    }

    /**
     * Access to select text
     * @param i Select the picker
     *          1-first  2-second  3-third
     * @return String
     */
    public T getText(int i) {
        T text = null;
        switch (i) {
            case 1:
                text = leftText;
                break;
            case 2:
                text = middleText;
                break;
            case 3:
                text = rightText;
                break;
        }
        return text;
    }

    /**
     * redy
     */
    public void setPrepare() {
        if (selectedPosition1 != -1) {
            leftText = leftList.get(selectedPosition1);
        } else {
            leftText = leftList.get(leftList.size() / 2);
        }

        if (selectedPosition2 != -1) {
            middleText = middleList.get(selectedPosition2);
        } else {
            middleText = middleList.get(middleList.size() / 2);
        }

        if (selectedPosition3 != -1) {
            rightText = rightList.get(selectedPosition3);
        } else {
            rightText = rightList.get(rightList.size() / 2);
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            this.dismiss();
        }else {
            if (mListener != null)
            mListener.onFinish();
        }
    }

    public void setSelectedFinishListener(SelectedFinishListener listener){
        mListener = listener;
    }

    public interface SelectedFinishListener {
        void onFinish();
    }
}
