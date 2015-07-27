package com.tutorial.glsltutorials.tutorials.Shapes;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;

/**
 * Created by jamie on 7/26/15.
 */
public class Octahedron extends Shape {
    //				  0
    //	
    //		    4------------3
    //		   /            /
    //		  /			   /
    //		 / 	 		  /
    //	    1------------2
    //
    //
    //			  5

    Vector3f size;

    static short[] lmbIndexData = new short[]
            {	0, 1, 2,
                    0, 2, 3,
                    0, 3, 4,
                    0, 4, 1,
                    1, 5, 2,
                    2, 5, 3,
                    3, 5, 4,
                    4, 5, 1};

    static float RIGHT_EXTENT = 0.5f;
    static float LEFT_EXTENT = -RIGHT_EXTENT;
    static float TOP_EXTENT = 0.5f;
    static float BOTTOM_EXTENT = -TOP_EXTENT;
    static float FRONT_EXTENT = 0.5f;
    static float REAR_EXTENT = -FRONT_EXTENT;

    static float[] lmbVertexData = new float[]
            { 	0f, TOP_EXTENT, 0f,
                    LEFT_EXTENT, 0f, FRONT_EXTENT,
                    RIGHT_EXTENT, 0f, FRONT_EXTENT,
                    RIGHT_EXTENT, 0f, REAR_EXTENT,
                    LEFT_EXTENT, 0f, REAR_EXTENT,
                    0f, BOTTOM_EXTENT, 0f,
            };

    public Octahedron (Vector3f sizeIn, float[] colorIn)
    {
        size = sizeIn;
        color = colorIn;
        modelToWorld.M11 = size.x;
        modelToWorld.M22 = size.y;
        modelToWorld.M33 = size.z;

        programNumber = Programs.addProgram(VertexShader, FragmentShader);

        vertexCount = 3 * 8;
        vertexStride = 3 * 4; // no color for now
        // fill in index data
        indexData = lmbIndexData;

        // fill in vertex data
        vertexData = lmbVertexData; //  GetVertexData();

        initializeVertexBuffer();
    }

    private float[] getVertexData()
    {
        float[] vertexData = new float[lmbVertexData.length];
        for (int i = 0; i < vertexData.length; i = i + 3)
        {
            vertexData[i] = lmbVertexData[i] * size.x;
            vertexData[i+1] = lmbVertexData[i+1] * size.y;
            vertexData[i+2] = lmbVertexData[i+2] * size.z;
        }
        return vertexData;
    }

    public void draw()
    {
        Programs.draw(programNumber, vertexBufferObject, indexBufferObject, modelToWorld, indexData.length, color);
    }
}
