package com.tutorial.glsltutorials.tutorials.Interpolator;

/**
 * Created by jamie on 1/2/15.
 */
public interface ILinearInterpolate<T> {
    T linearInterpolate(T a, T b, float alpha);
}
