package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Textures.PaintBox;

import java.util.Random;

/**
 * Created by jamie on 1/10/15.
 */
public class Tut_PaintBox2 extends TutorialBase {
    static PaintBox paintBox;
    Random random = new Random();
    float ballRadius = 0.25f;
    float ballSpeedFactor = 1f;
    static float ballLimit = 0.75f;
    static Vector3f ballOffset = new Vector3f(0f, 0f, -1f);
    Vector3f ballLimitLow = ballOffset.add(new Vector3f(-ballLimit, -ballLimit, -ballLimit));
    Vector3f ballLimitHigh = ballOffset.add(new Vector3f(ballLimit, ballLimit, ballLimit));
    Vector3f boxLimitLow;
    Vector3f boxLimitHigh;
    Vector3f ballSpeed;

    float perspectiveAngle = 90f;
    float newPerspectiveAngle = 90f;

    float textureRotation = -90f;
    float epsilon = 0.251f;

    float moveZ = -1f;

    int ballProgram;
    int numberOfLights = 2;

    protected void init ()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        boxLimitLow = new Vector3f();
        boxLimitLow = ballLimitLow.sub(new Vector3f(ballRadius, ballRadius, ballRadius));
        boxLimitHigh = new Vector3f();
        boxLimitHigh = ballLimitHigh.add(new Vector3f(ballRadius, ballRadius, ballRadius));

        paintBox = new PaintBox();
        ballSpeed = new Vector3f(
                ballSpeedFactor + ballSpeedFactor * (float)random.nextDouble(),
                ballSpeedFactor + ballSpeedFactor * (float)random.nextDouble(),
                ballSpeedFactor + ballSpeedFactor * (float)random.nextDouble());

        paintBox.setLimits(boxLimitLow, boxLimitHigh, new Vector3f(epsilon, epsilon, epsilon));
        paintBox.move(new Vector3f(0f, 0f, -1f));

        setupDepthAndCull();
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        Textures.enableTextures();
        g_fzNear = 0.5f;
        g_fzFar = 100f;
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
        paintBox.draw();
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
                    paintBox.move(new Vector3f(0f, 0f, 0.1f));
                    break;
                case KeyEvent.KEYCODE_2:
                    paintBox.move(new Vector3f(0f, 0f, -0.1f));
                    break;
                case KeyEvent.KEYCODE_3:
                    paintBox.moveFront(new Vector3f(0f, 0f, 0.1f));
                    break;
                case KeyEvent.KEYCODE_4:
                    paintBox.moveFront(new Vector3f(0f, 0f, -0.1f));
                    break;
                case KeyEvent.KEYCODE_5:
                    paintBox.rotateShape(Vector3f.UnitX, 5f);
                    Log.i("KeyEvent", "RotateShape 5X");
                    break;
                case KeyEvent.KEYCODE_6:
                    paintBox.rotateShape(Vector3f.UnitY, 5f);
                    Log.i("KeyEvent", "RotateShape 5Y");
                    break;
                case KeyEvent.KEYCODE_7:
                    paintBox.rotateShape(Vector3f.UnitZ, 5f);
                    Log.i("KeyEvent", "RotateShape 5Z");
                    break;
                case KeyEvent.KEYCODE_8:
                    paintBox.rotateShapeOffset(Vector3f.UnitX, 5f);
                    Log.i("KeyEvent", "RotateShapeOffset 5X");
                    break;
                case KeyEvent.KEYCODE_9:
                    break;
                case KeyEvent.KEYCODE_0:
                    break;
                case KeyEvent.KEYCODE_A:
                    break;
                case KeyEvent.KEYCODE_B:
                    break;
                case KeyEvent.KEYCODE_C:
                    paintBox.clear();
                    break;
                case KeyEvent.KEYCODE_D:
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
            }
        }
        return result.toString();
    }
}
