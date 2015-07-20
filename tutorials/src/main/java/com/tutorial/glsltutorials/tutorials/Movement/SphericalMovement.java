package com.tutorial.glsltutorials.tutorials.Movement;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;

/**
 * Created by jamie on 7/19/15.
 */
public class SphericalMovement extends Movement {
    int programNumber = -1;
    int theProgram = -1;
    int systemMovementMatrixUnif = -1;
    int rotationMatrixUnif = -1;
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

    float thetaStep = 1f;
    float phiStep = 1f;

    Vector3f radiusBasis;
    Vector3f thetaBasis;
    Vector3f phiBasis;

    public SphericalMovement(int programNumberIn) {
        this(programNumberIn, 1f, 1f);
    }

    public SphericalMovement(int programNumberIn, float thetaStepIn, float phiStepIn)
    {
        programNumber = programNumberIn;
        thetaStep = thetaStepIn;
        phiStep = phiStepIn;
        theProgram = Programs.getProgram(programNumber);
        GLES20.glUseProgram(theProgram);
        systemMovementMatrixUnif = GLES20.glGetUniformLocation(theProgram, "systemMovementMatrix");
        rotationMatrixUnif = GLES20.glGetUniformLocation(theProgram, "rotationMatrix");
        GLES20.glUseProgram(0);
        sphericalTranslation = Matrix4f.createTranslation(new Vector3f(r, 0.0f, 0.0f));
        setSystemMatrix(sphericalTranslation);
        setRotationMatrix(Matrix4f.Identity());
        setScale(0.25f);
    }

    private void updateSphericalMatrix() {
        spericalTransform = Matrix4f.mul(sphericalTranslation, sphericalScale);
        spericalTransform = Matrix4f.mul(sphericalRotation, spericalTransform);
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

    public Vector3f newOffset(Vector3f oldOffset)
    {
        changeRadius(0f);
        changePhi(phiStep);
        changeTheta(thetaStep);
        sphericalRotation = Matrix4f.CreateRotationY(180f);
        updateSphericalMatrix();
        setSystemMatrix(spericalTransform);
        setRotationMatrix(rotationMatrix);
        calculatePosition();
        return oldOffset;
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
        rotationTheta = Matrix4f.CreateRotationZ(theta);
        rotationPhi = Matrix4f.CreateFromAxisAngle(
                new Vector3f((float)Math.sin(theta), (float)Math.cos(theta), 0f), phi);
        rotationMatrix = Matrix4f.mul(rotationTheta, rotationPhi);
    }

    private void setThetaOffset(float thetaoffset)
    {
        float thetaOffset = theta +  (float)Math.PI / 180.0f * thetaoffset;
        rotationTheta = Matrix4f.CreateRotationZ(thetaOffset);
        rotationPhi = Matrix4f.CreateFromAxisAngle(
                new Vector3f((float)Math.sin(thetaOffset), (float)Math.cos(thetaOffset), 0f), phi);
        rotationMatrix = Matrix4f.mul(rotationTheta, rotationPhi);
    }

    private void setThetaPhiOffset(float thetaOffset, float phiOffset)
    {
        thetaOffset = theta + (float)Math.PI / 180.0f * thetaOffset;
        phiOffset = phi + (float)Math.PI / 180.0f * phiOffset;
        rotationTheta = Matrix4f.CreateRotationZ(thetaOffset);
        rotationPhi = Matrix4f.CreateFromAxisAngle(
                new Vector3f((float)Math.sin(thetaOffset), (float)Math.cos(thetaOffset), 0f), phiOffset);
        rotationMatrix = Matrix4f.mul(rotationTheta, rotationPhi);
    }

    private void changePhi(float phiChange)
    {
        phi = phi +  (float)Math.PI / 180.0f * phiChange;
        rotationPhi = Matrix4f.CreateFromAxisAngle(
                new Vector3f((float)Math.sin(theta), (float)Math.cos(theta), 0f), phi);
        rotationMatrix = Matrix4f.mul(rotationTheta, rotationPhi);
    }

    private void setScale(float scale)
    {
        sphericalScale = Matrix4f.createScale(scale);
    }

    private void calculatePosition()
    {
        currentPosition.x = (float)(r * Math.cos(theta)*Math.cos(phi));
        currentPosition.y = (float)(r * Math.sin(theta)*Math.cos(phi));
        currentPosition.z = (float)(r * Math.sin(phi));
    }

    private void calculateBasisVectors()
    {
        radiusBasis.x = (float)(Math.cos(theta)*Math.cos(phi));
        radiusBasis.y = (float)(Math.sin(theta)*Math.cos(phi));
        radiusBasis.z = (float)(Math.sin(phi));
        thetaBasis.x = (float)(-Math.sin(theta));
        thetaBasis.y = (float)(Math.cos(theta));
        thetaBasis.z = 0;
        phiBasis.x = (float)(-Math.cos(theta)*Math.sin(phi));
        phiBasis.y = (float)(-Math.sin(theta)*Math.sin(phi));
        phiBasis.z = (float)(Math.cos(phi));
    }

    public String movementInfo()
    {
        StringBuilder result = new StringBuilder();
        result.append("Position = " + currentPosition.toString());
        result.append("r = " + String.valueOf(r));
        result.append("theta = " + String.valueOf(theta) + " " + String.valueOf((theta * 180f / Math.PI) % 360));
        result.append("phi = " + String.valueOf(phi) + " " + String.valueOf((phi * 180f / Math.PI) % 360));
        return result.toString();
    }

    public void translate(Vector3f offset)
    {
        sphericalTranslation = Matrix4f.mul(Matrix4f.createTranslation(offset), sphericalTranslation);
    }
}
