package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixBlock2;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

import java.util.ArrayList;

/**
 * Created by jamie on 10/12/14.
 */
public class Tut_Blocks extends TutorialBase {
    public Tut_Blocks() {
    }

    ArrayList<LitMatrixBlock2> lmbs;

    private void AddBlock(Vector3f size, Vector3f offset, Vector3f axis, float[] color) {
        LitMatrixBlock2 lmb1 = new LitMatrixBlock2(size, color);
        lmb1.setOffset(offset);
        lmb1.setAxis(axis);
        lmbs.add(lmb1);
    }


    protected void init() {
        lmbs = new ArrayList<LitMatrixBlock2>();

        AddBlock(new Vector3f(0.01f, 0.01f, 0.01f), new Vector3f(0f, 0.25f, 0.25f),
                new Vector3f(0f, 1f, 0f), Colors.BLUE_COLOR);
        AddBlock(new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0f, -0.25f, -0.25f),
                new Vector3f(1f, 0f, 0f), Colors.GREEN_COLOR);

        AddBlock(new Vector3f(0.1f, 0.1f, 0.1f), new Vector3f(0.5f, 0f, 0f),
                new Vector3f(0f, 0f, 1f), Colors.CYAN_COLOR);

        AddBlock(new Vector3f(0.15f, 0.1f, 0.15f), new Vector3f(0f, -0f, 0f),
                new Vector3f(0.2f, 0.2f, .2f), Colors.MAGENTA_COLOR);

        lmbs.add(new LitMatrixBlock2(new Vector3f(0.25f, 0.25f, 0.25f), Colors.RED_COLOR));

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CW);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthMask(true);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthRangef(0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_CLAMP_TO_EDGE);
    }

    private float angle = 0;

    public void display() {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        for (LitMatrixBlock2 lmb : lmbs) {
            lmb.draw();
            lmb.updateAngle(angle);
        }
        angle++;
        //angle = angle + (float)Math.PI / 180f;
        //if (angle > 2 * Math.PI) angle = 0;
    }

    public void TouchEvent(int x_position, int y_position) throws Exception
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
