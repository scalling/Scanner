package com.zm.scanner.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.zm.scanner.MyApplication;
import com.zm.scanner.util.ProjectPlanarYUVLuminanceSource;
import com.zm.scanner.tess.TessEngine;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.core.ScanResult;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * @author : zengmei
 * @version : v
 * @date : 2018/11/30
 * @description : 描述.....
 */
public class PhoneZXingView extends ZXingView {

    private boolean isScannerPhone = true;//是否扫描识别手机号码 true 识别手机号码 false 识别其他

    public PhoneZXingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PhoneZXingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                String phone = TessEngine.Generate(MyApplication.sAppContext).detectText(bitmap);
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
}
