package com.tutorial.glsltutorials.tutorials.Creatures;

import android.graphics.Color;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Shapes.Sphere;
import com.tutorial.glsltutorials.tutorials.Shapes.Triangle3d;

/**
 * Created by Jamie on 4/30/14.
 */
public class DragonFly extends Bug {

        float abs_x_step = 0.05f;
        float abs_y_direction = 0.04f;
        int direction = 0;
        float x_step = abs_x_step;
        float y_direction = abs_y_direction;
        int last_direction = 0;
        int new_direction_count = 0;
        int wing_step = 0;
        float wing_offset = 0;
        float wing_length = 0.100f;

        Sphere[] body;
        Triangle3d[] wings;

        public DragonFly(int x_in, int y_in, int z_in)
        {
            super(x_in, y_in, z_in);
            body = new Sphere[6];
            body[0] = new Sphere(0.05f, 2);
            body[1] = new Sphere(0.04f, 2);
            body[2] = new Sphere(0.04f, 2);
            body[3] = new Sphere(0.04f, 2);
            body[4] = new Sphere(0.04f, 2);
            body[5] = new Sphere(0.03f, 2);
            body[0].solidColor(Color.GREEN);
            body[1].multiColor(new int[]{Colors.Green, Colors.DarkOliveGreen});
            body[2].multiColor(new int[]{Colors.Green, Colors.LimeGreen});
            body[3].multiColor(new int[]{Colors.Green, Colors.DarkOliveGreen});
            body[4].multiColor(new int[]{Colors.Green, Colors.LimeGreen});
            body[5].solidColor(Colors.LimeGreen);
            wings = new Triangle3d[2];
            Vector3f a = new Vector3f(x - x_step, y + wing_offset + wing_length, z + 0.05f);
            Vector3f b = new Vector3f(x, y + wing_offset, z);
            Vector3f c = new Vector3f(x + x_step, y + wing_offset + wing_length, z -  0.05f);
            wings[0] = new Triangle3d(a, b, c, true);
            wings[0].solidColor(Colors.LimeGreen);
            Vector3f d = new Vector3f(x -x_step, y - wing_offset - wing_length, z -  0.05f);
            Vector3f e = new Vector3f(x, y - wing_offset, z);
            Vector3f f = new Vector3f(x + x_step, y - wing_offset - wing_length, z +  0.05f);
            wings[1] = new Triangle3d(d,e,f, true);
            wings[1].solidColor(Colors.LimeGreen);
            setOffsets();
        }

    private void setOffsets()
    {
        body[0].setOffset(x - x_step, y, z);
        body[1].setOffset(x, y + y_direction, z);
        body[2].setOffset(x + 1 * x_step, y + 2 * y_direction, z);
        body[3].setOffset(x + 2 * x_step, y + 3 * y_direction, z);
        body[4].setOffset(x + 3 * x_step, y + 4 * y_direction, z);
        body[5].setOffset(x, y + wing_offset / 2000, z + 0.100f);
        wings[0].setOffset(x, y, z);
        wings[1].setOffset(x, y, z);
    }

    private void setRotations()
    {
        float rotation = (x % 256) - 128;
        Shape.global_y_rotate = rotation;
        wings[0].setAngles(90 + wing_offset / 5, 0, 0);
        wings[1].setAngles(90 - wing_offset / 5, 0, 0);
			/*
			body[0].SetAngles(0, rotation, 0);
			body[1].SetAngles(0, rotation, 0);;
			body[2].SetAngles(0, rotation, 0);
			body[3].SetAngles(0, rotation, 0);
			body[4].SetAngles(0, rotation, 0);
			body[5].SetAngles(0, rotation, 0);
			*/
    }

    @Override
    public void draw()
    {
        x_step = abs_x_step;
        if (direction == 1)
        {
            x_step = -abs_x_step;
        }
        //GraphicsClass.DrawTriangle(Color.LimeGreen, x + x_step, y  + y_direction, x , y + wing_offset, x + 2 * x_step, y + wing_offset);
        switch(wing_step)
        {
            case -40:  wing_step = -30; break;
            case -30:  wing_step = -20; break;
            case -20:  wing_step = -10; break;
            case -10:  wing_step = 0; break;
            case 0:  wing_step = 10; break;
            case 10: wing_step = 20; break;
            case 20: wing_step = 30; break;
            case 30: wing_step = 40; break;
            case 40: wing_step = -40; break;
        }
        wing_offset = wing_step/500f;
        setOffsets();
        setRotations();
        y_direction = 0.0f;
        body[0].draw();
        body[1].draw();
        body[2].draw();
        body[3].draw();
        body[4].draw();
        //body[5].Draw();
        wings[0].draw();
        wings[1].draw();
    }

    public void keyPress(int keyCode, KeyEvent event)
    {
        switch (keyCode) {
            case KeyEvent.KEYCODE_8:
            {
                if (y < 240) {
                    y += 2;
                    y_direction = -abs_y_direction;
                }
                break;
            }
            case KeyEvent.KEYCODE_2:
            {
                if (y > -240) {
                    y -= 2;
                    y_direction = abs_y_direction;
                }
                break;
            }
        }
    }

    public void newWorldPosition(int wp)
    {
        x = wp;
        //ShapeClass.global_x_offset = wp;
    }

    public void move(float x_in, float y_in, float z_in)
    {
        x = x + x_in / 100f;
        if (y_in < 0)
        {
            if (y > -240) {
                y -= 0.02f;
                y_direction = abs_y_direction;
            }
        }
        if (y_in > 0)
        {
            if (y < 240) {
                y += 0.02f;
                y_direction = -abs_y_direction;
            }
        }
    }

    public void rotateShapes(Vector3f rotationAxis, float angle)
    {

    }
}

