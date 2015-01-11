package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Textures.TextureElement;

/**
 * Created by jamie on 12/31/14.
 */
public class Tut_TexturePerspective extends TutorialBase {
    static TextureElement wood;
    static TextureElement wood2;
    static TextureElement wood3;
    static TextureElement wood4;
    boolean drawWood = true;

    float perspectiveAngle = 90f;
    float newPerspectiveAngle = 90f;

    float textureRotation = -90f;

    protected void init ()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        wood = new TextureElement(R.drawable.wood4_rotate);
        wood.scale(1.0f);
        wood.rotateShape(new Vector3f(1f, 0f, 0f), textureRotation);
        wood.move(0f, -1f, -0.5f);

        wood2 = new TextureElement(R.drawable.wood4_rotate);
        wood2.scale(1.0f);
        wood2.rotateShape(new Vector3f(0f, 1f, 0f), textureRotation);
        wood2.move(1f, 0f, -0.5f);

        wood3 = new TextureElement(R.drawable.wood4_rotate);
        wood3.scale(1.0f);
        wood3.rotateShape(new Vector3f(1f, 0f, 0f), -textureRotation);
        wood3.move(0f, 1f, -0.5f);

        wood4 = new TextureElement(R.drawable.wood4_rotate);
        wood4.scale(1.0f);
        wood4.rotateShape(new Vector3f(0f, -1f, 0f), textureRotation);
        wood4.move(-1f, 0f, -0.5f);

        setupDepthAndCull();
        Textures.enableTextures();
        g_fzNear = 0.5f;
        g_fzFar = 10f;
        reshape();
    }

    static private void SetGlobalMatrices()
    {
        Shape.setCameraToClipMatrix(cameraToClipMatrix);
        Shape.setWorldToCameraMatrix(worldToCameraMatrix);
    }

    public void reshape()
    {
        MatrixStack persMatrix = new MatrixStack();
        persMatrix.perspective(perspectiveAngle, (width / (float) height), g_fzNear, g_fzFar);

        worldToCameraMatrix = persMatrix.Top();

        cameraToClipMatrix = Matrix4f.Identity();

        SetGlobalMatrices();

        GLES20.glViewport(0, 0, width, height);

    }

    public void display()
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if (drawWood)
        {
            wood.draw();
            wood2.draw();
            wood3.draw();
            wood4.draw();
        }
        if (perspectiveAngle != newPerspectiveAngle)
        {
            perspectiveAngle = newPerspectiveAngle;
            reshape();
        }
        updateDisplayOptions();
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        if (displayOptions)
        {
            setDisplayOptions(keyCode);
        }
        else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    displayOptions = true;
                    break;
                case KeyEvent.KEYCODE_1:
                    wood.rotateShape(Vector3f.UnitX, 5f);
                    break;
                case KeyEvent.KEYCODE_2:
                    wood.rotateShape(Vector3f.UnitY, 5f);
                    break;
                case KeyEvent.KEYCODE_3:
                    wood.rotateShape(Vector3f.UnitZ, 5f);
                    break;
                case KeyEvent.KEYCODE_4:
                    wood.rotateShape(Vector3f.UnitX, -5f);
                    break;
                case KeyEvent.KEYCODE_5:
                    wood.rotateShape(Vector3f.UnitY, -5f);
                    break;
                case KeyEvent.KEYCODE_6:
                    wood.rotateShape(Vector3f.UnitZ, -5f);
                    break;
                case KeyEvent.KEYCODE_7:
                    wood.move(0f, 0f, 0.5f);
                    break;
                case KeyEvent.KEYCODE_8:
                    wood.move(0f, 0f, -0.5f);
                    break;
                case KeyEvent.KEYCODE_9:
                    break;
                case KeyEvent.KEYCODE_0:
                    wood.setRotation(Matrix3f.Identity());
                    break;
                case KeyEvent.KEYCODE_F:
                    break;
                case KeyEvent.KEYCODE_I:
                    Log.i("KeyEvent", "g_fzNear = " + String.valueOf(g_fzNear));
                    Log.i("KeyEvent", "g_fzFar = " + String.valueOf(g_fzFar));
                    Log.i("KeyEvent", "perspectiveAngle = " + String.valueOf(perspectiveAngle));
                    Log.i("KeyEvent", "textureRotation = " + String.valueOf(textureRotation));
                    break;
                case KeyEvent.KEYCODE_P:
                    newPerspectiveAngle = perspectiveAngle + 5f;
                    if (newPerspectiveAngle > 170f) {
                        newPerspectiveAngle = 30f;
                    }
                    break;
                case KeyEvent.KEYCODE_W:
                    if (drawWood)
                        drawWood = false;
                    else
                        drawWood = true;
                    break;
                case  KeyEvent.KEYCODE_Y:
                    wood.scale(0.9f);
                    break;
                case  KeyEvent.KEYCODE_Z:
                    wood.scale(1.1f);
                    break;
            }
        }
        return result.toString();
    }
}
