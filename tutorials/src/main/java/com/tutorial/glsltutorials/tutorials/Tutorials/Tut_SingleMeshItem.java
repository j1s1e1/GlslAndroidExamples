package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Camera;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Text.TextClass;

import java.io.InputStream;

/**
 * Created by Jamie on 6/8/14.
 */
public class Tut_SingleMeshItem extends TutorialBase {

    static float g_fzNear = 1.0f;
    static float g_fzFar = 1000.0f;

    int UniformColor;
    int ObjectColor;
    int UniformColorTint;

    int g_WhiteDiffuseColor;
    int g_VertexDiffuseColor;
    int g_WhiteAmbDiffuseColor;
    int g_VertexAmbDiffuseColor;

    int currentProgram;

    String touchTextString = " ";
    TextClass touchText;
    boolean updateTouchText = false;


    void InitializeProgram()
    {
        UniformColor = Programs.addProgram(VertexShaders.PosOnlyWorldTransform_vert, FragmentShaders.ColorUniform_frag);
        Programs.setUniformColor(UniformColor, new Vector4f(0.694f, 0.4f, 0.106f, 1.0f));

        UniformColorTint = Programs.addProgram(VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorMultUniform_frag);
        Programs.setUniformColor(UniformColorTint, new Vector4f(0.5f, 0.5f, 0f, 1.0f));

        g_WhiteDiffuseColor = Programs.addProgram(VertexShaders.PosColorLocalTransform_vert, FragmentShaders.ColorPassthrough_frag);

        g_WhiteAmbDiffuseColor = Programs.addProgram(VertexShaders.DirAmbVertexLighting_PN_vert, FragmentShaders.ColorPassthrough_frag);
        Programs.setDirectionToLight(g_WhiteAmbDiffuseColor, new Vector3f(10f, 10f, 0f));
        Programs.setLightIntensity(g_WhiteAmbDiffuseColor, new Vector4f(0.5f, 0.5f, 0.5f, 0.5f));
        Programs.setAmbientIntensity(g_WhiteAmbDiffuseColor, new Vector4f(0.3f, 0.0f, 0.3f, 0.6f));

        Matrix3f m = Matrix3f.Identity();
        Programs.setNormalModelToCameraMatrix(g_WhiteAmbDiffuseColor, m);

        ObjectColor = Programs.addProgram(VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorPassthrough_frag);
        currentProgram = ObjectColor;
    }
    static Mesh current_mesh;
    static Mesh g_pCubeColorMesh;
    static Mesh g_pCylinderMesh;

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init() throws Exception
    {
        Programs.reset();
        Shape.resetWorldToCameraMatrix();
        InitializeProgram();
        try 
        {
            InputStream UnitCubeColor = Shader.context.getResources().openRawResource(R.raw.unitcubecolor);
            g_pCubeColorMesh = new Mesh(UnitCubeColor);
            InputStream UnitCylinder = Shader.context.getResources().openRawResource(R.raw.unitcylinder);
            g_pCylinderMesh = new Mesh(UnitCylinder);
        } catch (Exception ex) {
            throw new Exception("Error " + ex.toString());
        }

        setupDepthAndCull();
        reshape();
        current_mesh = g_pCubeColorMesh;
        touchText = new TextClass(" ", 0.5f, 0.05f);
        touchText.setOffset(-0.5f, 0.5f, 0.0f);
    }

    public void display() throws Exception
    {
        clearDisplay();

        if (current_mesh != null)
        {

            MatrixStack modelMatrix = new MatrixStack();

            GLES20.glDisable(GLES20.GL_DEPTH_TEST);
            {
                try (PushStack pushstack = new PushStack(modelMatrix)) {
                    modelMatrix.Translate(Camera.g_camTarget);
                    modelMatrix.Translate(50f, 50f, 0f);
                    modelMatrix.Scale(15.0f, 15.0f, 15.0f);
                    modelMatrix.Rotate(axis, angle);
                    angle = angle + 1f;

                    Matrix4f mm = modelMatrix.Top();

                    if (noWorldMatrix) {
                        Matrix4f cm2 = Matrix4f.Mult(mm, cm);
                        Programs.setModelToCameraMatrix(currentProgram, cm2);
                    } else {
                        //mm = Matrix4f.Identity();
                        //mm.Scale(new Vector3f(0.02f, 0.02f, 0.02f));
                        Programs.setModelToWorldMatrix(currentProgram, mm);

                    }
                }
                Programs.use(currentProgram);
                current_mesh.Render();
                GLES20.glUseProgram(0);
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            }
        }
        touchText.draw();
        if (updateTouchText)
        {
            touchText.UpdateText(touchTextString);
            updateTouchText = false;
        }
    }

    static Vector3f axis = new Vector3f(1f, 1f, 0);
    static float angle = 0;

    static Matrix4f pm;
    static Matrix4f cm;

    static private void SetGlobalMatrices(int program)
    {
        Programs.setCameraToClipMatrixUnif(program, pm);
        Programs.setWorldToCameraMatrixUnif(program, cm);
    }

    //Called whenever the window is resized. The new window size is given, in pixels.
    //This is an opportunity to call glViewport or glScissor to keep up with the change in size.
    public void reshape()
    {
        MatrixStack camMatrix = new MatrixStack();
        camMatrix.SetMatrix(Camera.GetLookAtMatrix());

        cm = camMatrix.Top();

        MatrixStack persMatrix = new MatrixStack();
        persMatrix.Perspective(45.0f, (width / (float)height), g_fzNear, g_fzFar);
        pm = persMatrix.Top();

        SetGlobalMatrices(currentProgram);

        GLES20.glViewport(0, 0, width, height);
    }

    static boolean noWorldMatrix = false;

    //Called whenever a key on the keyboard was pressed.
    //The key is given by the ''key'' parameter, which is in ASCII.
    //It's often a good idea to have the escape key (ASCII value 27) call glutLeaveMainLoop() to 
    //exit the program.
    public String keyboard(int keyCode, int x, int y) throws Exception
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode) {
            case KeyEvent.KEYCODE_A:
                currentProgram = g_WhiteAmbDiffuseColor;
                noWorldMatrix = true;
                break;
            case KeyEvent.KEYCODE_B:
                current_mesh = g_pCylinderMesh;
                break;
            case KeyEvent.KEYCODE_C:
                current_mesh = g_pCubeColorMesh;
                break;
            case KeyEvent.KEYCODE_D:
                currentProgram = g_WhiteDiffuseColor;
                noWorldMatrix = true;
                break;
            case KeyEvent.KEYCODE_O:
                currentProgram = ObjectColor;
                noWorldMatrix = false;
                break;
            case KeyEvent.KEYCODE_U:
                currentProgram = UniformColor;
                noWorldMatrix = false;
                break;
            case KeyEvent.KEYCODE_T:
                currentProgram = UniformColorTint;
                noWorldMatrix = false;
                break;
            case KeyEvent.KEYCODE_W:
                currentProgram = g_WhiteDiffuseColor;
                noWorldMatrix = true;
                break;
            case KeyEvent.KEYCODE_ESCAPE:
                //timer.Enabled = false;
                break;
            case KeyEvent.KEYCODE_SPACE:
                break;
        }
        result.append(keyCode);
        reshape();
        return result.toString();
    }

    public void touchEvent(int x_position, int y_position) throws Exception {
        int selectionX = x_position / (width / 7);
        int selectoinY = y_position / (height / 4);
        String lastTouchTextString = touchTextString;
        switch (selectoinY) {
            case 0:
                switch (selectionX) {
                    case 0:
                        currentProgram = g_WhiteDiffuseColor;
                        noWorldMatrix = true;
                        touchTextString = "g_WhiteDiffuseColor";
                        break;
                    case 1:
                        currentProgram = ObjectColor;
                        noWorldMatrix = false;
                        touchTextString = "ObjectColor";
                        break;
                    case 2:
                        currentProgram = UniformColor;
                        noWorldMatrix = false;
                        touchTextString = "UniformColor";
                        break;
                    case 3:
                        currentProgram = UniformColorTint;
                        noWorldMatrix = false;
                        touchTextString = "UniformColorTint";
                        break;
                    case 4:
                        currentProgram = g_WhiteDiffuseColor;
                        noWorldMatrix = true;
                        touchTextString = "g_WhiteDiffuseColor";
                        break;
                    case 5:
                        currentProgram = g_WhiteAmbDiffuseColor;
                        noWorldMatrix = true;
                        touchTextString = "g_WhiteAmbDiffuseColor";
                        break;
                }
                break;
            case 3: {
                switch (selectionX) {
                    case 0:
                        current_mesh = g_pCylinderMesh;
                        break;
                    case 1:
                        current_mesh = g_pCubeColorMesh;
                        break;
                }
                break;
            }
            default:
                break;
        }
        if (!touchTextString.equals(lastTouchTextString)) {
            updateTouchText = true;
        }
    }
}
