package com.tutorial.glsltutorials.tutorials.Creatures;

import android.graphics.Color;

import com.tutorial.glsltutorials.tutorials.Colors;

import java.util.Random;

/**
 * Created by jamie on 6/13/15.
 */
public class Bug3d extends Animal {
    protected float x;
    protected float y;
    protected float z;
    protected int wing_step = 0;
    protected float wing_angle = 0;
    public int size = 25;
    public float[] color = Colors.RED_COLOR;
    boolean alive;
    static protected Random random = new Random();

    protected float scale = 0.005f;
    protected boolean autoMove = true;

    public Bug3d (int x_in, int y_in, int z_in)
    {
        x = x_in;
        y = y_in;
        z = z_in;
        alive = true;
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

    int move_count = 0;
    protected int direction = 0;

    private void SI_Move()
    {
        switch (move_count)
        {
            case   0:
                direction = 1;
                break;
            case  50:
                direction = 3;
                break;
            case  60:
                direction = 2;
                break;
            case  160:
                direction = 3;
                break;
            case  170:
                direction = 1;
                break;
            case  270:
                direction = 3;
                break;
            case  280:
                direction = 2;
                break;
            case  380:
                direction = 3;
                break;
            case  390:
                direction = 0;
                break;
        }
        if (direction == 1)
        {
            x = x - speed;
        }
        if (direction == 2)
        {
            x = x + speed;
        }
        if (direction == 3)
        {
            y = y - speed;
        }
        if (direction == 4)
        {
            y = y + speed;
        }
        move_count++;
    }

    int repeat_count = 0;
    int repeat_limit = 50;
    float x_low = -1;
    float x_high = 1;
    float y_low = -1;
    float y_high = 1;

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

    float speed = 1;

    private void random_Move()
    {
        speed = scale;
        if (repeat_count < repeat_limit)
        {
            repeat_count++;
            chaseCheck();
        }
        else
        {
            direction = random.nextInt(5);
            repeat_count = random.nextInt(repeat_limit/2);
        }
        if (direction == 1)
        {
            x = x - speed;
            if (x < x_low) repeat_count = repeat_limit;
        }
        if (direction == 2)
        {
            x = x + speed;
            if (x > x_high) repeat_count = repeat_limit;
        }
        if (direction == 3)
        {
            y = y - speed;
            if (y < y_low) repeat_count = repeat_limit;
        }
        if (direction == 4)
        {
            y = y + speed;
            if (y > y_high) repeat_count = repeat_limit;
        }
        move_count++;
    }

    public bug_move_option_enum bug_move_option = bug_move_option_enum.LADYBUG;

    public static int player_x;
    public static int player_y;

    public static int player_distance = 10;

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
            switch(bug_move_option)
            {
                case LADYBUG:
                    player_Hit ();
                    random_Move ();
                    break;
                default:
                    player_Hit();
                    random_Move();
                    break;
            }
        }
    }
}
