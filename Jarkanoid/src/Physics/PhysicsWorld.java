package Physics;

import Jarkanoid.Ball;

import java.awt.*;
import java.util.ArrayList;

public class PhysicsWorld {
    protected double dt;
    protected PhysicsUtils.Vec2 gravity = new PhysicsUtils.Vec2(0, 0);
    protected int iterations;
    protected ArrayList<Body> bodies = new ArrayList<>();
    protected ArrayList<Manifold> contacts = new ArrayList<>();
    protected PhysicsWorldInterface worldInterface = null;

    public PhysicsWorld(double dt, int iterations) {
        this.dt = dt;
        this.iterations = iterations;
    }

    public double getDeltaTime() {
        return dt;
    }

    public void setInterface(PhysicsWorldInterface inter) {
        worldInterface = inter;
    }

    public void setGravity(PhysicsUtils.Vec2 v) {
        this.gravity = v;
    }

    public void integrateForces(Body b, double dt) {
        if (b.im == 0.0)
            return;

        b.velocity.add((new PhysicsUtils.Vec2(b.force).scale(b.im).add(gravity)).scale(dt / 2.0));
    }

    public void integrateVelocity(Body b, double dt) {
        if (b.im == 0.0)
            return;

        b.position.add(new PhysicsUtils.Vec2(b.velocity).scale(dt));
        if (!b.ignoresGravity)
            integrateForces(b, dt);
    }

    public void step() {
        synchronized (bodies) {
            contacts.clear();
            for (int i = 0; i < bodies.size(); ++i) {
                Body A = bodies.get(i);

                for (int j = i + 1; j < bodies.size(); ++j) {
                    Body B = bodies.get(j);
                    if (A.im == 0 && B.im == 0)
                        continue;
                    if (A.sleep || B.sleep)
                        continue;
                    if (!A.shouldInteract(B))
                        continue;
                    Manifold m = new Manifold(A, B);
                    m.solve();
                    if (m.contact_count != 0)
                        contacts.add(m);
                }
            }

            // Integrate forces
            for (Body body : bodies)
                if (!body.sleep && !body.ignoresGravity)
                    integrateForces(body, dt);

            // Initialize collision
            for (Manifold contact : contacts)
                contact.Initialize(dt, gravity);

            // Solve collisions
            for (int j = 0; j < iterations; j++)
                for (Manifold contact : contacts) contact.ApplyImpulse();

            // Integrate velocities
            for (Body body : bodies)
                if (!body.sleep) {
                    integrateVelocity(body, dt);
                }

            // Correct positions
            for (Manifold contact : contacts) contact.PositionalCorrection();

            for (Manifold contact : contacts) {
                if (worldInterface != null)
                    worldInterface.onCollision(contact.A, contact.B);
            }
            // Clear all forces
            for (Body b : bodies) {
                b.force.set(0, 0);
            }
        }
    }

    public void render(Graphics2D g2d) {
        synchronized (bodies) {
            for (Body b : bodies) {
                b.shape.draw(g2d);
            }

/*            g2d.setColor(Color.RED);
            for (Manifold m : contacts) {
                for (int j = 0; j < m.contact_count; ++j) {
                    PhysicsUtils.Vec2 c = m.contacts[j];
                    g2d.fillOval((int) c.x - 2, (int) c.y - 2, 4, 4);
                }
            }

            g2d.setColor(Color.GREEN);
            for (Manifold m : contacts) {
                PhysicsUtils.Vec2 n = m.normal;
                for (int j = 0; j < m.contact_count; ++j) {
                    PhysicsUtils.Vec2 c = m.contacts[j];
                    g2d.drawLine((int) c.x, (int) c.y, (int) (c.x + (n.x * 1.75)), (int) (c.y + (n.y * 1.75)));
                }
            }*/
        }
    }

    public Body add(Shape shape, int x, int y) {
        synchronized (bodies) {
            Body b = new Body(shape, x, y);
            bodies.add(b);
            return b;
        }
    }

    public void add(Body b) {
        synchronized (bodies) {
            bodies.add(b);
        }
    }

    public void remove(Body b) {
        synchronized (bodies) {
            bodies.remove(b);
        }
    }

    public <T extends Body> boolean containsType(Class<T> c) {
        for (Body b : bodies) {
            if (b instanceof Ball) {
                System.out.println("true");
            }
            if (c.isInstance(b)) {
                System.out.println("true-");
                return true;
            }
        }
        System.out.println("false");
        return false;
    }
}
