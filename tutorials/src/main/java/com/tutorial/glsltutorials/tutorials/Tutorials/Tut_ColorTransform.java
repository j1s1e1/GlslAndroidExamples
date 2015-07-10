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
 * Created by jamie on 7/8/15.
 */
public class Tut_ColorTransform extends TutorialBase {
    TextureElement image;

    float colors[] = new float[]
            {
                    1f, 0f, 0f, 1f,
                    0f, 1f, 0f, 1f,
                    0f, 0f, 1f, 1f,
                    1f, 1f, 0f, 1f,
                    1f, 0f, 1f, 1f,
                    0f, 1f, 1f, 1f,
                    0f, 0f, 0f, 1f,
                    1f, 1f, 1f, 1f,
                    0.5f, 0f, 0.5f, 1f,
                    0.5f, 0.5f, 0f, 1f,
                    0f, 0.5f, 0.5f, 1f,
                    0.5f, 0.5f, 0.5f, 1f,
                    0.5f, 0.333f, 0f, 1f,
                    0f, 0.5f, 0.333f, 1f,
                    0.333f, 0f, 0.5f, 1f,
                    0.333f, 0.333f, 0.333f, 1f
            };

    float colors2[] = new float[]
            {
                    0f, 0f/16f, 0f, 1f,
                    0f, 1f/16f, 0f, 1f,
                    0f, 2f/16f, 0f, 1f,
                    0f, 3f/16f, 0f, 1f,
                    0f, 4f/16f, 0f, 1f,
                    0f, 5f/16f, 0f, 1f,
                    0f, 6f/16f, 0f, 1f,
                    0f, 7f/16f, 0f, 1f,
                    0f, 8f/16f, 0f, 1f,
                    0f, 9f/16f, 0f, 1f,
                    0f, 10f/16f, 0f, 1f,
                    0f, 11f/16f, 0f, 1f,
                    0f, 12f/16f, 0f, 1f,
                    0f, 13f/16f, 0f, 1f,
                    0f, 14f/16f, 0f, 1f,
                    0f, 15f/16f, 0f, 1f,
            };

    boolean useColors = true;
    int colorTransformProgram;
    int nocolorTransformProgram;
    boolean changeProgram = false;
    boolean dither = false;
    int ditherStep = 0;

    protected void init() {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        image = new TextureElement(R.drawable.colors);
        nocolorTransformProgram = Programs.addProgram(VertexShaders.MatrixTexture, FragmentShaders.NoColorSwapTexture);
        colorTransformProgram = Programs.addProgram(VertexShaders.MatrixTexture, FragmentShaders.ColorSwapTexture);
        image.setProgram(colorTransformProgram);
        SetColors(colors);
        setupDepthAndCull();
        Textures.enableTextures();
    }

    private void SetColors(float[] newColors)
    {
        GLES20.glUseProgram(Programs.getProgram(colorTransformProgram));
        int COLOR_MASKS_location = GLES20.glGetUniformLocation(Programs.getProgram(colorTransformProgram), "COLOR_MASKS");
        GLES20.glUniform4fv(COLOR_MASKS_location, 16, newColors, 0);
        GLES20.glUseProgram(0);
    }

    public void display() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        image.draw();
        if (changeProgram) {
            changeProgram = false;
            if (useColors) {
                image.setProgram(nocolorTransformProgram);
                useColors = false;
            } else {
                image.setProgram(colorTransformProgram);
                useColors = true;
            }
        }
        if (dither) {
            if (ditherStep == 0) {
                ditherStep = 1;
                SetColors(colors);
            }
            else
            {
                ditherStep = 0;
                SetColors(colors2);
            }
        }
        else
        {
            SetColors(colors);
        }
    }

    public String keyboard(int keyCode, int x, int y) {
        StringBuilder result = new StringBuilder();
        switch (keyCode) {
            case KeyEvent.KEYCODE_P: {
                changeProgram = true;
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
                changeProgram = true;
                break;
            case 3:
                dither = !dither;
                break;
        }
    }
}
