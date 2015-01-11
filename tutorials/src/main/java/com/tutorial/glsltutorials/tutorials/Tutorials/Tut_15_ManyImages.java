package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Light.FrameworkTimer;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.ProjectionBlock;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.R;

/**
 * Created by jamie on 1/2/15.
 */
public class Tut_15_ManyImages extends TutorialBase {

    class ProgramData
    {
        public int theProgram;
        public int modelToCameraMatrixUnif;
        public int cameraToClipMatrixUnif;
    };

    float g_fzNear = 1.0f;
    float g_fzFar = 1000.0f;

    ProgramData g_program;

    int g_colorTexUnit = 0;

    ProgramData LoadProgram(String vertexShader, String fragmentShader)
    {
        ProgramData data = new ProgramData();
        int vertexShaderInt = Shader.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderInt = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        data.theProgram = Shader.createAndLinkProgram(vertexShaderInt, fragmentShaderInt);
        data.modelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToCameraMatrix");

        data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");

        int colorTextureUnif = GLES20.glGetUniformLocation(data.theProgram, "colorTexture");
        GLES20.glUseProgram(data.theProgram);
        GLES20.glUniform1f(colorTextureUnif, g_colorTexUnit);
        GLES20.glUseProgram(0);

        return data;
    }

    void InitializePrograms()
    {
        g_program = LoadProgram(VertexShaders.PT, FragmentShaders.Tex);
    }

    int g_checkerTexture = 0;
    int g_mipmapTestTexture = 0;

    int NUM_SAMPLERS = 6;

    void useSampler(int samplerNumber)
    {
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        switch (samplerNumber) {
            case 0:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                break;

            case 1:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                break;

            case 2:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
                break;

            case 3:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
                break;

            case 4:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
                //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.TextureMaxAnisotropyExt, 4f);
                break;

            case 5:
                //Max anisotropic
                float maxAniso = 0.0f;
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
                //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.TextureMaxAnisotropyExt, maxAniso);
                break;
        }
    }

    void LoadMipmapTexture()
    {
        try
        {
            g_mipmapTestTexture = Textures.createMipMapTexture(Shader.context, R.drawable.checker);
        }
        catch(Exception ex)
        {
            Log.i("Loading Texture ", "Error " + ex.toString());
        }
    }

    void LoadCheckerTexture()
    {
        try
        {
            g_checkerTexture = Textures.createMipMapTexture(Shader.context, R.drawable.checker);
        }
        catch(Exception ex)
        {
            Log.i("Loading Texture ", "Error " + ex.toString());
        }
    }

    Mesh g_pPlane = null;
    Mesh g_pCorridor = null;

    protected void init ()
    {
        InitializePrograms();

        try
        {
            g_pCorridor = new Mesh("corridor.xml");
            g_pPlane = new Mesh("bigplane.xml");
        }
        catch(Exception ex)
        {
            Log.e("Mesh Creation ", "Error  " + ex.toString());
        }
        
        setupDepthAndCull();

        LoadCheckerTexture();
        LoadMipmapTexture();
        clearColor = new Vector4f(0.75f, 0.75f, 1.0f, 1.0f);
    }

    FrameworkTimer g_camTimer = new FrameworkTimer(FrameworkTimer.Type.TT_LOOP, 5.0f);
    int g_currSampler = 3;

    boolean g_useMipmapTexture = true;
    boolean g_drawCorridor = true;

    public void display() throws Exception
    {
        clearDisplay();

        if(g_pPlane != null && g_pCorridor != null )
        {
            g_camTimer.Update();

            float cyclicAngle = g_camTimer.GetAlpha() * 6.28f;
            float hOffset = (float)Math.cos(cyclicAngle) * 0.25f;
            float vOffset = (float)Math.sin(cyclicAngle) * 0.25f;

            MatrixStack modelMatrix = new MatrixStack();

            Matrix4f worldToCamMat = Matrix4f.LookAt(
                    new Vector3f(hOffset, 1.0f, -64.0f),
                    new Vector3f(hOffset, -5.0f + vOffset, -44.0f),
                    new Vector3f(0.0f, 1.0f, 0.0f));

            modelMatrix.ApplyMatrix(worldToCamMat);

            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                GLES20.glUseProgram(g_program.theProgram);
                Matrix4f mm = modelMatrix.Top();
                GLES20.glUniformMatrix4fv(g_program.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + g_colorTexUnit);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,
                        g_useMipmapTexture ? g_mipmapTestTexture : g_checkerTexture);
                useSampler(g_currSampler);

                if(g_drawCorridor)
                    g_pCorridor.render("tex");
                else
                    g_pPlane.render("tex");

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

                GLES20.glUseProgram(0);
            }
        }
    }

    public void reshape()
    {
        MatrixStack persMatrix = new MatrixStack();
        persMatrix.perspective(90.0f, (width / (float) height), g_fzNear, g_fzFar);

        ProjectionBlock projData = new ProjectionBlock();
        projData.cameraToClipMatrix = persMatrix.Top();

        GLES20.glUseProgram(g_program.theProgram);
        GLES20.glUniformMatrix4fv(g_program.cameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
        GLES20.glUseProgram(0);

        GLES20.glViewport(0, 0, width, height);
    }

    String[] g_samplerNames = new String[]
    {
        "Nearest",
        "Linear",
        "Linear with nearest mipmaps",
        "Linear with linear mipmaps",
        "Low anisotropic",
        "Max anisotropic",
    };

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_ESCAPE:
                g_pPlane = null;
                g_pCorridor = null;
                break;
            case KeyEvent.KEYCODE_SPACE:
                g_useMipmapTexture = !g_useMipmapTexture;
                break;
            case KeyEvent.KEYCODE_Y:
                g_drawCorridor = !g_drawCorridor;
                break;
            case KeyEvent.KEYCODE_P:
                g_camTimer.TogglePause();
                break;
            case KeyEvent.KEYCODE_1:
                g_currSampler = 0;
                Log.i("KeyEvent", "Sampler: " + g_samplerNames[g_currSampler]);
                break;
            case KeyEvent.KEYCODE_2:
                g_currSampler = 1;
                Log.i("KeyEvent", "Sampler: " + g_samplerNames[g_currSampler]);
                break;
            case KeyEvent.KEYCODE_3:
                g_currSampler = 2;
                Log.i("KeyEvent", "Sampler: " + g_samplerNames[g_currSampler]);
                break;
            case KeyEvent.KEYCODE_4:
                g_currSampler = 3;
                Log.i("KeyEvent", "Sampler: " + g_samplerNames[g_currSampler]);
                break;
            case KeyEvent.KEYCODE_5:
                g_currSampler = 4;
                Log.i("KeyEvent", "Sampler: " + g_samplerNames[g_currSampler]);
                break;
            case KeyEvent.KEYCODE_6:
                g_currSampler = 5;
                Log.i("KeyEvent", "Sampler: " + g_samplerNames[g_currSampler]);
                break;
        }
        return result.toString();
    }
}
