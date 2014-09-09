package com.tutorial.glsltutorials.tutorials;

/**
 * Created by Jamie on 1/3/14.
 */
public class Numbers {
    static {Zero();One();Two();Three();Four();Five();Six();Seven();Eight();Nine();}
    public static Float[] Zero;
    public static Float[] One;
    public static Float[] Two;
    public static Float[] Three;
    public static Float[] Four;
    public static Float[] Five;
    public static Float[] Six;
    public static Float[] Seven;
    public static Float[] Eight;
    public static Float[] Nine;

    private static Float[] MoveX(Float[] input, float distance)
    {
        for (int i = 0; i < input.length; i = i + 3)
        {
            input[i] = input[i] + distance;
        }
        return input;
    }

    private static Float[] MoveY(Float[] input, float distance)
    {
        for (int i = 1; i < input.length; i = i + 3)
        {
            input[i] = input[i] + distance;
        }
        return input;
    }

    private static Float[] MoveXY(Float[] input, float distanceX, float distanceY)
    {
        input = MoveX(input, distanceX);
        input = MoveY(input, distanceY);
        return input;
    }

    private static Float[] SwapY(Float[] input)
    {
        for (int i = 1; i < input.length; i = i + 3)
        {
            input[i] = -input[i];
        }
        return input;
    }

    private static Float[] SwapX(Float[] input)
    {
        for (int i = 0; i < input.length; i = i + 3)
        {
            input[i] = -input[i];
        }
        return input;
    }

    private static Float[] ReverseRotation(Float[] input)
    {
        Float[] output = new Float[input.length];
        for (int i = 0; i < output.length; i++)
        {
            switch (i % 9)
            {
                case 0:
                case 1:
                case 2:
                    output[i] = input[i];
                    break;
                case 3:
                case 4:
                case 5:
                    output[i] = input[i+3];
                    break;
                case 6:
                case 7:
                case 8:
                    output[i] = input[i-3];
                    break;
            }
        }
        return output;
    }

    private static Float[] Rectangle(float width, float height)
    {
        Float[] rectangle = new Float[18];
        rectangle[0] = -width/2;
        rectangle[1] = height/2;
        rectangle[2] = 0f;
        rectangle[3] = rectangle[0];
        rectangle[4] = -rectangle[1];
        rectangle[5] = 0f;
        rectangle[6] = -rectangle[0];
        rectangle[7] = rectangle[1];
        rectangle[8] = 0f;

        rectangle[9] = -rectangle[0];
        rectangle[10] = rectangle[1];
        rectangle[11] = 0f;
        rectangle[12] = rectangle[0];
        rectangle[13] = -rectangle[1];
        rectangle[14] = 0f;
        rectangle[15] = -rectangle[0];
        rectangle[16] = -rectangle[1];
        rectangle[17] = 0f;
        return rectangle;
    }

    private static Float[] ZeroTop()
    {
        Float[] ZeroTop = new Float [18];
        ZeroTop[0] = -1.5f;
        ZeroTop[1] = 6f;
        ZeroTop[2] = 0f;
        ZeroTop[3] = -2.5f;
        ZeroTop[4] = 5f;
        ZeroTop[5] = 0f;
        ZeroTop[6] = -ZeroTop[0];
        ZeroTop[7] = ZeroTop[1];
        ZeroTop[8] = 0f;


        ZeroTop[9] = ZeroTop[3];
        ZeroTop[10] = ZeroTop[4];
        ZeroTop[11] = ZeroTop[5];
        ZeroTop[12] = -ZeroTop[3];
        ZeroTop[13] = ZeroTop[4];
        ZeroTop[14] = ZeroTop[5];
        ZeroTop[15] = -ZeroTop[0];
        ZeroTop[16] = ZeroTop[1];
        ZeroTop[17] = 0f;
        return ZeroTop;
    }

    private static Float[] ZeroLeft()
    {
        Float[] ZeroLeft = new Float [18];
        ZeroLeft[0] = -2.5f;
        ZeroLeft[1] = 5f;
        ZeroLeft[2] = 0f;
        ZeroLeft[3] = ZeroLeft[0];
        ZeroLeft[4] = -ZeroLeft[1];
        ZeroLeft[5] = 0f;
        ZeroLeft[6] = ZeroLeft[0] + 1f;
        ZeroLeft[7] = ZeroLeft[1];
        ZeroLeft[8] = 0f;


        ZeroLeft[9] = ZeroLeft[6];
        ZeroLeft[10] = ZeroLeft[1];
        ZeroLeft[11] = 0f;
        ZeroLeft[12] = ZeroLeft[0];
        ZeroLeft[13] = -ZeroLeft[1];
        ZeroLeft[14] = 0f;
        ZeroLeft[15] = ZeroLeft[6];
        ZeroLeft[16] = -ZeroLeft[1];
        ZeroLeft[17] = 0f;
        return ZeroLeft;
    }

    private static Float[]  ZeroBottom()
    {
        Float[] ZeroBottom = ZeroTop();
        ZeroBottom = SwapY(ZeroBottom);
        ZeroBottom = ReverseRotation(ZeroBottom);
        return ZeroBottom;
    }

    private static Float[]  ZeroRight()
    {
        Float[] ZeroRight = ZeroLeft();
        ZeroRight = SwapX(ZeroRight);
        ZeroRight = ReverseRotation(ZeroRight);
        return ZeroRight;
    }

    private static void Zero()
    {
        Zero = new Float [72];
        System.arraycopy(ZeroTop(),0,Zero,0,18);
        System.arraycopy(ZeroBottom(),0,Zero,18,18);
        System.arraycopy(ZeroLeft(),0,Zero,36,18);
        System.arraycopy(ZeroRight(),0,Zero,54,18);
    }

    private static void One()
    {
        One = Rectangle(1f, 12f);
    }

    private static Float[] TwoMiddle()
    {
        Float[] TwoMiddle = FiveMiddle();
        TwoMiddle = SwapY(TwoMiddle);
        TwoMiddle = ReverseRotation(TwoMiddle);
        return TwoMiddle;
    }

    private static void Two()
    {
        Two = new Float [90];
        System.arraycopy(ZeroTop(),0,Two,0,18);
        System.arraycopy(ZeroBottom(),0,Two,18,18);
        System.arraycopy(TwoMiddle(),0,Two,36,18);
        System.arraycopy(EightUpperRight(),0,Two,54,18);
        System.arraycopy(EightLowerLeft(),0,Two,72,18);
    }

    private static Float[] ThreeMiddleTop()
    {
        Float[] ThreeMiddleTop = new Float[18];
        ThreeMiddleTop[0] = -1.5f;
        ThreeMiddleTop[1] = 1f;
        ThreeMiddleTop[2] = 0f;
        ThreeMiddleTop[3] = -2.5f;
        ThreeMiddleTop[4] = 0f;
        ThreeMiddleTop[5] = 0f;
        ThreeMiddleTop[6] = -ThreeMiddleTop[0];
        ThreeMiddleTop[7] = 0f;
        ThreeMiddleTop[8] = 0f;

        ThreeMiddleTop[9] = ThreeMiddleTop[0];
        ThreeMiddleTop[10] = ThreeMiddleTop[1];
        ThreeMiddleTop[11] = 0f;
        ThreeMiddleTop[12] = -ThreeMiddleTop[0];
        ThreeMiddleTop[13] = 0f;
        ThreeMiddleTop[14] = 0f;
        ThreeMiddleTop[15] = -ThreeMiddleTop[3];
        ThreeMiddleTop[16] = ThreeMiddleTop[1];
        ThreeMiddleTop[17] = 0f;
        return ThreeMiddleTop;
    }

    private static Float[] ThreeMiddleBottom()
    {
        Float[] ThreeMiddleBottom = ThreeMiddleTop();
        ThreeMiddleBottom = SwapY(ThreeMiddleBottom);
        ThreeMiddleBottom = ReverseRotation(ThreeMiddleBottom);
        return ThreeMiddleBottom;
    }

    private static Float[] ThreeMiddle()
    {
        Float[] ThreeMiddle = new Float[36];
        System.arraycopy(ThreeMiddleTop(),0,ThreeMiddle,0,18);
        System.arraycopy(ThreeMiddleBottom(),0,ThreeMiddle,18,18);
        return ThreeMiddle;
    }

    private static void Three()
    {
        Three = new Float [108];
        System.arraycopy(ZeroTop(),0,Three,0,18);
        System.arraycopy(ZeroBottom(),0,Three,18,18);
        System.arraycopy(ThreeMiddle(),0,Three,36,36);
        System.arraycopy(EightUpperRight(),0,Three,72,18);
        System.arraycopy(EightLowerRight(),0,Three,90,18);
    }

    private static void Four()
    {
        Four = new Float [72];
        System.arraycopy(EightUpperBottom(),0,Four,0,18);
        System.arraycopy(EightUpperLeft(),0,Four,18,18);
        System.arraycopy(EightUpperRight(),0,Four,36,18);
        System.arraycopy(NineLowerRight(),0,Four,54,18);
    }

    private static Float[] FiveMiddle()
    {
        Float[] FiveMiddle = new Float[18];
        FiveMiddle[0] = -2.5f;
        FiveMiddle[1] = 1f;
        FiveMiddle[2] = 0f;
        FiveMiddle[3] = -1.5f;
        FiveMiddle[4] = -FiveMiddle[1];
        FiveMiddle[5] = 0f;
        FiveMiddle[6] = -FiveMiddle[0];
        FiveMiddle[7] =  -FiveMiddle[1];
        FiveMiddle[8] = 0f;

        FiveMiddle[9] = FiveMiddle[0];
        FiveMiddle[10] = FiveMiddle[1];
        FiveMiddle[11] = 0f;
        FiveMiddle[12] = -FiveMiddle[0];
        FiveMiddle[13] =  -FiveMiddle[1];
        FiveMiddle[14] = 0f;
        FiveMiddle[15] = -FiveMiddle[3];
        FiveMiddle[16] =  FiveMiddle[1];
        FiveMiddle[17] = 0f;

        return FiveMiddle;
    }

    private static void Five()
    {
        Five = new Float [90];
        System.arraycopy(ZeroTop(),0,Five,0,18);
        System.arraycopy(ZeroBottom(),0,Five,18,18);
        System.arraycopy(FiveMiddle(),0,Five,36,18);
        System.arraycopy(EightUpperLeft(),0,Five,54,18);
        System.arraycopy(EightLowerRight(),0,Five,72,18);
    }

    private static void Six()
    {
        Six = new Float [108];
        System.arraycopy(ZeroTop(),0,Six,0,18);
        System.arraycopy(EightLowerTop(),0,Six,18,18);
        System.arraycopy(EightLowerLeft(),0,Six,36,18);
        System.arraycopy(EightLowerRight(),0,Six,54,18);
        System.arraycopy(SixUpperLeft(),0,Six,72,18);
        System.arraycopy(ZeroBottom(),0,Six,90,18);
    }

    private static void Seven()
    {
        Seven = new Float [54];
        System.arraycopy(ZeroTop(),0,Seven,0,18);
        System.arraycopy(EightUpperRight(),0,Seven,18,18);
        System.arraycopy(NineLowerRight(),0,Seven,36,18);
    }

    private static Float[] EightUpperBottom()
    {
        Float[] EightUpperBottom = ZeroBottom();
        EightUpperBottom = MoveY(EightUpperBottom, 6f);
        return EightUpperBottom;
    }
    private static Float[] EightLowerTop()
    {
        Float[] EightLowerTop = ZeroTop();
        EightLowerTop = MoveY(EightLowerTop, -6f);
        return EightLowerTop;
    }

    private static Float[] EightUpperLeft()
    {
        Float[] EightUpperLeft = Rectangle(1f, 4f);
        EightUpperLeft = MoveXY(EightUpperLeft, -2f, 3f);
        return EightUpperLeft;
    }

    private static Float[] SixUpperLeft()
    {
        Float[] EightUpperLeft = Rectangle(1f, 6f);
        EightUpperLeft = MoveXY(EightUpperLeft, -2f, 2f);
        return EightUpperLeft;
    }

    private static Float[] EightUpperRight()
    {
        Float[] EightUpperRight = Rectangle(1f, 4f);
        EightUpperRight = MoveXY(EightUpperRight, 2f, 3f);
        return EightUpperRight;
    }

    private static Float[] EightLowerLeft()
    {
        Float[] EightUpperLeft = Rectangle(1f, 4f);
        EightUpperLeft = MoveXY(EightUpperLeft, -2f, -3f);
        return EightUpperLeft;
    }

    private static Float[] EightLowerRight()
    {
        Float[] EightUpperRight = Rectangle(1f, 4f);
        EightUpperRight = MoveXY(EightUpperRight, 2f, -3f);
        return EightUpperRight;
    }

    private static void Eight()
    {
        Eight = new Float [144];
        System.arraycopy(ZeroTop(),0,Eight,0,18);
        System.arraycopy(ZeroBottom(),0,Eight,18,18);
        System.arraycopy(EightUpperBottom(),0,Eight,36,18);
        System.arraycopy(EightLowerTop(),0,Eight,54,18);
        System.arraycopy(EightUpperLeft(),0,Eight,72,18);
        System.arraycopy(EightUpperRight(),0,Eight,90,18);
        System.arraycopy(EightLowerLeft(),0,Eight,108,18);
        System.arraycopy(EightLowerRight(),0,Eight,126,18);
    }

    private static Float[] NineLowerRight()
    {
        Float[] EightUpperRight = Rectangle(1f, 6f);
        EightUpperRight = MoveXY(EightUpperRight, 2f, -2f);
        return EightUpperRight;
    }

    private static void Nine()
    {
        Nine = new Float [108];
        System.arraycopy(ZeroTop(),0,Nine,0,18);
        System.arraycopy(EightUpperBottom(),0,Nine,18,18);
        System.arraycopy(EightUpperLeft(),0,Nine,36,18);
        System.arraycopy(EightUpperRight(),0,Nine,54,18);
        System.arraycopy(NineLowerRight(),0,Nine,72,18);
        System.arraycopy(ZeroBottom(),0,Nine,90,18);
    }
}
