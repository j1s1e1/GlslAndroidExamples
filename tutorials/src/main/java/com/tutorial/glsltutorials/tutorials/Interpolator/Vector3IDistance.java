package com.tutorial.glsltutorials.tutorials.Interpolator;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by jamie on 1/2/15.
 */
public class Vector3IDistance implements IDistance<Vector3IDistance>, ILinearInterpolate<Vector3IDistance> {
    Vector3f vector3Value;

    public Vector3IDistance(float x, float y, float z)
    {
        vector3Value = new Vector3f(x, y, z);
    }

    public Vector3IDistance(Vector3f vector3ValueIn)
    {
        vector3Value = vector3ValueIn;
    }

    public Vector3f GetValue()
    {
        return vector3Value;
    }

    public float distance(Vector3IDistance a, Vector3IDistance b)
    {
        return Vector3f.distance(a.GetValue(), b.GetValue());
    }

    public Vector3IDistance linearInterpolate(Vector3IDistance a, Vector3IDistance b, float sectionAlpha)
    {
        Vector3f interpolatedValue = a.GetValue().mul((1f - sectionAlpha)).add(b.GetValue().mul(sectionAlpha));
        Vector3IDistance result = new Vector3IDistance(interpolatedValue);
        return result;
    }
}
