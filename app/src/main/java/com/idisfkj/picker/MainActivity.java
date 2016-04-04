package com.idisfkj.picker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import com.idisfkj.mypicker.MyPicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.bt)
    Button bt;
    @InjectView(R.id.tv)
    TextView tv;
    private List<String> leftList;
    private List<String> middleList;
    private List<String> rightList;

    private MyPicker tp;

    private String leftText;
    private String middleText;
    private String rightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    public void initData() {
        leftList = new ArrayList<>();
        middleList = new ArrayList<>();
        rightList = new ArrayList<>();
        for (int i = 10; i <= 16; i++) {
            leftList.add("20" + i + "年");
        }
        for (int i = 1; i <= 12; i++) {
            middleList.add(i + "月");
        }
        for (int i = 1; i <= 31; i++) {
            rightList.add(i + "日");
        }
    }

    @OnClick(R.id.bt)
    public void onClick() {
        tp = new MyPicker(this);
        //init data
        initData();
        //loding data
        tp.setData(leftList, 1);
        tp.setData(middleList, 2);
        tp.setData(rightList, 3);
        //set title
        tp.setPickerTitle(getResources().getString(R.string.title_name));
        //set the default centered text
        //if not set,show centered text in the data
        tp.setMiddleText(5, 1);
        tp.setMiddleText(2, 2);
        tp.setMiddleText(25, 3);
        //redy
        tp.setPrepare();

        // default show three
//        tp.setShowNum(3);

        tp.setSelectedFinishListener(new MyPicker.SelectedFinishListener() {
            @Override
            public void onFinish() {
                leftText = String.valueOf(tp.getText(1));
                middleText = String.valueOf(tp.getText(2));
                rightText = String.valueOf(tp.getText(3));
                tv.setText(leftText+"-"+middleText+"-"+rightText);
                tp.dismiss();
            }
        });
        tp.showAtLocation(this.findViewById(R.id.main), Gravity.CENTER, 0, 0);
    }
}
