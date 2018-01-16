package Jarkanoid;

import Physics.PhysicsUtils;
import Physics.PhysicsWorld;

import java.util.ArrayList;

public class PowerUpManager {
    private Platform platform;
    private ArrayList<Ball> ballz;
    private int lastCase = -1;
    private double speedboost = 1.5;
    private PhysicsWorld world;

    public PowerUpManager(Platform platform, PhysicsWorld world) {
        this.platform = platform;
        ballz = new ArrayList<>();
        this.world = world;
    }

    private void resetPlatform() {
        switch (lastCase) {
            case 0:
                platform.disableLazer();
                break;
            case 1:
                platform.scalePlatform(1.0);
                break;
            case 2:
                platform.notWantABall();
                break;
            case 3:
                platform.scaleLaunchSpeed(1 / speedboost);
                for (Ball b : ballz) {
                    b.velocity.scale(1 / speedboost);
                }
                break;
            case 4:
                platform.scaleLaunchSpeed(speedboost);
                for (Ball b : ballz) {
                    b.velocity.scale(speedboost);
                }
                break;
        }
    }

    public int enablePowerUp(PowerUp p) {
        resetPlatform();
        lastCase = p.getType();
        switch (p.getType()) {
            case 0:
                platform.enableLazer(world);
                break;
            case 1:
                platform.scalePlatform(2.0);
                break;
            case 2:
                platform.wantABall();
                break;
            case 3:
                platform.scaleLaunchSpeed(speedboost);
                for (Ball b : ballz) {
                    b.velocity.scale(speedboost);
                }
                break;
            case 4:
                platform.scaleLaunchSpeed(1 / speedboost);
                for (Ball b : ballz) {
                    b.velocity.scale(1 / speedboost);
                }
                break;
            case 5:
                splitBall();
                break;
            case 6:
                return 1;
        }
        return 0;
    }

    public void splitBall() {
        ArrayList<Ball> balls = new ArrayList<>();
        for (Ball b : ballz) {
            PhysicsUtils.Vec2 vel1 = new PhysicsUtils.Vec2(b.velocity);
            PhysicsUtils.Vec2 vel2 = new PhysicsUtils.Vec2(b.velocity);
            vel1.x = vel1.x * Math.cos(Math.PI / 2) - vel1.y * Math.sin(Math.PI / 2);
            vel1.y = vel1.x * Math.sin(Math.PI / 2) + vel1.y * Math.cos(Math.PI / 2);
            vel2.x = vel2.x * Math.cos(-Math.PI / 2) - vel2.y * Math.sin(-Math.PI / 2);
            vel2.y = vel2.x * Math.sin(-Math.PI / 2) + vel2.y * Math.cos(-Math.PI / 2);

            PhysicsUtils.Vec2 sPos = new PhysicsUtils.Vec2(b.position).sub(vel1.scale(world.getDeltaTime()));
            Ball ball = new Ball((int) sPos.x, (int) (sPos.y),
                    b.radius);
            ball.velocity = vel1;
            balls.add(ball);

            ball = new Ball((int) sPos.x, (int) (sPos.y),
                    b.radius);
            ball.velocity = vel2;
            balls.add(ball);
        }
        for (Ball b : balls) {
            recordBall(b);
            world.add(b);
        }
    }

    public void recordBall(Ball b) {
        ballz.add(b);
    }

    public void removeBallRecord(Ball b) {
        ballz.remove(b);
    }
}
