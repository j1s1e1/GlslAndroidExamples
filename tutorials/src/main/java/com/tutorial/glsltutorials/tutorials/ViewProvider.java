package com.tutorial.glsltutorials.tutorials;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;

/**
 * Created by Jamie on 6/7/14.
 */
public class ViewProvider extends ViewPole {
    public ViewProvider(ViewData initialView, ViewScale viewScale)
    {
        this(initialView, viewScale, MouseButtons.MB_LEFT_BTN, false);
    }

    public ViewProvider(ViewData initialView, ViewScale viewScale, MouseButtons actionButton)
    {
        this(initialView, viewScale, actionButton, false);
    }

    public ViewProvider(ViewData initialView, ViewScale viewScale, MouseButtons actionButton,
        boolean bRightKeyboardCtrls)
    {
        super(initialView, viewScale, actionButton, bRightKeyboardCtrls);
    }
    ///Computes the camera matrix.
    public Matrix4f CalcMatrix()
    {
        Matrix4f matrix = new Matrix4f();
        return matrix;
    }
}
