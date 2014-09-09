package com.tutorial.glsltutorials.tutorials.GLES_Helpers;

/**
 * Created by Jamie on 6/7/14.
 */
public class FragmentShaders {
    public static final String ColorPassthrough_frag =
    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = theColor;" +
    "}";

    public static  final String ColorUniform_frag =
    "uniform vec4 baseColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = baseColor;" +
    "}";

    public static final String ColorMultUniform_frag =
    "uniform vec4 baseColor;" +

    "varying vec4 theColor;" +

    "void main()" +
    "{" +
        "gl_FragColor = theColor * baseColor;" +
    "}";
}

