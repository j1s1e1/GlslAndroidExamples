package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.tutorial.glsltutorials.tutorials.Camera;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.AnalysisTools;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Textures;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.Light.LightBlock;
import com.tutorial.glsltutorials.tutorials.Material.MaterialBlock;
import com.tutorial.glsltutorials.tutorials.MatrixStack;
import com.tutorial.glsltutorials.tutorials.Mesh.Mesh;
import com.tutorial.glsltutorials.tutorials.Objects.Ball;
import com.tutorial.glsltutorials.tutorials.ProgramData.Programs;
import com.tutorial.glsltutorials.tutorials.PushStack;
import com.tutorial.glsltutorials.tutorials.Shapes.Shape;
import com.tutorial.glsltutorials.tutorials.Textures.PaintWall;

import java.util.Random;

/**
 * Created by jamie on 12/25/14.
 */
public class Tut_Tennis3D extends TutorialBase 
{
    boolean zeroCameraToClip = false;
    boolean zeroWorldToCamera = true;
    boolean perspectiveOnly = false;

    float limit = 10f;
    float wallMovementStep = 0.1f;

    float frontwallRotation = 180f;
    static float wallAngle = 70f;
    static float wallMovement = 0.7f;
    static Vector3f wallScale = new Vector3f(20f, 20f, 20f);
    boolean useBall = false;
    boolean drawWalls = true;
    static boolean oldWallMovement = false;
    boolean reverseCamRot = false;
    Random random = new Random();
    int playerNumber = 0;
    int lastPlayer = 5;
    Ball ball;
    float ballRadius = 5f;
    float ballSpeedFactor = 25f;
    static float ballLimit = 50f;
    Vector3f ballLimitLow = new Vector3f(-ballLimit, -ballLimit, -ballLimit);
    Vector3f ballLimitHigh = new Vector3f(ballLimit, ballLimit, ballLimit);
    Vector3f ballSpeed;
    int ballProgram;
    Vector3f[] playerRotations = new Vector3f[]
            {
                    new Vector3f(0f, 0f, 0f),
                    new Vector3f(90f, 0f, 0f),
                    new Vector3f(-90f, 0f, 0f),
                    new Vector3f(0f, 90f, 0f),
                    new Vector3f(0f, -90f, 0f),
                    new Vector3f(0f, 0f, 180f),

            };
    boolean rotateWorld = false;
    static float g_fzNear = 10.0f;
    static float g_fzFar = 100.0f;

    float perspectiveAngle = 60f;
    float newPerspectiveAngle = 60f;

    static int NUMBER_OF_LIGHTS = 2;
    boolean pause = false;
    Vector3f position = new Vector3f(0f, 0f, 0f);
    Vector3f velocity = new Vector3f(5f, 7f, 3f);
    Vector3f positionLimitLow =  new Vector3f(-50f, -50f, -100f);
    Vector3f positionLimitHigh =  new Vector3f(50f, 50f, 100f);
    Matrix4f ballModelMatrix = Matrix4f.Identity();
    boolean renderWithString = false;
    String renderString = "";
    PaintWall frontWall;
    PaintWall backWall;
    PaintWall leftWall;
    PaintWall rightWall;
    PaintWall topWall;
    PaintWall bottomWall;

    class ProgramData
    {
        public int theProgram;
        public int positionAttribute;
        public int colorAttribute;
        public int modelToCameraMatrixUnif;
        public int modelToWorldMatrixUnif;
        public int worldToCameraMatrixUnif;
        public int cameraToClipMatrixUnif;
        public int baseColorUnif;

        public int normalModelToCameraMatrixUnif;
        public int dirToLightUnif;
        public int lightIntensityUnif;
        public int ambientIntensityUnif;
        public int normalAttribute;

        public LightBlock lightBlock;
        public MaterialBlock materialBlock;
    };

    static ProgramData UniformColor;
    static ProgramData ObjectColor;
    static ProgramData UniformColorTint;

    static ProgramData g_VertexDiffuseColor;
    static ProgramData g_WhiteAmbDiffuseColor;
    static ProgramData g_Unlit;
    static ProgramData g_litShaderProg;


    static ProgramData currentProgram;

    static Vector4f g_lightDirection = new Vector4f(0.866f, 0.5f, 0.0f, 0.0f);
    Vector3f dirToLight = new Vector3f(0.5f, 0.5f, 1f);

    ProgramData loadProgram(String strVertexShader, String strFragmentShader)
    {
        ProgramData data = new ProgramData();
        int vertex_shader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, strVertexShader);
        int fragment_shader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, strFragmentShader);
        data.theProgram  = Shader.createAndLinkProgram(vertex_shader, fragment_shader);

        data.positionAttribute = GLES20.glGetAttribLocation(data.theProgram, "position");
        data.colorAttribute = GLES20.glGetAttribLocation(data.theProgram, "color");
        if (data.positionAttribute != -1)
        {
            if (data.positionAttribute != 0)
            {
                Log.i("Tennis3D", "These meshes only work with position at location 0 " + strVertexShader);
            }
        }
        if (data.colorAttribute != -1)
        {
            if (data.colorAttribute != 1)
            {
                Log.i("Tennis3D", "These meshes only work with color at location 1" + strVertexShader);
            }
        }

        data.modelToWorldMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToWorldMatrix");
        data.worldToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "worldToCameraMatrix");
        data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "cameraToClipMatrix");
        if (data.cameraToClipMatrixUnif == -1)
        {
            data.cameraToClipMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "Projection.cameraToClipMatrix");
        }
        data.baseColorUnif = GLES20.glGetUniformLocation(data.theProgram, "baseColor");
        if (data.baseColorUnif == -1)
        {
            data.baseColorUnif = GLES20.glGetUniformLocation(data.theProgram, "objectColor");
        }

        data.modelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "modelToCameraMatrix");

        data.normalModelToCameraMatrixUnif = GLES20.glGetUniformLocation(data.theProgram, "normalModelToCameraMatrix");
        data.dirToLightUnif =  GLES20.glGetUniformLocation(data.theProgram, "dirToLight");
        data.lightIntensityUnif = GLES20.glGetUniformLocation(data.theProgram, "lightIntensity");
        data.ambientIntensityUnif = GLES20.glGetUniformLocation(data.theProgram, "ambientIntensity");
        data.normalAttribute = GLES20.glGetAttribLocation(data.theProgram, "normal");

        return data;
    }

    void initializeProgram()
    {
        UniformColor = loadProgram(VertexShaders.PosOnlyWorldTransform_vert, FragmentShaders.ColorUniform_frag);
        GLES20.glUseProgram(UniformColor.theProgram);
        GLES20.glUniform4f(UniformColor.baseColorUnif, 0.694f, 0.4f, 0.106f, 1.0f);
        GLES20.glUseProgram(0);

        UniformColorTint = loadProgram(VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorMultUniform_frag);
        GLES20.glUseProgram(UniformColorTint.theProgram);
        GLES20.glUniform4f(UniformColorTint.baseColorUnif, 0.5f, 0.5f, 0f, 1.0f);
        GLES20.glUseProgram(0);

        g_WhiteAmbDiffuseColor = loadProgram(VertexShaders.DirAmbVertexLighting_PN_vert,
                FragmentShaders.ColorPassthrough_frag);

        g_VertexDiffuseColor = loadProgram(VertexShaders.DirVertexLighting_PCN,
                FragmentShaders.ColorPassthrough_frag);

        g_Unlit = loadProgram(VertexShaders.unlit, FragmentShaders.unlit);

        g_litShaderProg = loadProgram(VertexShaders.BasicTexture_PN, FragmentShaders.ShaderGaussian);

        GLES20.glUseProgram(g_Unlit.theProgram);
        GLES20.glUniform4f(g_Unlit.baseColorUnif, 0.5f, 0.5f, 0f, 1.0f);
        Matrix4f test = Matrix4f.Identity();
        GLES20.glUniformMatrix4fv(g_Unlit.cameraToClipMatrixUnif, 1, false, test.toArray(), 0);
        GLES20.glUseProgram(0);

        // Test shader lights and materials
        GLES20.glUseProgram(g_litShaderProg.theProgram);
        g_litShaderProg.lightBlock = new LightBlock(NUMBER_OF_LIGHTS);
        g_litShaderProg.lightBlock.setUniforms(g_litShaderProg.theProgram);

        g_litShaderProg.lightBlock.ambientIntensity = new Vector4f(0.1f, 0.1f, 0.1f, 1.0f);

        g_litShaderProg.lightBlock.lights[0].cameraSpaceLightPos = new Vector4f(4.0f, 0.0f, 1.0f, 1.0f);
        g_litShaderProg.lightBlock.lights[0].lightIntensity = new Vector4f(0.7f, 0.0f, 0.0f, 1.0f);

        g_litShaderProg.lightBlock.lights[1].cameraSpaceLightPos = new Vector4f(4.0f, 0.0f, 1.0f, 1.0f);
        g_litShaderProg.lightBlock.lights[1].lightIntensity = new Vector4f(0.0f, 0.0f, 0.7f, 1.0f);

        g_litShaderProg.lightBlock.updateInternal();

        g_litShaderProg.materialBlock = new MaterialBlock(new Vector4f(0.0f, 0.3f, 0.0f, 1.0f),
                new Vector4f(0.5f, 0.0f, 0.5f, 1.0f), 0.6f);
        g_litShaderProg.materialBlock.setUniforms(g_litShaderProg.theProgram);
        g_litShaderProg.materialBlock.updateInternal();

        GLES20.glUseProgram(0);

        ObjectColor = loadProgram(VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorPassthrough_frag);
        currentProgram = ObjectColor;

        ballProgram = Programs.addProgram(VertexShaders.PosColorWorldTransform_vert, FragmentShaders.ColorPassthrough_frag);
    }
    static Mesh current_mesh;
    static Mesh g_unitSphereMesh;

    protected void init() throws Exception
    {
        frontWall = new PaintWall();
        backWall = new PaintWall();
        topWall = new PaintWall();
        bottomWall = new PaintWall();
        leftWall = new PaintWall();
        rightWall = new PaintWall();
        checkGlError("After Paintwall Setup");
        initializeProgram();
        checkGlError("After initializeProgram Setup");
        ball = new Ball(ballRadius);
        ball.setLimits(ballLimitLow, ballLimitHigh);
        ballSpeed = new Vector3f(
                ballSpeedFactor + ballSpeedFactor * (float)random.nextDouble(),
                ballSpeedFactor + ballSpeedFactor * (float)random.nextDouble(),
                ballSpeedFactor + ballSpeedFactor * (float)random.nextDouble());
        ball.setSpeed(ballSpeed);
        //ball.SetProgram(ballProgram);

        try
        {
            g_unitSphereMesh = new Mesh("unitsphere12.xml");

        } catch (Exception ex) {
            throw new Exception("Error " + ex.toString());
        }

        checkGlError("After Mesh");
        setupDepthAndCull();
        checkGlError("After setupDepthAndCull");
        Textures.enableTextures();
        checkGlError("After enableTextures");

        Camera.move(0f, 0f, 0f);
        Camera.moveTarget(0f, 0f, 0.0f);
        reshape();
        current_mesh = g_unitSphereMesh;
        if (oldWallMovement)
        {
            frontWall.move(0f, 0f, -0.05f);
            frontWall.rotateShape(Vector3f.UnitX, frontwallRotation);

            backWall.move(0f, 0f, -0.9f);
            backWall.scale(0.5f);


            leftWall.move(-wallMovement, 0f, 0f);
            leftWall.rotateShape(Vector3f.UnitY, wallAngle);
            rightWall.move(wallMovement, 0f, 0f);
            rightWall.rotateShape(Vector3f.UnitY, -wallAngle);

            topWall.move(0f, wallMovement, 0f);
            topWall.rotateShape(Vector3f.UnitX, wallAngle);
            bottomWall.move(0f, -wallMovement, 0f);
            bottomWall.rotateShape(Vector3f.UnitX, -wallAngle);
        }
        else
        {
        frontWall.move(0f, 0f, 1f);
        frontWall.scale(wallScale);

        backWall.move(0f, 0f, -1f);
        backWall.scale(wallScale);

        leftWall.move(-wallMovement, 0f, 0f);
        leftWall.scale(wallScale);
        leftWall.rotateShape(Vector3f.UnitY, wallAngle);
        rightWall.move(wallMovement, 0f, 0f);
        rightWall.scale(wallScale);
        rightWall.rotateShape(Vector3f.UnitY, -wallAngle);

        topWall.move(0f, wallMovement, 0f);
        topWall.scale(wallScale);
        topWall.rotateShape(Vector3f.UnitX, wallAngle);
        bottomWall.move(0f, -wallMovement, 0f);
        bottomWall.scale(wallScale);
        bottomWall.rotateShape(Vector3f.UnitX, -wallAngle);

        }
    }
    public void display() throws Exception
    {
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        clearDisplay();
        if (zeroCameraToClip)
        {
            Shape.cameraToClip = Matrix4f.Identity();
        }
        if (zeroWorldToCamera)
        {
            Shape.worldToCamera = Matrix4f.Identity();
        }
        if (drawWalls) backWall.draw();
        if (drawWalls) leftWall.draw();
        if (drawWalls) rightWall.draw();
        if (drawWalls) topWall.draw();
        if (drawWalls) bottomWall.draw();

        Shape.cameraToClip = cameraToClipMatrix;
        Shape.worldToCamera = worldToCameraMatrix;

        if(useBall) ball.draw();

        if (current_mesh != null)
        {
            MatrixStack modelMatrix = new MatrixStack();
            try (PushStack pushstack = new PushStack(modelMatrix))
            {
                modelMatrix.rotate(axis, angle);   // rotate last to leave in place
                modelMatrix.translate(position);
                modelMatrix.scale(15.0f, 15.0f, 15.0f);
                ballModelMatrix = modelMatrix.Top();

                GLES20.glUseProgram(currentProgram.theProgram);
                Matrix4f mm = modelMatrix.Top();

                if (noWorldMatrix)
                {
                    Matrix4f cm2 = Matrix4f.mul(mm, worldToCameraMatrix);
                    GLES20.glUniformMatrix4fv(currentProgram.modelToCameraMatrixUnif, 1, false, cm2.toArray(), 0);
                    if (currentProgram.normalModelToCameraMatrixUnif != 0)
                    {
                        Matrix3f normalModelToCameraMatrix = Matrix3f.Identity();
                        Matrix4f applyMatrix = Matrix4f.mul(Matrix4f.Identity(),
                                Matrix4f.createTranslation(dirToLight));
                        normalModelToCameraMatrix = new Matrix3f(applyMatrix);
                        // FIXME normalModelToCameraMatrix.invert();
                        GLES20.glUniformMatrix3fv(currentProgram.normalModelToCameraMatrixUnif, 1, false,
                                normalModelToCameraMatrix.toArray(), 0);
                        //Matrix4f cameraToClipMatrix = Matrix4f.Identity;
                        //GLES20.glUniformMatrix4f(currentProgram.cameraToClipMatrixUnif, false, cameraToClipMatrix);

                    }
                    //Matrix4f cameraToClipMatrix = Matrix4f.Identity;
                    //GLES20.glUniformMatrix4f(currentProgram.cameraToClipMatrixUnif, false, cameraToClipMatrix);
                }
                else
                {
                    GLES20.glUniformMatrix4fv(currentProgram.modelToWorldMatrixUnif, 1, false, mm.toArray(), 0);
                }
            }
            if (renderWithString)
            {
                current_mesh.render(renderString);
            }
            else
            {
                current_mesh.render();
            }
            GLES20.glUseProgram(0);
            if (perspectiveAngle != newPerspectiveAngle)
            {
                perspectiveAngle = newPerspectiveAngle;
                reshape();
            }
        }
        if (zeroCameraToClip)
        {
            Shape.cameraToClip = Matrix4f.Identity();
        }
        if (zeroWorldToCamera)
        {
            Shape.worldToCamera = Matrix4f.Identity();
        }
        if (drawWalls) frontWall.draw();
        Shape.cameraToClip = cameraToClipMatrix;
        Shape.worldToCamera = worldToCameraMatrix;
        if (pause == false)
        {
            UpdatePosition();
            if (rotateWorld)
            {
                RotateWorldSub();
            }
        }
        updateDisplayOptions();
    }

    private void RotateWorldSub()
    {
        Matrix4f rotX = Matrix4f.CreateRotationX(0.05f * (float)random.nextDouble());
        Matrix4f rotY = Matrix4f.CreateRotationY(0.05f * (float)random.nextDouble());
        Matrix4f rotZ = Matrix4f.CreateRotationZ(0.05f * (float)random.nextDouble());
        if (reverseCamRot) {
            worldToCameraMatrix = Matrix4f.mul(worldToCameraMatrix, rotX);
            worldToCameraMatrix = Matrix4f.mul(worldToCameraMatrix, rotY);
            worldToCameraMatrix = Matrix4f.mul(worldToCameraMatrix, rotZ);
        }
        else {
            worldToCameraMatrix = Matrix4f.mul(rotX, worldToCameraMatrix);
            worldToCameraMatrix = Matrix4f.mul(rotY, worldToCameraMatrix);
            worldToCameraMatrix = Matrix4f.mul(rotZ, worldToCameraMatrix);
        }
        SetGlobalMatrices(currentProgram);
    }

    private void ChangePlayerView()
    {
        MatrixStack camMatrix = new MatrixStack();
        camMatrix.SetMatrix(Camera.GetLookAtMatrix());
        worldToCameraMatrix = camMatrix.Top();
        Matrix4f rotX = Matrix4f.CreateRotationX(playerRotations[playerNumber].x * (float)Math.PI / 180f);
        Matrix4f rotY = Matrix4f.CreateRotationY(playerRotations[playerNumber].y * (float)Math.PI / 180f);
        Matrix4f rotZ = Matrix4f.CreateRotationZ(playerRotations[playerNumber].z * (float)Math.PI / 180f);
        if (reverseCamRot) {
            worldToCameraMatrix = Matrix4f.mul(worldToCameraMatrix, rotX);
            worldToCameraMatrix = Matrix4f.mul(worldToCameraMatrix, rotY);
            worldToCameraMatrix = Matrix4f.mul(worldToCameraMatrix, rotZ);
        }
        else {
            worldToCameraMatrix = Matrix4f.mul(rotX, worldToCameraMatrix);
            worldToCameraMatrix = Matrix4f.mul(rotY, worldToCameraMatrix);
            worldToCameraMatrix = Matrix4f.mul(rotZ, worldToCameraMatrix);
        }
         SetGlobalMatrices(currentProgram);
    }

    private void UpdatePosition()
    {
        if (ballModelMatrix.M41 < positionLimitLow.x)
        {
            leftWall.Paint(ballModelMatrix.M43/positionLimitHigh.x, ballModelMatrix.M42/positionLimitHigh.y);
            if (velocity.x < 0) velocity.x *= -1;
        }
        if (ballModelMatrix.M41 > positionLimitHigh.x)
        {
            rightWall.Paint(ballModelMatrix.M43/positionLimitHigh.x, ballModelMatrix.M42/positionLimitHigh.y);
            if (velocity.x > 0) velocity.x *= -1;
        }
        if (ballModelMatrix.M42 < positionLimitLow.y)
        {
            bottomWall.Paint(ballModelMatrix.M41/positionLimitHigh.x, ballModelMatrix.M43/positionLimitHigh.y);
            if (velocity.y < 0) velocity.y *= -1;
        }
        if (ballModelMatrix.M42 > positionLimitHigh.y)
        {
            topWall.Paint(ballModelMatrix.M41/positionLimitHigh.x, ballModelMatrix.M43/positionLimitHigh.y);
            if (velocity.y > 0) velocity.y *= -1;
        }
        if (ballModelMatrix.M43 < positionLimitLow.z)
        {
            backWall.Paint(ballModelMatrix.M41/positionLimitHigh.x, ballModelMatrix.M42/positionLimitHigh.y);
            if (velocity.z < 0) velocity.z *= -1;
        }
        if (ballModelMatrix.M43 > positionLimitHigh.z)
        {
            frontWall.Paint(ballModelMatrix.M41/positionLimitHigh.x, ballModelMatrix.M42/positionLimitHigh.y);
            if (velocity.z > 0) velocity.z *= -1;
        }
        position = position.add(velocity);
    }

    static Vector3f axis = new Vector3f(1f, 1f, 0);
    static float angle = 0;

    static private void SetGlobalMatrices(ProgramData program)
    {
        Shape.worldToCamera = worldToCameraMatrix;
        Shape.cameraToClip = cameraToClipMatrix;

        GLES20.glUseProgram(program.theProgram);
        GLES20.glUniformMatrix4fv(program.cameraToClipMatrixUnif, 1, false, cameraToClipMatrix.toArray(), 0);  // this one is first
        GLES20.glUniformMatrix4fv(program.worldToCameraMatrixUnif, 1, false, worldToCameraMatrix.toArray(), 0); // this is the second one
        GLES20.glUseProgram(0);
    }

    public void reshape()
    {
        MatrixStack camMatrix = new MatrixStack();
        camMatrix.SetMatrix(Camera.GetLookAtMatrix());

        worldToCameraMatrix = camMatrix.Top();

        MatrixStack persMatrix = new MatrixStack();
        persMatrix.perspective(perspectiveAngle, (width / (float) height), g_fzNear, g_fzFar);
        cameraToClipMatrix = persMatrix.Top();
        //ChangePlayerView();
        SetGlobalMatrices(currentProgram);

        GLES20.glViewport(0, 0, width, height);

    }

    static boolean noWorldMatrix = false;

    float xTotal = -limit;
    float yTotal = -limit;
    float zTotal = -limit;

    void moveWallsSub(float x, float y, float z)
    {
        frontWall.move(x, y, z);
        backWall.move(x, y, z);
        leftWall.move(x, y, z);
        rightWall.move(x, y, z);
        topWall.move(x, y, z);
        bottomWall.move(x, y, z);
    }

    void moveWalls(float x, float y, float z) {
        moveWallsSub(x, y, z);
        xTotal += x;
        yTotal += y;
        zTotal += z;

        if (xTotal >= limit)
        {
            xTotal = -limit;
            moveWallsSub(-2 * limit, 0f, 0f);
        }

        if (yTotal >= limit)
        {
            yTotal = -limit;
            moveWallsSub(0f, -2 * limit, 0f);
        }

        if (zTotal >= limit)
        {
            zTotal = -limit;
            moveWallsSub(0f, 0f, -2 * limit);
        }
    }

    public String keyboard(int keyCode, int x, int y) {
        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(keyCode));
        if (displayOptions)
        {
            setDisplayOptions(keyCode);
        }
        else
        {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    displayOptions = true;
                    break;
                case KeyEvent.KEYCODE_NUMPAD_6:
                    Camera.moveTarget(0.5f, 0f, 0.0f);
                    Log.i("KeyEvent", Camera.GetTargetString());
                    break;
                case KeyEvent.KEYCODE_NUMPAD_4:
                    Camera.moveTarget(-0.5f, 0f, 0.0f);
                    Log.i("KeyEvent", Camera.GetTargetString());
                    break;
                case KeyEvent.KEYCODE_NUMPAD_8:
                    Camera.moveTarget(0.0f, 0.5f, 0.0f);
                    Log.i("KeyEvent", Camera.GetTargetString());
                    break;
                case KeyEvent.KEYCODE_NUMPAD_2:
                    Camera.moveTarget(0f, -0.5f, 0.0f);
                    Log.i("KeyEvent", Camera.GetTargetString());
                    break;
                case KeyEvent.KEYCODE_NUMPAD_7:
                    Camera.moveTarget(0.0f, 0.0f, 0.5f);
                    Log.i("KeyEvent", Camera.GetTargetString());
                    break;
                case KeyEvent.KEYCODE_NUMPAD_3:
                    Camera.moveTarget(0f, 0.0f, -0.5f);
                    Log.i("KeyEvent", Camera.GetTargetString());
                    break;
                case KeyEvent.KEYCODE_1:
                    axis = Vector3f.UnitX;
                    angle = angle + 1;
                    break;
                case KeyEvent.KEYCODE_2:
                    axis = Vector3f.UnitY;
                    angle = angle + 1;
                    break;
                case KeyEvent.KEYCODE_3:
                    axis = Vector3f.UnitZ;
                    angle = angle + 1;
                    break;
                case KeyEvent.KEYCODE_4:
                    break;
                case KeyEvent.KEYCODE_7:
                    if (zeroCameraToClip)
                    {
                        zeroCameraToClip = false;
                    }
                    else
                    {
                        zeroCameraToClip = true;
                    }
                    break;
                case KeyEvent.KEYCODE_8:
                    if (zeroWorldToCamera)
                    {
                        frontWall.rotateShape(Vector3f.UnitX, 180f);
                        wallMovementStep = 0.1f;
                        limit = 1f;
                        zeroWorldToCamera = false;
                    }
                    else
                    {
                        frontWall.rotateShape(Vector3f.UnitX, 180f);
                        wallMovementStep = 0.1f;
                        limit = 1f;
                        zeroWorldToCamera = true;
                    }
                    break;
                case KeyEvent.KEYCODE_9:
                    wallAngle -= 10f;
                    break;
                case KeyEvent.KEYCODE_0:
                    wallMovement -= 0.1f;
                    break;
                case KeyEvent.KEYCODE_R:
                    if (rotateWorld) {
                        rotateWorld = false;
                        result.append("rotateWorld disabled");
                    } else {
                        rotateWorld = true;
                        result.append("rotateWorld enabled");
                    }
                    break;
                case KeyEvent.KEYCODE_ESCAPE:
                    //timer.Enabled = false;
                    break;
                case KeyEvent.KEYCODE_SPACE:
                    newPerspectiveAngle = perspectiveAngle + 5f;
                    if (newPerspectiveAngle > 120f) {
                        newPerspectiveAngle = 30f;
                    }
                    break;
                case KeyEvent.KEYCODE_Z:
                   moveWalls(0f, 0f, wallMovementStep);
                    break;
                case KeyEvent.KEYCODE_X:
                    moveWalls(wallMovementStep, 0f, 0f);
                    break;
                case KeyEvent.KEYCODE_Y:
                    moveWalls(0f, wallMovementStep, 0f);
                    break;
                case KeyEvent.KEYCODE_Q:
                    Log.i("KeyEvent", "currentProgram = " + currentProgram.toString());
                    break;
                case KeyEvent.KEYCODE_P:
                    if (pause) {
                        pause = false;
                    } else {
                        pause = true;
                    }
                    break;
                case KeyEvent.KEYCODE_V:
                    playerNumber++;
                    if (playerNumber > lastPlayer) {
                        playerNumber = 0;
                    }
                    ChangePlayerView();
                    result.append("Player number = " + String.valueOf(playerNumber));
                    break;
                case KeyEvent.KEYCODE_I:
                    if (info)
                    {
                        info = false;
                    }
                    else
                    {
                        info = true;
                    }
                    result.append("cameraToClipMatrix " + cameraToClipMatrix.toString());
                    result.append(AnalysisTools.CalculateMatrixEffects(cameraToClipMatrix));
                    result.append("worldToCameraMatrix " + worldToCameraMatrix.toString());
                    result.append(AnalysisTools.CalculateMatrixEffects(worldToCameraMatrix));

                    Matrix4f cameraToClipMatrixTimesWorldToCameraMatrix =
                            Matrix4f.mul(cameraToClipMatrix, worldToCameraMatrix);
                    result.append("cameraToClipMatrixTimesWorldToCameraMatrix " +
                            cameraToClipMatrixTimesWorldToCameraMatrix.toString());
                    result.append(AnalysisTools.CalculateMatrixEffects(cameraToClipMatrixTimesWorldToCameraMatrix));

                    Matrix4f worldToCameraMatrixTimesCameraToClipMatrix =
                            Matrix4f.mul(worldToCameraMatrix, cameraToClipMatrix);
                    result.append("worldToCameraMatrixTimesCameraToClipMatrix " +
                            worldToCameraMatrixTimesCameraToClipMatrix.toString());
                    result.append(AnalysisTools.CalculateMatrixEffects(worldToCameraMatrixTimesCameraToClipMatrix));
                    break;
                case KeyEvent.KEYCODE_S:
                    ball.setSocketControl();
                    result.append("Socket Control");
                    break;
                case KeyEvent.KEYCODE_E:
                    ball.setElasticControl();
                    result.append("Socket Control");
                    break;
                case KeyEvent.KEYCODE_W:
                    if (drawWalls) {
                        drawWalls = false;
                        Log.i("KeyEvent", "drawWalls = false");
                    }
                    else {
                        drawWalls = true;
                        Log.i("KeyEvent", "drawWalls = true");
                    }
                    break;
                case KeyEvent.KEYCODE_O:
                    if (oldWallMovement) {
                        oldWallMovement = false;
                        Log.i("KeyEvent", "newWallMovement");
                    }
                    else
                    {
                        oldWallMovement = true;
                        Log.i("KeyEvent", "oldWallMovement");
                    }
                    break;
            }
        }
        return result.toString();
    }

}
