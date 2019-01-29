package com.zm.scanner.tess;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.zm.scanner.translator.PhoneNumberTranslator;
import com.zm.scanner.translator.Translator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String detectText(PhoneNumberTranslator translator, Bitmap bitmap) {

        TessDataNumManager.initTessTrainedData(context);
        String path = TessDataNumManager.getTesseractFolder();
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(path, translator.initLanguage());
        //设置识别模式
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
        //设置要识别的图片
        baseApi.setImage(translator.catchText(bitmap));
        baseApi.setVariable(TessBaseAPI.VAR_SAVE_BLOB_CHOICES, TessBaseAPI.VAR_TRUE);
        //开始识别
        String result = baseApi.getUTF8Text();
        baseApi.clear();
        baseApi.end();
        bitmap.recycle();
        System.gc();
        BGAQRCodeUtil.e("识别出来的数据：" + result);
        String text = filter(translator, result);
        BGAQRCodeUtil.e("filter的数据：" + result);
        return text;
    }

    /**
     * 筛选扫描结果
     */
    public String filter(Translator translator, String str) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(translator.filterRule()))
            return str;
        Pattern pattern = Pattern.compile(translator.filterRule());
        Matcher matcher = pattern.matcher(str);
        StringBuffer bf = new StringBuffer();
        while (matcher.find()) {
            bf.append(matcher.group()).append(",");
        }
        int len = bf.length();
        if (len > 0) {
            bf.deleteCharAt(len - 1);
        }
        return bf.toString();
    }



}
