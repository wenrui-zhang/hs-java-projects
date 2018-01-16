package Jarkanoid;

import Physics.PolygonShape;
import java.awt.Color;
import java.awt.Graphics2D;

public class SolidPolygon extends PolygonShape{

        @Override
        public Physics.Shape copy() {
            SolidPolygon poly = new SolidPolygon();
            for (int i = 0; i < vertexCount; ++i) {
                poly.vertices[i] = vertices[i];
                poly.normals[i] = normals[i];
            }
            poly.vertexCount = vertexCount;
            return poly;
        }

        @Override
        public void draw(Graphics2D g2d) {
            Color color = body.color;
            g2d.setColor(color);
            fillPolygon(g2d);
        }
    }