package com.ryeeeeee.faceandflacdemo.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

/**
 * Created by Ryeeeeee on 1/12/16.
 */
public class ImageUtil {

    public static Bitmap decodeSampledBitmapFromPath(String imagePath, int requestWidth, int requestHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath);

        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        int inSampleSize = 1;

        if (imageWidth > requestWidth || imageHeight > requestHeight) {
            int halfWidth = imageWidth / 2;
            int halfHeight = imageHeight / 2;

            while ((halfWidth / inSampleSize) > requestWidth && (halfHeight / inSampleSize) > requestHeight) {
                inSampleSize *= 2;
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(imagePath, options);
    }
}
