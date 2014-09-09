package com.tutorial.glsltutorials.tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;

import java.util.ArrayList;

/**
 * Created by Jamie on 5/26/14.
 */
public class Tut_Text extends TutorialBase {

    static ArrayList<TextClass> text;
    static float[] rotationMat;
    void InitializeProgram()
    {
        int vertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, TextClass.textVertexShader);
        int fragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, TextClass.textFragmentShader);
        theProgram = Shader.createAndLinkProgram(vertexShader, fragmentShader);
    }

    private void SetupText()
    {
        text = new ArrayList<TextClass>();
        for (int i = 0; i < 2; i++)
        {
            TextClass new_text;
            if (i == 1)
            {
                new_text = new TextClass("01234", 1f, 0.05f);
            }
            else
            {
                new_text = new TextClass("56789", 0.5f, 0.03f);
            }

            new_text.Move(-0.5f, -0.5f+ i * 0.3f, 0.5f - i);
            text.add(new_text);
        }
        for (int i = 0; i < 2; i++)
        {
            TextClass new_text;
            if (i == 1)
            {
                new_text = new TextClass("867", 1f, 0.06f);
            }
            else
            {
                new_text = new TextClass("5309", 1f, 0.06f);
            }

            new_text.Move(0f, -0.25f + i * 0.3f, 0.0f);
            text.add(new_text);
        }
    }


    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        //InitializeProgram();
        SetupText();
    }

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display()
    {

        GLES20.glClearColor(0.2f, 0.2f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        for (TextClass t : text )
        {
            t.draw();
        }
        for(int i = 0; i < 2; i++) {
            text.get(i).draw();
        }

    }

}
