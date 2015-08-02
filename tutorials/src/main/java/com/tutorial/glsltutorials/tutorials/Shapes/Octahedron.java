package com.tutorial.glsltutorials.tutorials.Shapes;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Tutorials.Tutorials;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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

    static FloatBuffer vertexBuffer;
    static ShortBuffer indexBuffer;
    static int[] vertexBufferObject = new int[]{-1};
    static int[] indexBufferObject = new int[1];
    static float[] vertexData;
    static short[] indexData;
    static int tutorialRunCount = -1;

    static void initializeStaticVertexBuffer()
    {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer vb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                vertexData.length * 4);
        // use the device hardware's native byte order
        vb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = vb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(vertexData);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        // initialize index byte buffer for vertex indexes
        ByteBuffer sb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 2 bytes per short)
                indexData.length * 2);
        // use the device hardware's native byte order
        sb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        indexBuffer = sb.asShortBuffer();
        // add the coordinates to the FloatBuffer
        indexBuffer.put(indexData);
        // set the buffer to read the first coordinate
        indexBuffer.position(0);

        GLES20.glGenBuffers(1, vertexBufferObject, 0);
        GLES20.glGenBuffers(1, indexBufferObject, 0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * BYTES_PER_SHORT,
                indexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * BYTES_PER_FLOAT,
                vertexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }


    public Octahedron (Vector3f sizeIn, float[] colorIn)
    {
        if (Tutorials.tutorialRunCount > tutorialRunCount)
        {
            tutorialRunCount = Tutorials.tutorialRunCount;
            vertexBufferObject = new int[]{-1};
            indexBufferObject = new int[1];
        }
        size = sizeIn;
        color = colorIn;
        modelToWorld.M11 = size.x;
        modelToWorld.M22 = size.y;
        modelToWorld.M33 = size.z;

        programNumber = Programs.addProgram(VertexShader, FragmentShader);

        if (vertexBufferObject[0] == -1) {
            vertexCount = 3 * 8;
            vertexStride = 3 * 4; // no color for now
            // fill in index data
            indexData = lmbIndexData;

            // fill in vertex data
            vertexData = lmbVertexData; //  GetVertexData();

            initializeStaticVertexBuffer();
        }
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
        Programs.draw(programNumber, vertexBufferObject[0], indexBufferObject[0], modelToWorld, indexData.length, color);
    }
}
