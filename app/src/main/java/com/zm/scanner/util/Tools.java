package com.zm.scanner.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {


    private static Pattern pattern = Pattern.compile("(1|861)\\d{10}$*");

    public static String getTelNum(String sParam) {
        if (TextUtils.isEmpty(sParam)) {
            return "";
        }

        Matcher matcher = pattern.matcher(sParam.trim());
        StringBuilder bf = new StringBuilder();
        while (matcher.find()) {
            bf.append(matcher.group()).append("\n");
        }
        int len = bf.length();
        if (len > 0) {
            bf.deleteCharAt(len - 1);
        }
        return bf.toString();
    }
}
