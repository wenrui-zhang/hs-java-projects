package Physics;

import java.awt.*;

public class Circle extends Shape {

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public Shape copy() {
        return new Circle(radius);
    }

    @Override
    public void init(Body b) {
        this.body = b;
        computeMass(1.0);
    }

    @Override
    public void computeMass(double density) {
        body.m = Math.PI * radius * radius * density;
        body.im = (body.m != 0) ? 1.0 / body.m : 0.0;
        body.I = body.m * radius * radius;
        body.iI = (body.I != 0) ? 1.0 / body.I : 0.0;
    }

    @Override
    public void draw(Graphics2D g2d) {
        Color color = body.color;
        g2d.setColor(color);
        g2d.drawOval(PhysicsUtils.round(body.position.x - radius),
                PhysicsUtils.round(body.position.y - radius),
                PhysicsUtils.round(radius * 2), PhysicsUtils.round(radius * 2));

    }

    @Override
    public Type getType() {
        return Type.eCircle;
    }
}
