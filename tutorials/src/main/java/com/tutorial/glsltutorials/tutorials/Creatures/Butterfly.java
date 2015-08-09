package com.tutorial.glsltutorials.tutorials.Creatures;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.BodyParts.Wing;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Movement.BugMovement3D;
import com.tutorial.glsltutorials.tutorials.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by jamie on 8/9/15.
 */
public class Butterfly extends Bug3d {
    Wing[] wings;
    int rotateCount = 0;
    int rotateAngle = 0;
    int rotateDirection = 1;

    int startFlap = 0;
    int startupCount = 0;

    enum wingTextureEnum
    {
        SWALLOWTAIL,
        MONARCH,
        BUTTERFLY2,
        BUTTERFLY3,
        MOTH,
        GREEN,
        DRAGONFLY;
        private static final List<wingTextureEnum> values =
                Collections.unmodifiableList(Arrays.asList(values()));
        private static final int size = values.size();
        static Random random = new Random();
        public static wingTextureEnum selectRandom()  {
            return values.get(random.nextInt(size));
        }
    }

    public Butterfly()
    {
        this(0, 0, 0);
    }

    public Butterfly(int x_in, int y_in, int z_in)
    {
        super(x_in, y_in, z_in);
        int scale = random.nextInt(5) + 5;
        startFlap = random.nextInt(30);
        wingTextureEnum wingTexture = wingTextureEnum.selectRandom();
        int wingTextureReference = 0;
        float flapAngle = 5f;
        switch (wingTexture)
        {
            case MONARCH: wingTextureReference = R.drawable.monarch; break;
            case SWALLOWTAIL: wingTextureReference = R.drawable.swallowtail256; break;
            case BUTTERFLY2: wingTextureReference = R.drawable.butterfly2; break;
            case BUTTERFLY3: wingTextureReference = R.drawable.butterfly3; break;
            case MOTH: wingTextureReference = R.drawable.moth1; break;
            case GREEN: wingTextureReference = R.drawable.greenbutterfly; break;
            case DRAGONFLY:
                wingTextureReference = R.drawable.dragonfly1;
                flapAngle = 2f;
                break;
        }
        wings = new Wing[2];
        wings[0] = new Wing(wingTextureReference);
        wings[0].rotate(Vector3f.UnitZ, 180f);
        wings[0].scale(new Vector3f(scale / 10f, scale / 10f, scale / 10f));
        wings[0].setFlapAngle(flapAngle);
        wings[1] = new Wing(wingTextureReference);
        wings[1].rotate(Vector3f.UnitZ, 180f);
        wings[1].rotate(Vector3f.UnitY, 180f);
        wings[1].setFlapAngle(-flapAngle);
        wings[1].setRotationAxis(new Vector3f(0f, -1f, 0f));
        wings[1].scale(new Vector3f(scale / 10f, scale / 10f, scale / 10f));
        movement = new BugMovement3D(new Vector3f(0.02f, 0.02f, 0.02f));
        movement.setLimits(new Vector3f(-0.6f, -0.6f, -0.6f), new Vector3f(0.6f, 0.6f, 0.6f));
        wings[0].rotate(Vector3f.UnitX, 270f);
        wings[1].rotate(Vector3f.UnitX, 270f);
    }

    public void setFlapEnable(boolean flapEnable)
    {
        for (Wing w : wings)
        {
            w.setFlapEnable(flapEnable);
        }
    }

    public void draw()
    {
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        Textures.enableTextures();
        for (Wing w : wings)
        {
            if (startFlap == startupCount)
            {
                w.setFlapEnable(true);
            }
            w.Draw();
        }
        if (startupCount < startFlap + 1)
        {
            startupCount++;
        }
        if (autoMove)
        {
            rotateCount++;
            for (Wing w : wings)
            {
                w.move(position.sub(lastPosition));
                if ((rotateCount % (18 * 1)) == 0)
                {
                    rotateAngle++;
                    if(rotateAngle > 135)
                    {
                        rotateAngle = 0;
                        rotateDirection = -rotateDirection;
                    }
                    w.rotate(Vector3f.UnitX, rotateDirection);
                }
            }
            move();
        }
    }

    public void setProgram(int program)
    {
        super.setProgram(program);
        for (Wing w : wings)
        {
            w.setProgram(program);
        }
    }
}
