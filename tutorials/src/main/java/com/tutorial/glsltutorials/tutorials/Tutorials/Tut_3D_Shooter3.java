package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Blender.Blender;
import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Creatures.Alien;
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
public class Tut_3D_Shooter3 extends TutorialBase {
    public Tut_3D_Shooter3()
    {
    }

    Blender ship;
    ArrayList<Missle> missles = new ArrayList<Missle>();
    boolean addMissle = false;

    ArrayList<Alien> aliens;

    TextClass credit1;
    TextClass credit2;

    int deadAliensCount = 0;
    TextClass deadAliensText;

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
    Vector3f up = new Vector3f(0f, 0.07f, 0f);
    Vector3f right = new Vector3f(0.16f, 0f,0f);

    int defaultShader;
    int shaderFragWhiteDiffuseColor;

    Vector3f currentScale = new Vector3f(0.1f, 0.1f, 0.1f);

    private void SetupShaders()
    {
        defaultShader = Programs.AddProgram(VertexShaders.lms_vertexShaderCode,
                FragmentShaders.lms_fragmentShaderCode);

        shaderFragWhiteDiffuseColor = Programs.AddProgram(VertexShaders.FragmentLighting_PN,
                FragmentShaders.FragmentLighting);
        Programs.SetLightIntensity(shaderFragWhiteDiffuseColor, new Vector4f(0.8f, 0.8f, 0.8f, 1.0f));
        Programs.SetAmbientIntensity(shaderFragWhiteDiffuseColor, new Vector4f(0.2f, 0.2f, 0.2f, 1.0f));
        Matrix4f mm = Matrix4f.Identity();
        mm.M11 = 0.05f;
        mm.M22 = 0.05f;
        mm.M33 = 0.05f;
        Programs.SetModelToCameraMatrix(shaderFragWhiteDiffuseColor, mm);


        Vector4f worldLightPos = new Vector4f(-0.5f, -0.5f, -10f, 1.0f);
        Vector4f lightPosCameraSpace = Vector4f.Transform(worldLightPos, mm);
        Matrix4f invTransform = mm.inverted();
        Vector4f lightPosModelSpace = Vector4f.Transform(lightPosCameraSpace, invTransform);
        Vector3f lightPos = new Vector3f(lightPosModelSpace.x, lightPosModelSpace.y, lightPosModelSpace.z);

        Programs.SetModelSpaceLightPosition(shaderFragWhiteDiffuseColor, lightPos);
    }

    protected void init()
    {
        Programs.Reset();
        Shape.ResetWorldToCameraMatrix();
        InputStream binaryBlenderData = Shader.context.getResources().openRawResource(R.raw.xwng_with_normals_binary);
        ship = new Blender();
        ship.ReadBinaryFile(binaryBlenderData);
        ship.SetColor(Colors.WHITE_COLOR);
        ship.Scale(currentScale);

        aliens = new ArrayList<Alien>();

        for (int i = 0; i < 10; i++)
        {
            Alien alien = new Alien();
            aliens.add(alien);
        }

        credit1 = new TextClass("X-Wing Model based on Blender model by", 0.4f, 0.04f, staticText);
        credit1.SetOffset(new Vector3f(-0.75f, -0.65f, 0.0f));

        credit2 = new TextClass("Angel David Guzman of PixelOz Designs", 0.4f, 0.04f, staticText);
        credit2.SetOffset(new Vector3f(-0.75f, -0.75f, 0.0f));

        deadAliensText = new TextClass("Dead Aliens = " + String.valueOf(deadAliensCount), 0.4f, 0.04f, staticText);
        deadAliensText.SetOffset(new Vector3f(-0.75f, +0.9f, 0.0f));

        axis_info = new TextClass("Axis  " + axis.toString(), 0.4f, 0.03f, staticText);
        axis_info.SetOffset(new Vector3f(-0.9f, 0.8f, 0.0f));

        up_info = new TextClass("Up    " + up.toString(), 0.4f, 0.03f, staticText);
        up_info.SetOffset(new Vector3f(-0.9f, 0.7f, 0.0f));

        right_info = new TextClass("Right " + right.toString(), 0.4f, 0.03f, staticText);
        right_info.SetOffset(new Vector3f(-0.9f, 0.6f, 0.0f));

        infoEnable = new TextClass("Info" , 0.4f, 0.03f, staticText);
        infoEnable.SetOffset(new Vector3f(-0.9f, 0.8f, 0.0f));

        SetupDepthAndCull();
        SetupShaders();
    }

    public void display()
    {
        ArrayList<Integer> deadMissles = new ArrayList<Integer>();
        ArrayList<Integer> deadAliens = new ArrayList<Integer>();
        ClearDisplay();
        ship.Draw();
        anglehorizontal = anglehorizontal + 0.02f;
        anglevertical = anglevertical + 0.01f;
        for (int i = 0; i < missles.size(); i++)
        {
            if (missles.get(i).Firing())
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
        int dead = 0;
        for(int i = 0; i < aliens.size(); i++)
        {
            if (aliens.get(i).isDead())
            {
                dead++;
            }
            else
            {
                aliens.get(i).Draw();
                aliens.get(i).FireOn(missles);
            }
        }
        if (dead > deadAliensCount)
        {
            deadAliensCount = dead;
            deadAliensText.UpdateText("Dead Aliens = " + String.valueOf(deadAliensCount));
        }
        deadAliensText.draw();
        credit1.draw();
        credit2.draw();
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
        ship.RotateShapes(rotationAxis, angle);
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(rotationAxis, (float)Math.PI / 180.0f * angle);
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
                result.append("Found " + String.valueOf(ship.ObjectCount()) + " objects in ship file.");
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
        }
        return result.toString();
    }

    public void TouchEvent(int x_position, int y_position) throws Exception
    {
        int selection = x_position / (width / 7);
        switch (selection)
        {
            case 0: Rotate(Vector3f.UnitX, 5f); break;
            case 1: Rotate(Vector3f.UnitX, -5f); break;
            case 2: Rotate(Vector3f.UnitY, 5f); break;
            // Note *** Don't add missle here, different thread
            case 3:
                if (y_position / (height / 4) == 0) {
                    currentScale = currentScale.mul(1.05f);
                    ship.Scale(currentScale);
                }
                if (y_position / (height / 4) == 3) {
                    currentScale = currentScale.divide(1.05f);
                    ship.Scale(currentScale);
                }
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
                if (y_position / (height / 4) == 1) {
                    enableInfoDebounce = 15;
                    if (enableInfo) {
                        enableInfo = false;
                    } else {
                        enableInfo = true;
                    }
                }
                if (y_position / (height / 4) == 1) {
                    ship.SetProgram(shaderFragWhiteDiffuseColor);
                }
                if (y_position / (height / 4) == 2) {
                    ship.SetProgram(defaultShader);
                }
            }
        }
        if (enableInfo)
        {
            updateText = true;
        }
    }
}
