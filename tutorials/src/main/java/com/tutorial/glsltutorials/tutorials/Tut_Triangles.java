package com.tutorial.glsltutorials.tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Shapes.Triangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jamie on 5/26/14.
 */
public class Tut_Triangles extends TutorialBase {
    public class TriangleList {
        private List<Triangle> list;

        public TriangleList() {
            this.list = new ArrayList<Triangle>();
        }

        public void clear() {
            this.list.clear();
        }

        public void add(Triangle triangle) {
            this.list.add(triangle);
        }

        public Iterator<Triangle> iterator() {
            return this.list.iterator();
        }

        public Triangle get(int value) {
            return this.list.get(value);
        }
    }

    static TriangleList triangles;

    private void SetupTriangles()
    {
        triangles = new TriangleList();
        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                Triangle new_triangle = new Triangle(new float[]{0.0f, 0.0f, 0.0f, -0.01f, -0.1f, 0f, 0.01f, -0.1f, 0f});
                new_triangle.Move(-0.5f+ i * 0.2f, -0.5f+ j * 0.2f);
                triangles.add(new_triangle);
            }
        }
    }

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init()
    {
        SetupTriangles();
    }


    private static void DrawTriangles()
    {
        float angle = 0;
        float[] axis = new float[]{ 0.0f,0.0f,1.0f};
        for(Iterator<Triangle> i = triangles.iterator(); i.hasNext(); ) {
            Triangle triangle = i.next();
            triangle.rotate(angle, axis);
            triangle.draw();
            angle = angle + 1;
        }
    }

    public void display()
    {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        DrawTriangles();
    }
}
