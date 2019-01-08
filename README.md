# HtmlTextView
#### 初衷
因为项目中经常出现这种情况, 完整的一句话, 中间一些特殊需要注意的地方常常大小和颜色都不一样, 虽然用HTML类可以处理这类问题, 但是处理起来可能没有这个方便, 并且无法预览, 由于大小不一样, 加上如果是中英文数字混合显示, 可能底部看起来不在一条线, UI审核不容易通过, 因此制作这个控件, 希望能解决一些问题, 这是我的第一个开源项目, 我会用到我稍后的项目中, 可能会有很多问题, 欢迎提出来, 我会努力修复.

![avatar](https://github.com/ljwx/Image/blob/master/htmltextview.png)
#### 属性介绍
```xml
<com.ljwx.view.HtmlTextView
 android:layout_width="wrap_content"
 android:layout_height="wrap_content"
 android:layout_marginTop="10dp"
 app:htvBaseLineMode="mid"
 app:htvTv1Bold="false"
 app:htvTv1Color="#ff0000"
 app:htvTv1Size="12dp"
 app:htvTv1String="abc"
 app:htvTv2Bold="true"
 app:htvTv2Color="#00ff00"
 app:htvTv2Size="18dp"
 app:htvTv2String="中文"
 app:htvTv3Bold="false"
 app:htvTv3Color="#0000ff"
 app:htvTv3Size="25dp"
 app:htvTv3String="123(控件中间线对齐)" />
```
|   特殊属性  | 介绍 |
| --------:| :--: |
| htvTv2MarginL  | 中间文字左边间距 |
| htvTv2MarginR  | 中间文字右边间距 |
| htvGravityMode | 三段文字紧挨或者平分 |
| htvTv2Offset   | 三段文字平分时,中间文字起始位置距中点的偏移值 |
| htvBaseLineMode| 基准线是底部还是高度的中点 |
| htvTv1MarginBottom| 当底部无法对齐时,这个值可以设置底部偏移 |
### 引用方式
在根目录的build里添加
```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
  ```
  在需要引用的module的build里添加
  ```java
  dependencies {
	implementation 'com.github.ljwx:HtmlTextView:1.1.0'
}
  ```
