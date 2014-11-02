package com.tutorial.glsltutorials.tutorials.Creatures;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.Random;

/**
 * Created by jamie on 10/20/14.
 */
public class RandomMovement {
    public RandomMovement ()
    {
    }

    static Random random = new Random();

    float xLimitLow = -1.0f;
    float yLimitLow = -1.0f;
    float zLimitLow = 0f;

    float xLimitHigh = 1.0f;
    float yLimitHigh = 1.0f;
    float zLimitHigh = 1.0f;

    float maxXmovement = 0.1f;
    float maxYmovement = 0.1f;
    float maxZmovement = 0.1f;

    private float newValue(float oldValue, float maxMovement, float lowLimit, float highLimit)
    {
        oldValue = oldValue + random.nextInt(100)/100f * 2f * maxMovement - maxMovement;
        if (oldValue < lowLimit) oldValue = lowLimit;
        if (oldValue > highLimit) oldValue = highLimit;
        return oldValue;
    }

    public Vector3f newOffset(Vector3f oldOffset)
    {
        oldOffset.x = newValue(oldOffset.x, maxXmovement, xLimitLow, xLimitHigh);
        oldOffset.y = newValue(oldOffset.y, maxYmovement, yLimitLow, yLimitHigh);
        oldOffset.z = newValue(oldOffset.z, maxZmovement, zLimitLow, zLimitHigh);
        return oldOffset;
    }
}
