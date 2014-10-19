package com.tutorial.glsltutorials.tutorials.Text;

import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;

/**
 * Created by jamie on 10/19/14.
 */
public class Letters {
    public static Float[] A;
    public static Float[] B;
    public static Float[] C;
    public static Float[] D;
    public static Float[] E;
    public static Float[] F;
    public static Float[] G;
    public static Float[] H;
    public static Float[] I;
    public static Float[] J;
    public static Float[] K;
    public static Float[] L;
    public static Float[] M;
    public static Float[] N;
    public static Float[] O;
    public static Float[] P;
    public static Float[] Q;
    public static Float[] R;
    public static Float[] S;
    public static Float[] T;
    public static Float[] U;
    public static Float[] V;
    public static Float[] W;
    public static Float[] X;
    public static Float[] Y;
    public static Float[] Z;

    static
    {
        AddA();
        AddB();
        AddC();
        AddD();
        AddE();
        AddF();
        AddG();
        AddH();
        AddI();
        AddJ();
        AddK();
        AddL();
        AddM();
        AddN();
        AddO();
        AddP();
        AddQ();
        AddR();
        AddS();
        AddT();
        AddU();
        AddV();
        AddW();
        AddX();
        AddY();
        AddZ();
    }

    private static Float[] Rectangle(Float width, Float height)
    {
        return Symbols.Rectangle(width, height);
    }

    private static void AddVertex(Float[] number, int vertexNumber, Vector3f vertex)
    {
        number[vertexNumber * 3 + 0] = vertex.x;
        number[vertexNumber * 3 + 1] = vertex.y;
        number[vertexNumber * 3 + 2] = vertex.z;
    }

    private static void AddTrianglePair(Float[] number, int firstVertex,
                                        Vector3f V0, Vector3f V1, Vector3f V2, Vector3f V3)
    {
        AddVertex(number, firstVertex++, V0);
        AddVertex(number, firstVertex++, V1);
        AddVertex(number, firstVertex++, V2);
        AddVertex(number, firstVertex++, V2);
        AddVertex(number, firstVertex++, V1);
        AddVertex(number, firstVertex++, V3);
    }
		
		/*	Y0					V0		V2
		 * 
		 * 
		 * 	Y1			V6						V8
		 * 	
		 * 	Y2			V7						V9
		 * 
		 * 
		 * 	Y3	V1		V3						V4		V5
		 * 		X0		X1		X2		X3		X4		X5
		 */

    private static void AddA()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -0.5f;
        Float X3 = -X2;
        Float X4 = -X1;
        Float X5 = -X0;

        Float Y0 = 6f;
        Float Y1 = -0.5f;
        Float Y2 = -1.5f;
        Float Y3 = -6f;

        Vector3f V0 = new Vector3f(X2, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y3, 0f);
        Vector3f V2 = new Vector3f(X3, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y3, 0f);

        Vector3f V4 = new Vector3f(X4, Y3, 0f);
        Vector3f V5 = new Vector3f(X5, Y3, 0f);

        Vector3f V6 = new Vector3f(X1, Y1, 0f);
        Vector3f V7 = new Vector3f(X1, Y2, 0f);
        Vector3f V8 = new Vector3f(X4, Y1, 0f);
        Vector3f V9 = new Vector3f(X4, Y2, 0f);

        A = new Float [54];

        AddTrianglePair(A, 0, V0, V1, V2, V3);
        AddTrianglePair(A, 6, V0, V4, V2, V5);
        AddTrianglePair(A, 12, V6, V7, V8, V9);
    }

    /*	Y0	V0		V2		V4		V6
     *  
     * 	Y1			V8		V9		
     * 	
     *  Y2			V10		V12
     *  
     * 	Y3			V11		V13
     * 	
     *  Y4			V14		V15
     * 
     * 	Y5	V1		V3		V5		V7
     * 		X0		X1		X2		X3
     */
    private static void AddB()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y2 = 1f;
        Float Y3 = -Y2;
        Float Y4 = -Y1;
        Float Y5 = -Y0;


        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y5, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y5, 0f);

        Vector3f V4 = new Vector3f(X2, Y0, 0f);
        Vector3f V5 = new Vector3f(X2, Y5, 0f);
        Vector3f V6 = new Vector3f(X3, Y0, 0f);
        Vector3f V7 = new Vector3f(X3, Y5, 0f);

        Vector3f V8 = new Vector3f(X1, Y1, 0f);
        Vector3f V9 = new Vector3f(X2, Y1, 0f);

        Vector3f V10 = new Vector3f(X1, Y2, 0f);
        Vector3f V11 = new Vector3f(X1, Y3, 0f);
        Vector3f V12 = new Vector3f(X2, Y2, 0f);
        Vector3f V13 = new Vector3f(X2, Y3, 0f);

        Vector3f V14 = new Vector3f(X1, Y4, 0f);
        Vector3f V15 = new Vector3f(X2, Y4, 0f);

        B = new Float[90];
        AddTrianglePair(B, 0, V0, V1, V2, V3);
        AddTrianglePair(B, 6, V4, V5, V6, V7);
        AddTrianglePair(B, 12, V2, V8, V4, V9);
        AddTrianglePair(B, 18, V10, V11, V12, V13);
        AddTrianglePair(B, 24, V14, V3, V15, V5);
    }


    /*	Y0			V0		V2
     * 
     * 	Y1	V1		V8				V3
     * 	
     *  Y2	
     * 	
     *  Y3
     * 	
     *  Y4	V4		V9				V6
     * 
     * 	Y5			V5		V7
     * 		X0		X1		X2		X3
     */
    private static void AddC()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y4 = -Y1;
        Float Y5 = -Y0;


        Vector3f V0 = new Vector3f(X1, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y1, 0f);
        Vector3f V2 = new Vector3f(X2, Y0, 0f);
        Vector3f V3 = new Vector3f(X3, Y1, 0f);

        Vector3f V4 = new Vector3f(X0, Y4, 0f);
        Vector3f V5 = new Vector3f(X1, Y5, 0f);
        Vector3f V6 = new Vector3f(X3, Y4, 0f);
        Vector3f V7 = new Vector3f(X2, Y5, 0f);

        Vector3f V8 = new Vector3f(X1, Y1, 0f);
        Vector3f V9 = new Vector3f(X1, Y4, 0f);

        C = new Float[54];
        AddTrianglePair(C, 0, V0, V1, V2, V3);
        AddTrianglePair(C, 6, V4, V5, V6, V7);
        AddTrianglePair(C, 12, V1, V4, V8, V9);
    }

    /*	Y0	V0		V2		V4		V6
     * 
     * 	Y1			V8		V9
     * 	
     *  Y2			V10		V11
     * 
     * 	Y3	V1		V3		V5		V7
     * 		X0		X1		X2		X3
     */
    private static void AddD()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y2 = -Y1;
        Float Y3 = -Y0;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y3, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y3, 0f);

        Vector3f V4 = new Vector3f(X2, Y0, 0f);
        Vector3f V5 = new Vector3f(X2, Y3, 0f);
        Vector3f V6 = new Vector3f(X3, Y0, 0f);
        Vector3f V7 = new Vector3f(X3, Y3, 0f);

        Vector3f V8 = new Vector3f(X1, Y1, 0f);
        Vector3f V9 = new Vector3f(X2, Y1, 0f);
        Vector3f V10 = new Vector3f(X1, Y2, 0f);
        Vector3f V11 = new Vector3f(X2, Y2, 0f);

        D = new Float[72];
        AddTrianglePair(D, 0, V0, V1, V2, V3);
        AddTrianglePair(D, 6, V4, V5, V6, V7);
        AddTrianglePair(D, 12, V2, V8, V4, V9);
        AddTrianglePair(D, 18, V10, V3, V11, V5);
    }

    /*	Y0	V0		V2				V5
     *  
     * 	Y1			V4				V6
     * 	
     *  Y2			V7		V9
     *  
     * 	Y3			V8		V10
     * 	
     *  Y4			V11				V12
     * 
     * 	Y5	V1		V3				V13
     * 		X0		X1		X2		X3
     */
    private static void AddE()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y2 = 1f;
        Float Y3 = -Y2;
        Float Y4 = -Y1;
        Float Y5 = -Y0;


        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y5, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y5, 0f);

        Vector3f V4 = new Vector3f(X1, Y1, 0f);
        Vector3f V5 = new Vector3f(X3, Y0, 0f);
        Vector3f V6 = new Vector3f(X3, Y1, 0f);

        Vector3f V7 = new Vector3f(X1, Y2, 0f);
        Vector3f V8 = new Vector3f(X1, Y3, 0f);
        Vector3f V9 = new Vector3f(X2, Y2, 0f);
        Vector3f V10 = new Vector3f(X2, Y3, 0f);

        Vector3f V11 = new Vector3f(X1, Y4, 0f);
        Vector3f V12 = new Vector3f(X3, Y4, 0f);
        Vector3f V13 = new Vector3f(X3, Y5, 0f);

        E = new Float[72];
        AddTrianglePair(E, 0, V0, V1, V2, V3);
        AddTrianglePair(E, 6, V2, V4, V5, V6);
        AddTrianglePair(E, 12, V7, V8, V9, V10);
        AddTrianglePair(E, 18, V11, V3, V12, V13);
    }

    /*	Y0	V0		V2				V5
     *  
     * 	Y1			V4				V6
     * 	
     *  Y2			V7		V9
     *  
     * 	Y3			V8		V10
     * 	
     *  Y4							
     * 
     * 	Y5	V1		V3				
     * 		X0		X1		X2		X3
     */
    private static void AddF()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y2 = 1f;
        Float Y3 = -Y2;
        Float Y5 = -Y0;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y5, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y5, 0f);

        Vector3f V4 = new Vector3f(X1, Y1, 0f);
        Vector3f V5 = new Vector3f(X3, Y0, 0f);
        Vector3f V6 = new Vector3f(X3, Y1, 0f);

        Vector3f V7 = new Vector3f(X1, Y2, 0f);
        Vector3f V8 = new Vector3f(X1, Y3, 0f);
        Vector3f V9 = new Vector3f(X2, Y2, 0f);
        Vector3f V10 = new Vector3f(X2, Y3, 0f);

        F = new Float[54];
        AddTrianglePair(F, 0, V0, V1, V2, V3);
        AddTrianglePair(F, 6, V2, V4, V5, V6);
        AddTrianglePair(F, 12, V7, V8, V9, V10);
    }

    /*	Y0			V0				V2
     * 
     * 	Y1	V1		V8						V3
     * 	
     *  Y2					X10				X12
     * 	
     *  Y3					X11		X14		X13
     * 					
     *  Y4	V4		V9				X15		V6
     * 
     * 	Y5			V5				V7
     * 		X0		X1		X2		X3		X4
     */
    private static void AddG()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = 0f;
        Float X3 = -X1;
        Float X4 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y2 = 0.5f;
        Float Y3 = -Y2;
        Float Y4 = -Y1;
        Float Y5 = -Y0;


        Vector3f V0 = new Vector3f(X1, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y1, 0f);
        Vector3f V2 = new Vector3f(X3, Y0, 0f);
        Vector3f V3 = new Vector3f(X4, Y1, 0f);

        Vector3f V4 = new Vector3f(X0, Y4, 0f);
        Vector3f V5 = new Vector3f(X1, Y5, 0f);
        Vector3f V6 = new Vector3f(X4, Y4, 0f);
        Vector3f V7 = new Vector3f(X3, Y5, 0f);

        Vector3f V8 = new Vector3f(X1, Y1, 0f);
        Vector3f V9 = new Vector3f(X1, Y4, 0f);

        Vector3f V10 = new Vector3f(X2, Y2, 0f);
        Vector3f V11 = new Vector3f(X2, Y3, 0f);
        Vector3f V12 = new Vector3f(X4, Y2, 0f);
        Vector3f V13 = new Vector3f(X4, Y3, 0f);

        Vector3f V14 = new Vector3f(X3, Y3, 0f);
        Vector3f V15 = new Vector3f(X3, Y4, 0f);


        G = new Float[90];
        AddTrianglePair(G, 0, V0, V1, V2, V3);
        AddTrianglePair(G, 6, V4, V5, V6, V7);
        AddTrianglePair(G, 12, V1, V4, V8, V9);
        AddTrianglePair(G, 18, V10, V11, V12, V13);
        AddTrianglePair(G, 24, V14, V15, V13, V6);
    }

    /*	Y0	V0		V2		V4		V6
     * 
     * 	Y1			V8		V10					
     * 	
     *  Y2			V9		V11		
     * 	
     *  Y3	V1		V3		V5		V7
     * 					
     * 		X0		X1		X2		X3
     */
    private static void AddH()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = 0.5f;
        Float Y2 = -Y1;
        Float Y3 = -Y0;


        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y3, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y3, 0f);

        Vector3f V4 = new Vector3f(X2, Y0, 0f);
        Vector3f V5 = new Vector3f(X2, Y3, 0f);
        Vector3f V6 = new Vector3f(X3, Y0, 0f);
        Vector3f V7 = new Vector3f(X3, Y3, 0f);

        Vector3f V8 = new Vector3f(X1, Y1, 0f);
        Vector3f V9 = new Vector3f(X1, Y2, 0f);
        Vector3f V10 = new Vector3f(X2, Y1, 0f);
        Vector3f V11 = new Vector3f(X2, Y2, 0f);

        H = new Float[54];
        AddTrianglePair(H, 0, V0, V1, V2, V3);
        AddTrianglePair(H, 6, V4, V5, V6, V7);
        AddTrianglePair(H, 12, V8, V9, V10, V11);
    }

    private static void AddI()
    {
        I = Symbols.Rectangle(1.5f, 12f);
    }

    /*	Y0					V0		V2
     * 
     * 
     * 
     * 	Y1	V7		V8							
     * 	
     *  Y2	V4		V9		V1		V3	
     * 	
     *  Y3			V5		V6		
     * 						
     * 		X0		X1		X2		X3
     */
    private static void AddJ()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = -2f;
        Float Y2 = -5f;
        Float Y3 = -Y0;


        Vector3f V0 = new Vector3f(X2, Y0, 0f);
        Vector3f V1 = new Vector3f(X2, Y2, 0f);
        Vector3f V2 = new Vector3f(X3, Y0, 0f);
        Vector3f V3 = new Vector3f(X3, Y2, 0f);

        Vector3f V4 = new Vector3f(X0, Y2, 0f);
        Vector3f V5 = new Vector3f(X1, Y3, 0f);
        Vector3f V6 = new Vector3f(X2, Y3, 0f);

        Vector3f V7 = new Vector3f(X0, Y1, 0f);
        Vector3f V8 = new Vector3f(X1, Y1, 0f);
        Vector3f V9 = new Vector3f(X1, Y2, 0f);

        J = new Float[54];
        AddTrianglePair(J, 0, V0, V1, V2, V3);
        AddTrianglePair(J, 6, V4, V5, V3, V6);
        AddTrianglePair(J, 12, V7, V4, V8, V9);
    }

    /*	Y0	V0		V2		V4		V6
     * 
     * 	Y1			V5							
     * 	
     *  Y2			V7
     * 	
     *  Y3	V1		V3		V9		V8
     * 						
     * 		X0		X1		X2		X3
     */
    private static void AddK()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = 1.75f;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = -0.75f;
        Float Y2 = -Y1;
        Float Y3 = -Y0;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y3, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y3, 0f);

        Vector3f V4 = new Vector3f(X2, Y0, 0f);
        Vector3f V5 = new Vector3f(X1, Y1, 0f);
        Vector3f V6 = new Vector3f(X3, Y0, 0f);
        Vector3f V7 = new Vector3f(X1, Y2, 0f);

        Vector3f V8 = new Vector3f(X3, Y3, 0f);
        Vector3f V9 = new Vector3f(X2, Y3, 0f);

        K = new Float[54];
        AddTrianglePair(K, 0, V0, V1, V2, V3);
        AddTrianglePair(K, 6, V4, V5, V6, V7);
        AddTrianglePair(K, 12, V5, V7, V8, V9);
    }

    /*	Y0	V0		V2		
     * 
     * 	Y1			V4		V5
     * 
     * 	Y2	V1		V3		V6
     * 		X0		X1		X2
     */
    private static void AddL()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X0;

        Float Y0 = 6f;
        Float Y1 = -5f;
        Float Y2 = -6f;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y2, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y2, 0f);

        Vector3f V4 = new Vector3f(X1, Y1, 0f);
        Vector3f V5 = new Vector3f(X2, Y1, 0f);
        Vector3f V6 = new Vector3f(X2, Y2, 0f);

        L = new Float[36];
        AddTrianglePair(L, 0, V0, V1, V2, V3);
        AddTrianglePair(L, 6, V4, V3, V5, V6);
    }

    /*	Y0	V0		V2						V6		V8
     * 
     * 
     * 	Y1					V4		V5
     * 	
     * 
     * 	Y2	V1		V3						V7		V9
     * 		X0		X1		X2		X3		X4		X5
     */
    private static void AddM()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -0.5f;
        Float X3 = -X2;
        Float X4 = -X1;
        Float X5 = -X0;

        Float Y0 = 6f;
        Float Y1 = 0.0f;
        Float Y2 = -6f;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y2, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y2, 0f);

        Vector3f V4 = new Vector3f(X2, Y1, 0f);
        Vector3f V5 = new Vector3f(X3, Y1, 0f);

        Vector3f V6 = new Vector3f(X4, Y0, 0f);
        Vector3f V7 = new Vector3f(X4, Y2, 0f);
        Vector3f V8 = new Vector3f(X5, Y0, 0f);
        Vector3f V9 = new Vector3f(X5, Y2, 0f);

        M = new Float[72];
        AddTrianglePair(M, 0, V0, V1, V2, V3);
        AddTrianglePair(M, 6, V0, V4, V2, V5);
        AddTrianglePair(M, 12, V6, V4, V8, V5);
        AddTrianglePair(M, 18, V6, V7, V8, V9);
    }

    /*	Y0	V0		V2		V4		V6
     * 
     * 
     * 	
     * 
     * 	Y1	V1		V3		V5		V7
     * 		X0		X1		X2		X3
     */
    private static void AddN()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = -6f;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y1, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y1, 0f);

        Vector3f V4 = new Vector3f(X2, Y0, 0f);
        Vector3f V5 = new Vector3f(X2, Y1, 0f);
        Vector3f V6 = new Vector3f(X3, Y0, 0f);
        Vector3f V7 = new Vector3f(X3, Y1, 0f);

        N = new Float[54];
        AddTrianglePair(N, 0, V0, V1, V2, V3);
        AddTrianglePair(N, 6, V4, V5, V6, V7);
        AddTrianglePair(N, 12, V0, V5, V2, V7);
    }

    /*	Y0	V0		V2		V4		V6
     * 
     * 	Y1			V8		V9
     * 	
     *  Y2			V10		V11
     * 
     * 	Y3	V1		V3		V5		V7
     * 		X0		X1		X2		X3
     */
    private static void AddO()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y2 = -Y1;
        Float Y3 = -Y0;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y3, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y3, 0f);

        Vector3f V4 = new Vector3f(X2, Y0, 0f);
        Vector3f V5 = new Vector3f(X2, Y3, 0f);
        Vector3f V6 = new Vector3f(X3, Y0, 0f);
        Vector3f V7 = new Vector3f(X3, Y3, 0f);

        Vector3f V8 = new Vector3f(X1, Y1, 0f);
        Vector3f V9 = new Vector3f(X2, Y1, 0f);
        Vector3f V10 = new Vector3f(X1, Y2, 0f);
        Vector3f V11 = new Vector3f(X2, Y2, 0f);

        O = new Float[72];
        AddTrianglePair(O, 0, V0, V1, V2, V3);
        AddTrianglePair(O, 6, V4, V5, V6, V7);
        AddTrianglePair(O, 12, V2, V8, V4, V9);
        AddTrianglePair(O, 18, V10, V3, V11, V5);
    }

    /*	Y0	V0		V2		V4		
     *  
     * 	Y1			V8		V9		V6		
     * 	
     *  Y2			V10		V12		V7
     *  
     * 	Y3			V11		V5
     * 	
     *  Y4					
     * 
     * 	Y5	V1		V3				
     * 		X0		X1		X2		X3
     */
    private static void AddP()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y2 = 1f;
        Float Y3 = -Y2;
        Float Y5 = -Y0;


        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y5, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y5, 0f);

        Vector3f V4 = new Vector3f(X2, Y0, 0f);
        Vector3f V5 = new Vector3f(X2, Y3, 0f);
        Vector3f V6 = new Vector3f(X3, Y1, 0f);
        Vector3f V7 = new Vector3f(X3, Y2, 0f);

        Vector3f V8 = new Vector3f(X1, Y1, 0f);
        Vector3f V9 = new Vector3f(X2, Y1, 0f);

        Vector3f V10 = new Vector3f(X1, Y2, 0f);
        Vector3f V11 = new Vector3f(X1, Y3, 0f);
        Vector3f V12 = new Vector3f(X2, Y2, 0f);

        P = new Float[72];
        AddTrianglePair(P, 0, V0, V1, V2, V3);
        AddTrianglePair(P, 6, V4, V5, V6, V7);
        AddTrianglePair(P, 12, V2, V8, V4, V9);
        AddTrianglePair(P, 18, V10, V11, V12, V5);
    }

    /*	Y0			V2		V4		
     *
     * 	Y1	V0		V8		V9		V6
     * 		 	
     *  Y2				V12
     * 	
     *  Y3	V1		V10		V11	V14	V7
     * 	
     *  Y4				V13			
     * 
     * 	Y5			V3		V5	V15		
     * 		X0		X1	X2	X3	X4	X5
     */
    private static void AddQ()
    {
        Float X0 = -3f;
        Float X1 = -1.5f;
        Float X2 = 0f;
        Float X3 = -X1;
        Float X4 = 2.5f;
        Float X5 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y2 = 0f;
        Float Y3 = -Y1;
        Float Y4 = -5.5f;
        Float Y5 = -Y0;

        Vector3f V0 = new Vector3f(X0, Y1, 0f);
        Vector3f V1 = new Vector3f(X0, Y3, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y5, 0f);

        Vector3f V4 = new Vector3f(X3, Y0, 0f);
        Vector3f V5 = new Vector3f(X3, Y5, 0f);
        Vector3f V6 = new Vector3f(X5, Y1, 0f);
        Vector3f V7 = new Vector3f(X5, Y3, 0f);

        Vector3f V8 = new Vector3f(X1, Y1, 0f);
        Vector3f V9 = new Vector3f(X3, Y1, 0f);

        Vector3f V10 = new Vector3f(X1, Y3, 0f);
        Vector3f V11 = new Vector3f(X3, Y3, 0f);

        Vector3f V12 = new Vector3f(X2, Y2, 0f);
        Vector3f V13 = new Vector3f(X2, Y4, 0f);
        Vector3f V14 = new Vector3f(X4, Y3, 0f);
        Vector3f V15 = new Vector3f(X4, Y5, 0f);

        Q = new Float[90];
        AddTrianglePair(Q, 0, V0, V1, V2, V3);
        AddTrianglePair(Q, 6, V4, V5, V6, V7);
        AddTrianglePair(Q, 12, V2, V8, V4, V9);
        AddTrianglePair(Q, 18, V10, V3, V11, V5);
        AddTrianglePair(Q, 24, V12, V13, V14, V15);
    }

    /*	Y0	V0		V2		V4		
     *  
     * 	Y1			V8		V9		V6		
     * 	
     *  Y2			V10		V12
     *  
     * 	Y3			V11		V13
     * 	
     *  Y4					
     * 
     * 	Y5	V1		V3		V5		V7
     * 		X0		X1		X2		X3
     */
    private static void AddR()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y2 = 1f;
        Float Y3 = -Y2;
        Float Y5 = -Y0;


        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y5, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y5, 0f);

        Vector3f V4 = new Vector3f(X2, Y0, 0f);
        Vector3f V5 = new Vector3f(X2, Y5, 0f);
        Vector3f V6 = new Vector3f(X3, Y1, 0f);
        Vector3f V7 = new Vector3f(X3, Y5, 0f);

        Vector3f V8 = new Vector3f(X1, Y1, 0f);
        Vector3f V9 = new Vector3f(X2, Y1, 0f);

        Vector3f V10 = new Vector3f(X1, Y2, 0f);
        Vector3f V11 = new Vector3f(X1, Y3, 0f);
        Vector3f V12 = new Vector3f(X2, Y2, 0f);
        Vector3f V13 = new Vector3f(X2, Y3, 0f);

        R = new Float[72];
        AddTrianglePair(R, 0, V0, V1, V2, V3);
        AddTrianglePair(R, 6, V4, V5, V6, V7);
        AddTrianglePair(R, 12, V2, V8, V4, V9);
        AddTrianglePair(R, 18, V10, V11, V12, V13);
    }

    /*	Y0			V0		V2
     *  
     * 	Y1	V1		V12				V3		
     * 	
     *  Y2	V8		V13		V10	
     *  
     * 	Y3			V9		V14		V11	
     * 	
     *  Y4	V4				V15		V6
     * 
     * 	Y5			V5		V7
     * 		X0		X1		X2		X3
     */
    private static void AddS()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y2 = 1f;
        Float Y3 = -Y2;
        Float Y4 = -Y1;
        Float Y5 = -Y0;


        Vector3f V0 = new Vector3f(X1, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y1, 0f);
        Vector3f V2 = new Vector3f(X2, Y0, 0f);
        Vector3f V3 = new Vector3f(X3, Y1, 0f);

        Vector3f V4 = new Vector3f(X0, Y4, 0f);
        Vector3f V5 = new Vector3f(X1, Y5, 0f);
        Vector3f V6 = new Vector3f(X3, Y4, 0f);
        Vector3f V7 = new Vector3f(X2, Y5, 0f);

        Vector3f V8 = new Vector3f(X0, Y2, 0f);
        Vector3f V9 = new Vector3f(X1, Y3, 0f);
        Vector3f V10 = new Vector3f(X2, Y2, 0f);
        Vector3f V11 = new Vector3f(X3, Y3, 0f);

        Vector3f V12 = new Vector3f(X1, Y1, 0f);
        Vector3f V13 = new Vector3f(X1, Y2, 0f);

        Vector3f V14 = new Vector3f(X2, Y3, 0f);
        Vector3f V15 = new Vector3f(X2, Y4, 0f);

        S = new Float[90];
        AddTrianglePair(S, 0, V0, V1, V2, V3);
        AddTrianglePair(S, 6, V4, V5, V6, V7);
        AddTrianglePair(S, 12, V8, V9, V10, V11);
        AddTrianglePair(S, 18, V1, V8, V12, V13);
        AddTrianglePair(S, 24, V14, V15, V11, V6);
    }

    /*	Y0	V0						V2				
     *  
     * 	Y1	V1		V4		V5		V3		
     * 
     * 	Y2	 		V6		V7		
     *
     * 		X0		X1		X2		X3
     */
    private static void AddT()
    {
        Float X0 = -3f;
        Float X1 = -0.5f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5.0f;
        Float Y2 = -Y0;


        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y1, 0f);
        Vector3f V2 = new Vector3f(X3, Y0, 0f);
        Vector3f V3 = new Vector3f(X3, Y1, 0f);

        Vector3f V4 = new Vector3f(X1, Y1, 0f);
        Vector3f V5 = new Vector3f(X1, Y2, 0f);
        Vector3f V6 = new Vector3f(X2, Y1, 0f);
        Vector3f V7 = new Vector3f(X2, Y2, 0f);

        T = new Float[36];

        AddTrianglePair(T, 0, V0, V1, V2, V3);
        AddTrianglePair(T, 6, V4, V5, V6, V7);
    }
    /*	Y0	V0		V2		V4		V6				
     *  
     * 	Y1	V1		V8		V9		V7					
     *  
     * 	Y2			V3		V5	
     *  
     * 		X0		X1		X2		X3
     */
    private static void AddU()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = -5f;
        Float Y2 = -Y0;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y1, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y2, 0f);

        Vector3f V4 = new Vector3f(X2, Y0, 0f);
        Vector3f V5 = new Vector3f(X2, Y2, 0f);
        Vector3f V6 = new Vector3f(X3, Y0, 0f);
        Vector3f V7 = new Vector3f(X3, Y1, 0f);

        Vector3f V8 = new Vector3f(X1, Y1, 0f);
        Vector3f V9 = new Vector3f(X2, Y1, 0f);

        U = new Float[54];
        AddTrianglePair(U, 0, V0, V1, V2, V3);
        AddTrianglePair(U, 6, V4, V5, V6, V7);
        AddTrianglePair(U, 12, V8, V3, V9, V5);
    }

    /*	Y0	V0		V2						V4		V5				
     *  
     * 
     * 
     * 	Y1					V1		V3		
     *  
     * 		X0		X1		X2		X3		X4		X5
     */
    private static void AddV()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -0.5f;
        Float X3 = -X2;
        Float X4 = -X1;
        Float X5 = -X0;

        Float Y0 = 6f;
        Float Y1 = -Y0;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X2, Y1, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X3, Y1, 0f);

        Vector3f V4 = new Vector3f(X4, Y0, 0f);
        Vector3f V5 = new Vector3f(X5, Y0, 0f);

        V = new Float[36];
        AddTrianglePair(V, 0, V0, V1, V2, V3);
        AddTrianglePair(V, 6, V4, V1, V5, V3);
    }

    /*	Y0	V0		V2						V6		V8
     * 
     * 
     * 	Y1					V4		V5
     * 	
     * 
     * 	Y2		V1		V3				V7		V9
     * 		X0	X1	X2	X3	X4		X5	X6	X7	X8	X9
     */
    private static void AddW()
    {
        Float X0 = -3f;
        Float X1 = -2.5f;
        Float X2 = -2f;
        Float X3 = -1.5f;
        Float X4 = -0.5f;
        Float X5 = -X4;
        Float X6 = -X3;
        Float X7 = -X2;
        Float X8 = -X1;
        Float X9 = -X0;

        Float Y0 = 6f;
        Float Y1 = 0.0f;
        Float Y2 = -6f;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X1, Y2, 0f);
        Vector3f V2 = new Vector3f(X2, Y0, 0f);
        Vector3f V3 = new Vector3f(X3, Y2, 0f);

        Vector3f V4 = new Vector3f(X4, Y1, 0f);
        Vector3f V5 = new Vector3f(X5, Y1, 0f);

        Vector3f V6 = new Vector3f(X7, Y0, 0f);
        Vector3f V7 = new Vector3f(X6, Y2, 0f);
        Vector3f V8 = new Vector3f(X9, Y0, 0f);
        Vector3f V9 = new Vector3f(X8, Y2, 0f);

        W = new Float[72];
        AddTrianglePair(W, 0, V0, V1, V2, V3);
        AddTrianglePair(W, 6, V4, V1, V5, V3);
        AddTrianglePair(W, 12, V4, V7, V5, V9);
        AddTrianglePair(W, 18, V6, V7, V8, V9);
    }

    /*	Y0	V0		V2		V4		V6				
     *  
     * 	Y1	V5		V7		V1		V3		
     *  
     * 		X0		X1		X2		X3
     */
    private static void AddX()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -X1;
        Float X3 = -X0;

        Float Y0 = 6f;
        Float Y1 = -Y0;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X2, Y1, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X3, Y1, 0f);

        Vector3f V4 = new Vector3f(X2, Y0, 0f);
        Vector3f V5 = new Vector3f(X0, Y1, 0f);
        Vector3f V6 = new Vector3f(X3, Y0, 0f);
        Vector3f V7 = new Vector3f(X1, Y1, 0f);

        X = new Float[36];
        AddTrianglePair(X, 0, V0, V1, V2, V3);
        AddTrianglePair(X, 6, V4, V5, V6, V7);
    }

    /*	Y0	V0		V2						V4		V5				
     *  
     * 	Y1					V1		V3		
     * 	
     *  Y2					V6		V7
     *  
     * 		X0		X1		X2		X3		X4		X5
     */
    private static void AddY()
    {
        Float X0 = -3f;
        Float X1 = -2.0f;
        Float X2 = -0.5f;
        Float X3 = -X2;
        Float X4 = -X1;
        Float X5 = -X0;

        Float Y0 = 6f;
        Float Y1 = 1f;
        Float Y2 = -6f;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X2, Y1, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X3, Y1, 0f);

        Vector3f V4 = new Vector3f(X4, Y0, 0f);
        Vector3f V5 = new Vector3f(X5, Y0, 0f);

        Vector3f V6 = new Vector3f(X2, Y2, 0f);
        Vector3f V7 = new Vector3f(X3, Y2, 0f);

        Y = new Float[54];
        AddTrianglePair(Y, 0, V0, V1, V2, V3);
        AddTrianglePair(Y, 6, V4, V1, V5, V3);
        AddTrianglePair(Y, 12, V1, V6, V3, V7);
    }

    /*	Y0	V0						V2			
     *  
     * 	Y1	V1						V3		
     * 	
     *  Y2	V4						V6
     *  
     *  Y3	V5						V7
     *  
     * 		X0						X1
     */
    private static void AddZ()
    {
        Float X0 = -3f;
        Float X1 = -X0;

        Float Y0 = 6f;
        Float Y1 = 5f;
        Float Y2 = -Y1;
        Float Y3 = -Y0;

        Vector3f V0 = new Vector3f(X0, Y0, 0f);
        Vector3f V1 = new Vector3f(X0, Y1, 0f);
        Vector3f V2 = new Vector3f(X1, Y0, 0f);
        Vector3f V3 = new Vector3f(X1, Y1, 0f);

        Vector3f V4 = new Vector3f(X0, Y2, 0f);
        Vector3f V5 = new Vector3f(X0, Y3, 0f);
        Vector3f V6 = new Vector3f(X1, Y2, 0f);
        Vector3f V7 = new Vector3f(X1, Y3, 0f);

        Z = new Float[54];
        AddTrianglePair(Z, 0, V0, V1, V2, V3);
        AddTrianglePair(Z, 6, V4, V5, V6, V7);
        AddTrianglePair(Z, 12, V4, V5, V2, V3);
    }

}
