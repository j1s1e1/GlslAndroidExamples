package com.tutorial.glsltutorials.tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VBO_Tools;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

/**
 * Created by Jamie on 6/1/14.
 */
public class Tut_06_Scale extends TutorialBase  {

    static String PosColorLocalTransform_vert =
    "attribute vec4 color;" +
    "attribute vec4 position;" +

    "uniform mat4 cameraToClipMatrix;" +
    "uniform mat4 modelToCameraMatrix;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "vec4 cameraPos = modelToCameraMatrix * position;" +
        "gl_Position = cameraToClipMatrix * cameraPos;" +
        "theColor = color;" +
    "}";

    static String ColorPassthrough_frag =
    "varying vec4 theColor;" +
    "void main()" +
    "{" +
        "gl_FragColor = theColor;" +
    "}";

    private int numberOfVertices = 8;
    private int COLOR_START = numberOfVertices * POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;

    void InitializeProgram()
    {
        int vertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, PosColorLocalTransform_vert);
        int fragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, ColorPassthrough_frag);
        theProgram = Shader.createAndLinkProgram(vertexShader, fragmentShader);

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
            Colors.BROWN_COLOR[0], Colors.BROWN_COLOR[1], Colors.BROWN_COLOR[2], Colors.BROWN_COLOR[3]
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

    float CalcLerpFactor(float fElapsedTime, float fLoopDuration)
    {
        float fValue = fElapsedTime % fLoopDuration / fLoopDuration;
        if(fValue > 0.5f)
            fValue = 1.0f - fValue;

        return fValue * 2.0f;
    }

    enum scaleFunction
    {
        NullScale,
        StaticUniformScale,
        StaticNonUniformScale,
        DynamicUniformScale,
        DynamicNonUniformScale
    }

    Vector3f NullScale(float fElapsedTime)
    {
        return new Vector3f(1.0f, 1.0f, 1.0f);
    }

    Vector3f StaticUniformScale(float fElapsedTime)
    {
        return new Vector3f(4.0f, 4.0f, 4.0f);
    }

    Vector3f StaticNonUniformScale(float fElapsedTime)
    {
        return new Vector3f(0.5f, 1.0f, 10.0f);
    }

    Vector3f DynamicUniformScale(float fElapsedTime)
    {
        float fLoopDuration = 3.0f;

        return new Vector3f(Mix(1.0f, 4.0f, CalcLerpFactor(fElapsedTime, fLoopDuration)));
    }

    Vector3f DynamicNonUniformScale(float fElapsedTime)
    {
        float fXLoopDuration = 3.0f;
        float fZLoopDuration = 5.0f;

        return new Vector3f(Mix(1.0f, 0.5f, CalcLerpFactor(fElapsedTime, fXLoopDuration)),
                1.0f,
                Mix(1.0f, 10.0f, CalcLerpFactor(fElapsedTime, fZLoopDuration)));
    }

    class Instance
    {
        scaleFunction CalcScale;
        Vector3f offset;

        public Instance(scaleFunction sf, Vector3f o)
        {
            CalcScale = sf;
            offset = o;
        }

        public Matrix4f ConstructMatrix(float fElapsedTime)
        {
            Vector3f theScale = new Vector3f(0f);
            switch (CalcScale)
            {
                case NullScale: theScale = NullScale(fElapsedTime);  break;
                case StaticUniformScale: theScale = StaticUniformScale(fElapsedTime);  break;
                case StaticNonUniformScale: theScale = StaticNonUniformScale(fElapsedTime);  break;
                case DynamicUniformScale: theScale = DynamicUniformScale(fElapsedTime);  break;
                case DynamicNonUniformScale: theScale = DynamicNonUniformScale(fElapsedTime);  break;
            }
            Matrix4f theMat = new Matrix4f();
            theMat.M11 = theScale.x;
            theMat.M22 = theScale.y;
            theMat.M33 = theScale.z;
            theMat.SetRow3(new Vector4f(offset, 1.0f));

            return theMat;
        }
    };

    Instance[] g_instanceList = new Instance[5];


    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        g_instanceList[0]  = new Instance(scaleFunction.NullScale, new Vector3f(0.0f, 0.0f, -45.0f));
        g_instanceList[1]  = new Instance(scaleFunction.StaticUniformScale, new Vector3f(-10.0f, -10.0f, -45.0f));
        g_instanceList[2]  = new Instance(scaleFunction.StaticNonUniformScale, new Vector3f(-10.0f, 10.0f, -45.0f));
        g_instanceList[3]  = new Instance(scaleFunction.DynamicUniformScale, new Vector3f(10.0f, 10.0f, -45.0f));
        g_instanceList[4]  = new Instance(scaleFunction.DynamicNonUniformScale, new Vector3f(10.0f, -10.0f, -45.0f));

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
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, 0);
        }
        GLES20.glDisableVertexAttribArray(positionAttribute);
        GLES20.glDisableVertexAttribArray(colorAttribute);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glUseProgram(0);
    }

    //Called whenever the window is resized. The new window size is given, in pixels.
    //This is an opportunity to call glViewport or glScissor to keep up with the change in size.
    public void reshape ()
    {
        cameraToClipMatrix.M11 = fFrustumScale * (height / (float)width);
        cameraToClipMatrix.M22 = fFrustumScale;

        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(cameraToClipMatrixUnif, 1, false, cameraToClipMatrix.toArray(), 0);
        GLES20.glUseProgram(0);

        GLES20.glViewport(0, 0, width, height);
    }
}
