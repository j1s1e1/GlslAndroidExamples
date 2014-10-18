package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.os.Handler;
import android.widget.Toast;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixBlock2;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Tutorials.TutorialBase;

/**
 * Created by jamie on 10/18/14.
 */
public class Missle {
    LitMatrixSphere2 topLeft;
    LitMatrixSphere2 topRight;
    LitMatrixSphere2 bottomLeft;
    LitMatrixSphere2 bottomRight;

    float radius = 0.01f;
    public boolean fire = false;

    float[] color = Colors.BLUE_COLOR;

    Vector3f topLeftDirection;
    Vector3f topRightDirection;
    Vector3f bottomLeftDirection;
    Vector3f bottomRightDirection;

    float stepMultiple = 0.04f;

    Vector3f axis;

    public Missle (Vector3f axisIn, Vector3f up, Vector3f right)
    {
        axis = new Vector3f(axisIn);
        topLeft = new LitMatrixSphere2(radius);
        topRight = new LitMatrixSphere2(radius);
        bottomLeft = new LitMatrixSphere2(radius);
        bottomRight = new LitMatrixSphere2(radius);

        topLeft.SetColor(color);
        topRight.SetColor(color);
        bottomLeft.SetColor(color);
        bottomRight.SetColor(color);

        topLeft.SetOffset(right.mul(-1f).add(up));
        topRight.SetOffset(right.add(up));
        bottomLeft.SetOffset(right.mul(-1).sub(up));
        bottomRight.SetOffset(right.sub(up));

        topLeftDirection = Vector3f.multiply(axis.sub(topLeft.GetOffset()), stepMultiple);
        topRightDirection = Vector3f.multiply(axis.sub(topRight.GetOffset()), stepMultiple);
        bottomLeftDirection = Vector3f.multiply(axis.sub(bottomLeft.GetOffset()), stepMultiple);
        bottomRightDirection = Vector3f.multiply(axis.sub(bottomRight.GetOffset()), stepMultiple);

        fire = true;

    }

    public boolean Firing()
    {
        return fire;
    }

    public void Clear()
    {
        fire = false;
    }

    public void DrawMissle(LitMatrixSphere2 missle, Vector3f step)
    {

        missle.Draw();
        Vector3f offset = missle.GetOffset();
        missle.SetOffset(offset.add(step));

    }

    public void DrawMissle2(LitMatrixBlock2 missle, Vector3f step)
    {
        missle.Draw();
        Vector3f offset = missle.GetOffset();
        missle.SetOffset(offset.add(step));
    }

    float angle = 0;

    public void Draw()
    {
        float remaining;
        Vector3f difference= new Vector3f();
        DrawMissle(topLeft, topLeftDirection);
        DrawMissle(topRight, topRightDirection);
        DrawMissle(bottomLeft, bottomLeftDirection);
        DrawMissle(bottomRight, bottomRightDirection);
        Vector3f offset = topLeft.GetOffset();
        difference = offset.sub(axis);
        remaining = (offset.sub(axis)).length();
        if ((offset.sub(axis)).length() < 0.1) fire = false;
    }

}
