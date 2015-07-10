package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Textures.TextureElement;

/**
 * Created by jamie on 7/9/15.
 */
public class Tut_Dither extends TutorialBase {
    TextureElement image1;
    TextureElement image2;

    boolean zero = false;
    boolean one = false;
    boolean finish = false;

    int nocolorTransformProgram;

    protected void init ()
    {
        //Bitmap bitmap1 = new Bitmap(512, 512, System.Drawing.Imaging.PixelFormat.Format4bppIndexed);
        //Bitmap bitmap2 = new Bitmap(512, 512, System.Drawing.Imaging.PixelFormat.Format4bppIndexed);
        nocolorTransformProgram = Programs.addProgram(VertexShaders.MatrixTexture, FragmentShaders.NoColorSwapTexture);
        byte[] bitmap1data = new byte[256 * 512];
        byte[] bitmap2data = new byte[256 * 512];
        for (int i = 0; i < 256; i++)
        {
            for (int j = 0; j < 512; j++)
            {
                bitmap1data[j * 256 + i] = (byte)(4 * (j / 128) + i / 64);
            }
        }
        //FillBitmap(bitmap1, bitmap1data);
        image1 = new TextureElement(R.drawable.colors);
        image2 = new TextureElement(R.drawable.water1);
        image1.setProgram(nocolorTransformProgram);
        image2.setProgram(nocolorTransformProgram);
        setupDepthAndCull();
        Textures.enableTextures();
    }

    /*
    private void FillBitmap(Bitmap bitmap, byte[] rgbValues)
    {
        Rectangle rect = new Rectangle(0, 0, bitmap.Width, bitmap.Height);
        System.Drawing.Imaging.BitmapData bmpData =
                bitmap.LockBits(rect, System.Drawing.Imaging.ImageLockMode.ReadWrite,
                        bitmap.PixelFormat);

        IntPtr ptr = bmpData.Scan0;
        int bytes  = Math.Abs(bmpData.Stride) * bitmap.Height;
        System.Runtime.InteropServices.Marshal.Copy(rgbValues, 0, ptr, bytes);
        bitmap.UnlockBits(bmpData);
    }
*/
    int count = 0;

    public void display()
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if (zero) count = 0;
        if (one) count = 1;
        if (count == 0)
        {
            count = 1;
            image1.draw();
        }
        else
        {
            count = 0;
            image2.draw();
        }
        if (finish) GLES20.glFinish();
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case  KeyEvent.KEYCODE_0:
            {
                zero = true;
                break;
            }
            case  KeyEvent.KEYCODE_1:
            {
                one = true;
                break;
            }
            case  KeyEvent.KEYCODE_2:
            {
                zero = false;
                one = false;
                break;
            }
            case  KeyEvent.KEYCODE_F:
            {
                finish = !finish;
                break;
            }
        }
        return result.toString();
    }

    public void touchEvent(int x_position, int y_position) throws Exception {
        int selectionX = x_position / (width / 7);
        int selectoinY = y_position / (height / 4);
        switch (selectoinY) {
            case 0:
                zero = true;
                break;
            case 1:
                one = true;
                break;
            case 2:
                zero = false;
                one = false;
                break;
            case 3:
                finish = !finish;
                break;
        }
    }
}
