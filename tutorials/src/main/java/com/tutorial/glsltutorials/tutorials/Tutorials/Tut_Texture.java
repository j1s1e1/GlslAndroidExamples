package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Textures.PaintWall;
import com.tutorial.glsltutorials.tutorials.Textures.TextureElement;

/**
 * Created by jamie on 12/26/14.
 */
public class Tut_Texture extends TutorialBase
{
    TextureElement textureElement;
    PaintWall paintWall;
    TextureElement newTextureElement;
    boolean paint = false;

    protected void init ()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        textureElement = new TextureElement(R.drawable.wood4_rotate);
        textureElement.scale(0.2f);
        textureElement.move(0f, 0f, -0.2f);
        textureElement.rotateShape(Vector3f.UnitX, 45f);

        newTextureElement = new TextureElement(R.drawable.flashlight);
        newTextureElement.move(.04f, 0.4f, -0.1f);
        newTextureElement.scale(0.4f);
        paintWall = new PaintWall();
        setupDepthAndCull();
    }

    public void display()
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        newTextureElement.draw();
        textureElement.draw();
        textureElement.move(-0.02f, 0.05f, 0.00f);
        paintWall.draw();
        if (paint)
        {
            paintWall.PaintRandom();
            paint = false;
        }
    }

    public String keyboard(int keyCode, int x, int y)
        {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        switch (keyCode) {
            case KeyEvent.KEYCODE_1:
            newTextureElement.replace(R.drawable.flashlight);
            break;
            case KeyEvent.KEYCODE_2:
            newTextureElement.replace(R.drawable.wood4_rotate);
            break;
            case KeyEvent.KEYCODE_3:
            break;
            case KeyEvent.KEYCODE_4:
            break;
            case KeyEvent.KEYCODE_5:
            break;
            case KeyEvent.KEYCODE_6:
            break;
            case KeyEvent.KEYCODE_A:
            paint = true;
            break;
        }

        reshape();
        display();
        return result.toString();
    }
}
