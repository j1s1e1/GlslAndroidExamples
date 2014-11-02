package com.tutorial.glsltutorials.tutorials.Creatures;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by jamie on 10/20/14.
 */
public class Collisions {
    public Collisions ()
    {
    }

    float collisionDistance = 0.05f;

    private boolean detectCollision(Vector3f position1, Vector3f position2)
    {
        if (position1.sub(position2).length() < collisionDistance)
            return true;
        else
            return false;
    }

    public boolean detectColisions(Vector3f position, Vector3f[] otherPositions)
    {
        for (Vector3f v : otherPositions)
        {
            if (detectCollision(position, v))
                return true;
        }
        return false;
    }
}
