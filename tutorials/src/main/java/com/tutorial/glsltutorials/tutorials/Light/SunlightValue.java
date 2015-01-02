package com.tutorial.glsltutorials.tutorials.Light;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

/**
 * Created by jamie on 1/2/15.
 */
public class SunlightValue {
    public float normTime;
    public Vector4f ambient;
    public Vector4f sunlightIntensity;
    public Vector4f backgroundColor;
    public float maxIntensity;
    public boolean HDR = true;

    public SunlightValue (float normTimeIn, Vector4f ambientIn, Vector4f sunlightIntensityIn,
                          Vector4f backgroundColorIn)
    {
        normTime = normTimeIn;
        ambient = ambientIn;
        sunlightIntensity = sunlightIntensityIn;
        backgroundColor = backgroundColorIn;
        HDR = false;
    }
    public SunlightValue (float normTimeIn, Vector4f ambientIn, Vector4f sunlightIntensityIn,
                          Vector4f backgroundColorIn, float maxIntensityIn)
    {
        normTime = normTimeIn;
        ambient = ambientIn;
        sunlightIntensity = sunlightIntensityIn;
        backgroundColor = backgroundColorIn;
        maxIntensity = maxIntensityIn;
    }
}
