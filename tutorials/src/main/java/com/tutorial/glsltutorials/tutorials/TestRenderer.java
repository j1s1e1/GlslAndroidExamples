package com.tutorial.glsltutorials.tutorials;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.tutorial.glsltutorials.tutorials.Tutorials.TutorialBase;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TestRenderer implements GLSurfaceView.Renderer {

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        SetupTutorial();
    }

    int frame_count = 0;
    public static TutorialBase tutorial;

    private void SetupTutorial()
    {
        tutorial.Setup();
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        try
        {
            tutorial.display();
        }
        catch (Exception ex)
        {
            int debug = 0;
            debug++;
        }
        frame_count++;
        if ((frame_count % 10) == 0) {
            frame_count++;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);
        tutorial.height = height;
        tutorial.width = width;
        tutorial.reshape();
    }

}
