package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Textures.PaintWall;

/**
 * Created by jamie on 12/31/14.
 */
public class Tut_PaintWallPerspective extends TutorialBase {
    static PaintWall backWall;
    static PaintWall bottomWall;
    static PaintWall rightWall;
    static PaintWall topWall;
    static PaintWall leftWall;
    static PaintWall frontWall;
    boolean drawWalls = true;

    float perspectiveAngle = 90f;
    float newPerspectiveAngle = 90f;

    float textureRotation = -90f;

    float moveZ = -1f;

    protected void init ()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // back
        backWall = new PaintWall();
        backWall.scale(1.0f);
        backWall.rotateShape(new Vector3f(1f, 0f, 0f), 0f);
        backWall.move(0f, 0f, -1.9f);
        backWall.setLightPosition(new Vector3f(0f, 0f, 1.6f));

        // bottom
        bottomWall = new PaintWall();
        bottomWall.scale(1.0f);
        bottomWall.rotateShape(new Vector3f(1f, 0f, 0f), textureRotation);
        bottomWall.move(0f, -1f, moveZ);

        // right
        rightWall = new PaintWall();
        rightWall.scale(1.0f);
        rightWall.rotateShape(new Vector3f(0f, 1f, 0f), textureRotation);
        rightWall.move(1f, 0f, moveZ);

        // top
        topWall = new PaintWall();
        topWall.scale(1.0f);
        topWall.rotateShape(new Vector3f(1f, 0f, 0f), -textureRotation);
        topWall.move(0f, 1f, moveZ);

        // left
        leftWall = new PaintWall();
        leftWall.scale(1.0f);
        leftWall.rotateShape(new Vector3f(0f, 1f, 0f), -textureRotation);
        leftWall.move(-1f, 0f, moveZ);

        // front
        frontWall = new PaintWall();
        frontWall.scale(1.0f);
        frontWall.rotateShape(new Vector3f(1f, 0f, 0f), 0f);
        frontWall.move(0f, 0f, -0.6f);
        frontWall.setLightPosition(new Vector3f(0f, 0f, 0.2f));

        setupDepthAndCull();
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
        if (drawWalls)
        {
            backWall.draw();
            bottomWall.draw();
            rightWall.draw();
            topWall.draw();
            leftWall.draw();
            frontWall.draw();
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
                    bottomWall.rotateShape(Vector3f.UnitX, 5f);
                    break;
                case KeyEvent.KEYCODE_2:
                    bottomWall.rotateShape(Vector3f.UnitY, 5f);
                    break;
                case KeyEvent.KEYCODE_3:
                    bottomWall.rotateShape(Vector3f.UnitZ, 5f);
                    break;
                case KeyEvent.KEYCODE_4:
                    bottomWall.rotateShape(Vector3f.UnitX, -5f);
                    break;
                case KeyEvent.KEYCODE_5:
                    bottomWall.rotateShape(Vector3f.UnitY, -5f);
                    break;
                case KeyEvent.KEYCODE_6:
                    bottomWall.rotateShape(Vector3f.UnitZ, -5f);
                    break;
                case KeyEvent.KEYCODE_7:
                    bottomWall.move(0f, 0f, 0.1f);
                    break;
                case KeyEvent.KEYCODE_8:
                    bottomWall.move(0f, 0f, -0.1f);
                    break;
                case KeyEvent.KEYCODE_9:
                    bottomWall.move(0f, 0f, 0.1f);
                    topWall.move(0f, 0f, 0.1f);
                    leftWall.move(0f, 0f, 0.1f);
                    rightWall.move(0f, 0f, 0.1f);
                    break;
                case KeyEvent.KEYCODE_0:
                    bottomWall.move(0f, 0f, -0.1f);
                    topWall.move(0f, 0f, -0.1f);
                    leftWall.move(0f, 0f, -0.1f);
                    rightWall.move(0f, 0f, -0.1f);
                    break;
                case KeyEvent.KEYCODE_A:
                    backWall.move(0f, 0f, 0.1f);
                    break;
                case KeyEvent.KEYCODE_B:
                    backWall.move(0f, 0f, -0.1f);
                    break;
                case KeyEvent.KEYCODE_C:
                    frontWall.move(0f, 0f, 0.1f);
                    break;
                case KeyEvent.KEYCODE_D:
                    frontWall.move(0f, 0f, -0.1f);
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
                    if (drawWalls)
                        drawWalls = false;
                    else
                        drawWalls = true;
                    break;
                case  KeyEvent.KEYCODE_Y:
                    bottomWall.scale(0.9f);
                    break;
                case  KeyEvent.KEYCODE_Z:
                    bottomWall.scale(1.1f);
                    break;
            }
        }
        return result.toString();
    }
}
