package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.graphics.Color;
import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Textures.SampleTextures;
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

    boolean first = true;
    int count = 0;

    int frameCount = 0;
    int ditherCount = 1;
    int squaresize = 16;
    boolean updateTextures = false;
    int color1 = Color.GREEN;
    int color2 = Color.BLUE;
    int colorsCount = 0;

    int nocolorTransformProgram;

    private void SetTextures()
    {
        image1.replace(SampleTextures.getBitmapSquares(squaresize, squaresize, color1, color2));
        image2.replace(SampleTextures.getBitmapSquares(squaresize, squaresize, color2, color1));
    }

    protected void init ()
    {
        nocolorTransformProgram = Programs.addProgram(VertexShaders.MatrixTexture, FragmentShaders.NoColorSwapTexture);
        image1 = new TextureElement(SampleTextures.getBitmapSquares(squaresize, squaresize, color1, color2));
        image2 = new TextureElement(SampleTextures.getBitmapSquares(squaresize, squaresize, color2, color1));
        image1.setProgram(nocolorTransformProgram);
        image2.setProgram(nocolorTransformProgram);
        setupDepthAndCull();
        Textures.enableTextures();
    }

    public void display()
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        frameCount++;
        if (zero)
        {
            first = true;
            frameCount = 0;
        }
        if (one)
        {
            first = false;
            frameCount = 0;
        }
        if (first)
        {
            if (frameCount >= ditherCount)
            {
                frameCount = 0;
                first = false;
            }
            image1.draw();
        }
        else
        {
            if (frameCount >= ditherCount)
            {
                frameCount = 0;
                first = true;
            }
            image2.draw();
        }
        if (finish) GLES20.glFinish();
        if (updateTextures)
        {
            updateTextures = false;
            SetTextures();
        }
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.ACTION_UP:
                ditherCount++;
                break;
            case KeyEvent.ACTION_DOWN:
                if (ditherCount > 1) ditherCount--;
                break;
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
            case KeyEvent.KEYCODE_C:
                colorsCount ++;
                if (colorsCount > 4)
                {
                    colorsCount = 0;
                }
                switch (colorsCount)
                {
                    case 0: color1 = Color.GREEN; color2 = Color.BLUE; break;
                    case 1: color1 = Color.RED; color2 = Color.YELLOW; break;
                    case 2: color1 = Color.BLACK; color2 = Color.WHITE; break;
                    case 3: color1 = Color.CYAN; color2 = Color.MAGENTA; break;
                    case 4: color1 = Color.YELLOW; color2 = Color.BLUE; break;
                }
                updateTextures = true;
                break;
            case KeyEvent.KEYCODE_S:
                squaresize *= 2;
                if (squaresize > 128)
                    squaresize = 1;
                updateTextures = true;
                break;
        }
        return result.toString();
    }

    public void touchEvent(int x_position, int y_position) throws Exception {
        int selectionX = x_position / (width / 7);
        int selectoinY = y_position / (height / 4);
        switch (selectoinY) {
            case 0:
                colorsCount ++;
                if (colorsCount > 4)
                {
                    colorsCount = 0;
                }
                switch (colorsCount)
                {
                    case 0: color1 = Color.GREEN; color2 = Color.BLUE; break;
                    case 1: color1 = Color.RED; color2 = Color.YELLOW; break;
                    case 2: color1 = Color.BLACK; color2 = Color.WHITE; break;
                    case 3: color1 = Color.CYAN; color2 = Color.MAGENTA; break;
                    case 4: color1 = Color.YELLOW; color2 = Color.BLUE; break;
                }
                updateTextures = true;
                break;
            case 1:
                squaresize *= 2;
                if (squaresize > 128)
                    squaresize = 1;
                updateTextures = true;
                break;
            case 2:
                ditherCount++;
                break;
            case 3:
                if (ditherCount > 1) ditherCount--;
                break;
        }
    }
}
