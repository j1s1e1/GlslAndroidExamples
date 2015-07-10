package com.tutorial.glsltutorials.tutorials.Place;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Textures.TextureElement;

/**
 * Created by jamie on 6/13/15.
 */
public class Grass extends Exhibit {
    TextureElement grass;

    public Grass ()
    {
        grass = new TextureElement(R.drawable.grass);
        grass.scale(1.0f);
        grass.rotateShape(Vector3f.UnitX, 45f);
        grass.move(0f, -0.8f, -0.5f);
        grass.setLightPosition(new Vector3f(0f, 0f, 1f));
    }

    public void draw ()
    {
        grass.draw();
    }
}
