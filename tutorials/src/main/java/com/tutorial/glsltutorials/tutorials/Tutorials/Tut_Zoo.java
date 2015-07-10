package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Creatures.Animal;
import com.tutorial.glsltutorials.tutorials.Creatures.Cat;
import com.tutorial.glsltutorials.tutorials.Creatures.Dog;
import com.tutorial.glsltutorials.tutorials.Creatures.DragonFly3d;
import com.tutorial.glsltutorials.tutorials.Creatures.FireFly3d;
import com.tutorial.glsltutorials.tutorials.Creatures.LadyBug3d;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Place.Cage;
import com.tutorial.glsltutorials.tutorials.Place.Exhibit;
import com.tutorial.glsltutorials.tutorials.Place.Grass;
import com.tutorial.glsltutorials.tutorials.Place.River;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamie on 6/13/15.
 */
public class Tut_Zoo extends TutorialBase {
    ArrayList<Animal> animals;
    Exhibit exhibit;
    boolean nextAnimal = false;
    boolean addAnimal = false;
    boolean nextExhibit = false;

    protected void init()
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        animals = new ArrayList<Animal>();
        
        animals.add(new Animal());
        exhibit = new Grass();
        setupDepthAndCull();
    }

    public  void display()
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        exhibit.draw();
        for(Animal a : animals)
        {
            a.draw();
        }
        if (addAnimal)
        {
            addAnimal = false;
            selectNextAnimal();
        }
        if (nextAnimal)
        {
            nextAnimal = false;
            animals.clear();
            selectNextAnimal();
        }
        if (nextExhibit)
        {
            nextExhibit = false;
            selectNextExhibit();
        }
        updateDisplayOptions();
    }

    private void selectNextAnimal()
    {
        currentAnimal = currentAnimal.next();
        switch (currentAnimal)
        {
            case CAT: animals.add(new Cat());
                break;
            case DOG: animals.add(new Dog());
                break;
            case DRAGONFLY: animals.add(new DragonFly3d());
                break;
            case LADYBUG: animals.add(new LadyBug3d());
                break;
            case FIREFLY: animals.add(new FireFly3d());
                break;
        }
    }

    private void selectNextExhibit()
    {
        currentExhibit = currentExhibit.next();
        switch (currentExhibit)
        {
            case DEFAULT: exhibit = new Exhibit(); break;
            case CAGE: exhibit = new Cage(); break;
            case GRASS: exhibit = new Grass(); break;
            case RIVER: exhibit = new River(); break;
        }
    }

    enum AnimalsEnum
    {
        CAT,
        DOG,
        DRAGONFLY,
        LADYBUG,
        FIREFLY;
        private static AnimalsEnum[] vals = values();
        public AnimalsEnum next()
        {
            return vals[(this.ordinal()+1) % vals.length];
        }
    }

    enum ExhibitsEnum
    {
        DEFAULT,
        CAGE,
        GRASS,
        RIVER;
        private static ExhibitsEnum[] vals = values();
        public ExhibitsEnum next()
        {
            return vals[(this.ordinal()+1) % vals.length];
        }
    }

    AnimalsEnum currentAnimal = AnimalsEnum.CAT;

    ExhibitsEnum currentExhibit = ExhibitsEnum.DEFAULT;

    boolean moveExhibit = false;

    private void move(Vector3f v)
    {
        if (moveExhibit)
        {
            exhibit.move(v);
        }
        else
        {
            for(Animal a : animals)
            {
                a.move(v);
            }
        }
    }

    public  String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        if (displayOptions)
        {
            setDisplayOptions(keyCode);
        }
        else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_1:
                    break;
                case KeyEvent.KEYCODE_NUMPAD_8:
                    move(new Vector3f(0.0f, 0.1f, 0.0f));
                    break;
                case KeyEvent.KEYCODE_NUMPAD_2:
                    move(new Vector3f(0.0f, -0.1f, 0.0f));
                    break;
                case KeyEvent.KEYCODE_NUMPAD_4:
                    move(new Vector3f(-0.1f, 0.0f, 0.0f));
                    break;
                case KeyEvent.KEYCODE_NUMPAD_6:
                    move(new Vector3f(0.1f, 0.0f, 0.0f));
                    break;
                case KeyEvent.KEYCODE_NUMPAD_7:
                    move(new Vector3f(0.0f, 0.0f, 0.1f));
                    break;
                case KeyEvent.KEYCODE_NUMPAD_3:
                    move(new Vector3f(0.0f, 0.0f, -0.1f));
                    break;
                case KeyEvent.KEYCODE_A:
                    addAnimal = true;
                    break;
                case KeyEvent.KEYCODE_N:
                    nextAnimal = true;
                    break;
                case KeyEvent.KEYCODE_E:
                    nextExhibit = true;
                    break;
                case KeyEvent.KEYCODE_M:
                    moveExhibit = !moveExhibit;
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    displayOptions = true;
                    break;
            }
        }
        return result.toString();
    }
}
