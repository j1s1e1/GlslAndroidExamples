package com.tutorial.glsltutorials.tutorials.Movement;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Objects.Paddle;

import java.util.ArrayList;

/**
 * Created by jamie on 11/29/14.
 */
public class ElasticMovement extends Movement {
    public ElasticMovement()
    {
        otherObjects = new ArrayList<Paddle>();
        speed = new Vector3f(minSpeed + random.nextInt(100)/100f, minSpeed + random.nextInt(100)/100f,
                minSpeed + random.nextInt(100)/100f);
    }

    ArrayList<Paddle> otherObjects;
    float minSpeed = 0.25f;

    private float newValue(float oldValue, float maxMovement, float lowLimit,
                           float highLimit, float speed)
    {
        oldValue = oldValue + speed * maxMovement;
        if (oldValue < lowLimit) oldValue = lowLimit;
        if (oldValue > highLimit) oldValue = highLimit;
        return oldValue;
    }

    private void bounce(Vector3f oldOffset)
    {
        for (Paddle p : otherObjects)
        {
            Vector3f difference = oldOffset.sub(p.getOffset());
            if (difference.length() < 0.05f)
            {
                speed.y = -speed.y;
                return;
            }
        }
        if (oldOffset.x == xLimitLow) speed.x = Math.abs(speed.x);
        if (oldOffset.y == yLimitLow) speed.y = Math.abs(speed.y);
        if (oldOffset.z == zLimitLow) speed.z = Math.abs(speed.z);
        if (oldOffset.x == xLimitHigh) speed.x = -Math.abs(speed.x);
        if (oldOffset.y == yLimitHigh) speed.y = -Math.abs(speed.y);
        if (oldOffset.z == zLimitHigh) speed.z = -Math.abs(speed.z);

    }

    public Vector3f newOffset(Vector3f oldOffset)
    {
        oldOffset.x = newValue(oldOffset.x, maxXmovement, xLimitLow, xLimitHigh, speed.x);
        oldOffset.y = newValue(oldOffset.y, maxYmovement, yLimitLow, yLimitHigh, speed.y);
        oldOffset.z = newValue(oldOffset.z, maxZmovement, zLimitLow, zLimitHigh, speed.z);
        bounce(oldOffset);
        return oldOffset;
    }

    public void setPaddles(ArrayList<Paddle> paddles)
    {
        otherObjects = paddles;
    }

}
