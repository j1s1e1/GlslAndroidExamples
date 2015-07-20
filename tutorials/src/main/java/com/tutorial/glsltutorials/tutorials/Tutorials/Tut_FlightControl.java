package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Creatures.Dragonfly3d;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

/**
 * Created by jamie on 7/12/15.
 */
public class Tut_FlightControl extends TutorialBase {
    Dragonfly3d dragonFly;
    float totalScale = 1.0f;
    float minScale = 0.1f;
    float maxScale = 10f;

    int systemMovementMatrixUnif;

    int dragonflyProgram;

    Matrix4f dragonflyMatrix = Matrix4f.Identity();
    Matrix4f dragonflyScale = Matrix4f.Identity();
    Matrix4f dragonflyTranslation = Matrix4f.Identity();
    Matrix4f dragonflyRotation = Matrix4f.Identity();

    protected void init()
    {
        dragonFly = new Dragonfly3d(0, 0, 0);
        dragonFly.clearAutoMove();
        dragonflyProgram = Programs.addProgram(VertexShaders.flightControl_lms, FragmentShaders.lms_fragmentShaderCode);
        dragonflyMatrix = Matrix4f.CreateRotationY((float)Math.PI);
        dragonflyMatrix = Matrix4f.Identity();
        SetSystemMatrix(dragonflyMatrix, dragonflyProgram);
        dragonFly.setProgram(dragonflyProgram);
    }

    private void SetSystemMatrix(Matrix4f matrix, int program)
    {
        GLES20.glUseProgram(Programs.getProgram(program));
        systemMovementMatrixUnif = GLES20.glGetUniformLocation(Programs.getProgram(program), "systemMovementMatrix");
        GLES20.glUniformMatrix4fv(systemMovementMatrixUnif, 1, false, matrix.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void display()
    {
        clearDisplay();
        dragonFly.draw();
        SetSystemMatrix(dragonflyMatrix, dragonflyProgram);
    }

    private void UpdateDragonflyMatrix()
    {
        dragonflyMatrix = Matrix4f.mul(dragonflyTranslation, dragonflyScale);
        dragonflyMatrix = Matrix4f.mul(dragonflyRotation, dragonflyMatrix);
    }

    public void SetScale(float scale) {
        if ((totalScale * scale) > minScale)
        {
            if ((totalScale * scale) < maxScale) {
                totalScale = totalScale * scale;
                Shape.scaleWorldToCameraMatrix(scale);
            }
        }
    }

    private void Rotate(Vector3f rotationAxis, float angle)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(rotationAxis, (float)Math.PI / 180.0f * angle);
        dragonflyRotation = Matrix4f.mul(rotation, dragonflyRotation);
        UpdateDragonflyMatrix();
    }

    private void Translate(Vector3f translation)
    {
        Matrix4f translationMatrix = Matrix4f.createTranslation(translation);
        dragonflyTranslation = Matrix4f.mul(translationMatrix, dragonflyTranslation);
        UpdateDragonflyMatrix();
    }

    public void receiveMessage(String message)
    {
        String[] words = message.split(" ");
        switch (words[0])
        {
            case "ZoomIn": SetScale(1.05f); break;
            case "ZoomOut": SetScale(1f / 1.05f); break;
            case "RotateX":  Rotate(Vector3f.UnitX, Float.parseFloat(words[1])); break;
            case "RotateY":  Rotate(Vector3f.UnitY, Float.parseFloat(words[1])); break;
            case "RotateZ":  Rotate(Vector3f.UnitZ, Float.parseFloat(words[1])); break;
            case "RotateX+": Rotate(Vector3f.UnitX, 5f); break;
            case "RotateX-": Rotate(Vector3f.UnitX, -5f); break;
            case "RotateY+": Rotate(Vector3f.UnitY, 5f); break;
            case "RotateY-": Rotate(Vector3f.UnitY, -5f); break;
            case "RotateZ+": Rotate(Vector3f.UnitZ, 5f); break;
            case "RotateZ-": Rotate(Vector3f.UnitZ, -5f); break;
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

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_1: Rotate(Vector3f.UnitX, 5f); break;
            case KeyEvent.KEYCODE_2: Rotate(Vector3f.UnitY, 5f); break;
            case KeyEvent.KEYCODE_3: Rotate(Vector3f.UnitZ, 5f); break;
            case KeyEvent.KEYCODE_NUMPAD_8: Translate(new Vector3f(0f, 0.05f, 0f)); break;
            case KeyEvent.KEYCODE_NUMPAD_2: Translate(new Vector3f(0f, -0.05f, 0f)); break;
            case KeyEvent.KEYCODE_NUMPAD_6: Translate(new Vector3f(0.05f, 0f, 0f)); break;
            case KeyEvent.KEYCODE_NUMPAD_4: Translate(new Vector3f(-0.05f, 0f, 0f)); break;

        }
        return result.toString();
    }

    public void orientationEvent(float azimuth_angle, float pitch_angle, float roll_angle)
    {
        Matrix4f rotationX = Matrix4f.CreateRotationX(pitch_angle);
        Matrix4f rotationY = Matrix4f.CreateRotationY(azimuth_angle);
        Matrix4f rotationZ = Matrix4f.CreateRotationZ(roll_angle);
        dragonflyRotation = Matrix4f.mul(rotationY, rotationX);
        dragonflyRotation = Matrix4f.mul(rotationZ, dragonflyRotation);
        UpdateDragonflyMatrix();
    }
}
