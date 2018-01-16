package Physics;

import java.awt.*;

public abstract class Shape {
    public final int MaxPolyVertexCount = 64;
    protected Body body;
    protected double radius;

    public abstract Shape copy();

    public abstract void init(Body b);

    public abstract void computeMass(double density);

    public abstract void draw(Graphics2D g2d);

    public abstract Type getType();

    public enum Type {
        eCircle(0),
        ePoly(1),
        eCount(2);

        public int value;

        Type(int value) {
            this.value = value;
        }

    }

}
