package com.tutorial.glsltutorials.tutorials.Movement;

import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by jamie on 11/29/14.
 */
public class KeyboardMovement extends Movement {
    public KeyboardMovement()
    {
    }

    float xSpeed = 0f;
    float ySpeed = 0f;
    float zSpeed = 0f;

    private float NewValue(float oldValue, float maxMovement, float lowLimit,
                           float highLimit, float speed)
    {
        oldValue = oldValue + speed * maxMovement;
        if (oldValue < lowLimit) oldValue = lowLimit;
        if (oldValue > highLimit) oldValue = highLimit;
        return oldValue;
    }

    public Vector3f NewOffset(Vector3f oldOffset)
    {
        oldOffset.x = NewValue(oldOffset.x, maxXmovement, xLimitLow, xLimitHigh, xSpeed);
        oldOffset.y = NewValue(oldOffset.y, maxYmovement, yLimitLow, yLimitHigh, ySpeed);
        oldOffset.z = NewValue(oldOffset.z, maxZmovement, zLimitLow, zLimitHigh, zSpeed);
        return oldOffset;
    }

    public void keyboard(int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_NUMPAD_4: xSpeed = xSpeed - 0.01f; break;
            case KeyEvent.KEYCODE_NUMPAD_6: xSpeed = xSpeed + 0.01f; break;
            case KeyEvent.KEYCODE_NUMPAD_8: ySpeed = ySpeed + 0.01f; break;
            case KeyEvent.KEYCODE_NUMPAD_2: ySpeed = ySpeed - 0.01f; break;
        }
    }
}
