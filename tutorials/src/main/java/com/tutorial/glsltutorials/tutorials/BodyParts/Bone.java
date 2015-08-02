package com.tutorial.glsltutorials.tutorials.BodyParts;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;
import com.tutorial.glsltutorials.tutorials.Shapes.Octahedron;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamie on 7/26/15.
 */
public class Bone {
    ArrayList<Shape> shapes;
    Vector3f sphere1position;
    Vector3f sphere2position;
    Bone parent;
    ArrayList<Bone> children = new ArrayList<Bone>();

    public Bone()
    {
        shapes = new ArrayList<Shape>();
        shapes.add(new LitMatrixSphere2(0.1f));
        shapes.add(new Octahedron(new Vector3f(1f, 1f, 1f), Colors.RED_COLOR));
        shapes.add(new LitMatrixSphere2(0.1f));
        shapes.get(0).setYOffset(0.45f);
        shapes.get(2).setYOffset(-0.45f);
        sphere1position = shapes.get(0).getOffset();
        sphere2position = shapes.get(2).getOffset();
    }

    public void draw()
    {
        for(Shape s : shapes)
        {
            s.draw();
        }
    }

    public void rotate(Vector3f axis, float angleDeg)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(axis, (float)Math.PI / 180.0f * angleDeg);
        for(Shape s : shapes)
        {
            s.rotateShape(sphere1position, rotation);
        }
        sphere2position = sphere2position.sub(sphere1position);
        sphere2position = Vector3f.transform(sphere2position, rotation);
        sphere2position = sphere2position.add(sphere1position);
        if (children.size() != 0)
        {
            for (Bone child : children)
            {
                child.move(sphere2position.sub(child.getSphere1Position()));
            }
        }
    }

    public void rotate(Matrix4f rotation)
    {
        for(Shape s : shapes)
        {
            s.rotateShape(sphere1position, rotation);
        }
        sphere2position = sphere2position.sub(sphere1position);
        sphere2position = Vector3f.transform(sphere2position, rotation);
        sphere2position = sphere2position.add(sphere1position);
        if (children.size() != 0)
        {
            for (Bone child : children)
            {
                child.move(sphere2position.sub(child.getSphere1Position()));
            }
        }
    }

    public void scale(Vector3f scale)
    {
        for(Shape s : shapes)
        {
            s.scale(scale);
        }
        sphere1position.x = sphere1position.x * scale.x;
        sphere1position.y = sphere1position.y * scale.y;
        sphere1position.z = sphere1position.z * scale.z;
        sphere2position.x = sphere2position.x * scale.x;
        sphere2position.y = sphere2position.y * scale.y;
        sphere2position.z = sphere2position.z * scale.z;
        if (children.size() != 0)
        {
            for (Bone child : children)
            {
                child.move(sphere2position.sub(child.getSphere1Position()));
            }
        }
    }

    public void move(Vector3f offset)
    {
        for(Shape s : shapes)
        {
            s.move(offset);
        }
        sphere1position = sphere1position.add(offset);
        sphere2position = sphere2position.add(offset);
        if (children.size() != 0)
        {
            for (Bone child : children)
            {
                child.move(sphere2position.sub(child.getSphere1Position()));
            }
        }
    }

    public Vector3f getSphere1Position()
    {
        return sphere1position;
    }

    public void setParent(Bone parentIn)
    {
        parent = parentIn;
    }

    public void addChild(Bone childIn)
    {
        childIn.move(sphere2position.sub(childIn.getSphere1Position()));
        childIn.setParent(this);
        children.add(childIn);
    }

    public String getBoneInfo()
    {
        StringBuilder result = new StringBuilder();
        result.append("sphere1position = " + sphere1position.toString());
        result.append("sphere2position = " + sphere2position.toString());
        return result.toString();
    }

    public void setProgram(int program)
    {
        for(Shape s : shapes)
        {
            s.setProgram(program);
        }
    }
}
