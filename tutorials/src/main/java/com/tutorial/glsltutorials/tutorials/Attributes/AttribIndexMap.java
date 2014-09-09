package com.tutorial.glsltutorials.tutorials.Attributes;

import java.util.Dictionary;
import java.util.HashMap;

/**
 * Created by Jamie on 6/7/14.
 */
public class AttribIndexMap {
    HashMap<Integer, Integer> map;

    public AttribIndexMap()
    {
        map = new HashMap<Integer, Integer>();
    }

    public void add(Integer i1, Integer i2)
    {
        map.put(i1, i2);
    }
}
