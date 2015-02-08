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
import com.tutorial.glsltutorials.tutorials.Textures.TextureElement2;

/**
 * Created by jamie on 12/31/14.
 */
public class Tut_RotateTexture extends TutorialBase {
    boolean cull = true;
    boolean alpha = true;
    static TextureElement2 wood;
    boolean drawWood = true;

    float perspectiveAngle = 60f;
    float newPerspectiveAngle = 60f;

    protected void init ()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        wood = new TextureElement2(R.drawable.wood4_rotate);
        wood.scale(0.5f);
        wood.move(0f, 0f, -0.2f);
        setupDepthAndCull();
        Textures.enableTextures();
    }

    static Matrix4f cameraToClipMatrix;

    static private void SetGlobalMatrices()
    {
        wood.setCameraToClipMatrix(cameraToClipMatrix);
    }

    float g_fzNear = 1.0f;
    float g_fzFar = 10f;

    public void reshape()
    {
        MatrixStack persMatrix = new MatrixStack();
        persMatrix.perspective(perspectiveAngle, (width / (float) height), g_fzNear, g_fzFar);

        cameraToClipMatrix = Matrix4f.Identity();
        cameraToClipMatrix.M34 = -1f;

        SetGlobalMatrices();

        GLES20.glViewport(0, 0, width, height);

    }

    public void display()
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if (drawWood) wood.draw();
        if (perspectiveAngle != newPerspectiveAngle)
        {
            perspectiveAngle = newPerspectiveAngle;
            reshape();
        }
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        switch (keyCode) {
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
                break;
            case KeyEvent.KEYCODE_8:
                break;
            case KeyEvent.KEYCODE_9:
                break;
            case KeyEvent.KEYCODE_0:
                wood.setRotation(Matrix3f.Identity());
                break;
            case KeyEvent.KEYCODE_A:
                if (alpha)
                {
                    alpha = false;
                    GLES20.glDisable(GLES20.GL_ALPHA);
                    Log.i("KeyEvent", "alpha disabled");
                }
                else
                {
                    alpha = true;
                    GLES20.glEnable(GLES20.GL_ALPHA);
                    Log.i("KeyEvent", "alpha enabled");
                }
                break;
            case KeyEvent.KEYCODE_C:
                if (cull)
                {
                    cull = false;
                    GLES20.glDisable(GLES20.GL_CULL_FACE);
                    Log.i("KeyEvent", "cull disabled");
                }
                else
                {
                    cull = true;
                    GLES20.glEnable(GLES20.GL_CULL_FACE);
                    Log.i("KeyEvent", "cull enabled");
                }
                break;
            case KeyEvent.KEYCODE_F:
                break;
            case KeyEvent.KEYCODE_P:
                newPerspectiveAngle = perspectiveAngle + 5f;
                if (newPerspectiveAngle > 120f)
                {
                    newPerspectiveAngle = 30f;
                }
                break;
            case KeyEvent.KEYCODE_W:
                if (drawWood)
                    drawWood = false;
                else
                    drawWood = true;
                break;
        }
        return result.toString();
    }
}
