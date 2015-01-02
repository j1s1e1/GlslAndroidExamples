package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.MatrixStack;

/**
 * Created by jamie on 1/1/15.
 */
public class Tut_06_Hierarchy extends TutorialBase {
    int positionAttrib;
    int colorAttrib;

    void InitializeProgram()
    {
        fFrustumScale = calcFrustumScale(45.0f);
        int vertex_shader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, VertexShaders.PosColorLocalTransform_vert);
        int fragment_shader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, FragmentShaders.ColorPassthrough_frag);
        theProgram = Shader.createAndLinkProgram(vertex_shader, fragment_shader);
        positionAttribute = GLES20.glGetAttribLocation(theProgram, "position");
        colorAttribute = GLES20.glGetAttribLocation(theProgram, "color");

        modelToCameraMatrixUnif = GLES20.glGetUniformLocation(theProgram, "modelToCameraMatrix");
        cameraToClipMatrixUnif = GLES20.glGetUniformLocation(theProgram, "cameraToClipMatrix");

        fFrustumScale = calcFrustumScale(45.0f);
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

    static int numberOfVertices = 24;
    private int COLOR_START = numberOfVertices * POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;


    static float[] vertexData = new float[]
            {
                    //Front
                    +1.0f, +1.0f, +1.0f, 1.0f,
                    +1.0f, -1.0f, +1.0f, 1.0f,
                    -1.0f, -1.0f, +1.0f, 1.0f,
                    -1.0f, +1.0f, +1.0f, 1.0f,

                    //Top
                    +1.0f, +1.0f, +1.0f, 1.0f,
                    -1.0f, +1.0f, +1.0f, 1.0f,
                    -1.0f, +1.0f, -1.0f, 1.0f,
                    +1.0f, +1.0f, -1.0f, 1.0f,

                    //Left
                    +1.0f, +1.0f, +1.0f, 1.0f,
                    +1.0f, +1.0f, -1.0f, 1.0f,
                    +1.0f, -1.0f, -1.0f, 1.0f,
                    +1.0f, -1.0f, +1.0f, 1.0f,

                    //Back
                    +1.0f, +1.0f, -1.0f, 1.0f,
                    -1.0f, +1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f, 1.0f,
                    +1.0f, -1.0f, -1.0f, 1.0f,

                    //Bottom
                    +1.0f, -1.0f, +1.0f, 1.0f,
                    +1.0f, -1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, +1.0f, 1.0f,

                    //Right
                    -1.0f, +1.0f, +1.0f, 1.0f,
                    -1.0f, -1.0f, +1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f, 1.0f,
                    -1.0f, +1.0f, -1.0f, 1.0f,


                    Colors.GREEN_COLOR[0], Colors.GREEN_COLOR[1], Colors.GREEN_COLOR[2], Colors.GREEN_COLOR[3],
                    Colors.GREEN_COLOR[0], Colors.GREEN_COLOR[1], Colors.GREEN_COLOR[2], Colors.GREEN_COLOR[3],
                    Colors.GREEN_COLOR[0], Colors.GREEN_COLOR[1], Colors.GREEN_COLOR[2], Colors.GREEN_COLOR[3],
                    Colors.GREEN_COLOR[0], Colors.GREEN_COLOR[1], Colors.GREEN_COLOR[2], Colors.GREEN_COLOR[3],

                    Colors.BLUE_COLOR[0], Colors.BLUE_COLOR[1], Colors.BLUE_COLOR[2], Colors.BLUE_COLOR[3],
                    Colors.BLUE_COLOR[0], Colors.BLUE_COLOR[1], Colors.BLUE_COLOR[2], Colors.BLUE_COLOR[3],
                    Colors.BLUE_COLOR[0], Colors.BLUE_COLOR[1], Colors.BLUE_COLOR[2], Colors.BLUE_COLOR[3],
                    Colors.BLUE_COLOR[0], Colors.BLUE_COLOR[1], Colors.BLUE_COLOR[2], Colors.BLUE_COLOR[3],

                    Colors.RED_COLOR[0], Colors.RED_COLOR[1], Colors.RED_COLOR[2], Colors.RED_COLOR[3],
                    Colors.RED_COLOR[0], Colors.RED_COLOR[1], Colors.RED_COLOR[2], Colors.RED_COLOR[3],
                    Colors.RED_COLOR[0], Colors.RED_COLOR[1], Colors.RED_COLOR[2], Colors.RED_COLOR[3],
                    Colors.RED_COLOR[0], Colors.RED_COLOR[1], Colors.RED_COLOR[2], Colors.RED_COLOR[3],

                    Colors.YELLOW_COLOR[0], Colors.YELLOW_COLOR[1], Colors.YELLOW_COLOR[2], Colors.YELLOW_COLOR[3],
                    Colors.YELLOW_COLOR[0], Colors.YELLOW_COLOR[1], Colors.YELLOW_COLOR[2], Colors.YELLOW_COLOR[3],
                    Colors.YELLOW_COLOR[0], Colors.YELLOW_COLOR[1], Colors.YELLOW_COLOR[2], Colors.YELLOW_COLOR[3],
                    Colors.YELLOW_COLOR[0], Colors.YELLOW_COLOR[1], Colors.YELLOW_COLOR[2], Colors.YELLOW_COLOR[3],

                    Colors.CYAN_COLOR[0], Colors.CYAN_COLOR[1], Colors.CYAN_COLOR[2], Colors.CYAN_COLOR[3],
                    Colors.CYAN_COLOR[0], Colors.CYAN_COLOR[1], Colors.CYAN_COLOR[2], Colors.CYAN_COLOR[3],
                    Colors.CYAN_COLOR[0], Colors.CYAN_COLOR[1], Colors.CYAN_COLOR[2], Colors.CYAN_COLOR[3],
                    Colors.CYAN_COLOR[0], Colors.CYAN_COLOR[1], Colors.CYAN_COLOR[2], Colors.CYAN_COLOR[3],

                    Colors.MAGENTA_COLOR[0], Colors.MAGENTA_COLOR[1], Colors.MAGENTA_COLOR[2], Colors.MAGENTA_COLOR[3],
                    Colors.MAGENTA_COLOR[0], Colors.MAGENTA_COLOR[1], Colors.MAGENTA_COLOR[2], Colors.MAGENTA_COLOR[3],
                    Colors.MAGENTA_COLOR[0], Colors.MAGENTA_COLOR[1], Colors.MAGENTA_COLOR[2], Colors.MAGENTA_COLOR[3],
                    Colors.MAGENTA_COLOR[0], Colors.MAGENTA_COLOR[1], Colors.MAGENTA_COLOR[2], Colors.MAGENTA_COLOR[3],
            };

    static short[] indexData = new short[]
            {
                    0, 1, 2,
                    2, 3, 0,

                    4, 5, 6,
                    6, 7, 4,

                    8, 9, 10,
                    10, 11, 8,

                    12, 13, 14,
                    14, 15, 12,

                    16, 17, 18,
                    18, 19, 16,

                    20, 21, 22,
                    22, 23, 20,
            };

    int vao;

    void InitializeVAO()
    {
        initializeVertexBuffer(vertexData, indexData);

        GLES20.glUseProgram(theProgram);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vertexBufferObject[0]);
        // Bind Attributes
        GLES20.glEnableVertexAttribArray(positionAttribute);
        GLES20.glEnableVertexAttribArray(colorAttribute);
        GLES20.glVertexAttribPointer(positionAttribute, POSITION_DATA_SIZE_IN_ELEMENTS,
                GLES20.GL_FLOAT, false, POSITION_STRIDE, 0);
        GLES20.glVertexAttribPointer(colorAttribute, COLOR_DATA_SIZE_IN_ELEMENTS,
                GLES20.GL_FLOAT, false, COLOR_STRIDE, COLOR_START);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glUseProgram(0);
    }

    public static float Clamp(float fValue, float fMinValue, float fMaxValue)
    {
        if(fValue < fMinValue)
            return fMinValue;

        if(fValue > fMaxValue)
            return fMaxValue;

        return fValue;
    }

    Matrix3f RotateX(float fAngDeg)
    {
        float fAngRad = degToRad(fAngDeg);
        float fCos = (float) Math.cos(fAngRad);
        float fSin = (float) Math.sin(fAngRad);

        Matrix3f theMat = Matrix3f.Identity();
        theMat.M22 = fCos; theMat.M32 = -fSin;
        theMat.M23 = fSin; theMat.M33 = fCos;
        return theMat;
    }

    Matrix3f RotateY(float fAngDeg)
    {
        float fAngRad = degToRad(fAngDeg);
        float fCos = (float) Math.cos(fAngRad);
        float fSin = (float) Math.sin(fAngRad);

        Matrix3f theMat = Matrix3f.Identity();
        theMat.M11 = fCos; theMat.M31 = fSin;
        theMat.M13 = -fSin; theMat.M33 = fCos;
        return theMat;
    }

    Matrix3f RotateZ(float fAngDeg)
    {
        float fAngRad = degToRad(fAngDeg);
        float fCos = (float) Math.cos(fAngRad);
        float fSin = (float) Math.sin(fAngRad);

        Matrix3f theMat = Matrix3f.Identity();
        theMat.M11 = fCos; theMat.M21 = -fSin;
        theMat.M12 = fSin; theMat.M22 = fCos;
        return theMat;
    }

    class Hierarchy
    {
        public Hierarchy(int theProgramIn, int vaoIn, int modelToCameraMatrixUnifIn)
        {
            theProgram = theProgramIn;
            vao = vaoIn;
            modelToCameraMatrixUnif = modelToCameraMatrixUnifIn;
            posBase = new Vector3f(3.0f, -5.0f, -40.0f);
            angBase = -45.0f;
            posBaseLeft = new Vector3f(2.0f, 0.0f, 0.0f);
            posBaseRight = new Vector3f(-2.0f, 0.0f, 0.0f);
            scaleBaseZ = 3.0f;
            angUpperArm = -33.75f;
            sizeUpperArm = 9.0f;
            posLowerArm = new Vector3f(0.0f, 0.0f, 8.0f);
            angLowerArm = 146.25f;
            lenLowerArm = 5.0f;
            widthLowerArm = 1.5f;
            posWrist = new Vector3f(0.0f, 0.0f, 5.0f);
            angWristRoll = 0.0f;
            angWristPitch = 67.5f;
            lenWrist = 2.0f;
            widthWrist = 2.0f;
            posLeftFinger = new Vector3f(1.0f, 0.0f, 1.0f);
            posRightFinger = new Vector3f(-1.0f, 0.0f, 1.0f);
            angFingerOpen = 180.0f;
            lenFinger = 2.0f;
            widthFinger = 0.5f;
            angLowerFinger = 45.0f;
        }

        public void Draw()
        {
            MatrixStack modelToCameraStack = new MatrixStack();

            GLES20.glUseProgram(theProgram);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
            // Bind Attributes
            GLES20.glEnableVertexAttribArray(positionAttribute);
            GLES20.glEnableVertexAttribArray(colorAttribute);
            GLES20.glVertexAttribPointer(positionAttribute, POSITION_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false, POSITION_STRIDE, 0);
            GLES20.glVertexAttribPointer(colorAttribute, COLOR_DATA_SIZE_IN_ELEMENTS, GLES20.GL_FLOAT, false, COLOR_STRIDE, COLOR_START);

            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);

            modelToCameraStack.Translate(posBase);
            modelToCameraStack.RotateY(angBase);

            //Draw left base.
            {
                modelToCameraStack.push();
                modelToCameraStack.Translate(posBaseLeft);
                modelToCameraStack.Scale(new Vector3f(1.0f, 1.0f, scaleBaseZ));
                Matrix4f mm = modelToCameraStack.Top();
                GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, 0);
                modelToCameraStack.pop();
            }

            //Draw right base.
            {
                modelToCameraStack.push();
                modelToCameraStack.Translate(posBaseRight);
                modelToCameraStack.Scale(new Vector3f(1.0f, 1.0f, scaleBaseZ));
                Matrix4f mm = modelToCameraStack.Top();
                GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, 0);
                modelToCameraStack.pop();
            }

            //Draw main arm.
            DrawUpperArm(modelToCameraStack);

            GLES20.glDisableVertexAttribArray(positionAttribute);
            GLES20.glDisableVertexAttribArray(colorAttribute);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
            GLES20.glUseProgram(0);
        }

        float STANDARD_ANGLE_INCREMENT = 11.25f;
        float SMALL_ANGLE_INCREMENT = 9.0f;

        public void AdjBase(boolean bIncrement)
        {
            angBase += bIncrement ? STANDARD_ANGLE_INCREMENT : -STANDARD_ANGLE_INCREMENT;
            angBase = angBase % 360.0f;
        }

        public void AdjUpperArm(boolean bIncrement)
        {
            angUpperArm += bIncrement ? STANDARD_ANGLE_INCREMENT : -STANDARD_ANGLE_INCREMENT;
            angUpperArm = Clamp(angUpperArm, -90.0f, 0.0f);
        }

        public void AdjLowerArm(boolean bIncrement)
        {
            angLowerArm += bIncrement ? STANDARD_ANGLE_INCREMENT : -STANDARD_ANGLE_INCREMENT;
            angLowerArm = Clamp(angLowerArm, 0.0f, 146.25f);
        }

        public void AdjWristPitch(boolean bIncrement)
        {
            angWristPitch += bIncrement ? STANDARD_ANGLE_INCREMENT : -STANDARD_ANGLE_INCREMENT;
            angWristPitch = Clamp(angWristPitch, 0.0f, 90.0f);
        }

        public void AdjWristRoll(boolean bIncrement)
        {
            angWristRoll += bIncrement ? STANDARD_ANGLE_INCREMENT : -STANDARD_ANGLE_INCREMENT;
            angWristRoll = angWristRoll % 360.0f;
        }

        public void AdjFingerOpen(boolean bIncrement)
        {
            angFingerOpen += bIncrement ? SMALL_ANGLE_INCREMENT : -SMALL_ANGLE_INCREMENT;
            angFingerOpen = Clamp(angFingerOpen, 9.0f, 180.0f);
        }

        public String WritePose()
        {
            StringBuilder result = new StringBuilder();
            result.append("angBase: " + String.valueOf(angBase));

            result.append("angUpperArm: " + String.valueOf(angUpperArm));
            result.append("angLowerArm: " + String.valueOf(angLowerArm));
            result.append("angWristPitch: " + String.valueOf(angWristPitch));
            result.append("angWristRoll: " + String.valueOf(angWristRoll));
            result.append("angFingerOpen: " + String.valueOf(angFingerOpen));
            result.append("");
            return result.toString();
        }

        private void DrawFingers(MatrixStack modelToCameraStack)
        {
            //Draw left finger
            modelToCameraStack.push();
            modelToCameraStack.Translate(posLeftFinger);
            modelToCameraStack.RotateY(angFingerOpen);

            modelToCameraStack.push();
            modelToCameraStack.Translate(new Vector3f(0.0f, 0.0f, lenFinger / 2.0f));
            modelToCameraStack.Scale(new Vector3f(widthFinger / 2.0f, widthFinger/ 2.0f, lenFinger / 2.0f));
            Matrix4f mm = modelToCameraStack.Top();
            GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, 0);
            modelToCameraStack.pop();

            {
                //Draw left lower finger
                modelToCameraStack.push();
                modelToCameraStack.Translate(new Vector3f(0.0f, 0.0f, lenFinger));
                modelToCameraStack.RotateY(-angLowerFinger);

                modelToCameraStack.push();
                modelToCameraStack.Translate(new Vector3f(0.0f, 0.0f, lenFinger / 2.0f));
                modelToCameraStack.Scale(new Vector3f(widthFinger / 2.0f, widthFinger/ 2.0f, lenFinger / 2.0f));

                mm = modelToCameraStack.Top();
                GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, 0);
                modelToCameraStack.pop();

                modelToCameraStack.pop();
            }

            modelToCameraStack.pop();

            //Draw right finger
            modelToCameraStack.push();
            modelToCameraStack.Translate(posRightFinger);
            modelToCameraStack.RotateY(-angFingerOpen);

            modelToCameraStack.push();
            modelToCameraStack.Translate(new Vector3f(0.0f, 0.0f, lenFinger / 2.0f));
            modelToCameraStack.Scale(new Vector3f(widthFinger / 2.0f, widthFinger/ 2.0f, lenFinger / 2.0f));
            mm = modelToCameraStack.Top();
            GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, 0);
            modelToCameraStack.pop();

            {
                //Draw right lower finger
                modelToCameraStack.push();
                modelToCameraStack.Translate(new Vector3f(0.0f, 0.0f, lenFinger));
                modelToCameraStack.RotateY(angLowerFinger);

                modelToCameraStack.push();
                modelToCameraStack.Translate(new Vector3f(0.0f, 0.0f, lenFinger / 2.0f));
                modelToCameraStack.Scale(new Vector3f(widthFinger / 2.0f, widthFinger/ 2.0f, lenFinger / 2.0f));
                mm = modelToCameraStack.Top();
                GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 0, false, mm.toArray(), 0);
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, 0);
                modelToCameraStack.pop();

                modelToCameraStack.pop();
            }

            modelToCameraStack.pop();
        }

        private void DrawWrist(MatrixStack modelToCameraStack)
        {
            modelToCameraStack.push();
            modelToCameraStack.Translate(posWrist);
            modelToCameraStack.RotateZ(angWristRoll);
            modelToCameraStack.RotateX(angWristPitch);

            modelToCameraStack.push();
            modelToCameraStack.Scale(new Vector3f(widthWrist / 2.0f, widthWrist/ 2.0f, lenWrist / 2.0f));
            Matrix4f mm = modelToCameraStack.Top();
            GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, 0);
            modelToCameraStack.pop();

            DrawFingers(modelToCameraStack);

            modelToCameraStack.pop();
        }

        private void DrawLowerArm(MatrixStack modelToCameraStack)
        {
            modelToCameraStack.push();
            modelToCameraStack.Translate(posLowerArm);
            modelToCameraStack.RotateX(angLowerArm);

            modelToCameraStack.push();
            modelToCameraStack.Translate(new Vector3f(0.0f, 0.0f, lenLowerArm / 2.0f));
            modelToCameraStack.Scale(new Vector3f(widthLowerArm / 2.0f, widthLowerArm / 2.0f, lenLowerArm / 2.0f));
            Matrix4f mm = modelToCameraStack.Top();
            GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, 0);
            modelToCameraStack.pop();

            DrawWrist(modelToCameraStack);

            modelToCameraStack.pop();
        }

        private void DrawUpperArm(MatrixStack modelToCameraStack)
        {
            modelToCameraStack.push();
            modelToCameraStack.RotateX(angUpperArm);

            {
                modelToCameraStack.push();
                modelToCameraStack.Translate(new Vector3f(0.0f, 0.0f, (sizeUpperArm / 2.0f) - 1.0f));
                modelToCameraStack.Scale(new Vector3f(1.0f, 1.0f, sizeUpperArm / 2.0f));
                Matrix4f mm = modelToCameraStack.Top();
                GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, 0);
                modelToCameraStack.pop();
            }

            DrawLowerArm(modelToCameraStack);

            modelToCameraStack.pop();
        }

        private Vector3f	posBase;
        private float	angBase;

        private Vector3f	posBaseLeft, posBaseRight;
        private float	scaleBaseZ;

        private float	angUpperArm;
        private float	sizeUpperArm;

        private Vector3f	posLowerArm;
        private float	angLowerArm;
        private float	lenLowerArm;
        private float	widthLowerArm;

        private Vector3f	posWrist;
        private float	angWristRoll;
        private float	angWristPitch;
        private float	lenWrist;
        private float	widthWrist;

        private Vector3f	posLeftFinger;
        private Vector3f	posRightFinger;
        private float	angFingerOpen;
        private float	lenFinger;
        private float	widthFinger;
        private float	angLowerFinger;

        private int theProgram;
        private int vao;
        private int modelToCameraMatrixUnif;
    };


    Hierarchy g_armature;

    protected void init()
    {
        InitializeProgram();
        InitializeVAO();

        setupDepthAndCull();
        g_armature = new Hierarchy(theProgram, vao, modelToCameraMatrixUnif);
        // FIXME MatrixStack.rightMultiply = false; // TEST
    }

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display()
    {
        clearDisplay();
        g_armature.Draw();
    }

    public void reshape ()
    {
        cameraToClipMatrix.M11 = fFrustumScale * (height / (float)width);
        cameraToClipMatrix.M22 = fFrustumScale;

        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(cameraToClipMatrixUnif, 1, false, cameraToClipMatrix.toArray(), 0);
        GLES20.glUseProgram(0);

        GLES20.glViewport(0, 0, width, height);
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_ESCAPE:
                //glutLeaveMainLoop();
                break;
            case KeyEvent.KEYCODE_A: g_armature.AdjBase(true); break;
            case KeyEvent.KEYCODE_D: g_armature.AdjBase(false); break;
            case KeyEvent.KEYCODE_W: g_armature.AdjUpperArm(false); break;
            case KeyEvent.KEYCODE_S: g_armature.AdjUpperArm(true); break;
            case KeyEvent.KEYCODE_R: g_armature.AdjLowerArm(false); break;
            case KeyEvent.KEYCODE_F: g_armature.AdjLowerArm(true); break;
            case KeyEvent.KEYCODE_T: g_armature.AdjWristPitch(false); break;
            case KeyEvent.KEYCODE_G: g_armature.AdjWristPitch(true); break;
            case KeyEvent.KEYCODE_Z: g_armature.AdjWristRoll(true); break;
            case KeyEvent.KEYCODE_C: g_armature.AdjWristRoll(false); break;
            case KeyEvent.KEYCODE_Q: g_armature.AdjFingerOpen(true); break;
            case KeyEvent.KEYCODE_E: g_armature.AdjFingerOpen(false); break;
            case KeyEvent.KEYCODE_SPACE: result.append(g_armature.WritePose()); break;
        }
        return result.toString();
    }
}
