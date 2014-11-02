package com.tutorial.glsltutorials.tutorials.Objects;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;

/**
 * Created by jamie on 10/18/14.
 */
public class Missle {
    LitMatrixSphere2 topLeft;
    LitMatrixSphere2 topRight;
    LitMatrixSphere2 bottomLeft;
    LitMatrixSphere2 bottomRight;

    float radius = 0.01f;
    boolean started = false;
    boolean prepare_to_stop = false;
    boolean finished = false;
    boolean fire = false;

    float[] color = Colors.BLUE_COLOR;

    Vector3f topLeftDirection;
    Vector3f topRightDirection;
    Vector3f bottomLeftDirection;
    Vector3f bottomRightDirection;

    float stepMultiple = 0.04f;

    Vector3f axis;
    int startFrameCount = 10;
    int finishedFrameCount = 10;

    public Missle (Vector3f axisIn, Vector3f up, Vector3f right)
    {
        axis = new Vector3f(axisIn);
        topLeft = new LitMatrixSphere2(radius);
        topRight = new LitMatrixSphere2(radius);
        bottomLeft = new LitMatrixSphere2(radius);
        bottomRight = new LitMatrixSphere2(radius);

        topLeft.setColor(color);
        topRight.setColor(color);
        bottomLeft.setColor(color);
        bottomRight.setColor(color);

        topLeft.setOffset(right.mul(-1f).add(up));
        topRight.setOffset(right.add(up));
        bottomLeft.setOffset(right.mul(-1).sub(up));
        bottomRight.setOffset(right.sub(up));

        topLeftDirection = Vector3f.multiply(axis.sub(topLeft.GetOffset()), stepMultiple);
        topRightDirection = Vector3f.multiply(axis.sub(topRight.GetOffset()), stepMultiple);
        bottomLeftDirection = Vector3f.multiply(axis.sub(bottomLeft.GetOffset()), stepMultiple);
        bottomRightDirection = Vector3f.multiply(axis.sub(bottomRight.GetOffset()), stepMultiple);

        fire = true;
        started = true;
    }

    public boolean Started()
    {
        return started;
    }

    public boolean firing()
    {
        return fire;
    }

    public boolean Finished()
    {
        return finished;
    }

    public void Clear()
    {
        fire = false;
    }

    public Vector3f[] GetOffsets()
    {
        Vector3f[] offsets = new Vector3f[4];
        offsets[0] = topLeft.GetOffset();
        offsets[1] = topLeft.GetOffset();
        offsets[2] = topLeft.GetOffset();
        offsets[3] = topLeft.GetOffset();
        return offsets;
    }

    public void DrawMissle(LitMatrixSphere2 missle, Vector3f step)
    {
        missle.draw();
        Vector3f offset = missle.GetOffset();
        missle.setOffset(offset.add(step));
    }

    public void Draw()
    {
        Vector3f offset = topLeft.GetOffset();
        if ((offset.sub(axis).length()) < 0.01)
        {
            if (finishedFrameCount == 0)
            {
               fire = false;
               finished = true;
            }
            else
            {
                finishedFrameCount--;  // added some delays to allow GPU to catch up
            }
        }
        else
        {
            if (startFrameCount == 0) {
                DrawMissle(topLeft, topLeftDirection);
                DrawMissle(topRight, topRightDirection);
                DrawMissle(bottomLeft, bottomLeftDirection);
                DrawMissle(bottomRight, bottomRightDirection);
            }
            else
            {
                startFrameCount--;
            }
        }
    }

}
