package com.tutorial.glsltutorials.tutorials.BodyParts;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Textures.TextureElement2;

/**
 * Created by jamie on 8/9/15.
 */
public class Wing {
    TextureElement2 te;
    Vector3f scale = new Vector3f(0.2f, 0.2f, 0.2f);
    Vector3f offset = new Vector3f(0.0f, 0f, 0f);
    Vector3f rotationAxis = Vector3f.UnitY;
    int flapCount = 0;
    boolean flapClosed = true;
    boolean flapEnabled = false;
    float flapAngle = 5f;

    public Wing (int wingTexture)
    {
        te = new TextureElement2(wingTexture);
        te.scale(scale);
        te.move(new Vector3f(-1f * 0.2f, 0f, 0f));
    }

    private void flap()
    {
        if (flapClosed)
        {
            rotate(rotationAxis, flapAngle);
        }
        else
        {
            rotate(rotationAxis, -flapAngle);
        }
        flapCount = flapCount + 5;
        if (flapCount == 90)
        {
            flapCount = 0;
            flapClosed = !flapClosed;
        }
    }

    public void setFlapAngle(float f)
    {
        flapAngle = f;
    }

    public void setRotationAxis(Vector3f v)
    {
        rotationAxis = v;
    }

    public void setFlapEnable(boolean flapEnable)
    {
        flapEnabled = flapEnable;
    }

    public void scale(Vector3f v)
    {
        te.scale(v);
    }

    public void move(Vector3f v)
    {
        te.move(v);
        offset = offset.add(v);
    }

    public void rotate(Vector3f axis, float angleDeg)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(axis, (float)Math.PI / 180.0f * angleDeg);
        te.rotateShape(offset, rotation);
        rotationAxis = Vector3f.transform(rotationAxis, rotation);
    }

    public void Draw()
    {
        te.draw();
        if (flapEnabled) flap();
    }

    public void setProgram(int program)
    {
        te.setProgram(program);
    }
}
