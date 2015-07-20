package com.tutorial.glsltutorials.tutorials.Creatures;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Movement.Movement;
import com.tutorial.glsltutorials.tutorials.Movement.SphericalMovement;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;

/**
 * Created by jamie on 6/13/15.
 */
public class Animal
{
    int programNumber = -1;
    int theProgram = -1;
    int systemMovementMatrixUnif = -1;
    int rotationMatrixUnif = -1;
    LitMatrixSphere2 s;
    protected boolean autoMove = true;
    protected Movement movement;

    public Animal()
    {
        s = new LitMatrixSphere2(0.1f);
    }

    public void move(Vector3f v)
    {
        s.move(v);
    }


    public void draw()
    {
        s.draw();
    }

    public int getProgram()
    {
        return programNumber;
    }

    public void setProgram(int program)
    {
        programNumber = program;
        theProgram = Programs.getProgram(programNumber);
        GLES20.glUseProgram(theProgram);
        systemMovementMatrixUnif = GLES20.glGetUniformLocation(theProgram, "systemMovementMatrix");
        rotationMatrixUnif = GLES20.glGetUniformLocation(theProgram, "rotationMatrix");
        GLES20.glUseProgram(0);
    }

    public void setSystemMatrix(Matrix4f matrix)
    {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(systemMovementMatrixUnif, 1, false, matrix.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void setRotationMatrix(Matrix4f matrix)
    {
        GLES20.glUseProgram(theProgram);
        GLES20.glUniformMatrix4fv(rotationMatrixUnif, 1, false, matrix.toArray(), 0);
        GLES20.glUseProgram(0);
    }

    public void setAutoMove()
    {
        autoMove = true;
    }

    public void clearAutoMove()
    {
        autoMove = false;
    }

    public void setSphericalMovement(int programNumber)
    {
        setSphericalMovement(programNumber, 1f, 1f);
    }

    public void setSphericalMovement(int programNumber, float thetaStep, float phiStep)
    {
        movement = new SphericalMovement(programNumber, thetaStep, phiStep);
    }

    public String getMovementInfo()
    {
        return movement.movementInfo();
    }

    public void translate(Vector3f offset)
    {
        movement.translate(offset);
    }

}
