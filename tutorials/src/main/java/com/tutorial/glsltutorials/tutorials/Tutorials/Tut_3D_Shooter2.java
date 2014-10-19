package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Blender.Blender;
import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
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
public class Tut_3D_Shooter2 extends TutorialBase {
    public Tut_3D_Shooter2()
    {
    }

    Blender ship;
    ArrayList<Missle> missles = new ArrayList<Missle>();
    boolean addMissle = false;

    TextClass credit1;
    TextClass credit2;
    boolean staticText = true;

    protected void init()
    {
        Programs.Reset();
        Shape.ResetWorldToCameraMatrix();
        InputStream test1 = Shader.context.getResources().openRawResource(R.raw.x_wing);
        ship = new Blender();
        ship.ReadFile(test1);
        ship.SetColor(Colors.WHITE_COLOR);
        ship.Scale(new Vector3f(0.1f, 0.1f, 0.1f));

        credit1 = new TextClass("X-Wing Model based on Blender model by", 0.4f, 0.04f, staticText);
        credit1.SetOffset(new Vector3f(-0.75f, -0.65f, 0.0f));

        credit2 = new TextClass("Angel David Guzman of PixelOz Designs", 0.4f, 0.04f, staticText);
        credit2.SetOffset(new Vector3f(-0.75f, -0.75f, 0.0f));

        SetupDepthAndCull();
    }

    double anglehorizontal = 0;
    double anglevertical = 0;

    Vector3f axis = new Vector3f(0f, 0f, 1f);
    Vector3f up = new Vector3f(0f, 0.125f, 0f);
    Vector3f right = new Vector3f(0.25f, 0f, 0f);

    public void display()
    {
        ArrayList<Integer> deadMissles = new ArrayList<Integer>();
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
            axis.z = -axis.z;
            addMissle = false;
        }
        credit1.draw();
        credit2.draw();
    }

    private void Rotate(Vector3f rotationAxis, float angle)
    {
        ship.RotateShapes(rotationAxis, angle);
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
    }
}
