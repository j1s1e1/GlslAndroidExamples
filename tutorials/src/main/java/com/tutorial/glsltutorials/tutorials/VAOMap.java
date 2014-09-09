package com.tutorial.glsltutorials.tutorials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;

/**
 * Created by Jamie on 6/7/14.
 */
public class VAOMap {
    HashMap<String, Integer> map;

    public VAOMap()
    {
        map = new HashMap<String, Integer>();
    }

    public void add(String key, Integer i)
    {
        map.put(key, i);
    }

    public String get(int index)
    {
        return map.get(index).toString();
    }

    public int Value(String key)
    {
        return map.get(key);
    }

    public void Remove(String key)
    {
        map.remove(key);
    }
    public ArrayList<String> getKeys()
    {
        String[] keys = map.keySet().toArray(new String[0]);
        ArrayList<String> myList = new ArrayList();
        Collections.addAll(myList, keys);
        return myList;
    }
}
