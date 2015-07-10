package com.tutorial.glsltutorials.tutorials.Creatures;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;

/**
 * Created by jamie on 6/13/15.
 */
public class LadyBug3d extends Bug3d {
    LitMatrixSphere2 sphere;
    LitMatrixSphere2[] wings;

    public LadyBug3d()
    {
        this(0, 0, 0);
    }

    public LadyBug3d(int x_in, int y_in, int z_in)
    {
      super(x_in, y_in, z_in);
        sphere = new LitMatrixSphere2(scale * 20, 2);
        sphere.setColor(Colors.PINK_COLOR);
        wings = new LitMatrixSphere2[2];
        wings[0] = new LitMatrixSphere2(scale * 30, 2);
        wings[1] = new LitMatrixSphere2(scale * 30, 2);
        wings[0].setColor(Colors.RED_COLOR);
        wings[1].setColor(Colors.RED_COLOR);
        setOffsets();
    }

    private void setOffsets()
    {
        sphere.setOffset(x, y, z);
        wings[0].setOffset(x, y - wing_angle, z);
        wings[1].setOffset(x, y + wing_angle, z);
        wings[0].setAngles(0, 0, -wing_angle * 10);
        wings[1].setAngles(80, 0, wing_angle * 10);
    }

    public void draw() {
        if (wing_step < 4) {
            wing_step++;
        } else {
            wing_step = 0;
        }
        wing_angle = wing_step * 5 * scale;
        setOffsets();
        sphere.draw();
        wings[0].drawSemi(0, 7);
        wings[1].drawSemi(0, 7);
        if (autoMove) {
            move();
        }
    }
}
