package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
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

    LitMatrixBlock2 lmb1;
    LitMatrixBlock2 lmb2;

    float lmb2_angle = 0f;

    protected void init()
    {
        lms1 = new LitMatrixSphere2(0.2f);
        lms2 = new LitMatrixSphere2(0.2f);
        lms2.setOffset(new Vector3f(-0.5f, 0f, 0f));
        lms2.setColor(0f, 1f, 0f);

        Vector3f size = new Vector3f(0.01f, 0.05f, 0.05f);
        Vector3f offset = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f axis = new Vector3f(0f, 0f, 1f);

        lmb1 = new LitMatrixBlock2(size, Colors.BLUE_COLOR);
        lmb1.setOffset(offset);
        lmb1.setAxis(axis);

        Vector3f size2 = new Vector3f(0.005f, 0.005f, 0.005f);
        Vector3f offset2 = new Vector3f(0.0f, 0.0f, 0.1f);
        Vector3f axis2 = new Vector3f(0f, 0f, 1f);

        lmb2 = new LitMatrixBlock2(size2, Colors.RED_COLOR);
        lmb2.setOffset(offset2);
        lmb2.setAxis(axis2);

        setupDepthAndCull();
        reshape();
    }

    public void display()
    {
        clearDisplay();
        lms1.draw();
        lms2.draw();
        lmb1.draw();
        lmb2.draw();
        lmb2.updateAngle(lmb2_angle++);
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_1:
                Shape.rotateWorld(new Vector3f(1f, 0f, 0f), 5f);
                break;
            case KeyEvent.KEYCODE_2:
                Shape.rotateWorld(new Vector3f(1f, 0f, 0f), -5f);
                break;
            case KeyEvent.KEYCODE_3:
                Shape.rotateWorld(new Vector3f(0f, 1f, 0f), 5f);
                break;
            case KeyEvent.KEYCODE_4:
                Shape.rotateWorld(new Vector3f(0f, 1f, 0f), -5f);
                break;
            case KeyEvent.KEYCODE_5:
                Shape.rotateWorld(new Vector3f(0f, 0f, 1f), 5f);
                break;
            case KeyEvent.KEYCODE_6:
                Shape.rotateWorld(new Vector3f(0f, 0f, 1f), -5f);
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

    public void touchEvent(int x_position, int y_position) throws Exception
    {
        int selection = x_position / 200;
        switch (selection)
        {
            case 0: Shape.rotateWorld(new Vector3f(1f, 0f, 0f), 5f); break;
            case 1: Shape.rotateWorld(new Vector3f(1f, 0f, 0f), -5f); break;
            case 2: Shape.rotateWorld(new Vector3f(0f, 1f, 0f), 5f); break;
            case 3: Shape.rotateWorld(new Vector3f(0f, 1f, 0f), -5f); break;
            case 4: Shape.rotateWorld(new Vector3f(0f, 0f, 1f), 5f); break;
            case 5: Shape.rotateWorld(new Vector3f(0f, 0f, 1f), -5f); break;
        }
        //QuickToast("Touch Event.  X = " + String.valueOf(x_position) + " Y = " + String.valueOf(y_position)
        //        + " selection " + String.valueOf(selection));
    }
}
