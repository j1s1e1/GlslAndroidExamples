package com.tutorial.glsltutorials.tutorials;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VBO_Tools;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;

import java.nio.FloatBuffer;

/**
 * Created by Jamie on 6/7/14.
 */
public class ProjectionBlock {
    public Matrix4f cameraToClipMatrix;
    static public int byteLength()
    {
        return 16 * 4;
    }

    public float[] ToFloat()
    {
        return cameraToClipMatrix.toArray();
    }

    public FloatBuffer ToFloatBuffer()
    {
        FloatBuffer fb = VBO_Tools.MakeFloatBuffer(ToFloat());
        return fb;
    }
}
