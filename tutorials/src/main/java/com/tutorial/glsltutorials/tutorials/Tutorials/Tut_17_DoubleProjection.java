package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

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
import com.tutorial.glsltutorials.tutorials.View.Framework;
import com.tutorial.glsltutorials.tutorials.View.MouseButtons;
import com.tutorial.glsltutorials.tutorials.View.ViewData;
import com.tutorial.glsltutorials.tutorials.View.ViewPole;
import com.tutorial.glsltutorials.tutorials.View.ViewScale;

import java.util.ArrayList;

/**
 * Created by jamie on 1/3/15.
 */
public class Tut_17_DoubleProjection extends TutorialBase {
    Vector3f translateVector = new Vector3f(0f, 0f, -10f);
    float scaleFactor = 0.02f;
    float reduceSpeed = 10f;
    static int NUMBER_OF_LIGHTS = 2;
    boolean rightMultiply;
    public Tut_17_DoubleProjection ()
    {
    }
    float g_fzNear = 1.0f;
    float g_fzFar = 1000.0f;

    int g_colorTexUnit = 0;

    ////////////////////////////////
    //View setup.
    static ViewData g_initialView = new ViewData
            (
                    new Vector3f(0.0f, 0.0f, 0.0f),
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

    static ViewData g_initPersView = new ViewData
            (
                    new Vector3f(0.0f, 0.0f, 0.0f),
                    new Quaternion(0.0f, 0.0f, 0.0f, 1.0f),
                    5.0f,
                    0.0f
            );

    static ViewScale g_initPersViewScale = new ViewScale
            (
                    0.05f, 10.0f,
                    0.1f, 0.05f,
                    4.0f, 1.0f,
                    90.0f/250.0f
            );

    ViewPole g_viewPole = new ViewPole(g_initialView, g_initialViewScale, MouseButtons.MB_LEFT_BTN);
    ViewPole g_persViewPole = new ViewPole(g_initPersView, g_initPersViewScale, MouseButtons.MB_RIGHT_BTN);

    public void MouseMotion(int x, int y)
    {
        Framework.forwardMouseMotion(g_viewPole, x, y);
        Framework.forwardMouseMotion(g_persViewPole, x, y);
    }

    public void MouseButton(int button, int state, int x, int y)
    {
        Framework.forwardMouseButton(g_viewPole, button, state, x, y);
        Framework.forwardMouseButton(g_persViewPole, button, state, x, y);
    }

    void MouseWheel(int wheel, int direction, int x, int y)
    {
        Framework.forwardMouseWheel(g_viewPole, wheel, direction, x, y);
    }

    FrameworkScene g_pScene = null;

    ArrayList<NodeRef> g_nodes = new ArrayList<NodeRef>();
    FrameworkTimer g_timer = new FrameworkTimer(FrameworkTimer.Type.TT_LOOP, 10.0f);

    UniformIntBinder g_lightNumBinder;

    int g_unlitModelToCameraMatrixUnif;
    int g_unlitCameraToClipMatrixUnif;
    int g_unlitObjectColorUnif;
    int g_unlitProg;

    int g_litCameraToClipMatrixUnif;
    int g_litProg;

    Mesh g_pSphereMesh;
    Quaternion g_spinBarOrient;

    void LoadAndSetupScene() throws Exception
    {

        FrameworkScene pScene = new FrameworkScene("dp_scene.xml");

        ArrayList<NodeRef> nodes = new ArrayList<NodeRef>();
        nodes.add(pScene.findNode("cube"));
        nodes.add(pScene.findNode("rightBar"));
        nodes.add(pScene.findNode("leaningBar"));
        nodes.add(pScene.findNode("spinBar"));
        g_lightNumBinder = new UniformIntBinder();
        AssociateUniformWithNodes(nodes, g_lightNumBinder, "numberOfLights");
        SetStateBinderWithNodes(nodes, g_lightNumBinder);

        int unlit = pScene.findProgram("p_unlit");
        int lit = pScene.findProgram("p_lit");
        Mesh pSphereMesh = pScene.FindMesh("m_sphere");

        //No more things that can throw.
        g_spinBarOrient = nodes.get(3).NodeGetOrient();

        g_unlitProg = unlit;
        GLES20.glUseProgram(unlit);
        g_unlitModelToCameraMatrixUnif = GLES20.glGetUniformLocation(unlit, "modelToCameraMatrix");
        g_unlitCameraToClipMatrixUnif  = GLES20.glGetUniformLocation(unlit, "cameraToClipMatrix");
        g_unlitObjectColorUnif = GLES20.glGetUniformLocation(unlit, "objectColor");
        GLES20.glUseProgram(0);

        g_litProg = lit;
        GLES20.glUseProgram(lit);
        g_litCameraToClipMatrixUnif  = GLES20.glGetUniformLocation(lit, "cameraToClipMatrix");
        GLES20.glUseProgram(0);

        g_nodes = nodes;
        g_pSphereMesh = pSphereMesh;

        g_pScene = pScene;
    }

    protected void init()
    {
        setupDepthAndCull();
        // FIXME GLES20.glEnable(GLES20.GL_FRAMEBUFFER_SRGB);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D); // must be before scene??
        try
        {
            LoadAndSetupScene();
        }
        catch(Exception ex)
        {
            Log.e("Double Projection ", "Failed to load scene: " + ex.toString());
        }
        //reshape();
        // FIXME ?? MatrixStack.rightMultiply = false;
        // FIXME ?? rightMultiply = true;
        clearColor = new Vector4f(0.8f, 0.8f, 0.8f, 1.0f);
    }

    boolean g_bDrawCameraPos = true;
    boolean g_bDepthClampProj = true;

    void BuildLights(Matrix4f camMatrix)
    {
        LightBlock lightData = new LightBlock(NUMBER_OF_LIGHTS);
        lightData.ambientIntensity = new Vector4f(0.2f, 0.2f, 0.2f, 1.0f);
        lightData.lightAttenuation = 1.0f / (5.0f * 5.0f);
        lightData.maxIntensity = 3.0f;
        lightData.lights[0].lightIntensity = new Vector4f(2.0f, 2.0f, 2.5f, 1.0f);
        lightData.lights[0].cameraSpaceLightPos =
                Vector4f.Transform(new Vector4f(-0.2f, 0.5f, 0.5f, 0.0f), camMatrix);
        lightData.lights[1].lightIntensity = (new Vector4f(3.5f, 6.5f, 3.0f, 1.0f)).Mult(1.2f);
        lightData.lights[1].cameraSpaceLightPos =
                Vector4f.Transform(new Vector4f(5.0f, 6.0f, 0.5f, 1.0f), camMatrix);

        g_lightNumBinder.SetValue(2);

        lightData.setUniforms(g_unlitProg);
        lightData.updateInternal();

        lightData.setUniforms(g_litProg);
        lightData.updateInternal();
    }

    public void display() throws Exception
    {
        if(g_pScene == null)
            return;

        g_timer.Update();
        
        clearDisplay();

        MatrixStack modelMatrix = new MatrixStack();
        modelMatrix.ApplyMatrix(g_viewPole.CalcMatrix());


        BuildLights(modelMatrix.Top());

        if (rightMultiply)
        {
            g_nodes.get(0).NodeSetOrient(Quaternion.fromAxisAngle(new Vector3f(0.0f, 1.0f, 0.0f),
                    360.0f * g_timer.GetAlpha() / reduceSpeed));
            g_nodes.get(3).NodeSetOrient(Quaternion.mult(g_spinBarOrient,
                    Quaternion.fromAxisAngle(new Vector3f(0.0f, 0.0f, 1.0f), 360.0f * g_timer.GetAlpha() / reduceSpeed)));
        }
        else
        {
            g_nodes.get(0).NodeSetOrient(Quaternion.fromAxisAngle(new Vector3f(0.0f, 1.0f, 0.0f),
                    360.0f * g_timer.GetAlpha() / reduceSpeed));
            g_nodes.get(3).NodeSetOrient(Quaternion.mult(
                    Quaternion.fromAxisAngle(new Vector3f(0.0f, 0.0f, 1.0f), 360.0f * g_timer.GetAlpha() / reduceSpeed),
                    g_spinBarOrient));
        }

        {
            MatrixStack persMatrix = new MatrixStack();
            persMatrix.perspective(60.0f, (width / 2f / height), g_fzNear, g_fzFar);

            // added
            persMatrix.translate(translateVector);
            persMatrix.Scale(scaleFactor);
            // end added

            ProjectionBlock projData = new ProjectionBlock();
            projData.cameraToClipMatrix = persMatrix.Top();

            GLES20.glUseProgram(g_unlitProg);
            GLES20.glUniformMatrix4fv(g_unlitCameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
            GLES20.glUseProgram(0);

            GLES20.glUseProgram(g_pScene.findProgram("p_lit"));
            GLES20.glUniformMatrix4fv(g_litCameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
            GLES20.glUseProgram(0);
        }

        GLES20.glViewport(0, 0, width/2, height);
        g_pScene.render(modelMatrix.Top());

        if(g_bDrawCameraPos)
        {
            try (PushStack pushstack = new PushStack(modelMatrix))//Draw lookat point.
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

        {
            MatrixStack persMatrix = new MatrixStack();
            Matrix4f applyMatrix = g_persViewPole.CalcMatrix();
            applyMatrix.SetRow3(new Vector4f(0f, 0f, 0f, 0f));
            applyMatrix.SetCol3(new Vector4f(0f, 0f, 0f, 0f));
            applyMatrix.M44 = 1f;
            persMatrix.ApplyMatrix(applyMatrix);
            persMatrix.perspective(60.0f, (width / 2f / height), g_fzNear, g_fzFar);

            // added
            persMatrix.translate(translateVector);
            persMatrix.Scale(scaleFactor);
            // end added

            ProjectionBlock projData = new ProjectionBlock();
            projData.cameraToClipMatrix = persMatrix.Top();

            GLES20.glUseProgram(g_unlitProg);
            GLES20.glUniformMatrix4fv(g_unlitCameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
            GLES20.glUseProgram(0);

            GLES20.glUseProgram(g_pScene.findProgram("p_lit"));
            GLES20.glUniformMatrix4fv(g_litCameraToClipMatrixUnif, 1, false, projData.cameraToClipMatrix.toArray(), 0);
            GLES20.glUseProgram(0);

        }

        // FIXME FIXME if(!g_bDepthClampProj)
        //    GLES20.glDisable(GLES20.DEPTH_CLAMP);
        GLES20.glViewport(width/2, 0, width/2, height);
        g_pScene.render(modelMatrix.Top());
        // FIXME FIXME GLES20.glEnable(EnableCap.DepthClamp);
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
                g_persViewPole.reset();
                break;
            case KeyEvent.KEYCODE_T:
                g_bDrawCameraPos = !g_bDrawCameraPos;
                break;
            case KeyEvent.KEYCODE_Y:
                g_bDepthClampProj = !g_bDepthClampProj;
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
                    Log.e("Double Projection ", "Failed to reload, due to: " + ex.toString());
                }
            }
            break;
        }

        g_viewPole.CharPress((char)keyCode);
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
