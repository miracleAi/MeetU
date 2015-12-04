package com.meetu.tools;

import java.lang.ref.SoftReference;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * @author lucifer
 * @date 2015-11-13
 * @return
 */
public class BitmapChange {
	public static Bitmap zoomImg(Bitmap bm, int newWidth) {
		// 获得图片的宽高
		SoftReference<Bitmap> sr = new SoftReference<Bitmap>(bm);
		int width = sr.get().getWidth();
		int height = sr.get().getHeight();
		int newHeight = (int) (height * (Float.parseFloat(newWidth + "") / Float
				.parseFloat(width + "")));
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(sr.get(), 0, 0, width, height,
				matrix, true);
		SoftReference<Bitmap> srn = new SoftReference<Bitmap>(newbm);
		return srn.get();
	}

}
