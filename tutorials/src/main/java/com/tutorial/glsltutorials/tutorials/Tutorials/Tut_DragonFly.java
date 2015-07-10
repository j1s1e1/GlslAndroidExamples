package com.tutorial.glsltutorials.tutorials.Tutorials;

import com.tutorial.glsltutorials.tutorials.Creatures.DragonFly;

/**
 * Created by jamie on 5/25/15.
 */
public class Tut_DragonFly extends TutorialBase {

    DragonFly dragonFly;
    protected void init()
    {
        dragonFly = new DragonFly(0, 0, 0);
    }

    public void display()
    {
        clearDisplay();
        dragonFly.draw();
    }
}
