package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Blender.Blender;
import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Creatures.Enemy;
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
import com.tutorial.glsltutorials.tutorials.Shapes.TextureSphere;
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
    TextureSphere planet;
    ArrayList<Missle> missles = new ArrayList<Missle>();
    boolean addMissle = false;

    ArrayList<Enemy> enemies;

    TextClass credit1;
    TextClass credit2;

    int deadEnemysCount = 0;
    TextClass deadEnemysText;

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

    Vector3f initialScale = new Vector3f(0.1f, 0.1f, 0.1f);
    Vector3f currentScale = new Vector3f(0.1f, 0.1f, 0.1f);
    float totalScale = 1.0f;
    float minScale = 0.1f;
    float maxScale = 10f;

    float planetAngle = 0f;
    Vector3f planetAxis = new Vector3f(0.2f, 0.8f, 0.1f);

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
        InputStream binaryBlenderData = Shader.context.getResources().openRawResource(R.raw.xwng_with_normals_binary);
        ship = new Blender();
        ship.readBinaryFile(binaryBlenderData);
        ship.setColor(Colors.WHITE_COLOR);
        ship.scale(currentScale);
        planet = new TextureSphere(0.35f, R.drawable.venus_magellan);

        enemies = new ArrayList<Enemy>();

        for (int i = 0; i < 10; i++)
        {
            Enemy enemy = new Enemy();
            enemies.add(enemy);
        }

        credit1 = new TextClass("X-Wing Model based on Blender model by", 0.4f, 0.04f, staticText);
        credit1.setOffset(new Vector3f(-0.75f, -0.65f, 0.0f));

        credit2 = new TextClass("Angel David Guzman of PixelOz Designs", 0.4f, 0.04f, staticText);
        credit2.setOffset(new Vector3f(-0.75f, -0.75f, 0.0f));

        deadEnemysText = new TextClass("Dead Enemys = " + String.valueOf(deadEnemysCount), 0.4f, 0.04f, staticText);
        deadEnemysText.setOffset(new Vector3f(-0.75f, +0.9f, 0.0f));

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

    public void display()
    {
        ArrayList<Integer> deadMissles = new ArrayList<Integer>();
        ArrayList<Integer> deadEnemys = new ArrayList<Integer>();
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
        for(int i = 0; i < enemies.size(); i++)
        {
            if (enemies.get(i).isDead())
            {
                dead++;
            }
            else
            {
                enemies.get(i).draw();
                enemies.get(i).fireOn(missles);
            }
        }
        if (dead > deadEnemysCount)
        {
            deadEnemysCount = dead;
            deadEnemysText.UpdateText("Dead Enemys = " + String.valueOf(deadEnemysCount));
        }
        deadEnemysText.draw();
        credit1.draw();
        credit2.draw();
        planet.setOffset(new Vector3f(0.45f + (float) Math.sin(planetAngle),
                0.45f + (float) Math.cos(planetAngle), 2.5f + 0.01f * (float) Math.tan(planetAngle)));
        planetAngle = planetAngle + 0.02f;
        planet.rotateShape(planetAxis, planetAngle/40f);
        planet.draw();
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
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(rotationAxis, (float)Math.PI / 180.0f * angle);
        axis = Vector3f.transform(axis, rotation);
        up = Vector3f.transform(up, rotation);
        right = Vector3f.transform(right, rotation);
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
                setScale(1.05f);
                break;
            case KeyEvent.KEYCODE_NUMPAD_SUBTRACT:
                setScale(1f / 1.05f);
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
                if (y_position / (height / 8) == 0) {
                    setScale(1.05f);
                }
                if (y_position / (height / 8) == 7) {
                    setScale(1f / 1.05f);
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
                    enableInfo = !enableInfo;
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

    public void setScale(float scale) {
        if ((totalScale * scale) > minScale)
        {
            if ((totalScale * scale) < maxScale) {
                totalScale = totalScale * scale;
                Shape.scaleWorldToCameraMatrix(scale);
            }
        }
        //ship.Scale(currentScale);
    }

    public void receiveMessage(String message)
    {
        String[] words = message.split(" ");
        switch (words[0])
        {
            case "ZoomIn": setScale(1.05f); break;
            case "ZoomOut": setScale(1f / 1.05f); break;
            case "RotateX":  Rotate(Vector3f.UnitX, Float.parseFloat(words[1])); break;
            case "RotateY":  Rotate(Vector3f.UnitY, Float.parseFloat(words[1])); break;
            case "RotateZ":  Rotate(Vector3f.UnitZ, Float.parseFloat(words[1])); break;
            case "RotateX+": Rotate(Vector3f.UnitX, 5f); break;
            case "RotateX-": Rotate(Vector3f.UnitX, -5f); break;
            case "RotateY+": Rotate(Vector3f.UnitY, 5f); break;
            case "RotateY-": Rotate(Vector3f.UnitY, -5f); break;
            case "RotateZ+": Rotate(Vector3f.UnitZ, 5f); break;
            case "RotateZ-": Rotate(Vector3f.UnitZ, -5f); break;
            case "Shoot":
                if (addMissle == false)
                {
                    if (missles.size() < 10)
                    {
                        addMissle = true;
                    }
                }
                break;
            case "ShipColor":
                if (words.length == 5) {
                    ship.setColor(new float[]{Float.parseFloat(words[1]), Float.parseFloat(words[2]),
                            Float.parseFloat(words[3]), Float.parseFloat(words[4])});
                }
                break;
            case "SetScale":
                if (words.length == 2) {
                    Shape.resetWorldToCameraMatrix();
                    Shape.scaleWorldToCameraMatrix(Float.parseFloat(words[1]));
                }
                break;
            case "SetScaleLimit":
                if (words.length == 3) {
                    minScale = Float.parseFloat(words[1]);
                    maxScale = Float.parseFloat(words[2]);
                }
                break;
        }
    }
}
