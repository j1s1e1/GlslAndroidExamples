package com.tutorial.glsltutorials.tutorials.Movement;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.Random;

/**
 * Created by jamie on 7/19/15.
 */
public class BugMovement2D extends Movement {
    Random random = new Random();
    int direction = 0;
    int move_count = 0;
    int repeat_count = 0;
    int repeat_limit = 50;

    public BugMovement2D(Vector3f speedIn)
    {
        speed = speedIn;
    }

    public Vector3f newOffset(Vector3f oldOffset)
    {
        if (repeat_count < repeat_limit)
        {
            repeat_count++;
        }
        else
        {
            direction = random.nextInt(5);
            repeat_count = random.nextInt(repeat_limit/2);
        }
        if (direction == 1)
        {
            oldOffset.x = oldOffset.x - speed.x;
            if (oldOffset.x < xLimitLow) repeat_count = repeat_limit;
        }
        if (direction == 2)
        {
            oldOffset.x = oldOffset.x + speed.x;
            if (oldOffset.x > xLimitHigh) repeat_count = repeat_limit;
        }
        if (direction == 3)
        {
            oldOffset.y = oldOffset.y - speed.y;
            if (oldOffset.y < yLimitLow) repeat_count = repeat_limit;
        }
        if (direction == 4)
        {
            oldOffset.y = oldOffset.y + speed.y;
            if (oldOffset.y > yLimitHigh) repeat_count = repeat_limit;
        }
        move_count++;
        currentPosition = oldOffset;
        return oldOffset;
    }

    public int getDirection()
    {
        return direction;
    }
}
