package com.tutorial.glsltutorials.tutorials.GLES_Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by jamie on 11/23/14.
 */
public class Textures {


    public static int loadTexture(final Context context, final int resourceId, boolean oneTwenty)
    {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Add 10% on each side for sphere mapping
            if (oneTwenty)
            {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int extra = width/10;
                int newWidth = width + 2 * extra;
                Bitmap bitmap2 = Bitmap.createBitmap(newWidth, height, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(bitmap2);

                Rect lastTen = new Rect(width - extra, 0, width, height);
                Rect original = new Rect(0, 0, width, height);
                Rect firstTen = new Rect(0, 0, extra, height);

                Rect leftSlice = new Rect(0, 0, extra, height);
                Rect middleSlice = new Rect(extra, 0, extra + width, height);
                Rect rightSlice = new Rect(extra + width, 0, extra + width + extra, height);

                c.drawBitmap(bitmap, lastTen, leftSlice, null);
                c.drawBitmap(bitmap, original, middleSlice, null);
                c.drawBitmap(bitmap, firstTen, rightSlice, null);


                bitmap = bitmap2;
            }

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }
}
