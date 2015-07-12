package com.tutorial.glsltutorials.tutorials.Textures;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by jamie on 7/11/15.
 */
public class SampleTextures {
    public static Bitmap getBitmapSquares(int row, int col, int color1, int color2)
    {
        int width = 512;
        int height = 512;
        Bitmap bitmap = Bitmap.createBitmap( width, height,Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmap.getWidth(); i++)
        {
            for (int j = 0; j < bitmap.getHeight(); j++)
            {
                if ((((i / row) + (j / col)) % 2) == 0)
                {
                    bitmap.setPixel(i, j, color1);
                }
                else
                {
                    bitmap.setPixel(i, j, color2);
                }
            }
        }
        return bitmap;
    }
}
