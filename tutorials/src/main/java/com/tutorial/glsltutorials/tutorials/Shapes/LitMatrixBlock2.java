package com.tutorial.glsltutorials.tutorials.Shapes;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;

/**
 * Created by jamie on 10/12/14.
 */
public class LitMatrixBlock2 extends Shape {
    Matrix4f cameraToClip = Matrix4f.Identity();
    Matrix4f modelToWorld = Matrix4f.Identity();

    Vector3f size;

    //		  2-------3
    //		 /|		 /|
    //		0-|-----1 |
    //		| 6-----|-7
    //		|/ 		|/ 
    //	    4-------5
    //


    static short[] lmbIndexData = new short[] {	0, 1, 2, 1, 3, 2,
            4, 6, 5, 5, 6, 7,
            3, 1, 5, 3, 5, 7,
            4, 0, 6, 6, 0, 2,
            0, 4, 1, 1, 4, 5,
            6, 2, 3, 6, 3, 7,};

    static float RIGHT_EXTENT = 0.5f;
    static float LEFT_EXTENT = -RIGHT_EXTENT;
    static float TOP_EXTENT = 0.5f;
    static float BOTTOM_EXTENT = -TOP_EXTENT;
    static float FRONT_EXTENT = 0.5f;
    static float REAR_EXTENT = -FRONT_EXTENT;

    static float[] lmbVertexData = new float[] { 	LEFT_EXTENT, TOP_EXTENT, FRONT_EXTENT,
            RIGHT_EXTENT, TOP_EXTENT, FRONT_EXTENT,
            LEFT_EXTENT, TOP_EXTENT, REAR_EXTENT,
            RIGHT_EXTENT, TOP_EXTENT, REAR_EXTENT,
            LEFT_EXTENT, BOTTOM_EXTENT, FRONT_EXTENT,
            RIGHT_EXTENT, BOTTOM_EXTENT, FRONT_EXTENT,
            LEFT_EXTENT, BOTTOM_EXTENT, REAR_EXTENT,
            RIGHT_EXTENT, BOTTOM_EXTENT, REAR_EXTENT,
    };

    String VertexShader = VertexShaders.PosOnlyWorldTransform_vert;
    String FragmentShader = FragmentShaders.ColorUniform_frag;

    int progarmNumber;

    public LitMatrixBlock2 (Vector3f sizeIn, float[] colorIn)
    {
        size = sizeIn;
        color = colorIn;
        modelToWorld.M11 = size.x;
        modelToWorld.M22 = size.y;
        modelToWorld.M33 = size.z;

        progarmNumber = Programs.AddProgram(VertexShader, FragmentShader);

        vertexCount = 2 * 3 * 6;
        vertexStride = 3 * 4; // no color for now
        // fill in index data
        indexData = lmbIndexData;

        // fill in vertex data
        vertexData = lmbVertexData; //  GetVertexData();

        InitializeVertexBuffer();
    }

    private float[] GetVertexData()
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

    public void Draw()
    {
        Matrix4f mm = Rotate(modelToWorld, axis, angle);
        mm.M41 = offset.x;
        mm.M42 = offset.y;
        mm.M43 = offset.z;

        Programs.Draw(progarmNumber, vertexBufferObject, indexBufferObject, cameraToClip, worldToCamera, mm,
                indexData.length, color, COORDS_PER_VERTEX, vertexStride);
    }
   
}
