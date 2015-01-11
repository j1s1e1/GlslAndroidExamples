package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.AnalysisTools;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Light.LightBlock;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.ProgramData.ProgramSet;
import com.tutorial.glsltutorials.tutorials.ProgramData.ProgramSets;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.View.MouseButtons;
import com.tutorial.glsltutorials.tutorials.View.ViewData;
import com.tutorial.glsltutorials.tutorials.View.ViewPole;
import com.tutorial.glsltutorials.tutorials.View.ViewScale;

import java.util.ArrayList;

/**
 * Created by jamie on 1/2/15.
 */
public class Tut_Projected_Light_Test extends TutorialBase {
    boolean updateLightBlock = false;
    boolean initializationComplete = false;
    static int NUMBER_OF_LIGHTS = 2;
    boolean renderWithString = false;
    ArrayList<String> renderStrings = new ArrayList<String>();
    int renderString = 0;
    Vector3f initialScale = new Vector3f(50f, 50f, 50f);
    Vector3f scaleFactor = new Vector3f(10f, 10f, 10f);
    Vector3f translateVector = new Vector3f(0f, 0f, 0f);
    boolean allLightsTogether = false;
    Vector3f cameraSpaceProjLightPos= new Vector3f(0.0f, 0.0f, 1.0f);
    Vector3f greenLightPos= new Vector3f(0.0f, 0.0f, 1.0f);
    Vector3f redLightPos= new Vector3f(0.0f, 0.0f, 1.0f);
    Vector4f ambientIntensity = new Vector4f(0.1f, 0.1f, 0.1f, 1.0f);
    Vector4f redLight = new Vector4f(0.3f, 0.0f, 0.0f, 1.0f);
    Vector4f greenLight = new Vector4f(0.0f, 0.5f, 0.0f, 1.0f);

    Matrix4f modelToCameraMatrix = Matrix4f.Identity();
    Matrix4f cameraToLightProjMatrix = Matrix4f.Identity();

    MatrixStack cameraMatrixStack = new MatrixStack();

    class ProgramData
    {
        public String name = "";
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

        public int diffuseColorTexUnif;
        public int lightProjTexUnif;
        public int cameraToLightProjMatrixUnif;
        public int cameraSpaceProjLightPosUnif;

        public int[] attribLocations;

        public String toString()
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

    int currentProgram = 0;
    ArrayList<ProgramData> programs = new ArrayList<ProgramData>();

    Vector3f dirToLight = new Vector3f(0.0f, 0.0f, 1f);

    float perspectiveAngle = 60f;
    float newPerspectiveAngle = 60f;

    ProgramData LoadProgram(String programSetString)
    {
        ProgramSet programSet = ProgramSets.Find(programSetString);
        ProgramData data = new ProgramData();
        data.attribLocations = new int[] { -1, -1, -1};
        data.name = programSet.name;
        int vertex_shader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, programSet.vertexShader);
        int fragment_shader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, programSet.fragmentShader);
        data.theProgram  = Shader.createAndLinkProgram(vertex_shader, fragment_shader);

        data.attribLocations[0]  = GLES20.glGetAttribLocation(data.theProgram, "position");
        data.attribLocations[1]  = GLES20.glGetAttribLocation(data.theProgram, "color");

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
        data.normalAttribute = GLES20.glGetAttribLocation(data.theProgram, "normal");

        data.diffuseColorTexUnif = GLES20.glGetUniformLocation(data.theProgram, "diffuseColorTex");
        data.lightProjTexUnif = GLES20.glGetUniformLocation(data.theProgram, "lightProjTex");
        data.cameraToLightProjMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToLightProjMatrix");
        data.cameraSpaceProjLightPosUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraSpaceProjLightPos");

        return data;
    }

    private void UpdateLightBlock()
    {
        programs.get(currentProgram).lightBlock.ambientIntensity = ambientIntensity;

        programs.get(currentProgram).lightBlock.lights[0].cameraSpaceLightPos = new Vector4f(1.0f, 0.0f, 1.0f, -1.0f);
        programs.get(currentProgram).lightBlock.lights[0].lightIntensity = redLight;

        programs.get(currentProgram).lightBlock.lights[1].cameraSpaceLightPos = new Vector4f(0.0f, 1.0f, 1.0f, -1.0f);
        programs.get(currentProgram).lightBlock.lights[1].lightIntensity = greenLight;

        programs.get(currentProgram).lightBlock.maxIntensity = 0.5f;

        if (allLightsTogether)
        {
            programs.get(currentProgram).lightBlock.lights[0].cameraSpaceLightPos = new Vector4f(cameraSpaceProjLightPos, 1.0f);
            programs.get(currentProgram).lightBlock.lights[1].cameraSpaceLightPos = new Vector4f(cameraSpaceProjLightPos, 1.0f);
        }

        programs.get(currentProgram).lightBlock.updateInternal();
    }

    void InitializeProgram()
    {
        ProgramData projlight = LoadProgram("projlight");

        // Test shader lights and materials
        GLES20.glUseProgram(projlight.theProgram);
        projlight.lightBlock = new LightBlock(NUMBER_OF_LIGHTS);
        projlight.lightBlock.setUniforms(projlight.theProgram);
        programs.add(projlight);
        UpdateLightBlock();
    }
    int currentMesh = 0;
    ArrayList<Mesh> meshes = new ArrayList<Mesh>();

    int currentTexture;
    int lightTexture;
    private void CreateTextures()
    {
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        currentTexture = Textures.createMipMapTexture(Shader.context, R.drawable.wood4_rotate);
        lightTexture = Textures.loadTexture(Shader.context, R.drawable.flashlight, false);
    }

    protected void init() throws Exception
    {
        InitializeProgram();
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        CreateTextures();
        try
        {
            meshes.add(new Mesh("sceneunitcube.xml"));
            meshes.add(new Mesh("unitcubecolor.xml"));
            meshes.add(new Mesh("unitcylinder.xml"));
            meshes.add(new Mesh("unitplane.xml"));
            meshes.add(new Mesh("infinity.xml"));
            meshes.add(new Mesh("unitsphere12.xml"));
            meshes.add(new Mesh("unitcylinder9.xml"));
            meshes.add(new Mesh("unitdiorama.xml"));
            meshes.add(new Mesh("ground.xml"));
            renderStrings.add("lit");
            renderStrings.add("lit-color");
            renderStrings.add("color");
            renderStrings.add("lit-tex");
            renderStrings.add("lit-color-tex");
            renderStrings.add("color-tex");
            renderStrings.add("tex");
            renderStrings.add("flat");
        } catch (Exception ex) {
            throw new Exception("Error " + ex.toString());
        }

        setupDepthAndCull();

        cameraMatrixStack.Reset();
        cameraMatrixStack.Translate(0.5f, 0.5f, 0.0f);
        initializationComplete = true;
    }

    static ViewData g_initialView = new ViewData
            (
                    new Vector3f(0.0f, 0.0f, 1.0f),
                    Quaternion.fromAxisAngle(new Vector3f(0f, 0f, 1f), 0f),
                    25.0f,
                    180.0f
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
                    new Quaternion(1.0f, 0.0f, 0.0f, 0.0f),
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

    Matrix4f cameraMatrix;

    public void display() throws Exception
    {
        if (initializationComplete == false)
            return;
        clearDisplay();

        cameraMatrix = g_viewPole.CalcMatrix();
        Matrix4f lightView = g_lightViewPole.CalcMatrix();

        if (meshes.get(currentMesh) != null)
        {
            MatrixStack modelMatrix = new MatrixStack();
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.rotate(axis, angle);   // rotate last to leave in place
                //modelMatrix.Translate(Camera.g_camTarget);
                //modelMatrix.Scale(initialScale.X / scaleFactor.X, 
                //	initialScale.Y / scaleFactor.Y,
                //	initialScale.Z / scaleFactor.Z);

                GLES20.glUseProgram(programs.get(currentProgram).theProgram);

                GLES20.glUniformMatrix4fv(programs.get(currentProgram).cameraToLightProjMatrixUnif, 1, 
                        false, cameraToLightProjMatrix.toArray(), 0);

                GLES20.glUniform3fv(programs.get(currentProgram).cameraSpaceProjLightPosUnif, 1,
                        cameraSpaceProjLightPos.toArray(), 0);


                GLES20.glUniform1i(programs.get(currentProgram).diffuseColorTexUnif, 0);
                GLES20.glUniform1i(programs.get(currentProgram).lightProjTexUnif, 1);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, currentTexture);

                GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, lightTexture);

                cameraMatrix = cameraMatrixStack.Top();

                modelToCameraMatrix = Matrix4f.mul(Matrix4f.Identity(), cameraMatrix);
                GLES20.glUniformMatrix4fv(programs.get(currentProgram).modelToCameraMatrixUnif, 1, 
                        false, modelToCameraMatrix.toArray(), 0);
                if (programs.get(currentProgram).normalModelToCameraMatrixUnif != 0)
                {
                    Matrix3f normalModelToCameraMatrix = Matrix3f.Identity();
                    Matrix4f applyMatrix = Matrix4f.mul(Matrix4f.Identity(),
                            Matrix4f.createTranslation(dirToLight));
                    normalModelToCameraMatrix = new Matrix3f(applyMatrix);
                    normalModelToCameraMatrix.invert();
                    GLES20.glUniformMatrix3fv(programs.get(currentProgram).normalModelToCameraMatrixUnif, 1, false,
                           normalModelToCameraMatrix.toArray(), 0);
                    Matrix4f cameraToClipMatrix = Matrix4f.Identity();
                    GLES20.glUniformMatrix4fv(programs.get(currentProgram).cameraToClipMatrixUnif, 1, 
                            false,cameraToClipMatrix.toArray(), 0);

                }
            }
            if (renderWithString)
            {
                try
                {
                    meshes.get(currentMesh).render(programs.get(currentProgram).attribLocations, renderStrings.get(renderString));
                }
                catch (Exception ex)
                {
                    renderWithString = false;
                    Log.e("Render","Error displaying mesh wih render String " + renderStrings.get(renderString) + " " + ex.toString());
                }
            }
            else
            {
                meshes.get(currentMesh).render(programs.get(currentProgram).attribLocations);
            }
            GLES20.glUseProgram(0);
            if (perspectiveAngle != newPerspectiveAngle)
            {
                perspectiveAngle = newPerspectiveAngle;
                reshape();
            }
        }
        if (updateLightBlock) {
            UpdateLightBlock();
            updateLightBlock = false;
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

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        switch (keyCode) {
            case KeyEvent.KEYCODE_NUMPAD_6:
                cameraMatrixStack.translate(new Vector3f(0.1f, 0.0f, 0.0f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_4:
                cameraMatrixStack.translate(new Vector3f(-0.1f, 0.0f, 0.0f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_8:
                cameraMatrixStack.translate(new Vector3f(0.0f, 0.1f, 0.0f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_2:
                cameraMatrixStack.translate(new Vector3f(0.0f, -0.1f, 0.0f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_7:
                cameraMatrixStack.translate(new Vector3f(0.0f, 0.0f, 0.1f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_3:
                cameraMatrixStack.translate(new Vector3f(0.0f, 0.0f, -0.1f));
                break;
            case KeyEvent.KEYCODE_1:
                cameraMatrixStack.RotateX(5f);
                break;
            case KeyEvent.KEYCODE_2:
                cameraMatrixStack.RotateY(5f);
                break;
            case KeyEvent.KEYCODE_3:
                cameraMatrixStack.RotateZ(5f);
                break;
            case KeyEvent.KEYCODE_8:
                ambientIntensity.w = 1f;
                ambientIntensity = ambientIntensity.add(new Vector4f(0.1f, 0.1f, 0.1f, 0.0f));
                if (ambientIntensity.x > 0.8f)
                {
                    ambientIntensity = new Vector4f(0f, 0f, 0f, 1.0f);
                }
                Log.i("KeyEvent", "ambientIntensity = " + ambientIntensity.toString());
                updateLightBlock = true;;
                break;
            case KeyEvent.KEYCODE_9:
                redLight.w = 1f;
                redLight = redLight.add(new Vector4f(0.1f, 0.0f, 0.0f, 0.0f));
                if (redLight.x > 0.8f)
                {
                    redLight = new Vector4f(0f, 0f, 0f, 1.0f);
                }
                result.append("redLight = " + redLight.toString());
                updateLightBlock = true;
                break;
            case KeyEvent.KEYCODE_0:
                greenLight.w = 1f;
                greenLight = greenLight.add(new Vector4f(0.0f, 0.1f, 0.0f, 0.0f));
                if (greenLight.y > 0.8f)
                {
                    greenLight = new Vector4f(0f, 0f, 0f, 1.0f);
                }
                Log.i("KeyEvent", "greenLight = " + greenLight.toString());
                updateLightBlock = true;
                break;

            case KeyEvent.KEYCODE_V:
                newPerspectiveAngle = perspectiveAngle + 5f;
                if (newPerspectiveAngle > 120f)
                {
                    newPerspectiveAngle = 30f;
                }
                break;

            case KeyEvent.KEYCODE_S:
                renderWithString = true;
                renderString++;
                if (renderString > renderStrings.size() - 1) renderString = 0;
                result.append("Render String = " + renderStrings.get(renderString));
                break;
            case KeyEvent.KEYCODE_M:
                renderWithString = false;
                currentMesh++;
                if (currentMesh > meshes.size() - 1) currentMesh = 0;
                Log.i("KeyEvent", "Mesh = " + meshes.get(currentMesh).fileName);
                break;
            case KeyEvent.KEYCODE_P:
                currentProgram++;
                if (currentProgram > programs.size() - 1) currentProgram = 0;
                Log.i("KeyEvent", "Program = " + programs.get(currentProgram).name);
                break;
            case KeyEvent.KEYCODE_O:
                scaleFactor = meshes.get(currentMesh).getUnitScaleFactor();
                Log.i("KeyEvent", scaleFactor.toString());
                break;
            case KeyEvent.KEYCODE_X:
                cameraSpaceProjLightPos.x += 1f;
                if (cameraSpaceProjLightPos.x > 10f) cameraSpaceProjLightPos.x = -10f;
                break;
            case KeyEvent.KEYCODE_Y:
                cameraSpaceProjLightPos.y += 1f;
                if (cameraSpaceProjLightPos.y > 10f) cameraSpaceProjLightPos.y = -10f;
                break;
            case KeyEvent.KEYCODE_Z:
                cameraSpaceProjLightPos.z += 1f;
                if (cameraSpaceProjLightPos.z > 10f) cameraSpaceProjLightPos.z = -10f;
                break;
            case KeyEvent.KEYCODE_I:
                Log.i("KeyEvent", "CameraMatrix " + cameraMatrix.toString());
                Log.i("KeyEvent", AnalysisTools.CalculateMatrixEffects(cameraMatrix));
                break;
            case KeyEvent.KEYCODE_C:
                GLES20.glFrontFace(GLES20.GL_CW);
                Log.i("KeyEvent", "FrontFaceDirection.Cw");
                break;
            case KeyEvent.KEYCODE_D:
                GLES20.glFrontFace(GLES20.GL_CCW);
                Log.i("KeyEvent", "FrontFaceDirection.Ccw");
                break;
            case KeyEvent.KEYCODE_E:
                if (allLightsTogether)
                {
                    allLightsTogether = false;
                }
                else
                {
                    allLightsTogether = true;
                }
                break;
        }

        reshape();
        return result.toString();
    }
}
