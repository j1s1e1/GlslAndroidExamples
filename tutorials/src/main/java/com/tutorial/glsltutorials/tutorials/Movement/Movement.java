package com.tutorial.glsltutorials.tutorials.Movement;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.Random;

/**
 * Created by jamie on 11/29/14.
 */
public class Movement {

    static Random random = new Random();
    protected float xLimitLow = -1.0f;
    protected float yLimitLow = -1.0f;
    protected float zLimitLow = 0f;

    protected float xLimitHigh = 1.0f;
    protected float yLimitHigh = 1.0f;
    protected float zLimitHigh = 1.0f;

    protected float maxXmovement = 0.1f;
    protected float maxYmovement = 0.1f;
    protected float maxZmovement = 0.1f;

    protected Vector3f speed = new Vector3f(0f, 0f, 0f);
    protected Vector3f currentPosition = new Vector3f(0f, 0f, 0f);

    public Vector3f newOffset(Vector3f oldOffset)
    {
        return oldOffset;
    }

    public void setXlimits(float xLow, float xHigh)
    {
        xLimitLow = xLow;
        xLimitHigh = xHigh;
    }

    public void setYlimits(float yLow, float yHigh)
    {
        yLimitLow = yLow;
        yLimitHigh = yHigh;
    }

    public void setZlimits(float zLow, float zHigh)
    {
        zLimitLow = zLow;
        zLimitHigh = zHigh;
    }

    public void setLimits(Vector3f low, Vector3f high)
    {
        xLimitLow = low.x;
        xLimitHigh = high.x;
        yLimitLow = low.y;
        yLimitHigh = high.y;
        zLimitLow = low.z;
        zLimitHigh = high.z;
    }

    public void moveLimits(Vector3f v)
    {
        xLimitLow += v.x;
        xLimitHigh += v.x;
        yLimitLow += v.y;
        yLimitHigh += v.y;
        zLimitLow += v.z;
        zLimitHigh += v.z;
    }

    public String getLimits()
    {
        Vector3f low = new Vector3f(xLimitLow, yLimitLow, zLimitLow);
        Vector3f high = new Vector3f(xLimitHigh, yLimitHigh, zLimitHigh);
        return low.toString() + " " + high.toString();
    }

    public String movementInfo()
    {
        return "Position = " + currentPosition.toString();
    }

    public void translate(Vector3f offset)
    {
    }
}
