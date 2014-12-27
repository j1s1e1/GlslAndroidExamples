package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
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
    boolean blend = false;
    boolean lastBlend = false;
    boolean depthTest = false;
    boolean lastDepthTest = false;

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
        paintWall.move(0.0f, 0.0f, 0.5f);
        setupDepthAndCull();
        //Test
        Textures.enableTextures();
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
        if (blend != lastBlend)
        {
            if (blend)
            {
                GLES20.glEnable(GLES20.GL_BLEND);
            }
            else
            {
                GLES20.glDisable(GLES20.GL_BLEND);
            }
            lastBlend = blend;
        }
        if (depthTest != lastDepthTest)
        {
            if (depthTest)
            {
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            }
            else
            {
                GLES20.glDisable(GLES20.GL_DEPTH_TEST);
            }
            lastDepthTest = depthTest;
        }
    }

    public String keyboard(int keyCode, int x, int y)
    {
        Vector3f movement;
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
                depthTest = true;
                Log.i("Keyevent ", "GL_DEPTH_TEST on");
            break;
            case KeyEvent.KEYCODE_4:
                depthTest = false;
                Log.i("Keyevent ", "GL_DEPTH_TEST off");
            break;
            case KeyEvent.KEYCODE_5:
                blend = true;
                Log.i("Keyevent ", "GL_BLEND on");
            break;
            case KeyEvent.KEYCODE_6:
                blend = false;
                Log.i("Keyevent ", "GL_BLEND off");
            break;
            case KeyEvent.KEYCODE_7:
                newTextureElement.move(new Vector3f(0.1f, 0.1f, 0.1f));
                Log.i("Keyevent ", "");
                break;
            case KeyEvent.KEYCODE_8:
                newTextureElement.move(new Vector3f(-0.1f, -0.1f, -0.1f));
                Log.i("Keyevent ", "");
                break;
            case KeyEvent.KEYCODE_9:
                movement = new Vector3f(0.0f, 0.0f, 0.1f);
                paintWall.move(movement);
                Log.i("Keyevent ", "Paintwall to " + movement.add(paintWall.getOffset()).toString());
                break;
            case KeyEvent.KEYCODE_0:
                movement = new Vector3f(0.0f, 0.0f, -0.1f);
                paintWall.move(movement);
                Log.i("Keyevent ", "Paintwall to " + movement.add(paintWall.getOffset()).toString());
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
