package com.tutorial.glsltutorials.tutorials.View;

import android.graphics.Point;

/**
 * Created by Jamie on 6/7/14.
 */
public interface IPole {

    void mouseButton(int button, int state, int x, int y);
    void mouseButton(int button, int state, Point p);
    public void mouseClick(MouseButtons eButton, boolean glut_down, int modifiers, Point p);
    public void mouseMove(Point position);
    public void mouseWheel(int wheel, int direction, Point p);
}
