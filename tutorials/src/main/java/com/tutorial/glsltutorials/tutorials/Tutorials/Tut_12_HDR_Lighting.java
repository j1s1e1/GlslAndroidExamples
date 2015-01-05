package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.AnalysisTools;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.ShadersNames;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Light.FrameworkTimer;
import com.tutorial.glsltutorials.tutorials.Light.LightBlock;
import com.tutorial.glsltutorials.tutorials.Light.LightManager;
import com.tutorial.glsltutorials.tutorials.Light.LightingProgramTypes;
import com.tutorial.glsltutorials.tutorials.Light.SunlightValue;
import com.tutorial.glsltutorials.tutorials.Light.TimerTypes;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.ProjectionBlock;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.Scene.Scene;
import com.tutorial.glsltutorials.tutorials.Scene.SceneProgramData;
import com.tutorial.glsltutorials.tutorials.View.Framework;
import com.tutorial.glsltutorials.tutorials.View.MouseButtons;
import com.tutorial.glsltutorials.tutorials.View.ViewData;
import com.tutorial.glsltutorials.tutorials.View.ViewPole;
import com.tutorial.glsltutorials.tutorials.View.ViewScale;

/**
 * Created by jamie on 1/2/15.
 */
public class Tut_12_HDR_Lighting extends TutorialBase {
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
    boolean renderSun = false;
    boolean initializationComplete = false;
    // debug fields
    Matrix4f sunModelToCameraMatrix = Matrix4f.Identity();
    Matrix4f g_viewPole_CalcMatrix = Matrix4f.Identity();

    class UnlitProgData
    {
        public int theProgram;

        public int objectColorUnif;
        public int cameraToClipMatrixUnif;
        public int modelToCameraMatrixUnif;

    void SetWindowData(Matrix4f cameraToClip)
    {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(cameraToClipMatrixUnif, 1, false, cameraToClip.toArray(), 0);
        GLES20.glUseProgram(0);
    }
};

    float g_fzNear = 1.0f;
    float g_fzFar = 1000.0f;

    SceneProgramData[] g_Programs = new SceneProgramData[LightingProgramTypes.LP_MAX_LIGHTING_PROGRAM_TYPES];
    ShadersNames[] g_ShaderFiles = new ShadersNames[]
    {
        new ShadersNames(VertexShaders.HDR_PCN, FragmentShaders.DiffuseSpecularHDR),
        new ShadersNames(VertexShaders.HDR_PCN, FragmentShaders.DiffuseOnlyHDR),

        new ShadersNames(VertexShaders.HDR_PCN, FragmentShaders.DiffuseSpecularMtlHDR),
        new ShadersNames(VertexShaders.HDR_PCN, FragmentShaders.DiffuseOnlyMtlHDR),
    };

    UnlitProgData g_Unlit;

    int g_materialBlockIndex = 0;

    UnlitProgData LoadUnlitProgram(String vertexShader, String fragmentShader)
    {
        UnlitProgData data = new UnlitProgData();
        int vertexShaderInt = Shader.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderInt = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        data.theProgram  = Shader.createAndLinkProgram(vertexShaderInt, fragmentShaderInt);
        data.modelToCameraMatrixUnif =  GLES20.glGetUniformLocation(data.theProgram, "modelToCameraMatrix");
        data.objectColorUnif =  GLES20.glGetUniformLocation(data.theProgram, "baseColor");

        data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");

        return data;
    }

    void InitializePrograms()
    {
        for(int iProg = 0; iProg < (int)LightingProgramTypes.LP_MAX_LIGHTING_PROGRAM_TYPES; iProg++)
        {
            g_Programs[iProg] = SceneProgramData.LoadLitProgram(g_ShaderFiles[iProg]);
        }

        g_Unlit = LoadUnlitProgram(VertexShaders.PosTransform, FragmentShaders.ColorUniform_frag);
    }

    SceneProgramData GetProgram(int eType)
    {
        return g_Programs[eType];
    }


    LightManager g_lights = new LightManager();

    ///////////////////////////////////////////////
    // View/Object Setup
    static ViewData g_initialViewData = new ViewData
    (
        new Vector3f(-59.5f, 44.0f, 95.0f),
        Quaternion.fromAxisAngle(new Vector3f(1.0f, 0f, 0f), 0f),
        50.0f,
        0.0f
    );

    static ViewScale g_viewScale = new ViewScale
    (
        3.0f, 80.0f,
        4.0f, 1.0f,
        5.0f, 1.0f,
        90.0f/250.0f
    );

        ViewPole g_viewPole = new ViewPole(g_initialViewData,
        g_viewScale, MouseButtons.MB_LEFT_BTN);

    public void mouseMotion(int x, int y)
    {
        Framework.forwardMouseMotion(g_viewPole, x, y);
        Framework.forwardMouseMotion(g_viewPole, x, y);
    }

    public void mouseButton(int button, int state, int x, int y)
    {
        Framework.forwardMouseButton(g_viewPole, button, state, x, y);
        Framework.forwardMouseButton(g_viewPole, button, state, x, y);
    }

    void mouseWheel(int wheel, int direction, int x, int y)
    {
        Framework.forwardMouseWheel(g_viewPole, wheel, direction, x, y);
        Framework.forwardMouseWheel(g_viewPole, wheel, direction, x, y);
    }

    Vector4f g_skyDaylightColor = new Vector4f(0.65f, 0.65f, 1.0f, 1.0f);

    void SetupDaytimeLighting()
    {
        SunlightValue[] values = new SunlightValue[]
        {
            new SunlightValue(0.0f/24.0f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector4f(0.6f, 0.6f, 0.6f, 1.0f), g_skyDaylightColor),
            new SunlightValue(4.5f/24.0f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector4f(0.6f, 0.6f, 0.6f, 1.0f), g_skyDaylightColor),
            new SunlightValue(6.5f/24.0f, new Vector4f(0.15f, 0.05f, 0.05f, 1.0f), new Vector4f(0.3f, 0.1f, 0.10f, 1.0f), new Vector4f(0.5f, 0.1f, 0.1f, 1.0f)),
            new SunlightValue(8.0f/24.0f, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f)),
            new SunlightValue(18.0f/24.0f, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f)),
            new SunlightValue(19.5f/24.0f, new Vector4f(0.15f, 0.05f, 0.05f, 1.0f), new Vector4f(0.3f, 0.1f, 0.1f, 1.0f), new Vector4f(0.5f, 0.1f, 0.1f, 1.0f)),
            new SunlightValue(20.5f/24.0f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector4f(0.6f, 0.6f, 0.6f, 1.0f), g_skyDaylightColor),
        };

        g_lights.SetSunlightValues(values);

        g_lights.SetPointLightIntensity(0, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f));
        g_lights.SetPointLightIntensity(1, new Vector4f(0.0f, 0.0f, 0.3f, 1.0f));
        g_lights.SetPointLightIntensity(2, new Vector4f(0.3f, 0.0f, 0.0f, 1.0f));
    }

    void SetupNighttimeLighting()
    {
        SunlightValue[] values = new SunlightValue[]
        {
            new SunlightValue(0.0f/24.0f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector4f(0.6f, 0.6f, 0.6f, 1.0f), g_skyDaylightColor),
            new SunlightValue(4.5f/24.0f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector4f(0.6f, 0.6f, 0.6f, 1.0f), g_skyDaylightColor),
            new SunlightValue(6.5f/24.0f, new Vector4f(0.15f, 0.05f, 0.05f, 1.0f), new Vector4f(0.3f, 0.1f, 0.10f, 1.0f), new Vector4f(0.5f, 0.1f, 0.1f, 1.0f)),
            new SunlightValue(8.0f/24.0f, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f)),
            new SunlightValue(18.0f/24.0f, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f)),
            new SunlightValue(19.5f/24.0f, new Vector4f(0.15f, 0.05f, 0.05f, 1.0f), new Vector4f(0.3f, 0.1f, 0.1f, 1.0f), new Vector4f(0.5f, 0.1f, 0.1f, 1.0f)),
            new SunlightValue(20.5f/24.0f, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), new Vector4f(0.6f, 0.6f, 0.6f, 1.0f), g_skyDaylightColor),
        };

        g_lights.SetSunlightValues(values);

        g_lights.SetPointLightIntensity(0, new Vector4f(0.6f, 0.6f, 0.6f, 1.0f));
        g_lights.SetPointLightIntensity(1, new Vector4f(0.0f, 0.0f, 0.7f, 1.0f));
        g_lights.SetPointLightIntensity(2, new Vector4f(0.7f, 0.0f, 0.0f, 1.0f));
    }

    void SetupHDRLighting()
    {
        SunlightValue[] values = new SunlightValue[]
        {
            new SunlightValue(0.0f/24.0f, new Vector4f(0.6f, 0.6f, 0.6f, 1.0f), new Vector4f(1.8f, 1.8f, 1.8f, 1.0f), g_skyDaylightColor, 3.0f),
            new SunlightValue(4.5f/24.0f, new Vector4f(0.6f, 0.6f, 0.6f, 1.0f), new Vector4f(1.8f, 1.8f, 1.8f, 1.0f), g_skyDaylightColor, 3.0f),
            new SunlightValue(6.5f/24.0f, new Vector4f(0.225f, 0.075f, 0.075f, 1.0f), new Vector4f(0.45f, 0.15f, 0.15f, 1.0f), new Vector4f(0.5f, 0.1f, 0.1f, 1.0f), 1.5f),
            new SunlightValue(8.0f/24.0f, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), 1.0f),
            new SunlightValue(18.0f/24.0f, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), 1.0f),
            new SunlightValue(19.5f/24.0f, new Vector4f(0.225f, 0.075f, 0.075f, 1.0f), new Vector4f(0.45f, 0.15f, 0.15f, 1.0f), new Vector4f(0.5f, 0.1f, 0.1f, 1.0f), 1.5f),
            new SunlightValue(20.5f/24.0f, new Vector4f(0.6f, 0.6f, 0.6f, 1.0f), new Vector4f(1.8f, 1.8f, 1.8f, 1.0f), g_skyDaylightColor, 3.0f),
        };

        g_lights.SetSunlightValues(values);

        g_lights.SetPointLightIntensity(0, new Vector4f(0.6f, 0.6f, 0.6f, 1.0f));
        g_lights.SetPointLightIntensity(1, new Vector4f(0.0f, 0.0f, 0.7f, 1.0f));
        g_lights.SetPointLightIntensity(2, new Vector4f(0.7f, 0.0f, 0.0f, 1.0f));
     }

    Scene g_pScene;

    protected void init()
    {
        InitializePrograms();

        try
        {
            Scene.TutorialPrograms = g_Programs;
            g_pScene = new Scene();
        }
        catch(Exception ex)
        {
            Log.e("Scene Creation ", "Error creating scene " + ex.toString());
        }

        SetupDaytimeLighting();

        g_lights.CreateTimer("tetra", FrameworkTimer.Type.TT_LOOP, 2.5f);

        reshape();
        setupDepthAndCull();
        // FIXME ??  MatrixStack.rightMultiply = false;
        initializationComplete = true;
    }

    boolean g_bDrawCameraPos = false;
    boolean g_bDrawLights = false;

    public void display() throws Exception
    {
        if (!initializationComplete) return;
        g_lights.UpdateTime();
        Vector4f bkg = g_lights.GetBackgroundColor();
        GLES20.glClearColor(bkg.x, bkg.y, bkg.z, bkg.w);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT |  GLES20.GL_DEPTH_BUFFER_BIT);

        MatrixStack modelMatrix = new MatrixStack();
        // TEST  
        g_viewPole_CalcMatrix = g_viewPole.CalcMatrix();
        modelMatrix.SetMatrix(g_viewPole.CalcMatrix());

        Matrix4f worldToCamMat = modelMatrix.Top();
        LightBlock lightData = g_lights.GetLightInformationHDR(worldToCamMat);

        for (SceneProgramData spd : g_Programs)
        {
            spd.lightBlock.update(lightData);
        }

        if(g_pScene !=  null)
        {
            try ( PushStack pushstack = new PushStack(modelMatrix))
            {
                g_pScene.Draw(modelMatrix, g_materialBlockIndex, g_lights.GetTimerValue("tetra"));
            }

            //Render the sun
            if (renderSun) {
                try (PushStack pushstack = new PushStack(modelMatrix)) {
                    Vector3f sunlightDir = new Vector3f(g_lights.GetSunlightDirection());
                    modelMatrix.Translate(sunlightDir.mul(500.0f));
                    //TEST
                    modelMatrix.Scale(30.0f, 30.0f, 30.0f);

                    GLES20.glUseProgram(g_Unlit.theProgram);
                    Matrix4f mm = modelMatrix.Top();
                    sunModelToCameraMatrix = mm;
                    GLES20.glUniformMatrix4fv(g_Unlit.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);

                    Vector4f lightColor = g_lights.GetSunlightIntensity();
                    GLES20.glUniform4fv(g_Unlit.objectColorUnif, 1, lightColor.toArray(), 0);
                    g_pScene.GetSphereMesh().render("flat");
                }

                //Render the lights
                if (g_bDrawLights) {
                    for (int light = 0; light < g_lights.GetNumberOfPointLights(); light++) {
                        try (PushStack pushstack = new PushStack(modelMatrix)) {
                            modelMatrix.Translate(g_lights.GetWorldLightPosition(light));

                            GLES20.glUseProgram(g_Unlit.theProgram);
                            Matrix4f mm = modelMatrix.Top();
                            GLES20.glUniformMatrix4fv(g_Unlit.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);

                            Vector4f lightColor = g_lights.GetPointLightIntensity(light);
                            GLES20.glUniform4fv(g_Unlit.objectColorUnif, 1, lightColor.toArray(), 0);
                            g_pScene.GetCubeMesh().render("flat");
                        }
                    }
                }

                if (g_bDrawCameraPos) {
                    try (PushStack pushstack = new PushStack(modelMatrix)) {
                        modelMatrix.SetIdentity();
                        modelMatrix.Translate(new Vector3f(0.0f, 0.0f, -g_viewPole.GetView().radius));

                        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                        GLES20.glDepthMask(false);
                        GLES20.glUseProgram(g_Unlit.theProgram);
                        Matrix4f mm = modelMatrix.Top();
                        GLES20.glUniformMatrix4fv(g_Unlit.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                        GLES20.glUniform4f(g_Unlit.objectColorUnif, 0.25f, 0.25f, 0.25f, 1.0f);
                        g_pScene.GetCubeMesh().render("flat");
                        GLES20.glDepthMask(true);
                        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                        GLES20.glUniform4f(g_Unlit.objectColorUnif, 1.0f, 1.0f, 1.0f, 1.0f);
                        g_pScene.GetCubeMesh().render("flat");
                    }
                }
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


    ProjectionBlock projData;

    public void reshape()
    {
        MatrixStack persMatrix = new MatrixStack();
        persMatrix.Perspective(45.0f, (width / (float)height), g_fzNear, g_fzFar);
        // added
        persMatrix.Translate(0.0f, 0.0f, -3f);
        persMatrix.Scale(0.01f);
        // end added
        projData = new ProjectionBlock();
        projData.cameraToClipMatrix = persMatrix.Top();

        for(SceneProgramData spd : g_Programs)
        {
            GLES20.glUseProgram(spd.theProgram);
            GLES20.glUniformMatrix4fv(spd.cameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
            GLES20.glUseProgram(0);
        }

        GLES20.glUseProgram(g_Unlit.theProgram);
        GLES20.glUniformMatrix4fv(g_Unlit.cameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
        GLES20.glUseProgram(0);

        GLES20.glViewport(0, 0, width, height);
    }


    TimerTypes g_eTimerMode = TimerTypes.TIMER_ALL;

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_ESCAPE:
                g_pScene = null;
                g_pScene = null;
                break;

            case KeyEvent.KEYCODE_P: g_lights.TogglePause(g_eTimerMode); break;
            case KeyEvent.KEYCODE_NUMPAD_SUBTRACT: g_lights.RewindTime(g_eTimerMode, 1.0f); break;
            case KeyEvent.KEYCODE_NUMPAD_ADD: g_lights.FastForwardTime(g_eTimerMode, 1.0f); break;
            case KeyEvent.KEYCODE_T: g_bDrawCameraPos = !g_bDrawCameraPos; break;
            case KeyEvent.KEYCODE_1: g_eTimerMode = TimerTypes.TIMER_ALL; result.append("All"); break;
            case KeyEvent.KEYCODE_2: g_eTimerMode = TimerTypes.TIMER_SUN; result.append("Sun"); break;
            case KeyEvent.KEYCODE_3: g_eTimerMode = TimerTypes.TIMER_LIGHTS; result.append("Lights"); break;

            case KeyEvent.KEYCODE_D: SetupDaytimeLighting(); break;
            case KeyEvent.KEYCODE_N: SetupNighttimeLighting(); break;
            case KeyEvent.KEYCODE_H: SetupHDRLighting(); break;

            case KeyEvent.KEYCODE_I:
                Log.i("KeyEvent", "projData.cameraToClipMatrix" + projData.cameraToClipMatrix.toString());
                Log.i("KeyEvent", AnalysisTools.CalculateMatrixEffects(projData.cameraToClipMatrix));

                Log.i("KeyEvent", "sunModelToCameraMatrix = " + sunModelToCameraMatrix.toString());
                Log.i("KeyEvent", AnalysisTools.CalculateMatrixEffects(sunModelToCameraMatrix));

                Log.i("KeyEvent", "g_viewPole.CalcMatrix()" + g_viewPole_CalcMatrix.toString());
                Log.i("KeyEvent", AnalysisTools.CalculateMatrixEffects(g_viewPole_CalcMatrix));
                break;

            case KeyEvent.KEYCODE_SPACE:
                float sunAlpha = g_lights.GetSunTime();
                float sunTimeHours = sunAlpha * 24.0f + 12.0f;
                sunTimeHours = sunTimeHours > 24.0f ? sunTimeHours - 24.0f : sunTimeHours;
                int sunHours = (int)(sunTimeHours);
                float sunTimeMinutes = (sunTimeHours - sunHours) * 60.0f;
                int sunMinutes = (int)(sunTimeMinutes);
                Log.i("KeyEvent", "SunHours " + String.valueOf(sunHours) + " SunMinutes " + String.valueOf(sunMinutes));
                break;
            case KeyEvent.KEYCODE_S:
                if (renderSun)
                {
                    renderSun = false;
                }
                else
                {
                    renderSun = true;
                }
                break;
            case KeyEvent.KEYCODE_L:
                if (g_bDrawLights)
                {
                    g_bDrawLights = false;
                }
                else
                {
                    g_bDrawLights = true;
                }
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
            case KeyEvent.KEYCODE_E:
                updateDepth = true;
                break;
            case KeyEvent.KEYCODE_W:
                updateCullFace = true;
                break;
        }

        g_viewPole.CharPress((char)keyCode);
        return result.toString();
    }
}
