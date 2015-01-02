package com.tutorial.glsltutorials.tutorials.Geometry;

/**
 * Created by Jamie on 5/27/14.
 */
public class Matrix4f {
    private Vector4f row0, row1, row2, row3;
    public float   M11, M12, M13, M14,
                    M21, M22, M23, M24,
                    M31, M32, M33, M34,
                    M41, M42, M43, M44;

    public Matrix4f()
    {
        row0 = new Vector4f(1f, 0f, 0f, 0f);
        row1 = new Vector4f(0f, 1f, 0f, 0f);
        row2 = new Vector4f(0f, 0f, 1f, 0f);
        row3 = new Vector4f(0f, 0f, 0f, 1f);
        setFloats();
    }

    public Matrix4f(float[] data)
    {
        row0 = new Vector4f(data[0], data[1], data[2], data[3]);
        row1 = new Vector4f(data[4], data[5], data[6], data[7]);
        row2 = new Vector4f(data[8], data[9], data[10], data[11]);
        row3 = new Vector4f(data[12], data[13], data[14], data[15]);
        setFloats();
    }


    public Matrix4f(Vector4f row0_in, Vector4f row1_in, Vector4f row2_in,Vector4f row3_in)
    {
        row0 = row0_in;
        row1 = row1_in;
        row2 = row2_in;
        row3 = row3_in;
        setFloats();
    }

    public Matrix4f(Matrix4f m)
    {
        row0 = m.GetRow0();
        row1 = m.GetRow1();
        row2 = m.GetRow2();
        row3 = m.GetRow3();
        setFloats();
    }

    public float[] toArray()
    {
        float[] result;
        result = new float[]{
                M11, M12, M13, M14,
                M21, M22, M23, M24,
                M31, M32, M33, M34,
                M41, M42, M43, M44,};
        return result;
    }

    public void SetRow0(Vector4f v)
    {
        row0 = v;
        M11 = row0.x;
        M12 = row0.y;
        M13 = row0.z;
        M14 = row0.w;
    }

    public void SetRow1(Vector4f v)
    {
        row1 = v;
        M21 = row1.x;
        M22 = row1.y;
        M23 = row1.z;
        M24 = row1.w;
    }

    public void SetRow2(Vector4f v)
    {
        row2 = v;
        M31 = row2.x;
        M32 = row2.y;
        M33 = row2.z;
        M34 = row2.w;
    }

    public void SetRow3(Vector4f v)
    {
        row3 = v;
        M41 = row3.x;
        M42 = row3.y;
        M43 = row3.z;
        M44 = row3.w;
    }

    public void SetRow0(Vector3f v, float f)
    {
        row0 = new Vector4f(v.x, v.y, v.z, f);
        M11 = row0.x;
        M12 = row0.y;
        M13 = row0.z;
        M14 = row0.w;
    }

    public void SetRow1(Vector3f v, float f)
    {
        row1 = new Vector4f(v.x, v.y, v.z, f);
        M21 = row1.x;
        M22 = row1.y;
        M23 = row1.z;
        M24 = row1.w;
    }

    public void SetRow2(Vector3f v, float f)
    {
        row2 = new Vector4f(v.x, v.y, v.z, f);
        M31 = row2.x;
        M32 = row2.y;
        M33 = row2.z;
        M34 = row2.w;
    }

    public void SetRow3(Vector3f v, float f)
    {
        row3 = new Vector4f(v.x, v.y, v.z, f);
        M41 = row3.x;
        M42 = row3.y;
        M43 = row3.z;
        M44 = row3.w;
    }

    public Vector4f GetRow0()
    {
        return (new Vector4f(M11, M12, M13, M14));
    }

    public Vector4f GetRow1()
    {
        return (new Vector4f(M21, M22, M23, M24));
    }

    public Vector4f GetRow2()
    {
        return (new Vector4f(M31, M32, M33, M34));
    }

    public Vector4f GetRow3()
    {
        return (new Vector4f(M41, M42, M43, M44));
    }

    public Vector4f GetCol0()
    {
        return (new Vector4f(M11, M21, M31, M41));
    }

    public Vector4f GetCol1()
    {
        return (new Vector4f(M12, M22, M32, M42));
    }

    public Vector4f GetCol2()
    {
        return (new Vector4f(M13, M23, M33, M43));
    }

    public Vector4f GetCol3()
    {
        return (new Vector4f(M14, M24, M34, M44));
    }

    private void setFloats()
    {
        M11 = row0.x;
        M12 = row0.y;
        M13 = row0.z;
        M14 = row0.w;
        M21 = row1.x;
        M22 = row1.y;
        M23 = row1.z;
        M24 = row1.w;
        M31 = row2.x;
        M32 = row2.y;
        M33 = row2.z;
        M34 = row2.w;
        M41 = row3.x;
        M42 = row3.y;
        M43 = row3.z;
        M44 = row3.w;
    }

    static public Matrix4f Identity()
    {
        return new Matrix4f();
    }

    static public Matrix4f CreateTranslation(Vector3f v)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.SetRow3(new Vector4f(v.x, v.y, v.z, 1));
        return matrix;
    }

    static public Matrix4f Mult(Matrix4f left, Matrix4f right)
    {
        Matrix4f matrix = new Matrix4f();
        matrix.M11 = Vector4f.Dot(left.GetRow0(), right.GetCol0());
        matrix.M12 = Vector4f.Dot(left.GetRow0(), right.GetCol1());
        matrix.M13 = Vector4f.Dot(left.GetRow0(), right.GetCol2());
        matrix.M14 = Vector4f.Dot(left.GetRow0(), right.GetCol3());

        matrix.M21 = Vector4f.Dot(left.GetRow1(), right.GetCol0());
        matrix.M22 = Vector4f.Dot(left.GetRow1(), right.GetCol1());
        matrix.M23 = Vector4f.Dot(left.GetRow1(), right.GetCol2());
        matrix.M24 = Vector4f.Dot(left.GetRow1(), right.GetCol3());

        matrix.M31 = Vector4f.Dot(left.GetRow2(), right.GetCol0());
        matrix.M32 = Vector4f.Dot(left.GetRow2(), right.GetCol1());
        matrix.M33 = Vector4f.Dot(left.GetRow2(), right.GetCol2());
        matrix.M34 = Vector4f.Dot(left.GetRow2(), right.GetCol3());

        matrix.M41 = Vector4f.Dot(left.GetRow3(), right.GetCol0());
        matrix.M42 = Vector4f.Dot(left.GetRow3(), right.GetCol1());
        matrix.M43 = Vector4f.Dot(left.GetRow3(), right.GetCol2());
        matrix.M44 = Vector4f.Dot(left.GetRow3(), right.GetCol3());

        return matrix;
    }

    static public Matrix4f CreateFromAxisAngle(Vector3f axis, float angle)
    {
        axis.normalize();
        Matrix4f matrix = new Matrix4f();
        float cos = (float)Math.cos(-angle);
        float sin = (float)Math.sin(-angle);
        float t = 1.0f - cos;

        axis = axis.normalize();
        matrix.M11 = t * axis.x * axis.x + cos;
        matrix.M12 = t * axis.x * axis.y - sin * axis.z;
        matrix.M13 = t * axis.x * axis.z + sin * axis.y;
        matrix.M14 = 0.0f;
        matrix.M21 = t * axis.x * axis.y + sin * axis.z;
        matrix.M22 = t * axis.y * axis.y + cos;
        matrix.M23 = t * axis.y * axis.z - sin * axis.x;
        matrix.M24 = 0.0f;
        matrix.M31 = t * axis.x * axis.z - sin * axis.y;
        matrix.M32 = t * axis.y * axis.z + sin * axis.x;
        matrix.M33 = t * axis.z * axis.z + cos;
        matrix.M34 = 0.0f;
        matrix.M41 = 0;
        matrix.M42 = 0;
        matrix.M43 = 0;
        matrix.M44 = 1;
        return matrix;
    }

    public void Scale(Vector3f scale)
    {
        row0 = new Vector4f(M11, M12, M13, M14);
        row0.Scale(scale.x);
        row1 = new Vector4f(M21, M22, M23, M24);
        row1.Scale(scale.y);
        row2 = new Vector4f(M31, M32, M33, M34);
        row2.Scale(scale.z);
        row3 = new Vector4f(M41, M42, M43, M44);
        setFloats();
    }

    static public Matrix4f LookAt(Vector3f cameraPos,Vector3f lookatPos, Vector3f upDir)
    {
        Vector3f z = Vector3f.normalize(cameraPos.add(lookatPos.mul(-1)));
        Vector3f x = Vector3f.normalize(upDir.cross(z));
        Vector3f y = Vector3f.normalize(z.cross(x));

        Matrix4f rot = new Matrix4f(new Vector4f(x.x, y.x, z.x, 0.0f),
                new Vector4f(x.y, y.y, z.y, 0.0f),
                new Vector4f(x.z, y.z, z.z, 0.0f),
                new Vector4f(0f, 0f, 0f, 1f));

        Matrix4f trans = Matrix4f.CreateTranslation(cameraPos.mul(-1));
        return Matrix4f.Mult(trans, rot);
    }

    // Formula based on OpenTK formula
    static public Matrix4f CreatePerspectiveFieldOfView(float fov, float aspectRatio, float zNear, float zFar)
    {
        if (fov <= 0)
            fov = 0;
        if (fov > Math.PI)
            fov = (float)Math.PI;

        if (aspectRatio <= 0)
            aspectRatio = 0.0000001f;
        if (zNear <= 0)
            zNear = 0.0000001f;
        if (zFar <= 0)
            zFar = 0.0000001f;

        float yMax = zNear * (float)Math.tan(0.5f * fov);
        float yMin = -yMax;
        float xMin = yMin * aspectRatio;
        float xMax = yMax * aspectRatio;

        return CreatePerspectiveOffCenter(xMin, xMax, yMin, yMax, zNear, zFar);
    }

    // Formula based on OpenTK formula
    public static Matrix4f CreatePerspectiveOffCenter(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        if (zNear <= 0)
            zNear = 0.0000001f;
        if (zFar <= 0)
            zFar = 0.0000001f;
        if (zNear >= zFar)
            zFar = zNear + 0.0000001f;

        float x = (2.0f * zNear) / (right - left);
        float y = (2.0f * zNear) / (top - bottom);
        float a = (right + left) / (right - left);
        float b = (top + bottom) / (top - bottom);
        float c = -(zFar + zNear) / (zFar - zNear);
        float d = -(2.0f * zFar * zNear) / (zFar - zNear);

        Matrix4f result = new Matrix4f();

        result.M11 = x;

        result.M22 = y;

        result.M31 = a;
        result.M32 = b;
        result.M33 = c;
        result.M34 = -1;

        result.M43 = d;
        result.M44 = 0f;

        return result;
    }

    static public Matrix4f CreateOrthographic(float  width, float height, float zNear, float zFar)
    {
        Matrix4f matrix = new Matrix4f();
        //FIX_THIS
        return matrix;
    }

    public void Transpose()
    {
        Vector4f new_row0 = new Vector4f(M11, M21, M31, M41);
        Vector4f new_row1 = new Vector4f(M12, M22, M32, M42);
        Vector4f new_row2 = new Vector4f(M13, M23, M33, M43);
        Vector4f new_row3 = new Vector4f(M14, M24, M34, M44);
        SetRow0(new_row0);
        SetRow1(new_row1);
        SetRow2(new_row2);
        SetRow3(new_row3);
        setFloats();
    }

    public Matrix4f inverted ()
    {
        Matrix4f result = this;
        if (result.determinant() != 0f)
        {
            float[] invOut = new float[16];
            InvertMatrix(result.toArray(),  invOut);
            result = new Matrix4f(invOut);
        }

        return result;
    }

    public float determinant()
    {
       return M11 * M22 * M33 * M44 - M11 * M22 * M34 * M43 + M11 * M23 * M34 * M42 - M11 * M23 * M32 * M44
            + M11 * M24 * M32 * M43 - M11 * M24 * M33 * M42 - M12 * M23 * M34 * M41 + M12 * M23 * M31 * M44
            - M12 * M24 * M31 * M43 + M12 * M24 * M33 * M41 - M12 * M21 * M33 * M44 + M12 * M21 * M34 * M43
            + M13 * M24 * M31 * M42 - M13 * M24 * M32 * M41 + M13 * M21 * M32 * M44 - M13 * M21 * M34 * M42
            + M13 * M22 * M34 * M41 - M13 * M22 * M31 * M44 - M14 * M21 * M32 * M43 + M14 * M21 * M33 * M42
            - M14 * M22 * M33 * M41 + M14 * M22 * M31 * M43 - M14 * M23 * M31 * M42 + M14 * M23 * M32 * M41;
    }

    static boolean InvertMatrix(float[] m, float[] invOut)
    {
        float[] inv = new float[16];
        float det;
        int i;

        inv[0] = m[5]  * m[10] * m[15] -
                m[5]  * m[11] * m[14] -
                m[9]  * m[6]  * m[15] +
                m[9]  * m[7]  * m[14] +
                m[13] * m[6]  * m[11] -
                m[13] * m[7]  * m[10];

        inv[4] = -m[4]  * m[10] * m[15] +
                m[4]  * m[11] * m[14] +
                m[8]  * m[6]  * m[15] -
                m[8]  * m[7]  * m[14] -
                m[12] * m[6]  * m[11] +
                m[12] * m[7]  * m[10];

        inv[8] = m[4]  * m[9] * m[15] -
                m[4]  * m[11] * m[13] -
                m[8]  * m[5] * m[15] +
                m[8]  * m[7] * m[13] +
                m[12] * m[5] * m[11] -
                m[12] * m[7] * m[9];

        inv[12] = -m[4]  * m[9] * m[14] +
                m[4]  * m[10] * m[13] +
                m[8]  * m[5] * m[14] -
                m[8]  * m[6] * m[13] -
                m[12] * m[5] * m[10] +
                m[12] * m[6] * m[9];

        inv[1] = -m[1]  * m[10] * m[15] +
                m[1]  * m[11] * m[14] +
                m[9]  * m[2] * m[15] -
                m[9]  * m[3] * m[14] -
                m[13] * m[2] * m[11] +
                m[13] * m[3] * m[10];

        inv[5] = m[0]  * m[10] * m[15] -
                m[0]  * m[11] * m[14] -
                m[8]  * m[2] * m[15] +
                m[8]  * m[3] * m[14] +
                m[12] * m[2] * m[11] -
                m[12] * m[3] * m[10];

        inv[9] = -m[0]  * m[9] * m[15] +
                m[0]  * m[11] * m[13] +
                m[8]  * m[1] * m[15] -
                m[8]  * m[3] * m[13] -
                m[12] * m[1] * m[11] +
                m[12] * m[3] * m[9];

        inv[13] = m[0]  * m[9] * m[14] -
                m[0]  * m[10] * m[13] -
                m[8]  * m[1] * m[14] +
                m[8]  * m[2] * m[13] +
                m[12] * m[1] * m[10] -
                m[12] * m[2] * m[9];

        inv[2] = m[1]  * m[6] * m[15] -
                m[1]  * m[7] * m[14] -
                m[5]  * m[2] * m[15] +
                m[5]  * m[3] * m[14] +
                m[13] * m[2] * m[7] -
                m[13] * m[3] * m[6];

        inv[6] = -m[0]  * m[6] * m[15] +
                m[0]  * m[7] * m[14] +
                m[4]  * m[2] * m[15] -
                m[4]  * m[3] * m[14] -
                m[12] * m[2] * m[7] +
                m[12] * m[3] * m[6];

        inv[10] = m[0]  * m[5] * m[15] -
                m[0]  * m[7] * m[13] -
                m[4]  * m[1] * m[15] +
                m[4]  * m[3] * m[13] +
                m[12] * m[1] * m[7] -
                m[12] * m[3] * m[5];

        inv[14] = -m[0]  * m[5] * m[14] +
                m[0]  * m[6] * m[13] +
                m[4]  * m[1] * m[14] -
                m[4]  * m[2] * m[13] -
                m[12] * m[1] * m[6] +
                m[12] * m[2] * m[5];

        inv[3] = -m[1] * m[6] * m[11] +
                m[1] * m[7] * m[10] +
                m[5] * m[2] * m[11] -
                m[5] * m[3] * m[10] -
                m[9] * m[2] * m[7] +
                m[9] * m[3] * m[6];

        inv[7] = m[0] * m[6] * m[11] -
                m[0] * m[7] * m[10] -
                m[4] * m[2] * m[11] +
                m[4] * m[3] * m[10] +
                m[8] * m[2] * m[7] -
                m[8] * m[3] * m[6];

        inv[11] = -m[0] * m[5] * m[11] +
                m[0] * m[7] * m[9] +
                m[4] * m[1] * m[11] -
                m[4] * m[3] * m[9] -
                m[8] * m[1] * m[7] +
                m[8] * m[3] * m[5];

        inv[15] = m[0] * m[5] * m[10] -
                m[0] * m[6] * m[9] -
                m[4] * m[1] * m[10] +
                m[4] * m[2] * m[9] +
                m[8] * m[1] * m[6] -
                m[8] * m[2] * m[5];

        det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];

        if (det == 0)
            return false;

        det = (float)(1.0 / det);

        for (i = 0; i < 16; i++)
            invOut[i] = inv[i] * det;

        return true;
    }

    public static Matrix4f CreateRotationX(float angle)
    {
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);

        Matrix4f result = Matrix4f.Identity();
        result.M22 = cos;
        result.M23 = sin;
        result.M32 = -sin;
        result.M33 = cos;
        return result;
    }

    public static Matrix4f CreateRotationY(float angle)
    {
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);

        Matrix4f result = Matrix4f.Identity();
        result.M11 = cos;
        result.M13 = -sin;
        result.M31 = sin;
        result.M33 = cos;
        return result;
    }

    public static Matrix4f CreateRotationZ(float angle)
    {
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);

        Matrix4f result = Matrix4f.Identity();
        result.M11 = cos;
        result.M12 = sin;
        result.M21 = -sin;
        result.M22 = cos;
        return result;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Row 0 = " + String.valueOf(M11) + " " + String.valueOf(M12));
        result.append(" " + String.valueOf(M13) + " " + String.valueOf(M14) + "\n");
        result.append("Row 1 = " + String.valueOf(M21) + " " + String.valueOf(M22));
        result.append(" " + String.valueOf(M23) + " " + String.valueOf(M24) + "\n");
        result.append("Row 2 = " + String.valueOf(M31) + " " + String.valueOf(M32));
        result.append(" " + String.valueOf(M33) + " " + String.valueOf(M34) + "\n");
        result.append("Row 3 = " + String.valueOf(M41) + " " + String.valueOf(M42));
        result.append(" " + String.valueOf(M43) + " " + String.valueOf(M44) + "\n");
        return result.toString();
    }

    public static Matrix4f createFromAxisAngle(Vector3f axis, float angle)
    {
        Matrix4f result = new Matrix4f();
        // normalize and create a local copy of the vector.
        axis = axis.normalize();
        float axisX = axis.x;
        float axisY = axis.y;
        float axisZ = axis.z;

        // calculate angles
        float cos = (float)Math.cos(-angle);
        float sin = (float)Math.sin(-angle);
        float t = 1.0f - cos;

        // do the conversion math once
        float tXX = t * axisX * axisX,
                tXY = t * axisX * axisY,
                tXZ = t * axisX * axisZ,
                tYY = t * axisY * axisY,
                tYZ = t * axisY * axisZ,
                tZZ = t * axisZ * axisZ;

        float sinX = sin * axisX,
                sinY = sin * axisY,
                sinZ = sin * axisZ;

        result.M11 = tXX + cos;
        result.M12 = tXY - sinZ;
        result.M13 = tXZ + sinY;
        result.M14 = 0;
        result.M21 = tXY + sinZ;
        result.M22 = tYY + cos;
        result.M23 = tYZ - sinX;
        result.M24 = 0;
        result.M31 = tXZ - sinY;
        result.M32 = tYZ + sinX;
        result.M33 = tZZ + cos;
        result.M34 = 0;
        result.SetRow3(new Vector4f(0f, 0f, 0f, 1f));
        return result;
    }

    public static Matrix4f createFromQuaternion(Quaternion q)
    {
        Vector3f axis = q.getAxis();
        float angle = q.getAngle();
        return createFromAxisAngle(axis, angle);
    }
}
