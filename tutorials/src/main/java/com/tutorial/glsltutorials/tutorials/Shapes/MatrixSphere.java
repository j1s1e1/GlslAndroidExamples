package com.tutorial.glsltutorials.tutorials.Shapes;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;


/**
 * Created by Jamie on 1/5/14.
 */
public class MatrixSphere extends Shape {

    public static int mProgram = -1;
    private int positionAttribute;

    private int cameraToClipMatrixUnif;
    private static int worldToCameraMatrixUnif;
    private int modelToWorldMatrixUnif;
    private int colorUnif;

    static Matrix4f cameraToClip = new Matrix4f();
    static Matrix4f worldToCamera = new Matrix4f();
    Matrix4f modelToWorld = new Matrix4f();

    float center_x = 0f;
    float center_y = 0f;
    float center_z = 0f;

    public void SetCenter(float[] center)
    {
        SetCenter(center[0], center[1], center[2] );
    }

    public void SetCenter(float x_center, float y_center, float z_center) {
        float[] temp = GetCircleCoords(radius); // can't move cirlceCoords, so don't reinitialize
        for (int i = 0; i < temp.length; i++) {
            vertexCoords[i] = temp[i];
        }
        for (int i = 0; i < vertexCoords.length; i++) {
            switch (i % 3) {
                case 0:
                    vertexCoords[i] = vertexCoords[i] + x_center;
                    break;
                case 1:
                    vertexCoords[i] = vertexCoords[i] + y_center;
                    break;
                case 2:
                    vertexCoords[i] = vertexCoords[i] + z_center;
                    break;
            }

        }
        SetupVertexBuffer();
    }

    @Override
    public void SetOffset (float x_in, float y_in, float z_in)
    {
        Move(x_in - x_offset, y_offset - y_in, z_offset - z_in );
        x_offset = x_in;
        y_offset = y_in;
        z_offset = z_in;
    }

    public void Move(float[] coords)
    {
        Move(coords[0], coords[1], coords[2]);
    }

    public void Move(float x_move, float y_move, float z_move) {
        for (int i = 0; i < vertexCoords.length; i++) {
            switch (i % 3) {
                case 0:
                    vertexCoords[i] = vertexCoords[i] + x_move;
                    break;
                case 1:
                    vertexCoords[i] = vertexCoords[i] + y_move;
                    break;
                case 2:
                    vertexCoords[i] = vertexCoords[i] + z_move;
                    break;
            }

        }
        center_x = center_x + x_move;
        center_y = center_y + y_move;
        center_z = center_z + y_move;
        SetupVertexBuffer();
    }

    float radius;
    float angle_step = (float)(Math.PI / 6.0);

    private float[] GetCircleCoords(float radius) {
        float[] coords = Icosahedron.triangles.clone();
        for (int i = 0; i < coords.length; i++) {
            coords[i] = coords[i] * radius;
        }
        return coords;
    }

    public MatrixSphere(float radius_in)
    {
        this(radius_in, 1);
    }

    public MatrixSphere(float radius_in, int new_divide_count) {
        radius = radius_in;
        vertexCoords = GetCircleCoords(radius);
        vertexCount = vertexCoords.length / COORDS_PER_VERTEX;
        SetupVertexBuffer();

        if (mProgram < 0)
        {

            int vertexShader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, VertexShaders.PosOnlyWorldTransform_vert);
            int fragmentShader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, FragmentShaders.ColorUniform_frag);

            mProgram = Shader.createAndLinkProgram(vertexShader, fragmentShader,
                    new String[] {"a_Position", "a_Normal"});


            // get handle to vertex shader's vPosition member
            positionAttribute = GLES20.glGetAttribLocation(mProgram, "position");

            cameraToClipMatrixUnif = GLES20.glGetUniformLocation(mProgram, "cameraToClipMatrix");
            worldToCameraMatrixUnif = GLES20.glGetUniformLocation(mProgram, "worldToCameraMatrix");
            modelToWorldMatrixUnif = GLES20.glGetUniformLocation(mProgram, "modelToWorldMatrix");
            colorUnif = GLES20.glGetUniformLocation(mProgram, "baseColor");

        }
        vertexStride = vertexStride/2; // no normals here
    }

    Vector3f axis = new Vector3f(0f, 0f, 1f);
    float angle = 0;

    public void UpdateAngle(float degrees)
    {
        angle = degrees;
    }

    private void drawSub(int first_triangle, int last_triangle)
    {
        int newVertexCount = (last_triangle - first_triangle + 1) * 3 * 3 / COORDS_PER_VERTEX;
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        Matrix4f mm = Rotate(modelToWorld, axis,angle);

        GLES20.glUniformMatrix4fv(cameraToClipMatrixUnif, 1, false, cameraToClip.toArray(), 0);
        GLES20.glUniformMatrix4fv(worldToCameraMatrixUnif, 1, false, worldToCamera.toArray(), 0);
        GLES20.glUniformMatrix4fv(modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
        GLES20.glUniform4fv(colorUnif, 1, color, 0);

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionAttribute);

        vertexBuffer.position(first_triangle * 3 * 3);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionAttribute, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, newVertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionAttribute);

        GLES20.glUseProgram(0);
    }

    public void draw() {
        drawSub(0, 19);
    }

    public void drawSemi(int first_triangle, int last_triangle) {
        drawSub(first_triangle, last_triangle);
    }

    public static void MoveWorld(float newx, float newy, float newz)
    {
        worldToCamera.M41 = newx;
        worldToCamera.M42 = newy;
        worldToCamera.M43 = newz;
    }

    public void MoveModel(float newx, float newy, float newz)
    {
        modelToWorld.M41 = newx;
        modelToWorld.M42 = newy;
        modelToWorld.M43 = newz;
    }

}