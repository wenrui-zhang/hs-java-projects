package Physics;

public class Collision {
    // LOL, new "LAMBDA" arguments introduced in java 8!!!
    // It is one HELL OF A WAY to play with enums XD
    private static ManifoldAction[][] actions = {
            {Collision::CircletoCircle, Collision::CircletoPolygon},
            {Collision::PolygontoCircle, Collision::PolygontoPolygon}
    };

    /* Collision::CircletoCirle simply translates to:

        new ManifoldAction() {
            @Override
            public void makeManifold(Manifold m, Body a, Body b) {
                Collision.CircletoCircle(m, a, b);
            }
        };
     */

    public static void dispatch(Manifold m, Body a, Body b) {
        actions[a.shape.getType().value][b.shape.getType().value].makeManifold(m, a, b);
    }

    public static void CircletoCircle(Manifold m, Body a, Body b) {
        CircleShape A = (CircleShape) a.shape, B = (CircleShape) b.shape;


        PhysicsUtils.Vec2 normal = new PhysicsUtils.Vec2(b.position).sub(a.position);

        double distanceSquared = normal.lengthSquared();
        double radius = A.radius + B.radius;


        if (distanceSquared >= radius * radius) {
            m.contact_count = 0;
            return;
        }

        double distance = Math.sqrt(distanceSquared);

        m.contact_count = 1;

        if (distance == 0.0) {
            m.penetration = A.radius;
            m.normal = new PhysicsUtils.Vec2(1, 0);
            m.contacts[0] = a.position;
        } else {
            m.penetration = radius - distance;
            m.normal = normal.scale(1 / distance);
            m.contacts[0] = new PhysicsUtils.Vec2(m.normal).scale(A.radius).add(a.position);
        }
    }

    public static void CircletoPolygon(Manifold m, Body a, Body b) {
        CircleShape A = (CircleShape) a.shape;
        PolygonShape B = (PolygonShape) b.shape;

        m.contact_count = 0;


        PhysicsUtils.Vec2 center = new PhysicsUtils.Vec2(a.position);
        center.sub(b.position);


        double separation = -Double.MAX_VALUE;
        int faceNormal = 0;
        for (int i = 0; i < B.vertexCount; ++i) {
            double s = PhysicsUtils.dotProduct(B.normals[i], new PhysicsUtils.Vec2(center).sub(B.vertices[i]));

            if (s > A.radius)
                return;

            if (s > separation) {
                separation = s;
                faceNormal = i;
            }
        }


        PhysicsUtils.Vec2 v1 = new PhysicsUtils.Vec2(B.vertices[faceNormal]);
        int i2 = faceNormal + 1 < B.vertexCount ? faceNormal + 1 : 0;
        PhysicsUtils.Vec2 v2 = new PhysicsUtils.Vec2(B.vertices[i2]);


        if (separation < PhysicsUtils.EPSILON) {
            m.contact_count = 1;
            m.normal = new PhysicsUtils.Vec2(B.normals[faceNormal]).scale(-1);
            m.contacts[0] = new PhysicsUtils.Vec2(m.normal).scale(A.radius).add(a.position);
            m.penetration = A.radius;
            return;
        }


        double dot1 = PhysicsUtils.dotProduct(new PhysicsUtils.Vec2(center).sub(v1), new PhysicsUtils.Vec2(v2).sub(v1));
        double dot2 = PhysicsUtils.dotProduct(new PhysicsUtils.Vec2(center).sub(v2), new PhysicsUtils.Vec2(v1).sub(v2));
        m.penetration = A.radius - separation;


        if (dot1 <= 0.0) {
            if (PhysicsUtils.distanceSquared(center, v1) > A.radius * A.radius)
                return;

            m.contact_count = 1;
            PhysicsUtils.Vec2 n = new PhysicsUtils.Vec2(v1).sub(center);
            n.normalize();
            m.normal = n;
            v1.add(b.position);
            m.contacts[0] = v1;
        } else if (dot2 <= 0.0) {
            if (PhysicsUtils.distanceSquared(center, v2) > A.radius * A.radius)
                return;

            m.contact_count = 1;
            PhysicsUtils.Vec2 n = new PhysicsUtils.Vec2(v2).sub(center);
            v2.add(b.position);
            m.contacts[0] = v2;
            n.normalize();
            m.normal = n;
        } else {
            PhysicsUtils.Vec2 n = new PhysicsUtils.Vec2(B.normals[faceNormal]);
            if (PhysicsUtils.dotProduct(new PhysicsUtils.Vec2(center).sub(v1), n) > A.radius)
                return;

            m.normal = n.scale(-1);
            m.contacts[0] = new PhysicsUtils.Vec2(m.normal).scale(A.radius).add(a.position);
            m.contact_count = 1;
        }

    }

    public static void PolygontoCircle(Manifold m, Body a, Body b) {
        CircletoPolygon(m, b, a);
        if (m.normal != null)
            m.normal.scale(-1);
    }

    public static double FindAxisLeastPenetration(int faceIndex[], PolygonShape A, PolygonShape B) {
        double bestDistance = -Double.MAX_VALUE;
        int bestIndex = -1;

        for (int i = 0; i < A.vertexCount; ++i) {

            PhysicsUtils.Vec2 n = new PhysicsUtils.Vec2(A.normals[i]);

            PhysicsUtils.Vec2 s = B.getSupport(new PhysicsUtils.Vec2(n).scale(-1));

            PhysicsUtils.Vec2 v = new PhysicsUtils.Vec2(A.vertices[i]);
            v.add(A.body.position);
            v.sub(B.body.position);


            double d = PhysicsUtils.dotProduct(n, new PhysicsUtils.Vec2(s).sub(v));


            if (d > bestDistance) {
                bestDistance = d;
                bestIndex = i;
            }
        }

        faceIndex[0] = bestIndex;
        return bestDistance;
    }

    public static void FindIncidentFace(PhysicsUtils.Vec2 v[], PolygonShape RefPoly, PolygonShape IncPoly, int referenceIndex) {
        PhysicsUtils.Vec2 referenceNormal = new PhysicsUtils.Vec2(RefPoly.normals[referenceIndex]);

        int incidentFace = 0;
        double minDot = Double.MAX_VALUE;
        for (int i = 0; i < IncPoly.vertexCount; ++i) {
            double dot = PhysicsUtils.dotProduct(referenceNormal, IncPoly.normals[i]);
            if (dot < minDot) {
                minDot = dot;
                incidentFace = i;
            }
        }


        v[0] = new PhysicsUtils.Vec2(IncPoly.vertices[incidentFace]).add(IncPoly.body.position);
        incidentFace = incidentFace + 1 >= IncPoly.vertexCount ? 0 : incidentFace + 1;
        v[1] = new PhysicsUtils.Vec2(IncPoly.vertices[incidentFace]).add(IncPoly.body.position);
    }

    public static int Clip(PhysicsUtils.Vec2 n, double c, PhysicsUtils.Vec2[] face) {
        int sp = 0;
        PhysicsUtils.Vec2[] out = {new PhysicsUtils.Vec2(face[0]), new PhysicsUtils.Vec2(face[1])};


        double d1 = PhysicsUtils.dotProduct(n, face[0]) - c;
        double d2 = PhysicsUtils.dotProduct(n, face[1]) - c;


        if (d1 <= 0.0) out[sp++] = face[0];
        if (d2 <= 0.0) out[sp++] = face[1];


        if (d1 * d2 < 0.0) {

            double alpha = d1 / (d1 - d2);
            out[sp] = new PhysicsUtils.Vec2(face[0]).add((new PhysicsUtils.Vec2(face[1]).sub(face[0])).scale(alpha));
            ++sp;
        }


        face[0] = out[0];
        face[1] = out[1];

        assert (sp != 3);

        return sp;
    }

    public static void PolygontoPolygon(Manifold m, Body a, Body b) {
        PolygonShape A = (PolygonShape) a.shape;
        PolygonShape B = (PolygonShape) b.shape;
        m.contact_count = 0;


        int[] faceA = new int[1];
        double penetrationA = FindAxisLeastPenetration(faceA, A, B);
        if (penetrationA >= 0.0)
            return;


        int faceB[] = new int[1];
        double penetrationB = FindAxisLeastPenetration(faceB, B, A);
        if (penetrationB >= 0.0)
            return;

        int referenceIndex;
        boolean flip;

        PolygonShape RefPoly;
        PolygonShape IncPoly;


        if (PhysicsUtils.biasGreaterThan(penetrationA, penetrationB)) {
            RefPoly = A;
            IncPoly = B;
            referenceIndex = faceA[0];
            flip = false;
        } else {
            RefPoly = B;
            IncPoly = A;
            referenceIndex = faceB[0];
            flip = true;
        }


        PhysicsUtils.Vec2 incidentFace[] = new PhysicsUtils.Vec2[2];
        FindIncidentFace(incidentFace, RefPoly, IncPoly, referenceIndex);


        PhysicsUtils.Vec2 v1 = new PhysicsUtils.Vec2(RefPoly.vertices[referenceIndex]);
        referenceIndex = referenceIndex + 1 == RefPoly.vertexCount ? 0 : referenceIndex + 1;
        PhysicsUtils.Vec2 v2 = new PhysicsUtils.Vec2(RefPoly.vertices[referenceIndex]);


        v1.add(RefPoly.body.position);
        v2.add(RefPoly.body.position);


        PhysicsUtils.Vec2 sidePlaneNormal = (new PhysicsUtils.Vec2(v2).sub(v1));
        sidePlaneNormal.normalize();


        PhysicsUtils.Vec2 refFaceNormal = new PhysicsUtils.Vec2(sidePlaneNormal.y, -sidePlaneNormal.x);


        double refC = PhysicsUtils.dotProduct(refFaceNormal, v1);
        double negSide = -PhysicsUtils.dotProduct(sidePlaneNormal, v1);
        double posSide = PhysicsUtils.dotProduct(sidePlaneNormal, v2);


        if (Clip(new PhysicsUtils.Vec2(sidePlaneNormal).scale(-1), negSide, incidentFace) < 2)
            return;

        if (Clip(sidePlaneNormal, posSide, incidentFace) < 2)
            return;


        m.normal = flip ? new PhysicsUtils.Vec2(refFaceNormal).scale(-1) : refFaceNormal;


        int cp = 0;
        double separation = PhysicsUtils.dotProduct(refFaceNormal, incidentFace[0]) - refC;
        if (separation <= 0.0) {
            m.contacts[cp] = incidentFace[0];
            m.penetration = -separation;
            ++cp;
        } else
            m.penetration = 0;

        separation = PhysicsUtils.dotProduct(refFaceNormal, incidentFace[1]) - refC;
        if (separation <= 0.0f) {
            m.contacts[cp] = incidentFace[1];

            m.penetration += -separation;
            ++cp;


            m.penetration /= (double) cp;
        }

        m.contact_count = cp;

        m.normal.normalize(); /// DAYUM<<< THIS BUG!!! IT MADE ME USE 2 HOURS TO FIND and FIX 1 LINE !!!
    }

    interface ManifoldAction {
        void makeManifold(Manifold m, Body a, Body b);
    }
}
