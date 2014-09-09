package com.tutorial.glsltutorials.tutorials;

import android.opengl.GLES20;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Jamie on 5/26/14.
 */
public class Tut_03_CPU_Position_Offset extends TutorialBase {

    private final String standard_vert =
    "attribute vec4 position;"+

    "void main(void)"+
    "{"+
        "gl_Position = position;"+
    " }";

    private final String standard_frag =
    "void main()"+
    "{"+
        "gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);"+
    "}";

    static float[] vertexData = new float[]{
            0.25f, 0.25f, 0.0f, 1.0f,
            0.25f, -0.25f, 0.0f, 1.0f,
            -0.25f, -0.25f, 0.0f, 1.0f,
    };

    static short[] indexData = new short[]{
            0, 1, 2
    };

    private int POSITION_STRIDE = POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;
    private int POSITION_START = 0;

    void UpdateVertexBuffer(float[] data)
    {
        vertexDataFB = VBO_Tools.InitializeVertexBuffer(data);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexDataFB.capacity()
                * BYTES_PER_FLOAT, vertexDataFB, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }


    private void InitializeProgram()
    {
        int vertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, standard_vert);
        int fragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, standard_frag);
        theProgram = Shader.createAndLinkProgram(vertexShader, fragmentShader);
        positionAttribute = GLES20.glGetAttribLocation(theProgram, "position");
    }

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        InitializeProgram();
        InitializeVertexBuffer(vertexData, indexData);
    }

    static float fXOffset;
    static float fYOffset;

    static FloatBuffer vertexBuffer;

    static void ComputePositionOffsets()
    {
        float fLoopDuration = 5.0f;
        float fScale = 3.14159f * 2.0f / fLoopDuration;
        float fElapsedTime = GetElapsedTime() / 1000f;
        float fCurrTimeThroughLoop = fElapsedTime % fLoopDuration;
        fXOffset = (float)Math.cos(fCurrTimeThroughLoop * fScale) * 0.5f;
        fYOffset = (float)Math.sin(fCurrTimeThroughLoop * fScale) * 0.5f;
    }

    void AdjustVertexData(float fXOffset, float fYOffset)
    {
        float[] fNewData = new float[vertexData.length];
        System.arraycopy(vertexData, 0, fNewData, 0, vertexData.length);
        for(int iVertex = 0; iVertex < vertexData.length; iVertex += 4)
        {
            fNewData[iVertex] += fXOffset;
            fNewData[iVertex + 1] += fYOffset;
        }
        UpdateVertexBuffer(fNewData);
    }


    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display()
    {
        fXOffset = 0.0f;
        fYOffset = 0.0f;
        ComputePositionOffsets();
        AdjustVertexData(fXOffset, fYOffset);

        GLES20.glClearColor(0.0f, 0.2f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(theProgram);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glEnableVertexAttribArray(positionAttribute);
        GLES20.glVertexAttribPointer(positionAttribute, POSITION_DATA_SIZE_IN_ELEMENTS,GLES20.GL_FLOAT,
                false, POSITION_STRIDE, POSITION_START);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        GLES20.glDisableVertexAttribArray(positionAttribute);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glUseProgram(0);
    }
}
