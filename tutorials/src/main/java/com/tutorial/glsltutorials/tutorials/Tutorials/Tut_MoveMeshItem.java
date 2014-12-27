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
import com.tutorial.glsltutorials.tutorials.Light.LightBlock;
import com.tutorial.glsltutorials.tutorials.Material.MaterialBlock;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.R;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by jamie on 12/26/14.
 */
public class Tut_MoveMeshItem extends TutorialBase {
    boolean renderWithString = false;
    String renderString = "";

    class ProgramData {
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

        public LightBlock lightBlock;
        public MaterialBlock materialBlock;
    }


    static float g_fzNear = 10.0f;
    static float g_fzFar = 1000.0f;

    static ProgramData UniformColor;
    static ProgramData ObjectColor;
    static ProgramData UniformColorTint;

    static ProgramData g_WhiteDiffuseColor;
    static ProgramData g_VertexDiffuseColor;
    static ProgramData g_WhiteAmbDiffuseColor;
    static ProgramData g_VertexAmbDiffuseColor;
    static ProgramData g_Unlit;
    static ProgramData g_litShaderProg;


    static ProgramData currentProgram;

    static Vector4f g_lightDirection = new Vector4f(0.866f, 0.5f, 0.0f, 0.0f);
    Vector3f dirToLight = new Vector3f(0.5f, 0.5f, 1f);

    float perspectiveAngle = 60f;
    float newPerspectiveAngle = 60f;

    ProgramData LoadProgram(String strVertexShader, String strFragmentShader) {
        ProgramData data = new ProgramData();
        int vertex_shader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, strVertexShader);
        int fragment_shader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, strFragmentShader);
        data.theProgram = Shader.createAndLinkProgram(vertex_shader, fragment_shader);

        data.positionAttribute = GLES20.glGetAttribLocation(data.theProgram, "position");
        data.colorAttribute = GLES20.glGetAttribLocation(data.theProgram, "color");

        data.modelToWorldMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToWorldMatrix");
        data.worldToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "worldToCameraMatrix");
        data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");
        if (data.cameraToClipMatrixUnif == -1) {
            data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "Projection.cameraToClipMatrix");
        }
        data.baseColorUnif = GLES20.glGetUniformLocation(data.theProgram, "baseColor");
        if (data.baseColorUnif == -1) {
            data.baseColorUnif = GLES20.glGetUniformLocation(data.theProgram, "objectColor");
        }

        data.modelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToCameraMatrix");

        data.normalModelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "normalModelToCameraMatrix");
        data.dirToLightUnif = GLES20.glGetUniformLocation(data.theProgram, "dirToLight");
        data.lightIntensityUnif = GLES20.glGetUniformLocation(data.theProgram, "lightIntensity");
        data.ambientIntensityUnif = GLES20.glGetUniformLocation(data.theProgram, "ambientIntensity");
        data.normalAttribute = GLES20.glGetAttribLocation(data.theProgram, "normal");

        return data;
    }

    void InitializeProgram() {
        UniformColor = LoadProgram(VertexShaders.PosOnlyWorldTransform_vert, FragmentShaders.ColorUniform_frag);
        GLES20.glUseProgram(UniformColor.theProgram);
        GLES20.glUniform4f(UniformColor.baseColorUnif, 0.694f, 0.4f, 0.106f, 1.0f);
        GLES20.glUseProgram(0);

        UniformColorTint = LoadProgram(VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorMultUniform_frag);
        GLES20.glUseProgram(UniformColorTint.theProgram);
        GLES20.glUniform4f(UniformColorTint.baseColorUnif, 0.5f, 0.5f, 0f, 1.0f);
        GLES20.glUseProgram(0);

        g_WhiteDiffuseColor = LoadProgram(VertexShaders.PosColorLocalTransform_vert,
                FragmentShaders.ColorPassthrough_frag);

        g_WhiteAmbDiffuseColor = LoadProgram(VertexShaders.DirAmbVertexLighting_PN_vert,
                FragmentShaders.ColorPassthrough_frag);

        g_VertexDiffuseColor = LoadProgram(VertexShaders.DirVertexLighting_PCN,
                FragmentShaders.ColorPassthrough_frag);

        g_Unlit = LoadProgram(VertexShaders.unlit, FragmentShaders.unlit);

        g_litShaderProg = LoadProgram(VertexShaders.BasicTexture_PN, FragmentShaders.ShaderGaussian);

        GLES20.glUseProgram(g_VertexDiffuseColor.theProgram);
        Vector4f lightIntensity = new Vector4f(0.5f, 0.5f, 0.5f, 1.0f);
        GLES20.glUniform3fv(g_VertexDiffuseColor.dirToLightUnif, 1, dirToLight.toArray(), 0);
        GLES20.glUniform4fv(g_VertexDiffuseColor.lightIntensityUnif, 1, lightIntensity.toArray(), 0);
        GLES20.glUseProgram(0);

        GLES20.glUseProgram(g_WhiteAmbDiffuseColor.theProgram);
        Vector3f light_direction = new Vector3f(10f, 10f, 0f);
        Vector4f light_intensity = new Vector4f(0.5f, 0.5f, 0.5f, 0.5f);
        Vector4f ambient_intensity = new Vector4f(0.3f, 0.0f, 0.3f, 0.6f);
        GLES20.glUniform3fv(g_WhiteAmbDiffuseColor.dirToLightUnif, 1, light_direction.toArray(), 0);
        GLES20.glUniform4fv(g_WhiteAmbDiffuseColor.lightIntensityUnif, 1, light_intensity.toArray(), 0);
        GLES20.glUniform4fv(g_WhiteAmbDiffuseColor.ambientIntensityUnif, 1, ambient_intensity.toArray(), 0);
        Matrix3f m = Matrix3f.Identity();
        GLES20.glUniformMatrix3fv(g_WhiteAmbDiffuseColor.normalModelToCameraMatrixUnif, 1, false, m.toArray(), 0);
        GLES20.glUseProgram(0);

        GLES20.glUseProgram(g_Unlit.theProgram);
        GLES20.glUniform4f(g_Unlit.baseColorUnif, 0.5f, 0.5f, 0f, 1.0f);
        Matrix4f test = Matrix4f.Identity();
        GLES20.glUniformMatrix4fv(g_Unlit.cameraToClipMatrixUnif, 1, false, test.toArray(), 0);
        GLES20.glUseProgram(0);

        // Test shader lights and materials
        GLES20.glUseProgram(g_litShaderProg.theProgram);
        g_litShaderProg.lightBlock = new LightBlock(2);
        g_litShaderProg.lightBlock.setUniforms(g_litShaderProg.theProgram);

        g_litShaderProg.lightBlock.ambientIntensity = new Vector4f(0.1f, 0.1f, 0.1f, 1.0f);

        g_litShaderProg.lightBlock.lights[0].cameraSpaceLightPos = new Vector4f(4.0f, 0.0f, 1.0f, 1.0f);
        g_litShaderProg.lightBlock.lights[0].lightIntensity = new Vector4f(0.7f, 0.0f, 0.0f, 1.0f);

        g_litShaderProg.lightBlock.lights[1].cameraSpaceLightPos = new Vector4f(4.0f, 0.0f, 1.0f, 1.0f);
        g_litShaderProg.lightBlock.lights[1].lightIntensity = new Vector4f(0.0f, 0.0f, 0.7f, 1.0f);

        g_litShaderProg.lightBlock.updateInternal();

        g_litShaderProg.materialBlock = new MaterialBlock(new Vector4f(0.0f, 0.3f, 0.0f, 1.0f),
                new Vector4f(0.5f, 0.0f, 0.5f, 1.0f), 0.6f);
        g_litShaderProg.materialBlock.setUniforms(g_litShaderProg.theProgram);
        g_litShaderProg.materialBlock.updateInternal();

        GLES20.glUseProgram(0);

        ObjectColor = LoadProgram(VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorPassthrough_frag);
        currentProgram = ObjectColor;
    }

    Mesh current_mesh;
    Mesh g_pCubeColorMesh;
    Mesh g_pCylinderMesh;
    Mesh g_pPlaneMesh;
    Mesh g_pInfinityMesh;
    Mesh g_unitSphereMesh;

    protected void init() throws Exception {
        InitializeProgram();

        try {
            InputStream UnitCubeColor = Shader.context.getResources().openRawResource(R.raw.unitcubecolor);
            g_pCubeColorMesh = new Mesh(UnitCubeColor);


            InputStream UnitCylinder = Shader.context.getResources().openRawResource(R.raw.unitcylinder);
            g_pCylinderMesh = new Mesh(UnitCylinder);

            InputStream unitplane = Shader.context.getResources().openRawResource(R.raw.unitplane);
            g_pPlaneMesh = new Mesh(unitplane);

            InputStream infinity = Shader.context.getResources().openRawResource(R.raw.infinity);
            g_pInfinityMesh = new Mesh(infinity);

            InputStream unitSphere = Shader.context.getResources().openRawResource(R.raw.unitsphere12);
            g_unitSphereMesh = new Mesh(unitSphere);

        } catch (Exception ex) {
            throw new Exception("Error " + ex.toString());
        }

        setupDepthAndCull();

        Camera.Move(0f, 0f, 0f);
        Camera.MoveTarget(0f, 0f, 0.0f);
        reshape();
        current_mesh = g_pCubeColorMesh;
    }

    public void display() throws Exception {
        clearDisplay();

        if (current_mesh != null) {
            MatrixStack modelMatrix = new MatrixStack();
            try (PushStack pushstack = new PushStack(modelMatrix)) {
                modelMatrix.Rotate(axis, angle);   // rotate last to leave in place
                modelMatrix.Translate(Camera.g_camTarget);
                modelMatrix.Scale(15.0f, 15.0f, 15.0f);


                GLES20.glUseProgram(currentProgram.theProgram);
                Matrix4f mm = modelMatrix.Top();

                if (noWorldMatrix) {
                    Matrix4f cm2 = Matrix4f.Mult(mm, cm);
                    GLES20.glUniformMatrix4fv(currentProgram.modelToCameraMatrixUnif, 1, false, cm2.toArray(), 0);
                    if (currentProgram.normalModelToCameraMatrixUnif != 0) {
                        Matrix3f normalModelToCameraMatrix = Matrix3f.Identity();
                        Matrix4f applyMatrix = Matrix4f.Mult(Matrix4f.Identity(),
                                Matrix4f.CreateTranslation(dirToLight));
                        normalModelToCameraMatrix = new Matrix3f(applyMatrix);
                        // FIXME normalModelToCameraMatrix.Invert();
                        GLES20.glUniformMatrix3fv(currentProgram.normalModelToCameraMatrixUnif, 1, false, normalModelToCameraMatrix.toArray(), 0);
                        //Matrix4f cameraToClipMatrix = Matrix4f.Identity;
                        //GLES20.glUniformMatrix4f(currentProgram.cameraToClipMatrixUnif, false, 1, cameraToClipMatrix); 

                    }
                    //Matrix4f cameraToClipMatrix = Matrix4f.Identity;
                    //GLES20.glUniformMatrix4f(currentProgram.cameraToClipMatrixUnif, false, 1, cameraToClipMatrix); 
                } else {
                    GLES20.glUniformMatrix4fv(currentProgram.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
                }
            }
            if (renderWithString) {
                current_mesh.Render(renderString);
            } else {
                current_mesh.Render();
            }
            GLES20.glUseProgram(0);
            if (perspectiveAngle != newPerspectiveAngle) {
                perspectiveAngle = newPerspectiveAngle;
                reshape();
            }
        }
    }

    static Vector3f axis = new Vector3f(1f, 1f, 0);
    static float angle = 0;

    static Matrix4f pm;
    static Matrix4f cm;

    static private void SetGlobalMatrices(ProgramData program) {
        GLES20.glUseProgram(program.theProgram);
        GLES20.glUniformMatrix4fv(program.cameraToClipMatrixUnif, 1, false, pm.toArray(), 0);  // this one is first
        GLES20.glUniformMatrix4fv(program.worldToCameraMatrixUnif, 1, false, cm.toArray(), 0); // this is the second one
        GLES20.glUseProgram(0);
    }

    public void reshape() {
        MatrixStack camMatrix = new MatrixStack();
        camMatrix.SetMatrix(Camera.GetLookAtMatrix());

        cm = camMatrix.Top();

        MatrixStack persMatrix = new MatrixStack();
        persMatrix.Perspective(perspectiveAngle, (width / (float) height), g_fzNear, g_fzFar);
        pm = persMatrix.Top();

        SetGlobalMatrices(currentProgram);

        GLES20.glViewport(0, 0, width, height);

    }

    static boolean noWorldMatrix = false;


    public String keyboard(int keyCode, int x, int y) {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        switch (keyCode) {
            case KeyEvent.KEYCODE_NUMPAD_6:
                Camera.MoveTarget(0.5f, 0f, 0.0f);
                result.append(Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_4:
                Camera.MoveTarget(-0.5f, 0f, 0.0f);
                result.append(Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_8:
                Camera.MoveTarget(0.0f, 0.5f, 0.0f);
                result.append(Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_2:
                Camera.MoveTarget(0f, -0.5f, 0.0f);
                result.append(Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_7:
                Camera.MoveTarget(0.0f, 0.0f, 0.5f);
                result.append(Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_3:
                Camera.MoveTarget(0f, 0.0f, -0.5f);
                result.append(Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_1:
                axis = Vector3f.UnitX;
                angle = angle + 1;
                break;
            case KeyEvent.KEYCODE_2:
                axis = Vector3f.UnitY;
                angle = angle + 1;
                break;
            case KeyEvent.KEYCODE_3:
                axis = Vector3f.UnitZ;
                angle = angle + 1;
                break;
            case KeyEvent.KEYCODE_P:
                newPerspectiveAngle = perspectiveAngle + 5f;
                if (newPerspectiveAngle > 120f) {
                    newPerspectiveAngle = 30f;
                }
                break;
            case KeyEvent.KEYCODE_A:
                renderWithString = false;
                current_mesh = g_pCylinderMesh;
                break;
            case KeyEvent.KEYCODE_B:
                renderWithString = false;
                current_mesh = g_pCubeColorMesh;
                break;
            case KeyEvent.KEYCODE_C:
                renderWithString = false;
                current_mesh = g_pPlaneMesh;
                break;
            case KeyEvent.KEYCODE_D:
                renderWithString = false;
                current_mesh = g_pInfinityMesh;
                break;
            case KeyEvent.KEYCODE_E:
                renderWithString = false;
                current_mesh = g_unitSphereMesh;
                break;
            case KeyEvent.KEYCODE_F:
                renderWithString = true;
                renderString = "flat";
                current_mesh = g_unitSphereMesh;
                break;
            case KeyEvent.KEYCODE_I:
                result.append("I Decrease g_camTarget.X");
                Camera.MoveTarget(-4.0f, 0, 0);
                break;
            case KeyEvent.KEYCODE_M:
                result.append("M Increase g_camTarget.X");
                Camera.MoveTarget(4.0f, 0, 0);
                break;
            case KeyEvent.KEYCODE_J:
                result.append("J Increase g_camTarget.Z");
                Camera.MoveTarget(0, 0, 4.0f);
                break;
            case KeyEvent.KEYCODE_K:
                result.append("K Decrease g_camTarget.Z");
                Camera.MoveTarget(0, 0, -4.0f);
                break;
            case KeyEvent.KEYCODE_W:
                currentProgram = ObjectColor;
                reshape();
                break;
            case KeyEvent.KEYCODE_X:
                noWorldMatrix = true;
                currentProgram = g_Unlit;
                reshape();
                break;
            case KeyEvent.KEYCODE_Y:
                noWorldMatrix = true;
                currentProgram = g_litShaderProg;
                reshape();
                break;
            case KeyEvent.KEYCODE_Q:
                result.append("currentProgram = " + currentProgram.toString());
                break;
        }
        reshape();
        return result.toString();
    }
}