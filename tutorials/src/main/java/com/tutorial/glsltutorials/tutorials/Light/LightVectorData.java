package com.tutorial.glsltutorials.tutorials.Light;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Interpolator.IDistance;
import com.tutorial.glsltutorials.tutorials.Interpolator.IGetValueTime;
import com.tutorial.glsltutorials.tutorials.Interpolator.ILinearInterpolate;

/**
 * Created by jamie on 1/2/15.
 */ // //,
public class LightVectorData implements IDistance<LightVectorData>, IGetValueTime<LightVectorData>, ILinearInterpolate<LightVectorData>  {
    public Vector4f first;
    public float second;

    public Vector4f getFirst()
    {
        return first;
    }

    public LightVectorData GetValue()
    {
        return this;
    }

    public float GetTime()
    {
        return second;
    }

    public float distance(LightVectorData a, LightVectorData b)
    {
        return Vector4f.distance(a.getFirst(), b.getFirst());
    }

    public LightVectorData(Vector4f firstIn, float secondIn)
    {
        first = firstIn;
        second = secondIn;
    }

    public LightVectorData linearInterpolate(LightVectorData a, LightVectorData b, float sectionAlpha)
    {
        Vector4f interpolatedValue = a.getFirst().Mult((1f - sectionAlpha)).add(b.getFirst().Mult(sectionAlpha));
        float interp2 = 0f;
        LightVectorData result = new LightVectorData(interpolatedValue, interp2);
        return result;
    }
}
