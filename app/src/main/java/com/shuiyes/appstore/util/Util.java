package com.shuiyes.appstore.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.math.BigDecimal;
import java.util.Objects;

public class Util {

    /**
     * 格式化文件大小
     *
     * @param size
     * @return
     */
    public static String getFormatDownload(double size) {
        double wByte = size / 10000;
        if (wByte < 1) {
            return size + "";
        }

        double yByte = wByte / 10000;
        if (yByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(wByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "万";
        }

        BigDecimal result = new BigDecimal(Double.toString(yByte));
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "亿";
    }

    /**
     * 格式化文件大小
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }

        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }

    public static void toggleSoftInput(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSoftInputFromWindow(Activity activity, EditText et) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(et, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftInputFromWindow(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && imm.isActive()) {
                imm.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertToQuotedString(String string) {
        return "\"" + string + "\"";
    }

    public static String getBundleExtras(Intent intent) {
        android.os.Bundle bundle = intent.getExtras();
        StringBuffer buf = new StringBuffer();
        if (bundle != null) {
            java.util.Set<String> keys = bundle.keySet();
            java.util.Iterator<String> iterator = keys.iterator();
            buf.append("Bundle[");
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = bundle.get(key).toString();
                buf.append(key + "=" + value);
                if (iterator.hasNext()) {
                    buf.append(", ");
                }
            }
            buf.append("]");
        }
        return buf.toString();
    }

}