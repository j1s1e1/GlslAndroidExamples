package com.tutorial.glsltutorials.tutorials.View;

import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by Jamie on 6/7/14.
 */
public class ViewData {
    public Vector3f targetPos;	///<The starting target position position.
    public Quaternion orient;		///<The initial orientation aroudn the target position.
    public float radius;			///<The initial radius of the camera from the target point.
    public float degSpinRotation;	///<The initial spin rotation of the "up" axis, relative to \a orient
    public float radSpinRotation;
    public ViewData(Vector3f t, Quaternion o, float r, float d)
    {
        targetPos = t;
        orient = o;
        radius = r;
        degSpinRotation = d;
        radSpinRotation = d * (float)Math.PI / 180f;
    }
}
