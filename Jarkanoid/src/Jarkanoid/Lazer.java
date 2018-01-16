package Jarkanoid;

import Physics.Body;
import java.awt.*;

public class Lazer extends Body {
    public Lazer(int x, int y) {
        super(null, x, y);
        SolidPolygon shape = new SolidPolygon();
        shape.setBox(4, 10);
        setShape(shape);
        ignoresGravity = true;
        velocity.set(0, 30);
        color = Color.RED;

        bitmask = 0x01 << 4;
        interactiveBitmask = 0xFF ^ (0x01 << 2) ^ (0x01 << 3);
    }
}
