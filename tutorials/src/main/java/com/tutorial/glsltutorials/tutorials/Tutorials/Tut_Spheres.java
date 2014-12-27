package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;
import com.tutorial.glsltutorials.tutorials.Shapes.MatrixSphere;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Shapes.Sphere;
import com.tutorial.glsltutorials.tutorials.Shapes.TextureSphere;

/**
 * Created by Jamie on 6/13/14.
 */
public class Tut_Spheres extends TutorialBase {
    LitMatrixSphere2 lms1;
    LitMatrixSphere2 lms2;
    TextureSphere ts;

    protected void init() {
        lms1 = new LitMatrixSphere2(0.2f);
        lms2 = new LitMatrixSphere2(0.2f);
        ts = new TextureSphere(0.2f, R.drawable.venus_magellan);
        setupDepthAndCull();
    }

    float angle = 0;
    Vector3f tsAxis = new Vector3f(0.1f, 1f, 0f);

    private void MoveSpheres()
    {
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        lms1.setOffset(new Vector3f(sin, cos, 0f));
        lms2.setOffset(new Vector3f(cos, sin, 0f));
        ts.setOffset(new Vector3f(-sin/2f, cos/2f, 0f));
        ts.rotateShape(tsAxis, angle/4f);
        angle += 0.02f;
    }

    public void display()
    {
        clearDisplay();
        lms1.draw();
        lms2.draw();
        ts.draw();
        MoveSpheres();
    }

}
