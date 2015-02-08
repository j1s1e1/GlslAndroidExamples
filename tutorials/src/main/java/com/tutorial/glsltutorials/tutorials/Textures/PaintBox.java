package com.tutorial.glsltutorials.tutorials.Textures;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by jamie on 1/10/15.
 */
public class PaintBox {
    static PaintWall backWall;
    static PaintWall bottomWall;
    static PaintWall rightWall;
    static PaintWall topWall;
    static PaintWall leftWall;
    static PaintWall frontWall;

    float textureRotation = -90f;

    float moveZ = -1f;

    Vector3f lowLimits;
    Vector3f highLimits;

    Vector3f center;

    Vector3f backWallOffset;
    Vector3f frontWallOffset;
    Vector3f bottomWallOffset;
    Vector3f topWallOffset;
    Vector3f rightWallOffset;
    Vector3f leftWallOffset;

    boolean drawFrontWall = true;

    public PaintBox()
    {
        center = new Vector3f(0f, 0f, moveZ);
        backWallOffset = new Vector3f(0f, 0f, -2f);
        frontWallOffset = new Vector3f(0f, 0f, 0f);
        bottomWallOffset = new Vector3f(0f, -1f, moveZ);
        topWallOffset = new Vector3f(0f, 1f, moveZ);
        rightWallOffset = new Vector3f(1f, 0f, moveZ);
        leftWallOffset = new Vector3f(-1f, 0f, moveZ);
        // back
        backWall = new PaintWall();
        backWall.scale(1.0f);
        backWall.rotateShape(new Vector3f(1f, 0f, 0f), 0f);
        backWall.move(backWallOffset);
        backWall.setLightPosition(new Vector3f(0f, 0f, 1.6f));

        // bottom
        bottomWall = new PaintWall();
        bottomWall.scale(1.0f);
        bottomWall.rotateShape(new Vector3f(1f, 0f, 0f), textureRotation);
        bottomWall.move(bottomWallOffset);

        // right
        rightWall = new PaintWall();
        rightWall.scale(1.0f);
        rightWall.rotateShape(new Vector3f(0f, 1f, 0f), textureRotation);
        rightWall.move(rightWallOffset);

        // top
        topWall = new PaintWall();
        topWall.scale(1.0f);
        topWall.rotateShape(new Vector3f(1f, 0f, 0f), -textureRotation);
        topWall.move(topWallOffset);

        // left
        leftWall = new PaintWall();
        leftWall.scale(1.0f);
        leftWall.rotateShape(new Vector3f(0f, 1f, 0f), -textureRotation);
        leftWall.move(leftWallOffset);

        // front
        frontWall = new PaintWall();
        frontWall.scale(1.0f);
        frontWall.rotateShape(new Vector3f(1f, 0f, 0f), 0f);
        frontWall.move(frontWallOffset);
        frontWall.setLightPosition(new Vector3f(0f, 0f, 0.2f));
    }

    public void setLimits(Vector3f low, Vector3f high, Vector3f epsilon)
    {
        lowLimits = low.add(epsilon);
        highLimits = high.sub(epsilon);
    }

    public void paint(Vector3f position)
    {
        if (position.x < lowLimits.x)
        {
            leftWall.paint(position.y, position.z);
        }
        if (position.x > highLimits.x)
        {
            rightWall.paint(position.y, position.z);
        }
        if (position.y < lowLimits.y)
        {
            bottomWall.paint(position.x, position.z);
        }
        if (position.y > highLimits.y)
        {
            topWall.paint(position.x, position.z);
        }
        if (position.z < lowLimits.z)
        {
            backWall.paint(position.x, position.y);
        }
        if (position.z > highLimits.z)
        {
            frontWall.paint(position.x, position.y);
        }
    }

    public void move(Vector3f v)
    {
        backWall.move(v);
        bottomWall.move(v);
        rightWall.move(v);
        topWall.move(v);
        leftWall.move(v);
        frontWall.move(v);
    }

    public void rotateShape(Vector3f r, float a)
    {
        backWall.rotateShape(r, a);
        bottomWall.rotateShape(r, a);
        rightWall.rotateShape(r, a);
        topWall.rotateShape(r, a);
        leftWall.rotateShape(r, a);
        frontWall.rotateShape(r, a);
    }

    public void rotateShapeOffset(Vector3f r, float a)
    {
        backWall.rotateShape(center, r, a);
        bottomWall.rotateShape(center, r, a);
        rightWall.rotateShape(center,r, a);
        topWall.rotateShape(center,r, a);
        leftWall.rotateShape(center,r, a);
        frontWall.rotateShape(center,r, a);
    }

    public void moveFront(Vector3f v)
    {
        frontWall.move(v);
    }

    public void clear()
    {
        backWall.clear();
        bottomWall.clear();
        rightWall.clear();
        topWall.clear();
        leftWall.clear();
        frontWall.clear();
    }

    public void draw()
    {
        backWall.draw();
        bottomWall.draw();
        rightWall.draw();
        topWall.draw();
        leftWall.draw();
        if (drawFrontWall) frontWall.draw();
    }

    public void setDrawFrontWall(boolean b)
    {
        drawFrontWall = b;
    }
}
