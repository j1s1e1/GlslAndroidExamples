package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VBO_Tools;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;
import com.tutorial.glsltutorials.tutorials.TestRenderer;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Timer;

/**
 * Created by Jamie on 5/26/14.
 */
public abstract class TutorialBase {

    boolean tutorialLog = true;
    boolean info = false;

    protected static final int positionAttributeLocation = 0;
    protected static final int colorAttributeLocation = 1;
    protected static final int normalAttributeLocation = 2;

    protected static final int BYTES_PER_FLOAT = 4;
    protected static final int BYTES_PER_SHORT = 2;

    protected int COORDS_PER_VERTEX = 4;
    protected int POSITION_DATA_SIZE_IN_ELEMENTS = 4;
    protected int COLOR_DATA_SIZE_IN_ELEMENTS = 4;
    protected int POSITION_STRIDE = POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;
    protected int COLOR_STRIDE = COLOR_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;
    protected static int TEXTURE_DATA_SIZE_IN_ELEMENTS = 2;
    protected static int TEXTURE_STRIDE = TEXTURE_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;

    static long startTime;

    static Timer timer;

    // For single program tutorials
    protected int 		theProgram;
    protected int 		positionAttribute;
    protected int 		colorAttribute;
    protected int  		modelToCameraMatrixUnif;
    protected int		cameraToClipMatrixUnif;
    protected int  		baseColorUnif;
    protected static Matrix4f cameraToClipMatrix = new Matrix4f();
    protected static Matrix4f worldToCameraMatrix = new Matrix4f();


    protected ShortBuffer elementSB;
    protected FloatBuffer vertexDataFB;

    protected int[] vertexBufferObject = new int[1];
    protected int[] indexBufferObject = new int[1];

    public static int width = 512;
    public static int height = 512;
    protected static float fFrustumScale;

    public static Vector4f clearColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

    public TutorialBase()
    {
    }

    protected void checkGlError(String TAG) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, ": glError " + error);
        }
    }

    public StringBuilder setup()
    {
        StringBuilder messages = new StringBuilder();
        startTime = System.currentTimeMillis();
        try {
            Log.i("Setup", "Start Initialization");
            TestRenderer.initComplete = false;
            checkGlError("Before Init");
            init();
            checkGlError("Init Error");
            TestRenderer.initComplete = true;
            Log.i("Setup", "Initialization Complete");
        }
        catch (Exception ex)
        {
            Log.e("Setup ",  ex.getMessage());
        }
        reshape();
        timer = new Timer(false);
        messages.append("Tutorial Setup Complete");
        return messages;
    }

    public static float getElapsedTime()
    {
        return System.currentTimeMillis() - startTime;
    }

    void initializeVertexBuffer(float[] vertexData)
    {
        vertexDataFB = VBO_Tools.InitializeVertexBuffer(vertexData);
        GLES20.glGenBuffers(1, vertexBufferObject, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexDataFB.capacity()
                * BYTES_PER_FLOAT, vertexDataFB, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    void initializeVertexBuffer(float[] vertexData, short[] indexData)
    {
        vertexDataFB = VBO_Tools.InitializeVertexBuffer(vertexData);
        elementSB = VBO_Tools.InitializeElementBuffer(indexData);
        GLES20.glGenBuffers(1, vertexBufferObject, 0);
        GLES20.glGenBuffers(1, indexBufferObject, 0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBufferObject[0]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, elementSB.capacity()
                * BYTES_PER_SHORT, elementSB, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexDataFB.capacity()
                * BYTES_PER_FLOAT, vertexDataFB, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    protected void init() throws Exception
    {
    }

    public void reshape()
    {
        GLES20.glViewport(0, 0, width, height);
    }

    public void display() throws Exception
    {

    }

    public String keyboard(int key, int x, int y) throws Exception
    {
        switch (key) {
            case KeyEvent.KEYCODE_ESCAPE:
                timer.cancel();
                break;
        }
        return "Default keyboard hanlder, only escape enabled.";
    }

    public void setScale(float scale) {
    }

    public void scroll(float distanceX, float distanceY)
    {
    }

    public void onLongPress(MotionEvent event)
    {}

    protected static int defaults(int displayMode, int width, int height)
    {
        return displayMode;
    }

    protected static float degToRad(float fAngDeg)
    {
        float fDegToRad = 3.14159f * 2.0f / 360.0f;
        return fAngDeg * fDegToRad;
    }

    protected static float calcFrustumScale(float fFovDeg)
    {
        float degToRad = 3.14159f * 2.0f / 360.0f;
        float fFovRad = fFovDeg * degToRad;
        return 1.0f / (float)Math.tan(fFovRad / 2.0f);
    }

    protected static float mix(float in1, float in2, float mix_factor)
    {
        return in1 * 1 - mix_factor + in2 * mix_factor;
    }

    public static void quickToast(String data)
    {
        final Toast toast = Toast.makeText(Shader.context,data, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {@Override public void run() {toast.cancel();}},200);
    }

    public void touchEvent(int x_position, int y_position) throws Exception
    {

    }

    public void receiveMessage(String message)
    {

    }

    protected void setupDepthAndCull()
    {
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glFrontFace(GLES20.GL_CW);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthMask(true);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthRangef(0.0f, 1.0f);
    }

    public static void setBackgroundColor(Vector4f color)
    {
        clearColor = color;
    }

    protected void clearDisplay()
    {
        GLES20.glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
        GLES20.glClearDepthf(1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT |  GLES20.GL_DEPTH_BUFFER_BIT);
    }

    boolean displayOptions = false;
    boolean updateCull = false;
    boolean updateDepth = false;
    boolean updateDepthMask = false;
    boolean updateAlpha = false;
    boolean updateCcw = false;
    boolean updateBlend = false;
    boolean blend = false;
    boolean ccw = false;
    boolean updateCullFace = false;
    int cullFaceSelection = 0;
    boolean cull = true;
    boolean depth = true;
    boolean depthMask = true;
    boolean alpha = false;

    protected float g_fzNear = 1.0f;
    protected float g_fzFar = 10f;
    boolean callReshape = false;

    void logDisplayState()
    {
        Log.i("Display State", "alpha " +  String.valueOf(alpha));
        Log.i("Display State", "cull " +  String.valueOf(cull));
        Log.i("Display State", "cullFaceSelection " +  String.valueOf(cullFaceSelection));
        Log.i("Display State", "depth " +  String.valueOf(depth));
        Log.i("Display State", "blend " +  String.valueOf(blend));
    }

    void setDisplayOptions(int keyCode)
    {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                displayOptions = false;
                break;
            case KeyEvent.KEYCODE_A:
                updateAlpha = true;
                break;
            case KeyEvent.KEYCODE_C:
                updateCull = true;
                break;
            case KeyEvent.KEYCODE_D:
                updateDepth = true;
                break;
            case KeyEvent.KEYCODE_M:
                updateDepthMask = true;
                break;
            case KeyEvent.KEYCODE_1:
                g_fzNear = 1f;
                g_fzFar = 10;
                callReshape = true;
                break;
            case KeyEvent.KEYCODE_2:
                g_fzNear = 1f;
                g_fzFar = 100;
                callReshape = true;
                break;
            case KeyEvent.KEYCODE_3:
                g_fzNear = 10f;
                g_fzFar = 100;
                callReshape = true;
                break;
            case KeyEvent.KEYCODE_4:
                g_fzNear = 10f;
                g_fzFar = 1000;
                callReshape = true;
                break;
            case KeyEvent.KEYCODE_5:
                g_fzNear = 0.1f;
                g_fzFar = 2f;
                callReshape = true;
                break;
            case KeyEvent.KEYCODE_6:
                g_fzNear = 0.1f;
                g_fzFar = 100f;
                callReshape = true;
                break;
            case KeyEvent.KEYCODE_7:
                g_fzNear = 0.1f;
                g_fzFar = 10f;
                callReshape = true;
                break;
        }
    }

    void updateDisplayOptions() {
        if (updateAlpha) {
            updateAlpha = false;
            if (alpha) {
                alpha = false;
                GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
                GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
                Log.i("KeyEvent", "alpha disabled");
            } else {
                alpha = true;
                GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
                GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                Log.i("KeyEvent", "alpha enabled");
            }
        }
        if (updateBlend) {
            updateBlend = false;
            if (blend) {
                blend = false;
                GLES20.glDisable(GLES20.GL_BLEND);
                Log.i("KeyEvent", "blend disabled");
            } else {
                blend = true;
                GLES20.glEnable(GLES20.GL_BLEND);
                Log.i("KeyEvent", "blend enabled");
            }
        }
        if (updateCull) {
            updateCull = false;
            if (cull) {
                cull = false;
                GLES20.glDisable(GLES20.GL_CULL_FACE);
                Log.i("KeyEvent", "cull disabled");
            } else {
                cull = true;
                GLES20.glEnable(GLES20.GL_CULL_FACE);
                Log.i("KeyEvent", "cull enabled");
            }
        }
        if (updateDepth)
        {
            updateDepth = false;
            if (depth)
            {
                depth = false;
                GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                GLES20.glDepthMask(false);
                Log.i("KeyEvent", "depth disabled");
            }
            else
            {
                depth = true;
                GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                GLES20.glDepthMask(true);
                Log.i("KeyEvent", "depth enabled");
            }
        }
        if (updateCullFace)
        {
            updateCullFace = false;
            cullFaceSelection++;
            if (cullFaceSelection > 2) cullFaceSelection = 0;
            switch (cullFaceSelection) {
                case 0:
                    GLES20.glCullFace(GLES20.GL_FRONT_AND_BACK);
                    Log.i("KeyEvent", "cull face GL_FRONT_AND_BACK");
                    break;
                case 1:
                    GLES20.glCullFace(GLES20.GL_FRONT);
                    Log.i("KeyEvent", "cull face GL_FRONT");
                    break;
                case 2:
                    GLES20.glCullFace(GLES20.GL_BACK);
                    Log.i("KeyEvent", "cull face GL_BACK");
                    break;
            }
        }
        if (callReshape)
        {
            callReshape = false;
            reshape();
        }
        if (info) logDisplayState();
    }
}
