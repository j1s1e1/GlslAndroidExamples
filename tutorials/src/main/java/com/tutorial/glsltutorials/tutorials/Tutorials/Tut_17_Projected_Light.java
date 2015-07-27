package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Light.FrameworkTimer;
import com.tutorial.glsltutorials.tutorials.Light.LightBlock;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.ProjectionBlock;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.Scene.FrameworkScene;
import com.tutorial.glsltutorials.tutorials.Scene.NodeRef;
import com.tutorial.glsltutorials.tutorials.Scene.UniformBinderBase;
import com.tutorial.glsltutorials.tutorials.Scene.UniformIntBinder;
import com.tutorial.glsltutorials.tutorials.Scene.UniformMat4Binder;
import com.tutorial.glsltutorials.tutorials.Scene.UniformVec3Binder;
import com.tutorial.glsltutorials.tutorials.View.Framework;
import com.tutorial.glsltutorials.tutorials.View.MouseButtons;
import com.tutorial.glsltutorials.tutorials.View.ViewData;
import com.tutorial.glsltutorials.tutorials.View.ViewPole;
import com.tutorial.glsltutorials.tutorials.View.ViewScale;

import java.util.ArrayList;

/**
 * Created by jamie on 1/3/15.
 */
public class Tut_17_Projected_Light extends TutorialBase {
    Vector3f translateVector = new Vector3f(0f, 0f, 0f);
    float scaleFactor = 0.02f;
    static int NUMBER_OF_LIGHTS = 2;
    public Tut_17_Projected_Light ()
    {
    }

    float g_fzNear = 1.0f;
    float g_fzFar = 1000.0f;

    int g_projectionBlockIndex = 0;
    int g_lightBlockIndex = 1;
    int g_lightProjTexUnit = 3;


    int NUM_SAMPLERS = 2;
    int[] g_samplers = new int[NUM_SAMPLERS];

    void useSampler(int samplerNumber)
    {
        // FIXME GLES20.glGenSamplers(NUM_SAMPLERS, out g_samplers[0]);

        // FIXME GLES20.glGenSamplers(NUM_SAMPLERS, out g_samplers[0]);
        // For all groups
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

        switch (samplerNumber)
        {
            case 0:
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                break;
            case 1:
                // FIXME GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_BORDER);
                // FIXME GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_BORDER);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                break;

        }

        //float[] color = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
        // FIXME GLES20.glSamplerParameter(g_samplers[1], SamplerParameterName.bor  GL_TEXTURE_BORDER_COLOR, color);
    }

    class TexDef
    {
        public String fileName;
        public String name;
        public TexDef(String fn, String n)
        {
            fileName = fn;
            name = n;
        }

    }

    TexDef[] g_texDefs = new TexDef[]
    {
            new TexDef("flashlight.png", "Flashlight"),
            new TexDef("pointsoflight.png", "Multiple Point Lights"),
            new TexDef("bands.png", "Light Bands")
    };


    int[] g_lightTextures = new int[g_texDefs.length];
    int NUM_LIGHT_TEXTURES = g_texDefs.length;
    int g_currTextureIndex = 0;

    void LoadTextures()
    {
        try
        {
            for(int tex = 0; tex < NUM_LIGHT_TEXTURES; ++tex)
            {
                g_lightTextures[tex] = Textures.loadTexture(Shader.context, g_texDefs[tex].fileName);
            }
        }
        catch(Exception ex)
        {
           Log.i("Projected Light", "Error loading textures " + ex.toString());
        }
    }

    ////////////////////////////////
    //View setup.
    static ViewData g_initialView = new ViewData
            (
                    new Vector3f(0.0f, 0.0f, 10.0f),
                    new Quaternion(0.16043f, -0.376867f, -0.0664516f, 0.909845f),
                    25.0f,
                    0.0f
            );

    static ViewScale g_initialViewScale = new ViewScale
    (
            5.0f, 70.0f,
            2.0f, 0.5f,
            2.0f, 0.5f,
            90.0f/250.0f
    );


    static ViewData g_initLightView = new ViewData
            (
                    new Vector3f(0.0f, 0.0f, 20.0f),
                    new Quaternion(0.0f, 0.0f, 0.0f, 1.0f),
                    5.0f,
                    0.0f
            );

    static ViewScale g_initLightViewScale = new ViewScale
            (
                    0.05f, 10.0f,
                    0.1f, 0.05f,
                    4.0f, 1.0f,
                    90.0f/250.0f
            );

    ViewPole g_viewPole = new ViewPole(g_initialView, g_initialViewScale, MouseButtons.MB_LEFT_BTN);
    ViewPole g_lightViewPole = new ViewPole(g_initLightView, g_initLightViewScale, MouseButtons.MB_RIGHT_BTN, true);

    public void MouseMotion(int x, int y)
    {
        Framework.forwardMouseMotion(g_viewPole, x, y);
        Framework.forwardMouseMotion(g_lightViewPole, x, y);
    }

    public void MouseButton(int button, int state, int x, int y)
    {
        Framework.forwardMouseButton(g_viewPole, button, state, x, y);
        Framework.forwardMouseButton(g_lightViewPole, button, state, x, y);
    }

    void MouseWheel(int wheel, int direction, int x, int y)
    {
        Framework.forwardMouseWheel(g_viewPole, wheel, direction, x, y);
    }

    FrameworkScene g_pScene;
    ArrayList<NodeRef> g_nodes;
    FrameworkTimer g_timer = new FrameworkTimer(FrameworkTimer.Type.TT_LOOP, 10.0f);

    UniformIntBinder g_lightNumBinder;
    UniformMat4Binder g_lightProjMatBinder;
    UniformVec3Binder g_camLightPosBinder;

    Quaternion g_spinBarOrient;

    int g_unlitModelToCameraMatrixUnif;
    int g_unlitCameraToClipMatrixUnif;
    int g_unlitObjectColorUnif;
    int g_unlitProg;
    Mesh g_pSphereMesh;

    int g_coloredModelToCameraMatrixUnif;
    int g_coloredCameraToClipMatrixUnif;
    int g_colroedProg;

    int g_projLightCameraToClipMatrixUnif;
    int g_projLightProg;

    Mesh g_pAxesMesh;


    void LoadAndSetupScene() throws Exception
    {

        FrameworkScene pScene = new FrameworkScene("proj2d_scene.xml");

        ArrayList<NodeRef> nodes = new ArrayList<NodeRef>();
        nodes.add(pScene.findNode("cube"));
        nodes.add(pScene.findNode("rightBar"));
        nodes.add(pScene.findNode("leaningBar"));
        nodes.add(pScene.findNode("spinBar"));
        nodes.add(pScene.findNode("diorama"));
        nodes.add(pScene.findNode("floor"));

        g_lightNumBinder = new UniformIntBinder();
        AssociateUniformWithNodes(nodes, g_lightNumBinder, "numberOfLights");
        SetStateBinderWithNodes(nodes, g_lightNumBinder);
        g_lightProjMatBinder = new UniformMat4Binder();
        AssociateUniformWithNodes(nodes, g_lightProjMatBinder, "cameraToLightProjMatrix");
        SetStateBinderWithNodes(nodes, g_lightProjMatBinder);
        g_camLightPosBinder = new UniformVec3Binder();
        AssociateUniformWithNodes(nodes, g_camLightPosBinder, "cameraSpaceProjLightPos");
        SetStateBinderWithNodes(nodes, g_camLightPosBinder);

        int unlit = pScene.findProgram("p_unlit");
        Mesh pSphereMesh = pScene.FindMesh("m_sphere");

        int colored = pScene.findProgram("p_colored");

        int projLight = pScene.findProgram("p_proj");

        Mesh pAxesMesh = pScene.FindMesh("m_axes");

        //No more things that can throw.
        g_spinBarOrient = nodes.get(3).NodeGetOrient();

        g_unlitProg = unlit;
        GLES20.glUseProgram(unlit);
        g_unlitModelToCameraMatrixUnif = GLES20.glGetUniformLocation(unlit, "modelToCameraMatrix");
        g_unlitCameraToClipMatrixUnif  = GLES20.glGetUniformLocation(unlit, "cameraToClipMatrix");
        g_unlitObjectColorUnif = GLES20.glGetUniformLocation(unlit, "objectColor");
        GLES20.glUseProgram(0);

        g_colroedProg = colored;
        GLES20.glUseProgram(colored);
        g_coloredCameraToClipMatrixUnif= GLES20.glGetUniformLocation(colored, "cameraToClipMatrix");
        g_coloredModelToCameraMatrixUnif = GLES20.glGetUniformLocation(colored, "modelToCameraMatrix");
        GLES20.glUseProgram(0);

        g_projLightProg = projLight;
        GLES20.glUseProgram(projLight);
        g_projLightCameraToClipMatrixUnif= GLES20.glGetUniformLocation(projLight, "cameraToClipMatrix");
        GLES20.glUseProgram(0);

        g_nodes = nodes;

        g_pSphereMesh = pSphereMesh;

        g_pScene = pScene;

        g_pAxesMesh = pAxesMesh;
    }

    int MAX_NUMBER_OF_LIGHTS = 4;

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        setupDepthAndCull();

        // FIXME GLES20.glEnable(GLES20.GL_FRAMEBUFFER_SRGB);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        // FIXME MatrixStack.rightMultiply = false;

        // FIXME replace with use samplers CreateSamplers();
        LoadTextures();

        try
        {
            LoadAndSetupScene();
        }
        catch(Exception ex)
        {
           Log.e("Projected Light", "Error loading scene " + ex.toString());
        }
        clearColor = new Vector4f(0.8f, 0.8f, 0.8f, 1.0f);
    }

    int g_currSampler = 0;

    float[] g_lightFOVs = new float[]{ 10.0f, 20.0f, 45.0f, 75.0f, 90.0f, 120.0f, 150.0f, 170.0f };
    int g_currFOVIndex = 3;

    boolean g_bDrawCameraPos = true;
    boolean g_bShowOtherLights = true;

    int g_displayWidth = 500;
    int g_displayHeight = 500;

    void BuildLights(Matrix4f camMatrix )
    {
        LightBlock lightData = new LightBlock(NUMBER_OF_LIGHTS);
        lightData.ambientIntensity = new Vector4f(0.2f, 0.2f, 0.2f, 1.0f);
        lightData.lightAttenuation = 1.0f / (30.0f * 30.0f);
        lightData.maxIntensity = 2.0f;
        lightData.lights[0].lightIntensity = new Vector4f(0.2f, 0.2f, 0.2f, 1.0f);
        lightData.lights[0].cameraSpaceLightPos =
                Vector4f.Transform(Vector4f.normalize(new Vector4f(-0.2f, 0.5f, 0.5f, 0.0f)), camMatrix);
        lightData.lights[1].lightIntensity = new Vector4f(3.5f, 6.5f, 3.0f, 1.0f).Mult(0.5f);
        lightData.lights[1].cameraSpaceLightPos =
                Vector4f.Transform(new Vector4f(5.0f, 6.0f, 0.5f, 1.0f), camMatrix);

        // Update in used programs
        lightData.setUniforms(g_unlitProg);
        lightData.updateInternal();

        lightData.setUniforms(g_colroedProg);
        lightData.updateInternal();

        lightData.setUniforms(g_projLightProg);
        lightData.updateInternal();
    }

    public void display() throws Exception
    {
        if(g_pScene == null)
            return;

        g_timer.Update();
        
        clearDisplay();

        Matrix4f cameraMatrix = g_viewPole.CalcMatrix();
        Matrix4f lightView = g_lightViewPole.CalcMatrix();

        MatrixStack modelMatrix = new MatrixStack();
        modelMatrix.ApplyMatrix(cameraMatrix);

        BuildLights(cameraMatrix);

        g_nodes.get(0).NodeSetOrient(Quaternion.fromAxisAngle(new Vector3f(0.0f, 1.0f, 0.0f),
                360.0f * g_timer.GetAlpha()));
        g_nodes.get(3).NodeSetOrient(Quaternion.mult(g_spinBarOrient, Quaternion.fromAxisAngle(new Vector3f(0.0f, 0.0f, 1.0f),
                360.0f * g_timer.GetAlpha())));

        {
            MatrixStack persMatrix = new MatrixStack();
            persMatrix.perspective(60.0f, (g_displayWidth / (float) g_displayHeight), g_fzNear, g_fzFar);
            // added
            //persMatrix.Scale(scaleFactor);
            //persMatrix.Translate(translateVector);
            // end added

            ProjectionBlock projData = new ProjectionBlock();
            projData.cameraToClipMatrix = persMatrix.Top();

            GLES20.glUseProgram(g_colroedProg);
            GLES20.glUniformMatrix4fv(g_coloredCameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
            GLES20.glUseProgram(0);

            GLES20.glUseProgram(g_unlitProg);
            GLES20.glUniformMatrix4fv(g_unlitCameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
            GLES20.glUseProgram(0);

            GLES20.glUseProgram(g_projLightProg);
            GLES20.glUniformMatrix4fv(g_projLightCameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
            GLES20.glUseProgram(0);

        }

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + g_lightProjTexUnit);
        GLES20.glBindTexture(GLES10.GL_TEXTURE_2D, g_lightTextures[g_currTextureIndex]);
        // FIXME GLES20.glBindSampler(g_lightProjTexUnit, g_samplers[g_currSampler]);

        {
            MatrixStack lightProjStack = new MatrixStack();
            //Texture-space transform
            lightProjStack.Translate(0.5f, 0.5f, 0.0f);
            lightProjStack.scale(0.5f, 0.5f, 1.0f);
            //Project. Z-range is irrelevant.
            lightProjStack.perspective(g_lightFOVs[g_currFOVIndex], 1.0f, 1.0f, 100.0f);
            //transform from main camera space to light camera space.
            lightProjStack.ApplyMatrix(lightView);
            Matrix4f cmI = cameraMatrix.inverted();
            lightProjStack.ApplyMatrix(cmI);

            g_lightProjMatBinder.setValue(lightProjStack.Top());

            // Row or Column??
            Matrix4f lightViewI = lightView.inverted();
            Vector4f worldLightPos = lightViewI.GetRow3();
            Vector3f lightPos = new Vector3f(Vector4f.Transform(worldLightPos, cameraMatrix));

            g_camLightPosBinder.setValue(lightPos);
        }

        GLES20.glViewport(0, 0, width, height);
        g_pScene.render(modelMatrix.Top());

        {
            //Draw axes
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.ApplyMatrix(lightView.inverted());
                modelMatrix.Scale(15.0f);
                modelMatrix.scale(1.0f, 1.0f, -1.0f); //Invert the Z-axis so that it points in the right direction.

                GLES20.glUseProgram(g_colroedProg);
                Matrix4f mm = modelMatrix.Top();
                GLES20.glUniformMatrix4fv(g_coloredModelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                g_pAxesMesh.render();
            }
        }

        if(g_bDrawCameraPos)
        {
            //Draw lookat point.
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.SetIdentity();
                modelMatrix.translate(new Vector3f(0.0f, 0.0f, -g_viewPole.GetView().radius));
                modelMatrix.Scale(0.5f);

                GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                GLES20.glDepthMask(false);
                GLES20.glUseProgram(g_unlitProg);
                Matrix4f mm = modelMatrix.Top();
                GLES20.glUniformMatrix4fv(g_unlitModelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                GLES20.glUniform4f(g_unlitObjectColorUnif, 0.25f, 0.25f, 0.25f, 1.0f);
                g_pSphereMesh.render("flat");
                GLES20.glDepthMask(true);
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                GLES20.glUniform4f(g_unlitObjectColorUnif, 1.0f, 1.0f, 1.0f, 1.0f);
                g_pSphereMesh.render("flat");
            }
        }

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + g_lightProjTexUnit);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        // FIXME GLES20.glBindSampler(g_lightProjTexUnit, 0);
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_ESCAPE:
                g_pScene = null;
                break;
            case KeyEvent.KEYCODE_SPACE:
                g_lightViewPole.reset();
                break;
            case KeyEvent.KEYCODE_T:
                g_bDrawCameraPos = !g_bDrawCameraPos;
                break;
            case KeyEvent.KEYCODE_G:
                g_bShowOtherLights = !g_bShowOtherLights;
                break;
            case KeyEvent.KEYCODE_H:
                g_currSampler = (g_currSampler + 1) % NUM_SAMPLERS;
                break;
            case KeyEvent.KEYCODE_P:
                g_timer.TogglePause();
                break;
            case KeyEvent.KEYCODE_ENTER: //Enter key.
            {
                try
                {
                    LoadAndSetupScene();
                }
                catch(Exception ex)
                {
                   Log.e("KeyEvent", "Failed to reload, due to: " + ex.toString());
                }
            }
            break;
            case KeyEvent.KEYCODE_Y:
                g_currFOVIndex = Math.min(g_currFOVIndex + 1, g_lightFOVs.length - 1);
                Log.i("KeyEvent", "Curr FOV: " + String.valueOf(g_lightFOVs[g_currFOVIndex]));
                break;
            case KeyEvent.KEYCODE_N:
                g_currFOVIndex = Math.max(g_currFOVIndex - 1, 0);
                Log.i("KeyEvent", "Curr FOV: " + String.valueOf(g_lightFOVs[g_currFOVIndex]));
                break;
            case KeyEvent.KEYCODE_1:
                g_currTextureIndex = 0;
                Log.i("KeyEvent", "Current Texture = " + g_texDefs[g_currTextureIndex].name);
                break;
            case KeyEvent.KEYCODE_2:
                g_currTextureIndex = 1;
                Log.i("KeyEvent", "Current Texture = " + g_texDefs[g_currTextureIndex].name);
                break;
            case KeyEvent.KEYCODE_3:
                g_currTextureIndex = 2;
                Log.i("KeyEvent", "Current Texture = " + g_texDefs[g_currTextureIndex].name);
                break;
            case KeyEvent.KEYCODE_NUMPAD_SUBTRACT:
                MouseWheel(1, 0, 10, 10);
                break;
            case KeyEvent.KEYCODE_NUMPAD_ADD:
                MouseWheel(1, 1, 10, 10);
                break;
        }

        g_viewPole.CharPress((char)keyCode);
        g_lightViewPole.CharPress((char)keyCode);
        return result.toString();
    }

    void AssociateUniformWithNodes(ArrayList<NodeRef> nodes, UniformBinderBase binder, String unifName)
    {
        for (NodeRef nr : nodes)
        {
            binder.AssociateWithProgram(nr.getProgram(), unifName);
        }
    }

    void SetStateBinderWithNodes(ArrayList<NodeRef> nodes, UniformBinderBase binder)
    {
        for (NodeRef nr : nodes)
        {
            nr.setStateBinder(binder);
        }
    }
}
