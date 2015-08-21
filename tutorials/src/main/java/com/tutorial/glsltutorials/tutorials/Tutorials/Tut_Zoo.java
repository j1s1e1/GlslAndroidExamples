package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.tutorial.glsltutorials.tutorials.Creatures.Animal;
import com.tutorial.glsltutorials.tutorials.Creatures.Butterfly;
import com.tutorial.glsltutorials.tutorials.Creatures.Cat;
import com.tutorial.glsltutorials.tutorials.Creatures.Dog;
import com.tutorial.glsltutorials.tutorials.Creatures.Dragonfly3d;
import com.tutorial.glsltutorials.tutorials.Creatures.FireFly3d;
import com.tutorial.glsltutorials.tutorials.Creatures.LadyBug3d;
import com.tutorial.glsltutorials.tutorials.Creatures.Scorpion;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Place.Cage;
import com.tutorial.glsltutorials.tutorials.Place.Exhibit;
import com.tutorial.glsltutorials.tutorials.Place.Grass;
import com.tutorial.glsltutorials.tutorials.Place.Meadow;
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
    boolean autoRotate = true;

    float totalScale = 1.0f;
    float minScale = 1f;
    float maxScale = 10f;

    Vector3f offset = new Vector3f();

    AnimalsEnum currentAnimal = AnimalsEnum.BUTTERFLY;

    ExhibitsEnum currentExhibit = ExhibitsEnum.DEFAULT;

    boolean moveExhibit = true;
    Vector4f backgroundColor = new Vector4f(32f/256f, 130f/256f, 180f/256f, 0.0f);
    boolean newBackgroundColor = false;

    protected void init()
    {
        sphericalProgram = Programs.addProgram(VertexShaders.spherical_lms, FragmentShaders.lms_fragmentShaderCode);
        GLES20.glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w);
        animals = new ArrayList<Animal>();

        for (int i = 0; i< 20; i++)
        {
            animals.add(new Butterfly());
        }
        exhibit = new Meadow();
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
            currentAnimal = currentAnimal.next();
            nextAnimal = false;
            animals.clear();
            selectNextAnimal();
        }
        if (nextExhibit)
        {
            nextExhibit = false;
            selectNextExhibit();
        }
        if (autoRotate)
        {
            yRotation += 0.1f;
            Shape.rotateWorld(offset, Vector3f.UnitY, 01f);
        }
        if (newBackgroundColor)
        {
            newBackgroundColor = false;
            GLES20.glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w);
        }
        updateDisplayOptions();
    }

    private void selectNextAnimal()
    {
        switch (currentAnimal)
        {
            case CAT: animals.add(new Cat()); break;
            case DOG: animals.add(new Dog()); break;
            case DRAGONFLY: animals.add(new Dragonfly3d()); break;
            case LADYBUG: animals.add(new LadyBug3d()); break;
            case FIREFLY: animals.add(new FireFly3d()); break;
            case SCORPION: animals.add(new Scorpion()); break;
            case BUTTERFLY: animals.add(new Butterfly()); break;
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
            case MEADOW: exhibit = new Meadow(); break;
        }
    }

    enum AnimalsEnum
    {
        CAT,
        DOG,
        DRAGONFLY,
        LADYBUG,
        FIREFLY,
        SCORPION,
        BUTTERFLY;
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
        RIVER,
        MEADOW;
        private static ExhibitsEnum[] vals = values();
        public ExhibitsEnum next()
        {
            return vals[(this.ordinal()+1) % vals.length];
        }
    }

    private boolean checkLimits(Vector3f v)
    {
        Vector3f test = offset.add(v);
        if (Math.abs(test.x) > totalScale) return false;
        if (Math.abs(test.y) > totalScale) return false;
        return Math.abs(test.z) <= totalScale;
    }

    private void move(Vector3f v)
    {
        if (moveExhibit)
        {
            if (checkLimits(v))
            {
                offset = offset.add(v);
                Shape.moveWorld(v);
            }
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
                    Shape.rotateWorld(offset, Vector3f.UnitY, yRotation);
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
                case KeyEvent.KEYCODE_V:
                    autoRotate = !autoRotate;
                    break;
            }
        }
        return result.toString();
    }


    public void scroll(float distanceX, float distanceY)
    {
        Vector3f movement = new Vector3f();
        if (Math.abs(distanceX) > Math.abs(distanceY))
        {
            if (distanceX > 0)
            {
                movement.x = -0.1f * (float)Math.cos(Math.PI / 180f * yRotation);
                movement.z = -0.1f * (float)Math.sin(Math.PI / 180f * yRotation);
            }
            else
            {
                movement.x = 0.1f * (float)Math.cos(Math.PI / 180f * yRotation);
                movement.z = 0.1f * (float)Math.sin(Math.PI / 180f * yRotation);
            }
        }
        else
        {
            if (distanceY > 0)
            {
                movement.y = 0.1f;
            }
            else
            {
                movement.y = -0.1f;
            }
        }
        move(movement);
    }

    public void touchEvent(int x_position, int y_position) throws Exception
    {
        int selectionX = x_position / (width / 2);
        int selectionY = y_position / (height / 2);
        switch (2 * selectionX + selectionY)
        {
            case 0: break;
            case 1: break;
            case 2: break;
            case 3: break;
        }
    }

    public void onLongPress(MotionEvent event)
    {
        Shape.resetWorldToCameraMatrix();
        offset = new Vector3f();
    }

    public void setScale(float scale) {
        if ((totalScale * scale) > minScale)
        {
            if ((totalScale * scale) < maxScale) {
                totalScale = totalScale * scale;
                Shape.scaleWorldToCameraMatrix(scale);
            }
        }
    }

    public void receiveMessage(String message) {
        String[] words = message.split(" ");
        switch (words[0]) {
            case "Background":
                if (words.length == 5) {
                    float x = Float.parseFloat(words[1]);
                    float y = Float.parseFloat(words[2]);
                    float z = Float.parseFloat(words[3]);
                    float w = Float.parseFloat(words[3]);
                    backgroundColor = new Vector4f(x, y, z, w);
                    newBackgroundColor = true;
                }
                break;
            case "ZoomIn":
                setScale(1.05f);
                break;
            case "ZoomOut":
                setScale(1f / 1.05f);
                break;
            case "SetScale":
                if (words.length == 2) {
                    Shape.resetWorldToCameraMatrix();
                    Shape.scaleWorldToCameraMatrix(Float.parseFloat(words[1]));
                    offset = new Vector3f();
                }
                break;
            case "SetOffset":
                if (words.length == 4) {
                    float x = Float.parseFloat(words[1]);
                    float y = Float.parseFloat(words[2]);
                    float z= Float.parseFloat(words[3]);
                    offset = new Vector3f(x, y, z);
                    Shape.setWorldOffset(offset);
                }
                break;
            case "SetScaleLimit":
                if (words.length == 3) {
                    minScale = Float.parseFloat(words[1]);
                    maxScale = Float.parseFloat(words[2]);
                }
                break;
        }
    }
}
