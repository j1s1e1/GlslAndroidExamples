package com.tutorial.glsltutorials.tutorials.Movement;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jamie on 11/29/14.
 */
public class SocketMovement extends Movement {
    public SocketMovement ()
    {
        forwardedData = new OutputStream() {
            @Override
            public void write(int oneByte) throws IOException {
            }
            @Override
            public void write(byte[] data)
            {
                processData(data);
            }
        };

        new SocketListener(Shader.context, forwardedData).execute();;
    }

    float xSpeed = 0f;
    float ySpeed = 0f;
    float zSpeed = 0f;

    float maxStep = 0.1f;

    float xGoal = 0f;
    float yGoal = 0f;
    float zGoal = 0f;

    boolean newXgoal = false;
    boolean newYgoal = false;
    boolean newZgoal = false;

    OutputStream forwardedData;

    public void processData(byte[] data)
    {
        String message;
        try {
            message = new String(data, "UTF-8");
        }
        catch (Exception ex)
        {
            return;
        }
        String[] words = message.split(" ");
        if (words.length >= 2)
        {
            switch (words[0])
            {
                case "X+": xSpeed = xSpeed + Float.parseFloat(words[1]); newXgoal = false;  break;
                case "X-": xSpeed = xSpeed - Float.parseFloat(words[1]); newXgoal = false; break;
                case "Y+": ySpeed = ySpeed + Float.parseFloat(words[1]); newYgoal= false; break;
                case "Y-": ySpeed = ySpeed - Float.parseFloat(words[1]); newYgoal= false; break;
                case "Z+": zSpeed = zSpeed + Float.parseFloat(words[1]); newZgoal= false; break;
                case "Z-": zSpeed = zSpeed - Float.parseFloat(words[1]); newZgoal= false; break;
                case "SetX": xGoal = Float.parseFloat(words[1]); newXgoal = true; break;
                case "SetY": yGoal = Float.parseFloat(words[1]); newYgoal = true;  break;
                case "SetZ": zGoal = Float.parseFloat(words[1]); newZgoal= true; break;
            }
            if (xGoal < xLimitLow) xGoal = xLimitLow;
            if (yGoal < yLimitLow) yGoal = yLimitLow;
            if (zGoal < zLimitLow) zGoal = zLimitLow;
            if (xGoal > xLimitHigh) xGoal = xLimitHigh;
            if (yGoal > yLimitHigh) yGoal = yLimitHigh;
            if (zGoal > zLimitHigh) zGoal = zLimitHigh;
        }
    }

    private float newValue(float oldValue, float maxMovement, float lowLimit,
                           float highLimit, float speed, boolean newGoal, float goal)
    {
        if (newGoal)
        {
            float increment = goal - oldValue;
            if (increment > maxStep) increment = maxStep;
            if (increment < -maxStep) increment = -maxStep;
            oldValue = oldValue +  increment;
        }
        else
        {
            oldValue = oldValue + speed * maxMovement;
        }
        if (oldValue < lowLimit) oldValue = lowLimit;
        if (oldValue > highLimit) oldValue = highLimit;
        return oldValue;
    }

    public Vector3f newOffset(Vector3f oldOffset)
    {
        oldOffset.x = newValue(oldOffset.x, maxXmovement, xLimitLow, xLimitHigh, xSpeed, newXgoal, xGoal);
        oldOffset.y = newValue(oldOffset.y, maxYmovement, yLimitLow, yLimitHigh, ySpeed, newYgoal, yGoal);
        oldOffset.z = newValue(oldOffset.z, maxZmovement, zLimitLow, zLimitHigh, zSpeed, newZgoal, zGoal);
        return oldOffset;
    }
}
