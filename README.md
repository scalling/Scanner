# Scanner 扫一扫，扫描手机号码
#### 是在[BGAQRCode-Android](https://github.com/bingoogolapple/BGAQRCode-Android)的功能上增加实现的识别手机号码,是基于Tesseract-OCR实现的数字自动识别

## 使用
```
     <com.zm.scanner.view.PhoneZXingView
            android:id="@+id/my_zxing_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:qrcv_animTime="1000"
            app:qrcv_barcodeRectHeight="@dimen/scan_express_height"
            app:qrcv_borderColor="@android:color/white"
            app:qrcv_borderSize="1dp"
            app:qrcv_cornerColor="@android:color/white"
            app:qrcv_cornerLength="20dp"
            app:qrcv_cornerSize="2dp"
            app:qrcv_isBarcode="true"
            app:qrcv_isOnlyDecodeScanBoxArea="true"
            app:qrcv_isScanLineReverse="true"
            app:qrcv_isShowDefaultGridScanLineDrawable="false"
            app:qrcv_isShowDefaultScanLineDrawable="true"
            app:qrcv_maskColor="#50000000"
            app:qrcv_rectWidth="@dimen/scan_express_width"
            app:qrcv_scanLineColor="@color/colorPrimaryDark"
            app:qrcv_scanLineMargin="0dp"
            app:qrcv_scanLineSize="0.5dp"
            app:qrcv_topOffset="@dimen/scan_express_height" />
 ```          
 ## 方法
 ```
     setScannerPhone true扫描手机号码 false 扫描条形码、二维码
```
## 需要权限
```
    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.VIBRATE"/>

    <uses-feature
        android:name="android.hardware.camera"
        android:required="false"/>

    <uses-feature
        android:name="android.hardware.camera.autofocus"
        android:required="false"/>

    <uses-feature
        android:name="android.hardware.camera.flash"
        android:required="false"/>
```
## 图片展示
 <img src="https://github.com/scalling/Scanner/blob/master/screenshot/Screenshot_1.jpg" width = "250" align="left"/>
 <img src="https://github.com/scalling/Scanner/blob/master/screenshot/Screenshot_2.jpg" width = "250" align="left"/>
 <img src="https://github.com/scalling/Scanner/blob/master/screenshot/Screenshot_3.jpg" width = "250" />
 

## 具体用法请查看DEMO
>[MainActivity](https://github.com/scalling/Scanner/blob/master/app/src/main/java/com/zm/scanner/MainActivity.java)

##Thanks For

* [BGAQRCode-Android](https://github.com/bingoogolapple/BGAQRCode-Android)
* [tess-two](https://github.com/rmtheis/tess-two)

## 参考

* [Tesseract-OCR-Scanne](https://github.com/simplezhli/Tesseract-OCR-Scanner)

