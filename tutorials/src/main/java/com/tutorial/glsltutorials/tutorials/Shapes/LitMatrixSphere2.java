package com.tutorial.glsltutorials.tutorials.Shapes;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Tutorials.Tutorials;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by jamie on 10/12/14.
 */
public class LitMatrixSphere2 extends Shape {
    float radius;
    int divideCount = 1;

    static FloatBuffer vertexBuffer;
    static ShortBuffer indexBuffer;
    static int[] vertexBufferObject = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    static int[] indexBufferObject = new int[10];
    static float[] vertexData;
    static short[] indexData;
    static int COORDS_PER_VERTEX = 3;
    static int tutorialRunCount = -1;

    static void setupStaticIndexBuffer()
    {
        indexData = new short[vertexData.length/COORDS_PER_VERTEX];
        for (short i = 0; i < indexData.length; i++)
        {
            indexData[i] = i;
        }
    }

    static void initializeVertexBuffer(int divideCount)
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

        GLES20.glGenBuffers(1, vertexBufferObject, divideCount);
        GLES20.glGenBuffers(1, indexBufferObject, divideCount);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[divideCount]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * BYTES_PER_SHORT,
                indexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[divideCount]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * BYTES_PER_FLOAT,
                vertexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public LitMatrixSphere2 (float radius_in, int divideCountIn) {
        radius = radius_in;
        divideCount = divideCountIn;
        setup();
    }

    public LitMatrixSphere2 (float radius_in) {
        radius = radius_in;
        setup();
    }

    void setup()
    {
        if (Tutorials.tutorialRunCount > tutorialRunCount)
        {
            tutorialRunCount = Tutorials.tutorialRunCount;
            vertexBufferObject = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
            indexBufferObject = new int[10];
        }
        scale(new Vector3f(radius, radius, radius));

        if (vertexBufferObject[divideCount] == -1) {
            vertexCoords = getCircleCoords();
            vertexCount = vertexCoords.length / COORDS_PER_VERTEX / 2;

            vertexData = vertexCoords;
            setupStaticIndexBuffer();

            initializeVertexBuffer(divideCount);
        }

        programNumber = Programs.addProgram(VertexShaders.lms_vertexShaderCode,
                FragmentShaders.lms_fragmentShaderCode);
    }

    private float[] getCircleCoords()
    {
        float[] coords = Icosahedron.GetDividedTriangles(divideCount);
        float[] coords_with_normals = new float[2*coords.length];
        int j = 0;
        for (int i = 0; i < coords.length * 2; i++)
        {
            switch (i % 6)
            {
                case 0:
                case 1:
                case 2:
                    coords_with_normals[i] = coords[j];
                    j++;
                    break;
                case 3:  coords_with_normals[i] = coords[j-3]; break;
                case 4:  coords_with_normals[i] = coords[j-2]; break;
                case 5:  coords_with_normals[i] = coords[j-1]; break;

            }

        }
        return coords_with_normals;
    }

    private void drawSub(int first_triangle, int last_triangle)
    {
        int newVertexCount = indexData.length / 20 * (last_triangle - first_triangle + 1);

        Programs.draw(programNumber, vertexBufferObject[divideCount], indexBufferObject[divideCount], modelToWorld, newVertexCount, color);
    }

    public void setLightPosition(Vector3f lightPosition)
    {
        Programs.setLightPosition(programNumber, lightPosition);
    }

    public void draw() {
        drawSub(0, 19);
    }

    public void drawSemi(int first_triangle, int last_triangle)
    {
        drawSub(first_triangle, last_triangle);
    }
}
