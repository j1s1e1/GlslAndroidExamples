package com.tutorial.glsltutorials.tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;

import java.io.InputStream;

/**
 * Created by Jamie on 6/8/14.
 */
public class SingleMeshItem extends TutorialBase {
    class ProgramData
    {
        public int theProgram;
        public int positionAttribute;
        public int colorAttribute;
        public int modelToCameraMatrixUnif;
        public int modelToWorldMatrixUnif;
        public int worldToCameraMatrixUnif;
        public int cameraToClipMatrixUnif;
        public int baseColorUnif;

        public int normalModelToCameraMatrixUnif;
        public int dirToLightUnif;
        public int lightIntensityUnif;
        public int ambientIntensityUnif;
        public int normalAttribute;
    };

    static float g_fzNear = 1.0f;
    static float g_fzFar = 1000.0f;

    static ProgramData UniformColor;
    static ProgramData ObjectColor;
    static ProgramData UniformColorTint;

    static ProgramData g_WhiteDiffuseColor;
    static ProgramData g_VertexDiffuseColor;
    static ProgramData g_WhiteAmbDiffuseColor;
    static ProgramData g_VertexAmbDiffuseColor;


    static ProgramData currentProgram;

    ProgramData LoadProgram(String strVertexShader, String strFragmentShader) throws Exception
    {
        ProgramData data = new ProgramData();
        int vertex_shader = Shader.loadShader30(GLES20.GL_VERTEX_SHADER, strVertexShader);
        int fragment_shader = Shader.loadShader30(GLES20.GL_FRAGMENT_SHADER, strFragmentShader);
        data.theProgram  = Shader.createAndLinkProgram30(vertex_shader, fragment_shader);

        data.positionAttribute = GLES20.glGetAttribLocation(data.theProgram, "position");
        data.colorAttribute = GLES20.glGetAttribLocation(data.theProgram, "color");

        data.modelToWorldMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToWorldMatrix");
        data.worldToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "worldToCameraMatrix");
        data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");
        data.baseColorUnif = GLES20.glGetUniformLocation(data.theProgram, "baseColor");

        data.modelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToCameraMatrix");

        data.normalModelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "normalModelToCameraMatrix");
        data.dirToLightUnif =  GLES20.glGetUniformLocation(data.theProgram, "dirToLight");
        data.lightIntensityUnif = GLES20.glGetUniformLocation(data.theProgram, "lightIntensity");
        data.ambientIntensityUnif = GLES20.glGetUniformLocation(data.theProgram, "ambientIntensity");
        data.normalAttribute = GLES20.glGetAttribLocation(data.theProgram, "normal");

        if (data.normalAttribute != -1)
        {
            AssignNormals(data);
            //throw new Exception("Normals needed");
        }

        return data;
    }

    static void AssignNormals(ProgramData program)
    {
        GLES20.glUseProgram(program.theProgram);


        GLES20.glUseProgram(0);
    }

    void InitializeProgram() throws Exception
    {
        UniformColor = LoadProgram(VertexShaders.PosOnlyWorldTransform_vert, FragmentShaders.ColorUniform_frag);
        GLES20.glUseProgram(UniformColor.theProgram);
        GLES20.glUniform4f(UniformColor.baseColorUnif, 0.694f, 0.4f, 0.106f, 1.0f);
        GLES20.glUseProgram(0);

        UniformColorTint = LoadProgram(VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorMultUniform_frag);
        GLES20.glUseProgram(UniformColorTint.theProgram);
        GLES20.glUniform4f(UniformColorTint.baseColorUnif, 0.5f, 0.5f, 0f, 1.0f);
        GLES20.glUseProgram(0);

        g_WhiteDiffuseColor = LoadProgram(VertexShaders.PosColorLocalTransform_vert, FragmentShaders.ColorPassthrough_frag);

        g_WhiteAmbDiffuseColor = LoadProgram(VertexShaders.DirAmbVertexLighting_PN_vert, FragmentShaders.ColorPassthrough_frag);
        GLES20.glUseProgram(g_WhiteAmbDiffuseColor.theProgram);
        Vector3f light_direction = new Vector3f(10f, 10f, 0f);
        Vector4f light_intensity = new Vector4f(0.5f, 0.5f, 0.5f, 0.5f);
        Vector4f  ambient_intensity = new Vector4f(0.3f, 0.0f, 0.3f, 0.6f);
        GLES20.glUniform3fv(g_WhiteAmbDiffuseColor.dirToLightUnif, 1, light_direction.toArray(), 0);
        GLES20.glUniform4fv(g_WhiteAmbDiffuseColor.lightIntensityUnif, 1, light_intensity.toArray(), 0);
        GLES20.glUniform4fv(g_WhiteAmbDiffuseColor.ambientIntensityUnif, 1, ambient_intensity.toArray(), 0);
        Matrix3f m = Matrix3f.Identity();
        GLES20.glUniformMatrix3fv(g_WhiteAmbDiffuseColor.normalModelToCameraMatrixUnif, 1, false, m.toArray(), 0);
        GLES20.glUseProgram(0);

        ObjectColor = LoadProgram(VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorPassthrough_frag);
        currentProgram = ObjectColor;
    }
    static Mesh current_mesh;
    static Mesh g_pCubeColorMesh;
    static Mesh g_pCylinderMesh;

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init() throws Exception
    {
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

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CW);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthMask(true);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthRangef(0.0f, 1.0f);
        reshape();
        current_mesh = g_pCubeColorMesh;
    }

    float g_fColumnBaseHeight = 0.25f;

    float g_fParthenonWidth = 14.0f;
    float g_fParthenonLength = 20.0f;
    float g_fParthenonColumnHeight = 5.0f;
    float g_fParthenonBaseHeight = 1.0f;
    float g_fParthenonTopHeight = 2.0f;

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display() throws Exception
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

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

                    GLES20.glUseProgram(currentProgram.theProgram);
                    Matrix4f mm = modelMatrix.Top();

                    if (noWorldMatrix) {
                        Matrix4f cm2 = Matrix4f.Mult(mm, cm);
                        GLES20.glUniformMatrix4fv(currentProgram.modelToCameraMatrixUnif, 1, false, cm2.toArray(), 0);
                    } else {
                        //mm = Matrix4f.Identity();
                        //mm.Scale(new Vector3f(0.02f, 0.02f, 0.02f));
                        GLES20.glUniformMatrix4fv(currentProgram.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
                    }
                }
                current_mesh.Render();
                GLES20.glUseProgram(0);
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            }
        }
    }

    static Vector3f axis = new Vector3f(1f, 1f, 0);
    static float angle = 0;

    static Matrix4f pm;
    static Matrix4f cm;

    static private void SetGlobalMatrices(ProgramData program)
    {
        GLES20.glUseProgram(program.theProgram);
        GLES20.glUniformMatrix4fv(program.cameraToClipMatrixUnif, 1, false, pm.toArray(), 0);  // this one is first
        GLES20.glUniformMatrix4fv(program.worldToCameraMatrixUnif, 1, false, cm.toArray(), 0); // this is the second one
        GLES20.glUseProgram(0);
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
        display();
        return result.toString();
    }

}
