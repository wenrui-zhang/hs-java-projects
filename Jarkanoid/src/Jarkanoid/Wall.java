package Jarkanoid;

import Physics.Body;
import Physics.PolygonShape;

public class Wall extends Body {
    public Wall(int x, int y, int width, int height) {
        super(null, x, y);
        PolygonShape p = new PolygonShape();
        p.setBox(width, height);
        setShape(p);
        setStatic();
        restitution = 1.0;
    }
}
