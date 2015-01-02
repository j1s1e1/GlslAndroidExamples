package com.tutorial.glsltutorials.tutorials;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector2f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by Jamie on 6/7/14.
 */
public class ViewPole extends Pole {
    protected ViewData position;
    protected ViewData initialPosition;
    protected float rotateScale;
    protected boolean isDragging;

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
        position = initialView;
        initialPosition = initialView;
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
    public void MouseClick(MouseButtons button, boolean isPressed, int modifiers, Vector2f position)
    {
    }
    public void MouseMove(Vector2f position)
    {
    }
    public void MouseWheel(int direction, int modifiers, Vector2f position)
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

    enum RotateMode
    {
        RM_DUAL_AXIS_ROTATE,
        RM_BIAXIAL_ROTATE,
        RM_XZ_AXIS_ROTATE,
        RM_Y_AXIS_ROTATE,
        RM_SPIN_VIEW_AXIS,
    };

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
        BeginDragRotate(ptStart, RotateMode.RM_DUAL_AXIS_ROTATE);
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
    }

    void MoveAway()
    {
        MoveAway(true);
    }

    void MoveAway(boolean bLargeStep)
    {
    }
}
