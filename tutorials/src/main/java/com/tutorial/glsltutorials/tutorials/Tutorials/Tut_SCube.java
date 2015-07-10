package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixBlock2;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

/**
 * Created by jamie on 7/4/15.
 */
public class Tut_SCube extends TutorialBase {
    Matrix3f rotation;
    boolean rotateCube = true;
    Vector3f lmb3Scale = new Vector3f(1f, 1f, 1f);

    int tick = -1;

    Vector4f lightPos = new Vector4f(2.0f, 4.0f, 2.0f, 1.0f);
    Vector4f groundPlane = new Vector4f(0.0f, 1.0f, 0.0f, 1.499f);
    Vector4f backPlane =  new Vector4f(0.0f, 0.0f, 1.0f, 0.899f);

    int sCubeProgram;

    boolean matrixesAreDifferent = false;

    LitMatrixBlock2 lmb2;
    LitMatrixBlock2 backLMB;
    LitMatrixBlock2 groundLMB;

    float lmb3Offset = -2;
    int offsetUnif;

    protected void init()
    {
        sCubeProgram = Programs.addProgram(VertexShaders.sCube, FragmentShaders.sCube);
        lmb2 = new LitMatrixBlock2(new Vector3f(1.0f, 1.0f, 1.0f), Colors.BLUE_COLOR);
        lmb2.scale(lmb3Scale);
        lmb2.setProgram(sCubeProgram);

        backLMB = new LitMatrixBlock2(new Vector3f(0.666f, 0.666f, 0.2f), Colors.YELLOW_COLOR);
        backLMB.setProgram(sCubeProgram);

        groundLMB = new LitMatrixBlock2(new Vector3f(0.666f, 0.2f, 0.666f), Colors.YELLOW_COLOR);
        groundLMB.setProgram(sCubeProgram);

        int program = Programs.getProgram(sCubeProgram);
        GLES20.glUseProgram(program);
        offsetUnif = GLES20.glGetUniformLocation(program, "offset");
        GLES20.glUniform4f(offsetUnif, 0f, 0f, lmb3Offset, 0f);
        GLES20.glUseProgram(0);

        Matrix4f perspectiveFOV = Matrix4f.CreatePerspectiveFieldOfView((float)(Math.PI/2), width/height, 1f, 3f);

        //Matrix4f perspectiveOffCenter = Matrix4f.CreatePerspectiveOffCenter(-1.0f, 1.0f, -1.0f, 1.0f, 1f, 3f);

        Shape.setCameraToClipMatrix(perspectiveFOV);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);


        //FIXME GLES20.glEnable(EnableCap.Normalize);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);

        //FIXME GLES20.glShadeModel(ShadingModel.Smooth);

        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //FIXME GLES20.glClearIndex(0);
        //FIXME GLES20.glClearDepth(1);
    }

    private void DrawGround()
    {
        for (int x = 0; x < 6; x++)
        {
            for (int z = 0; z < 6; z++)
            {
                groundLMB.setOffset(new Vector3f(-2f + 0.666f / 2f + x * 0.666f, -1.6f,
                        -2f + 0.666f / 2f + z * 0.666f));
                if (((x + z) % 2) == 0)
                {
                    groundLMB.setColor(Colors.YELLOW_COLOR);
                }
                else
                {
                    groundLMB.setColor(Colors.BLUE_COLOR);
                }
                groundLMB.draw();
            }
        }
    }

    private void DrawBack()
    {
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                backLMB.setOffset(new Vector3f(-2f + 0.666f/2f + x * 0.666f,
                        -2f + 0.666f/2f + y * 0.666f, -1f));
                if (((x + y) % 2) == 1)
                {
                    backLMB.setColor(Colors.YELLOW_COLOR);
                }
                else {
                    backLMB.setColor(Colors.BLUE_COLOR);
                }
                backLMB.draw();
            }
        }
    }

    private Matrix4f CalculateCubeXform()
    {
        Matrix4f cubeXformMatrix = new Matrix4f();
        Matrix4f translate2 = Matrix4f.createTranslation(new Vector3f(0.0f, 0.2f, 0.0f));
        Matrix4f scaleMatrix = Matrix4f.createScale(new Vector3f(0.3f, 0.3f, 0.3f));
        Matrix4f rotation = Matrix4f.Identity();
        rotation = Matrix4f.mul(Matrix4f.createFromAxisAngle(Vector3f.UnitX, (360.0f / (30f * 1) * (float) Math.PI / 180f) * tick), rotation);
        rotation = Matrix4f.mul(Matrix4f.createFromAxisAngle(Vector3f.UnitY, (360.0f / (30f * 2) * (float) Math.PI / 180f) * tick), rotation);
        rotation = Matrix4f.mul(Matrix4f.createFromAxisAngle(Vector3f.UnitZ, (360.0f / (30f * 4) * (float) Math.PI / 180f) * tick), rotation);
        cubeXformMatrix = Matrix4f.mul(scaleMatrix, translate2);
        cubeXformMatrix = Matrix4f.mul(rotation, cubeXformMatrix);
        scaleMatrix = Matrix4f.createScale(new Vector3f(1f, 2f, 1f));

        cubeXformMatrix = Matrix4f.mul(scaleMatrix, cubeXformMatrix);
        return cubeXformMatrix;
    }

    private void DrawMainBlock(Matrix4f cubeXform)
    {
        lmb2.setColor(Colors.RED_COLOR);
        lmb2.modelToWorld = cubeXform;
        lmb2.draw();
    }

    private void DrawGroundShadow(Matrix4f cubeXform)
    {
        Matrix4f shadowMat = myShadowMatrix(groundPlane, lightPos);
        lmb2.setColor(Colors.SHADOW_COLOR);
        lmb2.modelToWorld = Matrix4f.mul(cubeXform, shadowMat);
        lmb2.draw();
    }

    private void DrawBackShadow(Matrix4f cubeXform)
    {
        Matrix4f shadowMat = myShadowMatrix(backPlane, lightPos);
        lmb2.setColor(Colors.SHADOW_COLOR);
        lmb2.modelToWorld = Matrix4f.mul(cubeXform, shadowMat);
        lmb2.draw();
    }

    public void display()
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        DrawGround();
        DrawBack();
        Matrix4f cubeXform = CalculateCubeXform();

        DrawMainBlock(cubeXform);

        GLES20.glDepthMask(false);

        GLES20.glEnable(GLES20.GL_BLEND);

        DrawGroundShadow(cubeXform);
        DrawBackShadow(cubeXform);

        GLES20.glDepthMask(true);

        GLES20.glDisable(GLES20.GL_BLEND);
        if (rotateCube) tick++;
        if (tick >= 120) {
            tick = 0;
        }
    }

    int initialized = 0;

    Matrix4f myShadowMatrix(Vector4f groundVector, Vector4f lightVector)
    {

        float dot;
        Matrix4f shadowMat = Matrix4f.Identity();

        dot = Vector4f.Dot(groundVector, lightVector);
        shadowMat = shadowMat.mul(dot);
        shadowMat.SetRow0(shadowMat.GetRow0().sub(groundVector.Mult(lightVector.x)));
        shadowMat.SetRow1(shadowMat.GetRow1().sub(groundVector.Mult(lightVector.y)));
        shadowMat.SetRow2(shadowMat.GetRow2().sub(groundVector.Mult(lightVector.z)));
        shadowMat.SetRow3(shadowMat.GetRow3().sub(groundVector.Mult(lightVector.w)));
        shadowMat.Transpose();
        return shadowMat;
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        result.append(keyCode);
        if (displayOptions)
        {
            setDisplayOptions(keyCode);
        }
        else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    displayOptions = true;
                    break;
                case KeyEvent.KEYCODE_I:
                    result.append("rotation = " + rotation.toString());
                    result.append("lmb3.modelToWorld = " + lmb2.modelToWorld.toString());
                    result.append("matrixesAreDifferent = " + String.valueOf(matrixesAreDifferent));
                    result.append(Programs.dumpShaders());
                    break;
                case KeyEvent.KEYCODE_M:
                {
                    lmb3Offset -= 0.1f;
                    int program = Programs.getProgram(sCubeProgram);
                    GLES20.glUseProgram(program);
                    GLES20.glUniform4f(offsetUnif, 0f, 0f, lmb3Offset, 0f);
                    result.append("lmb3Offset = " + lmb3Offset);
                    GLES20.glUseProgram(0);
                }
                break;
                case KeyEvent.KEYCODE_P:
                {
                    lmb3Offset += 0.1f;
                    int program = Programs.getProgram(sCubeProgram);
                    GLES20.glUseProgram(program);
                    GLES20.glUniform4f(offsetUnif, 0f, 0f, lmb3Offset, 0f);
                    result.append("lmb3Offset = " + lmb3Offset);
                    GLES20.glUseProgram(0);
                }
                break;
                case KeyEvent.KEYCODE_R:
                    rotateCube = !rotateCube;
                    break;
            }
        }
        return result.toString();
    }
}
