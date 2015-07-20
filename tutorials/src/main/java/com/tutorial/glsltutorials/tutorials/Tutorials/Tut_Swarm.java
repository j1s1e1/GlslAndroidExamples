package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Creatures.Animal;
import com.tutorial.glsltutorials.tutorials.Creatures.Dragonfly3d;
import com.tutorial.glsltutorials.tutorials.Creatures.FireFly3d;
import com.tutorial.glsltutorials.tutorials.Creatures.LadyBug3d;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.AnalysisTools;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Shapes.TextureSphere;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jamie on 7/18/15.
 */
public class Tut_Swarm extends TutorialBase {
    TextureSphere planet;
    ArrayList<Animal> animals;

    int sphericalProgram;

    Matrix4f spericalTransform = Matrix4f.Identity();
    Matrix4f sphericalScale = Matrix4f.Identity();
    Matrix4f sphericalTranslation = Matrix4f.Identity();
    Matrix4f sphericalRotation = Matrix4f.Identity();

    Matrix4f rotationTheta = Matrix4f.Identity();
    Matrix4f rotationPhi = Matrix4f.Identity();
    Matrix4f rotationMatrix = Matrix4f.Identity();

    float r = 3f;
    float theta = 0f;
    float phi = 0f;

    float thetaStep = 15f;
    float phiStep = 45f;

    Vector3f offset = new Vector3f(0f, -2f, 1f);
    Vector3f minusOffset =  new Vector3f(0f, 2f, -1f);

    boolean rotateWorld = false;
    float worldRotationAngle = 1f;
    Vector3f worldRotationAxis = new Vector3f(0f, 1f, 0.5f);

    animal_enum currentAnimal = animal_enum.DRAGONFLY;

    Random random = new Random();
    boolean initComplete = false;
    float pitchAngle = 0f;
    float rollAngle = 0f;
    float azimuthAngle = 0f;

    enum animal_enum
    {
        DRAGONFLY,
        LADYBUG,
        FIREFLY;
        private static animal_enum[] vals = values();
        public animal_enum next()
        {
            return vals[(this.ordinal()+1) % vals.length];
        }
    }


    protected void init()
    {
        sphericalProgram = Programs.addProgram(VertexShaders.spherical_lms, FragmentShaders.lms_fragmentShaderCode);

        planet = new TextureSphere(2f, R.drawable.venus_magellan);
        Shape.moveWorld(offset);
        setupDepthAndCull();

        sphericalScale = Matrix4f.createScale(0.25f);
        sphericalTranslation = Matrix4f.createTranslation(new Vector3f(r, -8f, 4f));  // -8 due to scale

        animals = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            addAnimal();
            incrementAnimal();
        }
        initComplete = true;
    }

    private void setupSphericalAnimal(Animal animal)
    {
        animal.clearAutoMove();
        animal.setProgram(sphericalProgram);
        animal.setSystemMatrix(spericalTransform);
        animal.setRotationMatrix(rotationMatrix);

        float phiSpeed = (random.nextInt(20) - 10)/10f;
        float thetaSpeed = (random.nextInt(20) - 10)/10f;
        animal.setProgram(sphericalProgram);
        animal.setSphericalMovement(sphericalProgram, phiSpeed, thetaSpeed);
        animal.translate(new Vector3f(0f, -8f, 4f));
        //a.Translate(new Vector3(0f, 8f, -4f));
        animal.setAutoMove();
    }

    private void incrementAnimal()
    {
        currentAnimal = currentAnimal.next();
    }

    private void addAnimal()
    {
        Animal animal;
        switch(currentAnimal)
        {
            case DRAGONFLY: animal = new Dragonfly3d(); break;
            case LADYBUG: animal = new LadyBug3d(); break;
            case FIREFLY: animal = new FireFly3d(); break;
            default: animal = new Dragonfly3d(); break;
        }
        setupSphericalAnimal(animal);
        animals.add(animal);
    }

    public void display()
    {
        clearDisplay();
        planet.draw();
        for (Animal a : animals)
        {
            a.draw();
        }
        if (rotateWorld)
        {
            Shape.rotateWorld(minusOffset, worldRotationAxis, worldRotationAngle);
        }
    }

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        if (displayOptions)
        {
            setDisplayOptions(keyCode);
        }
        else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    displayOptions = true;
                    break;
                case KeyEvent.KEYCODE_1:
                    Shape.rotateWorld(minusOffset, Vector3f.UnitX, 1f);
                    break;
                case KeyEvent.KEYCODE_2:
                    Shape.rotateWorld(minusOffset, Vector3f.UnitX, -1f);
                    break;
                case KeyEvent.KEYCODE_3:
                    Shape.rotateWorld(minusOffset, Vector3f.UnitY, 1f);
                    break;
                case KeyEvent.KEYCODE_4:
                    Shape.rotateWorld(minusOffset, Vector3f.UnitY, -1f);
                    break;
                case KeyEvent.KEYCODE_5:
                    Shape.rotateWorld(minusOffset, Vector3f.UnitZ, 1f);
                    break;
                case KeyEvent.KEYCODE_6:
                    Shape.rotateWorld(minusOffset, Vector3f.UnitZ, -1f);
                    break;
                case KeyEvent.KEYCODE_0:
                    changePhi(3.6f);
                    changeTheta(1.8f);
                    updateSphericalMatrix();
                    break;
                case KeyEvent.KEYCODE_NUMPAD_6:
                    planet.move(new Vector3f(0.1f, 0.0f, 0.0f));
                    break;
                case KeyEvent.KEYCODE_NUMPAD_4:
                    planet.move(new Vector3f(-0.1f, 0.0f, 0.0f));
                    break;
                case KeyEvent.KEYCODE_NUMPAD_8:
                    planet.move(new Vector3f(0.0f, 0.1f, 0.0f));
                    break;
                case KeyEvent.KEYCODE_NUMPAD_2:
                    planet.move(new Vector3f(0.0f, -0.1f, 0.0f));
                    break;
                case KeyEvent.KEYCODE_NUMPAD_7:
                    planet.move(new Vector3f(0.0f, 0.0f, 0.1f));
                    break;
                case KeyEvent.KEYCODE_NUMPAD_3:
                    planet.move(new Vector3f(0.0f, 0.0f, -0.1f));
                    break;
                case KeyEvent.KEYCODE_I:
                    result.append("worldToCamera");
                    result.append(Shape.worldToCamera.toString());
                    result.append("modelToWorld");
                    result.append(planet.modelToWorld.toString());
                    result.append(AnalysisTools.calculateMatrixEffects(planet.modelToWorld));
                    Log.i("Swarm", "pitchAngle = " + String.valueOf(pitchAngle));
                    Log.i("Swarm", "rollAngle = " + String.valueOf(rollAngle));
                    Log.i("Swarm", "azimuthAngle = " + String.valueOf(azimuthAngle));
                    break;
                case KeyEvent.KEYCODE_P:
                    phiStep += 5f;
                    if(phiStep > 90f)
                    {
                        phiStep = 5f;
                    }
                    result.append("phiStep " + String.valueOf(phiStep));
                    break;
                case KeyEvent.KEYCODE_R:
                    rotateWorld = !rotateWorld;
                    break;
                case KeyEvent.KEYCODE_T:
                    thetaStep += 5f;
                    if(thetaStep > 90f)
                    {
                        thetaStep = 5f;
                    }
                    result.append("thetaStep " + String.valueOf(thetaStep));
                    break;
            }
        }
        return result.toString();
    }

    private void updateSphericalMatrix()
    {
        spericalTransform = Matrix4f.mul(sphericalTranslation, sphericalScale);
        spericalTransform = Matrix4f.mul(sphericalRotation, spericalTransform);
    }

    private void rotate(Vector3f rotationAxis, float angle)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(rotationAxis, (float)Math.PI / 180.0f * angle);
        sphericalRotation = Matrix4f.mul(rotation, sphericalRotation);
        updateSphericalMatrix();
    }

    private void translate(Vector3f translation)
    {
        Matrix4f translationMatrix = Matrix4f.createTranslation(translation);
        sphericalTranslation = Matrix4f.mul(translationMatrix, sphericalTranslation);
        updateSphericalMatrix();
    }

    private void changeRadius(float rChange)
    {
        r = r + rChange;
        Matrix4f translationMatrix = Matrix4f.createTranslation(new Vector3f(rChange, 0f, 0f));
        sphericalTranslation = Matrix4f.mul(translationMatrix, sphericalTranslation);
        updateSphericalMatrix();
    }

    private void changeTheta(float thetaChange)
    {
        theta = theta +  (float)Math.PI / 180.0f * thetaChange;
        rotationTheta = Matrix4f.CreateRotationY(theta);
        rotationPhi = Matrix4f.CreateFromAxisAngle(
                new Vector3f((float) Math.sin(theta), 0f, (float) Math.cos(theta)), phi);
        rotationMatrix = Matrix4f.mul(rotationTheta, rotationPhi);
    }

    private void changePhi(float phiChange)
    {
        phi = phi +  (float)Math.PI / 180.0f * phiChange;
        rotationPhi = Matrix4f.CreateFromAxisAngle(
                new Vector3f((float)Math.sin(theta), 0f, (float)Math.cos(theta)), phi);
        rotationMatrix = Matrix4f.mul(rotationTheta, rotationPhi);
    }

    private void setThetaPhiOffset(float thetaOffset, float phiOffset)
    {
        thetaOffset = theta + (float)Math.PI / 180.0f * thetaOffset;
        phiOffset = phi + (float)Math.PI / 180.0f * phiOffset;
        rotationTheta = Matrix4f.CreateRotationY(thetaOffset);
        rotationPhi = Matrix4f.CreateFromAxisAngle(new Vector3f((float)Math.sin(thetaOffset),
                0f, (float)Math.cos(thetaOffset)), phiOffset);
        rotationMatrix = Matrix4f.mul(rotationTheta, rotationPhi);
    }

    public void orientationEvent(float azimuth_angle, float pitch_angle, float roll_angle)
    {
        if (initComplete) {
            pitchAngle = pitch_angle;
            rollAngle = roll_angle;
            azimuthAngle = azimuth_angle;
            float divider = 1f;
            if (pitch_angle > 5) Shape.rotateWorld(minusOffset, Vector3f.UnitY, pitch_angle / divider);
            if (pitch_angle < -5) Shape.rotateWorld(minusOffset, Vector3f.UnitY, pitch_angle / divider);
            if (azimuth_angle > 5) Shape.rotateWorld(minusOffset, Vector3f.UnitX, azimuth_angle / divider);
            if (azimuth_angle < -5) Shape.rotateWorld(minusOffset, Vector3f.UnitX, azimuth_angle / divider);
            //if (roll_angle > Math.PI/4) Shape.rotateWorld(minusOffset, Vector3f.UnitZ, roll_angle / divider);
        }
    }
}
