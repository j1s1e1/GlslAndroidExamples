package com.tutorial.glsltutorials.tutorials.BodyParts;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.ArrayList;

/**
 * Created by jamie on 7/26/15.
 */
public class Leg {
    ArrayList<Bone> bones;

    int crawlCountOffset = 0;

    static {setCrawlRotations();}

    public Leg (int boneCount)
    {
        bones = new ArrayList<Bone>();
        for (int i = 0; i < boneCount; i++)
        {
            Bone bone = new Bone();
            if (i > 0)
            {
                bones.get(i-1).addChild(bone);
            }
            bones.add(bone);
        }
    }

    public void scale(Vector3f v)
    {
        for (Bone b : bones)
        {
            b.scale(v);
        }
    }

    public void move(Vector3f v)
    {
        bones.get(0).move(v);
    }

    public void rotateAngles(Vector3f[] axis, float[] angles)
    {
        for (int i = 0; i < bones.size(); i++)
        {
            if (i < axis.length) bones.get(i).rotate(axis[i], angles[i]);
        }
    }

    public void rotateAngles(Matrix4f[] rotations)
    {
        for (int i = 0; i < bones.size(); i++)
        {
            if (i < rotations.length) bones.get(i).rotate(rotations[i]);
        }
    }

    public void draw()
    {
        for (Bone b : bones)
        {
            b.draw();
        }
    }

    public void setProgram(int program)
    {
        for (Bone b : bones)
        {
            b.setProgram(program);
        }
    }

    public void setCrawlCountOffset(int crawlCountOffsetIn)
    {
        crawlCountOffset = crawlCountOffsetIn;
    }

    static Matrix4f[][] crawlRotations;

    private static Matrix4f[] createRotationsX(float[] anglesDeg)
    {
        Matrix4f[] result = new Matrix4f[anglesDeg.length];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = Matrix4f.CreateRotationX((float)Math.PI / 180.0f * anglesDeg[i]);
        }
        return result;
    }

    private static void setCrawlRotations()
    {
        crawlRotations = new Matrix4f[8][];
        crawlRotations[0] = createRotationsX(new float[]{2f, 2f, 0f});
        crawlRotations[1] = createRotationsX(new float[]{3f, 3f, 0f});
        crawlRotations[2] = createRotationsX(new float[]{-2f, -2f, 0f});
        crawlRotations[3] = createRotationsX(new float[]{-3f, -3f, 0f});
        crawlRotations[4] = createRotationsX(new float[]{0f, 0f, 0f});
        crawlRotations[5] = createRotationsX(new float[]{0f, 0f, 0f});
        crawlRotations[6] = createRotationsX(new float[]{0f, 0f, 0f});
        crawlRotations[7] = createRotationsX(new float[]{0f, 0f, 0f});

    }

    public void crawl(int crawlCount)
    {
        rotateAngles(crawlRotations[(crawlCount + crawlCountOffset) % 8]);
    }
}
