package com.tutorial.glsltutorials.tutorials.Geometry;

/**
 * Created by Jamie on 5/27/14.
 */
public class Matrix3f {
    public float   M11, M12, M13,
                   M21, M22, M23,
                   M31, M32, M33;

    public Matrix3f()
    {
        SetRow0(new Vector3f(1f, 0f, 0f));
        SetRow1(new Vector3f(0f, 1f, 0f));
        SetRow2(new Vector3f(0f, 0f, 1f));
    }

    public Matrix3f(Vector3f row0_in, Vector3f row1_in, Vector3f row2_in)
    {
        SetRow0(row0_in);
        SetRow1(row1_in);
        SetRow2(row2_in);
    }

    public Matrix3f(Matrix3f mIn)
    {
        SetRow0(mIn.GetRow0());
        SetRow1(mIn.GetRow1());
        SetRow2(mIn.GetRow2());
    }

    public void Set(Matrix3f mIn)
    {
        SetRow0(mIn.GetRow0());
        SetRow1(mIn.GetRow1());
        SetRow2(mIn.GetRow2());
    }

    public Matrix3f(Matrix4f mIn)
    {
        SetRow0(new Vector3f(mIn.GetRow0()));
        SetRow1(new Vector3f(mIn.GetRow1()));
        SetRow2(new Vector3f(mIn.GetRow2()));
    }

    public void Set(Matrix4f mIn)
    {
        SetRow0(new Vector3f(mIn.GetRow0()));
        SetRow1(new Vector3f(mIn.GetRow1()));
        SetRow2(new Vector3f(mIn.GetRow2()));
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
        M11 = v.x;
        M12 = v.y;
        M13 = v.z;
    }

    public void SetRow1(Vector3f v)
    {
        M21 = v.x;
        M22 = v.y;
        M23 = v.z;
    }

    public void SetRow2(Vector3f v)
    {
        M31 = v.x;
        M32 = v.y;
        M33 = v.z;
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

    public float Determinant()
    {
        return M11 * M22 * M33 + M12 * M23 * M31 + M13 * M21 * M32
                    - M13 * M22 * M31 - M11 * M23 * M32 - M12 * M21 * M33;
    }

    public void normalize()
    {
        float determinant = Determinant();
        this.divide(determinant);
    }

    public void transpose()
    {
        float M21_temp = M12;
        float M31_temp = M13;
        float M32_temp = M23;
        M12 = M21;
        M13 = M31;
        M23 = M32;
        M21 = M21_temp;
        M31 = M31_temp;
        M32 = M32_temp;
    }

    public Matrix3f transposed()
    {
        Matrix3f result = this;
        result.transpose();
        return result;
    }

    public void divide(float divisor)
    {
        SetRow0(GetRow0().divide(divisor));
        SetRow1(GetRow1().divide(divisor));
        SetRow2(GetRow2().divide(divisor));
    }

    public Matrix3f divided(float divisor)
    {
        Matrix3f result = this;
        result.divide(divisor);
        return result;
    }

    static public Matrix3f Identity()
    {
        return new Matrix3f();
    }

    public void invert()
    {
        float det = this.Determinant();
        if (det != 0) {
        Matrix3f result = new Matrix3f();
            result.M11 = +M22 * M33 - M23 * M32;
            result.M12 = -M12 * M33 + M13 * M32;
            result.M13 = +M12 * M23 - M13 * M22;
            result.M21 = -M21 * M33 + M23 * M31;
            result.M22 = +M11 * M33 - M13 * M31;
            result.M23 = -M11 * M23 + M13 * M21;
            result.M31 = +M21 * M32 - M22 * M31;
            result.M32 = -M11 * M32 + M12 * M31;
            result.M33 = +M11 * M22 - M12 * M21;
            result.divide(det);
            Set(result);
        }
    }

    public Matrix3f inverted()
    {
        float det = this.Determinant();
        if (det != 0) {
            Matrix3f result = new Matrix3f();
            result.M11 = +M22 * M33 - M23 * M32;
            result.M12 = -M12 * M33 + M13 * M32;
            result.M13 = +M12 * M23 - M13 * M22;
            result.M21 = -M21 * M33 + M23 * M31;
            result.M22 = +M11 * M33 - M13 * M31;
            result.M23 = -M11 * M23 + M13 * M21;
            result.M31 = +M21 * M32 - M22 * M31;
            result.M32 = -M11 * M32 + M12 * M31;
            result.M33 = +M11 * M22 - M12 * M21;
            return result.divided(det);
        }
        return this;
    }

}
