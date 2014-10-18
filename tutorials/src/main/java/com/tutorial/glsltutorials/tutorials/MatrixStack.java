package com.tutorial.glsltutorials.tutorials;

import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector2f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

import java.util.Stack;

/**
 * Created by Jamie on 6/7/14.
 */
public class MatrixStack {
    private Stack<Matrix4f> m_stack;
    private Matrix4f m_currMatrix;


    /**
     \file
     \brief Contains a \ref module_glutil_matrixstack "matrix stack and associated classes".
     **/

    ///\addtogroup module_glutil_matrixstack
    ///@{

    /**
     \brief Implements a stack for glm::mat4 transformations.

     A matrix stack is a sequence of transforms which you can preserve and restore as needed. The
     stack has the concept of a "current matrix", which can be retrieved with the Top() function.
     The top matrix can even be obtained as a float array. The pointer returned will remain valid until
     this object is destroyed (though its values will change). This is useful for uploading matrices
     to OpenGL via glUniformMatrix4ffv.

     The other functions will right-multiply a transformation matrix with the current matrix, thus
     changing the current matrix.

     The main power of the matrix stack is the ability to preserve and restore matrices in a stack fashion.
     The current matrix can be preserved on the stack with push() and the most recently preserved matrix
     can be restored with pop(). You must ensure that you do not pop() more times than you push(). Also,
     while this matrix stack does not have an explicit size limit, if you push() more times than you pop(),
     then you can eventually run out of memory (unless you create and destroy the MatrixStack every frame).

     The best way to manage the stack is to never use the push() and pop() methods directly.
     Instead, use the pushStack object to do all pushing and popping. That will ensure that
     overflows and underflows cannot not happen.
     **/

    ///Initializes the matrix stack with the identity matrix.
    public MatrixStack()
    {
        m_stack = new Stack<Matrix4f>();
        m_currMatrix = Matrix4f.Identity();
        m_stack.push(m_currMatrix);
    }

    ///Initializes the matrix stack with the given matrix.
    public MatrixStack(Matrix4f initialMatrix)
    {
        m_stack = new Stack<Matrix4f>();
        m_currMatrix = initialMatrix;
        m_stack.push(m_currMatrix);
    }

    /**
     \name Stack Maintanence Functions

     These functions maintain the matrix stack. You must take care not to underflow or overflow the stack.
     **/
    ///@{

    ///Preserves the current matrix on the stack.
    public void push()
    {
        Matrix4f new_matrix = new Matrix4f(m_currMatrix);  // avoid same object
        m_stack.push(new_matrix);
    }

    ///Restores the most recently preserved matrix.
    public void pop()
    {
        m_currMatrix = m_stack.peek();
        m_stack.pop();
    }

    /**
     \brief Restores the current matrix to the value of the most recently preserved matrix.

     This function does not affect the depth of the matrix stack.
     **/
    public void Reset() { m_currMatrix = m_stack.peek(); }

    ///Retrieve the current matrix.
    public Matrix4f Top()
    {
        return m_currMatrix;
    }
    ///@}

    /**
     \name Rotation Matrix Functions

     These functions right-multiply the current matrix with a rotation matrix of some form.
     All rotation angles are counter-clockwise for an observer looking down the axis direction.
     If an observer is facing so that the axis of rotation is pointing directly towards the user,
     then positive angles will rotate counter-clockwise.
     **/
    ///@{

    ///Applies a rotation matrix about the given axis, with the given angle in degrees.
    public void Rotate(Vector3f axis, float angDegCCW)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(axis, (float) Math.PI / 180.0f * angDegCCW);
        m_currMatrix = Matrix4f.Mult(rotation, m_currMatrix);
    }

    ///Applies a rotation matrix about the given axis, with the given angle in radians.
    public void RotateRadians(Vector3f axis, float angRadCCW)
    {
        Matrix4f rotation = Matrix4f.CreateFromAxisAngle(axis, angRadCCW);
        m_currMatrix = Matrix4f.Mult(rotation, m_currMatrix);
    }

    ///Applies a rotation matrix about the +X axis, with the given angle in degrees.
    public void RotateX(float angDegCCW)
    {
        Rotate(new Vector3f(1,0,0), angDegCCW);
    }
    ///Applies a rotation matrix about the +Y axis, with the given angle in degrees.
    public void RotateY(float angDegCCW)
    {
        Rotate(new Vector3f(0,1,0), angDegCCW);
    }
    ///Applies a rotation matrix about the +Z axis, with the given angle in degrees.
    public void RotateZ(float angDegCCW)
    {
        Rotate(new Vector3f(0,0,1), angDegCCW);
    }
    ///@}

    /**
     \name Scale Matrix Functions

     These functions right-multiply the current matrix with a scaling matrix of some form.
     **/
    ///@{

    ///Applies a scale matrix, with the given glm::vec3 as the axis scales.
    public void Scale(Vector3f scaleVec)
    {
        m_currMatrix.Scale(scaleVec);
    }
    ///Applies a scale matrix, with the given values as the axis scales.
    public void Scale(float scaleX, float scaleY, float scaleZ)
    {
        Scale(new Vector3f(scaleX, scaleY, scaleZ));
    }
    ///Applies a uniform scale matrix.
    public void Scale(float uniformScale)
    {
        Scale(new Vector3f(uniformScale));
    }
    ///@}

    /**
     \name Translation Matrix Functions

     These functions right-multiply the current matrix with a translation matrix of some form.
     **/
    ///@{

    ///Applies a translation matrix, with the given glm::vec3 as the offset.
    public void Translate(Vector3f offsetVec)
    {
        m_currMatrix = Matrix4f.Mult(Matrix4f.CreateTranslation(offsetVec), m_currMatrix);
    }
    ///Applies a translation matrix, with the given X, Y and Z values as the offset.
    public void Translate(float transX, float transY, float transZ)
    {
        Translate(new Vector3f(transX, transY, transZ));
    }
    ///@}

    /**
     \name Camera Matrix Functions

     These functions right-multiply the current matrix with a matrix that transforms from a world space to
     the camera space expected by the Perspective() or Orthographic() functions.
     **/
    ///@{

    /**
     \brief Applies a matrix that transforms to a camera-space defined by a position, a target in the world, and an up direction.

     \param cameraPos The world-space position of the camera.
     \param lookatPos The world-space position the camera should be facing. It should not be equal to \a cameraPos.
     \param upDir The world-space direction vector that should be considered up. The generated matrix will be bad
     if the up direction is along the same direction as the direction the camera faces (the direction between
     \a cameraPos and \a lookatPos).
     **/
    void LookAt(Vector3f cameraPos, Vector3f lookatPos, Vector3f upDir)
    {
        Matrix4f look_at = Matrix4f.LookAt(cameraPos, lookatPos, upDir);
        m_currMatrix = Matrix4f.Mult(look_at, m_currMatrix);
    }
    ///@}

    /**
     \name Projection Matrix Functions

     These functions right-multiply the current matrix with a projection matrix of some form. These
     functions all transform positions into the 4D homogeneous space expected by the output of
     OpenGL vertex shaders. As such, these can be used directly with GLSL shaders.

     The space that these matrices transform from is defined as follows. The pre-projection space,
     called camera space or eye space, has the camera/eye position at the origin. The camera faces down the
     -Z axis, so objects with larger negative Z values are farther away. +Y is up and +X is to the right.
     **/
    ///@{

    /**
     \brief Applies a standard, OpenGL-style perspective projection matrix.

     \param degFOV The field of view. This is the angle in degrees between directly forward and the farthest
     visible point horizontally.
     \param aspectRatio The ratio of the width of the view area to the height.
     \param zNear The closest camera-space distance to the camera that can be seen.
     The projection will be clipped against this value. It cannot be negative or 0.0.
     \param zFar The farthest camera-space distance from the camera that can be seen.
     The projection will be clipped against this value. It must be larger than \a zNear.
     **/
    public void Perspective(float degFOV, float aspectRatio, float zNear, float zFar)
    {
        Matrix4f persp = Matrix4f.CreatePerspectiveFieldOfView(
                (float)Math.PI / 180 * degFOV, aspectRatio, zNear, zFar);
        m_currMatrix = Matrix4f.Mult(persp, m_currMatrix);
    }

    /**
     \brief Applies a standard, OpenGL-style orthographic projection matrix.

     \param left The left camera-space position in the X axis that will be captured within the projection.
     \param right The right camera-space position in the X axis that will be captured within the projection.
     \param bottom The bottom camera-space position in the Y axis that will be captured within the projection.
     \param top The top camera-space position in the Y axis that will be captured within the projection.
     \param zNear The front camera-space position in the Z axis that will be captured within the projection.
     \param zFar The rear camera-space position in the Z axis that will be captured within the projection.
     **/
    void Orthographic(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        Matrix4f orth = Matrix4f.CreateOrthographic(right - left, top-bottom, zNear, zFar);
        m_currMatrix = Matrix4f.Mult(orth, m_currMatrix);
    }

    void Orthographic(float left, float right, float bottom, float top)
    {
        Orthographic(left, right, bottom, top, -1f, 1f);
    }

    /**
     \brief Applies an ortho matrix for pixel-accurate reproduction.

     A common use for orthographic projections is to create an ortho matrix that allows for pixel-accurate
     reproduction of textures. It allows you to provide vertices directly in window space.

     The camera space that this function creates can have the origin at the top-left (with +y going down)
     or bottom-left (with +y going up). Note that a top-left orientation will have to flip the Y coordinate,
     which means that the winding order of any triangles are reversed.

     The depth range is arbitrary and up to the user.

     \param size The size of the window space.
     \param depthRange The near and far depth range. The x coord is zNear, and the y coord is zFar.
     \param isTopLeft True if this should be top-left orientation, false if it should be bottom-left.
     **/
    void PixelPerfectOrtho(Vector2f size, Vector2f depthRange, boolean isTopLeft)
    {
        Matrix4f orth = Matrix4f.CreateOrthographic(size.x, size.y, depthRange.x, depthRange.y);
        m_currMatrix = Matrix4f.Mult(orth, m_currMatrix);
    }

    void PixelPerfectOrtho(Vector2f size, Vector2f depthRange)
    {
        PixelPerfectOrtho(size, depthRange, true);
    }
    ///@}

    /**
     \name Matrix Application

     These functions right-multiply a user-provided matrix by the current matrix; the result
     becomes the new current matrix.
     **/
    ///@{
    public void ApplyMatrix(Matrix4f theMatrix)
    {
        m_currMatrix = Matrix4f.Mult(theMatrix, m_currMatrix);
    }
    ///@}

    /**
     \name Matrix Setting

     These functions directly set the value of the current matrix, replacing the old value.
     Previously preserved matrices on the stack are unaffected.
     **/
    ///@{

    ///The given matrix becomes the current matrix.
    public void SetMatrix(Matrix4f theMatrix)
    {
        m_currMatrix = theMatrix;
    }
    ///Sets the current matrix to the identity matrix.
    public void SetIdentity()
    {
        m_currMatrix = Matrix4f.Identity();
    }
    ///@}

}
