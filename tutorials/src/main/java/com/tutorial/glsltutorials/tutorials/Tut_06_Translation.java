package com.tutorial.glsltutorials.tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by Jamie on 6/6/14.
 */
public class Tut_06_Translation extends TutorialBase {

    public String ColorPassthrough_frag =
    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = theColor;" +
    "}";

    enum OffsetFunc
    {
        StationaryOffset,
        OvalOffset,
        BottomCircleOffset
    }

    void InitializeProgram()
    {
        int vertex_shader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, VertexShaders.PosColorLocalTransform_vert);
        int fragment_shader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, ColorPassthrough_frag);
        theProgram = Shader.createAndLinkProgram(vertex_shader, fragment_shader);
        positionAttribute = GLES20.glGetAttribLocation(theProgram, "position");
        colorAttribute = GLES20.glGetAttribLocation(theProgram, "color");

        modelToCameraMatrixUnif = GLES20.glGetUniformLocation(theProgram, "modelToCameraMatrix");
        cameraToClipMatrixUnif = GLES20.glGetUniformLocation(theProgram, "cameraToClipMatrix");

        fFrustumScale = CalcFrustumScale(45.0f);
        float fzNear = 1.0f;
        float fzFar = 61.0f;

        cameraToClipMatrix.M11 = fFrustumScale;
        cameraToClipMatrix.M22 = fFrustumScale;
        cameraToClipMatrix.M33 = (fzFar + fzNear) / (fzNear - fzFar);
        cameraToClipMatrix.M34 = -1.0f;
        cameraToClipMatrix.M43 = (2 * fzFar * fzNear) / (fzNear - fzFar);

        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(cameraToClipMatrixUnif, 1, false, cameraToClipMatrix.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    static int numberOfVertices = 8;
    private int COLOR_START = numberOfVertices * POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;

    static float[] vertexData =
    {
            +1.0f, +1.0f, +1.0f, 1.0f,
            -1.0f, -1.0f, +1.0f, 1.0f,
            -1.0f, +1.0f, -1.0f, 1.0f,
            +1.0f, -1.0f, -1.0f, 1.0f,

            -1.0f, -1.0f, -1.0f, 1.0f,
            +1.0f, +1.0f, -1.0f, 1.0f,
            +1.0f, -1.0f, +1.0f, 1.0f,
            -1.0f, +1.0f, +1.0f, 1.0f,

            Colors.GREEN_COLOR[0], Colors.GREEN_COLOR[1], Colors.GREEN_COLOR[2], Colors.GREEN_COLOR[3],
            Colors.BLUE_COLOR[0], Colors.BLUE_COLOR[1], Colors.BLUE_COLOR[2], Colors.BLUE_COLOR[3],
            Colors.RED_COLOR[0], Colors.RED_COLOR[1], Colors.RED_COLOR[2], Colors.RED_COLOR[3],
            Colors.BROWN_COLOR[0], Colors.BROWN_COLOR[1], Colors.BROWN_COLOR[2], Colors.BROWN_COLOR[3],

            Colors.GREEN_COLOR[0], Colors.GREEN_COLOR[1], Colors.GREEN_COLOR[2], Colors.GREEN_COLOR[3],
            Colors.BLUE_COLOR[0], Colors.BLUE_COLOR[1], Colors.BLUE_COLOR[2], Colors.BLUE_COLOR[3],
            Colors.RED_COLOR[0], Colors.RED_COLOR[1], Colors.RED_COLOR[2], Colors.RED_COLOR[3],
            Colors.BROWN_COLOR[0], Colors.BROWN_COLOR[1], Colors.BROWN_COLOR[2], Colors.BROWN_COLOR[3],
    };

    static short[] indexData =
    {
            0, 1, 2,
            1, 0, 3,
            2, 3, 0,
            3, 2, 1,

            5, 4, 6,
            4, 5, 7,
            7, 6, 4,
            6, 7, 5,
    };

    static Vector3f StationaryOffset(float fElapsedTime)
    {
        return new Vector3f(0.0f, 0.0f, -20.0f);
    }

    Vector3f OvalOffset(float fElapsedTime)
    {
        float fLoopDuration = 3.0f;
        float fScale = 3.14159f * 2.0f / fLoopDuration;

        float fCurrTimeThroughLoop = fElapsedTime % fLoopDuration;

        return new Vector3f((float)Math.cos(fCurrTimeThroughLoop * fScale) * 4f,
                (float)Math.sin(fCurrTimeThroughLoop * fScale) * 6f,
                -20.0f);
    }

    Vector3f BottomCircleOffset(float fElapsedTime)
    {
        float fLoopDuration = 12.0f;
        float fScale = 3.14159f * 2.0f / fLoopDuration;

        float fCurrTimeThroughLoop = fElapsedTime % fLoopDuration;

        return new Vector3f((float)Math.cos(fCurrTimeThroughLoop * fScale) * 5f,
                -3.5f,
                (float)Math.sin(fCurrTimeThroughLoop * fScale) * 5f - 20.0f);
    }

    class Instance
    {
        OffsetFunc CalcOffset;

        public Matrix4f ConstructMatrix(float fElapsedTime)
        {
            Matrix4f theMat = Matrix4f.Identity();

            switch (CalcOffset)
            {
                case StationaryOffset: theMat.SetRow3(StationaryOffset(fElapsedTime), 1.0f); break;
                case OvalOffset:  theMat.SetRow3(OvalOffset(fElapsedTime), 1.0f); break;
                case BottomCircleOffset:  theMat.SetRow3(BottomCircleOffset(fElapsedTime), 1.0f); break;
            }

            return theMat;
        }
        public Instance(OffsetFunc CalcOffset_In)
        {
            CalcOffset = CalcOffset_In;
        }
    };

    static Instance[] g_instanceList;

    void SetupGInstanceList()
    {
        g_instanceList = new Instance[3];
        g_instanceList[0] = new Instance(OffsetFunc.StationaryOffset);
        g_instanceList[1] = new Instance(OffsetFunc.OvalOffset);
        g_instanceList[2] = new Instance(OffsetFunc.BottomCircleOffset);
    }

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        SetupGInstanceList();
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
            // Bind Attributes
            GLES20.glEnableVertexAttribArray(positionAttribute);
            GLES20.glEnableVertexAttribArray(colorAttribute);
            GLES20.glVertexAttribPointer(positionAttribute, POSITION_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false, POSITION_STRIDE, 0);
            GLES20.glVertexAttribPointer(colorAttribute, COLOR_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false, COLOR_STRIDE, COLOR_START);

            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);

            float fElapsedTime = GetElapsedTime() / 1000.0f;
            for(int iLoop = 0; iLoop < g_instanceList.length; iLoop++)
            {
                Instance currInst = g_instanceList[iLoop];
                Matrix4f transformMatrix = currInst.ConstructMatrix(fElapsedTime);

                GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, transformMatrix.toArray(), 0);
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length,  GLES20.GL_UNSIGNED_SHORT, 0);
            }

            GLES20.glDisableVertexAttribArray(positionAttribute);
            GLES20.glDisableVertexAttribArray(colorAttribute);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
            GLES20.glUseProgram(0);
        }
}
