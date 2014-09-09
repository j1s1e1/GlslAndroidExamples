package com.tutorial.glsltutorials.tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VBO_Tools;
import com.tutorial.glsltutorials.tutorials.Geometry.*;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by Jamie on 5/27/14.
 */
public class Tut_04_MatrixPerspective extends TutorialBase  {

    static String MatrixPerspective_vert =
    "attribute vec4 position;" +
    "attribute vec4 color;" +

    "uniform vec2 offset;" +
    "uniform mat4 perspectiveMatrix;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "vec4 cameraPos = position + vec4(offset.x, offset.y, 0.0, 0.0);" +
        "gl_Position = perspectiveMatrix * cameraPos;" +
        "theColor = color;" +
    "}";

    static String StandardColors_frag =
    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = theColor;" +
    "}";

    static short[] indexData = new short[]{
            0,1,2,
            3,4,5,
            6,7,8,
            9,10,11,
            12,13,14,
            15,16,17,
            18,19,20,
            21,22,23,
            24,25,26,
            27,28,29,
            30,31,32,
            33,34,35
    };

    static float[] vertexData = new float[]{

            0.25f,  0.25f, 0.75f, 1.0f,
            0.25f, -0.25f, 0.75f, 1.0f,
            -0.25f,  0.25f, 0.75f, 1.0f,

            0.25f, -0.25f, 0.75f, 1.0f,
            -0.25f, -0.25f, 0.75f, 1.0f,
            -0.25f,  0.25f, 0.75f, 1.0f,

            0.25f,  0.25f, -0.75f, 1.0f,
            -0.25f,  0.25f, -0.75f, 1.0f,
            0.25f, -0.25f, -0.75f, 1.0f,

            0.25f, -0.25f, -0.75f, 1.0f,
            -0.25f,  0.25f, -0.75f, 1.0f,
            -0.25f, -0.25f, -0.75f, 1.0f,

            -0.25f,  0.25f,  0.75f, 1.0f,
            -0.25f, -0.25f,  0.75f, 1.0f,
            -0.25f, -0.25f, -0.75f, 1.0f,

            -0.25f,  0.25f,  0.75f, 1.0f,
            -0.25f, -0.25f, -0.75f, 1.0f,
            -0.25f,  0.25f, -0.75f, 1.0f,

            0.25f,  0.25f,  0.75f, 1.0f,
            0.25f, -0.25f, -0.75f, 1.0f,
            0.25f, -0.25f,  0.75f, 1.0f,

            0.25f,  0.25f,  0.75f, 1.0f,
            0.25f,  0.25f, -0.75f, 1.0f,
            0.25f, -0.25f, -0.75f, 1.0f,

            0.25f,  0.25f, -0.75f, 1.0f,
            0.25f,  0.25f,  0.75f, 1.0f,
            -0.25f,  0.25f,  0.75f, 1.0f,

            0.25f,  0.25f, -0.75f, 1.0f,
            -0.25f,  0.25f,  0.75f, 1.0f,
            -0.25f,  0.25f, -0.75f, 1.0f,

            0.25f, -0.25f, -0.75f, 1.0f,
            -0.25f, -0.25f,  0.75f, 1.0f,
            0.25f, -0.25f,  0.75f, 1.0f,

            0.25f, -0.25f, -0.75f, 1.0f,
            -0.25f, -0.25f, -0.75f, 1.0f,
            -0.25f, -0.25f,  0.75f, 1.0f,

            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            0.8f, 0.8f, 0.8f, 1.0f,
            0.8f, 0.8f, 0.8f, 1.0f,
            0.8f, 0.8f, 0.8f, 1.0f,

            0.8f, 0.8f, 0.8f, 1.0f,
            0.8f, 0.8f, 0.8f, 1.0f,
            0.8f, 0.8f, 0.8f, 1.0f,

            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,

            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,

            0.5f, 0.5f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.0f, 1.0f,

            0.5f, 0.5f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.0f, 1.0f,

            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,

            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,

    };

    private final int vertexCount = vertexData.length / 2 / COORDS_PER_VERTEX;

    private final int COLOR_START = vertexCount * POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;


    static int offsetUniform;
    static int perspectiveMatrixUnif;

    void InitializeProgram()
    {
        int vertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, MatrixPerspective_vert);
        int fragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, StandardColors_frag);
        theProgram = Shader.createAndLinkProgram(vertexShader, fragmentShader);

        offsetUniform = GLES20.glGetUniformLocation(theProgram, "offset");
        perspectiveMatrixUnif = GLES20.glGetUniformLocation(theProgram, "perspectiveMatrix");

        float fFrustumScale = 1.0f;
        float fzNear = 0.5f;
        float fzFar = 3.0f;

        Matrix4f theMatrix = new Matrix4f();

        theMatrix.M11 = fFrustumScale;
        theMatrix.M22 = fFrustumScale;
        theMatrix.M33 = (fzFar + fzNear) / (fzNear - fzFar);
        theMatrix.M43 = (2 * fzFar * fzNear) / (fzNear - fzFar);
        theMatrix.M34 = -1.0f;

        //theMatrix = Matrix4f.Identity();

        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(perspectiveMatrixUnif, 1, false, theMatrix.toArray(), 0);
        positionAttribute = GLES20.glGetAttribLocation(theProgram, "position");
        colorAttribute = GLES20.glGetAttribLocation(theProgram, "color");
        GLES20.glUseProgram(0);
    }

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        InitializeProgram();
        InitializeVertexBuffer(vertexData, indexData);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_FRONT);
        GLES20.glFrontFace(GLES20.GL_CW);
    }

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(theProgram);

        GLES20.glUniform2f(offsetUniform, 0.5f, 0.5f);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        // Bind Attributes
        GLES20.glEnableVertexAttribArray(positionAttribute);
        GLES20.glEnableVertexAttribArray(colorAttribute);
        GLES20.glVertexAttribPointer(positionAttribute, POSITION_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT,
                false, POSITION_STRIDE, 0);
        GLES20.glVertexAttribPointer(colorAttribute, COLOR_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT,
                false, COLOR_STRIDE,  COLOR_START);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(positionAttribute);
        GLES20.glDisableVertexAttribArray(colorAttribute);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glUseProgram(0);

    }

}
