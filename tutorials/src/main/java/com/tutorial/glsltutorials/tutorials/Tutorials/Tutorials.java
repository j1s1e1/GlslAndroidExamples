package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.MainActivity;
import com.tutorial.glsltutorials.tutorials.R;
import com.tutorial.glsltutorials.tutorials.TestRenderer;
import com.tutorial.glsltutorials.tutorials.TestRenderer30;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * Created by Jamie on 5/26/14.
 */
public class Tutorials extends Activity implements
        GestureDetector.OnGestureListener {

    private GestureDetector mDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials);
        ListView listview = (ListView) findViewById(R.id.listView);

        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,
                R.array.Tutorials, android.R.layout.simple_list_item_1);
        listview.setAdapter(adapter);
        listview.setFocusableInTouchMode(true);
        listview.setOnItemClickListener(
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView adapterView, View view,
                                        int pos, long arg3) {
                    chooseTutorial(pos);
                }
            });

        listview.setOnCreateContextMenuListener(
            new View.OnCreateContextMenuListener() {

                public void onCreateContextMenu(ContextMenu menu, View view,
                                                ContextMenu.ContextMenuInfo menuInfo)
                {
                }

            });

        mDetector = new GestureDetector(this,this);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    static boolean firstlaunch = true;

    public void chooseTutorial(int pos) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        int version = 2;
        switch (pos)
        {
            case 0:
                MainActivity.requestExit = true;
                final Toast toast = Toast.makeText(Shader.context, "Android hates when we close our apps..." +
                                "Please press back a few times to release all resources.",
                        Toast.LENGTH_SHORT);
                toast.show();
                this.finish();
                super.onBackPressed();
                break;
            case 1: TestRenderer.tutorial = new  Tut_3D_Shooter3(); break;
            case 2: TestRenderer.tutorial = new  Tut_02_Vertex_Colors(); break;
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
            case 29: TestRenderer.tutorial = new Tut_MultipleShaders(); break;
            case 30: TestRenderer.tutorial = new Tut_3D_Shooter(); break;
            case 31:
                final Toast toast1 = Toast.makeText(Shader.context,"Slow Loading ....", Toast.LENGTH_SHORT);
                toast1.show();
                TestRenderer.tutorial = new Tut_3D_Shooter2();
                break;
            case 32: TestRenderer.tutorial = new Tut_Triangles(); break;
            case 33: TestRenderer.tutorial = new Tut_SingleMeshItem(); break;
            default:
                final Toast toast2 = Toast.makeText(Shader.context,"Not implemented", Toast.LENGTH_SHORT);
                toast2.show();
                return;
        }
        if (pos != 0)
        {
            paused = false;
            SetupOpenGL(version);
        }
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

        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        final boolean supportsEs3 = configurationInfo.reqGlEsVersion >= 0x30000;

        if (supportsEs2) {
            mGLView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
            // Request an OpenGL ES 2.0 compatible context.
            mGLView.setEGLContextClientVersion(version);
            String result = GLES20.glGetString(GLES20.GL_VERSION);
            if (version == 3) {
                if (supportsEs3) {
                    Renderer30 = new TestRenderer30();
                    mGLView.setRenderer(Renderer30);
                } else {
                    return messages;
                }
            } else {
                Renderer = new TestRenderer();
                // Set the renderer to our demo renderer, defined below.
                mGLView.setRenderer(Renderer);
                Renderer.EnableGlesDisplay();
            }
        } else {
            // This is where you could create an OpenGL ES 1.x compatible
            // renderer if you wanted to support both ES 1 and ES 2.
            return messages;
        }
        setContentView(mGLView);

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
        this.mDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
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

    static boolean paused = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (TestRenderer.tutorial != null) {
            try {
                TestRenderer.tutorial.keyboard(keyCode, 0, 0);
            } catch (Exception ex) {

            }}

        return super.onKeyDown(keyCode, event);
    }




    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            if (TestRenderer.tutorial != null) {
                try {
                    TestRenderer.tutorial.SetScale(scaleFactor);
                } catch (Exception ex) {

                }}
            return true;
        }
    }

    public void onBackPressed(){
        if (paused)
        {
            paused = false;
            // wait for any more intents
            android.os.SystemClock.sleep(1000);
            super.onBackPressed();
        }
        else {
            Renderer.DisableGlesDisplay();
            paused = true;
            Intent startIntent = new Intent(Shader.context, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Shader.context.startActivity(startIntent);
        }
    }
}
