package com.tutorial.glsltutorials.tutorials;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

/**
 * Created by Jamie on 6/8/14.
 */
public class Camera {
    //In spherical coordinates.
    public static Vector3f g_sphereCamRelPos = new Vector3f(67.5f, -46.0f, 150.0f);

    public static Vector3f g_camTarget = new Vector3f(0.0f, 0.4f, 0.0f);

    public static float Clamp(float value, float min, float max)
    {
        if (value < min)
            return min;

        if (value > max)
            return max;

        return value;
    }

    public static void Move(float x, float y, float z)
    {
        g_sphereCamRelPos = g_sphereCamRelPos.add(new Vector3f(x, y, z));
        g_sphereCamRelPos.y = Clamp(g_sphereCamRelPos.y, -78.75f, -1.0f);
        g_sphereCamRelPos.z = g_sphereCamRelPos.z > 5.0f ? g_sphereCamRelPos.z : 5.0f;
    }

    public static void MoveTarget(float x, float y, float z)
    {
        g_camTarget = g_camTarget.add(new Vector3f(x, y, z));
        g_camTarget.y = g_camTarget.y > 0.0f ? g_camTarget.y : 0.0f;
    }

    public static String GetTargetString()
    {
        return String.format("\nTarget: {0:f} {1:f} {2:f}", g_camTarget.x, g_camTarget.y,
                g_camTarget.z);
    }

    public static String GetPositionString()
    {
        return String.format("\nPosition: {0:f} {1:f} {2:f}", g_sphereCamRelPos.x, g_sphereCamRelPos.y,
                g_sphereCamRelPos.z);
    }

    public static Vector3f ResolveCamPosition()
    {
        MatrixStack tempMat;

        float phi = (float)(Math.PI / 180 * g_sphereCamRelPos.x);
        float theta = (float)(Math.PI / 180 * g_sphereCamRelPos.y + 90.0f);

        float fSinTheta = (float)Math.sin(theta);
        float fCosTheta = (float)Math.cos(theta);
        float fCosPhi = (float)Math.cos(phi);
        float fSinPhi = (float)Math.sin(phi);

        Vector3f dirToCamera = new Vector3f(fSinTheta * fCosPhi, fCosTheta, fSinTheta * fSinPhi);
        return dirToCamera.mul(g_sphereCamRelPos.z).add(g_camTarget);
    }

    public static Matrix4f CalcLookAtMatrix(Vector3f cameraPt, Vector3f lookPt, Vector3f upPt)
    {
        Vector3f lookDir = lookPt.sub(cameraPt);
        lookDir = lookDir.normalize();
        Vector3f upDir = upPt;
        upDir = upDir.normalize();

        Vector3f rightDir = lookDir.cross(upDir);
        rightDir = rightDir.normalize();
        Vector3f perpUpDir = rightDir.cross(lookDir);

        Matrix4f rotMat = Matrix4f.Identity();
        rotMat.SetRow0(new Vector4f(rightDir, 0.0f));
        rotMat.SetRow1(new Vector4f(perpUpDir, 0.0f));
        rotMat.SetRow2(new Vector4f(lookDir.mul(-1f), 0.0f));

        rotMat.Transpose();

        Matrix4f transMat = Matrix4f.Identity();
        transMat.SetRow3(new Vector4f(cameraPt.mul(-1f), 1.0f));

        return Matrix4f.Mult(rotMat, transMat);
    }

    public static Matrix4f GetLookAtMatrix()
    {
        return CalcLookAtMatrix(ResolveCamPosition(), g_camTarget, new Vector3f(0.0f, 1.0f, 0.0f));
    }
}
