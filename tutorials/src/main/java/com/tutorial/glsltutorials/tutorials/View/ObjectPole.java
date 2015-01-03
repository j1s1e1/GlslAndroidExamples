package com.tutorial.glsltutorials.tutorials.View;

import android.graphics.Point;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Quaternion;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector2f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

/**
 * Created by Jamie on 6/7/14.
 */
public class ObjectPole implements IPole {
    int MM_KEY_SHIFT = 0x01;	///<One of the shift keys.
    int MM_KEY_CTRL = 0x02;	///<One of the control keys.
    int MM_KEY_ALT = 0x04;	///<One of the alt keys.
    boolean rightMultiply()
    {
        return true; // MatrixStack.rightMultiply;
    }
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
        m_pView = LookatProvider;
        m_po = initialData;
        m_initialPo = initialData;
        m_rotateScale = rotateScale;
        m_actionButton = actionButton;
        m_bIsDragging = false;
        initialPosition = initialData;
    }

    ///Generates the local-to-world matrix for this object.
    public Matrix4f CalcMatrix()
    {
        Matrix4f translateMat = Matrix4f.Identity();
        translateMat.SetRow3(new Vector4f(m_po.position, 1.0f));
        if (rightMultiply())
        {
            return Matrix4f.Mult(translateMat, Matrix4f.createFromQuaternion(m_po.orientation));
        }
        else
        {
            return Matrix4f.Mult(Matrix4f.createFromQuaternion(m_po.orientation), translateMat);
        }
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
    public ObjectData GetPosOrient()
    {
        return m_po;
    }

    public void move(float x, float y, float z)
    {
        move(new Vector3f(x, y, z));
    }

    public void move(Vector3f v)
    {
        m_po.position = m_po.position.add(v);
    }

    public Quaternion getOrient()
    {
        return m_po.orientation;
    }

    public void setOrient(Quaternion newOrientation)
    {
        m_po.orientation = newOrientation;
    }


    public void rotate(Quaternion rotation)
    {
        if (rightMultiply())
        {
            m_po.orientation = Quaternion.mult(m_po.orientation, rotation);
        }
        else
        {
            m_po.orientation = Quaternion.mult(rotation, m_po.orientation);
        }
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
    public void mouseClick(MouseButtons button, boolean isPressed, int modifiers, Point position)
    {
        if(isPressed)
        {
            //Ignore button presses when dragging.
            if(!m_bIsDragging)
            {
                if(button == m_actionButton)
                {
                    if((modifiers & MM_KEY_ALT) != 0)
                        m_RotateMode = RotateMode.SPIN_VIEW_AXIS;
                    else if((modifiers & MM_KEY_CTRL) != 0)
                        m_RotateMode = RotateMode.BIAXIAL;
                    else
                        m_RotateMode = RotateMode.DUAL_AXIS;

                    m_prevMousePos = new Vector2f(position.x, position.y);
                    m_startDragMousePos =  new Vector2f(position.x, position.y);
                    m_startDragOrient = m_po.orientation;

                    m_bIsDragging = true;
                }
            }
        }
        else
        {
            //Ignore up buttons if not dragging.
            if(m_bIsDragging)
            {
                if(button == m_actionButton)
                {
                    mouseMove(position);

                    m_bIsDragging = false;
                }
            }
        }
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

    public void mouseButton(int button, int state, int x, int y)
    {
    }

    public void mouseButton(int button, int state, Point p)
    {
    }

    public void mouseMove(Point position)
    {
        Vector2f vectorPositoin = new Vector2f(position.x, position.y);
        if(m_bIsDragging)
        {
            Vector2f iDiff = vectorPositoin.sub(m_prevMousePos);

            switch(m_RotateMode)
            {
                case DUAL_AXIS:
                {
                    Quaternion rotRight =  Quaternion.fromAxisAngle(Vector3f.UnitY, iDiff.x * m_rotateScale);
                    Quaternion rotLeft =  Quaternion.fromAxisAngle(Vector3f.UnitX, iDiff.y * m_rotateScale);
                    Quaternion rot = Quaternion.mult(rotLeft, rotRight);
                    rot.normalize();
                    RotateViewDegrees(rot);
                }
                break;
                case BIAXIAL:
                {
                    Vector2f iInitDiff = vectorPositoin.sub(m_startDragMousePos);
                    Quaternion rot;

                    float degAngle;
                    if(Math.abs(iInitDiff.x) > Math.abs(iInitDiff.y))
                    {
                        degAngle = iInitDiff.x * m_rotateScale;
                        rot =  Quaternion.fromAxisAngle(Vector3f.UnitY, degAngle);
                    }
                    else
                    {
                        degAngle = iInitDiff.y * m_rotateScale;
                        rot =  Quaternion.fromAxisAngle(Vector3f.UnitX, degAngle);
                    }
                    RotateViewDegrees(rot, true);
                }
                break;
                case SPIN_VIEW_AXIS:
                    RotateViewDegrees(Quaternion.fromAxisAngle(Vector3f.UnitZ, -iDiff.x * m_rotateScale));
                    break;
            }

            m_prevMousePos = vectorPositoin;
        }
    }

    public void mouseWheel(int direction, int modifiers, Point position)
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
