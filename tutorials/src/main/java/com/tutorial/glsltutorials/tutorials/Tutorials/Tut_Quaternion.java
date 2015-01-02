package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Camera;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.ProgramData.ProgramSet;
import com.tutorial.glsltutorials.tutorials.ProgramData.ProgramSets;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.Text.TextClass;

import java.util.ArrayList;

/**
 * Created by jamie on 1/1/15.
 */
public class Tut_Quaternion extends TutorialBase {
    Vector3f cubeScaleFactor = new Vector3f(0.1f, 1f, 0.1f);
    Vector3f cylinderScaleFactor = new Vector3f(0.2f, 0.4f, 0.2f);
    Quaternion quaternion = new Quaternion(0f, 0f, 0f, 1f);
    float quaternionElement = 1f;

    TextClass quaternionText;
    TextClass axisAngleText;

    boolean staticText = true;
    boolean reverseRotation = true;
    Vector3f textOffset = new Vector3f(-0.9f, -0.8f, 0f);
    Vector3f textOffset2 = new Vector3f(-0.9f, 0.8f, 0f);

    int cube = 0;
    int cylinder = 1;
    boolean updateText = false;

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
    };

    int currentProgram = 0;
    ArrayList<ProgramData> programs = new ArrayList<ProgramData>();

    float perspectiveAngle = 60f;
    float newPerspectiveAngle = 60f;

    ProgramData loadProgram(String programSetString)
    {
        ProgramSet programSet = ProgramSets.Find(programSetString);
        ProgramData data = new ProgramData();
        data.name = programSet.name;
        int vertex_shader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, programSet.vertexShader);
        int fragment_shader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, programSet.fragmentShader);
        data.theProgram  = Shader.createAndLinkProgram(vertex_shader, fragment_shader);

        data.positionAttribute = GLES20.glGetAttribLocation(data.theProgram, "position");
        data.colorAttribute = GLES20.glGetAttribLocation(data.theProgram, "color");
        if (data.positionAttribute != -1)
        {
            if (data.positionAttribute != 0)
            {
                Log.e("LoadProgram", "These meshes only work with position at location 0 " + programSet.vertexShader);
            }
        }
        if (data.colorAttribute != -1)
        {
            if (data.colorAttribute != 1)
            {
                Log.e("LoadProgram", "These meshes only work with color at location 1" + programSet.vertexShader);
            }
        }

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

        return data;
    }

    void InitializeProgram()
    {
        ProgramData ObjectPositionColor = loadProgram("ObjectPositionColor");
        programs.add(ObjectPositionColor);
        ProgramData UniformColor = loadProgram("PosOnlyWorldTransform_vert ColorUniform_frag");
        programs.add(UniformColor);
    }
    int currentMesh = 0;
    ArrayList<Mesh> meshes = new ArrayList<Mesh>();

    private String QuaternionString()
    {
        return quaternion.toString();
    }

    private String AxisAngeString()
    {
        return "Axis " + axis.toString() + " Angle " + String.format("%.3f" , angle * 180f / (float)Math.PI);
    }

    protected void init() throws Exception
    {
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDisable(GLES20.GL_ALPHA);
        quaternionText = new TextClass(QuaternionString(), 0.5f, 0.05f, staticText, reverseRotation);
        quaternionText.setOffset(textOffset);

        axisAngleText = new TextClass(AxisAngeString(), 0.4f, 0.04f, staticText, reverseRotation);
        axisAngleText.setOffset(textOffset2);

        InitializeProgram();
        try
        {
            meshes.add(new Mesh("unitcubecolor.xml"));
            meshes.add(new Mesh("unitcylinder.xml"));
        } catch (Exception ex) {
            throw new Exception("Error " + ex.toString());
        }

        setupDepthAndCull();
        reshape();
        axis = quaternion.getAxis();
        angle = quaternion.getAngle();
        quaternionText.UpdateText(QuaternionString());
        axisAngleText.UpdateText(AxisAngeString());
        GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
        GLES20.glBlendFunc(GLES20.GL_BLEND_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

    }

    Vector3f axis = new Vector3f();
    float angle = 0;

    public void display() throws Exception
    {
        clearDisplay();
        // FIXME check GLES20.glFrontFace(GLES20.GL_CW);
        quaternionText.draw();
        axisAngleText.draw();
        // FIXME check GLES20.glFrontFace(GLES20.GL_CCW);
        if (meshes.get(currentMesh) != null)
        {
            MatrixStack modelMatrix = new MatrixStack();
            modelMatrix.Scale(0.8f);
            modelMatrix.Translate(0.0f, 0.0f, 0f);
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.Scale(cubeScaleFactor);
                GLES20.glUseProgram(programs.get(currentProgram).theProgram);
                modelMatrix.Rotate(axis, angle * 180f / (float)Math.PI);
                Matrix4f mm = modelMatrix.Top();

                GLES20.glUniformMatrix4fv(programs.get(currentProgram).modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
                if (programs.get(currentProgram).baseColorUnif != -1)
                {
                    GLES20.glUniform4f(programs.get(currentProgram).baseColorUnif, 0.5f, 0.5f, 0f, 1.0f);
                }
                meshes.get(cube).render();
            }

            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.Scale(cylinderScaleFactor);
                modelMatrix.Translate(new Vector3f(0f, 0.4f, 0.0f));
                GLES20.glUseProgram(programs.get(currentProgram).theProgram);

                axis = quaternion.getAxis();
                angle = quaternion.getAngle();
                modelMatrix.Rotate(axis, angle * 180f / (float)Math.PI);
                Matrix4f mm = modelMatrix.Top();

                GLES20.glUniformMatrix4fv(programs.get(currentProgram).modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
                if (programs.get(currentProgram).baseColorUnif != -1)
                {
                    GLES20.glUniform4f(programs.get(currentProgram).baseColorUnif, 0.0f, 0.5f, 0.5f, 1.0f);
                }
                meshes.get(cylinder).render();
            }

            GLES20.glUseProgram(0);
            if (perspectiveAngle != newPerspectiveAngle)
            {
                perspectiveAngle = newPerspectiveAngle;
                reshape();
            }
        }
        if (updateText)
        {
            axis = quaternion.getAxis();
            angle = quaternion.getAngle();
            quaternionText.UpdateText(QuaternionString());
            axisAngleText.UpdateText(AxisAngeString());
            updateText = false;
        }
    }

    static Matrix4f pm;
    static Matrix4f cm;

    static private void SetGlobalMatrices(ProgramData program)
    {
        GLES20.glUseProgram(program.theProgram);
        GLES20.glUniformMatrix4fv(program.cameraToClipMatrixUnif, 1, false, pm.toArray(), 0);  // this one is first
        GLES20.glUniformMatrix4fv(program.worldToCameraMatrixUnif, 1, false, cm.toArray(), 0); // this is the second one
        GLES20.glUseProgram(0);
    }

    public void reshape()
    {
        MatrixStack camMatrix = new MatrixStack();
        cm = camMatrix.Top();

        MatrixStack persMatrix = new MatrixStack();
        pm = persMatrix.Top();

        SetGlobalMatrices(programs.get(currentProgram));

        GLES20.glViewport(0, 0, width, height);

    }

    public String keyboard(int keyCode, int x, int y)
    {
        Quaternion rotateX;
        Quaternion rotateY;
        Quaternion rotateZ;
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        switch (keyCode) {
            case KeyEvent.KEYCODE_NUMPAD_6:
                Camera.MoveTarget(0.5f, 0f, 0.0f);
                Log.i("KeyEvent", Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_4:
                Camera.MoveTarget(-0.5f, 0f, 0.0f);
                Log.i("KeyEvent", Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_8:
                Camera.MoveTarget(0.0f, 0.5f, 0.0f);
                Log.i("KeyEvent", Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_2:
                // FIXME ??Camera.MoveTarget(0f, -0.5f, 0.0f);
                // FIXME ??Log.i("KeyEvent", (Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_7:
                Camera.MoveTarget(0.0f, 0.0f, 0.5f);
                Log.i("KeyEvent", Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_3:
                Camera.MoveTarget(0f, 0.0f, -0.5f);
                Log.i("KeyEvent", Camera.GetTargetString());
                break;
            case KeyEvent.KEYCODE_1:
                rotateX = Quaternion.fromAxisAngle(new Vector3f(1f, 0f, 0f), 5f * (float)Math.PI / 180f);
                quaternion = quaternion.mul(rotateX);
                break;
            case KeyEvent.KEYCODE_2:
                rotateY = Quaternion.fromAxisAngle(new Vector3f(0f, 1f, 0f), 5f * (float)Math.PI / 180f);
                quaternion = quaternion.mul(rotateY);
                break;
            case KeyEvent.KEYCODE_3:
                rotateZ = Quaternion.fromAxisAngle(new Vector3f(0f, 0f, 1f), 5f * (float)Math.PI / 180f);
                quaternion = quaternion.mul(rotateZ);
                break;
            case KeyEvent.KEYCODE_4:
                rotateX = Quaternion.fromAxisAngle(new Vector3f(1f, 0f, 0f), -5f * (float)Math.PI / 180f);
                quaternion = quaternion.mul(rotateX);
                break;
            case KeyEvent.KEYCODE_5:
                rotateY = Quaternion.fromAxisAngle(new Vector3f(0f, 1f, 0f), -5f * (float)Math.PI / 180f);
                quaternion = quaternion.mul(rotateY);
                break;
            case KeyEvent.KEYCODE_6:
                rotateZ = Quaternion.fromAxisAngle(new Vector3f(0f, 0f, 1f), -5f * (float)Math.PI / 180f);
                quaternion = quaternion.mul(rotateZ);
                break;
            case KeyEvent.KEYCODE_V:
                newPerspectiveAngle = perspectiveAngle + 5f;
                if (newPerspectiveAngle > 120f)
                {
                    newPerspectiveAngle = 30f;
                }
                break;
            case KeyEvent.KEYCODE_M:
                currentMesh++;
                if (currentMesh > meshes.size() - 1) currentMesh = 0;
                // FIXME addfield Log.i("KeyEvent", "Mesh = " + meshes.get(currentMesh).fileName);
                break;
            case KeyEvent.KEYCODE_P:
                currentProgram++;
                if (currentProgram > programs.size() - 1) currentProgram = 0;
                result.append("Program = " + programs.get(currentProgram).name);
                reshape();
                break;
            case KeyEvent.KEYCODE_Q:
                Log.i("KeyEvent", "currentProgram = " + String.valueOf(currentProgram));
                break;
            case KeyEvent.KEYCODE_I:
                Log.i("KeyEvent", "Quaternion = " + quaternion.toString());
                Log.i("KeyEvent", "axis = " + axis.toString());
                Log.i("KeyEvent", "angle = " + Float.toString(angle));
                break;
            case KeyEvent.KEYCODE_O:
                // FIXME addfunction cubeScaleFactor = meshes.get(currentMesh).getUnitScaleFactor();
                Log.i("KeyEvent", cubeScaleFactor.toString());
                break;
            case KeyEvent.KEYCODE_X:
                quaternion = new Quaternion(quaternionElement, 0f, 0f, 0f);
                break;
            case KeyEvent.KEYCODE_Y:
                quaternion = new Quaternion(0f, quaternionElement, 0f, 0f);
                break;
            case KeyEvent.KEYCODE_Z:
                quaternion = new Quaternion(0f, 0f, quaternionElement, 0f);
                break;
            case KeyEvent.KEYCODE_W:
                quaternion = new Quaternion(0f, 0f, 0f, quaternionElement);
                break;
            case KeyEvent.KEYCODE_NUMPAD_ADD:
                quaternionElement += 0.1f;
                Log.i("KeyEvent", "quaternionElement = " + String.valueOf(quaternionElement));
                break;
            case KeyEvent.KEYCODE_NUMPAD_SUBTRACT:
                quaternionElement -= 0.1f;
                Log.i("KeyEvent", "quaternionElement = " + String.valueOf(quaternionElement));
                break;
            case KeyEvent.KEYCODE_C:
                GLES20.glFrontFace(GLES20.GL_CW);
                Log.i("KeyEvent", "FrontFaceDirection.Cw");
                break;
            case KeyEvent.KEYCODE_D:
                GLES20.glFrontFace(GLES20.GL_CCW);
                Log.i("KeyEvent", "FrontFaceDirection.Ccw");
                break;
            case KeyEvent.KEYCODE_R:
                // FIXME MatrixStack.rightMultiply = true;
                break;
            case KeyEvent.KEYCODE_L:
                // FIXME MatrixStack.rightMultiply = false;
                break;
        }
        updateText = true;
        return result.toString();
    }
}
