package Physics;

import java.awt.*;

public class PolygonShape extends Shape {
    protected int vertexCount;
    protected PhysicsUtils.Vec2[] vertices = new PhysicsUtils.Vec2[MaxPolyVertexCount];
    protected PhysicsUtils.Vec2[] normals = new PhysicsUtils.Vec2[MaxPolyVertexCount];

    public PolygonShape() {
    }

    @Override
    public Shape copy() {
        PolygonShape poly = new PolygonShape();
        for (int i = 0; i < vertexCount; ++i) {
            poly.vertices[i] = vertices[i];
            poly.normals[i] = normals[i];
        }
        poly.vertexCount = vertexCount;
        return poly;
    }

    @Override
    public void init(Body b) {
        body = b;
        computeMass(1.0);
    }

    @Override
    public void computeMass(double density) {
        // Calculate centroid and moment of interia
        PhysicsUtils.Vec2 c = new PhysicsUtils.Vec2(0.0, 0.0); // centroid
        double area = 0.0;
        double I = 0.0;
        final double k_inv3 = 1.0 / 3.0;

        for (int i1 = 0; i1 < vertexCount; ++i1) {
            // Triangle vertices, third vertex implied as (0, 0)
            PhysicsUtils.Vec2 p1 = new PhysicsUtils.Vec2(vertices[i1]);
            int i2 = i1 + 1 < vertexCount ? i1 + 1 : 0;
            PhysicsUtils.Vec2 p2 = new PhysicsUtils.Vec2(vertices[i2]);

            double D = PhysicsUtils.crossProduct(p1, p2);
            double triangleArea = 0.5 * D;

            area += triangleArea;

            // Use area to weight the centroid average, not just vertex position
            c.add((new PhysicsUtils.Vec2(p1).add(p2)).scale(k_inv3).scale(triangleArea));

            double intx2 = p1.x * p1.x + p2.x * p1.x + p2.x * p2.x;
            double inty2 = p1.y * p1.y + p2.y * p1.y + p2.y * p2.y;
            I += (0.25 * k_inv3 * D) * (intx2 + inty2);
        }

        c.scale(1.0 / area);

        for (int i = 0; i < vertexCount; ++i)
            vertices[i].sub(c);

        body.m = density * area;
        body.im = (body.m != 0) ? 1.0 / body.m : 0.0;
        body.I = I * density;
        body.iI = (body.I != 0) ? 1.0 / body.I : 0.0;
    }

    @Override
    public void draw(Graphics2D g2d) {
        Color color = body.color;
        g2d.setColor(color);
        drawPolygon(g2d);

    }

    public void setColor(Color c) {
        body.color = c;
    }

    public void drawPolygon(Graphics2D g2d) {
        PhysicsUtils.Vec2 vOriginal = null;
        PhysicsUtils.Vec2 vPrev = null;
        for (int i = 0; i < vertexCount; ++i) {
            PhysicsUtils.Vec2 v = new PhysicsUtils.Vec2(body.position).add(vertices[i]);
            if (vOriginal == null)
                vOriginal = new PhysicsUtils.Vec2(v);
            if (vPrev == null) {
                vPrev = new PhysicsUtils.Vec2(v);
                continue;
            }
            g2d.drawLine(PhysicsUtils.round(vPrev.x), PhysicsUtils.round(vPrev.y), PhysicsUtils.round(v.x), PhysicsUtils.round(v.y));
            vPrev = new PhysicsUtils.Vec2(v);
        }
        g2d.drawLine(PhysicsUtils.round(vPrev.x), PhysicsUtils.round(vPrev.y), PhysicsUtils.round(vOriginal.x), PhysicsUtils.round(vOriginal.y));

//        g2d.setColor(Color.GREEN);
        g2d.fillOval((int) body.position.x - 2, (int) body.position.y - 2, 4, 4);
    }
    
    public void fillPolygon(Graphics2D g2d) {
        int[] polyX = new int[vertexCount], polyY = new int[vertexCount];
        for (int i = 0; i < vertexCount; ++i) {
            PhysicsUtils.Vec2 v = new PhysicsUtils.Vec2(body.position).add(vertices[i]);
            polyX[i] = PhysicsUtils.round(v.x);
            polyY[i] = PhysicsUtils.round(v.y);
        }
        g2d.fillPolygon(polyX, polyY, vertexCount);
    }

    @Override
    public Type getType() {
        return Type.ePoly;
    }

    public void setBox(double hw, double hh) {
        vertexCount = 4;
        vertices[0] = new PhysicsUtils.Vec2(-hw / 2, -hh / 2);
        vertices[1] = new PhysicsUtils.Vec2(hw / 2, -hh / 2);
        vertices[2] = new PhysicsUtils.Vec2(hw / 2, hh / 2);
        vertices[3] = new PhysicsUtils.Vec2(-hw / 2, hh / 2);
        normals[0] = new PhysicsUtils.Vec2(0.0f, -1.0f);
        normals[1] = new PhysicsUtils.Vec2(1.0f, 0.0f);
        normals[2] = new PhysicsUtils.Vec2(0.0f, 1.0f);
        normals[3] = new PhysicsUtils.Vec2(-1.0f, 0.0f);
    }

    public void set(PhysicsUtils.Vec2[] vertices, int count) {
        assert (count > 2 && count <= MaxPolyVertexCount);
        count = Math.min(count, MaxPolyVertexCount);

        int rightMost = 0;
        double highestXCoord = vertices[0].x;
        for (int i = 1; i < count; ++i) {
            double x = vertices[i].x;
            if (x > highestXCoord) {
                highestXCoord = x;
                rightMost = i;
            }

            else if (x == highestXCoord)
                if (vertices[i].y < vertices[rightMost].y)
                    rightMost = i;
        }

        int[] hull = new int[MaxPolyVertexCount];
        int outCount = 0;
        int indexHull = rightMost;

        while (true) {
            hull[outCount] = indexHull;

            int nextHullIndex = 0;
            for (int i = 1; i < count; ++i) {
                if (nextHullIndex == indexHull) {
                    nextHullIndex = i;
                    continue;
                }

                PhysicsUtils.Vec2 e1 = new PhysicsUtils.Vec2(vertices[nextHullIndex]).sub(vertices[hull[outCount]]);
                PhysicsUtils.Vec2 e2 = new PhysicsUtils.Vec2(vertices[i]).sub(vertices[hull[outCount]]);
                double c = PhysicsUtils.crossProduct(e1, e2);
                if (c < 0.0f)
                    nextHullIndex = i;

                if (c == 0.0f && e2.lengthSquared() > e1.lengthSquared())
                    nextHullIndex = i;
            }

            ++outCount;
            indexHull = nextHullIndex;

            if (nextHullIndex == rightMost) {
                vertexCount = outCount;
                break;
            }
        }

        for (int i = 0; i < vertexCount; ++i)
            this.vertices[i] = vertices[hull[i]];

        for (int i1 = 0; i1 < vertexCount; ++i1) {
            int i2 = i1 + 1 < vertexCount ? i1 + 1 : 0;
            PhysicsUtils.Vec2 face = new PhysicsUtils.Vec2(this.vertices[i2]).sub(this.vertices[i1]);

            assert (face.lengthSquared() > PhysicsUtils.EPSILON * PhysicsUtils.EPSILON);

            normals[i1] = new PhysicsUtils.Vec2(face.y, -face.x);
            normals[i1].normalize();
        }
    }

    public PhysicsUtils.Vec2 getSupport(PhysicsUtils.Vec2 dir) {
        double bestProjection = -Double.MAX_VALUE;
        PhysicsUtils.Vec2 bestVertex = null;

        for (int i = 0; i < vertexCount; ++i) {
            PhysicsUtils.Vec2 v = vertices[i];
            double projection = PhysicsUtils.dotProduct(v, dir);

            if (projection > bestProjection) {
                bestVertex = v;
                bestProjection = projection;
            }
        }

        return new PhysicsUtils.Vec2(bestVertex);
    }


}
