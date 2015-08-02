package com.tutorial.glsltutorials.tutorials.Creatures;

import com.tutorial.glsltutorials.tutorials.BodyParts.Leg;
import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Movement.BugMovement3D;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;

import java.util.ArrayList;

/**
 * Created by jamie on 7/26/15.
 */
public class Scorpion extends Bug3d {
    ArrayList<Leg> legs;
    Leg tail;
    LitMatrixSphere2 body;
    int crawlCount = 0;
    Vector3f currentMove = new Vector3f();
    Vector3f offset = new Vector3f();

    private void addLeg(Vector3f offset, Vector3f[] rotationAxes, float[] rotationAngles, int crawlCountOffset)
    {
        Leg leg = new Leg(3);
        leg.scale(new Vector3f(0.05f, 0.1f, 0.05f));
        leg.rotateAngles(rotationAxes, rotationAngles);
        leg.move(offset);
        leg.setCrawlCountOffset(crawlCountOffset);
        legs.add(leg);
    }

    public Scorpion()
    {
        this(0, 0, 0);
    }

    public Scorpion(int x_in, int y_in, int z_in)
    {
        super(x_in, y_in, z_in);
        Vector3f[] rotationAxes = new Vector3f[]{Vector3f.UnitX, Vector3f.UnitX, Vector3f.UnitX};
        float[] rotationAngles = new float[]{-135f, -45f, 0f};
        legs = new ArrayList<>();
        addLeg(new Vector3f(-0.2f, 0f, 0.6f), rotationAxes, rotationAngles, 0);
        addLeg(new Vector3f(-0.1f, 0f, 0.6f), rotationAxes, rotationAngles, 4);
        addLeg(new Vector3f(0.1f, 0f, 0.6f), rotationAxes, rotationAngles, 0);
        addLeg(new Vector3f(0.2f, 0f, 0.6f), rotationAxes, rotationAngles, 4);

        rotationAngles = new float[]{135f, 45f, 0f};
        addLeg(new Vector3f(-0.2f, 0f, 0.4f), rotationAxes, rotationAngles, 4);
        addLeg(new Vector3f(-0.1f, 0f, 0.4f), rotationAxes, rotationAngles, 0);
        addLeg(new Vector3f(0.1f, 0f, 0.4f), rotationAxes, rotationAngles, 4);
        addLeg(new Vector3f(0.2f, 0f, 0.4f), rotationAxes, rotationAngles, 0);

        tail = new Leg(5);
        tail.scale(new Vector3f(0.05f, 0.1f, 0.05f));
        tail.move(new Vector3f(-0.2f, 0.1f, 0.5f));
        rotationAxes = new Vector3f[]{Vector3f.UnitZ, Vector3f.UnitZ, Vector3f.UnitZ, Vector3f.UnitZ, Vector3f.UnitZ};
        rotationAngles = new float[]{-180f, -210f, -240f, -270f, -300f};
        tail.rotateAngles(rotationAxes, rotationAngles);

        body = new LitMatrixSphere2(0.1f);
        body.scale(new Vector3f(2.0f, 0.55f, 1.0f));
        body.move(new Vector3f(0f, 0.1f, 0.5f));
        body.setColor(Colors.RED_COLOR);
        movement = new BugMovement3D(new Vector3f(0.02f, 0.02f, 0.02f));
        movement.setLimits(new Vector3f(-0.6f, -0.6f, -0.6f), new Vector3f(0.6f, -0.4f, 0.6f));
    }

    public void draw()
    {
        for (Leg l : legs) {
            l.draw();
        }
        tail.draw();
        body.draw();
        if (autoMove)
        {
            crawlCount++;
            if (crawlCount > 31)
            {
                crawlCount = 0;
            }
            for (Leg l : legs)
            {
                l.move(currentMove);
                l.crawl(crawlCount / 4);
            }
            tail.move(currentMove);
            tail.crawl(crawlCount / 4);
            body.move(currentMove);
            move();
            currentMove = position.sub(lastPosition);
            offset = offset.add(currentMove);
        }
    }

    public void setProgram(int program)
    {
        super.setProgram(program);
        for(Leg l : legs)
        {
            l.setProgram(program);
        }
        tail.setProgram(program);
        body.setProgram(program);
    }

    public String getInfo()
    {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append("Scorpion Position = " + position.toString() + "\n");
        result.append("Scorpion Auto Move = " + String.valueOf(autoMove) + "\n");
        result.append("Scorpion Body Position = " + String.valueOf(body.getOffset()) + "\n");
        result.append("Scorpion offset = " + String.valueOf(offset) + "\n");
        result.append("\n");
        return result.toString();
    }
}
