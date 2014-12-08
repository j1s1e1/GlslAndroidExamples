package com.tutorial.glsltutorials.tutorials.Creatures;

import com.tutorial.glsltutorials.tutorials.Blender.Blender;
import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Movement.RandomMovement;
import com.tutorial.glsltutorials.tutorials.Objects.Missle;
import com.tutorial.glsltutorials.tutorials.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jamie on 10/20/14.
 */
public class Enemy {
    Blender tieFighter;
    float radius = 0.05f;
    static Random random = new Random();
    RandomMovement movement = new RandomMovement();
    Collisions collision = new Collisions();
    int framesPerMove = 10;
    int frameCount;
    float scale = 0.5f;
    boolean dead = false;
    public Enemy()
    {

        InputStream binaryBlenderData = Shader.context.getResources().openRawResource(R.raw.test_with_normals_binary);
        tieFighter = new Blender();
        tieFighter.readBinaryFile(binaryBlenderData);
        tieFighter.scale(new Vector3f(0.02f, 0.02f, 0.02f));
        float xOffset = random.nextInt(20)/10f - 1f;
        float yOffset = random.nextInt(20)/10f - 1f;
        float zOffset = random.nextInt(10)/10f - 0.5f;
        int colorSelection = random.nextInt(3);
        switch (colorSelection)
        {
            case 0:	tieFighter.setColor(Colors.RED_COLOR); break;
            case 1: tieFighter.setColor(Colors.GREEN_COLOR); break;
            case 2: tieFighter.setColor(Colors.BLUE_COLOR); break;
            default: tieFighter.setColor(Colors.YELLOW_COLOR); break;
        }
        xOffset = xOffset * scale;
        yOffset = yOffset * scale;
        zOffset = zOffset * scale;
        tieFighter.setOffset(new Vector3f(xOffset, yOffset, zOffset));
    }

    public boolean isDead()
    {
        return dead;
    }

    public void draw()
    {
        tieFighter.draw();
        if (frameCount < framesPerMove)
        {
            frameCount++;
        }
        else
        {
            frameCount = 0;
            Vector3f oldOffset = tieFighter.getOffset();
            tieFighter.setOffset(movement.newOffset(tieFighter.getOffset()));
            Vector3f newOffset = tieFighter.getOffset();
            Vector3f direction = newOffset.sub(oldOffset);
            tieFighter.face(direction);
        }
    }

    public void fireOn(ArrayList<Missle> missles)
    {
        for (Missle m : missles)
        {
            if (collision.detectColisions(tieFighter.getOffset(), m.GetOffsets()))
            {
                dead = true;
                break;
            }
        }
    }

    public void setProgram(int newProgram)
    {
        tieFighter.setProgram(newProgram);
    }
}
