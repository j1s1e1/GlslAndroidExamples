package com.tutorial.glsltutorials.tutorials.Interpolator;

import java.util.ArrayList;

/**
 * Created by jamie on 1/2/15.
 */
public class ConstVelLinearInterpolator<T extends IDistance<T> & ILinearInterpolate<T>> extends  WeightedLinearInterpolator<T>{
    float m_totalDist;

    public ConstVelLinearInterpolator()
    {
        m_totalDist = 0.0f;
    }

    public void SetValues(ArrayList<T> data)
    {
        m_values.clear();

        for (T newValue : data)
        {
            Data currData = new Data();
            currData.data = newValue;
            currData.weight = 0.0f;
            m_values.add(currData);
        }

        //Compute the distances of each segment.
        m_totalDist = 0.0f;
        for(int iLoop = 1; iLoop < m_values.size(); ++iLoop)
        {
            m_totalDist += Distance(iLoop - 1, iLoop);
            m_values.get(iLoop).weight = m_totalDist;
        }

        //Compute the alpha value that represents when to use this segment.
        for(int iLoop = 1; iLoop < m_values.size(); ++iLoop)
        {
            m_values.get(iLoop).weight /= m_totalDist;
        }
    }

    float Distance()
    {
        return m_totalDist;
    }
}
