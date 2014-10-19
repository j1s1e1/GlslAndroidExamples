package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Text.TextClass;

import java.util.ArrayList;

/**
 * Created by Jamie on 5/26/14.
 */
public class Tut_Text extends TutorialBase {

    static ArrayList<TextClass> text;
    boolean staticText = true;
    boolean reverseRotation = true;

    private void SetupText()
    {
        TextClass new_text;
        text = new ArrayList<TextClass>();
        for (int i = 0; i < 2; i++)
        {
            if (i == 1)
            {
                new_text = new TextClass("ABC123", 1f, 0.1f, staticText, reverseRotation);
            }
            else
            {
                new_text = new TextClass("56789", 0.5f, 0.2f);
            }

            new_text.SetOffset(-0.5f, -0.5f+ i * 0.4f, 0.5f - i);
            text.add(new_text);
        }
        for (int i = 0; i < 2; i++)
        {
            if (i == 1)
            {
                new_text = new TextClass("867", 1f, 0.06f);
            }
            else
            {
                new_text = new TextClass("5309", 1f, 0.06f);
            }

            new_text.SetOffset(0f, -0.25f + i * 0.3f, 0.0f);
            text.add(new_text);
        }
        new_text = new TextClass("ABCDEFGHIJKLM", 0.5f, 0.05f, staticText, reverseRotation);
        new_text.SetOffset(new Vector3f(0f, 0.5f, 0.0f));
        text.add(new_text);

        new_text = new TextClass("NOPQRSTUVWXYZ", 0.5f, 0.05f, staticText, reverseRotation);
        new_text.SetOffset(new Vector3f(0f, 0.4f, 0.0f));
        text.add(new_text);
    }


    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        Programs.Reset();
        Shape.ResetWorldToCameraMatrix();
        SetupText();
        SetupDepthAndCull();
    }

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display()
    {
        ClearDisplay();
        for (TextClass t : text )
        {
            t.draw();
        }
        for(int i = 0; i < 2; i++) {
            text.get(i).draw();
        }

    }
}
