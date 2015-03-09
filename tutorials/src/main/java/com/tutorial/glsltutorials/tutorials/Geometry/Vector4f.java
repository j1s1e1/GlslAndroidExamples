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
        v.x = Vector4f.Dot(vin, m.GetCol0());
        v.y = Vector4f.Dot(vin, m.GetCol1());
        v.z = Vector4f.Dot(vin, m.GetCol2());
        v.z = Vector4f.Dot(vin, m.GetCol3());
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

    public static int sizeInBytes()
    {
        return 16;
    }

    public Vector4f add(Vector4f b){
        return new Vector4f(
                x + b.x,
                y + b.y,
                z + b.z,
                w - b.w
        );
    }

    public Vector4f sub(Vector4f b) {
        return new Vector4f(
                x - b.x,
                y - b.y,
                z - b.z,
                w - b.w
        );
    }

    public Vector4f normalize(){
        float length =  length();
        if(length >= 0.0000001f){
            float inv = 1/length;
            return new Vector4f(x *inv, y *inv, z *inv, w*inv);
        }
        return new Vector4f(0.f, 0.f, 0.f, 0.f);
    }

    public static Vector4f normalize(Vector4f v){
        float length =  v.length();
        if(length >= 0.0000001f){
            float inv = 1/length;
            return new Vector4f(v.x *inv, v.y *inv, v.z *inv, v.w*inv);
        }
        return new Vector4f(0.f, 0.f, 0.f, 0.f);
    }

    public float length()
    {
        return  (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2) + Math.pow(w, 2));
    }

    public static float distance(Vector4f a, Vector4f b)
    {
        return (a.sub(b).length());
    }

    public String toString()
    {
        return "X " + String.format("%.3f" , x) + " Y " + String.format("%.3f" , y) + " Z " +
                String.format("%.3f" , z);
    }
}
