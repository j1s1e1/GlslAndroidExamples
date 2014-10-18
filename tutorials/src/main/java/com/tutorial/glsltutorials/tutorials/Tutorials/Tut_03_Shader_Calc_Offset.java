package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.*;

import java.nio.FloatBuffer;

/**
 * Created by Jamie on 5/26/14.
 */
public class Tut_03_Shader_Calc_Offset extends TutorialBase {
    static float[] vertexPositions = new float[]{
            0.25f, 0.25f, 0.0f, 1.0f,
            0.25f, -0.25f, 0.0f, 1.0f,
            -0.25f, -0.25f, 0.0f, 1.0f,
    };

    static int elapsedTimeUniform;
    static int positionBufferObject;

    private static int mPositionHandle;

    static FloatBuffer vertexBuffer;

    static String calcOffset_vert =
    "attribute vec4 position;"+
    "uniform float loopDuration;"+
    "uniform float time;"+

    "void main()"+
    "{"+
    "float timeScale = 3.14159 * 2.0 / loopDuration;" +

    "float currTime = mod(time, loopDuration);" +
            "vec4 totalOffset = vec4(" +
            "cos(currTime * timeScale) * 0.5," +
            "sin(currTime * timeScale) * 0.5," +
            " 0.0," +
            "0.0);" +

            "gl_Position = position + totalOffset;" +
    "}";

    static String calcColor_frag =

    "uniform float fragLoopDuration;"+
    "uniform float time;"+

    "const vec4 firstColor = vec4(1.0, 0.0, 1.0, 1.0);"+
    "const vec4 secondColor = vec4(0.0, 1.0, 0.0, 1.0);"+

    "void main()"+
    "{"+
        "float currTime = mod(time, fragLoopDuration);"+
        "float currLerp = currTime / fragLoopDuration;"+

        "gl_FragColor = mix(firstColor, secondColor, currLerp);"+
    "}";

    void InitializeProgram()
    {
        // prepare shaders and OpenGL program
        int vertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, calcOffset_vert);
        int fragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, calcColor_frag);

        theProgram = Shader.createAndLinkProgram(vertexShader, fragmentShader);

        positionAttribute = GLES20.glGetAttribLocation(theProgram, "position");

        elapsedTimeUniform = GLES20.glGetUniformLocation(theProgram, "time");


        int loopDurationUnf = GLES20.glGetUniformLocation(theProgram, "loopDuration");
        int fragLoopDurUnf = GLES20.glGetUniformLocation(theProgram, "fragLoopDuration");
        GLES20.glUseProgram(theProgram);
        GLES20.glUniform1f(loopDurationUnf, 5.0f);
        GLES20.glUniform1f(fragLoopDurUnf, 2.0f);
        GLES20.glUseProgram(0);

    }

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        InitializeProgram();
        vertexBuffer = VBO_Tools.InitializeVertexBuffer(vertexPositions);

        //glGenVertexArrays(1, &vao);
        //glBindVertexArray(vao);
    }

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.2f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(theProgram);

        GLES20.glUniform1f(elapsedTimeUniform, GetElapsedTime() / 1000.0f);

        GLES20.glEnableVertexAttribArray(positionAttribute);
        GLES20.glVertexAttribPointer(positionAttribute, 4, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        GLES20.glDisableVertexAttribArray(0);
        GLES20.glUseProgram(0);

        //GlControl.SwapBuffers();
    }
}
