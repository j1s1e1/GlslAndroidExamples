package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Shapes.MatrixSphere;
import com.tutorial.glsltutorials.tutorials.Shapes.Sphere;

/**
 * Created by Jamie on 6/13/14.
 */
public class Tut_Spheres extends TutorialBase {
    Sphere[] spheres;
    MatrixSphere[] mSphreres;

    protected void init() {
        spheres = new Sphere[5];
        for (int i = 0; i < spheres.length; i++) {
            float x_offset = (0.05f * i);
            float y_offset = (0.05f * i);
            float z_offset = (-0.1f * i);
            spheres[i] = new Sphere(0.05f);
            spheres[i].Move(x_offset, y_offset, z_offset);
        }

        mSphreres = new MatrixSphere[5];
        for (int i = 0; i < mSphreres.length; i++) {
            float x_offset = (-0.05f * i);
            float y_offset = (-0.05f * i);
            float z_offset = (-0.1f * i);
            mSphreres[i] = new MatrixSphere(0.07f);
            mSphreres[i].Move(x_offset, y_offset, z_offset);
            mSphreres[i].SetColor(1f, 0f, 0f);
        }
    }

   float worldx = 0;

    float mSphere0x = 0;
    float mSphere0y = 0;
    float mSphere0z = 0;

    float mSphere4Angle = 0;

    public void display()
    {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        for (int i = 0; i < spheres.length; i++) {
            spheres[i].draw();
            mSphreres[i].draw();
        }
        worldx = worldx + 0.01f;
        if (worldx > 1.5f) worldx = -1.5f;
        MatrixSphere.MoveWorld(worldx, 0, 0);
        mSphreres[0].MoveModel(mSphere0x, mSphere0y,mSphere0z);
        mSphere0x = mSphere0x + 0.01f;
        if (mSphere0x > 0.4f) mSphere0x = -0.4f;
        mSphere0y = mSphere0y - 0.02f;
        if (mSphere0y < -0.3f) mSphere0y = 0.3f;
        mSphere0z = mSphere0z + 0.01f;
        if (mSphere0z > 0.3f) mSphere0z = -0.3f;
        mSphreres[4].UpdateAngle(mSphere4Angle);
        mSphere4Angle = mSphere4Angle + 1f;
        if (mSphere4Angle > 360f) mSphere4Angle = 0f;
    }

}
