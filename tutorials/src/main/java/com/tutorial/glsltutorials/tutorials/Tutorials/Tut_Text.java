package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.view.KeyEvent;

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
    static TextClass keyText;
    boolean staticText = true;
    boolean reverseRotation = true;
    boolean updateText = false;
    String keyTextString = "";

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

            new_text.setOffset(-0.5f, -0.5f + i * 0.4f, 0.5f - i);
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

            new_text.setOffset(0f, -0.25f + i * 0.3f, 0.0f);
            text.add(new_text);
        }
        new_text = new TextClass("ABCDEFGHIJKLM", 0.5f, 0.05f, staticText, reverseRotation);
        new_text.setOffset(new Vector3f(0f, 0.5f, 0.0f));
        text.add(new_text);

        new_text = new TextClass("NOPQRSTUVWXYZ", 0.5f, 0.05f, staticText, reverseRotation);
        new_text.setOffset(new Vector3f(0f, 0.4f, 0.0f));
        text.add(new_text);

        keyText = new TextClass("1234", 0.5f, 0.05f, staticText, reverseRotation);
        keyText.setOffset(new Vector3f(-0.8f, -0.8f, 0.0f));
    }


    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        Programs.reset();
        Shape.resetWorldToCameraMatrix();
        SetupText();
        setupDepthAndCull();
    }

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display()
    {
        clearDisplay();
        for (TextClass t : text )
        {
            t.draw();
        }
        for(int i = 0; i < 2; i++) {
            text.get(i).draw();
        }
        keyText.draw();
        if (updateText)
        {
            keyText.UpdateText(keyTextString);
            updateText = false;
        }
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        keyTextString = String.valueOf(keyCode);
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_1:
                keyTextString = "1";
                break;
            case KeyEvent.KEYCODE_2:
                keyTextString = "2";
                break;
            case KeyEvent.KEYCODE_3:
                keyTextString = "3";
                break;
            case KeyEvent.KEYCODE_4:
                keyTextString = "4";
                break;
            case KeyEvent.KEYCODE_5:
                keyTextString = "5";
                break;
            case KeyEvent.KEYCODE_6:
                keyTextString = "6";
                break;
            case KeyEvent.KEYCODE_I:
                keyTextString = "I";
                break;
            case KeyEvent.KEYCODE_SPACE:
                keyTextString = "SPACE";
                break;
            case KeyEvent.KEYCODE_NUMPAD_ADD:
                keyTextString = "NUMPAD_ADD";
                break;
            case KeyEvent.KEYCODE_NUMPAD_SUBTRACT:
                keyTextString = "NUMPAD_SUBTRACT";
                break;
            case KeyEvent.KEYCODE_NUMPAD_0:
                keyTextString = "NUMPAD_0";
                break;
            case KeyEvent.KEYCODE_NUMPAD_1:
                keyTextString = "NUMPAD_1";
                break;
            case KeyEvent.KEYCODE_NUMPAD_2:
                keyTextString = "NUMPAD_2";
                break;
            case KeyEvent.KEYCODE_NUMPAD_3:
                keyTextString = "NUMPAD_3";
                break;
            case KeyEvent.KEYCODE_NUMPAD_4:
                keyTextString = "NUMPAD_4";
                break;
            case KeyEvent.KEYCODE_NUMPAD_5:
                keyTextString = "NUMPAD_5";
                break;
            case KeyEvent.KEYCODE_NUMPAD_6:
                keyTextString = "NUMPAD_6";
                break;
            case KeyEvent.KEYCODE_NUMPAD_7:
                keyTextString = "NUMPAD_7";
                break;
            case KeyEvent.KEYCODE_NUMPAD_8:
                keyTextString = "NUMPAD_8";
                break;
            case KeyEvent.KEYCODE_NUMPAD_9:
                keyTextString = "NUMPAD_9";
                break;
        }
        if (keyTextString !=  "Unknown")
        {
            updateText = true;
        }
        return result.toString();
    }

    public void receiveMessage(String message)
    {
        keyTextString = message;
        updateText = true;
    }
}
