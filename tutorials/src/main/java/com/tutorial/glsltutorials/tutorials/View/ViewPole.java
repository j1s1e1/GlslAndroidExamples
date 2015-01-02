package com.tutorial.glsltutorials.tutorials.View;

import android.graphics.Point;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector2f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by Jamie on 6/7/14.
 */
public class ViewPole extends ViewProvider {

    int MM_KEY_SHIFT = 0x01;	///<One of the shift keys.
    int MM_KEY_CTRL = 0x02;	///<One of the control keys.
    int MM_KEY_ALT = 0x04;	///<One of the alt keys.

    public ViewPole(ViewData initialView, ViewScale viewScale)
    {
        this(initialView, viewScale, MouseButtons.MB_LEFT_BTN, false);
    }

    public ViewPole(ViewData initialView, ViewScale viewScale, MouseButtons actionButton)
    {
        this(initialView, viewScale, actionButton, false);
    }

    public ViewPole(ViewData initialView, ViewScale viewScale, MouseButtons actionButton,
                    boolean bRightKeyboardCtrls)
    {
        m_currView = initialView;
        m_viewScale = viewScale;
        m_initialView = initialView;
        m_actionButton = actionButton;
        m_bRightKeyboardCtrls = bRightKeyboardCtrls;
        m_bIsDragging = false;
    }

    ///Generates the world-to-camera matrix for the view.
    public Matrix4f CalcMatrix()
    {
        Matrix4f theMat = Matrix4f.Identity();

        //Remember: these transforms are in reverse order.

        //In this space, we are facing in the correct direction. Which means that the camera point
        //is directly behind us by the radius number of units.
        Matrix4f translation = Matrix4f.CreateTranslation(new Vector3f(0.0f, 0.0f, -m_currView.radius));

        theMat = Matrix4f.Mult(theMat, translation);

        //CreateFromAxisAngle the world to look in the right direction..
        Quaternion fullRotation =
                Quaternion.mult(new Quaternion(new Vector3f(0.0f, 0.0f, 1.0f), m_currView.degSpinRotation),
                        m_currView.orient);
        theMat = Matrix4f.Mult(theMat, fullRotation.toMatrix());

        //Translate the world by the negation of the lookat point, placing the origin at the
        //lookat point.
        translation  = Matrix4f.CreateTranslation(m_currView.targetPos.mul(-1f));
        theMat = Matrix4f.Mult(theMat, translation);

        return theMat;
    }

    /**
     \brief Sets the scaling factor for orientation changes.

     The scaling factor is the number of degrees to rotate the view per window space pixel.
     The scale is the same for all mouse movements.
     **/
    void SetRotationScale(float rotateScale)
    {
    }

    ///Gets the current scaling factor for orientation changes.
    float GetRotationScale()
    {
        return m_viewScale.rotationScale;
    }

    ///Retrieves the current viewing information.
    public ViewData GetView()
    {
        return m_currView;
    }

    ///Resets the view to the initial view. Will fail if currently dragging.
    void Reset()
    {
    }

    /**
     \name Input Providers

     These functions provide input, since Poles cannot get input for themselves. See
     \ref module_glutil_poles "the Pole manual" for details.
     **/
    ///@{
    public void mouseClick(MouseButtons button, boolean isPressed, int modifiers, Point p)
    {
        Vector2f position = new Vector2f(p.x, p.y);
        if(isPressed)
        {
            //Ignore all other button presses when dragging.
            if(!m_bIsDragging)
            {
                if(button == m_actionButton)
                {
                    if((modifiers & MM_KEY_CTRL) != 0)
                        this.BeginDragRotate(position, RotateMode.BIAXIAL);
                    else if((modifiers & MM_KEY_ALT) != 0)
                        this.BeginDragRotate(position, RotateMode.SPIN_VIEW_AXIS);
                    else
                        this.BeginDragRotate(position, RotateMode.DUAL_AXIS);
                }
            }
        }
        else
        {
            //Ignore all other button releases when not dragging
            if(m_bIsDragging)
            {
                if(button == m_actionButton)
                {
                    if(m_RotateMode == RotateMode.DUAL_AXIS ||
                            m_RotateMode == RotateMode.SPIN_VIEW_AXIS ||
                            m_RotateMode == RotateMode.BIAXIAL)
                        this.EndDragRotate(position);
                }
            }
        }
    }
    public void mouseMove(Point position)
    {
        if(m_bIsDragging)
            OnDragRotate(new Vector2f(position.x, position.y));
    }
    public void mouseWheel(int direction, int modifiers, Point position)
    {
        if(direction > 0)
            this.MoveCloser((modifiers & MM_KEY_SHIFT) == 0);
        else
            this.MoveAway((modifiers & MM_KEY_SHIFT) == 0);
    }

    public void mouseButton(int button, int state, int x, int y)
    {
    }
    public void mouseButton(int button, int state, Point p)
    {
    }

    public void CharPress(char key)
    {
    }

    ///@}

    ///Returns true if the mouse is being dragged.
    boolean IsDragging()
    {
        return m_bIsDragging;
    }

    private enum TargetOffsetDir
    {
        DIR_UP,
        DIR_DOWN,
        DIR_FORWARD,
        DIR_BACKWARD,
        DIR_RIGHT,
        DIR_LEFT,
    };

    void OffsetTargetPos(TargetOffsetDir eDir, float worldDistance)
    {
    }
    void OffsetTargetPos(Vector3f cameraOffset)
    {
    }

    ViewData m_currView;
    ViewScale m_viewScale;

    ViewData m_initialView;
    MouseButtons m_actionButton;
    boolean m_bRightKeyboardCtrls;

    //Used when rotating.
    boolean m_bIsDragging;
    RotateMode m_RotateMode;

    float m_degStarDragSpin;
    Vector2f m_startDragMouseLoc;
    Quaternion m_startDragOrient;

    void ProcessXChange(int iXDiff)
    {
        ProcessXChange(iXDiff, false);
    }

    void ProcessXChange(int iXDiff, boolean bClearY)
    {
    }

    void ProcessYChange(int iYDiff)
    {
        ProcessYChange(iYDiff, false);
    }

    void ProcessYChange(int iYDiff, boolean bClearXZ)
    {
    }
    void ProcessXYChange(int iXDiff, int iYDiff)
    {
    }

    void ProcessSpinAxis(int iXDiff, int iYDiff)
    {
    }

    void BeginDragRotate(Vector2f ptStart)
    {
        BeginDragRotate(ptStart, RotateMode.DUAL_AXIS);
    }

    void BeginDragRotate(Vector2f ptStart, RotateMode rotMode)
    {
    }

    void OnDragRotate(Vector2f ptCurr)
    {
    }

    void EndDragRotate(Vector2f ptEnd)
    {
        EndDragRotate(ptEnd, true);
    }

    void EndDragRotate(Vector2f ptEnd, boolean bKeepResults)
    {
    }

    void MoveCloser()
    {
        MoveCloser(true);
    }

    void MoveCloser(boolean bLargeStep)
    {
        if(bLargeStep)
            m_currView.radius -= m_viewScale.largeRadiusDelta;
        else
            m_currView.radius -= m_viewScale.smallRadiusDelta;

        if(m_currView.radius < m_viewScale.minRadius)
            m_currView.radius = m_viewScale.minRadius;
    }

    void MoveAway()
    {
        MoveAway(true);
    }

    void MoveAway(boolean bLargeStep)
    {
        if(bLargeStep)
            m_currView.radius += m_viewScale.largeRadiusDelta;
        else
            m_currView.radius += m_viewScale.smallRadiusDelta;

        if(m_currView.radius > m_viewScale.maxRadius)
            m_currView.radius = m_viewScale.maxRadius;
    }
}
