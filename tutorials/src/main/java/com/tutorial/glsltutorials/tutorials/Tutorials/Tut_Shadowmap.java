package com.tutorial.glsltutorials.tutorials.Tutorials;

import android.opengl.GLES20;

import com.tutorial.glsltutorials.tutorials.GLES_Helpers.FragmentShaders;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.Shader;
import com.tutorial.glsltutorials.tutorials.GLES_Helpers.VertexShaders;

/**
 * Created by jamie on 6/20/15.
 */
public class Tut_Shadowmap extends TutorialBase {
    int shadowMapUniform;

    float RENDER_WIDTH = 512.0f;
    float RENDER_HEIGHT = 512.0f;
    float SHADOW_MAP_RATIO = 2f;


    //Camera position
    float[] p_camera = new float[] {32,20,0};

    //Camera lookAt
    float[] l_camera = new float[]{2,0,-10};

    //Light position
    float[] p_light = new float[]{3,20,0};

    //Light lookAt
    float[] l_light = new float[]{0,0,-5};


    //Light mouvement circle radius
    float light_mvnt = 30.0f;

    // Hold id of the framebuffer for light POV rendering
    int fboId;

    // Z values will be rendered to this texture when using fboId framebuffer
    int depthTextureId;
    int depthBufferId;


    void InitializeProgram()
    {
        int vertex_shader = Shader.compileShader(GLES20.GL_VERTEX_SHADER, VertexShaders.ShadowMap);
        int fragment_shader = Shader.compileShader(GLES20.GL_FRAGMENT_SHADER, FragmentShaders.ShadowMap);
        theProgram = Shader.createAndLinkProgram(vertex_shader, fragment_shader);
        shadowMapUniform = GLES20.glGetUniformLocation(theProgram, "ShadowMap");
    }

    protected void init()
    {
        //glutSetWindowTitle("Tut_ShadowMap");
        //glViewport(0, 0, (GLsizei) 512, (GLsizei) 512);
        InitializeProgram();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // test2
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        //GLES20.glEnable(EnableCap.Blend);

        // TEST
//			GLES20.glClearStencil(0);
//			GLES20.glStencilMask(1);
//			GLES20.glEnable(EnableCap.StencilTest);
//			GLES20.glEnable(EnableCap.Texture2D);
//			GLES20.glEnable(EnableCap.FramebufferSrgb);
//			GLES20.glEnable(EnableCap.TextureGenS);
//			GLES20.glEnable(EnableCap.TextureGenT);
//			GLES20.glEnable(EnableCap.Lighting);
//			GLES20.glEnable(EnableCap.Light0);
//			GLES20.glEnable(EnableCap.DepthTest);
//			GLES20.glDepthFunc(DepthFunction.Less);
//			GLES20.glClearDepth(1.0);
//
//			GLES20.glEnable(EnableCap.StencilTest);
//			GLES20.glClearStencil(0);
//			GLES20.glStencilMask(0xFFFFFFFF); // read&write
        // End Test

        // FIXME GLES20.glHint(GLES20.GL_HIN  HintTarget.PerspectiveCorrectionHint, HintMode.Nicest);
        //generateShadowFBO();
    }
/*
    public void display()
    {
        update();

        //First step: Render from the light POV to a FBO, story depth values only
        GLES20.glBindFramebuffer(FramebufferTarget.FramebufferExt, fboId);	//Rendering offscreen

        //Using the fixed pipeline to render to the depthbuffer
        GLES20.glUseProgram(0);

        // In the case we render the shadowmap to a higher resolution, the viewport must be modified accordingly.
        GLES20.glViewport(0, 0, (int) (RENDER_WIDTH * SHADOW_MAP_RATIO), (int) (RENDER_HEIGHT * SHADOW_MAP_RATIO));

        // Clear previous frame values
        GLES20.glClear(ClearBufferMask.DepthBufferBit);

        //Disable color rendering, we only want to write to the Z-Buffer
        GLES20.glColorMask(false, false, false, false);

        setupMatrices(p_light[0],p_light[1],p_light[2],l_light[0],l_light[1],l_light[2]);

        // Culling switching, rendering only backface, this is done to avoid self-shadowing
        GLES20.glCullFace(CullFaceMode.Front);
        drawObjects();

        //Save modelview/projection matrice into texture7, also add a biais
        setTextureMatrix();


        // Now rendering from the camera POV, using the FBO to generate shadows
        GLES20.glBindFramebuffer(FramebufferTarget.FramebufferExt, 0);

        GLES20.glViewport(0, 0, (int) RENDER_WIDTH, (int) RENDER_HEIGHT);

        //Enabling color write (previously disabled for light POV z-buffer rendering)
        GLES20.glColorMask(true, true, true, true);

        // Clear previous frame values
        GLES20.glClear(ClearBufferMask.ColorBufferBit | ClearBufferMask.DepthBufferBit);

        //Using the shadow shader
        GLES20.glUseProgram(theProgram);
        GLES20.glUniform1(shadowMapUniform, 7);
        GLES20.glActiveTexture(TextureUnit.Texture7);
        GLES20.glBindTexture(TextureTarget.Texture2D, depthTextureId);

        setupMatrices(p_camera[0],p_camera[1],p_camera[2],l_camera[0],l_camera[1],l_camera[2]);

        GLES20.glCullFace(CullFaceMode.Back);
        drawObjects();

        GLES20.glUseProgram(0);
    }

    */

    private void drawObjects()
    {

        /*
        // Ground
        GLES20.glColor4fv(0.3f, 0.3f, 0.3f, 1);
        GLES20.glBegin(BeginMode.Quads);
        GLES20.glVertex3(-35, 2, -35);
        GLES20.glVertex3(-35, 2, 15);
        GLES20.glVertex3(15, 2, 15);
        GLES20.glVertex3(15, 2, -35);
        GLES20.glEnd();

        GLES20.glColor4f(0.9f, 0.9f, 0.9f, 1);

        // Instead of calling glTranslatef, we need a custom function that also maintain the light matrix
        startTranslate(0,4,-16);
        Glut.SolidCube(4);
        endTranslate();

        startTranslate(0,4,-5);
        Glut.SolidCube(4);
        endTranslate();
        */
    }

    /*

    private void startTranslate(float x,float y,float z)
    {
        GLES20.glPushMatrix();
        GLES20.glTranslate(x, y, z);

        GLES20.glMatrixMode(MatrixMode.Texture);
        GLES20.glActiveTexture(TextureUnit.Texture7);
        GLES20.glPushMatrix();
        GLES20.glTranslate(x, y, z);
    }

    private void endTranslate()
    {
        GLES20.glPopMatrix();
        GLES20.glMatrixMode(MatrixMode.Modelview);
        GLES20.glPopMatrix();
    }

    private void generateShadowFBO()
    {
        int shadowMapWidth = (int)(RENDER_WIDTH * SHADOW_MAP_RATIO);
        int shadowMapHeight = (int)(RENDER_HEIGHT * SHADOW_MAP_RATIO);

        //GLfloat borderColor[4] = {0,0,0,0};

        FramebufferErrorCode FBOstatus;

        // Try to use a texture depth component
        GLES20.glGenTextures(1, out depthTextureId);
        GLES20.glBindTexture(TextureTarget.Texture2D, depthTextureId);


//			// added
//			GLES20.glExt.GenRenderbuffers(1, out depthBufferId);
//			GLES20.glExt.BindRenderbuffer(RenderbufferTarget.RenderbufferExt, depthBufferId);
//			GLES20.glExt.RenderbufferStorage(RenderbufferTarget.RenderbufferExt, (RenderbufferStorage)All.DepthComponent32, 512, 512);
//			GLES20.glExt.FramebufferRenderbuffer(FramebufferTarget.FramebufferExt, FramebufferAttachment.DepthAttachmentExt, RenderbufferTarget.RenderbufferExt, depthBufferId);
//			// end added

        // GL_LINEAR does not make sense for depth texture. However, next tutorial shows usage of GL_LINEAR and PCF
        GLES20.glTexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMinFilter, (int) All.Nearest);
        GLES20.glTexParameter(TextureTarget.Texture2D, TextureParameterName.TextureMagFilter, (int) All.Nearest);

        // Remove artefact on the edges of the shadowmap
        GLES20.glTexParameter(TextureTarget.Texture2D, TextureParameterName.TextureWrapS, (int) All.Clamp);
        GLES20.glTexParameter(TextureTarget.Texture2D, TextureParameterName.TextureWrapT, (int) All.Clamp);

        //glTexParameterfv( GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor );



        // No need to force GL_DEPTH_COMPONENT24, drivers usually give you the max precision if available 
        GLES20.glTexImage2D(TextureTarget.Texture2D, 0, PixelInternalFormat.DepthComponent, shadowMapWidth,
                shadowMapHeight, 0, PixelFormat.DepthComponent, PixelType.UnsignedByte, IntPtr.Zero);

        GLES20.glBindTexture(TextureTarget.Texture2D, 0);

        // create a framebuffer object
        GLES20.glGenFramebuffers(1, out fboId);
        GLES20.glBindFramebuffer(FramebufferTarget.FramebufferExt, fboId);

        // Instruct openGL that we won't bind a color texture with the currently binded FBO
        GLES20.glDrawBuffer(DrawBufferMode.None);
        GLES20.glReadBuffer(ReadBufferMode.None);

        // attach the texture to FBO depth attachment point
        GLES20.glFramebufferTexture2D(FramebufferTarget.FramebufferExt, FramebufferAttachment.DepthAttachmentExt, TextureTarget.Texture2D, depthTextureId, 0);

        // check FBO status
        FBOstatus = GLES20.glCheckFramebufferStatus(FramebufferTarget.FramebufferExt);
        if(FBOstatus != FramebufferErrorCode.FramebufferCompleteExt)
            MessageBox.Show("GL_FRAMEBUFFER_COMPLETE_EXT failed, CANNOT use FBO\n");

        // switch back to window-system-provided framebuffer
        GLES20.glBindFramebuffer(FramebufferTarget.FramebufferExt, 0);
    }

    private void setupMatrices(float position_x,float position_y,float position_z,float lookAt_x,float lookAt_y,float lookAt_z)
    {
        GLES20.glMatrixMode(MatrixMode.Projection);
        GLES20.glLoadIdentity();
        OpenTK.Graphics.Glu.Perspective(45,RENDER_WIDTH/RENDER_HEIGHT,10,40000);
        GLES20.glMatrixMode(MatrixMode.Modelview);
        GLES20.glLoadIdentity();
        OpenTK.Graphics.Glu.LookAt(position_x,position_y,position_z,lookAt_x,lookAt_y,lookAt_z,0,1,0);
    }

    static double[] modelView = new double[16];
    static double[] projection = new double[16];

    private void setTextureMatrix()
    {
        // This is matrix transform every coordinate x,y,z
        // x = x* 0.5 + 0.5 
        // y = y* 0.5 + 0.5 
        // z = z* 0.5 + 0.5 
        // Moving from unit cube [-1,1] to [0,1]  
        double[] bias = new double[]{
                0.5, 0.0, 0.0, 0.0,
                0.0, 0.5, 0.0, 0.0,
                0.0, 0.0, 0.5, 0.0,
                0.5, 0.5, 0.5, 1.0};

        // Grab modelview and transformation matrices
        GLES20.glGetDouble(GetPName.ModelviewMatrix, modelView);
        GLES20.glGetDouble(GetPName.ProjectionMatrix, projection);


        GLES20.glMatrixMode(MatrixMode.Projection);
        GLES20.glActiveTexture(TextureUnit.Texture7);

        GLES20.glLoadIdentity();
        GLES20.glLoadMatrix(bias);

        // concatating all matrice into one.
        GLES20.glMultMatrix(projection);
        GLES20.glMultMatrix(modelView);

        // Go back to normal matrix mode
        GLES20.glMatrixMode(MatrixMode.Modelview);
    }

    */

    private void update()
    {
        p_light[0] = light_mvnt * (float)Math.cos(getElapsedTime()/1000.0);
        p_light[2] = light_mvnt * (float)Math.sin(getElapsedTime()/1000.0);
    }
}
