package com.tutorial.glsltutorials.tutorials.Interpolator;

import java.util.ArrayList;

/**
 * Created by jamie on 1/2/15.
 */
public class WeightedLinearInterpolator <T extends IDistance<T> & ILinearInterpolate<T> >{
    protected class Data
    {
        public T data;
        public float weight;
    };

    protected ArrayList<Data> m_values = new ArrayList<Data>();

    int NumSegments()
    {
        return m_values.size() - 1;
    }

    public T Interpolate(float fAlpha)
    {
        if(m_values.size() == 0)
            return null;
        if(m_values.size() == 1)
            return m_values.get(0).data;

        //Find which segment we are within.
        int segment = 1;
        for(; segment < m_values.size(); ++segment)
        {
            if(fAlpha < m_values.get(segment).weight)
                break;
        }

        if(segment == m_values.size())
            return m_values.get(m_values.size()-1).data;

        float sectionAlpha = fAlpha - m_values.get(segment - 1).weight;
        sectionAlpha /= m_values.get(segment).weight - m_values.get(segment - 1).weight;

        float invSecAlpha = 1.0f - sectionAlpha;

        return LinearInterpolate(segment - 1, segment, sectionAlpha);
    }

    protected float Distance(int a, int b)
    {
        return m_values.get(a).data.distance(m_values.get(a).data, m_values.get(b).data);
    }

    protected T LinearInterpolate(int a, int b, float sectionAlpha)
    {
        return m_values.get(a).data.linearInterpolate(m_values.get(a).data, m_values.get(b).data, sectionAlpha);
    }
}
