package com.tutorial.glsltutorials.tutorials.Shapes;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {

    private final String vertexShaderCode =
                    "uniform mat4 uRotation;" +
                    "attribute vec4 vPosition;" +
                     "uniform vec3 uTranslate;" +

                    "void main()" +
                    "{" +
                        "gl_Position = uRotation * vPosition + vec4(uTranslate,0.0);" +
                    "}";

    private final String vertexShaderCode_with_MVP =
        "precision mediump float;" +
        "uniform mat4 uMVPMatrix;" +
        "uniform mat4 uRotation;" +
        "attribute vec2 a_texCoords;" +
        "attribute vec3 vPosition;" +
        "varying v_texCoord;" +

        "void main()" +
        "{" +
            "v_texCoord = a_texCoords;" +
            "gl_Position = uMVPMatrix* uRotation * vec4(vPosition,1.0);" +
        "}";

    private final String vertexShaderCodeOld =
            "attribute vec4 vPosition;" +
            "attribute float a_angle;" +
            "varying mat4 v_rotationMatrix;" +
                "void main() {" +
                    "float cos = cos(a_angle);" +
                    "float sin = sin(a_angle);" +
                    "mat4 transInMat = mat4(1.0, 0.0, 0.0, 0.0," +
                        "0.0, 1.0, 0.0, 0.0," +
                        "0.0, 0.0, 1.0, 0.0," +
                        "0.5, 0.5, 0.0, 1.0);" +
                    "mat4 rotMat = mat4(cos, -sin, 0.0, 0.0," +
                        "sin, cos, 0.0, 0.0," +
                        "0.0, 0.0, 1.0, 0.0," +
                        "0.0, 0.0, 0.0, 1.0);" +
                        "mat4 resultMat = transInMat * rotMat;" +
                        "resultMat[3][0] = resultMat[3][0] + resultMat[0][0] * -0.5 + resultMat[1][0] * -0.5;" +
                        "resultMat[3][1] = resultMat[3][1] + resultMat[0][1] * -0.5 + resultMat[1][1] * -0.5;" +
                        "resultMat[3][2] = resultMat[3][2] + resultMat[0][2] * -0.5 + resultMat[1][2] * -0.5;" +
                        "v_rotationMatrix = resultMat;" +
                    "gl_Position = vPosition;" +
                "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private FloatBuffer vertexBuffer;
    public static int mProgram = -1;
    private static int mPositionHandle;
    private static int mColorHandle;
    private static int uRotationHandle;
    private static int uTranslateHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    float[] triangleCoords = { // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f,   // top
            -0.5f, -0.311004243f, 0.0f,   // bottom left
            0.5f, -0.311004243f, 0.0f    // bottom right
    };
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    public void SetColor(float red, float green, float blue) {
        color[0] = red;
        color[1] = green;
        color[2] = blue;
    }

    float[] offset = new float[]{0,0,0};
    public void Move(float x_move, float y_move) {
        offset[0] = offset[0] + x_move;
        offset[1] = offset[1] + y_move;
    }

    public Triangle(float[] coords) {
        triangleCoords = coords;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        if (mProgram < 0)
        {
            // prepare shaders and OpenGL program
            int vertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER,
                    vertexShaderCode);
            int fragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderCode);

            mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
            GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
            GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
            GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

            uRotationHandle = GLES20.glGetUniformLocation(mProgram, "uRotation");
            uTranslateHandle = GLES20.glGetUniformLocation(mProgram, "uTranslate");
        }
        rotate(0, new float[]{ 0.0f,0.0f,1.0f});

    }

    float[] rotationMat;

    public void rotate(float angle, float[] axis)
    {
        rotationMat = new float[16];
        Matrix.setIdentityM(rotationMat,0);
        Matrix.rotateM(rotationMat, 0, angle, axis[0], axis[1], axis[2]);
    }

    public void draw() {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(uRotationHandle, 1, false, rotationMat, 0);

        GLES20.glUniform3fv(uTranslateHandle, 1, offset, 0);

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}

