package com.tutorial.glsltutorials.tutorials.Creatures;

import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;

/**
 * Created by jamie on 6/13/15.
 */
public class DragonFly3d extends Bug3d {
    int x_step = 0;
    float x_offset = 0f;
    float y_direction = 0;
    float wing_offset = 0;

    LitMatrixSphere2[] body;

    public DragonFly3d()
    {
        this(0, 0, 0);
    }

    public DragonFly3d(int x_in, int y_in, int z_in)
    {
        super(x_in, y_in, z_in);
        scale = 0.005f;
        body = new LitMatrixSphere2[6];
        body[0] = new LitMatrixSphere2(scale * 20, 2);
        body[1] = new LitMatrixSphere2(scale * 15, 2);
        body[2] = new LitMatrixSphere2(scale * 15, 2);
        body[3] = new LitMatrixSphere2(scale * 15, 2);
        body[4] = new LitMatrixSphere2(scale * 15, 2);
        body[5] = new LitMatrixSphere2(scale * 14, 2);
        body[0].setColor(Colors.GREEN_COLOR);
        body[1].setColor(Colors.GREEN_COLOR);
        body[2].setColor(Colors.GREEN_COLOR);
        body[3].setColor(Colors.GREEN_COLOR);
        body[4].setColor(Colors.GREEN_COLOR);
        body[5].setColor(Colors.LIME_GREEN_COLOR);

        setOffsets();
    }

    private void setOffsets()
    {
        body[0].setOffset(x, y, z);
        body[1].setOffset(x + x_offset, y + y_direction, z);
        body[2].setOffset(x + 2 * x_offset, y + 2 * y_direction, z);
        body[3].setOffset(x + 3 * x_offset, y + 3 * y_direction, z);
        body[4].setOffset(x + 4 * x_offset, y + 4 * y_direction, z);
        body[5].setOffset(x + x_offset, y + y_direction + wing_offset, z);
    }

    public void draw()
    {
        x_step = 20;
        if (direction == 1)
        {
            x_step = -20;
        }
        x_offset = scale * x_step;
        // FIXME GraphicsClass.drawTriangle(Color.LimeGreen, x + x_step, y  + y_direction, x , y + wing_offset, x + 2 * x_step, y + wing_offset);
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
        wing_offset = scale * wing_step;
        setOffsets();
        y_direction = 0;
        for (int i = 0; i < body.length; i++)
        {
            body[i].draw();
        }
        if (autoMove)
        {
            move();
            switch (direction)
            {
                case 3: y_direction = scale * 5; break;
                case 4: y_direction = scale * -5; break;
                default: y_direction = 0; break;
            }
        }
    }

    public void keyPress(int key)
    {
        switch (key) {
            case KeyEvent.KEYCODE_NUMPAD_8:
            {
                if (y < 500) {
                    y += 2;
                    y_direction = -5;
                }
                break;
            }
            case KeyEvent.KEYCODE_NUMPAD_2:
            {
                if (y > 12) {
                    y -= 2;
                    y_direction = 5;
                }
                break;
            }
        }
    }

    public void newWorldPosition(int wp)
    {
        if (wp > x - 256) {
            direction = 1;
        }
        if (wp < x - 256) {
            direction = 2;
        }
        x = wp + 256;
    }

}
