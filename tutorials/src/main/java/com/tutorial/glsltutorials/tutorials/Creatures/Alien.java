package com.tutorial.glsltutorials.tutorials.Creatures;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Objects.Missle;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jamie on 10/20/14.
 */
public class Alien {
    LitMatrixSphere2 body;
    float radius = 0.05f;
    static Random random = new Random();
    RandomMovement movement = new RandomMovement();
    Collisions collision = new Collisions();
    int framesPerMove = 10;
    int frameCount;
    float scale = 0.5f;
    boolean dead = false;
    public Alien ()
    {
        body = new LitMatrixSphere2(radius);
        float xOffset = random.nextInt(20)/10f - 1f;
        float yOffset = random.nextInt(20)/10f - 1f;
        float zOffset = random.nextInt(10)/10f - 0.5f;
        int colorSelection = random.nextInt(3);
        switch (colorSelection)
        {
            case 0:	body.setColor(Colors.RED_COLOR); break;
            case 1: body.setColor(Colors.GREEN_COLOR); break;
            case 2: body.setColor(Colors.BLUE_COLOR); break;
            default: body.setColor(Colors.YELLOW_COLOR); break;
        }
        xOffset = xOffset * scale;
        yOffset = yOffset * scale;
        zOffset = zOffset * scale;
        body.setOffset(new Vector3f(xOffset, yOffset, zOffset));
    }

    public boolean isDead()
    {
        return dead;
    }

    public void draw()
    {
        body.draw();
        if (frameCount < framesPerMove)
        {
            frameCount++;
        }
        else
        {
            frameCount = 0;
            body.setOffset(movement.newOffset(body.GetOffset()));
        }
    }

    public void fireOn(ArrayList<Missle> missles)
    {
        for (Missle m : missles)
        {
            if (collision.detectColisions(body.GetOffset(), m.GetOffsets()))
            {
                dead = true;
                break;
            }
        }
    }

    public void setProgram(int newProgram)
    {
        body.setProgram(newProgram);
    }
}
