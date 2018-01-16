package Jarkanoid;

import Jarkanoid.UI.SoundPlayer;
import Physics.Body;
import Physics.PhysicsUtils;
import Physics.PhysicsWorld;

import java.util.Timer;
import java.util.TimerTask;

public class Platform extends Body {
    Timer timer = null;
    private boolean wantsBall;
    private Ball attatchedBall;
    private int ballXLocationOnBoard = 0;
    private double width, height, launchSpeed;
    private SoundPlayer lazerSound = null;

    public Platform(int x, int y, double width, double height, double launchSpeed, SoundPlayer player) {
        super(null, x, y);
        SolidPolygon shape = new SolidPolygon();
        shape.setBox(width, height);
        setShape(shape);

        setStatic();

        this.width = width;
        this.height = height;
        wantsBall = true;
        attatchedBall = null;
        this.launchSpeed = launchSpeed;
        lazerSound = player;
    }

    public void wantABall() {
        wantsBall = true;
    }

    public void notWantABall() {
        wantsBall = false;
    }

    public boolean wantsBall() {
        return wantsBall;
    }

    public void attatchBall(Ball ball) {
        if(this.attatchedBall != null)
            this.attatchedBall.sleep = false;
        
        ball.velocity.set(0, 0);
        ball.sleep = true;
        attatchedBall = ball;
        ballXLocationOnBoard = PhysicsUtils.round(ball.position.x - position.x);
        attatchedBall.position.set(position.x + ballXLocationOnBoard,
                (position.y + height / 2 + attatchedBall.radius + 1));
        wantsBall = false;
    }

    public void launchBall() {
        if (attatchedBall != null) {
            attatchedBall.velocity.set(0, launchSpeed);
            changeBallDirection(attatchedBall);
            attatchedBall.sleep = false;
            attatchedBall = null;
        }
    }

    public void changeBallDirection(Ball ball) {
        int distance = (int) (ball.position.x - position.x);
        double velocity = ball.velocity.length();

        if (velocity < (launchSpeed / 4 * 3))
            velocity = launchSpeed;
        double angle = ((double) distance / width) * Math.PI * 0.25;
        ball.velocity.set(velocity * Math.sin(angle), velocity * Math.cos(angle));
    }

    public void applyMouseInput(int x) {
        position.x = x;
        if (attatchedBall != null) {
            attatchedBall.position.set(position.x + ballXLocationOnBoard,
                    (position.y + height / 2 + attatchedBall.radius + 1));
        }
    }

    public void scalePlatform(double scalar) {
        SolidPolygon shape = new SolidPolygon();
        shape.setBox(width * scalar, height);
        setShape(shape);
        setStatic();
    }

    public void scaleLaunchSpeed(double scalar) {
        launchSpeed *= scalar;
    }

    public void enableLazer(PhysicsWorld world) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int count = 0;
            @Override
            public void run() {
                world.add(new Lazer((int) position.x, (int) position.y + (int) (height / 2 + 6)));
                count++;
                lazerSound.start();
                if (count >= 50)
                    cancel();
            }
        }, 10, 60);
    }

    public void disableLazer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
