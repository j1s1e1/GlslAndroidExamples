package com.tutorial.glsltutorials.tutorials.Interpolator;

import java.util.ArrayList;

/**
 * Created by jamie on 1/2/15.
 */
public class TimedLinearInterpolator<T  extends IDistance<T> & ILinearInterpolate<T> > extends WeightedLinearInterpolator<T>{
    public void SetValues(ArrayList<? extends IGetValueTime<T>> dataSet)
    {
        m_values = new ArrayList<Data>();

        for(int i = 0; i < dataSet.size(); i++)
        {
            Data currData = new Data();
            currData.data = dataSet.get(i).GetValue();
            currData.weight = dataSet.get(i).GetTime();
            m_values.add(currData);
        }

        //Compute the distances of each segment.
        float m_totalDist = 0.0f;
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

}
