package com.morewise.mobile.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.king.wechat.qrcode.WeChatQRCodeDetector;
import com.morewise.mobile.qrcode.qrcodecore.BGAQRCodeUtil;
import com.morewise.mobile.qrcode.qrcodecore.BarcodeType;
import com.morewise.mobile.qrcode.qrcodecore.QRCodeView;
import com.morewise.mobile.qrcode.qrcodecore.ScanResult;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class ZXingView extends QRCodeView {


    public ZXingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZXingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setupReader() {

    }

    /**
     * 设置识别的格式
     *
     * @param barcodeType 识别的格式
     * @param      barcodeType 为 BarcodeType.CUSTOM 时，必须指定该值
     */
    public void setType(BarcodeType barcodeType,String s) {

        setupReader();
    }

    @Override
    protected ScanResult processBitmapData(Bitmap bitmap) {
        return new ScanResult(QRCodeDecoder.syncDecodeQRCode(bitmap));
    }

    @Override
    protected ScanResult processData(byte[] data, int width, int height, boolean isRetry) {
        List<String> rawResult = null;
        Rect scanBoxAreaRect = null;
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                width,
                height,
                null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, width, height), 100, baos);// 80--JPG图片的质量[0-100],100最高
        byte[]  rawImage = baos.toByteArray();
        //将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
        try {
            scanBoxAreaRect = mScanBoxView.getScanBoxAreaRect(height);


            rawResult = WeChatQRCodeDetector.detectAndDecode(bitmap);
            if (rawResult == null) {
                    BGAQRCodeUtil.d("GlobalHistogramBinarizer 没识别到，HybridBinarizer 能识别到");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rawResult == null) {
            return null;
        }

        String result = rawResult.get(0);
        if (TextUtils.isEmpty(result)) {
            return null;
        }

        // 处理自动缩放和定位点
//        boolean isNeedAutoZoom = isNeedAutoZoom(barcodeFormat);
//        if (isShowLocationPoint() || isNeedAutoZoom) {
//            ResultPoint[] resultPoints = rawResult.getResultPoints();
//            final PointF[] pointArr = new PointF[resultPoints.length];
//            int pointIndex = 0;
//            for (ResultPoint resultPoint : resultPoints) {
//                pointArr[pointIndex] = new PointF(resultPoint.getX(), resultPoint.getY());
//                pointIndex++;
//            }

//            if (transformToViewCoordinates(pointArr, scanBoxAreaRect, isNeedAutoZoom, result)) {
//                return null;
//            }
//        }
        return new ScanResult(result);
    }

//    private boolean isNeedAutoZoom(BarcodeFormat barcodeFormat) {
//        return isAutoZoom() && barcodeFormat == BarcodeFormat.QR_CODE;
//    }
}