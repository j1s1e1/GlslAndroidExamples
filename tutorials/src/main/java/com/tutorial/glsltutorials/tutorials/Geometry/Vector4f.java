package com.tutorial.glsltutorials.tutorials.Geometry;

/**
 * Created by Jamie on 5/27/14.
 */
public class Vector4f {
    public float x, y, z, w;

    public Vector4f()
    {

    }

    public Vector4f(float x_in, float y_in, float z_in, float w_in)
    {
        x = x_in;
        y = y_in;
        z = z_in;
        w = w_in;
    }

    public Vector4f(Vector3f v, float w_in)
    {
        x = v.x;
        y = v.y;
        z = v.z;
        w = w_in;
    }

    public float[] toArray() {
        return new float[]{x, y, z, w};
    }

    public static Vector4f Transform(Vector4f vin, Matrix4f m)
    {
        Vector4f v = new Vector4f();
        //Fixthis
        return v;
    }

    public void Scale(float scale)
    {
        x = x * scale;
        y = y * scale;
        z = z * scale;
        w = w * scale;
    }

    public Vector4f Mult(float f)
    {
        return new Vector4f(x*f, y*f, z*f, w*f);
    }

    public static float Dot(Vector4f left, Vector4f right)
    {
        return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
    }
}
