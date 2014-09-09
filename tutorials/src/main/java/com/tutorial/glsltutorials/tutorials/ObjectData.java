package com.tutorial.glsltutorials.tutorials;

import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by Jamie on 6/7/14.
 */
public class ObjectData {
    public Vector3f position;			///<The world-space position of the object.
    public Quaternion orientation;		///<The world-space orientation of the object.
    public ObjectData(Vector3f p, Quaternion o)
    {
        position = p;
        orientation = o;
    }
}
