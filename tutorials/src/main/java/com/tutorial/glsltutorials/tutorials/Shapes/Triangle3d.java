package com.tutorial.glsltutorials.tutorials.Shapes;

import android.opengl.GLES20;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.nio.FloatBuffer;

/**
 * Created by Jamie on 4/30/14.
 */
public class Triangle3d extends Shape {
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

        Vector3f[] vertices;
        boolean drawable = false;

        private float[] getTriangleCoords()
        {
            float[] coords_with_normals = new float[18];
            for (int i = 0; i < coords_with_normals.length; i++) {
                switch (i % 6) {
                    case 0:	coords_with_normals[i] = vertices[i/6].x; break;
                    case 1: coords_with_normals[i] = vertices[i/6].y; break;
                    case 2: coords_with_normals[i] = vertices[i/6].z; break;
                    case 3: coords_with_normals[i] = vertices[i/6].normalize().x; break;
                    case 4: coords_with_normals[i] = vertices[i / 6].normalize().y; break;
                    case 5: coords_with_normals[i] = vertices[i / 6].normalize().z; break;

                }
            }
            return coords_with_normals;
        }

        public Triangle3d (Vector3f vertex1, Vector3f vertex2, Vector3f vertex3)
        {
            this(vertex1, vertex2, vertex3, false);
        }


        public Triangle3d (Vector3f vertex1, Vector3f vertex2, Vector3f vertex3, boolean drawable_in)
        {
            drawable = drawable_in;
            vertices = new Vector3f[3];
            vertices[0] = vertex1;
            vertices[1] = vertex2;
            vertices[2] = vertex3;
            vertexCount = 3;
            if (drawable)  // some triangle just used in other structures
            {
                vertexCoords = getTriangleCoords();
                setupVertexBuffer();

                if (mProgram < 0)
                {

                    int testVertexShader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
                    int testFragmentShader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

                    // prepare shaders and OpenGL program
                    int vertexShader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
                    int fragmentShader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

                    mProgram = Shader.createAndLinkProgram(testVertexShader, testFragmentShader,
                            new String[] {"a_Position", "a_Normal"});


                    // get handle to vertex shader's vPosition member
                    mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
                    mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal");
                    mLightPosHandle = GLES20.glGetUniformLocation(mProgram, "u_LightPos");
                }
            }
        }

        public float[] getVertexFloats()
        {
            float[] vertex_floats = new float[9];
            System.arraycopy(vertices[0].getFloats(), 0, vertex_floats, 0, 3);
            System.arraycopy(vertices[1].getFloats (), 0, vertex_floats, 3, 3);
            System.arraycopy(vertices[2].getFloats (), 0, vertex_floats, 6, 3);
            return vertex_floats;
        }

        public Triangle3d[] create4Triangles()
        {
            Triangle3d[] four_triangles = new Triangle3d[4];
            Vector3f[] new_vertexes = new Vector3f[3];
            new_vertexes[0] = (vertices[0].add(vertices[1])).divide(2);
            new_vertexes[1] = (vertices[1].add(vertices[2])).divide(2);
            new_vertexes[2] = (vertices[2].add(vertices[0])).divide(2);
            four_triangles[0] = new Triangle3d(vertices[0], new_vertexes[0], new_vertexes[2]);
            four_triangles[1] = new Triangle3d(new_vertexes[2], new_vertexes[0], new_vertexes[1]);
            four_triangles[2] = new Triangle3d(new_vertexes[0], vertices[1], new_vertexes[1]);
            four_triangles[3] = new Triangle3d(new_vertexes[2], new_vertexes[1], vertices[2]);
            return four_triangles;
        }

    public void normalizeVertices()
    {
        vertices[0] = vertices[0].normalize();
        vertices[1] = vertices[1].normalize();
        vertices[2] = vertices[2].normalize();
    }

    @Override
    public void setOffset (float x_in, float y_in, float z_in)
    {
        move(x_in - x, y - y_in, z - z_in );
        x = x_in;
        y = y_in;
        z = z_in;
    }

    public void move(float x_move, float y_move, float z_move) {
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
        setupVertexBuffer();
    }

    public void draw() {
        if (drawable)
        {
            // Add program to OpenGL environment
            GLES20.glUseProgram(mProgram);

            // get handle to vertex shader's vPosition member
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
            mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal");
            mLightPosHandle = GLES20.glGetUniformLocation(mProgram, "u_LightPos");

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(mPositionHandle);

            vertexBuffer.position(0);

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT, false,
                    vertexStride, vertexBuffer);

            vertexBuffer.position(3);

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
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(mPositionHandle);
        }
    }

}
