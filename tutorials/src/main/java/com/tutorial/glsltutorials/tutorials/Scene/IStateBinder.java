package com.tutorial.glsltutorials.tutorials.Scene;

/**
 * Created by jamie on 1/3/15.
 */
public interface IStateBinder {
    //The current program will be in use when this is called.
    public void bindState(int prog);

    //The current program will be in use when this is called.
    public void unbindState(int prog);
}
