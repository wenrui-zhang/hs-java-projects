package Jarkanoid;

import Physics.CircleShape;
import Physics.PhysicsUtils;
import Physics.Shape;
import java.awt.Color;
import java.awt.Graphics2D;

public class SolidCircle extends CircleShape{
        public SolidCircle(double radius) {
            super(radius);
        }

        @Override
        public Shape copy() {
            return new SolidCircle(radius);
        }

        @Override
        public void draw(Graphics2D g2d) {
        Color color = body.color;
        g2d.setColor(color);
        g2d.fillOval(PhysicsUtils.round(body.position.x - radius),
                PhysicsUtils.round(body.position.y - radius),
                PhysicsUtils.round(radius * 2), PhysicsUtils.round(radius * 2));
        }

}
