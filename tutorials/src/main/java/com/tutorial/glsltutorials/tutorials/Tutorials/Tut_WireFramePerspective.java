package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Shapes.WireFrameBlock;

/**
 * Created by jamie on 1/10/15.
 */
public class Tut_WireFramePerspective extends TutorialBase {
    WireFrameBlock wireFrameBlock;
    boolean drawWireFrame = true;

    float perspectiveAngle = 160f;
    float newPerspectiveAngle = 160f;

    protected void init ()
    {
       GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        wireFrameBlock = new WireFrameBlock(new Vector3f(5f, 5f, 5f), Colors.RED_COLOR);
        wireFrameBlock.rotateShape(new Vector3f(1f, 0f, 0f), 60f);
        wireFrameBlock.move(0f, 0f, -5f);

        setupDepthAndCull();
        g_fzNear = 0.5f;
        g_fzFar = 10f;
        reshape();
    }

    private void SetGlobalMatrices()
    {
        wireFrameBlock.setCameraToClipMatrix(cameraToClipMatrix);
        wireFrameBlock.setWorldToCameraMatrix(worldToCameraMatrix);
    }

    public void reshape()
    {
        MatrixStack persMatrix = new MatrixStack();
        persMatrix.perspective(perspectiveAngle, (width / (float) height), g_fzNear, g_fzFar);

        worldToCameraMatrix = persMatrix.Top();

        cameraToClipMatrix = Matrix4f.Identity();
        //cameraToClipMatrix.M34 = -1f;

        SetGlobalMatrices();

       GLES20.glViewport(0, 0, width, height);

    }

    public void display()
    {
        clearDisplay();
        if (drawWireFrame) wireFrameBlock.draw();
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
                    wireFrameBlock.rotateShape(Vector3f.UnitX, 5f);
                    break;
                case KeyEvent.KEYCODE_2:
                    wireFrameBlock.rotateShape(Vector3f.UnitY, 5f);
                    break;
                case KeyEvent.KEYCODE_3:
                    wireFrameBlock.rotateShape(Vector3f.UnitZ, 5f);
                    break;
                case KeyEvent.KEYCODE_4:
                    wireFrameBlock.rotateShape(Vector3f.UnitX, -5f);
                    break;
                case KeyEvent.KEYCODE_5:
                    wireFrameBlock.rotateShape(Vector3f.UnitY, -5f);
                    break;
                case KeyEvent.KEYCODE_6:
                    wireFrameBlock.rotateShape(Vector3f.UnitZ, -5f);
                    break;
                case KeyEvent.KEYCODE_7:
                    break;
                case KeyEvent.KEYCODE_8:
                    break;
                case KeyEvent.KEYCODE_9:
                    break;
                case KeyEvent.KEYCODE_0:
                    wireFrameBlock.setRotation(Matrix3f.Identity());
                    break;
                case KeyEvent.KEYCODE_F:
                    break;
                case KeyEvent.KEYCODE_P:
                    newPerspectiveAngle = perspectiveAngle + 5f;
                    if (newPerspectiveAngle > 170f) {
                        newPerspectiveAngle = 30f;
                    }
                    break;
                case KeyEvent.KEYCODE_W:
                    if (drawWireFrame)
                        drawWireFrame = false;
                    else
                        drawWireFrame = true;
                    break;
                case KeyEvent.KEYCODE_Z:
                    wireFrameBlock.scale(new Vector3f(1.1f, 1.1f, 1.1f));
                    break;
            }
        }
        return result.toString();
    }
}
