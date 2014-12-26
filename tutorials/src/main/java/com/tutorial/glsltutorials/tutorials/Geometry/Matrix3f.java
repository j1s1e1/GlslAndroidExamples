package com.tutorial.glsltutorials.tutorials.Geometry;

/**
 * Created by Jamie on 5/27/14.
 */
public class Matrix3f {
    private Vector3f row0, row1, row2;
    public float   M11, M12, M13,
                   M21, M22, M23,
                   M31, M32, M33;

    public Matrix3f()
    {
        row0 = new Vector3f(1f, 0f, 0f);
        row1 = new Vector3f(0f, 1f, 0f);
        row2 = new Vector3f(0f, 0f, 1f);
        setFloats();
    }

    public Matrix3f(Vector3f row0_in, Vector3f row1_in, Vector3f row2_in)
    {
        row0 = row0_in;
        row1 = row1_in;
        row2 = row2_in;
        setFloats();
    }

    public Matrix3f(Matrix4f min)
    {
        row0 = new Vector3f(min.GetRow0());
        row1 = new Vector3f(min.GetRow1());
        row2 = new Vector3f(min.GetRow2());
        setFloats();
    }

    public float[] toArray()
    {
        float[] result;
        result = new float[]{
                M11, M12, M13,
                M21, M22, M23,
                M31, M32, M33};
        return result;
    }

    public void SetRow0(Vector3f v)
    {
        row0 = v;
        M11 = row0.x;
        M12 = row0.y;
        M13 = row0.z;
    }

    public void SetRow1(Vector3f v)
    {
        row1 = v;
        M21 = row1.x;
        M22 = row1.y;
        M23 = row1.z;
    }

    public void SetRow2(Vector3f v)
    {
        row2 = v;
        M31 = row2.x;
        M32 = row2.y;
        M33 = row2.z;
    }

    public Vector3f GetRow0()
    {
        return (new Vector3f(M11, M12, M13));
    }

    public Vector3f GetRow1()
    {
        return (new Vector3f(M21, M22, M23));
    }

    public Vector3f GetRow2()
    {
        return (new Vector3f(M31, M32, M33));
    }

    private void setFloats()
    {
        M11 = row0.x;
        M12 = row0.y;
        M13 = row0.z;
        M21 = row1.x;
        M22 = row1.y;
        M23 = row1.z;
        M31 = row2.x;
        M32 = row2.y;
        M33 = row2.z;
    }

    public float Determinant()
    {
        return M11 * M22 * M33 + M12 * M23 * M31 + M13 * M21 * M32
                    - M13 * M22 * M31 - M11 * M23 * M32 - M12 * M21 * M33;
    }

    public void Normalize()
    {
        float determinant = Determinant();
        row0 = row0.divide(determinant);
        row1 = row1.divide(determinant);
        row2 = row2.divide(determinant);
        setFloats();
    }

    static public Matrix3f Identity()
    {
        return new Matrix3f();
    }

    static public void invert()
    {

    }
}
