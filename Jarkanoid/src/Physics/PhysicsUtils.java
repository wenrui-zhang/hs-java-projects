package Physics;

import java.util.Random;

public class PhysicsUtils {
    public static final double EPSILON = 0.0001;
    private static Random r = new Random();

    private PhysicsUtils() {

    }

    public static Vec2 multiply(double s, Vec2 v) {
        return new Vec2(s * v.x, s * v.y);
    }

    public static Vec2 min(Vec2 a, Vec2 b) {
        return new Vec2(Math.min(a.x, b.x), Math.min(a.y, b.y));
    }

    public static Vec2 max(Vec2 a, Vec2 b) {
        return new Vec2(Math.max(a.x, b.x), Math.max(a.y, b.y));
    }

    public static double dotProduct(Vec2 a, Vec2 b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double distanceSquared(Vec2 a, Vec2 b) {
        Vec2 c = new Vec2(a).sub(b);
        return dotProduct(c, c);
    }

    public static double crossProduct(Vec2 a, Vec2 b) {
        return a.x * b.y - a.y * b.x;
    }

    public static Vec2 crossProduct(Vec2 v, double a) {
        return new Vec2(a * v.y, -a * v.x);
    }

    public static Vec2 crossProduct(double a, Vec2 v) {
        return new Vec2(-a * v.y, a * v.x);
    }

    public static boolean equals(double a, double b) {
        return Math.abs(a - b) <= EPSILON;
    }

    public static double square(double a) {
        return a * a;
    }

    public static double clamp(double min, double max, double a) {
        if (a < min) return min;
        if (a > max) return max;
        return a;
    }

    public static int round(double a) {
        return (int) (a + 0.5);
    }

    public static double random(double min, double max) {
        if (min == -Double.MAX_VALUE) min = -Double.MAX_VALUE + 1;
        if (max == Double.MAX_VALUE) max = Double.MAX_VALUE - 1;
        return min + (max - min) * r.nextDouble();
    }


    public static boolean biasGreaterThan(double a, double b) {
        final double k_biasRelative = 0.95;
        final double k_biasAbsolute = 0.01;
        return a >= (b * k_biasRelative + a * k_biasAbsolute);
    }

    public static class Vec2 {

        public double x, y;

        public Vec2() {
            this(0, 0);
        }

        public Vec2(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Vec2(Vec2 v) {
            this(v.x, v.y);
        }

        public void set(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Vec2 sub(double v) {
            return sub(new Vec2(v, v));
        }

        public Vec2 sub(Vec2 v) {
            this.x -= v.x;
            this.y -= v.y;
            return this;
        }

        public Vec2 add(double v) {
            return add(new Vec2(v, v));
        }

        public Vec2 add(Vec2 v) {
            this.x += v.x;
            this.y += v.y;
            return this;
        }

        public Vec2 scale(double s) {
            return scale(new Vec2(s, s));
        }

        public Vec2 scale(Vec2 v) {
            x *= v.x;
            y *= v.y;
            return this;
        }

        public double lengthSquared() {
            return x * x + y * y;
        }

        public double length() {
            return Math.sqrt(x * x + y * y);
        }

        public void rotate(double radians) {
            double c = java.lang.Math.cos(radians);
            double s = java.lang.Math.sin(radians);

            x = x * c - y * s;
            y = x * s + y * c;
        }

        public void normalize() {
            double length = length();
            if (length > EPSILON) {
                double inverseLength = 1 / length;
                x *= inverseLength;
                y *= inverseLength;
            }
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }
    }

}
