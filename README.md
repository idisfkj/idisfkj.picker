# Picker
# Integration
Add as a dependency to your build.gradle:

```
dependencies {
    compile 'com.idisfkj.picker:mypicker:1.2.1'
}
```

# Description
The android open source project is a pulley selector, it can better help you implement event selection effect.Below is a rendering:

![image](https://github.com/idisfkj/idisfkj.picker/raw/master/image/pickerView.gif)

## MyPicker
You can instantiate the class to use the controls

```
MyPicker picker = new MyPicker();
```

## Public Method
* setPickerTitle(String text)	set title
* setData(List<T> dataList, int i) Fill in the data
* setMiddleText(int position, int i) set default centered text
* getText(int i) Access to select text
* setPrepare() redy
* setShowNum(int num) default show three picker
* setSelectedFinishListener(SelectedFinishListener listener) set finished listener

## Use Demo

```
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
```
# End
personal blog：[http://idisfkj.github.io](http://idisfkj.github.io)

# License

```
Copyright (c) 2016. The Android Open Source Project
Created by idisfkj
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
