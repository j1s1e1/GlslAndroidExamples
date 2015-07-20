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

    float x_low = -1;
    float x_high = 1;
    float y_low = -1;
    float y_high = 1;

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
            if (oldOffset.x < x_low) repeat_count = repeat_limit;
        }
        if (direction == 2)
        {
            oldOffset.x = oldOffset.x + speed.x;
            if (oldOffset.x > x_high) repeat_count = repeat_limit;
        }
        if (direction == 3)
        {
            oldOffset.y = oldOffset.y - speed.y;
            if (oldOffset.y < y_low) repeat_count = repeat_limit;
        }
        if (direction == 4)
        {
            oldOffset.y = oldOffset.y + speed.y;
            if (oldOffset.y > y_high) repeat_count = repeat_limit;
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
