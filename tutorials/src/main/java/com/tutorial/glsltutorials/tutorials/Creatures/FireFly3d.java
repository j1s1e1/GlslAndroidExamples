package com.tutorial.glsltutorials.tutorials.Creatures;

import com.tutorial.glsltutorials.tutorials.Colors;
import com.tutorial.glsltutorials.tutorials.Geometry.Vector3f;
import com.tutorial.glsltutorials.tutorials.Shapes.LitMatrixSphere2;

/**
 * Created by jamie on 6/13/15.
 */
public class FireFly3d extends Bug3d {
    int flash = 10;
    int flash_count = 0;
    LitMatrixSphere2[] body;

    float sizef;
    float wingOffset = 0f;

    public FireFly3d()
    {
        this(0, 0, 0);
    }

    public FireFly3d(int x_in, int y_in, int z_in)
    {
        super(x_in, y_in, z_in);
        scale = 0.002f;
        body = new LitMatrixSphere2[7];
        sizef = size * scale;
        body[0] = new LitMatrixSphere2(sizef, 2);
        body[1] = new LitMatrixSphere2(sizef, 2);
        body[2] = new LitMatrixSphere2(sizef, 2);
        body[3] = new LitMatrixSphere2(sizef, 2);
        body[4] = new LitMatrixSphere2(2 * sizef, 2);
        body[5] = new LitMatrixSphere2(2 * sizef, 2);
        body[6] = new LitMatrixSphere2(sizef, 2);
        body[0].setColor(Colors.SADDLE_BROWN_COLOR);
        body[1].setColor(Colors.SADDLE_BROWN_COLOR);
        body[2].setColor(Colors.SADDLE_BROWN_COLOR);
        body[3].setColor(Colors.FLASH_COLOR);
        body[4].setColor(Colors.SADDLE_BROWN_COLOR);
        body[5].setColor(Colors.SADDLE_BROWN_COLOR);
        body[6].setColor(Colors.SADDLE_BROWN_COLOR);
        // FIXME body[3].LighterColor(15f);
        setOffsets();
    }

    private void setOffsets()
    {
        body[0].setOffset(position.add(new Vector3f(-sizef - scale * 1, 0f, 0f)));
        body[1].setOffset(position);
        body[2].setOffset(position.add(new Vector3f(+sizef, 0f, 0f)));
        body[3].setOffset(position.add(new Vector3f(2 * sizef, 0f, 0f)));
        body[4].setOffset(position.add(new Vector3f(+sizef - wingOffset, -wingOffset, 0f)));
        body[5].setOffset(position.add(new Vector3f(+sizef - wingOffset, +wingOffset, 0f)));
        body[6].setOffset(position.add(new Vector3f(2*sizef, 0f, 0f)));

        body[4].setAngles(0, 0, -wingOffset * 10);
        body[5].setAngles(80, 0, wingOffset * 10);
    }

    public void SetAutomove()
    {
        autoMove = true;
    }

    public void ClearAutomove()
    {
        autoMove = false;
    }

    public void draw()
    {
        body[0].draw();
        body[1].draw();
        body[2].draw();

        if (flash_count == 0)
        {
            flash = random.nextInt(100);
            if (flash < 3) {
                body[3].draw();
                flash_count = 10 + random.nextInt(20);
            } else {
                body[6].draw();
            }
        } else {
            flash_count--;
            body[3].draw();
        }

        body[4].drawSemi(0, 5);
        body[5].drawSemi(0, 5);

        if (wing_step < 3) {
            wing_step++;
        } else {
            wing_step = 0;
        }
        wingOffset = wing_step * scale * 5;
        setOffsets();
        if (autoMove)
        {
            move();
        }
    }

    public void setProgram(int program)
    {
        super.setProgram(program);
        for(LitMatrixSphere2 l : body)
        {
            l.setProgram(program);
        }
    }
}
