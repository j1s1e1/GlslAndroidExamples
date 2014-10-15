package com.tutorial.glsltutorials.tutorials.Shapes;

import android.opengl.GLES20;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;


/**
 * Created by Jamie on 1/5/14.
 */
public class Sphere extends Shape {

    private final String vertexShaderCode =
            "attribute vec4 a_Position;" +
            "attribute vec3 a_Normal;" +		// Per-vertex normal information we will pass in.
            "varying vec3 v_Normal;" +		// This will be passed into the fragment shader.
            "varying vec3 v_Position;" +		// This will be passed into the fragment shader.
            "void main() {" +
                "v_Position = vec3(a_Position);" +
                "v_Normal = a_Normal;" +
                "gl_Position = a_Position;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 u_Color;" +
            "uniform vec3 u_LightPos;" +       	// The position of the light in eye space.
            "varying vec3 v_Position;" +		// This will be passed into the fragment shader.
            "varying vec3 v_Normal;" +         	// Interpolated normal for this fragment.
            "void main() {" +

            // Will be used for attenuation.
            "float distance = length(u_LightPos - v_Position);" +

            // Get a lighting direction vector from the light to the vertex.
            "vec3 lightVector = normalize(u_LightPos - v_Position);" +

            // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
            // pointing in the same direction then it will get max illumination.
            "float diffuse = max(dot(v_Normal, lightVector), 0.0);" +

            // Add attenuation." +
            "diffuse = diffuse * (1.0 / distance);" +

            // Add ambient lighting"
            "diffuse = diffuse + 0.2;" +

            // Multiply the color by the diffuse illumination level and texture value to get final output color."
            "gl_FragColor = (diffuse * u_Color);" +
    "}";

    public static int mProgram = -1;
    private int mPositionHandle;
    private int mNormalHandle;
    private int mColorHandle;
    private int mLightPosHandle;

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
        Move(x_in - offset.x, offset.y - y_in, offset.z - z_in );
        offset = new Vector3f(x_in, y_in, z_in);
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
        float[] coords_with_normals = new float[2*coords.length];
        int j = 0;
        for (int i = 0; i < coords.length * 2; i++)
        {
            switch (i % 6)
            {
                case 0:
                case 1:
                case 2:
                    coords_with_normals[i] = coords[j] * radius;
                    j++;
                    break;
                case 3:  coords_with_normals[i] = coords[j-3]; break;
                case 4:  coords_with_normals[i] = coords[j-2]; break;
                case 5:  coords_with_normals[i] = coords[j-1]; break;

            }

        }
        return coords_with_normals;
    }

    public Sphere(float radius_in)
    {
        this(radius_in, 1);
    }

    public Sphere(float radius_in, int new_divide_count) {
        radius = radius_in;
        vertexCoords = GetCircleCoords(radius);
        vertexCount = vertexCoords.length / COORDS_PER_VERTEX / 2;
        SetupVertexBuffer();

        if (mProgram < 0)
        {
            // prepare shaders and OpenGL program
            int vertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER,
                    vertexShaderCode);
            int fragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderCode);

            mProgram = Shader.createAndLinkProgram(vertexShader, fragmentShader,
                    new String[] {"a_Position", "a_Normal"});


            // get handle to vertex shader's vPosition member
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
            mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal");
            mLightPosHandle = GLES20.glGetUniformLocation(mProgram, "u_LightPos");
        }

    }

    private void drawSub(int first_triangle, int last_triangle)
    {
        int newVertexCount = (last_triangle - first_triangle + 1) * 3 * 3 / COORDS_PER_VERTEX;
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal");
        mLightPosHandle = GLES20.glGetUniformLocation(mProgram, "u_LightPos");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        vertexBuffer.position(first_triangle * 3 * 3 * 2);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        vertexBuffer.position(first_triangle * 3 * 3 * 2 + 3);

        // Pass in the normal information
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLES20.glVertexAttribPointer(mNormalHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // Pass in the light position in eye space.
        GLES20.glUniform3f(mLightPosHandle, 0.75f, 0.75f, 0.75f);


        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "u_Color");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, newVertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

        GLES20.glUseProgram(0);
    }

    public void draw() {
        drawSub(0, 19);
    }

    public void drawSemi(int first_triangle, int last_triangle) {
        drawSub(first_triangle, last_triangle);
    }

}