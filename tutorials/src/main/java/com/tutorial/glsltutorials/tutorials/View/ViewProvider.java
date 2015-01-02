package com.tutorial.glsltutorials.tutorials.View;

import android.graphics.Point;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;

/**
 * Created by Jamie on 6/7/14.
 */
public abstract class ViewProvider implements IPole
{
    public abstract Matrix4f CalcMatrix();
    public abstract void mouseButton(int button, int state, int x, int y);
    public abstract void mouseButton(int button, int state, Point p);
    public abstract void mouseClick(MouseButtons eButton, boolean glut_down, int modifiers, Point p);
    public abstract void mouseMove(Point position);
    public abstract void mouseWheel(int wheel, int direction, Point p);
}
