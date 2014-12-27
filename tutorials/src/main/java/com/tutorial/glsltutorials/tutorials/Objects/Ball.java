package com.tutorial.glsltutorials.tutorials.Objects;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Creatures.Collisions;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Movement.ElasticMovement;
import com.tutorial.glsltutorials.tutorials.Movement.KeyboardMovement;
import com.tutorial.glsltutorials.tutorials.Movement.Movement;
import com.tutorial.glsltutorials.tutorials.Movement.RandomMovement;
import com.tutorial.glsltutorials.tutorials.Movement.SocketMovement;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jamie on 11/29/14.
 */
public class Ball {
    LitMatrixSphere2 body;
    static Random random = new Random();
    Movement movement = new ElasticMovement();
    Collisions collision = new Collisions();
    int framesPerMove = 10;
    int frameCount;
    float scale = 1f;
    boolean dead = false;
    Vector3f lowLimits = new Vector3f(-1f, -1f, 0f);
    Vector3f highLimits = new Vector3f(1f, 1f, 1f);
    ArrayList<Paddle> otherObjects;
    public Ball()
    {
        otherObjects = new ArrayList<Paddle>();
        body = new LitMatrixSphere2(0.05f);
        float xOffset = 0f; // random.Next(20)/10f - 1f;
        float yOffset = 0f; // random.Next(20)/10f - 1f;
        float zOffset = 0f; // random.Next(10)/10f - 0.5f;
        int colorSelection = random.nextInt(3);
        switch (colorSelection)
        {
            case 0:
                body.setColor(Colors.RED_COLOR);
                break;
            case 1:
                body.setColor(Colors.GREEN_COLOR);
                break;
            case 2: body.setColor(Colors.BLUE_COLOR);
                break;
            default:
                body.setColor(Colors.YELLOW_COLOR);
                break;
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
            body.setOffset(movement.newOffset(body.getOffset()));
        }
    }

    public void fireOn(ArrayList<Missle> missles)
    {
        for (Missle m : missles)
        {
            if (collision.detectColisions(body.getOffset(), m.GetOffsets()))
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

    public void setRandomControl()
    {
        movement = new RandomMovement();
        movement.setLimits(lowLimits, highLimits);
    }

    public void setKeyboardControl()
    {
        movement = new KeyboardMovement();
        movement.setLimits(lowLimits, highLimits);
    }

    public void setSocketControl()
    {
        movement = new SocketMovement();
        movement.setLimits(lowLimits, highLimits);
    }

    public void keyboard(int keyCode)
    {
        if (movement instanceof KeyboardMovement)
        {
            KeyboardMovement keyboardMovement = (KeyboardMovement) movement;
            keyboardMovement.keyboard(keyCode);
        }
    }

    public void setLimits(Vector3f low, Vector3f high)
    {
        lowLimits = low;
        highLimits = high;
        movement.setLimits(lowLimits, highLimits);
    }

    public Vector3f getOffset()
    {
        return body.getOffset();
    }

    public void addPaddle(Paddle paddle)
    {
        otherObjects.add(paddle);
        ElasticMovement em = (ElasticMovement) movement;
        em.setPaddles(otherObjects);
    }
}