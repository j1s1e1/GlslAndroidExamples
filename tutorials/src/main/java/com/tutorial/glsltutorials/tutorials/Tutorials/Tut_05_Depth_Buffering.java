package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;

/**
 * Created by Jamie on 6/7/14.
 */
public class Tut_05_Depth_Buffering extends TutorialBase {
    public String standard_vert5 =
    "attribute vec4 position;" +
    "attribute vec4 color;" +

    "uniform vec3 offset;" +
    "uniform mat4 perspectiveMatrix;" +

    "varying vec4 theColor;" +

    "void main(void)" +
    "{" +
        "vec4 cameraPos = position + vec4(offset.x, offset.y, offset.z, 0.0);" +

        "gl_Position = perspectiveMatrix * cameraPos;" +
        "theColor = color;" +
    "}";

    public String StandardColors_frag =
    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = theColor;" +
    "}";

    int numberOfVertices = 36;
    private int COLOR_START = numberOfVertices * POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;
    float RIGHT_EXTENT = 0.8f;
    float LEFT_EXTENT = -RIGHT_EXTENT;
    float TOP_EXTENT = 0.20f;
    float MIDDLE_EXTENT = 0.0f;
    float BOTTOM_EXTENT = -TOP_EXTENT;
    float FRONT_EXTENT = -1.25f;
    float REAR_EXTENT = -1.75f;
    static float[] GREEN_COLOR = { 0.0f, 1.0f, 0.0f, 1.0f};
    static float[] BLUE_COLOR = { 0.0f, 0.0f, 1.0f, 1.0f};
    static float[] RED_COLOR = { 1.0f, 0.0f, 0.0f, 1.0f};
    static float[] GREY_COLOR = { 0.8f, 0.8f, 0.8f, 1.0f};
    static float[] BROWN_COLOR = { 0.5f, 0.5f, 0.0f, 1.0f};
    float[] vertexData = {
            //Object 1 positions
            LEFT_EXTENT,	TOP_EXTENT,		REAR_EXTENT, 1.0f,
            LEFT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT, 1.0f,
            RIGHT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT, 1.0f,

            RIGHT_EXTENT,	TOP_EXTENT,		REAR_EXTENT, 1.0f,
            LEFT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT, 1.0f,
            LEFT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT, 1.0f,

            RIGHT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT, 1.0f,
            RIGHT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT, 1.0f,
            LEFT_EXTENT,	TOP_EXTENT,		REAR_EXTENT, 1.0f,

            LEFT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT, 1.0f,
            LEFT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT, 1.0f,
            RIGHT_EXTENT,	TOP_EXTENT,		REAR_EXTENT, 1.0f,

            RIGHT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT, 1.0f,
            RIGHT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT, 1.0f,
            LEFT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT, 1.0f,

            LEFT_EXTENT,	TOP_EXTENT,		REAR_EXTENT, 1.0f,
            RIGHT_EXTENT,	TOP_EXTENT,		REAR_EXTENT, 1.0f,
            RIGHT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT, 1.0f,

            //Object 2 positions
            TOP_EXTENT,		RIGHT_EXTENT,	REAR_EXTENT, 1.0f,
            MIDDLE_EXTENT,	RIGHT_EXTENT,	FRONT_EXTENT, 1.0f,
            MIDDLE_EXTENT,	LEFT_EXTENT,	FRONT_EXTENT, 1.0f,
            TOP_EXTENT,		LEFT_EXTENT,	REAR_EXTENT, 1.0f,

            BOTTOM_EXTENT,	RIGHT_EXTENT,	REAR_EXTENT, 1.0f,
            MIDDLE_EXTENT,	RIGHT_EXTENT,	FRONT_EXTENT, 1.0f,
            MIDDLE_EXTENT,	LEFT_EXTENT,	FRONT_EXTENT, 1.0f,
            BOTTOM_EXTENT,	LEFT_EXTENT,	REAR_EXTENT, 1.0f,

            TOP_EXTENT,		RIGHT_EXTENT,	REAR_EXTENT, 1.0f,
            MIDDLE_EXTENT,	RIGHT_EXTENT,	FRONT_EXTENT, 1.0f,
            BOTTOM_EXTENT,	RIGHT_EXTENT,	REAR_EXTENT, 1.0f,

            TOP_EXTENT,		LEFT_EXTENT,	REAR_EXTENT, 1.0f,
            MIDDLE_EXTENT,	LEFT_EXTENT,	FRONT_EXTENT, 1.0f,
            BOTTOM_EXTENT,	LEFT_EXTENT,	REAR_EXTENT, 1.0f,

            BOTTOM_EXTENT,	RIGHT_EXTENT,	REAR_EXTENT, 1.0f,
            TOP_EXTENT,		RIGHT_EXTENT,	REAR_EXTENT, 1.0f,
            TOP_EXTENT,		LEFT_EXTENT,	REAR_EXTENT, 1.0f,
            BOTTOM_EXTENT,	LEFT_EXTENT,	REAR_EXTENT, 1.0f,

            //Object 1 colors
            GREEN_COLOR[0], GREEN_COLOR[1], GREEN_COLOR[2], GREEN_COLOR[3],
            GREEN_COLOR[0], GREEN_COLOR[1], GREEN_COLOR[2], GREEN_COLOR[3],
            GREEN_COLOR[0], GREEN_COLOR[1], GREEN_COLOR[2], GREEN_COLOR[3],
            GREEN_COLOR[0], GREEN_COLOR[1], GREEN_COLOR[2], GREEN_COLOR[3],

            BLUE_COLOR[0], BLUE_COLOR[1], BLUE_COLOR[2], BLUE_COLOR[3],
            BLUE_COLOR[0], BLUE_COLOR[1], BLUE_COLOR[2], BLUE_COLOR[3],
            BLUE_COLOR[0], BLUE_COLOR[1], BLUE_COLOR[2], BLUE_COLOR[3],
            BLUE_COLOR[0], BLUE_COLOR[1], BLUE_COLOR[2], BLUE_COLOR[3],

            RED_COLOR[0], RED_COLOR[1], RED_COLOR[2], RED_COLOR[3],
            RED_COLOR[0], RED_COLOR[1], RED_COLOR[2], RED_COLOR[3],
            RED_COLOR[0], RED_COLOR[1], RED_COLOR[2], RED_COLOR[3],

            GREY_COLOR[0], GREY_COLOR[1], GREY_COLOR[2], GREY_COLOR[3],
            GREY_COLOR[0], GREY_COLOR[1], GREY_COLOR[2], GREY_COLOR[3],
            GREY_COLOR[0], GREY_COLOR[1], GREY_COLOR[2], GREY_COLOR[3],

            BROWN_COLOR[0], BROWN_COLOR[1], BROWN_COLOR[2], BROWN_COLOR[3],
            BROWN_COLOR[0], BROWN_COLOR[1], BROWN_COLOR[2], BROWN_COLOR[3],
            BROWN_COLOR[0], BROWN_COLOR[1], BROWN_COLOR[2], BROWN_COLOR[3],
            BROWN_COLOR[0], BROWN_COLOR[1], BROWN_COLOR[2], BROWN_COLOR[3],

            RED_COLOR[0], RED_COLOR[1], RED_COLOR[2], RED_COLOR[3],
            RED_COLOR[0], RED_COLOR[1], RED_COLOR[2], RED_COLOR[3],
            RED_COLOR[0], RED_COLOR[1], RED_COLOR[2], RED_COLOR[3],
            RED_COLOR[0], RED_COLOR[1], RED_COLOR[2], RED_COLOR[3],

            BROWN_COLOR[0], BROWN_COLOR[1], BROWN_COLOR[2], BROWN_COLOR[3],
            BROWN_COLOR[0], BROWN_COLOR[1], BROWN_COLOR[2], BROWN_COLOR[3],
            BROWN_COLOR[0], BROWN_COLOR[1], BROWN_COLOR[2], BROWN_COLOR[3],
            BROWN_COLOR[0], BROWN_COLOR[1], BROWN_COLOR[2], BROWN_COLOR[3],

            BLUE_COLOR[0], BLUE_COLOR[1], BLUE_COLOR[2], BLUE_COLOR[3],
            BLUE_COLOR[0], BLUE_COLOR[1], BLUE_COLOR[2], BLUE_COLOR[3],
            BLUE_COLOR[0], BLUE_COLOR[1], BLUE_COLOR[2], BLUE_COLOR[3],

            GREEN_COLOR[0], GREEN_COLOR[1], GREEN_COLOR[2], GREEN_COLOR[3],
            GREEN_COLOR[0], GREEN_COLOR[1], GREEN_COLOR[2], GREEN_COLOR[3],
            GREEN_COLOR[0], GREEN_COLOR[1], GREEN_COLOR[2], GREEN_COLOR[3],

            GREY_COLOR[0], GREY_COLOR[1], GREY_COLOR[2], GREY_COLOR[3],
            GREY_COLOR[0], GREY_COLOR[1], GREY_COLOR[2], GREY_COLOR[3],
            GREY_COLOR[0], GREY_COLOR[1], GREY_COLOR[2], GREY_COLOR[3],
            GREY_COLOR[0], GREY_COLOR[1], GREY_COLOR[2], GREY_COLOR[3],
    };
    static short[] indexData =
            {
                    0, 2, 1,
                    3, 2, 0,

                    4, 5, 6,
                    6, 7, 4,

                    8, 9, 10,
                    11, 13, 12,

                    14, 16, 15,
                    17, 16, 14,

                    18 + 0, 18 + 2, 18 + 1,
                    18 + 3, 18 + 2, 18 + 0,

                    18 + 4, 18 + 5, 18 + 6,
                    18 + 6, 18 + 7, 18 + 4,

                    18 + 8, 18 + 9, 18 + 10,
                    18 + 11, 18 + 13, 18 + 12,

                    18 + 14, 18 + 16, 18 + 15,
                    18 + 17, 18 + 16, 18 + 14,
            };
    static int offsetUniform;
    static int perspectiveMatrixUnif;

    void InitializeProgram()
    {
        int vertex_shader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, standard_vert5);
        int fragment_shader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, StandardColors_frag);
        theProgram = Shader.createAndLinkProgram(vertex_shader, fragment_shader);
        positionAttribute = GLES20.glGetAttribLocation(theProgram, "position");
        colorAttribute = GLES20.glGetAttribLocation(theProgram, "color");

        offsetUniform = GLES20.glGetUniformLocation(theProgram, "offset");

        perspectiveMatrixUnif = GLES20.glGetUniformLocation(theProgram, "perspectiveMatrix");
        fFrustumScale = 1.0f;
        float fzNear = 0.5f;
        float fzFar = 3.0f;

        Matrix4f theMatrix = new Matrix4f();

        theMatrix.M11 = fFrustumScale;
        theMatrix.M22 = fFrustumScale;
        theMatrix.M33 = (fzFar + fzNear) / (fzNear - fzFar);
        theMatrix.M43 = (2 * fzFar * fzNear) / (fzNear - fzFar);
        theMatrix.M34 = -1.0f;

        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(perspectiveMatrixUnif, 1, false, theMatrix.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        InitializeProgram();
        InitializeVertexBuffer(vertexData, indexData);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CW);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthMask(true);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthRangef(0.0f, 1.0f);
    }

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(theProgram);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glEnableVertexAttribArray(positionAttribute);
        GLES20.glEnableVertexAttribArray(colorAttribute);
        GLES20.glVertexAttribPointer(positionAttribute, POSITION_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false, POSITION_STRIDE, 0);
        GLES20.glVertexAttribPointer(colorAttribute, COLOR_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false, COLOR_STRIDE, COLOR_START);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);

        GLES20.glUniform3f(offsetUniform, 0.0f, 0.0f, -0.75f);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length/2,  GLES20.GL_UNSIGNED_SHORT, 0);

        GLES20.glUniform3f(offsetUniform, 0.0f, 0.0f, -1f);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length/2,  GLES20.GL_UNSIGNED_SHORT, indexData.length/2);

        GLES20.glDisableVertexAttribArray(positionAttribute);
        GLES20.glDisableVertexAttribArray(colorAttribute);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glUseProgram(0);
    }
}
