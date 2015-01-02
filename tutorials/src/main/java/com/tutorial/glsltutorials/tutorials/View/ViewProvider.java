package com.tutorial.glsltutorials.tutorials.View;

import android.graphics.Point;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.MouseButtons;

/**
 * Created by Jamie on 6/7/14.
 */
public abstract class ViewProvider implements IPole
{
    public abstract Matrix4f CalcMatrix();
    public abstract void MouseButton(int button, int state, int x, int y);
    public abstract void MouseButton(int button, int state, Point p);
    public abstract void MouseClick(MouseButtons eButton, boolean glut_down, int modifiers, Point p);
    public abstract void MouseMove(Point position);
    public abstract void MouseWheel(int wheel, int direction, Point p);
}
