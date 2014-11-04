package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VBO_Tools;
import com.tutorial.glsltutorials.tutorials.Geometry.Matrix4f;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector4f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jamie on 5/26/14.
 */
public abstract class TutorialBase {

    boolean tutorialLog = true;

    protected int COORDS_PER_VERTEX = 4;
    protected int POSITION_DATA_SIZE_IN_ELEMENTS = 4;
    protected int COLOR_DATA_SIZE_IN_ELEMENTS = 4;
    protected int POSITION_STRIDE = POSITION_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;
    protected int COLOR_STRIDE = COLOR_DATA_SIZE_IN_ELEMENTS * BYTES_PER_FLOAT;

    static long startTime;

    static Timer timer;

    // For single program tutorials
    protected int 		theProgram;
    protected int 		positionAttribute;
    protected int 		colorAttribute;
    protected int  		modelToCameraMatrixUnif;
    protected int		cameraToClipMatrixUnif;
    protected int  		baseColorUnif;
    protected Matrix4f cameraToClipMatrix = new Matrix4f();

    protected ShortBuffer elementSB;
    protected FloatBuffer vertexDataFB;

    protected int[] vertexBufferObject = new int[1];
    protected int[] indexBufferObject = new int[1];

    protected static final int BYTES_PER_FLOAT = 4;
    protected static final int BYTES_PER_SHORT = 2;

    public int width = 512;
    public int height = 512;
    protected static float fFrustumScale;

    public static Vector4f clearColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

    public TutorialBase()
    {
    }

    public StringBuilder setup()
    {
        StringBuilder messages = new StringBuilder();
        startTime = System.currentTimeMillis();
        try {
            init();
        }
        catch (Exception ex)
        {
            int debug = 0;
            debug++;
        }
        reshape();
        timer = new Timer(false);
        messages.append("Tutorial Setup Complete");
        return messages;
    }

    protected static float getElapsedTime()
    {
        return System.currentTimeMillis() - startTime;
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
        GLES20.glEnable(GLES20.GL_CLAMP_TO_EDGE);
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
}
