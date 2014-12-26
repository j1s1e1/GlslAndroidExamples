package com.tutorial.glsltutorials.tutorials.Light;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

/**
 * Created by jamie on 12/26/14.
 */
public class PerLight
{
    public Vector4f cameraSpaceLightPos;
    public Vector4f lightIntensity;

    public static int size()
    {
        int size = 0;
        size += Vector4f.sizeInBytes();
        size += Vector4f.sizeInBytes();
        return size;
    }

    public float[] toFloat()
    {
        float[] result = new float[size()/4];
        int position = 0;
        System.arraycopy(cameraSpaceLightPos.toArray(), 0, result, position, 4);
        position += 4;
        System.arraycopy(lightIntensity.toArray(), 0, result, position, 4);
        return result;
    }
}
