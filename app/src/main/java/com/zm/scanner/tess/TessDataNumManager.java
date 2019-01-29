package com.zm.scanner.tess;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;


/**
 * Created by Fadi on 6/11/2014.
 */
public class TessDataNumManager {

    static final String TAG = "DBG_" + TessDataNumManager.class.getName();

    private static final String tessdir = "tesseract";
    private static final String subdir = "tessdata";
    private static final String filename = "num.traineddata";

    private static String trainedDataPath;

    private static String tesseractFolder;

    public static String getTesseractFolder() {
        return tesseractFolder;
    }

    public static String getTrainedDataPath(){
        return initiated ? trainedDataPath : null;
    }

    private static boolean initiated;

    public static void initTessTrainedData(Context context){

        if(initiated){
            return;
        }

        File appFolder = context.getFilesDir();
        File folder = new File(appFolder, tessdir);
        if(!folder.exists()){
            folder.mkdir();
        }
            
        tesseractFolder = folder.getAbsolutePath();

        File subfolder = new File(folder, subdir);
        if(!subfolder.exists()){
            subfolder.mkdir();
        }

        File file = new File(subfolder, filename);
        trainedDataPath = file.getAbsolutePath();
        BGAQRCodeUtil.d(TAG, "Trained data filepath: " + trainedDataPath);

        if(!file.exists()) {

            try {
                FileOutputStream fileOutputStream;
                byte[] bytes = readRawTrainingData(context);
                if (bytes == null){
                    return;
                }
                    
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes);
                fileOutputStream.close();
                initiated = true;
                BGAQRCodeUtil.e("Prepared training data file");
            } catch (FileNotFoundException e) {
                BGAQRCodeUtil.e( "Error opening training data file\n" + e.getMessage());
            } catch (IOException e) {
                BGAQRCodeUtil.e( "Error opening training data file\n" + e.getMessage());
            }
        }
        else{
            initiated = true;
        }
    }

    private static byte[] readRawTrainingData(Context context){

        try {
            InputStream fileInputStream=context.getClass().getResourceAsStream("/assets/num.traineddata");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int bytesRead;

            while (( bytesRead = fileInputStream.read(b))!=-1){
                bos.write(b, 0, bytesRead);
            }

            fileInputStream.close();

            return bos.toByteArray();

        } catch (FileNotFoundException e) {
            BGAQRCodeUtil.e("Error reading raw training data file\n"+e.getMessage());
            return null;
        } catch (IOException e) {
            BGAQRCodeUtil.e( "Error reading raw training data file\n" + e.getMessage());
        }

        return null;
    }

}
