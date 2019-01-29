package com.zm.scanner;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zm.scanner.util.ArmsUtils;
import com.zm.scanner.widget.NumZXingView;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import io.reactivex.functions.Consumer;

public class TestActivity extends AppCompatActivity implements QRCodeView.Delegate, View.OnClickListener {

    private NumZXingView mQRCodeView;
    private EditText etExpressNoData;
    private EditText etPhoneNumberData;
    private TextView tvExpressNo;
    private TextView tvPhoneNumber;
    private TextView tvLight;
    private boolean mIsLight;//是否打开闪光灯

    public static void nav(final AppCompatActivity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            Toast.makeText(activity, "扫描二维码需要打开相机的权限", Toast.LENGTH_SHORT).show();
                        } else {
                            activity.startActivity(new Intent(activity, TestActivity.class));

                        }

                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        BGAQRCodeUtil.setDebug(true);
        initView();
        initClick();
        selectExpressNo();

    }

    /**
     * 初始化数据
     */
    private void initView() {
        mQRCodeView = findViewById(R.id.my_zxing_view);
        etExpressNoData = findViewById(R.id.et_express_no_data);
        etPhoneNumberData = findViewById(R.id.et_phone_number_data);
        tvExpressNo = findViewById(R.id.tv_express_no);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvLight = findViewById(R.id.tv_light);
        addTextChangedListener();
    }

    /**
     * 初始化点击监听
     */
    private void initClick() {
        tvLight.setOnClickListener(this);
        tvExpressNo.setOnClickListener(this);
        tvPhoneNumber.setOnClickListener(this);
        mQRCodeView.setDelegate(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.startSpotAndShowRect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mQRCodeView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (mQRCodeView.isScannerPhone()) {
            etPhoneNumberData.setText(result);
            etPhoneNumberData.requestFocus();
            if (!TextUtils.isEmpty(etPhoneNumberData.getText().toString())) {
                etPhoneNumberData.setSelection(etPhoneNumberData.getText().toString().length());
            }
        } else {
            etExpressNoData.setText(result);
            etExpressNoData.requestFocus();
            if (!TextUtils.isEmpty(etExpressNoData.getText().toString())) {
                etExpressNoData.setSelection(etExpressNoData.getText().toString().length());
            }
        }
        //扫码成功关闭摄像头
        mQRCodeView.stopCamera();
        mQRCodeView.closeFlashlight(); // 关闭闪光灯
        tvLight.setTextColor(Color.WHITE);
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }


    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_light:
                mIsLight = !mIsLight;
                if (mIsLight) {
                    mQRCodeView.openFlashlight(); // 打开闪光灯
                    tvLight.setTextColor(getResources().getColor(R.color.color_blue));
                } else {
                    mQRCodeView.closeFlashlight(); // 关闭闪光灯
                    tvLight.setTextColor(getResources().getColor(R.color.color_808080));
                }
                break;
            case R.id.tv_express_no:
                selectExpressNo();

                break;
            case R.id.tv_phone_number:
                selectPhoneNumber();

                break;
        }
    }

    /**
     * 选中扫描
     */
    private void selectExpressNo() {
        mQRCodeView.getScanBoxView().setTopOffset(ArmsUtils.getDimens(this, R.dimen.scan_express_top));
        mQRCodeView.getScanBoxView().setBarcodeRectHeight(ArmsUtils.getDimens(this, R.dimen.scan_express_height));
        mQRCodeView.getScanBoxView().setCornerLength(ArmsUtils.getDimens(this, R.dimen.scan_corner_length_express_no));
        mQRCodeView.getScanBoxView().setRectWidth(ArmsUtils.getScreenWidth(this) - ArmsUtils.getDimens(this, R.dimen.scan_express_left_right));
        mQRCodeView.getScanBoxView().setOnlyDecodeScanBoxArea(true);
        mQRCodeView.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
        mQRCodeView.setScannerPhone(false);
        tvExpressNo.setTextColor(getResources().getColor(R.color.color_blue));
        tvPhoneNumber.setTextColor(getResources().getColor(R.color.color_808080));
        etExpressNoData.requestFocus();
        if (!TextUtils.isEmpty(etExpressNoData.getText().toString())) {
            etExpressNoData.setSelection(etExpressNoData.getText().toString().length());
        }
        mQRCodeView.startSpotAndShowRect();
    }

    /**
     * 选中扫描手机号码
     */
    private void selectPhoneNumber() {
        mQRCodeView.getScanBoxView().setTopOffset(ArmsUtils.getDimens(this, R.dimen.scan_phone_top));
        mQRCodeView.getScanBoxView().setBarcodeRectHeight(ArmsUtils.getDimens(this, R.dimen.scan_phone_height));
        mQRCodeView.getScanBoxView().setRectWidth(ArmsUtils.getScreenWidth(this) - ArmsUtils.getDimens(this, R.dimen.scan_phone_left_right));
        mQRCodeView.getScanBoxView().setOnlyDecodeScanBoxArea(true);
        mQRCodeView.setType(BarcodeType.HIGH_FREQUENCY, null);
        mQRCodeView.getScanBoxView().setCornerLength(ArmsUtils.getDimens(this, R.dimen.scan_corner_length_phone));
        mQRCodeView.setScannerPhone(true);
        tvPhoneNumber.setTextColor(getResources().getColor(R.color.color_blue));
        tvExpressNo.setTextColor(getResources().getColor(R.color.color_808080));
        etPhoneNumberData.requestFocus();
        if (!TextUtils.isEmpty(etPhoneNumberData.getText().toString())) {
            etPhoneNumberData.setSelection(etPhoneNumberData.getText().toString().length());
        }
        mQRCodeView.startSpotAndShowRect();
    }


    /**
     * 添加清除监听
     */
    private void addTextChangedListener() {
        etPhoneNumberData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(etPhoneNumberData.getText().toString()) && mQRCodeView.isScannerPhone()) {
                    mQRCodeView.startSpotAndShowRect();
                }
            }
        });
        etExpressNoData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(etExpressNoData.getText().toString()) && !mQRCodeView.isScannerPhone()) {
                    mQRCodeView.startSpotAndShowRect();
                }

            }
        });
    }

}
