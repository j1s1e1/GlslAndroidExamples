package com.tutorial.glsltutorials.tutorials.GLES_Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.tutorial.glsltutorials.tutorials.R;

/**
 * Created by jamie on 11/23/14.
 */
public class Textures {

    public static int loadTexture(final Context context, String fileName) {
        int resource = -1;
        switch (fileName) {
            case "flashlight.png":
                resource = R.drawable.flashlight;
                break;
            case "pointsoflight.png":
                resource = R.drawable.pointsoflight;
                break;
            case "bands.png":
                resource = R.drawable.bands;
                break;
        }
        return loadTexture(context, resource, false);
    }

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

    public static int createFromBitmap(Bitmap bitmap)
    {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            // This does not preserve alpha
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);
        }
        else
        {
            throw new RuntimeException("Error loading texture.");
        }
        return textureHandle[0];
    }

    public static void enableTextures()
    {
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
        GLES20.glBlendFunc(GLES20.GL_BLEND_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_ALPHA);
        //GL.AlphaFunc(AlphaFunction.Gequal, 0.01f);
    }

    public static int createMipMapTexture(final Context context, final int resourceId)
    {
        final int[] mipMapTexture = new int[1];

        GLES20.glGenTextures(1, mipMapTexture, 0);

        if (mipMapTexture[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mipMapTexture[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (mipMapTexture[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }
        return mipMapTexture[0];
    }
}
