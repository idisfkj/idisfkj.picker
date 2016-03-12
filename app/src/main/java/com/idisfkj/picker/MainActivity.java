package com.idisfkj.picker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.idisfkj.picker.view.TextPicker;

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

    private Button ok;

    private TextPicker tp;

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
        leftList = new ArrayList<String>();
        middleList = new ArrayList<String>();
        rightList = new ArrayList<String>();
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
        tp = new TextPicker(this);
        //初始化数据
        initData();
        //加载数据
        tp.setData(leftList, 1);
        tp.setData(middleList, 2);
        tp.setData(rightList, 3);
        //设置标题
        tp.setPickerTitle("标题");
        //设置默认居中文本
        tp.setMiddleText(5, 1);
        tp.setMiddleText(2, 2);
        tp.setMiddleText(25, 3);
        //准备完毕
        tp.setPrepare();
        ok = tp.getOK();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftText = tp.getText(1);
                middleText = tp.getText(2);
                rightText = tp.getText(3);
                tv.setText(leftText+"-"+middleText+"-"+rightText);
                tp.dismiss();
            }
        });
        //展示
        tp.showAtLocation(this.findViewById(R.id.main), Gravity.CENTER, 0, 0);
    }
}
