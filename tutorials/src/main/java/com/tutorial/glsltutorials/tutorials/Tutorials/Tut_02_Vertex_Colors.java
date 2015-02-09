package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;

/**
 * Created by Jamie on 6/5/14.
 */
public class Tut_02_Vertex_Colors extends TutorialBase {

    public static String VertexColor_vert =

    "attribute vec4 position;" +
    "attribute vec4 color;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_Position = position;" +
        "theColor = color;" +
    "}";

    public static String VertexColor_frag =
    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = theColor;" +
    "}";

    void InitializeProgram()
    {
        int vertex_shader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, VertexColor_vert);
        int fragment_shader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, VertexColor_frag);
        theProgram = Shader.createAndLinkProgram(vertex_shader, fragment_shader);
        positionAttribute = GLES20.glGetAttribLocation(theProgram, "position");
        colorAttribute = GLES20.glGetAttribLocation(theProgram, "color");
    }

    private final int vertexCount = vertexData.length / 2 / COORDS_PER_VERTEX;
    private final int COLOR_START = vertexCount * POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;

    static float[] vertexData = {
            0.0f,    0.5f, 0.0f, 1.0f,
            0.5f, -0.366f, 0.0f, 1.0f,
            -0.5f, -0.366f, 0.0f, 1.0f,
            1.0f,    0.0f, 0.0f, 1.0f,
            0.0f,    1.0f, 0.0f, 1.0f,
            0.0f,    0.0f, 1.0f, 1.0f,
    };

    static short[] indexData = { 0, 1, 2 };


    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        InitializeProgram();
        initializeVertexBuffer(vertexData, indexData);
    }

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(theProgram);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glEnableVertexAttribArray(positionAttribute);
        GLES20.glEnableVertexAttribArray(colorAttribute);
        GLES20.glVertexAttribPointer(positionAttribute, POSITION_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false,
                POSITION_STRIDE, 0);
        GLES20.glVertexAttribPointer(colorAttribute, COLOR_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false,
                COLOR_STRIDE, COLOR_START);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        GLES20.glDisableVertexAttribArray(positionAttribute);
        GLES20.glDisableVertexAttribArray(colorAttribute);
        GLES20.glUseProgram(0);
    }
}
