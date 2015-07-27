package com.tutorial.glsltutorials.tutorials.BodyParts;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.ArrayList;

/**
 * Created by jamie on 7/26/15.
 */
public class Leg {
    ArrayList<Bone> bones;

    int crawlCountOffset = 0;

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

    public void crawl(int crawlCount)
    {
        switch ((crawlCount + crawlCountOffset) % 8)
        {
            case 0:
                rotateAngles(new Vector3f[]{Vector3f.UnitX, Vector3f.UnitX, Vector3f.UnitX}, new float[]{2f, 2f, 0f});
                break;
            case 1:
                rotateAngles(new Vector3f[]{Vector3f.UnitX, Vector3f.UnitX, Vector3f.UnitX}, new float[]{3f, 3f, 0f});
                break;
            case 2:
                rotateAngles(new Vector3f[]{Vector3f.UnitX, Vector3f.UnitX, Vector3f.UnitX}, new float[]{-2f, -2f, 0f});
                break;
            case 3:
                rotateAngles(new Vector3f[]{Vector3f.UnitX, Vector3f.UnitX, Vector3f.UnitX}, new float[]{-3f, -3f, 0f});
                break;
            case 4:
                rotateAngles(new Vector3f[]{Vector3f.UnitX, Vector3f.UnitX, Vector3f.UnitX}, new float[]{0f, 0f, 0f});
                break;
            case 5:
                rotateAngles(new Vector3f[]{Vector3f.UnitX, Vector3f.UnitX, Vector3f.UnitX}, new float[]{0f, 0f, 0f});
                break;
            case 6:
                rotateAngles(new Vector3f[]{Vector3f.UnitX, Vector3f.UnitX, Vector3f.UnitX}, new float[]{0f, 0f, 0f});
                break;
            case 7:
                rotateAngles(new Vector3f[]{Vector3f.UnitX, Vector3f.UnitX, Vector3f.UnitX}, new float[]{0f, 0f, 0f});
                break;
        }
    }
}
