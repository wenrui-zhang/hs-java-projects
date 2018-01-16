package Physics;

public class Manifold {
    public Body A, B;
    public double penetration;                                          // Depth of penetration from collision
    public PhysicsUtils.Vec2 normal;                                    // From A to B
    public PhysicsUtils.Vec2[] contacts = new PhysicsUtils.Vec2[2];     // Points of contact during collision
    public int contact_count;                                           // Number of contacts that occured during collision
    public double e;                                                    // Mixed restitution


    public Manifold(Body a, Body b) {
        A = a;
        B = b;
    }

    public void solve() {
        Collision.dispatch(this, A, B);
    }

    public void Initialize(double dt, PhysicsUtils.Vec2 gravity) {
        e = Math.min(A.restitution, B.restitution);

        for (int i = 0; i < contact_count; ++i) {
            PhysicsUtils.Vec2 ra = new PhysicsUtils.Vec2(contacts[i]).sub(A.position);
            PhysicsUtils.Vec2 rb = new PhysicsUtils.Vec2(contacts[i]).sub(B.position);

            PhysicsUtils.Vec2 rv = new PhysicsUtils.Vec2(B.velocity)
                    .sub(A.velocity);


            // Determine if we should perform a resting collision or not
            // if the only thing moving this object is gravity,
            // then the collision should be performed without any restitution
            if (rv.lengthSquared() < (new PhysicsUtils.Vec2(gravity).scale(dt))
                    .lengthSquared() + PhysicsUtils.EPSILON)
                e = 0.0;
        }
    }

    public void ApplyImpulse() {
        // Early out and positional correct if both objects have infinite mass
        // infinate mass means static object!! and they dont move
        if (PhysicsUtils.equals(A.im + B.im, 0)) {
            InfiniteMassCorrection();
            return;
        }

        for (int i = 0; i < contact_count; ++i) {
            PhysicsUtils.Vec2 ra = new PhysicsUtils.Vec2(contacts[i]).sub(A.position);
            PhysicsUtils.Vec2 rb = new PhysicsUtils.Vec2(contacts[i]).sub(B.position);

            PhysicsUtils.Vec2 rv = new PhysicsUtils.Vec2(B.velocity)
                    .sub(A.velocity);

            double contactVel = PhysicsUtils.dotProduct(rv, normal);

            if (contactVel > 0)
                return;

            double raCrossN = PhysicsUtils.crossProduct(ra, normal);
            double rbCrossN = PhysicsUtils.crossProduct(rb, normal);
            double invMassSum = A.im + B.im + PhysicsUtils.square(raCrossN) * A.iI + PhysicsUtils.square(rbCrossN) * B.iI;

            double j = -(1.0 + e) * contactVel;
            j /= invMassSum;
            j /= (double) contact_count;

            PhysicsUtils.Vec2 impulse = new PhysicsUtils.Vec2(normal).scale(j);
            B.applyImpulse(impulse);
            A.applyImpulse(new PhysicsUtils.Vec2(impulse).scale(-1));

            rv = new PhysicsUtils.Vec2(B.velocity)
                    .sub(A.velocity);

            PhysicsUtils.Vec2 t = new PhysicsUtils.Vec2(rv).sub(new PhysicsUtils.Vec2(normal).scale(PhysicsUtils.dotProduct(rv, normal)));
            t.normalize();

            // Apply friction impulse
            B.applyImpulse(t);
            A.applyImpulse(new PhysicsUtils.Vec2(t).scale(-1));
        }
    }

    public void PositionalCorrection() {
        final double k_slop = 0.05f; // Penetration allowance
        final double percent = 0.4f; // Penetration percentage to correct

        PhysicsUtils.Vec2 correction = normal.scale(Math.max(penetration - k_slop, 0.0f) / (A.im + B.im)).scale(percent);
        A.position.sub(new PhysicsUtils.Vec2(correction).scale(A.im));
        B.position.add(new PhysicsUtils.Vec2(correction).scale(B.im));
    }

    public void InfiniteMassCorrection() {
        A.velocity.set(0, 0);
        B.velocity.set(0, 0);
    }

}
