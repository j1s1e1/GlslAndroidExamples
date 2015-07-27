package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Creatures.Animal;
import com.tutorial.glsltutorials.tutorials.Creatures.Cat;
import com.tutorial.glsltutorials.tutorials.Creatures.Dog;
import com.tutorial.glsltutorials.tutorials.Creatures.Dragonfly3d;
import com.tutorial.glsltutorials.tutorials.Creatures.FireFly3d;
import com.tutorial.glsltutorials.tutorials.Creatures.LadyBug3d;
import com.tutorial.glsltutorials.tutorials.Creatures.Scorpion;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Place.Cage;
import com.tutorial.glsltutorials.tutorials.Place.Exhibit;
import com.tutorial.glsltutorials.tutorials.Place.Grass;
import com.tutorial.glsltutorials.tutorials.Place.River;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

import java.util.ArrayList;

/**
 * Created by jamie on 6/13/15.
 */
public class Tut_Zoo extends TutorialBase {
    ArrayList<Animal> animals;
    Exhibit exhibit;
    boolean nextAnimal = false;
    boolean addAnimal = false;
    boolean nextExhibit = false;
    float yRotation = 0f;
    int sphericalProgram;

    protected void init()
    {
        sphericalProgram = Programs.addProgram(VertexShaders.spherical_lms, FragmentShaders.lms_fragmentShaderCode);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        animals = new ArrayList<Animal>();
        
        animals.add(new Scorpion());
        exhibit = new Cage();
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
            case DRAGONFLY: animals.add(new Dragonfly3d());
                break;
            case LADYBUG: animals.add(new LadyBug3d());
                break;
            case FIREFLY: animals.add(new FireFly3d());
                break;
            case SCORPION: animals.add(new Scorpion());
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
        FIREFLY,
        SCORPION;
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
                case KeyEvent.KEYCODE_I:
                    for (Animal a : animals) {
                        Log.i("Zoo ", a.getMovementInfo());
                        Log.i("Zoo ", a.getInfo());
                    }
                    break;
                case KeyEvent.KEYCODE_M:
                    moveExhibit = !moveExhibit;
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    displayOptions = true;
                    break;
                case KeyEvent.KEYCODE_R:
                    yRotation += 5f;
                    Shape.setWorldToCameraRotation(0f, yRotation, 0f);
                    break;
                case KeyEvent.KEYCODE_S:
                    for(Animal animal : animals)
                {
                    animal.setProgram(sphericalProgram);
                    animal.setSphericalMovement(sphericalProgram, 1f, 0f);
                }
                break;
                case KeyEvent.KEYCODE_T:
                    for (Animal animal : animals)
                {
                    if (animal.getAutoMove())
                    {
                        animal.clearAutoMove();
                    }
                    else
                    {
                        animal.setAutoMove();
                    }
                }
                break;
            }
        }
        return result.toString();
    }

    public void touchEvent(int x_position, int y_position) throws Exception
    {
        int selection = x_position / (width / 7);
        switch (selection)
        {
            case 0: move(new Vector3f(0.1f, 0f, 0f)); break;
            case 1:
                move(new Vector3f(-0.1f, 0f, 0f)); break;
            case 2: move(new Vector3f(0f, 0.1f, 0f)); break;
            case 3: yRotation += 5f; Shape.setWorldToCameraRotation(0f, yRotation, 0f);
                break;
            case 4: move(new Vector3f(0f, -0.1f, 0f)); break;
            case 5: move(new Vector3f(0f, 0f, 0.1f)); break;
            case 6: move(new Vector3f(0f, 0f, -0.1f)); break;
        }
    }
}
