package Jarkanoid;

import Physics.Body;

public class Ball extends Body {
    double radius;
    

    public Ball(int x, int y, double radius) {
        super(null, x, y);
        SolidCircle c = new SolidCircle(radius);
        setShape(c);

        restitution = 1.0;
        this.radius = radius;
        ignoresGravity = true;

        bitmask = 0x01 << 3;
        interactiveBitmask = 0xFF ^ (0x01 << 2)^ (0x01 << 4);
    }
}
