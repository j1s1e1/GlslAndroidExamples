package com.tutorial.glsltutorials.tutorials.Place;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixBlock2;

import java.util.ArrayList;

/**
 * Created by jamie on 6/13/15.
 */
public class Cage extends Exhibit {
    public Cage ()
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
            LitMatrixBlock2 newBar = new LitMatrixBlock2(new Vector3f(0.05f, 2 * height, 0.5f), Colors.RED_COLOR);
            newBar.move(new Vector3f(x, 0f, 0.5f));
            bars.add(newBar);
        }
        for (float x = xstart; x <= xend; x += xstep)
        {
            LitMatrixBlock2 newBar = new LitMatrixBlock2(new Vector3f(0.05f, 2 * height, 0.5f), Colors.YELLOW_COLOR);
            newBar.move(new Vector3f(x, 0f, -0.5f));
            bars.add(newBar);
        }
        LitMatrixBlock2 floor = new LitMatrixBlock2(new Vector3f(1.8f, 0.1f,1.8f), Colors.GREEN_COLOR);
        floor.move(new Vector3f(0, -0.8f, 0f));
        bars.add(floor);
    }

    public void draw()
    {
        for (LitMatrixBlock2 l : bars)
        {
            l.draw();
        }
    }
}
