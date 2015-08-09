package com.tutorial.glsltutorials.tutorials.Objects;

import com.tutorial.glsltutorials.tutorials.Blender.Blender;
import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.R;

import java.io.InputStream;

/**
 * Created by jamie on 8/9/15.
 */
public class Tree {
    Blender trunk;
    Blender leaves;

    public Tree ()
    {
        trunk = new Blender();
        trunk.readFromResource(R.raw.trunk);
        trunk.scale(new Vector3f(1f, 1f, 1f));
        trunk.setOffset(new Vector3f(0f, -1f, 0f));
        trunk.setColor(Colors.BROWN_COLOR);
        leaves = new Blender();
        leaves.readFromResource(R.raw.leaves);
        leaves.scale(new Vector3f(1f, 1f, 1f));
        leaves.setOffset(new Vector3f(0f, -1f, 0f));
        leaves.setColor(Colors.GREEN_COLOR);
    }

    public void scale(Vector3f scale)
    {
        trunk.scale(scale);
        leaves.scale(scale);
    }

    public void setOffset(float x, float y, float z)
    {
        trunk.setOffset(new Vector3f(x, y, z));
        leaves.setOffset(new Vector3f(x, y, z));
    }

    public void draw()
    {
        trunk.draw();
        leaves.draw();
    }
}
