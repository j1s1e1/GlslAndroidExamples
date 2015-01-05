package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
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

import java.util.ArrayList;

/**
 * Created by jamie on 1/2/15.
 */
public class Tut_InfinityTest extends TutorialBase {
    boolean updateCull = false;
    boolean updateDepth = false;
    boolean updateAlpha = false;
    boolean updateCcw = false;
    boolean updateBlend = false;
    boolean blend = false;
    boolean cull = false;
    boolean depth = false;
    boolean alpha = false;
    boolean ccw = false;
    boolean updateCullFace = false;
    int cullFaceSelection = 0;
    boolean limitTriangles = false;
    int triangleCount = 0;

    static int NUMBER_OF_LIGHTS = 2;
    Vector3f translate = new Vector3f(0.5f, 0.5f, 0f);
    Vector3f scale = new Vector3f(80f, 80f, 80f);
    Vector3f rotate = new Vector3f();

    boolean rotateNotTranslate = false;

    boolean renderWithString = false;
    String renderString = "";
    class ProgramData
    {
        public int theProgram;
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

        public int[] attribLocations = new int[]{ -1, -1, -1};

        public LightBlock lightBlock;
        public MaterialBlock materialBlock;

        // TEST		
        public String ToString()
    {
        StringBuilder result = new StringBuilder();
        result.append("theProgram = " + String.valueOf(theProgram));
        result.append("positionAttribute = " + String.valueOf(positionAttribute));
        result.append("colorAttribute = " + String.valueOf(colorAttribute));
        result.append("modelToCameraMatrixUnif = " + String.valueOf(modelToCameraMatrixUnif));
        result.append("modelToWorldMatrixUnif = " + String.valueOf(modelToWorldMatrixUnif));
        result.append("worldToCameraMatrixUnif = " + String.valueOf(worldToCameraMatrixUnif));
        result.append("cameraToClipMatrixUnif = " + String.valueOf(cameraToClipMatrixUnif));
        result.append("baseColorUnif = " + String.valueOf(baseColorUnif));
        result.append("normalModelToCameraMatrixUnif = " + String.valueOf(normalModelToCameraMatrixUnif));
        result.append("dirToLightUnif = " + String.valueOf(dirToLightUnif));
        result.append("lightIntensityUnif = " + String.valueOf(lightIntensityUnif));
        result.append("ambientIntensityUnif = " + String.valueOf(ambientIntensityUnif));
        result.append("normalAttribute = " + String.valueOf(normalAttribute));
        return result.toString();
    }
    };

    static float g_fzNear = 10.0f;
    static float g_fzFar = 1000.0f;

    static ProgramData White;
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

    ProgramData LoadProgram(String strVertexShader, String strFragmentShader)
    {
        ProgramData data = new ProgramData();
        int vertex_shader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, strVertexShader);
        int fragment_shader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, strFragmentShader);
        data.theProgram  = Shader.createAndLinkProgram(vertex_shader, fragment_shader);

        data.attribLocations[0] = GLES20.glGetAttribLocation(data.theProgram, "position");
        data.attribLocations[1] = GLES20.glGetAttribLocation(data.theProgram, "color");

        data.modelToWorldMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToWorldMatrix");
        data.worldToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "worldToCameraMatrix");
        data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");
        if (data.cameraToClipMatrixUnif == -1)
        {
            data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "Projection.cameraToClipMatrix");
        }
        data.baseColorUnif = GLES20.glGetUniformLocation(data.theProgram, "baseColor");
        if (data.baseColorUnif == -1)
        {
            data.baseColorUnif = GLES20.glGetUniformLocation(data.theProgram, "objectColor");
        }

        data.modelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToCameraMatrix");

        data.normalModelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "normalModelToCameraMatrix");
        data.dirToLightUnif =  GLES20.glGetUniformLocation(data.theProgram, "dirToLight");
        data.lightIntensityUnif = GLES20.glGetUniformLocation(data.theProgram, "lightIntensity");
        data.ambientIntensityUnif = GLES20.glGetUniformLocation(data.theProgram, "ambientIntensity");
        data.attribLocations[2] = GLES20.glGetAttribLocation(data.theProgram, "normal");

        return data;
    }

    void InitializeProgram()
    {
        UniformColor = LoadProgram(VertexShaders.PosOnlyWorldTransform_vert, FragmentShaders.ColorUniform_frag);
        GLES20.glUseProgram(UniformColor.theProgram);
        GLES20.glUniform4f(UniformColor.baseColorUnif, 0.694f, 0.4f, 0.106f, 1.0f);
        GLES20.glUseProgram(0);

        White = LoadProgram(VertexShaders.noMatrix, FragmentShaders.white);

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
        Vector4f  ambient_intensity = new Vector4f(0.3f, 0.0f, 0.3f, 0.6f);
        GLES20.glUniform3fv(g_WhiteAmbDiffuseColor.dirToLightUnif, 1, light_direction.toArray(), 0);
        GLES20.glUniform4fv(g_WhiteAmbDiffuseColor.lightIntensityUnif, 1,  light_intensity.toArray(), 0);
        GLES20.glUniform4fv(g_WhiteAmbDiffuseColor.ambientIntensityUnif, 1,  ambient_intensity.toArray(), 0);
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
        g_litShaderProg.lightBlock = new LightBlock(NUMBER_OF_LIGHTS);
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

        ObjectColor = LoadProgram(VertexShaders.PosColorWorldTransform_vert3 , FragmentShaders.ColorPassthrough_frag);
        currentProgram = ObjectColor;
    }

    int currentMesh = 0;
    ArrayList<Mesh> meshes = new ArrayList<Mesh>();

    protected void init() throws Exception
    {
        InitializeProgram();

        try
        {
            meshes.add(new Mesh("infinity.xml"));
        } catch (Exception ex) {
            throw new Exception("Error " + ex.toString());
        }

        setupDepthAndCull();
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        Camera.Move(0f, 0f, 0f);
        Camera.MoveTarget(0f, 0f, 0.0f);
        reshape();
    }

    public void display() throws Exception
    {
        clearDisplay();

        if (meshes.get(currentMesh) != null)
        {
            MatrixStack modelMatrix = new MatrixStack();
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.RotateX(rotate.x);
                modelMatrix.RotateY(rotate.y);
                modelMatrix.RotateZ(rotate.z);
                modelMatrix.Scale(scale);
                modelMatrix.Translate(translate);

                GLES20.glUseProgram(currentProgram.theProgram);
                Matrix4f mm = modelMatrix.Top();

                if (noWorldMatrix)
                {
                    Matrix4f cm2 = Matrix4f.Mult(mm, cm);
                    GLES20.glUniformMatrix4fv(currentProgram.modelToCameraMatrixUnif, 1, false, cm2.toArray(), 0);
                    if (currentProgram.normalModelToCameraMatrixUnif != 0)
                    {
                        Matrix3f normalModelToCameraMatrix = Matrix3f.Identity();
                        Matrix4f applyMatrix = Matrix4f.Mult(Matrix4f.Identity(),
                                Matrix4f.CreateTranslation(dirToLight));
                        normalModelToCameraMatrix = new Matrix3f(applyMatrix);
                        normalModelToCameraMatrix.invert();
                        GLES20.glUniformMatrix3fv(currentProgram.normalModelToCameraMatrixUnif, 1, false,
                                normalModelToCameraMatrix.toArray(), 0);
                        //Matrix4f cameraToClipMatrix = Matrix4f.Identity;
                        //GLES20.glUniformMatrix4f(currentProgram.cameraToClipMatrixUnif, false, ref cameraToClipMatrix); 

                    }
                    //Matrix4f cameraToClipMatrix = Matrix4f.Identity;
                    //GLES20.glUniformMatrix4f(currentProgram.cameraToClipMatrixUnif, false, ref cameraToClipMatrix); 
                }
                else
                {
                    GLES20.glUniformMatrix4fv(currentProgram.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
                }
            }
            if (renderWithString)
            {
                meshes.get(currentMesh).render(currentProgram.attribLocations, renderString);
            }
            else
            {
                if (limitTriangles)
                {
                    meshes.get(currentMesh).render(currentProgram.attribLocations, triangleCount);
                }
                else
                {
                    meshes.get(currentMesh).render(currentProgram.attribLocations);
                }
            }
            GLES20.glUseProgram(0);
            if (perspectiveAngle != newPerspectiveAngle)
            {
                perspectiveAngle = newPerspectiveAngle;
                reshape();
            }
        }
        updateDisplayOptions();
    }

    void updateDisplayOptions() {
        if (updateAlpha) {
            updateAlpha = false;
            if (alpha) {
                alpha = false;
                GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
                GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
                Log.i("KeyEvent", "alpha disabled");
            } else {
                alpha = true;
                GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                Log.i("KeyEvent", "alpha enabled");
            }
        }
        if (updateBlend) {
            updateBlend = false;
            if (blend) {
                blend = false;
                GLES20.glDisable(GLES20.GL_BLEND);
                Log.i("KeyEvent", "blend disabled");
            } else {
                blend = true;
                GLES20.glEnable(GLES20.GL_BLEND);
                Log.i("KeyEvent", "blend enabled");
            }
        }
        if (updateCull) {
            updateCull = false;
            if (cull) {
                cull = false;
                GLES20.glDisable(GLES20.GL_CULL_FACE);
                Log.i("KeyEvent", "cull disabled");
            } else {
                cull = true;
                GLES20.glEnable(GLES20.GL_CULL_FACE);
                Log.i("KeyEvent", "cull enabled");
            }
        }
        if (updateDepth)
        {
            updateDepth = false;
            if (depth)
            {
                depth = false;
                GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                GLES20.glDepthMask(false);
                Log.i("KeyEvent", "depth disabled");
            }
            else
            {
                depth = true;
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                GLES20.glDepthMask(true);
                Log.i("KeyEvent", "depth enabled");
            }
        }
        if (updateCullFace)
        {
            updateCullFace = false;
            switch (cullFaceSelection) {
                case 0:
                    GLES20.glCullFace(GLES20.GL_FRONT_AND_BACK);
                    Log.i("KeyEvent", "cull face GL_FRONT_AND_BACK");
                    break;
                case 1:
                    GLES20.glCullFace(GLES20.GL_FRONT);
                    Log.i("KeyEvent", "cull face GL_FRONT");
                    break;
                case 2:
                    GLES20.glCullFace(GLES20.GL_BACK);
                    Log.i("KeyEvent", "cull face GL_BACK");
                    break;
            }
            cullFaceSelection++;
            if (cullFaceSelection > 2) cullFaceSelection = 0;
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
        persMatrix.Perspective(perspectiveAngle, (width / (float)height), g_fzNear, g_fzFar);
        pm = persMatrix.Top();

        SetGlobalMatrices(currentProgram);

        GLES20.glViewport(0, 0, width, height);

    }

    static boolean noWorldMatrix = false;

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        switch (keyCode) {
            case KeyEvent.KEYCODE_NUMPAD_6:
                translate.x += 0.5f;
                break;
            case KeyEvent.KEYCODE_NUMPAD_4:
                translate.x -= 0.5f;
                break;
            case KeyEvent.KEYCODE_NUMPAD_8:
                translate.y += 0.5f;
                break;
            case KeyEvent.KEYCODE_NUMPAD_2:
                translate.y -= 0.5f;
                break;
            case KeyEvent.KEYCODE_NUMPAD_7:
                translate.z += 0.5f;
                break;
            case KeyEvent.KEYCODE_NUMPAD_3:
                translate.z -= 0.5f;
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
            case KeyEvent.KEYCODE_4:
                break;
            case KeyEvent.KEYCODE_5:
                break;
            case KeyEvent.KEYCODE_6:
                break;
            case KeyEvent.KEYCODE_P:
                newPerspectiveAngle = perspectiveAngle + 5f;
                if (newPerspectiveAngle > 120f)
                {
                    newPerspectiveAngle = 30f;
                }
                break;
            case KeyEvent.KEYCODE_H:
                Log.i("KeyEvent", "I Decrease g_camTarget.x");
                Camera.MoveTarget(-4.0f, 0, 0);
                break;
            case KeyEvent.KEYCODE_I:
                Log.i("KeyEvent", "M Increase g_camTarget.x");
                Camera.MoveTarget(4.0f, 0, 0);
                break;
            case KeyEvent.KEYCODE_J:
                Log.i("KeyEvent", "J Increase g_camTarget.z");
                Camera.MoveTarget(0, 0, 4.0f);
                break;
            case KeyEvent.KEYCODE_K:
                Log.i("KeyEvent", "K Decrease g_camTarget.z");
                Camera.MoveTarget(0, 0, -4.0f);
                break;
            case KeyEvent.KEYCODE_ESCAPE:
                //timer.Enabled = false;
                break;
            case KeyEvent.KEYCODE_SPACE:
                break;
            case KeyEvent.KEYCODE_U:
                currentProgram = ObjectColor;
                reshape();
                break;
            case KeyEvent.KEYCODE_V:
                noWorldMatrix = true;
                currentProgram = g_Unlit;
                reshape();
                break;
            case KeyEvent.KEYCODE_Q:
                result.append("currentProgram = " + currentProgram.ToString());
                break;

            case KeyEvent.KEYCODE_S:
                scale = Vector3f.multiply(scale, 1.1f);
                if (scale.x > 100f) scale = new Vector3f(5f, 5f, 5f);
                break;
            case KeyEvent.KEYCODE_X:
                rotate.x += 10f;
                if (rotate.x > 360f) rotate.x  = 0f;
                break;
            case KeyEvent.KEYCODE_Y:
                rotate.y += 10f;
                if (rotate.y > 360f) rotate.y  = 0f;
                break;
            case KeyEvent.KEYCODE_Z:
                rotate.z += 10f;
                if (rotate.z > 360f) rotate.z = 0f;
                break;
            case KeyEvent.KEYCODE_R:
                rotateNotTranslate = true;
                break;
            case KeyEvent.KEYCODE_T:
                rotateNotTranslate = false;
                break;
            case KeyEvent.KEYCODE_M:
                renderWithString = false;
                currentMesh++;
                if (currentMesh > meshes.size() - 1) currentMesh = 0;
                Log.i("KeyEvent","Mesh = " + meshes.get(currentMesh).fileName);
                break;
            case KeyEvent.KEYCODE_A:
                updateAlpha = true;
                break;
            case KeyEvent.KEYCODE_B:
                updateBlend = true;
                break;
            case KeyEvent.KEYCODE_C:
                updateCull = true;
                break;
            case KeyEvent.KEYCODE_D:
                updateDepth = true;
                break;
            case KeyEvent.KEYCODE_N:
                limitTriangles = true;
                triangleCount += 3;
                Log.i("KeyEvent", "Triangle Count = " + String.valueOf(triangleCount));
                break;
            case KeyEvent.KEYCODE_O:
                if (triangleCount > 0)
                {
                    triangleCount -= 3;
                }
                Log.i("KeyEvent", "Triangle Count = " +  String.valueOf(triangleCount));
                limitTriangles = false;
                break;
            case KeyEvent.KEYCODE_W:
                updateCullFace = true;
                break;
        }

        reshape();
        return result.toString();
    }
}
