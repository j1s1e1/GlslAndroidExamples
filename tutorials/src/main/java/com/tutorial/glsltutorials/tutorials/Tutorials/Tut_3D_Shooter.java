package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Blender.Blender;
import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Objects.Missle;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Text.TextClass;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by jamie on 10/18/14.
 */
public class Tut_3D_Shooter extends TutorialBase {
    public Tut_3D_Shooter ()
    {
    }

    Blender ship;
    ArrayList<Missle> missles = new ArrayList<Missle>();
    boolean addMissle = false;

    TextClass controls;
    boolean staticText = true;
    boolean updateText = false;

    TextClass axis_info;
    TextClass up_info;
    TextClass right_info;
    TextClass infoEnable;
    boolean enableInfo = false;
    int enableInfoDebounce = 0;

    double anglehorizontal = 0;
    double anglevertical = 0;

    Vector3f axis = new Vector3f(0f, 0f, 1f);
    Vector3f up = new Vector3f(0f, 0.125f, 0f);
    Vector3f right = new Vector3f(0.125f, 0f, 0f);

    int defaultShader;
    int shaderFragWhiteDiffuseColor;

    Vector3f initialScale = new Vector3f(0.05f, 0.05f, 0.05f);
    Vector3f currentScale = new Vector3f(0.05f, 0.05f, 0.05f);

    private void SetupShaders()
    {
        defaultShader = Programs.addProgram(VertexShaders.lms_vertexShaderCode,
                FragmentShaders.lms_fragmentShaderCode);

        shaderFragWhiteDiffuseColor = Programs.addProgram(VertexShaders.FragmentLighting_PN,
                FragmentShaders.FragmentLighting);
        Programs.setLightIntensity(shaderFragWhiteDiffuseColor, new Vector4f(0.8f, 0.8f, 0.8f, 1.0f));
        Programs.setAmbientIntensity(shaderFragWhiteDiffuseColor, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f));
        Matrix4f mm = Matrix4f.Identity();
        mm.M11 = 0.05f;
        mm.M22 = 0.05f;
        mm.M33 = 0.05f;
        Programs.setModelToCameraMatrix(shaderFragWhiteDiffuseColor, mm);


        Vector4f worldLightPos = new Vector4f(-0.5f, -0.5f, -10f, 1.0f);
        Vector4f lightPosCameraSpace = Vector4f.Transform(worldLightPos, mm);
        Matrix4f invTransform = mm.inverted();
        Vector4f lightPosModelSpace = Vector4f.Transform(lightPosCameraSpace, invTransform);
        Vector3f lightPos = new Vector3f(lightPosModelSpace.x, lightPosModelSpace.y, lightPosModelSpace.z);

        Programs.setModelSpaceLightPosition(shaderFragWhiteDiffuseColor, lightPos);
    }

    protected void init()
    {
        Programs.reset();
        Shape.resetWorldToCameraMatrix();
        InputStream test1 = Shader.context.getResources().openRawResource(R.raw.test_with_normals_binary);
        ship = new Blender();
        ship.readBinaryFile(test1);
        ship.setColor(Colors.WHITE_COLOR);
        ship.scale(currentScale);

        controls = new TextClass("X_CCW  X_CW   Y_CCW   FIRE   Y_CW   Z_CCW  Z_CW", 0.4f, 0.04f, staticText);
        controls.setOffset(new Vector3f(-0.9f, -0.8f, 0.0f));

        axis_info = new TextClass("Axis  " + axis.toString(), 0.4f, 0.03f, staticText);
        axis_info.setOffset(new Vector3f(-0.9f, 0.8f, 0.0f));

        up_info = new TextClass("Up    " + up.toString(), 0.4f, 0.03f, staticText);
        up_info.setOffset(new Vector3f(-0.9f, 0.7f, 0.0f));

        right_info = new TextClass("Right " + right.toString(), 0.4f, 0.03f, staticText);
        right_info.setOffset(new Vector3f(-0.9f, 0.6f, 0.0f));

        infoEnable = new TextClass("Info" , 0.4f, 0.03f, staticText);
        infoEnable.setOffset(new Vector3f(-0.9f, 0.8f, 0.0f));

        setupDepthAndCull();
        SetupShaders();
    }

    static boolean looperCreated = false;

    public void display()
    {
        if (!looperCreated) {
            looperCreated = true;
            //Looper.prepare();
            //Looper.loop();
        }
        ArrayList<Integer> deadMissles = new ArrayList<Integer>();
        clearDisplay();
        ship.draw();
        anglehorizontal = anglehorizontal + 0.02f;
        anglevertical = anglevertical + 0.01f;
        for (int i = 0; i < missles.size(); i++)
        {
            if (missles.get(i).firing())
            {
                missles.get(i).Draw();
            }
            else
            {
                if (missles.get(i).Finished())
                {
                    deadMissles.add(i);
                }
            }
        }

        if (deadMissles.size() > 0)
        {
            missles.remove(missles.get(deadMissles.get(0)));
        }

        if (addMissle) {
            missles.add(new Missle(axis, up, right));
            addMissle = false;
        }
        controls.draw();
        if (enableInfo)
        {
            axis_info.draw();
            up_info.draw();
            right_info.draw();
        }
        else
        {
            infoEnable.draw();
        }
        if (updateText)
        {
            UpdateInfoText();
        }
        if (enableInfoDebounce > 0)
        {
            enableInfoDebounce--;
        }
    }

    private void UpdateInfoText()
    {
        axis_info.UpdateText("Axis " + axis.toString());
        up_info.UpdateText("Up    " + up.toString());
        right_info.UpdateText("Right " + right.toString());
        updateText = false;
    }

    private void Rotate(Vector3f rotationAxis, float angle)
    {
        ship.rotateShapes(rotationAxis, angle);
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(rotationAxis, angle);
        axis = Vector3f.Transform(axis, rotation);
        up = Vector3f.Transform(up, rotation);
        right = Vector3f.Transform(right, rotation);
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_1:
                Rotate(Vector3f.UnitX, 5f);
                break;
            case KeyEvent.KEYCODE_2:
                Rotate(Vector3f.UnitX, -5f);
                break;
            case KeyEvent.KEYCODE_3:
                Rotate(Vector3f.UnitY, 5f);
                break;
            case KeyEvent.KEYCODE_4:
                Rotate(Vector3f.UnitY, -5f);
                break;
            case KeyEvent.KEYCODE_5:
                Rotate(Vector3f.UnitZ, 5f);
                break;
            case KeyEvent.KEYCODE_6:
                Rotate(Vector3f.UnitZ, -5f);
                break;
            case KeyEvent.KEYCODE_I:
                result.append("Found " + String.valueOf(ship.objectCount()) + " objects in ship file.");
                break;
            case KeyEvent.KEYCODE_SPACE:
                if (addMissle == false)
                {
                    if (missles.size() < 10)
                    {
                        addMissle = true;
                    }
                }
                break;
            case KeyEvent.KEYCODE_NUMPAD_ADD:
                currentScale = currentScale.mul(1.05f);
                ship.scale(currentScale);
                break;
            case KeyEvent.KEYCODE_NUMPAD_SUBTRACT:
                currentScale = currentScale.divide(1.05f);
                ship.scale(currentScale);
                break;
            case KeyEvent.KEYCODE_NUMPAD_4:
                Rotate(Vector3f.UnitY, 5f);
                break;
            case KeyEvent.KEYCODE_NUMPAD_6:
                Rotate(Vector3f.UnitY, -5f);
                break;
            case KeyEvent.KEYCODE_NUMPAD_8:
                Rotate(Vector3f.UnitX, 5f);
                break;
            case KeyEvent.KEYCODE_NUMPAD_2:
                Rotate(Vector3f.UnitX, -5f);
                break;
            case KeyEvent.KEYCODE_NUMPAD_5:
                if (addMissle == false)
                {
                    if (missles.size() < 10)
                    {
                        addMissle = true;
                    }
                }
                break;
            case KeyEvent.KEYCODE_NUMPAD_7:
                Rotate(Vector3f.UnitZ, 5f);
                break;
            case KeyEvent.KEYCODE_NUMPAD_9:
                Rotate(Vector3f.UnitZ, 5f);
                break;
            case KeyEvent.KEYCODE_NUMPAD_1:
                Rotate(Vector3f.UnitZ, -5f);
                break;
            case KeyEvent.KEYCODE_NUMPAD_3:
                Rotate(Vector3f.UnitZ, -5f);
                break;
        }
        return result.toString();
    }

    public void touchEvent(int x_position, int y_position) throws Exception
    {
        int selection = x_position / (width / 7);
        switch (selection)
        {
            case 0: Rotate(Vector3f.UnitX, 5f); break;
            case 1: Rotate(Vector3f.UnitX, -5f); break;
            case 2: Rotate(Vector3f.UnitY, 5f); break;
            // Note *** Don't add missle here, different thread
            case 3:
                if (addMissle == false)
                {
                    if (missles.size() < 10)
                    {
                        addMissle = true;
                    }
                }
                break;
            case 4: Rotate(Vector3f.UnitY, -5f); break;
            case 5: Rotate(Vector3f.UnitZ, 5f); break;
            case 6: Rotate(Vector3f.UnitZ, -5f); break;
        }
        if (enableInfoDebounce == 0) {
            if (selection == 0) {
                if (y_position / (height / 4) == 0) {
                    enableInfoDebounce = 15;
                    if (enableInfo) {
                        enableInfo = false;
                    } else {
                        enableInfo = true;
                    }
                }
                if (y_position / (height / 4) == 1) {
                    ship.setProgram(shaderFragWhiteDiffuseColor);
                }
                if (y_position / (height / 4) == 2) {
                    ship.setProgram(defaultShader);
                }
            }
        }

        if (enableInfo)
        {
            updateText = true;
        }
    }

    public void setScale(float scale)
    {
        currentScale = initialScale.mul(scale);
        ship.scale(currentScale);
    }
}
