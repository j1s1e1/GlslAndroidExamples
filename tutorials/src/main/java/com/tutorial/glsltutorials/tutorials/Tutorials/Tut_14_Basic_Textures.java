package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VBO_Tools;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Light.FrameworkTimer;
import com.tutorial.glsltutorials.tutorials.Light.LightBlock;
import com.tutorial.glsltutorials.tutorials.Material.MaterialBlock;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.ProjectionBlock;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.View.Framework;
import com.tutorial.glsltutorials.tutorials.View.MouseButtons;
import com.tutorial.glsltutorials.tutorials.View.ObjectData;
import com.tutorial.glsltutorials.tutorials.View.ObjectPole;
import com.tutorial.glsltutorials.tutorials.View.ViewData;
import com.tutorial.glsltutorials.tutorials.View.ViewPole;
import com.tutorial.glsltutorials.tutorials.View.ViewScale;

import java.nio.ByteBuffer;

/**
 * Created by jamie on 11/15/14.
 */
public class Tut_14_Basic_Textures extends TutorialBase {

    /****** Strange example of 1D texture not really supported in GLES20 **********/

    class ProgramData
    {
        public int theProgram;
        public int modelToCameraMatrixUnif;
        public int normalModelToCameraMatrixUnif;
        public int cameraToClipMatrixUnif;
        public LightBlock lightBlock;
        public MaterialBlock materialBlock;
    };

    class UnlitProgData
    {
        public int theProgram;
        public int objectColorUnif;
        public int modelToCameraMatrixUnif;
        public int cameraToClipMatrixUnif;
    };

    static float g_fzNear = 1.0f;
    static float g_fzFar = 1000.0f;

    static ProgramData g_litShaderProg;
    static ProgramData g_litTextureProg;

    static UnlitProgData g_Unlit;

    static int g_gaussTexUnit = 0;

    static int NUMBER_OF_LIGHTS = 2;

    UnlitProgData LoadUnlitProgram(String strVertexShader, String strFragmentShader)
    {
        UnlitProgData data = new UnlitProgData();
        int vertex_shader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, strVertexShader);
        int fragment_shader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, strFragmentShader);
        data.theProgram  = Shader.createAndLinkProgram(vertex_shader, fragment_shader);

        data.modelToCameraMatrixUnif =  GLES20.glGetUniformLocation(data.theProgram, "modelToCameraMatrix");
        data.objectColorUnif =  GLES20.glGetUniformLocation(data.theProgram, "baseColor");

        data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");
        return data;
    }

    ProgramData LoadStandardProgram(String strVertexShader, String strFragmentShader)
    {
        ProgramData data = new ProgramData();
        int vertex_shader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, strVertexShader);
        int fragment_shader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, strFragmentShader);
        data.theProgram  = Shader.createAndLinkProgram(vertex_shader, fragment_shader);

        data.modelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToCameraMatrix");
        data.normalModelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "normalModelToCameraMatrix");

        data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");

        // Replace uniform buffers
        data.lightBlock = new LightBlock(NUMBER_OF_LIGHTS);
        data.lightBlock.setUniforms(data.theProgram);

        data.materialBlock = new MaterialBlock();
        data.materialBlock.setUniforms(data.theProgram);

        int gaussianTextureUnif = GLES20.glGetUniformLocation(data.theProgram, "gaussianTexture");
        GLES20.glUseProgram(data.theProgram);
        GLES20.glUniform1f(gaussianTextureUnif, g_gaussTexUnit);
        GLES20.glUseProgram(0);

        return data;
    }

    void InitializePrograms()
    {
        Shader.compileShader(GLES20.GL_VERTEX_SHADER, VertexShaders.BasicTexture_PN);
        Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, FragmentShaders.ShaderGaussian);

        g_litShaderProg = LoadStandardProgram(VertexShaders.BasicTexture_PN, FragmentShaders.ShaderGaussian);
        g_litTextureProg = LoadStandardProgram(VertexShaders.BasicTexture_PN, FragmentShaders.TextureGaussian);

        g_Unlit = LoadUnlitProgram(VertexShaders.PosTransform, FragmentShaders.ColorUniform_frag);
    }

    public static ObjectData g_initialObjectData = new ObjectData(new Vector3f(0.0f, 0.5f, 0.0f),
            new Quaternion(0.0f, 0.0f, 0.0f, 1.0f));

    static ViewData g_initialViewData;

    private static void InitializeGInitialViewData()
    {
        g_initialViewData = new ViewData(g_initialObjectData.position,
                new Quaternion(0.3826834f, 0.0f, 0.0f, 0.92387953f),
                10.0f,
                0.0f);
    }

    static ViewScale g_viewScale;

    private static void InitializeGViewScale()
    {
        g_viewScale = new ViewScale(
                1.5f, 70.0f,
                1.5f, 0.5f,
                0.0f, 0.0f,		//No camera movement.
                90.0f/250.0f);
    }

    public static ViewPole g_viewPole;

    public static ObjectPole g_objtPole;

    void mouseMotion(int x, int y)
    {
        Framework.forwardMouseMotion(g_viewPole, x, y);
        Framework.forwardMouseMotion(g_objtPole, x, y);
    }

    void mouseButton(int button, int state, int x, int y)
    {
        Framework.forwardMouseButton(g_viewPole, button, state, x, y);
        Framework.forwardMouseButton(g_objtPole, button, state, x, y);
    }

    void mouseWheel(int wheel, int direction, int x, int y)
    {
        Framework.forwardMouseWheel(g_viewPole, wheel, direction, x, y);
        Framework.forwardMouseWheel(g_objtPole, wheel, direction, x, y);
    }

    static Mesh g_pObjectMesh;
    static Mesh g_pCubeMesh;


    static int NUM_GAUSS_TEXTURES = 4;
    static int[] g_gaussTextures = new int[NUM_GAUSS_TEXTURES];

    float g_specularShininess = 0.2f;

    ByteBuffer BuildGaussianData(int cosAngleResolution)
    {
        byte[] textureData = new byte[cosAngleResolution];

        for(int iCosAng = 0; iCosAng < cosAngleResolution; iCosAng++)
        {
            float cosAng = iCosAng / (float)(cosAngleResolution - 1);
            float angle = (float) Math.acos(cosAng);
            float exponent = angle / g_specularShininess;
            exponent = -(exponent * exponent);
            float gaussianTerm = (float)Math.exp(exponent);

            textureData[iCosAng] = (byte)(gaussianTerm * 255.0f);
        }
        return VBO_Tools.MakeByteBuffer(textureData);
    }

    int CreateGaussianTexture(int cosAngleResolution)
    {
        ByteBuffer textureData = BuildGaussianData(cosAngleResolution);

        int[] gaussTexture = new int[1];
        int height = 1;

        GLES20.glGenTextures(1, gaussTexture, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, gaussTexture[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, cosAngleResolution, height, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, textureData);
        // FIXME GLES20.glTexParameteri(GLES20.GL_TEXTURE, GLES20 TextureParameterName.TextureBaseLevel, 0);
        // FIXME GLES20.glTexParameteri(GLES20.GL_TEXTURE,TextureParameterName.TextureMaxLevel, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return gaussTexture[0];
    }

    int CalcCosAngResolution(int level)
    {
        int cosAngleStart = 64;
        return cosAngleStart * ((int)Math.pow(2.0f, level));
    }

    void CreateGaussianTextures()
    {
        for(int loop = 0; loop < NUM_GAUSS_TEXTURES; loop++)
        {
            int cosAngleResolution = CalcCosAngResolution(loop);
            g_gaussTextures[loop] = CreateGaussianTexture(cosAngleResolution);
        }

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

    }

    protected void init () throws Exception
    {
        InitializeGInitialViewData();
        InitializeGViewScale();
        g_viewPole = new ViewPole(g_initialViewData, g_viewScale, MouseButtons.MB_LEFT_BTN);
        g_objtPole = new ObjectPole(g_initialObjectData, (float)(90.0f / 250.0f),
                MouseButtons.MB_RIGHT_BTN, g_viewPole);

        InitializePrograms();

        try
        {
            g_pObjectMesh = new Mesh("infinity.xml");
            g_pCubeMesh = new Mesh("unitcube.xml");
        }
        catch(Exception ex)
        {
            throw new Exception("Error " + ex.toString());
        }

        setupDepthAndCull();
        reshape();

        //Setup our Uniform Buffers
        MaterialBlock mtl = new MaterialBlock();
        mtl.diffuseColor = new Vector4f(1.0f, 0.673f, 0.043f, 1.0f);
        mtl.specularColor = (new Vector4f(1.0f, 0.673f, 0.043f, 1.0f)).Mult(0.4f);
        mtl.specularShininess = g_specularShininess;

        g_litShaderProg.materialBlock.update(mtl);
        g_litTextureProg.materialBlock.update(mtl);

        CreateGaussianTextures();
    }

    boolean g_bDrawCameraPos = false;
    boolean g_bDrawLights = true;
    boolean g_bUseTexture = false;
    int g_currTexture = 0;

    float g_lightHeight = 1.0f;
    float g_lightRadius = 3.0f;

    FrameworkTimer g_lightTimer = new FrameworkTimer(FrameworkTimer.Type.TT_LOOP, 6.0f);

    Vector4f CalcLightPosition()
    {
        float fLoopDuration = 5.0f;
        float fScale = 3.14159f * 2.0f;

        float fElapsedTime = getElapsedTime() / 1000f;
        float timeThroughLoop = fElapsedTime % fLoopDuration;

        Vector4f ret = new Vector4f(0.0f, g_lightHeight, 0.0f, 1.0f);

        ret.x = (float)Math.cos(timeThroughLoop * fScale) * g_lightRadius;
        ret.z = (float)Math.sin(timeThroughLoop * fScale) * g_lightRadius;

        return ret;
    }

    static float g_fHalfLightDistance = 25.0f;
    static float g_fLightAttenuation = 1.0f / (g_fHalfLightDistance * g_fHalfLightDistance);

    public void display() throws Exception
    {
        g_lightTimer.Update();

        GLES20.glClearColor(0.75f, 0.75f, 1.0f, 1.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if((g_pObjectMesh != null) && (g_pCubeMesh != null))
        {
            MatrixStack modelMatrix = new MatrixStack();
            modelMatrix.SetMatrix(g_viewPole.CalcMatrix());
            Matrix4f worldToCamMat = modelMatrix.Top();

            LightBlock lightData = new LightBlock(2);

            lightData.ambientIntensity = new Vector4f(0.2f, 0.2f, 0.2f, 1.0f);
            lightData.lightAttenuation = g_fLightAttenuation;

            Vector3f globalLightDirection = new Vector3f(0.707f, 0.707f, 0.0f);

            lightData.lights[0].cameraSpaceLightPos =
                    Vector4f.Transform(new Vector4f(globalLightDirection, 0.0f), worldToCamMat);
            lightData.lights[0].lightIntensity = new Vector4f(0.6f, 0.6f, 0.6f, 1.0f);

            lightData.lights[1].cameraSpaceLightPos = Vector4f.Transform(CalcLightPosition(), worldToCamMat);
            lightData.lights[1].lightIntensity = new Vector4f(0.4f, 0.4f, 0.4f, 1.0f);

            g_litShaderProg.lightBlock.update(lightData);
            g_litTextureProg.lightBlock.update(lightData);


            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.ApplyMatrix(g_objtPole.CalcMatrix());
                modelMatrix.Scale(2.0f);

                Matrix3f normMatrix = new Matrix3f(modelMatrix.Top());
                //normMatrix = glm::transpose(glm::inverse(normMatrix));
                normMatrix = Matrix3f.Identity();

                ProgramData prog = g_bUseTexture ? g_litTextureProg : g_litShaderProg;

                GLES20.glUseProgram(prog.theProgram);
                Matrix4f mm = modelMatrix.Top();
                GLES20.glUniformMatrix4fv(prog.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                GLES20.glUniformMatrix3fv(prog.normalModelToCameraMatrixUnif, 1, false, normMatrix.toArray(), 0);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + g_gaussTexUnit);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, g_gaussTextures[g_currTexture]);

                g_pObjectMesh.render("lit");

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

                GLES20.glUseProgram(0);
            }

            if(g_bDrawLights)
            {
                try (PushStack pushstack = new PushStack(modelMatrix))
                {
                    modelMatrix.translate(new Vector3f(CalcLightPosition()));
                    modelMatrix.Scale(0.25f);

                    GLES20.glUseProgram(g_Unlit.theProgram);
                    Matrix4f mm = modelMatrix.Top();
                    GLES20.glUniformMatrix4fv(g_Unlit.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);

                    Vector4f lightColor = new Vector4f(1f, 1f, 1f, 1f);
                    GLES20.glUniform4fv(g_Unlit.objectColorUnif, 1, lightColor.toArray(), 0);
                    g_pCubeMesh.render("flat");
                }

                modelMatrix.translate(globalLightDirection.mul(100.0f));
                modelMatrix.Scale(5.0f);

                Matrix4f mm2 = modelMatrix.Top();
                GLES20.glUniformMatrix4fv(g_Unlit.modelToCameraMatrixUnif, 1, false, mm2.toArray(), 0);
                g_pCubeMesh.render("flat");

                GLES20.glUseProgram(0);
            }

            if(g_bDrawCameraPos)
            {
                try (PushStack pushstack = new PushStack(modelMatrix))
                {
                    modelMatrix.SetIdentity();
                    modelMatrix.translate(new Vector3f(0.0f, 0.0f, -g_viewPole.GetView().radius));
                    modelMatrix.Scale(0.25f);

                    GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                    GLES20.glDepthMask(false);
                    GLES20.glUseProgram(g_Unlit.theProgram);
                    Matrix4f mm = modelMatrix.Top();
                    GLES20.glUniformMatrix4fv(g_Unlit.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                    GLES20.glUniform4f(g_Unlit.objectColorUnif, 0.25f, 0.25f, 0.25f, 1.0f);
                    g_pCubeMesh.render("flat");
                    GLES20.glDepthMask(true);
                    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                    GLES20.glUniform4f(g_Unlit.objectColorUnif, 1.0f, 1.0f, 1.0f, 1.0f);
                    g_pCubeMesh.render("flat");
                }
            }
        }
    }

    void reshape (int w, int h)
    {
        MatrixStack persMatrix = new MatrixStack();
        persMatrix.perspective(45.0f, (w / (float) h), g_fzNear, g_fzFar);

        ProjectionBlock projData = new ProjectionBlock();
        projData.cameraToClipMatrix = persMatrix.Top();

        Matrix4f cm = projData.cameraToClipMatrix;
        GLES20.glUseProgram(g_litShaderProg.theProgram);
        GLES20.glUniformMatrix4fv(g_litShaderProg.cameraToClipMatrixUnif, 1, false, cm.toArray(), 0);
        GLES20.glUseProgram(0);
        GLES20.glUseProgram(g_litTextureProg.theProgram);
        GLES20.glUniformMatrix4fv(g_litTextureProg.cameraToClipMatrixUnif, 1, false, cm.toArray(), 0);
        GLES20.glUseProgram(0);
        GLES20.glUseProgram(g_Unlit.theProgram);
        GLES20.glUniformMatrix4fv(g_Unlit.cameraToClipMatrixUnif, 1, false,  cm.toArray(), 0);
        GLES20.glUseProgram(0);

        GLES20.glViewport(0, 0, w, h);
    }


    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_ESCAPE:
                g_pObjectMesh = null;
                g_pCubeMesh = null;
                break;

            case KeyEvent.KEYCODE_P: g_lightTimer.TogglePause(); break;
            //case Keys.Subtract: g_lightTimer.Rewind(0.5f); break;
            //case Keys.Add: g_lightTimer.Fastforward(0.5f); break;
            case KeyEvent.KEYCODE_T: g_bDrawCameraPos = !g_bDrawCameraPos; break;
            case KeyEvent.KEYCODE_G: g_bDrawLights = !g_bDrawLights; break;
            case KeyEvent.KEYCODE_SPACE:
                g_bUseTexture = !g_bUseTexture;
                if(g_bUseTexture)
                    Log.i("KeyEvent", "Texture\n");
                else
                    Log.i("KeyEvent", "Shader\n");
                break;
            case KeyEvent.KEYCODE_1: g_currTexture = 0; break;
            case KeyEvent.KEYCODE_2: g_currTexture = 1; break;
            case KeyEvent.KEYCODE_3: g_currTexture = 2; break;
            case KeyEvent.KEYCODE_4: g_currTexture = 3; break;
            case KeyEvent.KEYCODE_Q:
                mouseWheel(1, 0, 10, 10);
                break;
            case KeyEvent.KEYCODE_R:
                mouseWheel(1, 1, 10, 10);
                break;
        }
        result.append("Angle Resolution:  " + String.valueOf(CalcCosAngResolution(g_currTexture)));
        return result.toString();
    }
}
