package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.BodyParts.Bone;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.ArrayList;

/**
 * Created by jamie on 7/26/15.
 */
public class Tut_Skeleton extends TutorialBase {
    ArrayList<Bone> bones;

    int boneToUpdate = 0;

    protected  void init()
    {
        bones = new ArrayList<Bone>();
        Bone bone = new Bone();
        bone.scale(new Vector3f(0.2f, 1f, 0.2f));
        bones.add(bone);
        bone = new Bone();
        bone.scale(new Vector3f(0.2f, 0.5f, 0.2f));
        bone.move(new Vector3f(0.3f, 0f, 0f));
        bones.get(0).addChild(bone);
        bones.add(bone);
        setupDepthAndCull();
    }


    public void display()
    {
        clearDisplay();
        for(Bone b : bones)
        {
            b.draw();
        }
    }

    private void rotate(Vector3f v, float angle)
    {
        bones.get(boneToUpdate).rotate(v, angle);
    }

    private void scale(Vector3f v)
    {
        bones.get(boneToUpdate).scale(v);
    }

    private void move(Vector3f v)
    {
        bones.get(boneToUpdate).move(v);
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_B:
                boneToUpdate++;
                if (boneToUpdate > (bones.size() - 1))
                {
                    boneToUpdate = 0;
                }
                result.append("boneToUpdate = " + String.valueOf(boneToUpdate));
                break;
            case KeyEvent.KEYCODE_1:  rotate(Vector3f.UnitX, 5f);   break;
            case KeyEvent.KEYCODE_2:  rotate(Vector3f.UnitX, -5f);  break;
            case KeyEvent.KEYCODE_3:  rotate(Vector3f.UnitY, 5f);   break;
            case KeyEvent.KEYCODE_4:  rotate(Vector3f.UnitY, -5f);  break;
            case KeyEvent.KEYCODE_5:  rotate(Vector3f.UnitZ, 5f);   break;
            case KeyEvent.KEYCODE_6:  rotate(Vector3f.UnitZ, -5f);  break;
            case KeyEvent.KEYCODE_7:  scale(new Vector3f(0.9f, 0.9f, 0.9f));  break;
            case KeyEvent.KEYCODE_8:  scale(new Vector3f(1.1f, 1.1f, 1.1f));  break;
            case KeyEvent.KEYCODE_9:
                move(new Vector3f(0.1f, 0.1f, 0.1f));  break;
            case KeyEvent.KEYCODE_0:
                move(new Vector3f(-0.1f, -0.1f, -0.1f));  break;
            case KeyEvent.KEYCODE_I:
                for(Bone b : bones)
            {
                result.append(b.getBoneInfo());
            }
            break;
        }
        result.append(String.valueOf(keyCode));
        return result.toString();
    }

    public void touchEvent(int x_position, int y_position) throws Exception
    {
        int selection = x_position / (width / 7);
        switch (selection)
        {
            case 0: rotate(Vector3f.UnitX, 5f); break;
            case 1: rotate(Vector3f.UnitX, -5f); break;
            case 2: rotate(Vector3f.UnitY, 5f); break;
            case 3:
            {
                boneToUpdate++;
                if (boneToUpdate > (bones.size() - 1)) {
                    boneToUpdate = 0;
                }
            }
            break;
            case 4: rotate(Vector3f.UnitY, -5f); break;
            case 5: rotate(Vector3f.UnitZ, 5f); break;
            case 6: rotate(Vector3f.UnitZ, -5f); break;
        }
    }
}
