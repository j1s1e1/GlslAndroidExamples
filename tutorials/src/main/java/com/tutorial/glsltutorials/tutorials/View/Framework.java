package com.tutorial.glsltutorials.tutorials.View;

import android.graphics.Point;

/**
 * Created by Jamie on 6/7/14.
 */
public class Framework {
    static int GLUT_ACTIVE_SHIFT = 0x0001;
    static int GLUT_ACTIVE_CTRL  = 0x0002;
    static int GLUT_ACTIVE_ALT   = 0x0004;
    static int modifiers = 0;
    public static void forwardMouseMotion(IPole forward, int x, int y)
    {
        forward.mouseMove(new Point(x, y));
    }

    public static void forwardMouseButton(IPole forward, int button, int state, int x, int y)
    {
        forward.mouseButton(button, state, new Point(x, y));
    }

    public static void forwardMouseWheel(IPole forward, int wheel, int direction, int x, int y)
    {
        forward.mouseWheel(direction, modifiers, new Point(x, y));
    }

}
