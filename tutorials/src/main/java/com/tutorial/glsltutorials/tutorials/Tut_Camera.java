package com.tutorial.glsltutorials.tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixBlock2;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

/**
 * Created by jamie on 10/12/14.
 */
public class Tut_Camera extends TutorialBase {
    public Tut_Camera ()
    {
    }
    LitMatrixSphere2 lms1;
    LitMatrixSphere2 lms2;
    LitMatrixBlock2 lmb2;

    float lmb2_angle = 0f;

    protected void init()
    {
        lms1 = new LitMatrixSphere2(0.2f);
        lms2 = new LitMatrixSphere2(0.2f);
        lms2.SetOffset(new Vector3f(-0.5f, 0f, 0f));
        lms2.SetColor(0f, 1f, 0f);

        Vector3f size = new Vector3f(0.1f, 0.1f, 0.1f);
        Vector3f offset = new Vector3f(0.5f, 0.025f, 0f);
        Vector3f axis = new Vector3f(1f, 1f, 1f);

        lmb2 = new LitMatrixBlock2(size, Colors.BLUE_COLOR);
        lmb2.SetOffset(offset);
        lmb2.SetAxis(axis);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CW);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthMask(true);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthRangef(0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_CLAMP_TO_EDGE);
    }

    public void display()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        lms1.Draw();
        lms2.Draw();
        lmb2.Draw();
        lmb2.UpdateAngle(lmb2_angle++);
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_1:
                Shape.RotateWorld(new Vector3f(1f, 0f, 0f), 5f);
                break;
            case KeyEvent.KEYCODE_2:
                Shape.RotateWorld(new Vector3f(1f, 0f, 0f), -5f);
                break;
            case KeyEvent.KEYCODE_3:
                Shape.RotateWorld(new Vector3f(0f, 1f, 0f), 5f);
                break;
            case KeyEvent.KEYCODE_4:
                Shape.RotateWorld(new Vector3f(0f, 1f, 0f), -5f);
                break;
            case KeyEvent.KEYCODE_5:
                Shape.RotateWorld(new Vector3f(0f, 0f, 1f), 5f);
                break;
            case KeyEvent.KEYCODE_6:
                Shape.RotateWorld(new Vector3f(0f, 0f, 1f), -5f);
                break;
            case KeyEvent.KEYCODE_N:
                Shape.worldToCamera.M42 = Shape.worldToCamera.M42 + 0.01f;
                break;
            case KeyEvent.KEYCODE_S:
                Shape.worldToCamera.M42 = Shape.worldToCamera.M42 - 0.01f;
                break;
            case KeyEvent.KEYCODE_E:
                Shape.worldToCamera.M41 = Shape.worldToCamera.M41 + 0.01f;
                break;
            case KeyEvent.KEYCODE_W:
                Shape.worldToCamera.M41 = Shape.worldToCamera.M41 - 0.01f;
                break;
            case KeyEvent.KEYCODE_SPACE:
                break;
        }
        result.append(keyCode);
        reshape();
        display();
        return result.toString();
    }
}
