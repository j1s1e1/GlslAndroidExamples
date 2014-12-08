package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector2f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Objects.Ball;
import com.tutorial.glsltutorials.tutorials.Objects.Paddle;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Text.TextClass;

/**
 * Created by jamie on 11/29/14.
 */
public class Tut_Tennis extends TutorialBase {
    public Tut_Tennis() {
    }

    private enum paddleControlEnum {
        TOUCH,
        SOCKET,
        RANDOM,
        KEYBOARD,
    }

    paddleControlEnum topPaddleControl = paddleControlEnum.TOUCH;
    paddleControlEnum bottomPaddleControl = paddleControlEnum.TOUCH;

    Paddle topPaddle;
    Paddle bottomPaddle;
    boolean incrementTopPaddle;
    boolean incrementBottomPaddle;
    Ball ball;
    TextClass ScoreTop;
    TextClass ScoreBottom;
    TextClass PaddleTop;
    TextClass PaddleBottom;
    int scoreTopInt = 0;
    int scoreBottomInt = 0;
    static String paddleSelection;

    protected void init() {
        Programs.reset();
        Shape.resetWorldToCameraMatrix();
        ball = new Ball();
        ball.setLimits(new Vector3f(-1f, -1f, 0.5f), new Vector3f(1f, 1f, 0.5f));
        topPaddle = new Paddle();
        topPaddle.setLimits(new Vector3f(-1f, 0.9f, 0.5f), new Vector3f(1f, 0.9f, 0.5f));
        topPaddle.setTouchControl();
        bottomPaddle = new Paddle();
        bottomPaddle.setLimits(new Vector3f(-1f, -0.9f, 0.5f), new Vector3f(1f, -0.9f, 0.5f));
        ball.addPaddle(topPaddle);
        ball.addPaddle(bottomPaddle);
        ScoreTop = new TextClass("0", 1f, 0.1f, true, true);
        ScoreTop.setOffset(new Vector3f(-0.8f, 0.7f, 0.5f));
        ScoreBottom = new TextClass("0", 1f, 0.1f, true, true);
        ScoreBottom.setOffset(new Vector3f(0.7f, -0.7f, 0.5f));
        PaddleTop = new TextClass("NONE", 0.5f, 0.05f, true, true);
        PaddleTop.setOffset(new Vector3f(0.8f, 0.7f, 0.5f));
        PaddleBottom = new TextClass("NONE", 0.5f, 0.05f, true, true);
        PaddleBottom.setOffset(new Vector3f(-0.7f, -0.7f, 0.5f));
        setupDepthAndCull();
    }

    public void display() {
        clearDisplay();
        ball.draw();
        topPaddle.draw();
        bottomPaddle.draw();
        ScoreTop.draw();
        ScoreBottom.draw();
        PaddleTop.draw();
        PaddleBottom.draw();
        updateScores();
        updatePaddles();
    }

    boolean goal = false;

    public void updateScores() {
        float ballHeight = ball.getOffset().y;
        if (goal == false) {
            if (ballHeight > 0.95f) {
                goal = true;
                scoreBottomInt++;
                ScoreBottom.UpdateText(String.valueOf(scoreBottomInt));

            }
            if (ballHeight < -0.95f) {
                goal = true;
                scoreTopInt++;
                ScoreTop.UpdateText(String.valueOf(scoreTopInt));
            }
        } else {
            if ((ballHeight > -0.85f) && (ballHeight < 0.85f)) {
                goal = false;
            }
        }
    }

    private void updatePaddles() {
        if (incrementTopPaddle) {
            topPaddleControl = NextControl(topPaddle, topPaddleControl);
            PaddleTop.UpdateText(topPaddleControl.toString());
            incrementTopPaddle = false;
        }
        if (incrementBottomPaddle)
        {
            bottomPaddleControl = NextControl(bottomPaddle, bottomPaddleControl);
            PaddleBottom.UpdateText(bottomPaddleControl.toString());
            incrementBottomPaddle = false;
        }
    }


    public String keyboard(int keyCode, int x, int y)
    {
        StringBuilder result = new StringBuilder();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_P:
                topPaddle.setKeyboardControl();
                break;
            case KeyEvent.KEYCODE_R:
                topPaddle.setRandomControl();
                break;
            case KeyEvent.KEYCODE_S:
                topPaddle.setSocketControl();
                break;
        }
        topPaddle.keyboard(keyCode);
        result.append(String.valueOf(keyCode));
        reshape();
        display();
        return result.toString();
    }

    public void touchEvent(int x_position, int y_position) throws Exception
    {
       topPaddle.touchEvent(new Vector2f(-1f + 2f * x_position / width, -1f + 2f * y_position / height) );
    }

    private paddleControlEnum NextControl (Paddle paddle, paddleControlEnum paddleControl)
    {
        switch (paddleControl)
        {
            case TOUCH:
                paddleControl = paddleControlEnum.SOCKET;
                paddle.setSocketControl();
                break;
            case SOCKET:
                paddleControl = paddleControlEnum.RANDOM;
                paddle.setRandomControl();
                break;
            case RANDOM:
                paddleControl = paddleControlEnum.KEYBOARD;
                paddle.setKeyboardControl();
                break;
            case KEYBOARD:
                paddleControl = paddleControlEnum.TOUCH;
                paddle.setTouchControl();
                break;
        }
        return paddleControl;
    }

    public void onLongPress(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();
        x = -1f + 2f * x / width;
        y =  -1f + 2f * y / height;
        if (Math.abs(x + 0.8f) < 0.1f)
        {
            if (Math.abs(y + 0.7f) < 0.1f)
            {
                incrementTopPaddle = true;
            }
        }
        if (Math.abs(x - 0.7f) < 0.1f)
        {
            if (Math.abs(y - 0.7f) < 0.1f)
            {
                incrementBottomPaddle = true;
            }
        }
    }
}
