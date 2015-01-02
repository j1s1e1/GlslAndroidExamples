package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Framework;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.AnalysisTools;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.MouseButtons;
import com.tutorial.glsltutorials.tutorials.ObjectData;
import com.tutorial.glsltutorials.tutorials.ObjectPole;
import com.tutorial.glsltutorials.tutorials.ProjectionBlock;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;
import com.tutorial.glsltutorials.tutorials.ViewData;
import com.tutorial.glsltutorials.tutorials.ViewProvider;
import com.tutorial.glsltutorials.tutorials.ViewScale;

import java.io.InputStream;

/**
 * Created by Jamie on 6/7/14.
 */
public class Tut_09_Ambient_Lighting extends TutorialBase {

    static String TUTORIAL = "Ambient Lighting";
    static float g_fzNear = 1.0f;
    static float g_fzFar = 1000.0f;

    static boolean useUniformBuffers = false;
    boolean zeroAllMatrixes = false;

    // Debug registers
    Matrix4f groundPlaneModelMatrix = Matrix4f.Identity();
    Matrix4f coloredCylinderModelmatrix  = Matrix4f.Identity();
    Vector3f cylinderTraslation = new Vector3f();
    LitMatrixSphere2 lms2;

    class ProgramData
    {
        public int theProgram;

        public int positionAttribute;
        public int colorAttribute;
        public int normalAttribute;

        public int dirToLightUnif;
        public int lightIntensityUnif;
        public int ambientIntensityUnif;

        public int modelToCameraMatrixUnif;
        public int normalModelToCameraMatrixUnif;

        public int cameraToClipMatrixUnif;	// to avoid uniform buffer block
    };

    static ProgramData g_WhiteDiffuseColor;
    static ProgramData g_VertexDiffuseColor;
    static ProgramData g_WhiteAmbDiffuseColor;
    static ProgramData g_VertexAmbDiffuseColor;

    static int g_projectionBlockIndex = 2;

    ProgramData LoadProgram(String vertexShader, String fragmentShader)
    {
        ProgramData data = new ProgramData();
        int vertexShaderInt = Shader.loadShader30(GLES20.GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderInt = Shader.loadShader30(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        data.theProgram = Shader.createAndLinkProgram30(vertexShaderInt, fragmentShaderInt);

        data.positionAttribute = GLES20.glGetAttribLocation(data.theProgram, "position");

        if (data.positionAttribute != -1)
        {
            if (data.positionAttribute != 0)
            {
                Log.e(TUTORIAL, "These meshes only work with position at location 0 " + vertexShader);
            }
        }
        data.colorAttribute = GLES20.glGetAttribLocation(data.theProgram, "color");
        if (data.colorAttribute != -1)
        {
            if (data.colorAttribute != 1)
            {
                Log.e(TUTORIAL, "These meshes only work with color at location 1" + vertexShader);
            }
        }
        data.normalAttribute = GLES20.glGetAttribLocation(data.theProgram, "normal");


        data.modelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToCameraMatrix");
        data.normalModelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "normalModelToCameraMatrix");
        data.dirToLightUnif = GLES20.glGetUniformLocation(data.theProgram, "dirToLight");
        data.lightIntensityUnif = GLES20.glGetUniformLocation(data.theProgram, "lightIntensity");
        data.ambientIntensityUnif = GLES20.glGetUniformLocation(data.theProgram, "ambientIntensity");

        //FIX_THIS  int projectionBlock = GLES20.glGetUniformBlockIndex(data.theProgram, "Projection");
        //FIX_THIS  GLES20.glUniformBlockBinding(data.theProgram, projectionBlock, g_projectionBlockIndex);
        // to avoid uniform block
        data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");
        return data;
    }

    void InitializeProgram()
    {
        g_WhiteDiffuseColor = LoadProgram(VertexShaders.DirVertexLighting_PN_vert, FragmentShaders.ColorPassthrough_frag);
        g_VertexDiffuseColor = LoadProgram(VertexShaders.DirVertexLighting_PCN_vert, FragmentShaders.ColorPassthrough_frag);
        g_WhiteAmbDiffuseColor = LoadProgram(VertexShaders.DirAmbVertexLighting_PN_vert, FragmentShaders.ColorPassthrough_frag);
        g_VertexAmbDiffuseColor = g_WhiteAmbDiffuseColor;
        //g_VertexAmbDiffuseColor = LoadProgram(VertexShaders.DirAmbVertexLighting_PCN_vert, FragmentShaders.ColorPassthrough_frag);
    }

    static Mesh g_pCylinderMesh = null;
    static Mesh g_pPlaneMesh = null;

    ///////////////////////////////////////////////
    // View/Object Setup
    static ViewData g_initialViewData;

    private static void InitializeGInitialViewData()
    {
        g_initialViewData = new ViewData(new Vector3f(0.0f, 0.5f, 0.0f),
                new Quaternion(0.92387953f, 0.3826834f, 0.0f, 0.0f),
                5.0f,
                0.0f);
    }

    static ViewScale g_viewScale;

    private static void InitializeGViewScale()
    {
        g_viewScale = new ViewScale(
                3.0f, 20.0f,
                1.5f, 0.5f,
                0.0f, 0.0f,		//No camera movement.
                90.0f/250.0f);
    }

    public static ObjectData g_initialObjectData = new ObjectData(new Vector3f(0.0f, 0.5f, 0.0f),
            new Quaternion(1.0f, 0.0f, 0.0f, 0.0f));

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

    static int[] g_projectionUniformBuffer = new int[]{0};

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init() throws Exception
    {
        lms2 = new LitMatrixSphere2(0.2f);
        InitializeGInitialViewData();
        InitializeGViewScale();
        g_viewPole = new ViewProvider(g_initialViewData, g_viewScale, MouseButtons.MB_LEFT_BTN);
        g_objtPole = new ObjectPole(g_initialObjectData, (float)(90.0f / 250.0f),
                MouseButtons.MB_RIGHT_BTN, g_viewPole);


        InitializeProgram();

        try
        {
            InputStream  unitcylinder = Shader.context.getResources().openRawResource(R.raw.unitcylinder);
            g_pCylinderMesh = new Mesh(unitcylinder);
            InputStream  unitplane = Shader.context.getResources().openRawResource(R.raw.unitplane);
            g_pPlaneMesh = new Mesh(unitplane);
        }
        catch(Exception ex)
        {
            throw new Exception("Error:" + ex.toString());
        }

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CW);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthMask(true);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthRangef(0.0f, 1.0f);

        reshape();
    }

    static Vector4f g_lightDirection = new Vector4f(0.866f, 0.5f, 0.0f, 0.0f);

    static boolean g_bDrawColoredCyl = true;
    static boolean g_bShowAmbient = true;

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.

    public void display() throws Exception
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if((g_pPlaneMesh != null) && (g_pCylinderMesh != null))
        {
            MatrixStack modelMatrix = new MatrixStack();
            modelMatrix.SetMatrix(g_viewPole.CalcMatrix());

            Vector4f lightDirCameraSpace = Vector4f.Transform(g_lightDirection, modelMatrix.Top());

            ProgramData whiteDiffuse = g_bShowAmbient ? g_WhiteAmbDiffuseColor : g_WhiteDiffuseColor;
            ProgramData vertexDiffuse = g_bShowAmbient ? g_VertexAmbDiffuseColor : g_VertexDiffuseColor;

            if(g_bShowAmbient)
            {
                GLES20.glUseProgram(whiteDiffuse.theProgram);
                GLES20.glUniform4f(whiteDiffuse.lightIntensityUnif, 0.8f, 0.8f, 0.8f, 1.0f);
                GLES20.glUniform4f(whiteDiffuse.ambientIntensityUnif, 0.2f, 0.2f, 0.2f, 1.0f);
                GLES20.glUseProgram(vertexDiffuse.theProgram);
                GLES20.glUniform4f(vertexDiffuse.lightIntensityUnif, 0.8f, 0.8f, 0.8f, 1.0f);
                GLES20.glUniform4f(vertexDiffuse.ambientIntensityUnif, 0.2f, 0.2f, 0.2f, 1.0f);
            }
            else
            {
                GLES20.glUseProgram(whiteDiffuse.theProgram);
                GLES20.glUniform4f(whiteDiffuse.lightIntensityUnif, 1.0f, 1.0f, 1.0f, 1.0f);
                GLES20.glUseProgram(vertexDiffuse.theProgram);
                GLES20.glUniform4f(vertexDiffuse.lightIntensityUnif, 1.0f, 1.0f, 1.0f, 1.0f);
            }

            GLES20.glUseProgram(whiteDiffuse.theProgram);
            GLES20.glUniform3fv(whiteDiffuse.dirToLightUnif, 1, lightDirCameraSpace.toArray(), 0);
            GLES20.glUseProgram(vertexDiffuse.theProgram);
            GLES20.glUniform3fv(vertexDiffuse.dirToLightUnif, 1, lightDirCameraSpace.toArray(), 0);
            GLES20.glUseProgram(0);

            {
                //Render the ground plane.
                try( PushStack pushstack = new PushStack(modelMatrix))
                {
                    GLES20.glUseProgram(whiteDiffuse.theProgram);
                    modelMatrix.Translate(cylinderTraslation);
                    Matrix4f mm =  modelMatrix.Top();
                    groundPlaneModelMatrix = mm;
                    GLES20.glUniformMatrix4fv(whiteDiffuse.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                    //projData.cameraToClipMatrix = Matrix4f.Identity(); // Test
                    GLES20.glUniformMatrix4fv(whiteDiffuse.cameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
                    Matrix3f normMatrix = new Matrix3f(modelMatrix.Top());
                    normMatrix.Normalize();
                    GLES20.glUniformMatrix3fv(whiteDiffuse.normalModelToCameraMatrixUnif, 1, false,
                            normMatrix.toArray(), 0);
                    g_pPlaneMesh.render();
                    GLES20.glUseProgram(0);
                }

                //Render the Cylinder
                try(PushStack pushstack = new PushStack(modelMatrix))
                {
                    modelMatrix.ApplyMatrix(g_objtPole.CalcMatrix());
                    //modelMatrix.Translate(new Vector3f(0f, 0f, 12.5f));
                    coloredCylinderModelmatrix = modelMatrix.Top ();
                    if(g_bDrawColoredCyl)
                    {
                        GLES20.glUseProgram(vertexDiffuse.theProgram);
                        Matrix4f mm = modelMatrix.Top();
                        if (zeroAllMatrixes) mm = Matrix4f.Identity();
                        GLES20.glUniformMatrix4fv(vertexDiffuse.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                        GLES20.glUniformMatrix4fv(whiteDiffuse.cameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
                        Matrix3f normMatrix = new Matrix3f(modelMatrix.Top());
                        if (zeroAllMatrixes) normMatrix = Matrix3f.Identity();
                        GLES20.glUniformMatrix3fv(vertexDiffuse.normalModelToCameraMatrixUnif, 1, false, normMatrix.toArray(), 0);
                        g_pCylinderMesh.Render("lit-color");
                    }
                    else
                    {
                        GLES20.glUseProgram(whiteDiffuse.theProgram);
                        Matrix4f mm = modelMatrix.Top();
                        GLES20.glUniformMatrix4fv(whiteDiffuse.modelToCameraMatrixUnif, 1, false, mm.toArray(), 0);
                        GLES20.glUniformMatrix4fv(whiteDiffuse.cameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
                        Matrix3f  normMatrix = new Matrix3f(modelMatrix.Top());
                        GLES20.glUniformMatrix3fv(whiteDiffuse.normalModelToCameraMatrixUnif, 1, false, normMatrix.toArray(), 0);
                        g_pCylinderMesh.Render("lit");
                    }
                    GLES20.glUseProgram(0);
                }
            }
        }
        lms2.setOffset(cylinderTraslation);
        lms2.draw();
    }

    static ProjectionBlock projData = new ProjectionBlock();

    //Called whenever the window is resized. The new window size is given, in pixels.
    //This is an opportunity to call glViewport or glScissor to keep up with the change in size.
    public void reshape ()
    {
        MatrixStack persMatrix = new MatrixStack();
        persMatrix.Perspective(45.0f, (width / (float)height), g_fzNear, g_fzFar);
        projData.cameraToClipMatrix = persMatrix.Top();
        GLES20.glViewport(0, 0, width, height);
    }

    //Called whenever a key on the keyboard was pressed.
    //The key is given by the ''key'' parameter, which is in ASCII.
    //It's often a good idea to have the escape key (ASCII value 27) call glutLeaveMainLoop() to 
    //exit the program.
    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_ESCAPE:
                //timer.Enabled = false;
                break;
            case KeyEvent.KEYCODE_SPACE:
                g_bDrawColoredCyl = !g_bDrawColoredCyl;
                if (g_bDrawColoredCyl)
                    result.append("Colored Cylinder On.\n");
                else
                    result.append("Colored Cylinder Off.\n");
                break;
            case KeyEvent.KEYCODE_T:
                g_bShowAmbient = !g_bShowAmbient;
                if(g_bShowAmbient)
                    result.append("Ambient Lighting On.\n");
                else
                    result.append("Ambient Lighting Off.\n");

                break;
            case KeyEvent.KEYCODE_INFO:
                Log.e(TUTORIAL, "cameraToClipMatrix = " + projData.cameraToClipMatrix.toString());
                Log.e(TUTORIAL, "coloredCylinderModelmatrix = " + coloredCylinderModelmatrix.toString());
                Matrix4f multiply = Matrix4f.Mult(projData.cameraToClipMatrix, coloredCylinderModelmatrix);
                Log.e(TUTORIAL, AnalysisTools.CalculateMatrixEffects(multiply));


                Log.e(TUTORIAL, "Ground Plane Model Matrix = " + groundPlaneModelMatrix.toString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_7:
                cylinderTraslation.addNoCopy(new Vector3f(0.0f, 0.0f, 0.1f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_8:
                cylinderTraslation.addNoCopy(new Vector3f(0.0f, 0.1f, 0.0f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_9:
                cylinderTraslation.addNoCopy(new Vector3f(0.0f, 0.0f, 0.1f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_4:
                cylinderTraslation.addNoCopy(new Vector3f(-0.1f, 0.0f, 0.0f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_5:

                break;
            case KeyEvent.KEYCODE_NUMPAD_6:
                cylinderTraslation.addNoCopy(new Vector3f(0.1f, 0.0f, 0.0f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_1:
                cylinderTraslation.addNoCopy(new Vector3f(0.0f, 0.0f, -0.1f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_2:
                cylinderTraslation.addNoCopy(new Vector3f(0.0f, -0.1f, 0.0f));
                break;
            case KeyEvent.KEYCODE_NUMPAD_3:
                cylinderTraslation.addNoCopy(new Vector3f(0.0f, 0.0f, -0.1f));
                break;
        }
        result.append(keyCode);
        reshape();
        return result.toString();
    }

    public void touchEvent(int x_position, int y_position) throws Exception
    {
        int selectionX = x_position / (width / 7);
        int selectoinY = y_position / (height / 4);
        switch (selectoinY) {
            case 0:
                switch (selectionX) {
                    case 0:
                        cylinderTraslation.addNoCopy(new Vector3f(0.1f, 0.0f, 0.0f));
                        break;
                    case 1:
                        cylinderTraslation.addNoCopy(new Vector3f(-0.1f, 0.0f, 0.0f));
                        break;
                    case 2:
                        cylinderTraslation.addNoCopy(new Vector3f(0.0f, 0.1f, 0.0f));
                        break;
                    case 3:
                        cylinderTraslation.addNoCopy(new Vector3f(0.0f, -0.1f, 0.0f));
                        break;
                    case 4:
                        cylinderTraslation.addNoCopy(new Vector3f(0.0f, 0.0f, 0.1f));
                        break;
                    case 5:
                        cylinderTraslation.addNoCopy(new Vector3f(0.0f, 0.0f, -0.1f));
                        break;
                    case 6:
                        cylinderTraslation = new Vector3f(0.0f, 0.0f, 0.0f);
                        break;
                }
                break;
            case 3: {
                if (zeroAllMatrixes)
                {
                    zeroAllMatrixes = false;
                }
                else
                {
                    zeroAllMatrixes = true;
                }
                break;
            }
            default:
                break;
        }
    }

}
