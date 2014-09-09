package com.tutorial.glsltutorials.tutorials;

import android.graphics.Point;

/**
 * Created by Jamie on 6/7/14.
 */
public class Pole {
    public Pole()
    {
    }

    public void MouseButton(int button, int state, int x, int y)
    {
        MouseButton(button, state, new Point(x,y));
    }

    public void MouseButton(int button, int state, Point p)
    {
    }

    public void MouseClick(MouseButtons eButton, boolean glut_down, int modifiers, Point p)
    {
    }

    public void MouseMove(Point position)
    {
    }

    public void MouseWheel(int wheel, int direction, Point p)
    {
    }

    void ForwardMouseMotion(Pole forward, int x, int y)
    {
        forward.MouseMove(new Point(x, y));
    }
}
