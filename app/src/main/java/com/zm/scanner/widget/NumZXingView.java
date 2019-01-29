package com.zm.scanner.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.zm.scanner.MyApplication;
import com.zm.scanner.tess.TessEngine;
import com.zm.scanner.tess.TessNum;
import com.zm.scanner.translator.PhoneNumberTranslator;
import com.zm.scanner.util.ProjectPlanarYUVLuminanceSource;

import java.io.ByteArrayOutputStream;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.core.ScanResult;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * @author : zengmei
 * @version : v
 * @date : 2018/11/30
 * @description : 描述.....
 */
public class NumZXingView extends ZXingView {
    private ImageView imageView,imageView1;
    private PhoneNumberTranslator translator;


    private boolean isScannerPhone = true;//是否扫描识别手机号码 true 识别手机号码 false 识别其他


    public NumZXingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NumZXingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageView(ImageView imageView,ImageView imageView1) {
        this.imageView = imageView;
        this.imageView1 = imageView1;
    }

    public void setScannerPhone(boolean scannerPhone) {
        isScannerPhone = scannerPhone;
    }

    public boolean isScannerPhone() {
        return isScannerPhone;
    }

    @Override
    protected ScanResult processBitmapData(Bitmap bitmap) {
        if (isScannerPhone) {
            return new ScanResult(TessEngine.Generate(MyApplication.sAppContext).detectText(bitmap));
        } else {
            return super.processBitmapData(bitmap);
        }

    }

    @Override
    protected ScanResult processData(byte[] data, int width, int height, boolean isRetry) {
        if (isScannerPhone) {
            BGAQRCodeUtil.d("正在解析---------------------------");
            String result = null;
            try {
                ProjectPlanarYUVLuminanceSource source;
                Rect scanBoxAreaRect = mScanBoxView.getScanBoxAreaRect(height);
                if (scanBoxAreaRect != null) {
                    source = new ProjectPlanarYUVLuminanceSource(data, width, height, scanBoxAreaRect.left, scanBoxAreaRect.top, scanBoxAreaRect.width(),
                            scanBoxAreaRect.height(), false);
                } else {
                    source = new ProjectPlanarYUVLuminanceSource(data, width, height, 0, 0, width, height, false);
                }
                Bitmap bitmap = source.renderCroppedGreyscaleBitmap();
                if(bitmap!=null){
                    showImage(source.renderCroppedGreyscaleBitmap());
                }
                if (translator == null) {
                    translator = new PhoneNumberTranslator(imageView1);
                }
                String phone = TessNum.Generate(MyApplication.sAppContext).detectText(translator, bitmap);
                if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
                    result = phone;
                    BGAQRCodeUtil.d("解析成功：手机号码" + result);
                } else {
                    BGAQRCodeUtil.d("解析成功，不是手机号码：" + result);
                }

            } catch (Exception e) {
                e.printStackTrace();
                BGAQRCodeUtil.d("解析失败：" + e.getMessage());
            }
            BGAQRCodeUtil.d("解析结束---------------------------");

            return new ScanResult(result);
        } else {
            return super.processData(data, width, height, isRetry);
        }

    }
    private void showImage(final Bitmap bmp) {
        final Bitmap bitmap = bmp;
        //将裁切的图片显示出来（测试用，需要为CameraView  setTag（ImageView））
        imageView.post(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
            }
        });
    }
}
