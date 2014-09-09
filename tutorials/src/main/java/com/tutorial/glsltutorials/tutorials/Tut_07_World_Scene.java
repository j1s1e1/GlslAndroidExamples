package com.tutorial.glsltutorials.tutorials;

import android.opengl.GLES20;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;

import java.io.InputStream;

/**
 * Created by Jamie on 6/8/14.
 */
public class Tut_07_World_Scene extends TutorialBase {
    class ProgramData
    {
        public int theProgram;
        public int positionAttribute;
        public int colorAttribute;
        public int modelToWorldMatrixUnif;
        public int worldToCameraMatrixUnif;
        public int cameraToClipMatrixUnif;
        public int baseColorUnif;
    };

    static float g_fzNear = 1.0f;
    static float g_fzFar = 1000.0f;

    static ProgramData UniformColor;
    static ProgramData ObjectColor;
    static ProgramData UniformColorTint;

    ProgramData LoadProgram(String strVertexShader, String strFragmentShader)
    {
        ProgramData data = new ProgramData();
        int vertex_shader = Shader.loadShader30(GLES20.GL_VERTEX_SHADER, strVertexShader);
        int fragment_shader = Shader.loadShader30(GLES20.GL_FRAGMENT_SHADER, strFragmentShader);
        data.theProgram  = Shader.createAndLinkProgram30(vertex_shader, fragment_shader);


        data.positionAttribute = GLES20.glGetAttribLocation(data.theProgram, "position");
        data.colorAttribute = GLES20.glGetAttribLocation(data.theProgram, "color");

        data.modelToWorldMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToWorldMatrix");
        data.worldToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "worldToCameraMatrix");
        data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");
        data.baseColorUnif = GLES20.glGetUniformLocation(data.theProgram, "baseColor");

        return data;
    }

    void InitializeProgram()
    {
        UniformColor = LoadProgram(VertexShaders.PosOnlyWorldTransform_vert, FragmentShaders.ColorUniform_frag);
        ObjectColor = LoadProgram(VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorPassthrough_frag);
        UniformColorTint = LoadProgram(VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorMultUniform_frag);
    }

    static Mesh g_pConeMesh;
    static Mesh g_pCylinderMesh;
    static Mesh g_pCubeTintMesh;
    static Mesh g_pCubeColorMesh;
    static Mesh g_pPlaneMesh;

    //Called after the window and OpenGL are initialized. Called exactly once, before the main loop.
    protected void init() throws Exception
    {
        InitializeProgram();

        try
        {
            InputStream UnitConeTint = Shader.context.getResources().openRawResource(R.raw.unitconetint);
            g_pConeMesh = new Mesh(UnitConeTint);
            InputStream UnitCylinderTint = Shader.context.getResources().openRawResource(R.raw.unitcylindertint);
            g_pCylinderMesh = new Mesh(UnitCylinderTint);
            InputStream UnitCubeTint = Shader.context.getResources().openRawResource(R.raw.unitcubetint);
            g_pCubeTintMesh = new Mesh(UnitCubeTint);
            InputStream UnitCubeColor = Shader.context.getResources().openRawResource(R.raw.unitcubecolor);
            g_pCubeColorMesh = new Mesh(UnitCubeColor);
            InputStream UnitPlane = Shader.context.getResources().openRawResource(R.raw.unitplane);
            g_pPlaneMesh = new Mesh(UnitPlane);
        }
        catch (Exception ex)
        {
            throw new Exception("Error " + ex.toString());
        }

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CW);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthMask(true);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthRangef(0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_CLAMP_TO_EDGE);
    }

    //Trees are 3x3 in X/Z, and fTrunkHeight+fConeHeight in the Y.
    static void DrawTree(MatrixStack modelMatrix) throws Exception
    {
        DrawTree(modelMatrix, 2.0f, 3.0f);
    }

    static void DrawTree(MatrixStack modelMatrix, float fTrunkHeight, float fConeHeight) throws Exception
    {
        try (PushStack pushstack = new PushStack(modelMatrix))
        {

            modelMatrix.Scale(1.0f, fTrunkHeight, 1.0f);
            modelMatrix.Translate(0.0f, 0.5f, 0.0f);

            GLES20.glUseProgram(UniformColorTint.theProgram);
            Matrix4f mm = modelMatrix.Top();
            GLES20.glUniformMatrix4fv(UniformColorTint.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
            GLES20.glUniform4f(UniformColorTint.baseColorUnif, 0.694f, 0.4f, 0.106f, 1.0f);
            g_pCylinderMesh.Render();
            GLES20.glUseProgram(0);
        }

        //Draw the treetop
        try (PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Translate(0.0f, fTrunkHeight, 0.0f);
            modelMatrix.Scale(3.0f, fConeHeight, 3.0f);

            GLES20.glUseProgram(UniformColorTint.theProgram);
            Matrix4f mm = modelMatrix.Top();

            GLES20.glUniformMatrix4fv(UniformColorTint.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
            GLES20.glUniform4f(UniformColorTint.baseColorUnif, 0.0f, 1.0f, 0.0f, 1.0f);

            g_pConeMesh.Render();
            GLES20.glUseProgram(0);
        }
    }

    float g_fColumnBaseHeight = 0.25f;

    //Columns are 1x1 in the X/Z, and fHieght units in the Y.
    void DrawColumn(MatrixStack modelMatrix) throws Exception
    {
        DrawColumn(modelMatrix, 5.0f);
    }
    
    void DrawColumn(MatrixStack modelMatrix, float fHeight) throws Exception
    {
        //Draw the bottom of the column.
        try (PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Scale(1.0f, g_fColumnBaseHeight, 1.0f);
            modelMatrix.Translate(0.0f, 0.5f, 0.0f);

            GLES20.glUseProgram(UniformColorTint.theProgram);
            Matrix4f mm = modelMatrix.Top();
            GLES20.glUniformMatrix4fv(UniformColorTint.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
            GLES20.glUniform4f(UniformColorTint.baseColorUnif, 1.0f, 1.0f, 1.0f, 1.0f);
            g_pCubeTintMesh.Render();
            GLES20.glUseProgram(0);
        }

        //Draw the top of the column.
        try (PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Translate(new Vector3f(0.0f, fHeight - g_fColumnBaseHeight, 0.0f));
            modelMatrix.Scale(new Vector3f(1.0f, g_fColumnBaseHeight, 1.0f));
            modelMatrix.Translate(new Vector3f(0.0f, 0.5f, 0.0f));

            GLES20.glUseProgram(UniformColorTint.theProgram);
            Matrix4f mm = modelMatrix.Top();
            GLES20.glUniformMatrix4fv(UniformColorTint.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
            GLES20.glUniform4f(UniformColorTint.baseColorUnif, 0.9f, 0.9f, 0.9f, 0.9f);
            g_pCubeTintMesh.Render();
            GLES20.glUseProgram(0);
        }

        //Draw the main column.
        try (PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Translate(new Vector3f(0.0f, g_fColumnBaseHeight, 0.0f));
            modelMatrix.Scale(new Vector3f(0.8f, fHeight - (g_fColumnBaseHeight * 2.0f), 0.8f));
            modelMatrix.Translate(new Vector3f(0.0f, 0.5f, 0.0f));

            GLES20.glUseProgram(UniformColorTint.theProgram);
            Matrix4f mm = modelMatrix.Top();
            GLES20.glUniformMatrix4fv(UniformColorTint.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
            GLES20.glUniform4f(UniformColorTint.baseColorUnif, 0.9f, 0.9f, 0.9f, 0.9f);
            g_pCylinderMesh.Render();
            GLES20.glUseProgram(0);
        }
    }

    float g_fParthenonWidth = 14.0f;
    float g_fParthenonLength = 20.0f;
    float g_fParthenonColumnHeight = 5.0f;
    float g_fParthenonBaseHeight = 1.0f;
    float g_fParthenonTopHeight = 2.0f;

    void DrawParthenon(MatrixStack modelMatrix) throws Exception
    {
        //Draw base.
        try (PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Scale(g_fParthenonWidth, g_fParthenonBaseHeight, g_fParthenonLength);
            modelMatrix.Translate(0.0f, 0.5f, 0.0f);

            GLES20.glUseProgram(UniformColorTint.theProgram);
            SetGlobalMatrices(UniformColorTint);
            Matrix4f mm = modelMatrix.Top();
            GLES20.glUniformMatrix4fv(UniformColorTint.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
            GLES20.glUniform4f(UniformColorTint.baseColorUnif, 0.9f, 0.9f, 0.9f, 0.9f);
            g_pCubeTintMesh.Render();
            GLES20.glUseProgram(0);
        }

        //Draw top.
        try (PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Translate(0.0f, g_fParthenonColumnHeight + g_fParthenonBaseHeight, 0.0f);
            modelMatrix.Scale(g_fParthenonWidth, g_fParthenonTopHeight, g_fParthenonLength);
            modelMatrix.Translate(0.0f, 0.5f, 0.0f);

            GLES20.glUseProgram(UniformColorTint.theProgram);
            Matrix4f mm = modelMatrix.Top();
            GLES20.glUniformMatrix4fv(UniformColorTint.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
            GLES20.glUniform4f(UniformColorTint.baseColorUnif, 0.9f, 0.9f, 0.9f, 0.9f);
            g_pCubeTintMesh.Render();
            GLES20.glUseProgram(0);
        }

        //Draw columns.
        float fFrontZVal = (g_fParthenonLength / 2.0f) - 1.0f;
        float fRightXVal = (g_fParthenonWidth / 2.0f) - 1.0f;

        for (int iColumnNum = 0; iColumnNum < (int)(g_fParthenonWidth / 2.0f); iColumnNum++)
        {
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.Translate((2.0f * iColumnNum) - (g_fParthenonWidth / 2.0f) + 1.0f,
                        g_fParthenonBaseHeight, fFrontZVal);

                DrawColumn(modelMatrix, g_fParthenonColumnHeight);
            }
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.Translate((2.0f * iColumnNum) - (g_fParthenonWidth / 2.0f) + 1.0f,
                        g_fParthenonBaseHeight, -fFrontZVal);

                DrawColumn(modelMatrix, g_fParthenonColumnHeight);
            }
        }

        //Don't draw the first or last columns, since they've been drawn already.
        for (int iColumnNum = 1; iColumnNum < (int)((g_fParthenonLength - 2.0f) / 2.0f); iColumnNum++)
        {
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.Translate(fRightXVal,
                        g_fParthenonBaseHeight, (2.0f * iColumnNum) - (g_fParthenonLength / 2.0f) + 1.0f);

                DrawColumn(modelMatrix, g_fParthenonColumnHeight);
            }
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.Translate(-fRightXVal,
                        g_fParthenonBaseHeight, (2.0f * iColumnNum) - (g_fParthenonLength / 2.0f) + 1.0f);

                DrawColumn(modelMatrix, g_fParthenonColumnHeight);
            }
        }

        //Draw interior.
        try (PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Translate(0.0f, 1.0f, 0.0f);
            modelMatrix.Scale(g_fParthenonWidth - 6.0f, g_fParthenonColumnHeight,
                    g_fParthenonLength - 6.0f);
            modelMatrix.Translate(0.0f, 0.5f, 0.0f);

            GLES20.glUseProgram(ObjectColor.theProgram);
            Matrix4f mm = modelMatrix.Top();
            GLES20.glUniformMatrix4fv(ObjectColor.modelToWorldMatrixUnif,1, false, mm.toArray(), 0);
            g_pCubeColorMesh.Render();
            GLES20.glUseProgram(0);
        }

        //Draw headpiece.
        try (PushStack pushstack = new PushStack(modelMatrix))
        {
            modelMatrix.Translate(
                    0.0f,
                    g_fParthenonColumnHeight + g_fParthenonBaseHeight + (g_fParthenonTopHeight / 2.0f),
                    g_fParthenonLength / 2.0f);
            modelMatrix.RotateX(-135.0f);
            modelMatrix.RotateY(45.0f);

            GLES20.glUseProgram(ObjectColor.theProgram);
            Matrix4f mm = modelMatrix.Top();
            GLES20.glUniformMatrix4fv(ObjectColor.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
            g_pCubeColorMesh.Render();
            GLES20.glUseProgram(0);
        }
    }

    static void DrawForest(MatrixStack modelMatrix) throws Exception
    {
        for (int iTree = 0; iTree < TreeData.g_forest.length; iTree++)
        {
            TreeData currTree = TreeData.g_forest[iTree];
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.Translate(currTree.fXPos, 0.0f, currTree.fZPos);
                DrawTree(modelMatrix, currTree.fTrunkHeight, currTree.fConeHeight);
            }
        }
    }

    static boolean g_bDrawLookatPoint = false;

    //Called to update the display.
    //You should call glutSwapBuffers after all of your rendering to display what you rendered.
    //If you need continuous updates of the screen, call glutPostRedisplay() at the end of the function.
    public void display() throws Exception
    {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if ((g_pConeMesh != null) && (g_pCylinderMesh != null) && (g_pCubeTintMesh != null) &&
                (g_pCubeColorMesh != null) && (g_pPlaneMesh != null)) {


            MatrixStack modelMatrix = new MatrixStack();

            //Render the ground plane.
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.Scale(100.0f, 1.0f, 100.0f);

                GLES20.glUseProgram(UniformColor.theProgram);

                Matrix4f mm = modelMatrix.Top();
                GLES20.glUniformMatrix4fv(UniformColor.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
                GLES20.glUniform4f(UniformColor.baseColorUnif, 0.302f, 0.416f, 0.0589f, 1.0f);
                g_pPlaneMesh.Render();
                GLES20.glUseProgram(0);
            }


            //Draw the trees
            DrawForest(modelMatrix);

            //Draw the building.
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.Translate(20.0f, 0.0f, -10.0f);
                DrawParthenon(modelMatrix);
            }

            if (g_bDrawLookatPoint)
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                GLES20.glDisable(GLES20.GL_DEPTH_TEST);

                modelMatrix.Translate(Camera.g_camTarget);
                modelMatrix.Scale(1.0f, 1.0f, 1.0f);

                GLES20.glUseProgram(ObjectColor.theProgram);
                Matrix4f mm = modelMatrix.Top();
                GLES20.glUniformMatrix4fv(ObjectColor.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
                g_pCubeColorMesh.Render();
                GLES20.glUseProgram(0);
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            }
        }
    }

    static Matrix4f pm;
    static Matrix4f cm;

    static private void SetGlobalMatrices(ProgramData program)
    {
        GLES20.glUseProgram(program.theProgram);
        GLES20.glUniformMatrix4fv(program.cameraToClipMatrixUnif, 1, false, pm.toArray(), 0);  // this one is first
        GLES20.glUniformMatrix4fv(program.worldToCameraMatrixUnif, 1, false, cm.toArray(), 0); // this is the second one
        GLES20.glUseProgram(0);
    }

    //Called whenever the window is resized. The new window size is given, in pixels.
    //This is an opportunity to call glViewport or glScissor to keep up with the change in size.
    public void reshape()
    {
        MatrixStack camMatrix = new MatrixStack();
        camMatrix.SetMatrix(Camera.GetLookAtMatrix());

        cm = camMatrix.Top();

        MatrixStack persMatrix = new MatrixStack();
        persMatrix.Perspective(45.0f, (width / (float)height), g_fzNear, g_fzFar);
        pm = persMatrix.Top();

        SetGlobalMatrices(UniformColor);
        SetGlobalMatrices(ObjectColor);
        SetGlobalMatrices(UniformColorTint);

        GLES20.glViewport(0, 0, width, height);
    }

    //Called whenever a key on the keyboard was pressed.
    //The key is given by the ''key'' parameter, which is in ASCII.
    //It's often a good idea to have the escape key (ASCII value 27) call glutLeaveMainLoop() to 
    //exit the program.
    public String keyboard(int keyCode, int x, int y)
    {
        String result = "";
        switch (keyCode) {
            case KeyEvent.KEYCODE_ESCAPE:
                g_pConeMesh = null;
                g_pCylinderMesh = null;
                g_pCubeTintMesh = null;
                g_pCubeColorMesh = null;
                g_pPlaneMesh = null;
                //timer.Enabled = false;
                return "Escape";
            case KeyEvent.KEYCODE_A:
                result = "A Decrease g_camTarget.X";
                Camera.MoveTarget(-4.0f, 0, 0);
                break;
            case KeyEvent.KEYCODE_D:
                result = "D Increase g_camTarget.X";
                Camera.MoveTarget(4.0f, 0, 0);
                break;
            case KeyEvent.KEYCODE_E:
                result = "E Decrease g_camTarget.Y";
                Camera.MoveTarget(0, -4.0f, 0);
                break;
            case KeyEvent.KEYCODE_Q:
                result = "Q Increase g_camTarget.Y";
                Camera.MoveTarget(0, 4.0f, 0);
                break;
            case KeyEvent.KEYCODE_W:
                result = "W Decrease g_camTarget.Z";
                Camera.MoveTarget(0, 0, -4.0f);
                break;
            case KeyEvent.KEYCODE_S:
                result = "S Increase g_camTarget.Z";
                Camera.MoveTarget(0, 0, 4.0f);
                break;
            case KeyEvent.KEYCODE_J:
                result = "J Decrease g_sphereCamRelPos.Y";
                Camera.Move(-4.0f, 0, 0);
                break;
            case KeyEvent.KEYCODE_L:
                result = "L Increase g_sphereCamRelPos.Y";
                Camera.Move(4.0f, 0, 0);
                break;
            case KeyEvent.KEYCODE_I:
                result = "I Decrease g_sphereCamRelPos.Y";
                Camera.Move(0, -4.0f, 0);
                break;
            case KeyEvent.KEYCODE_K:
                result = "K Increase g_sphereCamRelPos.Y";
                Camera.Move(0, 4.0f, 0);
                break;
            case KeyEvent.KEYCODE_O:
                result = "O Decrease g_sphereCamRelPos.Y";
                Camera.Move(0, 0, -4.0f);
                break;
            case KeyEvent.KEYCODE_U:
                result = "U Increase g_sphereCamRelPos.Y";
                Camera.Move(0, 0, 4.0f);
                break;

            case KeyEvent.KEYCODE_SPACE:
                result = "Space";
                g_bDrawLookatPoint = !g_bDrawLookatPoint;
                break;

        }
        result = result + Camera.GetPositionString();
        result = result + Camera.GetTargetString();

        reshape();
        return result;
    }

    public void TouchEvent(int x_position, int y_position) throws Exception
    {
        if (x_position > width * 3/4)
        {
            Camera.MoveTarget(-4.0f, 0, 0);
        }
        if (x_position < width * 1/4)
        {
            Camera.MoveTarget(0, 0, 4.0f);
        }
        reshape();
        display();
    }
}
