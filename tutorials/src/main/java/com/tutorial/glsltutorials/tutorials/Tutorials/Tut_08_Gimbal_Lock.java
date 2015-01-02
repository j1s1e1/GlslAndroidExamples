package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.R;

import java.io.InputStream;

/**
 * Created by Jamie on 6/8/14.
 */
public class Tut_08_Gimbal_Lock extends TutorialBase {
    float currentScale = 1.0f;
    void InitializeProgram()
    {
        fFrustumScale = calcFrustumScale(45.0f);
        float fzNear = 1.0f;
        float fzFar = 600.0f;
        int vertex_shader = Shader.loadShader30(GLES20.GL_VERTEX_SHADER, VertexShaders.PosColorLocalTransform_vert);
        int fragment_shader = Shader.loadShader30(GLES20.GL_FRAGMENT_SHADER, FragmentShaders.ColorMultUniform_frag);
        theProgram = Shader.createAndLinkProgram30(vertex_shader, fragment_shader);
        positionAttribute = GLES20.glGetAttribLocation(theProgram, "position");
        colorAttribute = GLES20.glGetAttribLocation(theProgram, "color");


        modelToCameraMatrixUnif = GLES20.glGetUniformLocation(theProgram, "modelToCameraMatrix");
        cameraToClipMatrixUnif = GLES20.glGetUniformLocation(theProgram, "cameraToClipMatrix");
        baseColorUnif = GLES20.glGetUniformLocation(theProgram, "baseColor");

        cameraToClipMatrix.M11 = fFrustumScale;
        cameraToClipMatrix.M22 = fFrustumScale;
        cameraToClipMatrix.M33 = (fzFar + fzNear) / (fzNear - fzFar);
        cameraToClipMatrix.M34 = -1.0f;
        cameraToClipMatrix.M43 = (2 * fzFar * fzNear) / (fzNear - fzFar);

        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(cameraToClipMatrixUnif, 1, false, cameraToClipMatrix.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    enum GimbalAxis
    {
        GIMBAL_X_AXIS,
        GIMBAL_Y_AXIS,
        GIMBAL_Z_AXIS,
    };

    static Mesh[] g_Gimbals = new Mesh[3];
    static String[] g_strGimbalNames =
    {
            "largegimbal.xml",
            "mediumgimbal.xml",
            "smallgimbal.xml",
    };

    static boolean g_bDrawGimbals = true;

    void DrawGimbal(MatrixStack currMatrix, GimbalAxis eAxis, Vector4f baseColor) throws Exception
    {
        if(g_bDrawGimbals == false)
            return;

        {
            PushStack pushstack = new PushStack(currMatrix);
            switch(eAxis)
            {
                case GIMBAL_X_AXIS:
                    break;
                case GIMBAL_Y_AXIS:
                    currMatrix.RotateZ(90.0f);
                    currMatrix.RotateX(90.0f);
                    break;
                case GIMBAL_Z_AXIS:
                    currMatrix.RotateY(90.0f);
                    currMatrix.RotateX(90.0f);
                    break;
            }

            GLES20.glUseProgram(theProgram);
            //Set the base color for this object.
            GLES20.glUniform4fv(baseColorUnif, 1, baseColor.toArray(), 0);
            Matrix4f cm = currMatrix.Top();
            GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, cm.toArray(), 0);

            switch (eAxis)
            {
                case GIMBAL_X_AXIS: g_Gimbals[0].render(); break;
                case GIMBAL_Y_AXIS: g_Gimbals[1].render(); break;
                case GIMBAL_Z_AXIS: g_Gimbals[2].render(); break;
            }

            GLES20.glUseProgram(0);
        }
    }

    static Mesh g_pObject = null;

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init() throws Exception
    {
        InitializeProgram();

        try
        {
            InputStream smallgimbal = Shader.context.getResources().openRawResource(R.raw.smallgimbal);
            g_Gimbals[0] = new Mesh(smallgimbal);
            InputStream mediumgimbal = Shader.context.getResources().openRawResource(R.raw.mediumgimbal);
            g_Gimbals[1] = new Mesh(mediumgimbal);
            InputStream largegimbal = Shader.context.getResources().openRawResource(R.raw.largegimbal);
            g_Gimbals[2] = new Mesh(largegimbal);
            InputStream ship = Shader.context.getResources().openRawResource(R.raw.ship);
            g_pObject = new Mesh(ship);
        }
        catch(Exception ex)
        {
            throw new Exception("Error:" + ex.toString());
        }

        setupDepthAndCull();
    }

    class GimbalAngles
    {
        public float fAngleX;
        public float fAngleY;
        public float fAngleZ;
    };

    GimbalAngles g_angles = new GimbalAngles();

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display() throws Exception
    {
        clearDisplay();

        MatrixStack currMatrix = new MatrixStack();
        currMatrix.Translate(new Vector3f(0.0f, 0.0f, -200.0f / currentScale));
        currMatrix.RotateX(g_angles.fAngleX);
        DrawGimbal(currMatrix, GimbalAxis.GIMBAL_X_AXIS, new Vector4f(0.4f, 0.4f, 1.0f, 1.0f));
        currMatrix.RotateY(g_angles.fAngleY);
        DrawGimbal(currMatrix, GimbalAxis.GIMBAL_Y_AXIS, new Vector4f(0.0f, 1.0f, 0.0f, 1.0f));
        currMatrix.RotateZ(g_angles.fAngleZ);
        DrawGimbal(currMatrix, GimbalAxis.GIMBAL_Z_AXIS, new Vector4f(1.0f, 0.3f, 0.3f, 1.0f));

        GLES20.glUseProgram(theProgram);
        currMatrix.Scale(3.0f, 3.0f, 3.0f);
        currMatrix.RotateX(-90);
        //Set the base color for this object.
        GLES20.glUniform4f(baseColorUnif, 1.0f, 1.0f, 1.0f, 1.0f);
        Matrix4f cm = currMatrix.Top();
        GLES20.glUniformMatrix4fv(modelToCameraMatrixUnif, 1, false, cm.toArray(), 0);

        g_pObject.render("tint");

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

    static float STANDARD_ANGLE_INCREMENT = 11.25f;
    static float  SMALL_ANGLE_INCREMENT = 9.0f;

    //Called whenever a key on the keyboard was pressed.
    //The key is given by the ''key'' parameter, which is in ASCII.
    //It's often a good idea to have the escape key (ASCII value 27) call glutLeaveMainLoop() to 
    //exit the program.
    public String keyboard(int keyCode, int x, int y) throws Exception
    {
        String result = String.valueOf(keyCode);
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_ESCAPE:
                //timer.Enabled = false;
                break;
            case KeyEvent.KEYCODE_W: g_angles.fAngleX += SMALL_ANGLE_INCREMENT; break;
            case KeyEvent.KEYCODE_S: g_angles.fAngleX -= SMALL_ANGLE_INCREMENT; break;

            case KeyEvent.KEYCODE_A: g_angles.fAngleY += SMALL_ANGLE_INCREMENT; break;
            case KeyEvent.KEYCODE_D: g_angles.fAngleY -= SMALL_ANGLE_INCREMENT; break;

            case KeyEvent.KEYCODE_Q: g_angles.fAngleZ += SMALL_ANGLE_INCREMENT; break;
            case KeyEvent.KEYCODE_E: g_angles.fAngleZ -= SMALL_ANGLE_INCREMENT; break;

            case KeyEvent.KEYCODE_SPACE:
                g_bDrawGimbals = !g_bDrawGimbals;
                break;
        }

        display();
        return result;
    }

    public void touchEvent(int x_position, int y_position) throws Exception
    {
        int selection = x_position / (width / 6);
        switch (selection)
        {
            case 0: g_angles.fAngleX += SMALL_ANGLE_INCREMENT; break;
            case 1: g_angles.fAngleX -= SMALL_ANGLE_INCREMENT; break;
            case 2: g_angles.fAngleY += SMALL_ANGLE_INCREMENT; break;
            case 3: g_angles.fAngleY -= SMALL_ANGLE_INCREMENT; break;
            case 4: g_angles.fAngleY += SMALL_ANGLE_INCREMENT; break;
            case 5: g_angles.fAngleY += SMALL_ANGLE_INCREMENT; break;
        }
    }

    public void setScale(float scale)
    {
        currentScale = scale;
    }
}
