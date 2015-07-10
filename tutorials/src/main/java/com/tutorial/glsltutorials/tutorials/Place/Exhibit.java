package com.tutorial.glsltutorials.tutorials.Place;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixBlock2;

import java.util.ArrayList;

/**
 * Created by jamie on 6/13/15.
 */
public class Exhibit {
    protected ArrayList<LitMatrixBlock2> bars;
    private Vector3f offset = new Vector3f();

    public Exhibit ()
    {
        bars = new ArrayList<LitMatrixBlock2>();
        float height = 0.8f;
        float xstart = -0.8f;
        float xend = 0.8f;
        float xstep = 0.2f;
        float ystart = -0.8f;
        float yend = 0.8f;
        for (float x = xstart; x <= xend; x += xstep)
        {
            LitMatrixBlock2 newBar = new LitMatrixBlock2(new Vector3f(0.05f, 1.6f, 0f), Colors.BLUE_COLOR);
            newBar.move(new Vector3f(x, 0f, 0f));
            bars.add(newBar);
        }
    }

    public void move(Vector3f v)
    {
        for (LitMatrixBlock2 l : bars)
        {
            l.move(v);
        }
        offset = offset.add(v);
    }

    public Vector3f getOffset()
    {
        return offset;
    }

    public void draw()
    {
        for (LitMatrixBlock2 l : bars)
        {
            l.draw();
        }
    }
}
