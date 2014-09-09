package com.tutorial.glsltutorials.tutorials;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector2f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

/**
 * Created by Jamie on 6/7/14.
 */
public class ObjectPole extends Pole {
    private ObjectData position;
    private ObjectData initialPosition;
    private float rotateScale;
    private boolean isDragging;
		/*
		private ViewPole.RotatingMode rotatingMode;
		private Vec2 prevMousePos;
		private Vec2 startDragMousePos;
		private Quat startDragOrient;
		private ViewPole viewPole;
		*/

    /**
     \brief Creates an object pole with a given initial position and orientation.

     \param initialData The starting position and orientation of the object in world space.
     \param rotateScale The number of degrees to rotate the object per window space pixel
     \param actionButton The mouse button to listen for. All other mouse buttons are ignored.
     \param pLookatProvider An object that will compute a view matrix. This defines the view space
     that orientations can be relative to. If it is NULL, then orientations will be relative to the world.
     **/
    public ObjectPole(ObjectData initialData, float rotateScale, MouseButtons actionButton, ViewProvider LookatProvider)
    {
        position = initialData;
        initialPosition = initialData;
    }

    ///Generates the local-to-world matrix for this object.
    public Matrix4f CalcMatrix()
    {
        return Matrix4f.CreateTranslation(position.position);
    }

    /**
     \brief Sets the scaling factor for orientation changes.

     The scaling factor is the number of degrees to rotate the object per window space pixel.
     The scale is the same for all mouse movements.
     **/
    void SetRotationScale(float rotateScale)
    {
    }
    ///Gets the current scaling factor for orientation changes.
    float GetRotationScale()
    {
        return m_rotateScale;
    }

    ///Retrieves the current position and orientation of the object.
    ObjectData GetPosOrient()
    {
        return m_po;
    }

    ///Resets the object to the initial position/orientation. Will fail if currently dragging.
    void Reset()
    {
    }

    /**
     \name Input Providers

     These functions provide input, since Poles cannot get input for themselves. See
     \ref module_glutil_poles "the Pole manual" for details.
     **/
    ///@{

    /**
     \brief Notifies the pole of a mouse button being pressed or released.

     \param button The button being pressed or released.
     \param isPressed Set to true if \a button is being pressed.
     \param modifiers A bitfield of MouseModifiers that specifies the modifiers being held down currently.
     \param position The mouse position at the moment of the mouse click.
     **/
    void MouseClick(MouseButtons button, boolean isPressed, int modifiers, Vector2f position)
    {
    }

    ///Notifies the pole that the mouse has moved to the given absolute position.
    void MouseMove(Vector2f  position)
    {
    }

    /**
     \brief Notifies the pole that the mouse wheel has been rolled up or down.

     \param direction A positive number if the mouse wheel has moved up, otherwise it should be negative.
     \param modifiers The modifiers currently being held down when the wheel was rolled.
     \param position The absolute mouse position at the moment the wheel was rolled.
     **/
    void MouseWheel(int direction, int modifiers, Vector2f position)
    {
    }

    /**
     \brief Notifies the pole that a character has been entered.

     \param key ASCII keycode.
     **/
    void CharPress(char key)
    {
    }
    ///@}

    ///Returns true if the mouse is currently being dragged.
    boolean IsDragging()
    {
        return m_bIsDragging;
    }

    private enum Axis
    {
        AXIS_X,
        AXIS_Y,
        AXIS_Z,

        NUM_AXES,
    };

    enum RotateMode
    {
        RM_DUAL_AXIS,
        RM_BIAXIAL,
        RM_SPIN,
    };

    void RotateWorldDegrees(Quaternion rot)
    {
        RotateWorldDegrees(rot, false);
    }

    void RotateWorldDegrees(Quaternion rot, boolean bFromInitial)
    {
    }

    void RotateLocalDegrees(Quaternion rot)
    {
        RotateLocalDegrees(rot, false);
    }

    void RotateLocalDegrees(Quaternion rot, boolean bFromInitial)
    {
    }

    void RotateViewDegrees(Quaternion rot)
    {
        RotateViewDegrees(rot, false);
    }

    void RotateViewDegrees(Quaternion rot, boolean bFromInitial)
    {
    }

    ViewProvider m_pView;
    ObjectData m_po;
    ObjectData m_initialPo;

    float m_rotateScale;
    MouseButtons m_actionButton;

    //Used when rotating.
    RotateMode m_RotateMode;
    boolean m_bIsDragging;

    Vector2f m_prevMousePos;
    Vector2f m_startDragMousePos;
    Quaternion m_startDragOrient;

}
