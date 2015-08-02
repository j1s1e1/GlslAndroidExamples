package com.tutorial.glsltutorials.tutorials.Movement;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.Random;

/**
 * Created by jamie on 7/19/15.
 */
public class BugMovement3D extends Movement {
    Random random = new Random();
    int move_count = 0;
    int repeat_count = 0;
    int repeat_limit = 50;

    public BugMovement3D(Vector3f speedLimitIn)
    {
        speedLimit = speedLimitIn;
        speed = new Vector3f(newSpeed(speedLimit.x), newSpeed(speedLimit.y), newSpeed(speedLimit.z));
    }

    private float newSpeed(float limit)
    {
        return random.nextFloat() * limit * 2f - limit;
    }

    public Vector3f newOffset(Vector3f oldOffset)
    {
        if (repeat_count < repeat_limit)
        {
            repeat_count++;
        }
        else
        {
            speed = new Vector3f(newSpeed(speedLimit.x), newSpeed(speedLimit.y), newSpeed(speedLimit.z));
            repeat_count = random.nextInt(repeat_limit/2);
        }
        oldOffset.x = oldOffset.x + speed.x;
        if (oldOffset.x < xLimitLow) oldOffset.x = xLimitLow;
        if (oldOffset.x > xLimitHigh) oldOffset.x = xLimitHigh;
        oldOffset.y = oldOffset.y + speed.y;
        if (oldOffset.y < yLimitLow) oldOffset.y = yLimitLow;
        if (oldOffset.y > yLimitHigh) oldOffset.y = yLimitHigh;
        oldOffset.z = oldOffset.z + speed.z;
        if (oldOffset.z < zLimitLow) oldOffset.z = zLimitLow;
        if (oldOffset.z > zLimitHigh) oldOffset.z = zLimitHigh;
        move_count++;
        currentPosition = oldOffset;
        return oldOffset;
    }
}
