package com.zm.scanner.tess;

import android.content.Context;
import android.graphics.Bitmap;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.zm.scanner.util.Tools;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;


/**
 * Created by Fadi on 6/11/2014.
 */
public class TessNum {
    static final String TAG = "DBG_" + TessNum.class.getName();

    private Context context;

    private TessNum(Context context) {
        this.context = context;
    }

    public static TessNum Generate(Context context) {
        return new TessNum(context);
    }

    public String detectText(Bitmap bitmap) {

        BGAQRCodeUtil.d(TAG, "Initialization of TessBaseApi");
        TessDataNumManager.initTessTrainedData(context);
        TessBaseAPI baseApi = new TessBaseAPI();
        String path = TessDataNumManager.getTesseractFolder();
        BGAQRCodeUtil.d(TAG, "Tess folder: " + path);
        //初始化OCR的字体数据，path，ENGLISH_LANGUAGE指明要用的字体库（不用加后缀）
        baseApi.setDebug(true);
        baseApi.init(path, "num");
        //设置识别模式
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
        //设置要识别的图片
        baseApi.setImage(bitmap);
        //开始识别
        String result = baseApi.getUTF8Text();
        BGAQRCodeUtil.d(TAG, "Confidence values: " + baseApi.meanConfidence());
        baseApi.clear();
        baseApi.end();
        System.gc();
        BGAQRCodeUtil.e("识别出来的数据：" + result);
        return Tools.isChinaPhoneLegal(result) ? result : null;
    }

}
