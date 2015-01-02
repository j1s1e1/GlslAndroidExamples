package com.tutorial.glsltutorials.tutorials.Interpolator;

/**
 * Created by jamie on 1/2/15.
 */
public class FloatIDistance implements IDistance<FloatIDistance>, ILinearInterpolate<FloatIDistance> {
    float floatValue;
    public FloatIDistance(float floatValueIn)
    {
        floatValue = floatValueIn;
    }

    public float GetValue()
    {
        return floatValue;
    }

    public float distance(FloatIDistance a, FloatIDistance b)
    {
        return (float)Math.abs(a.GetValue() - b.GetValue());
    }

    public FloatIDistance linearInterpolate(FloatIDistance a, FloatIDistance b, float sectionAlpha)
    {
        float interpolatedValue = (1f - sectionAlpha) * a.GetValue() + sectionAlpha * b.GetValue();
        FloatIDistance result = new FloatIDistance(interpolatedValue);
        return result;
    }
}
