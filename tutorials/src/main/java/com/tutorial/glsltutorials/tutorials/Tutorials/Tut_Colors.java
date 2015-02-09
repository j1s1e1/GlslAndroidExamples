package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;

/**
 * Created by Jamie on 6/5/14.
 */
public class Tut_Colors extends TutorialBase {

    public static String VertexColor_vert =

    "attribute vec4 position;" +
    "attribute vec4 color;" +
    "uniform float xScale;" +
    "uniform float xOffset;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_Position = vec4(position.x * xScale + xOffset, position.y, position.z, position.w);" +
        "theColor = color;" +
    "}";

    public static String VertexColor_frag =
    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = theColor;" +
    "}";

    private final int vertexCount = vertexData.length / 2 / COORDS_PER_VERTEX;
    private final int COLOR_START = vertexCount * POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;

    static float triangleWidth = 2f / 12f  ;

    static float[] vertexData = new float[] {
            // Position
            -1f + triangleWidth * 0f,    -1f, 0f, 1f,
            -1f + triangleWidth * 0f,  	  1f, 0f, 1f,
            -1f + triangleWidth * 1f,    -1f, 0f, 1f,
            -1f + triangleWidth * 1f,  	  1f, 0f, 1f,
            -1f + triangleWidth * 2f,    -1f, 0f, 1f,
            -1f + triangleWidth * 2f,  	  1f, 0f, 1f,
            -1f + triangleWidth * 3f,    -1f, 0f, 1f,
            -1f + triangleWidth * 3f,  	  1f, 0f, 1f,
            -1f + triangleWidth * 4f,    -1f, 0f, 1f,
            -1f + triangleWidth * 4f,  	  1f, 0f, 1f,
            -1f + triangleWidth * 5f,    -1f, 0f, 1f,
            -1f + triangleWidth * 5f,  	  1f, 0f, 1f,
            -1f + triangleWidth * 6f,    -1f, 0f, 1f,
            -1f + triangleWidth * 6f,  	  1f, 0f, 1f,
            -1f + triangleWidth * 7f,    -1f, 0f, 1f,
            -1f + triangleWidth * 7f,  	  1f, 0f, 1f,
            -1f + triangleWidth * 8f,    -1f, 0f, 1f,
            -1f + triangleWidth * 8f,  	  1f, 0f, 1f,
            -1f + triangleWidth * 9f,    -1f, 0f, 1f,
            -1f + triangleWidth * 9f,  	  1f, 0f, 1f,
            -1f + triangleWidth * 10f,   -1f, 0f, 1f,
            -1f + triangleWidth * 10f,    1f, 0f, 1f,
            -1f + triangleWidth * 11f,   -1f, 0f, 1f,
            -1f + triangleWidth * 11f,    1f, 0f, 1f,
            -1f + triangleWidth * 12f,   -1f, 0f, 1f,
            -1f + triangleWidth * 12f,    1f, 0f, 1f,
            -1f + triangleWidth * 13f,   -1f, 0f, 1f,
            -1f + triangleWidth * 13f,    1f, 0f, 1f,
            -1f + triangleWidth * 14f,   -1f, 0f, 1f,
            -1f + triangleWidth * 14f,    1f, 0f, 1f,
            -1f + triangleWidth * 15f,   -1f, 0f, 1f,
            -1f + triangleWidth * 15f,    1f, 0f, 1f,
            -1f + triangleWidth * 16f,   -1f, 0f, 1f,
            -1f + triangleWidth * 16f,    1f, 0f, 1f,
            // Color
            1.0f,    0.0f, 0.0f, 1.0f,
            1.0f,    0.0f, 0.0f, 1.0f,
            1.0f,    0.5f, 0.0f, 1.0f,
            1.0f,    0.5f, 0.0f, 1.0f,
            1.0f,    1.0f, 0.0f, 1.0f,
            1.0f,    1.0f, 0.0f, 1.0f,
            0.5f,    1.0f, 0.0f, 1.0f,
            0.5f,    1.0f, 0.0f, 1.0f,
            0.0f,    1.0f, 0.0f, 1.0f,
            0.0f,    1.0f, 0.0f, 1.0f,
            0.0f,    1.0f, 0.5f, 1.0f,
            0.0f,    1.0f, 0.5f, 1.0f,
            0.0f,    1.0f, 1.0f, 1.0f,
            0.0f,    1.0f, 1.0f, 1.0f,
            0.0f,    0.5f, 1.0f, 1.0f,
            0.0f,    0.5f, 1.0f, 1.0f,
            0.0f,    0.0f, 1.0f, 1.0f,
            0.0f,    0.0f, 1.0f, 1.0f,
            0.5f,    0.0f, 1.0f, 1.0f,
            0.5f,    0.0f, 1.0f, 1.0f,
            1.0f,    0.0f, 1.0f, 1.0f,
            1.0f,    0.0f, 1.0f, 1.0f,
            1.0f,    0.0f, 0.5f, 1.0f,
            1.0f,    0.0f, 0.5f, 1.0f,
            1.0f,    0.0f, 0.0f, 1.0f,
            1.0f,    0.0f, 0.0f, 1.0f,
            0.5f,    0.5f, 0.0f, 1.0f,
            0.5f,    0.5f, 0.0f, 1.0f,
            0.0f,    0.5f, 0.5f, 1.0f,
            0.0f,    0.5f, 0.5f, 1.0f,
            0.5f,    0.0f, 0.5f, 1.0f,
            0.5f,    0.0f, 0.5f, 1.0f,
            0.5f,    0.5f, 0.5f, 1.0f,
            0.5f,    0.5f, 0.5f, 1.0f,
    };

    private static float xOffset = 0f;
    private static float xScale = 1f;
    private static float minScale = 0.25f;
    private static float maxScale = 4f;
    private static int xOffsetUnif;
    private static int xScaleUnif;

    void InitializeProgram()
    {
        int vertex_shader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, VertexColor_vert);
        int fragment_shader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, VertexColor_frag);
        theProgram = Shader.createAndLinkProgram(vertex_shader, fragment_shader);
        positionAttribute = GLES20.glGetAttribLocation(theProgram, "position");
        colorAttribute = GLES20.glGetAttribLocation(theProgram, "color");
        xOffsetUnif = GLES20.glGetUniformLocation(theProgram, "xOffset");
        xScaleUnif = GLES20.glGetUniformLocation(theProgram, "xScale");
    }

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        InitializeProgram();
        initializeVertexBuffer(vertexData);
    }

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(theProgram);

        GLES20.glUniform1f(xOffsetUnif, xOffset);
        GLES20.glUniform1f(xScaleUnif, xScale);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glEnableVertexAttribArray(positionAttribute);
        GLES20.glEnableVertexAttribArray(colorAttribute);
        GLES20.glVertexAttribPointer(positionAttribute, POSITION_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false,
                POSITION_STRIDE, 0);
        GLES20.glVertexAttribPointer(colorAttribute, COLOR_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false,
                COLOR_STRIDE, COLOR_START);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(positionAttribute);
        GLES20.glDisableVertexAttribArray(colorAttribute);
        GLES20.glUseProgram(0);
    }

    public void setScale(float scale) {
        if ((xScale * scale) > minScale)
        {
            if ((xScale * scale) < maxScale) {
                xScale = xScale * scale;
            }
        }
    }

    public void scroll(float distanceX, float distanceY)
    {
        xOffset = xOffset - distanceX / width;
    }

    public void receiveMessage(String message)
    {
        String[] words = message.split(" ");
        switch (words[0])
        {
            case "ZoomIn": setScale(1.05f);  break;
            case "ZoomOut": setScale(1f / 1.05f);  break;
            case "ShiftRight": xOffset = xOffset + 0.05f; break;
            case "ShiftLeft": xOffset = xOffset - 0.05f; break;
            case "xOffset":
                if (words.length == 2) {
                    xOffset = Float.parseFloat(words[1]);
                }
                if (words.length == 3) {
                    if (words[1].equals("add")) {
                        xOffset = xOffset + Float.parseFloat(words[2]);
                    }
                    if (words[1].equals("sub") | words[1].equals("subtract")) {
                        xOffset = xOffset - Float.parseFloat(words[2]);
                    }
                }
                break;
            case "xScale":
                if (words.length == 2) {
                    xScale = Float.parseFloat(words[1]);
                }
                break;
        }
    }
}
