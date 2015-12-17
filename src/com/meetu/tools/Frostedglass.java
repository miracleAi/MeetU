package com.meetu.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogUtil.log;

/** 
 * @author  lucifer 
 * @date 2015-12-17
 * @return  
 */
public class Frostedglass {
	@SuppressLint("NewApi")
	public static Bitmap  frostedGrass(Context context,Bitmap sentBitmap,float radius){
		if (VERSION.SDK_INT > 16) {
			log.e("zcq", "处理毛玻璃效果");
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }
		return sentBitmap;


	}

}
