package com.tutorial.glsltutorials.tutorials.Movement;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector2f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jamie on 11/29/14.
 */
public class TouchMovement extends Movement {
    public TouchMovement()
    {
    }

    float xSpeed = 0f;
    float ySpeed = 0f;
    float zSpeed = 0f;

    Vector2f lastTouch = new Vector2f(0f, 0f);

    private float newValue(float oldValue, float maxMovement, float lowLimit,
                           float highLimit, float speed, float newValue)
    {
        oldValue = newValue;
        if (oldValue < lowLimit) oldValue = lowLimit;
        if (oldValue > highLimit) oldValue = highLimit;
        return oldValue;
    }

    public Vector3f newOffset(Vector3f oldOffset)
    {
        oldOffset.x = newValue(oldOffset.x, maxXmovement, xLimitLow, xLimitHigh, xSpeed, lastTouch.x);
        oldOffset.y = newValue(oldOffset.y, maxYmovement, yLimitLow, yLimitHigh, ySpeed, lastTouch.y);
        oldOffset.z = newValue(oldOffset.z, maxZmovement, zLimitLow, zLimitHigh, zSpeed, oldOffset.z);
        return oldOffset;
    }

    public void setLastTouch(Vector2f newTouch) {
        lastTouch = newTouch;
    }
}
