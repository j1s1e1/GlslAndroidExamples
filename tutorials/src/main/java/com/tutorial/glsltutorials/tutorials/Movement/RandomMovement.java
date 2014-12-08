package com.tutorial.glsltutorials.tutorials.Movement;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.Random;

/**
 * Created by jamie on 10/20/14.
 */
public class RandomMovement extends Movement {
    public RandomMovement ()
    {
    }

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
