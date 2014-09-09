package com.tutorial.glsltutorials.tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by Jamie on 6/6/14.
 */
public class Tut_06_Rotations extends TutorialBase {

    public String PosColorLocalTransform_vert =
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

    public String ColorPassthrough_frag =
    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = theColor;" +
    "}";

    enum RotationFunc
    {
        NullRotation,
        RotateX,
        RotateY,
        RotateZ,
        RotateAxis,
    }

    void InitializeProgram()
    {
        int vertex_shader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, PosColorLocalTransform_vert);
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

    Matrix3f NullRotation(float fElapsedTime)
    {
        return Matrix3f.Identity();
    }

    float ComputeAngleRad(float fElapsedTime, float fLoopDuration)
    {
        float fScale = 3.14159f * 2.0f / fLoopDuration;
        float fCurrTimeThroughLoop = fElapsedTime % fLoopDuration;
        return fCurrTimeThroughLoop * fScale;
    }

    Matrix3f RotateX(float fElapsedTime)
    {
        float fAngRad = ComputeAngleRad(fElapsedTime, 3.0f);
        float fCos = (float)Math.cos(fAngRad);
        float fSin = (float)Math.sin(fAngRad);

        Matrix3f theMat = Matrix3f.Identity();
        theMat.M11 = fCos; theMat.M21 = -fSin;
        theMat.M12 = fSin; theMat.M22 = fCos;
        return theMat;
    }

    Matrix3f RotateY(float fElapsedTime)
    {
        float fAngRad = ComputeAngleRad(fElapsedTime, 2.0f);
        float fCos = (float)Math.cos(fAngRad);
        float fSin = (float)Math.sin(fAngRad);

        Matrix3f theMat = Matrix3f.Identity();
        theMat.M11 = fCos; theMat.M31 = fSin;
        theMat.M13 = -fSin; theMat.M33 = fCos;
        return theMat;
    }

    Matrix3f RotateZ(float fElapsedTime)
    {
        float fAngRad = ComputeAngleRad(fElapsedTime, 2.0f);
        float fCos = (float)Math.cos(fAngRad);
        float fSin = (float)Math.sin(fAngRad);

        Matrix3f theMat = Matrix3f.Identity();
        theMat.M11 = fCos; theMat.M21 = -fSin;
        theMat.M12 = fSin; theMat.M22 = fCos;
        return theMat;
    }

    Matrix3f RotateAxis(float fElapsedTime)
    {
        float fAngRad = ComputeAngleRad(fElapsedTime, 2.0f);
        float fCos = (float)Math.cos(fAngRad);
        float fInvCos = 1.0f - fCos;
        float fSin = (float)Math.sin(fAngRad);

        Vector3f axis = new Vector3f(1.0f, 1.0f, 1.0f);
        axis.normalize();

        Matrix3f theMat = Matrix3f.Identity();
        theMat.M11 = (axis.x * axis.x) + ((1 - axis.x * axis.x) * fCos);
        theMat.M21 = axis.x * axis.y * (fInvCos) - (axis.z * fSin);
        theMat.M31 = axis.x * axis.z * (fInvCos) + (axis.y * fSin);

        theMat.M21 = axis.x * axis.y * (fInvCos) + (axis.z * fSin);
        theMat.M22 = (axis.y * axis.y) + ((1 - axis.y * axis.y) * fCos);
        theMat.M32 = axis.y * axis.z * (fInvCos) - (axis.x * fSin);

        theMat.M13 = axis.x * axis.z * (fInvCos) - (axis.y * fSin);
        theMat.M23 = axis.y * axis.z * (fInvCos) + (axis.x * fSin);
        theMat.M33 = (axis.z * axis.z) + ((1 - axis.z * axis.z) * fCos);
        return theMat;
    }


    class Instance
    {
        RotationFunc CalcRotation;
        Vector3f offset;

        public Matrix4f ConstructMatrix(float fElapsedTime)
        {
            Matrix3f rotMatrix = new Matrix3f();
            switch (CalcRotation)
            {
                case NullRotation: rotMatrix = NullRotation(fElapsedTime); break;
                case RotateX: rotMatrix = RotateX(fElapsedTime);  break;
                case RotateY: rotMatrix = RotateY(fElapsedTime);  break;
                case RotateZ: rotMatrix = RotateZ(fElapsedTime);  break;
                case RotateAxis:  rotMatrix = RotateAxis(fElapsedTime); break;
            }
            Matrix4f theMat =  Matrix4f.Identity();
            theMat.SetRow0(new Vector4f(rotMatrix.GetRow0(), 0.0f));
            theMat.SetRow1(new Vector4f(rotMatrix.GetRow1(), 0.0f));
            theMat.SetRow2(new Vector4f(rotMatrix.GetRow2(), 0.0f));
            theMat.SetRow3(new Vector4f(offset, 1.0f));
            return theMat;
        }

        public Instance(RotationFunc CalcRotation_in, Vector3f offset_in)
        {
            CalcRotation = CalcRotation_in;
            offset = offset_in;
        }
    };

    Instance[] g_instanceList;

    void SetupGInstanceList()
    {
        g_instanceList = new Instance[5];
        g_instanceList[0] = new Instance(RotationFunc.NullRotation, new Vector3f(0.0f, 0.0f, -25.0f));
        g_instanceList[1] = new Instance(RotationFunc.RotateX, new Vector3f(-5.0f, -5.0f, -25.0f));
        g_instanceList[2] = new Instance(RotationFunc.RotateY, new Vector3f(-5.0f, 5.0f, -25.0f));
        g_instanceList[3] = new Instance(RotationFunc.RotateZ, new Vector3f(5.0f, 5.0f, -25.0f));
        g_instanceList[4] = new Instance(RotationFunc.RotateAxis, new Vector3f(5.0f, -5.0f, -25.0f));
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
