package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Creatures.Dragonfly3d;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;

/**
 * Created by jamie on 5/25/15.
 */
public class Tut_DragonFly extends TutorialBase {
    Dragonfly3d dragonfly;
    int dragonflyProgram;
    int sphericalProgram;
    boolean automove = true;
    boolean startSpherical = false;
    boolean startBug = false;
    boolean theta = true;

    protected void init()
    {
        dragonfly = new Dragonfly3d(0, 0, 0);
        dragonflyProgram = dragonfly.getProgram();
        sphericalProgram = Programs.addProgram(VertexShaders.spherical_lms, FragmentShaders.lms_fragmentShaderCode);
        setupDepthAndCull();
    }

    public void display()
    {
        clearDisplay();
        dragonfly.draw();
        if (startBug)
        {
            startBug = false;
            dragonfly.setProgram(dragonflyProgram);
            dragonfly.setBug2DMovement();
        }
        if (startSpherical)
        {
            startSpherical = false;
            dragonfly.setProgram(sphericalProgram);
            if (theta) {
                dragonfly.setSphericalMovement(sphericalProgram, 1f, 0f);
            }
            else {
                dragonfly.setSphericalMovement(sphericalProgram, 0f, 1f);
            }
        }
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_A:
                if (automove)
                {
                    dragonfly.clearAutoMove();
                    automove = false;
                }
                else
                {
                    dragonfly.setAutoMove();
                    automove = true;
                }
                break;
            case KeyEvent.KEYCODE_B:
                startBug = true;
                break;
            case KeyEvent.KEYCODE_I:
                result.append(dragonfly.getMovementInfo());
                break;
            case KeyEvent.KEYCODE_S:
                theta = true;
                startSpherical = true;
                break;
            case KeyEvent.KEYCODE_T:
                theta = false;
                startSpherical = true;
                break;
        }
        return result.toString();
    }
}
