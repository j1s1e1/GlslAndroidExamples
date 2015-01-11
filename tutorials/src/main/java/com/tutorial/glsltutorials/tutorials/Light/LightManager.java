package com.tutorial.glsltutorials.tutorials.Light;

import android.util.Log;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Interpolator.ConstVelLinearInterpolator;
import com.tutorial.glsltutorials.tutorials.Interpolator.FloatIDistance;
import com.tutorial.glsltutorials.tutorials.Interpolator.LightInterpolator;
import com.tutorial.glsltutorials.tutorials.Interpolator.TimedLinearInterpolator;
import com.tutorial.glsltutorials.tutorials.Interpolator.Vector3IDistance;

import java.util.ArrayList;

/**
 * Created by jamie on 1/2/15.
 */
public class LightManager {
    static int NUMBER_OF_LIGHTS = 4;
    int NUMBER_OF_POINT_LIGHTS = NUMBER_OF_LIGHTS - 1;

    static float g_fLightHeight = 10.5f;
    static float g_fLightRadius = 70.0f;

    class ExtraTimer
    {
        public String name;
        public FrameworkTimer timer;

        public ExtraTimer(String nameIn, FrameworkTimer timerIn)
        {
            name = nameIn;
            timer = timerIn;
        }
    }

    FrameworkTimer m_sunTimer;
    TimedLinearInterpolator<LightVectorData> m_ambientInterpolator;
    TimedLinearInterpolator<LightVectorData> m_backgroundInterpolator;
    TimedLinearInterpolator<LightVectorData> m_sunlightInterpolator;
    TimedLinearInterpolator<FloatIDistance> m_maxIntensityInterpolator;

    ArrayList<ConstVelLinearInterpolator<Vector3IDistance>> m_lightPos;
    ArrayList<Vector4f> m_lightIntensity = new ArrayList<Vector4f>();
    ArrayList<FrameworkTimer> m_lightTimers = new ArrayList<FrameworkTimer>();
    ArrayList<ExtraTimer> m_extraTimers = new ArrayList<ExtraTimer>();

    Vector4f CalcLightPosition(FrameworkTimer timer, float alphaOffset)
    {
        float fLoopDuration = 5.0f;
        float fScale = 3.14159f * 2.0f;

        float timeThroughLoop = timer.GetAlpha() + alphaOffset;

        Vector4f ret = new Vector4f(0.0f, g_fLightHeight, 0.0f, 1.0f);

        ret.x = (float)Math.cos(timeThroughLoop * fScale) * g_fLightRadius;
        ret.z = (float)Math.sin(timeThroughLoop * fScale) * g_fLightRadius;

        return ret;
    }

    float g_fHalfLightDistance = 70.0f;
    float g_fLightAttenuation = 1.0f / (g_fHalfLightDistance * g_fHalfLightDistance);

    float distance(Vector3f lhs, Vector3f rhs)
    {
        return rhs.sub(lhs).length();
    }

    public LightManager()
    {
        m_ambientInterpolator = new TimedLinearInterpolator<LightVectorData>();
        m_backgroundInterpolator = new TimedLinearInterpolator<LightVectorData>();
        m_sunlightInterpolator = new TimedLinearInterpolator<LightVectorData>();
        m_maxIntensityInterpolator = new TimedLinearInterpolator<FloatIDistance>();
        m_sunTimer = new FrameworkTimer(FrameworkTimer.Type.TT_LOOP,  30.0f);
        m_ambientInterpolator = new TimedLinearInterpolator<LightVectorData>();
        m_lightTimers = new ArrayList<FrameworkTimer>();
        m_lightPos = new ArrayList<ConstVelLinearInterpolator<Vector3IDistance>>();
        m_lightPos.add(new LightInterpolator());
        m_lightPos.add(new LightInterpolator());
        m_lightPos.add(new LightInterpolator());

        m_lightIntensity = new ArrayList<Vector4f>();
        for (int i = 0; i < NUMBER_OF_POINT_LIGHTS; i++)
        {
            m_lightIntensity.add(new Vector4f(0.2f, 0.2f, 0.2f, 1.0f));
        }

        ArrayList<Vector3IDistance> posValues = new ArrayList<Vector3IDistance>();

        posValues.add(new Vector3IDistance(-50.0f, 30.0f, 70.0f));
        posValues.add(new Vector3IDistance(-70.0f, 30.0f, 50.0f));
        posValues.add(new Vector3IDistance(-70.0f, 30.0f, -50.0f));
        posValues.add(new Vector3IDistance(-50.0f, 30.0f, -70.0f));
        posValues.add(new Vector3IDistance(50.0f, 30.0f, -70.0f));
        posValues.add(new Vector3IDistance(70.0f, 30.0f, -50.0f));
        posValues.add(new Vector3IDistance(70.0f, 30.0f, 50.0f));
        posValues.add(new Vector3IDistance(50.0f, 30.0f, 70.0f));
        m_lightPos.get(0).SetValues(posValues);
        m_lightTimers.add(new FrameworkTimer(FrameworkTimer.Type.TT_LOOP, 15.0f));

        //Right-side light.
        posValues.clear();
        posValues.add(new Vector3IDistance(100.0f, 6.0f, 75.0f));
        posValues.add(new Vector3IDistance(90.0f, 8.0f, 90.0f));
        posValues.add(new Vector3IDistance(75.0f, 10.0f, 100.0f));
        posValues.add(new Vector3IDistance(60.0f, 12.0f, 90.0f));
        posValues.add(new Vector3IDistance(50.0f, 14.0f, 75.0f));
        posValues.add(new Vector3IDistance(60.0f, 16.0f, 60.0f));
        posValues.add(new Vector3IDistance(75.0f, 18.0f, 50.0f));
        posValues.add(new Vector3IDistance(90.0f, 20.0f, 60.0f));
        posValues.add(new Vector3IDistance(100.0f, 22.0f, 75.0f));
        posValues.add(new Vector3IDistance(90.0f, 24.0f, 90.0f));
        posValues.add(new Vector3IDistance(75.0f, 26.0f, 100.0f));
        posValues.add(new Vector3IDistance(60.0f, 28.0f, 90.0f));
        posValues.add(new Vector3IDistance(50.0f, 30.0f, 75.0f));

        posValues.add(new Vector3IDistance(105.0f, 9.0f, -70.0f));
        posValues.add(new Vector3IDistance(105.0f, 10.0f, -90.0f));
        posValues.add(new Vector3IDistance(72.0f, 20.0f, -90.0f));
        posValues.add(new Vector3IDistance(72.0f, 22.0f, -70.0f));
        posValues.add(new Vector3IDistance(105.0f, 32.0f, -70.0f));
        posValues.add(new Vector3IDistance(105.0f, 34.0f, -90.0f));
        posValues.add(new Vector3IDistance(72.0f, 44.0f, -90.0f));

        m_lightPos.get(1).SetValues(posValues);
        m_lightTimers.add(new FrameworkTimer(FrameworkTimer.Type.TT_LOOP, 25.0f));

        //Left-side light.
        posValues.clear();
        posValues.add(new Vector3IDistance(-7.0f, 35.0f, 1.0f));
        posValues.add(new Vector3IDistance(8.0f, 40.0f, -14.0f));
        posValues.add(new Vector3IDistance(-7.0f, 45.0f, -29.0f));
        posValues.add(new Vector3IDistance(-22.0f, 50.0f, -14.0f));
        posValues.add(new Vector3IDistance(-7.0f, 55.0f, 1.0f));
        posValues.add(new Vector3IDistance(8.0f, 60.0f, -14.0f));
        posValues.add(new Vector3IDistance(-7.0f, 65.0f, -29.0f));

        posValues.add(new Vector3IDistance(-83.0f, 30.0f, -92.0f));
        posValues.add(new Vector3IDistance(-98.0f, 27.0f, -77.0f));
        posValues.add(new Vector3IDistance(-83.0f, 24.0f, -62.0f));
        posValues.add(new Vector3IDistance(-68.0f, 21.0f, -77.0f));
        posValues.add(new Vector3IDistance(-83.0f, 18.0f, -92.0f));
        posValues.add(new Vector3IDistance(-98.0f, 15.0f, -77.0f));

        posValues.add(new Vector3IDistance(-50.0f, 8.0f, 25.0f));
        posValues.add(new Vector3IDistance(-59.5f, 4.0f, 65.0f));
        posValues.add(new Vector3IDistance(-59.5f, 4.0f, 78.0f));
        posValues.add(new Vector3IDistance(-45.0f, 4.0f, 82.0f));
        posValues.add(new Vector3IDistance(-40.0f, 4.0f, 50.0f));
        posValues.add(new Vector3IDistance(-70.0f, 20.0f, 40.0f));
        posValues.add(new Vector3IDistance(-60.0f, 20.0f, 90.0f));
        posValues.add(new Vector3IDistance(-40.0f, 25.0f, 90.0f));

        m_lightPos.get(2).SetValues(posValues);
        m_lightTimers.add(new FrameworkTimer(FrameworkTimer.Type.TT_LOOP, 15.0f));
    }

    Vector4f GetValue(LightVectorData data) {return data.first;}
    float GetTime(LightVectorData data) {return data.second;}
    float GetValue(MaxIntensityData data) {return data.first.GetValue();}
    float GetTime(MaxIntensityData data) {return data.second;}

    public void UpdateTime()
    {
        m_sunTimer.Update();
        for (FrameworkTimer ft : m_lightTimers)
        {
            ft.Update();
        }
        for (ExtraTimer et : m_extraTimers)
        {
            et.timer.Update();
        }
    }

    public void SetPause(TimerTypes eTimer, boolean pause)
    {
        if(eTimer == TimerTypes.TIMER_ALL || eTimer == TimerTypes.TIMER_LIGHTS)
        {
            for(FrameworkTimer ft : m_lightTimers)
            {
                ft.SetPause(true);
            }
            for(ExtraTimer et : m_extraTimers)
            {
                et.timer.SetPause(true);
            }
        }

        if(eTimer == TimerTypes.TIMER_ALL || eTimer == TimerTypes.TIMER_SUN)
        {
            m_sunTimer.TogglePause();
        }
    }

    public void TogglePause( TimerTypes eTimer )
    {
        SetPause(eTimer, !IsPaused(eTimer));
    }

    public boolean IsPaused( TimerTypes eTimer )
    {
        if(eTimer == TimerTypes.TIMER_ALL || eTimer == TimerTypes.TIMER_SUN)
            return m_sunTimer.IsPaused();

        return m_lightTimers.get(0).IsPaused();
    }

    public void RewindTime(TimerTypes eTimer, float secRewind )
    {
        if(eTimer == TimerTypes.TIMER_ALL || eTimer == TimerTypes.TIMER_SUN)
            m_sunTimer.Rewind(secRewind);

        if(eTimer == TimerTypes.TIMER_ALL || eTimer == TimerTypes.TIMER_LIGHTS)
        {
            for(FrameworkTimer ft : m_lightTimers)
            {
                ft.Rewind(secRewind);
            }
            for(ExtraTimer et : m_extraTimers)
            {
                et.timer.Rewind(secRewind);
            }
        }
    }

    public void FastForwardTime(TimerTypes eTimer,  float secFF )
    {
        if(eTimer == TimerTypes.TIMER_ALL || eTimer == TimerTypes.TIMER_SUN)
            m_sunTimer.Fastforward(secFF);

        if(eTimer == TimerTypes.TIMER_ALL || eTimer == TimerTypes.TIMER_LIGHTS)
        {
            for(FrameworkTimer ft : m_lightTimers)
            {
                ft.Fastforward(secFF);
            }
            for(ExtraTimer et : m_extraTimers)
            {
                et.timer.Fastforward(secFF);
            }
        }
    }

    public LightBlock GetLightInformation( Matrix4f worldToCameraMat )
    {
        LightBlock lightData = new LightBlock(NUMBER_OF_LIGHTS);

        lightData.ambientIntensity = m_ambientInterpolator.Interpolate(m_sunTimer.GetAlpha()).getFirst();
        lightData.lightAttenuation = g_fLightAttenuation;

        lightData.lights[0].cameraSpaceLightPos =
                Vector4f.Transform(GetSunlightDirection(), worldToCameraMat);
        lightData.lights[0].lightIntensity = m_sunlightInterpolator.Interpolate(m_sunTimer.GetAlpha()).getFirst();

        for(int light = 0; light < NUMBER_OF_POINT_LIGHTS; light++)
        {
            Vector4f worldLightPos =
                    new Vector4f(m_lightPos.get(light).Interpolate(m_lightTimers.get(light).GetAlpha()).GetValue(), 1.0f);
            Vector4f lightPosCameraSpace = Vector4f.Transform(worldLightPos, worldToCameraMat);

            lightData.lights[light + 1].cameraSpaceLightPos = lightPosCameraSpace;
            lightData.lights[light + 1].lightIntensity = m_lightIntensity.get(light);
        }

        return lightData;
    }

    public LightBlock GetLightInformationHDR(Matrix4f worldToCameraMat )
    {
        LightBlock lightData = new LightBlock(NUMBER_OF_LIGHTS);

        lightData.ambientIntensity = m_ambientInterpolator.Interpolate(m_sunTimer.GetAlpha()).getFirst();
        lightData.lightAttenuation = g_fLightAttenuation;
        lightData.maxIntensity = m_maxIntensityInterpolator.Interpolate(m_sunTimer.GetAlpha()).GetValue();

        lightData.lights[0].cameraSpaceLightPos =
                Vector4f.Transform(GetSunlightDirection(), worldToCameraMat);
        lightData.lights[0].lightIntensity = m_sunlightInterpolator.Interpolate(m_sunTimer.GetAlpha()).getFirst();

        for(int light = 0; light < NUMBER_OF_POINT_LIGHTS; light++)
        {
            Vector4f worldLightPos =
                    new Vector4f(m_lightPos.get(light).Interpolate(m_lightTimers.get(light).GetAlpha()).GetValue(), 1.0f);
            Vector4f lightPosCameraSpace = Vector4f.Transform(worldLightPos, worldToCameraMat);

            lightData.lights[light + 1].cameraSpaceLightPos = lightPosCameraSpace;
            lightData.lights[light + 1].lightIntensity = m_lightIntensity.get(light);
        }

        return lightData;
    }

    public LightBlock GetLightInformationGamma(Matrix4f worldToCameraMat )
    {
        LightBlock lightData  = GetLightInformationHDR(worldToCameraMat);
        return lightData;
    }

    public Matrix4f Rotate(Matrix4f input, Vector3f rotationAxis, float angleDeg)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(rotationAxis, (float)Math.PI / 180.0f * angleDeg);
        return Matrix4f.mul(rotation, input);
    }

    public Vector4f GetSunlightDirection()
    {
        float angle = 2.0f * 3.14159f * m_sunTimer.GetAlpha();
        Vector4f sunDirection = new Vector4f();
        sunDirection.x = (float)Math.sin(angle);
        sunDirection.y = (float)Math.cos(angle);

        //Keep the sun from being perfectly centered overhead.
        sunDirection = Vector4f.Transform(sunDirection,
                Rotate(Matrix4f.Identity(), new Vector3f(0.0f, 1.0f, 0.0f), 5.0f));

        return sunDirection;
    }

    public Vector4f GetSunlightIntensity()
    {
        return m_sunlightInterpolator.Interpolate(m_sunTimer.GetAlpha()).getFirst();
    }

    public int GetNumberOfPointLights()
    {
        return m_lightPos.size();
    }

    public Vector3f GetWorldLightPosition( int lightIx )
    {
        return m_lightPos.get(lightIx).Interpolate(m_lightTimers.get(lightIx).GetAlpha()).GetValue();
    }

    public void SetPointLightIntensity( int iLightIx, Vector4f intensity )
    {
        m_lightIntensity.set(iLightIx, intensity);
    }

    public Vector4f GetPointLightIntensity( int iLightIx )
    {
        return m_lightIntensity.get(iLightIx);
    }

    public void CreateTimer(String timerName,
                            FrameworkTimer.Type eType, float fDuration )
    {
        m_extraTimers.add(new ExtraTimer(timerName, new FrameworkTimer(eType, fDuration)));
    }

    public float GetTimerValue(String timerName)
    {
        for (ExtraTimer et : m_extraTimers)
        {
            if (et.name == timerName)
            {
                return et.timer.GetAlpha();
            }
        }
        return -1f;
    }

    public Vector4f GetBackgroundColor()
    {
        try
        {
            return m_backgroundInterpolator.Interpolate(m_sunTimer.GetAlpha()).getFirst();
        }
        catch (Exception ex)
        {
            Log.e("Get BackgroundColor", "Error " + ex.toString());
            return new Vector4f();
        }
    }

    public float GetMaxIntensity()
    {
        return m_maxIntensityInterpolator.Interpolate(m_sunTimer.GetAlpha()).GetValue();
    }

    public float GetSunTime()
    {
        return m_sunTimer.GetAlpha();
    }

    public void SetSunlightValues(SunlightValue[] values)
    {
        LightVector ambient = new LightVector();
        LightVector light = new LightVector();
        LightVector background = new LightVector();

        for(SunlightValue sv : values)
        {
            ambient.add(new LightVectorData(sv.ambient, sv.normTime));
            light.add(new LightVectorData(sv.sunlightIntensity, sv.normTime));
            background.add(new LightVectorData(sv.backgroundColor, sv.normTime));
        }

        m_ambientInterpolator.SetValues(ambient);
        m_sunlightInterpolator.SetValues(light);
        m_backgroundInterpolator.SetValues(background);

        MaxIntensityVector maxIntensity = new MaxIntensityVector();
        maxIntensity.add(new MaxIntensityData(1.0f, 0.0f));
        m_maxIntensityInterpolator.SetValues(maxIntensity);
    }

}
