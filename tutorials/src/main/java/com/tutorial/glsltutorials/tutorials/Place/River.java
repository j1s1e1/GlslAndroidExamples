package com.tutorial.glsltutorials.tutorials.Place;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Textures.TextureElement;

/**
 * Created by jamie on 6/13/15.
 */
public class River extends Exhibit {
    TextureElement water;
    public River ()
    {
        water = new TextureElement(R.drawable.water1);
        water.scale(1.0f);
        water.rotateShape(Vector3f.UnitX, 45f);
        water.move(0f, -0.8f, 0f);
    }

    public void move (Vector3f v)
    {
        water.move(v);
    }

    public void draw ()
    {
        water.draw();
    }
}
