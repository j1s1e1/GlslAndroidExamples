package com.tutorial.glsltutorials.tutorials.Place;

import com.tutorial.glsltutorials.tutorials.Blender.Blender;
import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Objects.Tree;
import com.tutorial.glsltutorials.tutorials.R;

import java.io.InputStream;
import java.util.Random;

/**
 * Created by jamie on 8/9/15.
 */
public class Meadow extends Exhibit {
    Blender grass;
    Tree[] trees;
    Blender[] dandelions;
    Random random = new Random();

    public Meadow ()
    {
        grass = new Blender();
        grass.readFromResource(R.raw.grass);
        grass.scale(new Vector3f(1f, 3f, 1f));
        grass.rotateShape(Vector3f.UnitX, 45f);
        grass.setColor(Colors.GREEN_COLOR);
        grass.setOffset(new Vector3f(0f, -1.1f, 0f));
        trees = new Tree[1];
        for (int i = 0; i < trees.length; i++)
        {
            trees[i] = AddTree();
        }
        dandelions = new Blender[2];
        for (int i = 0; i < dandelions.length; i++)
        {
            dandelions[i] = CreateDandelion();
        }
    }

    public Tree AddTree()
    {
        Tree tree = new Tree();
        float xOffset = (random.nextInt(20) - 10)/10f;
        float zOffset = (random.nextInt(20) - 10)/10f;
        tree.setOffset(xOffset, -1f, zOffset);
        float scale = (random.nextInt(100) + 100)/1000f;
        tree.scale(new Vector3f(scale));
        return tree;
    }

    private Blender CreateDandelion()
    {
        Blender dandelion = new Blender();
        dandelion.readFromResource(R.raw.dandelion);
        dandelion.setColor(Colors.YELLOW_COLOR);
        dandelion.rotateShape(Vector3f.UnitX, 45f);
        float xOffset = (random.nextInt(20) - 10) / 10f;
        float yOffset = (random.nextInt(20) - 10) / 100f;
        float zOffset = (random.nextInt(20) - 10) / 10f;
        dandelion.setOffset(new Vector3f(xOffset, -1f + yOffset, zOffset));
        int rotation = random.nextInt(360);
        float scale = (random.nextInt(7) + 3) / 10f;
        dandelion.scale(new Vector3f(scale, scale, scale));
        dandelion.rotateShape(Vector3f.UnitY, rotation);
        dandelion.scale(new Vector3f(0.2f, 0.2f, 0.2f));
        return dandelion;
    }

    public void draw ()
    {
        grass.draw();
        for (Tree tree : trees)
        {
            tree.draw();
        }
        for (Blender b : dandelions)
        {
            b.draw();
        }
    }
}
