package Physics;

import java.awt.*;

public class Body {
    public PhysicsUtils.Vec2 position;
    public PhysicsUtils.Vec2 velocity;

    public PhysicsUtils.Vec2 force;

    public double I;  // moment of inertia
    public double iI; // inverse inertia
    public double m;  // mass
    public double im; // inverse mass

    public double restitution;

    // Shape interface
    public Shape shape;

    public Color color = Color.WHITE;

    public boolean sleep = false;
    public boolean ignoresGravity = false;

    public int bitmask = 0x01;
    public int interactiveBitmask = 0xFF;

    public Body(Shape shape, int x, int y) {

        position = new PhysicsUtils.Vec2(x, y);
        velocity = new PhysicsUtils.Vec2(0, 0);
        force = new PhysicsUtils.Vec2(0, 0);
        restitution = 1.0;

        if (shape != null) {
            this.shape = shape.copy();
            shape.body = this;
            this.shape.init(this);
        }
        float r = (float) PhysicsUtils.random(0.2, 1.0);
        float g = (float) PhysicsUtils.random(0.2, 1.0);
        float b = (float) PhysicsUtils.random(0.2, 1.0);

        color = new Color(r, g, b);
    }

    public void setShape(Shape shape) {
        this.shape = shape.copy();
        shape.body = this;
        this.shape.init(this);
    }

    public void applyForce(PhysicsUtils.Vec2 f) {
        force.add(f);
    }

    public void applyImpulse(PhysicsUtils.Vec2 impulse) {
        velocity.add(new PhysicsUtils.Vec2(impulse).scale(im));
    }

    public boolean shouldInteract(Body b) {
        return (interactiveBitmask & b.bitmask) > 0;
    }

    public void setStatic() {
        I = 0.0f;
        iI = 0.0f;
        m = 0.0f;
        im = 0.0f;
    }

    public void setDynamic() {
        if (shape != null)
            shape.computeMass(1.0);
    }
}
