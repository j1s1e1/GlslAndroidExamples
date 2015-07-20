package com.tutorial.glsltutorials.tutorials.Creatures;

import android.graphics.Color;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Movement.BugMovement2D;

import java.util.Random;

/**
 * Created by jamie on 6/13/15.
 */
public class Bug3d extends Animal {
    protected Vector3f lastPosition = new Vector3f();
    protected Vector3f position = new Vector3f();
    protected float x;
    protected float y;
    protected float z;
    protected int wing_step = 0;
    protected float wing_angle = 0;
    public int size = 25;
    public float[] color = Colors.RED_COLOR;
    boolean alive;
    protected int direction = 0;
    public static int player_x;
    public static int player_y;
    public static int player_distance = 10;

    static protected Random random = new Random();

    protected float scale = 0.005f;
    protected Vector3f speed;

    public Bug3d (int x_in, int y_in, int z_in)
    {
        x = x_in;
        y = y_in;
        z = z_in;
        alive = true;
        lastPosition = new Vector3f(x, y, z);
        position = lastPosition;
        speed = new Vector3f(scale, scale, scale);

        movement = new BugMovement2D(speed);
    }

    private void drawBug()
    {
    }

    public void draw()
    {
        if (alive == true)
        {
            drawBug();
        }
    }

    private void chaseCheck()
    {
        /*
            int x_dif = Math.Abs(x - player_x);
            if (x_dif > last_frame_offset) x_dif = x_dif - last_frame_offset;
            int y_dif = Math.Abs(y - player_y);
            if (x_dif < 100)
            {
                speed = 2;
                if (x_dif < y_dif)
                {
                    if (x > player_x)
                    {
                       direction = 2;
                    }
                    else
                    {
                        direction = 1;
                    }
                }
                else
                {
                    if (y > player_y)
                    {
                        direction = 4;
                    }
                    else
                    {
                        direction = 3;
                    }
                }
            }
            */
    }

    private void random_Move()
    {
        lastPosition = position;
        position = movement.newOffset(position);
        x = position.x;
        y = position.y;
        z = position.z;
        if (movement instanceof BugMovement2D)
        {
            direction = ((BugMovement2D)movement).getDirection();
        }
        chaseCheck();
    }

    private void player_Hit()
    {
        	/*
            if (InLastFrame())
            {
                if (Math.Abs (x - player_x - last_frame_offset) < player_distance)
                {
                    if (Math.Abs (y - player_y) < player_distance)
                    {
                        alive = false;
                    }
                }
                if (Math.Abs (x - player_x + last_frame_offset) < player_distance)
                {
                    if (Math.Abs (y - player_y) < player_distance)
                    {
                        alive = false;
                    }
                }
            }
            if (Math.Abs (x - player_x) < player_distance)
            {
                if (Math.Abs (y - player_y) < player_distance)
                {
                    alive = false;
                }
            }
            */
    }

    public void move()
    {
        if (alive)
        {
            player_Hit ();
            random_Move ();
        }
    }

    public void setBug2DMovement()
    {
        movement = new BugMovement2D(speed);
    }
}
