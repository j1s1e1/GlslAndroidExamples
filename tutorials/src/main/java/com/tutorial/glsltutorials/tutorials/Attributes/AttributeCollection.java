package com.tutorial.glsltutorials.tutorials.Attributes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jamie on 6/7/14.
 */
public class AttributeCollection {
    public HashMap<String, ArrayList<Integer>> collection;

    public AttributeCollection()
    {
        collection = new HashMap<String, ArrayList<Integer>>();
    }

    public void add(String s, ArrayList<Integer> ints) {
        collection.put(s, ints);
    }

    public int Count()
    {
        return collection.size();
    }

    public ArrayList<Integer> getValue(String key)
    {
        return collection.get(key);
    }
}
