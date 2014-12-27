package com.tutorial.glsltutorials.tutorials.Textures;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.Random;

/**
 * Created by jamie on 12/25/14.
 */
public class PaintWall
{
    Bitmap bitmap;
    int width = 256;
    int height = 256;
    int clearWidth = 120;
    TextureElement textureElement;
    Random random;
    public PaintWall ()
    {
        bitmap=Bitmap.createBitmap( width, height,Bitmap.Config.ARGB_8888);
        for (int col = 0; col < width; col++)
        {
            for (int row = 0; row < height; row++)
            {
                if ((Math.abs(row - 128) < clearWidth) & (Math.abs(col - 128) < clearWidth))
                {
                    // clear pixels bitmap.SetPixel(col, row, Color.White);
                }
                else
                {
                    bitmap.setPixel(col, row, Color.BLUE);
                }
            }
        }
        textureElement = new TextureElement(bitmap);
        random = new Random();
    }

    private void Paint(int colStart, int rowStart, int size, int color)
    {
        int distanceFromCenter;
        int sizeSquared = (int)Math.pow(size, 2);
        int colCenter = colStart + size/2;
        int rowCenter = rowStart + size/2;
        if (colStart < 0) colStart = 0;
        if (rowStart < 0) rowStart = 0;
        if (colStart + size > width) colStart = width - size;
        if (rowStart + size > height) rowStart = height - size;
        for (int col = colStart; col < colStart + size; col++)
        {
            for (int row = rowStart; row < rowStart + size; row++)
            {
                distanceFromCenter = (int)(Math.pow((col - colCenter),2) + Math.pow((row - rowCenter),2));
                if (distanceFromCenter < sizeSquared/4)
                {
                    if ((random.nextDouble() * distanceFromCenter) < size)
                    {
                        bitmap.setPixel(col, row, color);
                    }
                }
            }
        }
        textureElement.replace(bitmap);
    }

    public void PaintRandom()
    {
        int colStart = random.nextInt(width - 10);
        int rowStart = random.nextInt(height - 10);
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        int color = Color.argb(255, red, green, blue);
        Paint(colStart, rowStart, 9, color);
    }

    public void Paint(float x, float y)
    {
        int colStart = (int)(width/2 + x * width/2);
        int rowStart = (int)(height/2 + y * width/2);
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        int color = Color.argb(255, red, green, blue);
        Paint(colStart, rowStart, 50, color);
    }

    public void move(float x, float y, float z)
    {
        textureElement.move(new Vector3f(x, y, z));
    }

    public void move(Vector3f movement)
    {
        textureElement.move(movement);
    }

    public Vector3f getOffset()
    {
        return textureElement.getOffset();
    }

    public void scale(float scale)
    {
        textureElement.scale(scale);
    }

    public void rotateShape(Vector3f axis, float angle)
    {
        textureElement.rotateShape(axis, angle);
    }

    public void draw()
    {
        textureElement.draw();
    }
}

