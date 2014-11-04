package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixBlock2;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

/**
 * Created by jamie on 10/18/14.
 */
public class Tut_Vectors extends TutorialBase {
    LitMatrixBlock2 lmb2;
    Vector3f axis;
    Vector3f angles = new Vector3f(0f, 0f, 0f);


    public Tut_Vectors ()
    {
    }

    protected void init()
    {
        Programs.reset();
        Shape.resetWorldToCameraMatrix();
        lmb2 = new LitMatrixBlock2(new Vector3f (0.05f, 1f, 0.05f), Colors.GREEN_COLOR);
        axis = new Vector3f(0f, 1f, 0f);
        lmb2.setAxis(axis);
        setupDepthAndCull();
    }

    public void display()
    {
        clearDisplay();
        lmb2.draw();
    }

    public void Rotate(Vector3f rotationAxis, float angle)
    {
        lmb2.rotateShape(rotationAxis, angle);
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_1:
                Rotate(Vector3f.UnitX, 5f);
                angles.x += 5f;
                break;
            case KeyEvent.KEYCODE_2:
                Rotate(Vector3f.UnitX, -5f);
                angles.x += -5f;
                break;
            case KeyEvent.KEYCODE_3:
                Rotate(Vector3f.UnitY, 5f);
                angles.y += 5f;
                break;
            case KeyEvent.KEYCODE_4:
                Rotate(Vector3f.UnitY, -5f);
                angles.y += -5f;
                break;
            case KeyEvent.KEYCODE_5:
                Rotate(Vector3f.UnitZ, 5f);
                angles.z += 5f;
                break;
            case KeyEvent.KEYCODE_6:
                Rotate(Vector3f.UnitZ, -5f);
                angles.z += -5f;
                break;
            case KeyEvent.KEYCODE_I:
                break;
            case KeyEvent.KEYCODE_SPACE:
                break;
        }
        if (angles.x > 180f) angles.x -= 360f;
        if (angles.y > 180f) angles.y -= 360f;
        if (angles.z > 180f) angles.z -= 360f;
        if (angles.x < -180f) angles.x += 360f;
        if (angles.y < -180f) angles.y += 360f;
        if (angles.z < -180f) angles.z += 360f;
        result.append("angles = " + String.valueOf(angles));
        return result.toString();
    }

    public void touchEvent(int x_position, int y_position) throws Exception
    {
        int selection = x_position / 200;
        switch (selection)
        {
            case 0: Rotate(Vector3f.UnitX, 5f); break;
            case 1: Rotate(Vector3f.UnitX, -5f); break;
            case 2: Rotate(Vector3f.UnitY, 5f); break;
            case 3: Rotate(Vector3f.UnitY, -5f); break;
            case 4: Rotate(Vector3f.UnitZ, 5f); break;
            case 5: Rotate(Vector3f.UnitZ, -5f); break;
        }
        //QuickToast("Touch Event.  X = " + String.valueOf(x_position) + " Y = " + String.valueOf(y_position)
        //        + " selection " + String.valueOf(selection));
    }
}
