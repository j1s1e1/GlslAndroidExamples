package com.tutorial.glsltutorials.tutorials.Creatures;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;

/**
 * Created by jamie on 6/13/15.
 */
public class Animal
{
    LitMatrixSphere2 s;

   public Animal()
    {
        s = new LitMatrixSphere2(0.1f);
    }

        public void move(Vector3f v)
    {
        s.move(v);
    }


        public void draw()
    {
        s.draw();
    }

}
