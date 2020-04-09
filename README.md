# HtmlTextView
> 这里是最新版本  

![](https://www.jitpack.io/v/ljwx/HtmlTextView.svg)
#### 初衷
> 因为项目中经常出现这种情况, 完整的一句话, 中间一些特殊需要注意的地方常常大小和颜色都不一样, 虽然用HTML类可以处理这类问题, 但是处理起来可能没有这个方便, 并且无法预览, 由于大小不一样, 加上如果是中英文数字混合显示, 可能底部看起来不在一条线, UI审核不容易通过, 因此制作这个控件, 希望能解决一些问题, 现已支持自定义字体,左右两边设置图片,并自定义大小.

![avatar](https://github.com/ljwx/Image/blob/master/htmltextview.png)
#### 属性介绍
```xml
<com.ljwx.view.HtmlTextView
	android:layout_width="wrap_content"
	android:layout_height="wrap_content"
	android:layout_marginTop="10dp"
	app:htvCenterBold="false"
	app:htvCenterColor="#00ff00"
	app:htvCenterMarginLeft="5dp"
	app:htvCenterMarginRight="20dp"
	app:htvCenterSize="14dp"
	app:htvCenterString="中文"
	app:htvLeftBold="false"
	app:htvLeftColor="#ff0000"
	app:htvLeftSize="12dp"
	app:htvLeftString="abc"
	app:htvRightBold="false"
	app:htvRightColor="#0000ff"
	app:htvRightSize="19dp"
	app:htvRightString="123(设置中间文字左右间距)" />
```
|   特殊属性  | 介绍 |
| --------:| :--: |
|htvLeftBold	|是否需要加粗|
| htvCenterMarginL  | 中间文字左边间距 |
| htvCenterMarginR  | 中间文字右边间距 |
| htvGravityType | 三段文字紧贴,或者均分在控件宽度的左中右 |
| htvCenterOffset   | 三段文字平分时,中间文字起始位置,距中点的偏移值 |
| htvBaseLineType| 基准线是控件底部,还是控件高度的中间位置 |
| htvLeftMarginBottom| 当底部无法对齐时,这个值可以设置底部偏移 |
|htvDrawLeftWidth|设置drawable图片的宽高|
|htvAutoSize|是否自适应字体大小,默认false|
|htvAutoSizeRatio|缩小比率,默认0.01|  

> 设置drawable资源图片方式还是原来一样android:drawableLeft="", 只是在检测到设置了htvDrawbaleleft或者htvDrawableRight宽高属性时,会修改drawable本来的大小, drawableTop和Bottom使用频率比较低,加入之后逻辑复杂很多, 我就没有处理，暂时只处理了left和right的drawable。  

> 在IDE的布局文件里, 可以通过app:htv查看提示所有属性, 有些属性我没有全部提及, 不过通过属性名及尝试, 可以大概知道意思
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
	implementation 'com.github.ljwx:HtmlTextView:Tag' //tag替换为JitPack标签后面的数字即可
}
  ```
