package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Creatures.Animal;
import com.tutorial.glsltutorials.tutorials.Creatures.Dragonfly3d;
import com.tutorial.glsltutorials.tutorials.Creatures.FireFly3d;
import com.tutorial.glsltutorials.tutorials.Creatures.LadyBug3d;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;

import java.util.ArrayList;

/**
 * Created by jamie on 7/12/15.
 */
public class Tut_SphericalCoordinates extends TutorialBase {
    ArrayList<Animal> animals;
    float totalScale = 1.0f;
    float minScale = 0.1f;
    float maxScale = 10f;

    float r = 2f;
    float theta = 0f;
    float phi = 0f;

    int systemMovementMatrixUnif;
    int rotationMatrixUnif;

    int sphericalProgram;

    Matrix4f spericalTransform = Matrix4f.Identity();
    Matrix4f sphericalScale = Matrix4f.Identity();
    Matrix4f sphericalTranslation = Matrix4f.Identity();
    Matrix4f dragonflyRotation = Matrix4f.Identity();

    Matrix4f rotationTheta = Matrix4f.Identity();
    Matrix4f rotationPhi = Matrix4f.Identity();
    Matrix4f rotationMatrix = Matrix4f.Identity();

    animal_enum currentAnimal = animal_enum.DRAGONFLY;
    boolean nextAnimal = false;
    boolean addAnimal = false;
    boolean rotateWorld = false;
    float worldRotationAngle = 0.1f;
    Vector3f worldRotationAxis = new Vector3f(0f, 1f, 0.5f);

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

    private void setupSphericalAnimal(Animal animal)
    {
        animal.clearAutoMove();
        animal.setProgram(sphericalProgram);
        animal.setSystemMatrix(spericalTransform);
        animal.setRotationMatrix(rotationMatrix);
    }

    protected void init()
    {
        animals = new ArrayList<>();
        Animal animal = new Dragonfly3d();


        sphericalProgram = Programs.addProgram(VertexShaders.spherical_lms, FragmentShaders.lms_fragmentShaderCode);
        spericalTransform = Matrix4f.CreateRotationY((float) Math.PI);
        spericalTransform = Matrix4f.Identity();

        sphericalScale = Matrix4f.createScale(0.25f);
        sphericalTranslation = Matrix4f.createTranslation(new Vector3f(r, 0.0f, 0.0f));
        updateSphericalMatrix();

        setupSphericalAnimal(animal);
        animals.add(animal);
        for (int i = 0; i < 12; i++)
        {
            incrementAnimal();
            addAnimal();
        }
    }

    public void display()
    {
        clearDisplay();
        float thetaOffset = 0f;
        float phiOffset = 0f;
        float thetaStep = 180 / animals.size();
        for (Animal a : animals)
        {
            a.draw();
            a.setSystemMatrix(spericalTransform);
            a.setRotationMatrix(rotationMatrix);
            setThetaPhiOffset(thetaOffset, phiOffset);
            thetaOffset = thetaOffset + thetaStep;
            phiOffset = phiOffset + thetaStep * 2f;
        }
        if (nextAnimal)
        {
            nextAnimal = false;
            animals = new ArrayList<>();
            incrementAnimal();
            addAnimal();
        }
        if (addAnimal)
        {
            addAnimal = false;
            incrementAnimal();
            addAnimal();
        }
        if (rotateWorld)
        {
            Shape.rotateWorld(worldRotationAxis, worldRotationAngle);
        }
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


    public void SetScale(float scale) {
        if ((totalScale * scale) > minScale)
        {
            if ((totalScale * scale) < maxScale) {
                totalScale = totalScale * scale;
                Shape.scaleWorldToCameraMatrix(scale);
            }
        }
    }

    private void updateSphericalMatrix()
    {
        spericalTransform = Matrix4f.mul(sphericalTranslation, sphericalScale);
        spericalTransform = Matrix4f.mul(dragonflyRotation, spericalTransform);
    }

    private void rotate(Vector3f rotationAxis, float angle)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(rotationAxis, (float)Math.PI / 180.0f * angle);
        dragonflyRotation = Matrix4f.mul(rotation, dragonflyRotation);
        updateSphericalMatrix();
    }

    private void translate(Vector3f translation)
    {
        Matrix4f translationMatrix = Matrix4f.createTranslation(translation);
        sphericalTranslation = Matrix4f.mul(translationMatrix, sphericalTranslation);
        updateSphericalMatrix();
    }

    public void receiveMessage(String message)
    {
        String[] words = message.split(" ");
        switch (words[0])
        {
            case "ZoomIn": SetScale(1.05f); break;
            case "ZoomOut": SetScale(1f / 1.05f); break;
            case "RotateX":  rotate(Vector3f.UnitX, Float.parseFloat(words[1])); break;
            case "RotateY":  rotate(Vector3f.UnitY, Float.parseFloat(words[1])); break;
            case "RotateZ":  rotate(Vector3f.UnitZ, Float.parseFloat(words[1])); break;
            case "RotateX+": rotate(Vector3f.UnitX, 5f); break;
            case "RotateX-": rotate(Vector3f.UnitX, -5f); break;
            case "RotateY+": rotate(Vector3f.UnitY, 5f); break;
            case "RotateY-": rotate(Vector3f.UnitY, -5f); break;
            case "RotateZ+": rotate(Vector3f.UnitZ, 5f); break;
            case "RotateZ-": rotate(Vector3f.UnitZ, -5f); break;
            case "SetScale":
                if (words.length == 2) {
                    Shape.resetWorldToCameraMatrix();
                    Shape.scaleWorldToCameraMatrix(Float.parseFloat(words[1]));
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

    private void ChangeRadius(float rChange)
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

    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_1: ChangeRadius(0.05f); break;
            case KeyEvent.KEYCODE_2: ChangeRadius(-0.05f); break;
            case KeyEvent.KEYCODE_3: changeTheta(5f); break;
            case KeyEvent.KEYCODE_4: changeTheta(-5f); break;
            case KeyEvent.KEYCODE_5: changePhi(5f); break;
            case KeyEvent.KEYCODE_6: changePhi(-5f); break;
            case KeyEvent.KEYCODE_I:
                result.append("dragonflyMatrix = " + spericalTransform.toString());
                result.append("rotationMatrix = " + rotationMatrix.toString());
                break;
            case KeyEvent.KEYCODE_NUMPAD_8: translate(new Vector3f(0f, 0.05f, 0f)); break;
            case KeyEvent.KEYCODE_NUMPAD_2: translate(new Vector3f(0f, -0.05f, 0f)); break;
            case KeyEvent.KEYCODE_NUMPAD_6: translate(new Vector3f(0.05f, 0f, 0f)); break;
            case KeyEvent.KEYCODE_NUMPAD_4: translate(new Vector3f(-0.05f, 0f, 0f)); break;

            case KeyEvent.KEYCODE_7: rotate(Vector3f.UnitY, 5f); break;
            case KeyEvent.KEYCODE_8: rotate(Vector3f.UnitY, -5f); break;
            case KeyEvent.KEYCODE_9: rotate(Vector3f.UnitX, 5f); break;
            case KeyEvent.KEYCODE_0: rotate(Vector3f.UnitX, -5f); break;
            case KeyEvent.KEYCODE_A: addAnimal = true; break;
            case KeyEvent.KEYCODE_N: nextAnimal = true; break;
            case KeyEvent.KEYCODE_R:
                rotateWorld = !rotateWorld;
                break;
        }
        return result.toString();
    }

    public void orientationEvent(float azimuth_angle, float pitch_angle, float roll_angle)
    {
        Matrix4f rotationX = Matrix4f.CreateRotationX(pitch_angle);
        Matrix4f rotationY = Matrix4f.CreateRotationY(azimuth_angle);
        Matrix4f rotationZ = Matrix4f.CreateRotationZ(roll_angle);
        dragonflyRotation = Matrix4f.mul(rotationY, rotationX);
        dragonflyRotation = Matrix4f.mul(rotationZ, dragonflyRotation);
        updateSphericalMatrix();
    }
}
