package com.tutorial.glsltutorials.tutorials.Creatures;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;

/**
 * Created by jamie on 6/13/15.
 */
public class Cat extends Animal {
    LitMatrixSphere2 s1;
    LitMatrixSphere2 s2;

    public Cat()
    {
        s1 = new LitMatrixSphere2(0.1f);
        s2 = new LitMatrixSphere2(0.3f);
        s2.setOffset(new Vector3f(0.15f, -0.20f, 0f));

    }

    public void move(Vector3f v)
    {
        s1.move(v);
        s2.move(v);
    }

    public void Draw()
    {
        s1.draw();
        s2.draw();
    }
}
