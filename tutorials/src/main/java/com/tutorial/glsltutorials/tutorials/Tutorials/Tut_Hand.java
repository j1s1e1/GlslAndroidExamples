package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.BodyParts.Bone;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.ArrayList;

/**
 * Created by jamie on 7/26/15.
 */
public class Tut_Hand extends TutorialBase {
    ArrayList<Bone> bones;

    int boneToUpdate = 0;

    enum handBonesEnum
    {
        ARM,
        METACARPAL1,
        METACARPAL2,
        METACARPAL3,
        METACARPAL4,
        METACARPAL5,
        PROXIMALPHALANGE1,
        PROXIMALPHALANGE2,
        PROXIMALPHALANGE3,
        PROXIMALPHALANGE4,
        PROXIMALPHALANGE5,
        MIDDLEPHALANGE1,
        MIDDLEPHALANGE2,
        MIDDLEPHALANGE3,
        MIDDLEPHALANGE4,
        DISTALPHALANGE1,
        DISTALPHALANGE2,
        DISTALPHALANGE3,
        DISTALPHALANGE4,
        DISTALPHALANGE5,
    }

    protected void init()
    {
        bones = new ArrayList<>();
        Bone arm = new Bone();
        arm.scale(new Vector3f(0.05f, 0.5f, 0.05f));
        arm.move(new Vector3f(0.0f, 0.5f, 0.0f));
        bones.add(arm);

        Bone metacarpal = new Bone();
        metacarpal.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        metacarpal.rotate(Vector3f.UnitZ, -40f);
        bones.get(handBonesEnum.ARM.ordinal()).addChild(metacarpal);
        bones.add(metacarpal);

        metacarpal = new Bone();
        metacarpal.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        metacarpal.rotate(Vector3f.UnitZ, -20f);
        bones.get(handBonesEnum.ARM.ordinal()).addChild(metacarpal);
        bones.add(metacarpal);

        metacarpal = new Bone();
        metacarpal.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        metacarpal.rotate(Vector3f.UnitZ, -0f);
        bones.get(handBonesEnum.ARM.ordinal()).addChild(metacarpal);
        bones.add(metacarpal);

        metacarpal = new Bone();
        metacarpal.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        metacarpal.rotate(Vector3f.UnitZ, 20f);
        bones.get(handBonesEnum.ARM.ordinal()).addChild(metacarpal);
        bones.add(metacarpal);

        metacarpal = new Bone();
        metacarpal.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        metacarpal.rotate(Vector3f.UnitZ, 40f);
        bones.get(handBonesEnum.ARM.ordinal()).addChild(metacarpal);
        bones.add(metacarpal);

        Bone proximalPhalange = new Bone();
        proximalPhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        proximalPhalange.rotate(Vector3f.UnitZ, -40f);
        bones.get(handBonesEnum.METACARPAL1.ordinal()).addChild(proximalPhalange);
        bones.add(proximalPhalange);

        proximalPhalange = new Bone();
        proximalPhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        proximalPhalange.rotate(Vector3f.UnitZ, -20f);
        bones.get(handBonesEnum.METACARPAL2.ordinal()).addChild(proximalPhalange);
        bones.add(proximalPhalange);

        proximalPhalange = new Bone();
        proximalPhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        proximalPhalange.rotate(Vector3f.UnitZ, 0f);
        bones.get(handBonesEnum.METACARPAL3.ordinal()).addChild(proximalPhalange);
        bones.add(proximalPhalange);

        proximalPhalange = new Bone();
        proximalPhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        proximalPhalange.rotate(Vector3f.UnitZ, 20f);
        bones.get(handBonesEnum.METACARPAL4.ordinal()).addChild(proximalPhalange);
        bones.add(proximalPhalange);

        proximalPhalange = new Bone();
        proximalPhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        proximalPhalange.rotate(Vector3f.UnitZ, 40f);
        bones.get(handBonesEnum.METACARPAL5.ordinal()).addChild(proximalPhalange);
        bones.add(proximalPhalange);

        Bone middlePhalange = new Bone();
        middlePhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        middlePhalange.rotate(Vector3f.UnitZ, -40f);
        bones.get(handBonesEnum.PROXIMALPHALANGE1.ordinal()).addChild(middlePhalange);
        bones.add(middlePhalange);

        middlePhalange = new Bone();
        middlePhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        middlePhalange.rotate(Vector3f.UnitZ, -20f);
        bones.get(handBonesEnum.PROXIMALPHALANGE2.ordinal()).addChild(middlePhalange);
        bones.add(middlePhalange);

        middlePhalange = new Bone();
        middlePhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        middlePhalange.rotate(Vector3f.UnitZ, 0f);
        bones.get(handBonesEnum.PROXIMALPHALANGE3.ordinal()).addChild(middlePhalange);
        bones.add(middlePhalange);

        middlePhalange = new Bone();
        middlePhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        middlePhalange.rotate(Vector3f.UnitZ, 20f);
        bones.get(handBonesEnum.PROXIMALPHALANGE4.ordinal()).addChild(middlePhalange);
        bones.add(middlePhalange);

        Bone distalPhalange = new Bone();
        distalPhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        distalPhalange.rotate(Vector3f.UnitZ, -40f);
        bones.get(handBonesEnum.MIDDLEPHALANGE1.ordinal()).addChild(distalPhalange);
        bones.add(distalPhalange);

        distalPhalange = new Bone();
        distalPhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        distalPhalange.rotate(Vector3f.UnitZ, -20f);
        bones.get(handBonesEnum.MIDDLEPHALANGE2.ordinal()).addChild(distalPhalange);
        bones.add(distalPhalange);

        distalPhalange = new Bone();
        distalPhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        distalPhalange.rotate(Vector3f.UnitZ, 0f);
        bones.get(handBonesEnum.MIDDLEPHALANGE3.ordinal()).addChild(distalPhalange);
        bones.add(distalPhalange);

        distalPhalange = new Bone();
        distalPhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        distalPhalange.rotate(Vector3f.UnitZ, 20f);
        bones.get(handBonesEnum.MIDDLEPHALANGE4.ordinal()).addChild(distalPhalange);
        bones.add(distalPhalange);

        distalPhalange = new Bone();
        distalPhalange.scale(new Vector3f(0.05f, 0.2f, 0.05f));
        distalPhalange.rotate(Vector3f.UnitZ, 40f);
        bones.get(handBonesEnum.PROXIMALPHALANGE5.ordinal()).addChild(distalPhalange);
        bones.add(distalPhalange);

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
            case KeyEvent.KEYCODE_9:  move(new Vector3f(0.1f, 0.1f, 0.1f));  break;
            case KeyEvent.KEYCODE_0:  move(new Vector3f(-0.1f, -0.1f, -0.1f));  break;
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
