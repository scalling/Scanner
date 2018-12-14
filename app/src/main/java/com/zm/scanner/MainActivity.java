package com.zm.scanner;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zm.scanner.view.PhoneZXingView;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements QRCodeView.Delegate, View.OnClickListener {

    PhoneZXingView mQRCodeView;
    EditText etExpressNoData;
    EditText etPhoneNumberData;
    TextView tvExpressNo;
    TextView tvPhoneNumber;
    TextView tvLight;
    boolean isLight = false;

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
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();

                        }

                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BGAQRCodeUtil.setDebug(true);
        initView();
        initClick();
        selectExpressNo();

    }

    private void initView() {
        mQRCodeView = findViewById(R.id.my_zxing_view);
        etExpressNoData = findViewById(R.id.et_express_no_data);
        etPhoneNumberData = findViewById(R.id.et_phone_number_data);
        tvExpressNo = findViewById(R.id.tv_express_no);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvLight = findViewById(R.id.tv_light);
    }

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
        } else {
            etExpressNoData.setText(result);
        }
        mQRCodeView.startSpotAndShowRect();
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
                isLight = !isLight;
                if (isLight) {
                    mQRCodeView.openFlashlight(); // 打开闪光灯
                    tvLight.setTextColor(Color.BLUE);
                } else {
                    mQRCodeView.closeFlashlight(); // 关闭闪光灯
                    tvLight.setTextColor(Color.WHITE);
                }
                break;
            case R.id.tv_express_no:
                selectExpressNo();
                mQRCodeView.startSpotAndShowRect();
                break;
            case R.id.tv_phone_number:
                selectPhoneNumber();
                mQRCodeView.startSpotAndShowRect();
                break;
        }
    }

    private void selectExpressNo() {
        mQRCodeView.getScanBoxView().setTopOffset(getResources().getDimensionPixelOffset(R.dimen.scan_express_top));
        mQRCodeView.getScanBoxView().setBarcodeRectHeight(getResources().getDimensionPixelOffset(R.dimen.scan_express_height));
        mQRCodeView.getScanBoxView().setOnlyDecodeScanBoxArea(false);
        mQRCodeView.setScannerPhone(false);
        tvExpressNo.setTextColor(Color.BLUE);
        tvPhoneNumber.setTextColor(Color.WHITE);
    }

    private void selectPhoneNumber() {
        mQRCodeView.getScanBoxView().setTopOffset(getResources().getDimensionPixelOffset(R.dimen.scan_phone_top));
        mQRCodeView.getScanBoxView().setBarcodeRectHeight(getResources().getDimensionPixelOffset(R.dimen.scan_phone_height));
        mQRCodeView.getScanBoxView().setOnlyDecodeScanBoxArea(true);
        mQRCodeView.setScannerPhone(true);
        tvPhoneNumber.setTextColor(Color.BLUE);
        tvExpressNo.setTextColor(Color.WHITE);
    }


}
