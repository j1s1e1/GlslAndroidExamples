package com.tutorial.glsltutorials.tutorials.Objects;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixBlock2;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Tutorials.TutorialBase;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jamie on 10/18/14.
 */
public class Missle {
    LitMatrixSphere2 topLeft;
    LitMatrixSphere2 topRight;
    LitMatrixSphere2 bottomLeft;
    LitMatrixSphere2 bottomRight;

    float radius = 0.01f;
    boolean started = false;
    boolean prepare_to_stop = false;
    boolean finished = false;
    boolean fire = false;

    float[] color = Colors.BLUE_COLOR;

    Vector3f topLeftDirection;
    Vector3f topRightDirection;
    Vector3f bottomLeftDirection;
    Vector3f bottomRightDirection;

    float stepMultiple = 0.04f;

    Vector3f axis;
/*
    final Handler timerHandler = new Handler();
    TimerTask timerElapsed = new TimerTask()
    {
        @Override
        public void run()
        {
            timerHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        TimerTick();
                    }
                    catch (Exception ex)
                    {
                        int debug = 0;
                        debug++;
                    }
                }
            });
        }
    };
    */
    Timer timer;
    int timerCount = 0;
    int startFrameCount = 10;
    int finishedFrameCount = 10;

    public Missle (Vector3f axisIn, Vector3f up, Vector3f right)
    {
        axis = new Vector3f(axisIn);
        topLeft = new LitMatrixSphere2(radius);
        topRight = new LitMatrixSphere2(radius);
        bottomLeft = new LitMatrixSphere2(radius);
        bottomRight = new LitMatrixSphere2(radius);

        topLeft.SetColor(color);
        topRight.SetColor(color);
        bottomLeft.SetColor(color);
        bottomRight.SetColor(color);

        topLeft.SetOffset(right.mul(-1f).add(up));
        topRight.SetOffset(right.add(up));
        bottomLeft.SetOffset(right.mul(-1).sub(up));
        bottomRight.SetOffset(right.sub(up));

        topLeftDirection = Vector3f.multiply(axis.sub(topLeft.GetOffset()), stepMultiple);
        topRightDirection = Vector3f.multiply(axis.sub(topRight.GetOffset()), stepMultiple);
        bottomLeftDirection = Vector3f.multiply(axis.sub(bottomLeft.GetOffset()), stepMultiple);
        bottomRightDirection = Vector3f.multiply(axis.sub(bottomRight.GetOffset()), stepMultiple);

        //timer = new Timer(false);
        //timer.schedule(timerElapsed, 50); // 10 = 0.01 second. second is repeat count
        fire = true;
        started = true;
    }

    void TimerTick()
    {
        if (started)
        {
            if (prepare_to_stop)
            {
                if (timerCount > 10) {
                    fire = false;
                    finished = true;
                }
                else
                {
                    timerCount++;
                }
            }
        }
        else
        {
            started = true;
            fire = true;
        }
    }

    public boolean Started()
    {
        return started;
    }

    public boolean Firing()
    {
        return fire;
    }

    public boolean Finished()
    {
        return finished;
    }

    public void Clear()
    {
        fire = false;
    }

    public void DrawMissle(LitMatrixSphere2 missle, Vector3f step)
    {
        missle.Draw();
        Vector3f offset = missle.GetOffset();
        missle.SetOffset(offset.add(step));
    }

    public void Draw()
    {
        Vector3f offset = topLeft.GetOffset();
        if ((offset.sub(axis).length()) < 0.01)
        {
            if (finishedFrameCount == 0)
            {
               fire = false;
               finished = true;
            }
            else
            {
                finishedFrameCount--;  // added some delays to allow GPU to catch up
            }
        }
        else
        {
            if (startFrameCount == 0) {
                DrawMissle(topLeft, topLeftDirection);
                DrawMissle(topRight, topRightDirection);
                DrawMissle(bottomLeft, bottomLeftDirection);
                DrawMissle(bottomRight, bottomRightDirection);
            }
            else
            {
                startFrameCount--;
            }
        }
    }

}
