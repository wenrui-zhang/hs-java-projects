package Jarkanoid;

import Physics.Body;
import Physics.PolygonShape;

import java.awt.*;

public class Brick extends Body {

    
    private int HP;
    private Color[] colors = {Color.WHITE, Color.GREEN, new Color(160,32,240), Color.BLUE, Color.YELLOW, Color.RED, Color.GRAY};

    public Brick(int x, int y, double width, double height, int HP) {
        super(null, x, y);
        SolidPolygon shape = new SolidPolygon();
        shape.setBox(width, height);
        setShape(shape);
        setStatic();
        this.restitution = 1;

        if (HP > 7) {
            this.HP = 7;
        } else if (HP < 1) {
            this.HP = 1;
        } else {
            this.HP = HP;
        }
        this.HP = HP;
        shape.setColor(colors[HP]);

        bitmask = 0x01 << 1;
        interactiveBitmask = 0xFF ^ (0x01 << 2);
    }

    public boolean isDead() {
        return HP < 1;
    }

    public void reduceHP() {
        if (HP - 1 < 0)
            return;
        HP--;
        ((PolygonShape) shape).setColor(colors[HP]);
    }

}
