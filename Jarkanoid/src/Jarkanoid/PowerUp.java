package Jarkanoid;

import Physics.Body;
import Physics.PhysicsUtils;

import java.awt.*;

public class PowerUp extends Body {
    private int type;
    private Color[] colors = {Color.RED, Color.GREEN, new Color(200, 200, 200), Color.ORANGE, Color.BLUE, Color.PINK, Color.cyan};

    public PowerUp(int x, int y) {
        super(null, x, y);
        SolidPolygon shape = new SolidPolygon();
        shape.setBox(20, 20);
        setShape(shape);

        type = PhysicsUtils.round(PhysicsUtils.random(0, 6));
        if (type == 5) {
            type = PhysicsUtils.round(PhysicsUtils.random(0, 6));
        }
        color = colors[type];

        bitmask = 0x01 << 2;
        interactiveBitmask = 0xFF ^ (0x01 << 1) ^ (0x01 << 3) ^ (0x01 << 4);
    }

    public int getType() {
        return type;
    }
}
