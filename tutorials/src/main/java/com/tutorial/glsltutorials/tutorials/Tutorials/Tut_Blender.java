package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Blender.Blender;
import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

import java.io.InputStream;

/**
 * Created by jamie on 10/12/14.
 */
public class Tut_Blender extends TutorialBase {
    public Tut_Blender ()
    {
    }

    Blender blender;
    Blender blender2;

    protected void init()
    {
        Programs.Reset();
        Shape.ResetWorldToCameraMatrix();
        InputStream test1 = Shader.context.getResources().openRawResource(R.raw.test_obj);
        blender = new Blender();
        blender.ReadFile(test1);
        blender.Scale(new Vector3f(0.05f, 0.05f, 0.05f));
        blender2 = new Blender();
        test1 = Shader.context.getResources().openRawResource(R.raw.test_obj);
        blender2.ReadFile(test1);
        blender2.Scale(new Vector3f(0.07f, 0.05f, 0.05f));
        blender2.SetColor(Colors.BLUE_COLOR);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CW);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthMask(true);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthRangef(0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_CLAMP_TO_EDGE);
    }

    double anglehorizontal = 0;
    double anglevertical = 0;
    float xoffset;
    float yoffset;
    float zoffset;


    public void display()
    {
       GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
       GLES20.glClearDepthf(1.0f);
       GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT |  GLES20.GL_DEPTH_BUFFER_BIT);
        blender.Draw();
        blender2.Draw();
        xoffset = (float) (Math.cos(anglevertical) * Math.cos(anglehorizontal));
        yoffset = (float) (Math.cos(anglevertical) * Math.sin(anglehorizontal));
        zoffset = (float) (Math.sin(anglevertical)) / 10f;
        blender.SetOffset(new Vector3f(xoffset, yoffset, zoffset));
        blender2.SetOffset(new Vector3f(-xoffset, yoffset, -zoffset));
        anglehorizontal = anglehorizontal + 0.02f;
        anglevertical = anglevertical + 0.01f;
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
            case KeyEvent.KEYCODE_I:
                result.append("Found " + String.valueOf(blender.ObjectCount()) + " objects in Blender file.");
                break;
        }
        return result.toString();
    }

    public void TouchEvent(int x_position, int y_position) throws Exception
    {
        int selection = x_position / 200;
        switch (selection)
        {
            case 0: Shape.RotateWorld(new Vector3f(1f, 0f, 0f), 5f); break;
            case 1: Shape.RotateWorld(new Vector3f(1f, 0f, 0f), -5f); break;
            case 2: Shape.RotateWorld(new Vector3f(0f, 1f, 0f), 5f); break;
            case 3: Shape.RotateWorld(new Vector3f(0f, 1f, 0f), -5f); break;
            case 4: Shape.RotateWorld(new Vector3f(0f, 0f, 1f), 5f); break;
            case 5: Shape.RotateWorld(new Vector3f(0f, 0f, 1f), -5f); break;
        }
        //QuickToast("Touch Event.  X = " + String.valueOf(x_position) + " Y = " + String.valueOf(y_position)
        //        + " selection " + String.valueOf(selection));
    }
}
