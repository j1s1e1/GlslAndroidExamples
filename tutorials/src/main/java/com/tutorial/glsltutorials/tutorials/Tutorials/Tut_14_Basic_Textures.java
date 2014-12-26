package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLES30;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Framework;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Light.LightBlock;
import com.tutorial.glsltutorials.tutorials.Material.MaterialBlock;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.MouseButtons;
import com.tutorial.glsltutorials.tutorials.ObjectData;
import com.tutorial.glsltutorials.tutorials.ObjectPole;
import com.tutorial.glsltutorials.tutorials.ProjectionBlock;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.ViewData;
import com.tutorial.glsltutorials.tutorials.ViewProvider;
import com.tutorial.glsltutorials.tutorials.ViewScale;

import java.io.InputStream;

/**
 * Created by jamie on 11/15/14.
 */
public class Tut_14_Basic_Textures extends TutorialBase {
    public Tut_14_Basic_Textures ()
    {
    }

    class ProgramData
    {
        public int theProgram;
        public int modelToCameraMatrixUnif;
        public int normalModelToCameraMatrixUnif;
        public int cameraToClipMatrixUnif;
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

    static int g_materialBlockIndex = 0;
    static int g_lightBlockIndex = 1;
    static int g_projectionBlockIndex = 2;

    static int g_gaussTexUnit = 0;

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
        int vertex_shader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, strVertexShader);
        int fragment_shader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, strFragmentShader);
        data.theProgram  = Shader.createAndLinkProgram(vertex_shader, fragment_shader);

        data.modelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToCameraMatrix");
        data.normalModelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "normalModelToCameraMatrix");

        int materialBlock = GLES30.glGetUniformBlockIndex(data.theProgram, "Material");
        int lightBlock = GLES30.glGetUniformBlockIndex(data.theProgram, "Light");

        data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");

        GLES30.glUniformBlockBinding(data.theProgram, materialBlock, g_materialBlockIndex);
        GLES30.glUniformBlockBinding(data.theProgram, lightBlock, g_lightBlockIndex);

        int gaussianTextureUnif = GLES20.glGetUniformLocation(data.theProgram, "gaussianTexture");
        GLES20.glUseProgram(data.theProgram);
        GLES20.glUniform1f(gaussianTextureUnif, g_gaussTexUnit);
        GLES20.glUseProgram(0);

        return data;
    }

    static void InitializePrograms()
    {
        /* FIXME
        Shader.compileShader(GLES20.GL_VERTEX_SHADER, VertexShaders.BasicTexture_PN);
        Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, FragmentShaders.ShaderGaussian);

        g_litShaderProg = LoadStandardProgram(VertexShaders.BasicTexture_PN, FragmentShaders.ShaderGaussian);
        g_litTextureProg = LoadStandardProgram(VertexShaders.BasicTexture_PN, FragmentShaders.TextureGaussian);

        g_Unlit = LoadUnlitProgram(VertexShaders.PosTransform, FragmentShaders.ColorUniform_frag);
        */
    }

    public static ObjectData g_initialObjectData = new ObjectData(new Vector3f(0.0f, 0.5f, 0.0f),
            new Quaternion(1.0f, 0.0f, 0.0f, 0.0f));

    static ViewData g_initialViewData;

    private static void InitializeGInitialViewData()
    {
        g_initialViewData = new ViewData(g_initialObjectData.position,
                new Quaternion(0.92387953f, 0.3826834f, 0.0f, 0.0f),
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

    public static ViewProvider g_viewPole;

    public static ObjectPole g_objtPole;

    void MouseMotion(int x, int y)
    {
        Framework.ForwardMouseMotion(g_viewPole, x, y);
        Framework.ForwardMouseMotion(g_objtPole, x, y);
    }

    void MouseButton(int button, int state, int x, int y)
    {
        Framework.ForwardMouseButton(g_viewPole, button, state, x, y);
        Framework.ForwardMouseButton(g_objtPole, button, state, x, y);
    }

    void MouseWheel(int wheel, int direction, int x, int y)
    {
        Framework.ForwardMouseWheel(g_viewPole, wheel, direction, x, y);
        Framework.ForwardMouseWheel(g_objtPole, wheel, direction, x, y);
    }

    static ProjectionBlock projData = new ProjectionBlock();

    static Mesh g_pObjectMesh;
    static Mesh g_pCubeMesh;

    static int g_lightUniformBuffer = 0;
    static int g_projectionUniformBuffer = 0;
    static int g_materialUniformBuffer = 0;

    static int NUM_GAUSS_TEXTURES = 4;
    static int[] g_gaussTextures = new int[NUM_GAUSS_TEXTURES];

    static int g_gaussSampler = 0;

    static int g_imposterVAO;
    static int g_imposterVBO;

    float g_specularShininess = 0.2f;

    void BuildGaussianData(byte[] textureData, int cosAngleResolution)
    {
        textureData = new byte[cosAngleResolution];

        for(int iCosAng = 0; iCosAng < cosAngleResolution; iCosAng++)
        {
            float cosAng = iCosAng / (float)(cosAngleResolution - 1);
            float angle = (float) Math.acos(cosAng);
            float exponent = angle / g_specularShininess;
            exponent = -(exponent * exponent);
            float gaussianTerm = (float)Math.exp(exponent);

            textureData[iCosAng] = (byte)(gaussianTerm * 255.0f);
        }
    }

    int CreateGaussianTexture(int cosAngleResolution)
    {
        byte[] textureData = new byte[1];
        BuildGaussianData(textureData, cosAngleResolution);

        int gaussTexture = 0;
        /* FIXME
        GLES20.glGenTextures(1, gaussTexture);
        GLES20.glBindTexture(GLES20.GL_TEXTURE, gaussTexture);
        GLES20.glTexImage1D(GLES20.GL_TEXTURE, 0, PixelInternalFormat.R8, cosAngleResolution, 0, PixelFormat.Red, PixelType.Byte, textureData);
        GLES20.glTexParameter(GLES20.GL_TEXTURE, TextureParameterName.TextureBaseLevel, 0);
        GLES20.glTexParameter(GLES20.GL_TEXTURE,TextureParameterName.TextureMaxLevel, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE, 0);
        */

        return gaussTexture;
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

        /* FIXME
        GLES30.glGenSamplers(1, g_gaussSampler);
        GLES30.glSamplerParameterf(g_gaussSampler, SamplerParameterName.TextureMagFilter,  (int)TextureMagFilter.Nearest);
        GLES30.glSamplerParameterf(g_gaussSampler, SamplerParameterName.TextureMinFilter,  (int)TextureMinFilter.Nearest);
        GLES30.glSamplerParameterf(g_gaussSampler, SamplerParameterName.TextureWrapT, (int)TextureWrapMode.ClampToEdge);
        */
    }


    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init () throws Exception
    {
        InitializeGInitialViewData();
        InitializeGViewScale();
        g_viewPole = new ViewProvider(g_initialViewData, g_viewScale, MouseButtons.MB_LEFT_BTN);
        g_objtPole = new ObjectPole(g_initialObjectData, (float)(90.0f / 250.0f),
                MouseButtons.MB_RIGHT_BTN, g_viewPole);



        InitializePrograms();

        try
        {
            /* FIXME
            InputStream Infinity = Shader.context.getResources().openRawResource(R.raw.infinity);
            g_pObjectMesh = new Mesh(Infinity);
            InputStream UnitCube = Shader.context.getResources().openRawResource(R.raw.unitcube);
            g_pCubeMesh = new Mesh(UnitCube);
            */
        }
        catch(Exception ex)
        {
            throw new Exception("Error " + ex.toString());
        }

        //glutMouseFunc(MouseButton);
        //glutMotionFunc(MouseMotion);
        //glutMouseWheelFunc(MouseWheel);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CW);

        float depthZNear = 0.0f;
        float depthZFar = 1.0f;


        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthMask(true);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthRangef(depthZNear, depthZFar);
        reshape();

        //Setup our Uniform Buffers
        MaterialBlock mtl = new MaterialBlock();
        mtl.diffuseColor = new Vector4f(1.0f, 0.673f, 0.043f, 1.0f);
        mtl.specularColor = (new Vector4f(1.0f, 0.673f, 0.043f, 1.0f)).Mult(0.4f);
        mtl.specularShininess = g_specularShininess;

        /* FIXME
        GLES20.glGenBuffers(1, g_materialUniformBuffer);
        GLES20.glBindBuffer(BufferTarget.UniformBuffer, g_materialUniformBuffer);
        GLES20.glBufferData(BufferTarget.UniformBuffer, (IntPtr)MaterialBlock.Size(), mtl.ToFloat(), BufferUsageHint.StaticDraw);

        GLES20.glGenBuffers(1, out g_lightUniformBuffer);
        GLES20.glBindBuffer(BufferTarget.UniformBuffer, g_lightUniformBuffer);
        GLES20.glBufferData(BufferTarget.UniformBuffer, (IntPtr)LightBlock.Size(), IntPtr.zero, BufferUsageHint.DynamicDraw);

        GLES20.glGenBuffers(1, out g_projectionUniformBuffer);
        GLES20.glBindBuffer(BufferTarget.UniformBuffer, g_projectionUniformBuffer);
        GLES20.glBufferData(BufferTarget.UniformBuffer, (IntPtr)ProjectionBlock.byteLength(), IntPtr.zero, BufferUsageHint.DynamicDraw);

        //Bind the static buffers.
        GLES20.glBindBufferRange(BufferTarget.UniformBuffer, g_lightBlockIndex, g_lightUniformBuffer, IntPtr.zero, (IntPtr)LightBlock.Size());

        GLES20.glBindBufferRange(BufferTarget.UniformBuffer, g_projectionBlockIndex, g_projectionUniformBuffer, IntPtr.zero, (IntPtr)ProjectionBlock.byteLength());

        GLES20.glBindBufferRange(BufferTarget.UniformBuffer, g_materialBlockIndex, g_materialUniformBuffer, IntPtr.zero, (IntPtr)MaterialBlock.Size());

        GLES20.glBindBuffer(BufferTarget.UniformBuffer, 0);
        */
        CreateGaussianTextures();
    }

    boolean g_bDrawCameraPos = false;
    boolean g_bDrawLights = true;
    boolean g_bUseTexture = false;
    int g_currTexture = 0;

    float g_lightHeight = 1.0f;
    float g_lightRadius = 3.0f;

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
        //g_lightTimer.Update();

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

            GLES20.glBindBuffer(GLES30.GL_UNIFORM_BUFFER, g_lightUniformBuffer);
            // FIXME GLES20.glBufferSubData(GLES30.GL_UNIFORM_BUFFER, 0, LightBlock.Size(), lightData.ToFloat());
            GLES20.glBindBuffer(GLES30.GL_UNIFORM_BUFFER, 0);

            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                GLES30.glBindBufferRange(GLES30.GL_UNIFORM_BUFFER, g_materialBlockIndex, g_materialUniformBuffer,
                        0, MaterialBlock.Size());

                modelMatrix.ApplyMatrix(g_objtPole.CalcMatrix());
                modelMatrix.Scale(2.0f);

                Matrix3f normMatrix = new Matrix3f(modelMatrix.Top());
                //normMatrix = glm::transpose(glm::inverse(normMatrix));

                ProgramData prog = g_bUseTexture ? g_litTextureProg : g_litShaderProg;

                GLES20.glUseProgram(prog.theProgram);
                Matrix4f mm = modelMatrix.Top();
                GLES20.glUniformMatrix4fv(prog.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                GLES20.glUniformMatrix3fv(prog.normalModelToCameraMatrixUnif, 1, false, normMatrix.toArray(), 0);

                /* FIXME
                GLES20.glActiveTexture(TextureUnit.Texture0 + g_gaussTexUnit);
                GLES20.glBindTexture(TextureTarget.Texture1D, g_gaussTextures[g_currTexture]);
                GLES20.glBindSampler(g_gaussTexUnit, g_gaussSampler);

                g_pObjectMesh.Render("lit");

                GLES30.glBindSampler(g_gaussTexUnit, 0);
                GLES20.glBindTexture(TextureTarget.Texture1D, 0);

                GLES20.glUseProgram(0);
                GLES30.glBindBufferBase(BufferTarget.UniformBuffer, g_materialBlockIndex, 0);
                */
            }

            if(g_bDrawLights)
            {
                try (PushStack pushstack = new PushStack(modelMatrix))
                {
                    modelMatrix.Translate(new Vector3f(CalcLightPosition()));
                    modelMatrix.Scale(0.25f);

                    GLES20.glUseProgram(g_Unlit.theProgram);
                    Matrix4f mm = modelMatrix.Top();
                    GLES20.glUniformMatrix4fv(g_Unlit.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);

                    Vector4f lightColor = new Vector4f(1f, 1f, 1f, 1f);
                    GLES20.glUniform4fv(g_Unlit.objectColorUnif, 1, lightColor.toArray(), 0);
                    g_pCubeMesh.Render("flat");
                }

                modelMatrix.Translate(globalLightDirection.mul(100.0f));
                modelMatrix.Scale(5.0f);

                Matrix4f mm2 = modelMatrix.Top();
                GLES20.glUniformMatrix4fv(g_Unlit.modelToCameraMatrixUnif, 1, false, mm2.toArray(), 0);
                g_pCubeMesh.Render("flat");

                GLES20.glUseProgram(0);
            }

            if(g_bDrawCameraPos)
            {
                try (PushStack pushstack = new PushStack(modelMatrix))
                {
                    modelMatrix.SetIdentity();
                    modelMatrix.Translate(new Vector3f(0.0f, 0.0f, -g_viewPole.GetView().radius));
                    modelMatrix.Scale(0.25f);

                    GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                    GLES20.glDepthMask(false);
                    GLES20.glUseProgram(g_Unlit.theProgram);
                    Matrix4f mm = modelMatrix.Top();
                    GLES20.glUniformMatrix4fv(g_Unlit.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                    GLES20.glUniform4f(g_Unlit.objectColorUnif, 0.25f, 0.25f, 0.25f, 1.0f);
                    g_pCubeMesh.Render("flat");
                    GLES20.glDepthMask(true);
                    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                    GLES20.glUniform4f(g_Unlit.objectColorUnif, 1.0f, 1.0f, 1.0f, 1.0f);
                    g_pCubeMesh.Render("flat");
                }
            }
        }

        //glutPostRedisplay();
        //glutSwapBuffers();
    }

    void reshape (int w, int h)
    {
        MatrixStack persMatrix = new MatrixStack();
        persMatrix.Perspective(45.0f, (w / (float)h), g_fzNear, g_fzFar);

        ProjectionBlock projData = new ProjectionBlock();
        projData.cameraToClipMatrix = persMatrix.Top();

        GLES20.glBindBuffer(GLES30.GL_UNIFORM_BUFFER, g_projectionUniformBuffer);
        // FIXME GLES20.glBufferSubData(GLES30.GL_UNIFORM_BUFFER, 0, ProjectionBlock.byteLength(), projData.ToFloat());
        GLES20.glBindBuffer(GLES30.GL_UNIFORM_BUFFER, 0);

        GLES20.glViewport(0, 0, w, h);
        //glutPostRedisplay();
    }


    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_ESCAPE:
                g_pObjectMesh = null;
                g_pCubeMesh = null;
                break;

            //case Keys.P: g_lightTimer.TogglePause(); break;
            //case Keys.Subtract: g_lightTimer.Rewind(0.5f); break;
            //case Keys.Add: g_lightTimer.Fastforward(0.5f); break;
            case KeyEvent.KEYCODE_T: g_bDrawCameraPos = !g_bDrawCameraPos; break;
            case KeyEvent.KEYCODE_G: g_bDrawLights = !g_bDrawLights; break;
            case KeyEvent.KEYCODE_SPACE:
                g_bUseTexture = !g_bUseTexture;
                if(g_bUseTexture)
                    result.append("Texture\n");
                else
                    result.append("Shader\n");
                break;
            case KeyEvent.KEYCODE_1: g_currTexture = 0; break;
            case KeyEvent.KEYCODE_2: g_currTexture = 1; break;
            case KeyEvent.KEYCODE_3: g_currTexture = 2; break;
            case KeyEvent.KEYCODE_4: g_currTexture = 3; break;
            case KeyEvent.KEYCODE_5: g_currTexture = 4; break;
            case KeyEvent.KEYCODE_6: g_currTexture = 5; break;
            case KeyEvent.KEYCODE_7: g_currTexture = 6; break;
            case KeyEvent.KEYCODE_8: g_currTexture = 7; break;
            case KeyEvent.KEYCODE_9: g_currTexture = 8; break;
        }
        result.append("Angle Resolution:  " + String.valueOf(CalcCosAngResolution(g_currTexture)));
        return result.toString();
    }
}

class PerLight
{
    public Vector4f cameraSpaceLightPos;
    public Vector4f lightIntensity;

    public static int Size()
    {
        int size = 0;
        size += Vector4f.sizeInBytes();
        return size;
    }

    public float[] ToFloat()
    {
        float[] result = new float[Size()/4];
        int position = 0;
        System.arraycopy(cameraSpaceLightPos.toArray(), 0, result, position, 4);
        position += 4;
        System.arraycopy(lightIntensity.toArray(), 0, result, position, 4);
        return result;
    }
};
