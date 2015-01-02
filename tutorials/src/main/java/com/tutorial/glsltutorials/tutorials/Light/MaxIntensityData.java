package com.tutorial.glsltutorials.tutorials.Light;

import com.tutorial.glsltutorials.tutorials.Interpolator.FloatIDistance;
import com.tutorial.glsltutorials.tutorials.Interpolator.IGetValueTime;

/**
 * Created by jamie on 1/2/15.
 */
public class MaxIntensityData implements IGetValueTime<FloatIDistance> {
    public FloatIDistance first;
    public float second;

    public MaxIntensityData(float firstIn, float secondIn)
    {
        first = new FloatIDistance(firstIn);
        second = secondIn;
    }

    public MaxIntensityData(FloatIDistance firstIn, float secondIn)
    {
        first = firstIn;
        second = secondIn;
    }

    public FloatIDistance GetValue()
    {
        return first;
    }

    public float GetFloat()
    {
        return first.GetValue();
    }

    public float GetTime()
    {
        return second;
    }
}
