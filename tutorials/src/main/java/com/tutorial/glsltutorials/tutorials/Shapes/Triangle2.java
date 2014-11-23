package com.tutorial.glsltutorials.tutorials.Shapes;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.ArrayList;

/**
 * Created by jamie on 11/23/14.
 */
public class Triangle2 {
    public Triangle2()
    {
        //this(new Vector3(1f, 0f, 0f), new Vector3(0f, 1f, 0f), new Vector3(0f, 0f, 1f));
    }

    public Triangle2(Vector3f aIn, Vector3f bIn, Vector3f cIn)
    {
        a = aIn;
        b = bIn;
        c = cIn;
    }

    Vector3f a;
    Vector3f b;
    Vector3f c;

    public ArrayList<Triangle2> Divide()
    {
        ArrayList<Triangle2> result = new ArrayList<Triangle2>();
        Vector3f midpointOne = (a.add(b)).divide(2);
        Vector3f midpointTwo = (b.add(c)).divide(2);
        Vector3f midpointThree = (c.add(a)).divide(2);
        result.add(new Triangle2(a, midpointOne, midpointThree));
        result.add(new Triangle2(midpointOne, midpointTwo, midpointThree));
        result.add(new Triangle2(midpointThree, midpointTwo, c));
        result.add(new Triangle2(midpointOne, b, midpointTwo));
        return result;
    }

    public float[] GetFloats()
    {
        float[] result = new float[9];
        System.arraycopy(a.toArray(), 0, result, 0, 3);
        System.arraycopy(b.toArray(), 0, result, 3, 3);
        System.arraycopy(c.toArray(), 0, result, 6, 3);
        return result;
    }

    public Triangle2 Clone()
    {
        Triangle2 result = new Triangle2();
        result.a = a.Clone();
        result.b = b.Clone();
        result.c = c.Clone();
        return result;
    }

    public ArrayList<Vector3f> GetVertices()
    {
        ArrayList<Vector3f> result = new ArrayList<>();
        result.add(a);
        result.add(b);
        result.add(c);
        return result;
    }
}
