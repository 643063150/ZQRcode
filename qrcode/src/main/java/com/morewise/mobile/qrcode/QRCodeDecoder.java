package com.morewise.mobile.qrcode;

import android.graphics.Bitmap;


import com.king.wechat.qrcode.WeChatQRCodeDetector;
import com.morewise.mobile.qrcode.qrcodecore.BGAQRCodeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/4/8 下午11:22
 * 描述:解析二维码图片。一维条码、二维码各种类型简介 https://blog.csdn.net/xdg_blog/article/details/52932707
 */
public class QRCodeDecoder {

    private QRCodeDecoder() {
    }

    /**
     * 同步解析本地图片二维码。该方法是耗时操作，请在子线程中调用。
     *
     * @param picturePath 要解析的二维码图片本地路径
     * @return 返回二维码图片里的内容 或 null
     */
    public static String syncDecodeQRCode(String picturePath) {
        return syncDecodeQRCode(BGAQRCodeUtil.getDecodeAbleBitmap(picturePath));
    }

    /**
     * 同步解析bitmap二维码。该方法是耗时操作，请在子线程中调用。
     *
     * @param bitmap 要解析的二维码图片
     * @return 返回二维码图片里的内容 或 null
     */
    public static String syncDecodeQRCode(Bitmap bitmap) {
        List<String> result = new ArrayList<>();
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            result = WeChatQRCodeDetector.detectAndDecode(bitmap);
            return result.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            if (result.size() == 0) {
                BGAQRCodeUtil.d("扫描", "扫描失败");
            }
            return null;
        }
    }
}