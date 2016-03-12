package com.idisfkj.picker.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.idisfkj.picker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by idisfkj on 16/3/12.
 * Email : idisfkj@qq.com.
 */
public class TextPicker extends PopupWindow implements View.OnClickListener {

    private TextView pickerTitle;
    private Button back;
    private Button OK;
    private PickerView pickerView1;
    private PickerView pickerView2;
    private PickerView pickerView3;

    private String leftText;
    private String middleText;
    private String rightText;
    private List<String> leftList;
    private List<String> middleList;
    private List<String> rightList;
    private int selectedPosition1 = -1;
    private int selectedPosition2 = -1;
    private int selectedPosition3 = -1;


    public TextPicker(Context context) {
        init(context);
        back.setOnClickListener(this);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.picker, null);
        pickerView1 = (PickerView) view.findViewById(R.id.picker_view1);
        pickerView2 = (PickerView) view.findViewById(R.id.picker_view2);
        pickerView3 = (PickerView) view.findViewById(R.id.picker_view3);
        pickerTitle = (TextView) view.findViewById(R.id.picker_title);
        OK = (Button) view.findViewById(R.id.OK);
        back = (Button) view.findViewById(R.id.back);

        pickerView1.setOnSelectorListener(new PickerView.OnSelectorListener() {
            @Override
            public void onSelector(String text) {
                leftText = text;
            }
        });
        pickerView2.setOnSelectorListener(new PickerView.OnSelectorListener() {
            @Override
            public void onSelector(String text) {
                middleText = text;
            }
        });
        pickerView3.setOnSelectorListener(new PickerView.OnSelectorListener() {
            @Override
            public void onSelector(String text) {
                rightText = text;
            }
        });
        leftList = new ArrayList<String>();
        middleList = new ArrayList<String>();
        rightList = new ArrayList<String>();

        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable color = new ColorDrawable(0xff000000);
        this.setBackgroundDrawable(color);
        this.update();
    }


    /**
     * 设置标题
     * @param text String
     */
    public void setPickerTitle(String text) {
        pickerTitle.setText(text);
    }

    /**
     * 填充数据
     * @param dataList List
     * @param i 其中 1代表左边的picker 2代表中间的picker 3代表右边的picker
     */
    public void setData(List<String> dataList, int i) {
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
     * 设置其中文本
     * @param position 在数据中的位置
     * @param i 其中 1代表左边的picker 2代表中间的picker 3代表右边的picker
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
     * 获取选择文本
     * @param i 其中 1代表左边的picker 2代表中间的picker 3代表右边的picker
     * @return String
     */
    public String getText(int i) {
        String text = null;
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
     * 准备就绪
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

    public Button getOK() {
        return OK;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
