# 滑轮选择器

# 实现原理

* 首先自定义了一个<font color=#FF0000>PickerView</font>继承android原生的<font color=#FF0000>View</font>
* 实现滑动点击监听事件，通过一系列触摸判断实现滑动效果
* 其次<font color=#FF0000>TextPicker</font>继承<font color=#FF0000>PopupWindow</font>装载这个<font color=#FF0000>PickerView</font>实现展示

## PickerView的实现

其中主要的实现方法是<font color=#FF0000>onDrawView()</font>与<font color=#FF0000>drawOtherView</font>绘制滑轮选择器的文本显示,然后就是触摸事件<font color=#FF0000>onTouchEvent()</font>的事件处理。

### onDrawView的想法

滑轮的文本效果是要中间的文本显示正常而上面与下面的文本要相应的缩小字体大小达到远离的感觉，所用首先绘制好中间的文本，再以中间的文本为中心绘制上下的文本.

然后就是触摸滑动效果，通过<font color=#FF0000>doDown()</font>,<font color=#FF0000>doMove()</font>与<font color=#FF0000>doUp()</font>来操作

## TextPicker
对于xml中<font color=#FF0000>PickerView</font>进行了封装，因为可以项目中要用到多次，所用写个封装类还是必须的
### 对外方法
* setPickerTitle(String text)	设置标题
* setData(List<String> dataList, int i) 填充数据
* setMiddleText(int position, int i) 设置居中文本
* getText(int i) 获取文本
* getOK() 获取确定控件
* setPrepare() 开启

## 实现
最后就是实例化中<font color=#FF0000>TextPicker</font>调用其方法实现

```
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
        ok.setOnClickListener(new View.OnClickListener(){
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
```

## 效果图
![image](https://github.com/idisfkj/idisfkj.picker/raw/master/image/pickerView.png)

个人博客：[http://idisfkj.github.io/](http://idisfkj.github.io/)