package com.tutorial.glsltutorials.tutorials.GLES_Helpers;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by jamie on 10/28/14.
 */
public class AnalysisTools {
    public AnalysisTools ()
    {
    }

    private static String CheckRotation(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3, Vector3f objectCenter)
    {
        Vector3f triangleCenter = new Vector3f((vertex1.x + vertex2.x + vertex3.x)/3,
                (vertex1.y + vertex2.y + vertex3.y)/3,
                (vertex1.z + vertex2.z + vertex3.z)/3);
        Vector3f Vertex2minusVertex1 = vertex2.sub(vertex1);
        Vector3f Vertex3minusVertex2 = vertex3.sub(vertex2);
        Vector3f outVector = Vertex2minusVertex1.cross(Vertex3minusVertex2);

        Vector3f triangleCenterToCenter = triangleCenter.sub(objectCenter);
        Vector3f triangelCenterPlusOutVector = triangleCenter.add(outVector);
        Vector3f triangelCenterPlusOutVectorToCenter = triangelCenterPlusOutVector.sub(objectCenter);

        float distanceA = triangleCenterToCenter.length();
        float distanceB = (triangleCenterToCenter.add(triangelCenterPlusOutVectorToCenter)).length();

        if (distanceA > distanceB)
        {
            return "CCW";
        }
        else
        {
            return "CW";
        }
    }

    public static String CheckRotations(float[] vertexes, short[] indexes,  Vector3f center)
    {
        StringBuilder result = new StringBuilder();
        int triangleCount = 0;
        for (int i = 0; i < indexes.length; i = i + 3)
        {
            int a = 3 * indexes[i];
            int b = 3 * indexes[i+1];
            int c = 3 * indexes[i+2];
            result.append(CheckRotation(
                    new Vector3f(vertexes[a], vertexes[a+1], vertexes[a+2]),
                    new Vector3f(vertexes[b], vertexes[b+1], vertexes[b+2]),
                    new Vector3f(vertexes[c], vertexes[c+1], vertexes[c+2]),
                    center
            ));
            triangleCount++;
        }
        result.append("Triangle Count = " + String.valueOf(triangleCount));
        return result.toString();
    }

    public static String CheckExtents(float[] vertexes)
    {
        StringBuilder result = new StringBuilder();
        float minX = vertexes[0];
        float maxX = minX;
        float minY = vertexes[1];
        float maxY = minY;
        float minZ = vertexes[2];
        float maxZ = minZ;
        for (int i = 3; i < vertexes.length; i = i + 3)
        {
            if (vertexes[i] < minX) minX = vertexes[i];
            if (vertexes[i] > maxX) maxX = vertexes[i];
            if (vertexes[i+1] < minY) minY = vertexes[i+1];
            if (vertexes[i+1] > maxY) maxY = vertexes[i+1];
            if (vertexes[i+2] < minZ) minZ = vertexes[i+2];
            if (vertexes[i+2] > maxZ) maxZ = vertexes[i+2];

        }
        result.append("minX = " + String.valueOf(minX));
        result.append("maxX = " + String.valueOf(maxX));
        result.append("minY = " + String.valueOf(minY));
        result.append("maxY = " + String.valueOf(maxY));
        result.append("minZ = " + String.valueOf(minZ));
        result.append("maxZ = " + String.valueOf(maxZ));
        return result.toString();
    }


    public static String CalculateMatrixEffects(Matrix4f matrix)
    {
        // Check This
        StringBuilder result = new StringBuilder();
        result.append(matrix.toString());
        result.append("");
        result.append("Translation by " + matrix.M41 + " " + matrix.M42 + " " + matrix.M43);
        Matrix3f normalizedMatrix = new Matrix3f(matrix);
        normalizedMatrix.normalize();
        float heading = 0f;
        float attitude = 0f;
        float bank = 0f;

        if (normalizedMatrix.M21 == 1)
        {
            result.append("North Pole");
            heading = (float)Math.atan2(normalizedMatrix.M13,normalizedMatrix.M33);
            bank = 0f;
        }
        else
        {
            if (normalizedMatrix.M21 == -1)
            {
                result.append("South Pole");
                heading = (float)Math.atan2(normalizedMatrix.M13,normalizedMatrix.M33);
                bank = 0;
            }
            else
            {
                heading = (float)Math.atan2(-normalizedMatrix.M31, normalizedMatrix.M11);
                attitude = (float)Math.asin(normalizedMatrix.M21);
                bank = (float)Math.atan2(-normalizedMatrix.M23, normalizedMatrix.M22);
            }
        }
        heading = heading * 180f / (float)Math.PI;
        attitude = attitude * 180f / (float)Math.PI;
        bank = bank * 180f / (float)Math.PI;
        result.append("heading = " + String.valueOf(heading));
        result.append("attitude = " + String.valueOf(attitude));
        result.append("bank = " + String.valueOf(bank));

        return result.toString();
    }

    public static String TestRotations()
    {
        StringBuilder result = new StringBuilder();
        Matrix4f X45 = Matrix4f.CreateRotationX(45f * (float)Math.PI / 180f);
        result.append("X45");
        result.append(CalculateMatrixEffects(X45));
        Matrix4f Y45 = Matrix4f.CreateRotationY(45f * (float)Math.PI / 180f);
        result.append("Y45");
        result.append(CalculateMatrixEffects(Y45));
        Matrix4f Z45 = Matrix4f.CreateRotationZ(45f * (float)Math.PI / 180f);
        result.append("Z45");
        result.append(CalculateMatrixEffects(Z45));
        return result.toString();
    }
}
