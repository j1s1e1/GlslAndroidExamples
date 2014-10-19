package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.SingleMeshItem;
import com.tutorial.glsltutorials.tutorials.TestRenderer;
import com.tutorial.glsltutorials.tutorials.TestRenderer30;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * Created by Jamie on 5/26/14.
 */
public class Tutorials extends Activity implements AdapterView.OnItemSelectedListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials);

        Spinner spinner = (Spinner) findViewById(R.id.Tutorials);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,
                R.array.Tutorials, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        int version = 2;
        switch (pos)
        {
            case 1: TestRenderer.tutorial = new  Tut_02_Vertex_Colors(); break;
            case 2: TestRenderer.tutorial = new Tut_Triangles(); break;
            case 3: TestRenderer.tutorial = new Tut_03_CPU_Position_Offset(); break;
            case 5: TestRenderer.tutorial = new Tut_03_Shader_Calc_Offset(); break;
            case 7: TestRenderer.tutorial = new Tut_04_MatrixPerspective(); break;
            case 9: TestRenderer.tutorial = new Tut_05_Depth_Buffering(); break;
            case 12: TestRenderer.tutorial = new Tut_06_Rotations(); break;
            case 13: TestRenderer.tutorial = new Tut_06_Translation(); break;
            case 14: TestRenderer.tutorial = new  Tut_06_Scale(); break;
            case 15: TestRenderer.tutorial = new  Tut_07_World_Scene(); break;
            case 18: TestRenderer.tutorial = new  Tut_08_Gimbal_Lock(); break;
            case 20: TestRenderer.tutorial = new  Tut_09_Ambient_Lighting(); break;

            case 23: TestRenderer.tutorial = new Tut_Text(); break;
            case 24: TestRenderer.tutorial = new Tut_Spheres(); break;
            case 25: TestRenderer.tutorial = new Tut_Blender(); break;
            case 26: TestRenderer.tutorial = new  Tut_Camera(); break;
            case 27: TestRenderer.tutorial = new  Tut_Blocks(); break;
            case 28: TestRenderer.tutorial = new Tut_Vectors(); break;
            case 29: TestRenderer.tutorial = new Tut_3D_Shooter(); break;
            case 30: TestRenderer.tutorial = new Tut_3D_Shooter2(); break;
            case 31: TestRenderer.tutorial = new SingleMeshItem(); break;
            default: return;
        }
        SetupOpenGL(version);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private static GLSurfaceView mGLView;
    TestRenderer Renderer;
    TestRenderer30 Renderer30;

    private static double glVersion = 3.0;

    private static class ContextFactory implements GLSurfaceView.EGLContextFactory {

        private static int EGL_CONTEXT_CLIENT_VERSION = 0x3098;

        public EGLContext createContext(
                EGL10 egl, EGLDisplay display, EGLConfig eglConfig) {
            int[] attrib_list = {EGL_CONTEXT_CLIENT_VERSION, (int) glVersion,
                    EGL10.EGL_NONE };
            // attempt to create a OpenGL ES 3.0 context
            EGLContext context = egl.eglCreateContext(
                    display, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);
            return context; // returns null if 3.0 is not supported;
        }

        public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context)
        {

        }


    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public StringBuilder SetupOpenGL(int version) {
        StringBuilder messages = new StringBuilder();

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity

        mGLView = new GLSurfaceView(this);

        /*
        ContextFactory cf = new ContextFactory();
        final EGL10 egl = (EGL10) EGLContext.getEGL();
        final EGLDisplay eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        EGL10 mEgl = (EGL10) EGLContext.getEGL();
        int[] configsCount = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        Boolean getconfigs = mEgl.eglChooseConfig(eglDisplay, sEglConfig, configs, 1, configsCount);

        EGLContext test = cf.createContext(egl, eglDisplay, configs[0]);
        */
        ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        final boolean supportsEs3 = configurationInfo.reqGlEsVersion >= 0x30000;

        if (supportsEs2) {
            mGLView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
            // Request an OpenGL ES 2.0 compatible context.
            mGLView.setEGLContextClientVersion(version);
            String result = GLES20.glGetString(GLES20.GL_VERSION);
            if (version == 3)
            {
                if (supportsEs3) {
                    Renderer30 = new TestRenderer30();
                    mGLView.setRenderer(Renderer30);
                }
                else
                {
                    return messages;
                }
            }
            else {
                Renderer = new TestRenderer();
                // Set the renderer to our demo renderer, defined below.
                mGLView.setRenderer(Renderer);
            }
        } else {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return messages;
        }
        setContentView(mGLView);

        messages.append("Setup Complete");
        return messages;
    }

    static final int[] sEglConfig = {
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_ALPHA_SIZE, 0,
            EGL10.EGL_DEPTH_SIZE, 0,
            EGL10.EGL_STENCIL_SIZE, 0,
            EGL10.EGL_NONE
    };

    static int x_position = 0;
    static int y_position = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        x_position = (int)event.getX();
        y_position = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            {
                if (TestRenderer.tutorial != null) {
                    try {
                        TestRenderer.tutorial.TouchEvent(x_position, y_position);
                    }
                    catch (Exception ex)
                    {

                    }
                }
                break;
            }
        }


        return false;
    }
}
