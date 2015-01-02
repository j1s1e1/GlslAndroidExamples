package com.tutorial.glsltutorials.tutorials.View;

import android.graphics.Point;

import com.tutorial.glsltutorials.tutorials.MouseButtons;

/**
 * Created by Jamie on 6/7/14.
 */
public interface IPole {

    void MouseButton(int button, int state, int x, int y);
    void MouseButton(int button, int state, Point p);
    public void MouseClick(MouseButtons eButton, boolean glut_down, int modifiers, Point p);
    public void MouseMove(Point position);
    public void MouseWheel(int wheel, int direction, Point p);
}
