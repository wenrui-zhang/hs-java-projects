package Jarkanoid;

import Core.Game;
import Core.Screen;
import Jarkanoid.UI.GameButton;
import Jarkanoid.UI.SoundPlayer;
import Physics.Body;
import Physics.PhysicsUtils;
import Physics.PhysicsWorld;
import Physics.PhysicsWorldInterface;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameScreen extends Screen implements MouseListener, MouseMotionListener, PhysicsWorldInterface {

    public int brickWidth = 26,
            brickHeight = 13,
            ballDiameter = 10,
            platformWidth = 50,
            platformHight = 10;
    private PhysicsWorld world;
    private Platform platform;
    private Levels levels;
    private Wall deathWall;
    private PowerUpManager manager;
    private int score = 0;
    private int ballsRemaining = 4;
    private boolean paused = false;

    private BufferedImage backGround = null;
    private GameButton pauseButton, quitButton;

    private SoundPlayer bgSound, boingSound, deathSound, lazerSound, endSound, levelUpSound, powerUpSound;

    public GameScreen(Game gam) {
        super(gam);

        world = new PhysicsWorld(0.25, 10);
        world.setInterface(this);
        world.setGravity(new PhysicsUtils.Vec2(0, -0.98));

        levels = new Levels(brickWidth, brickHeight);
        levels.spawnObjects(world);

        Wall wall = new Wall(-15, 0, 30, game.getScreenY() * 3);
        world.add(wall);
        wall = new Wall(game.getScreenX() + 15, 0, 30, game.getScreenY() * 3);
        world.add(wall);
        wall = new Wall(game.getScreenX() / 2, game.getScreenY() + 15, game.getScreenX()*4, 30);
        world.add(wall);
        deathWall = new Wall(game.getScreenX() / 2, -20, game.getScreenX()*3, 30);
        world.add(deathWall);

        //load all sound here
        deathSound = new SoundPlayer("death.wav", -20f);
        boingSound = new SoundPlayer("boing.wav", 4f);
        lazerSound = new SoundPlayer("lazer.wav");
        endSound = new SoundPlayer("c-mon-man.wav");
        levelUpSound = new SoundPlayer("newLevel.wav", -5);
        powerUpSound = new SoundPlayer("powerup.wav");

        bgSound = new SoundPlayer("crash.wav");
        bgSound.start();
        bgSound.loop(1000000000);

        platform = new Platform(game.getScreenX() / 2, 10, platformWidth, platformHight, 20, lazerSound);
        world.add(platform);

        manager = new PowerUpManager(platform, world);
        resetBall();

        game.addMouseListener(this);
        game.addMouseMotionListener(this);

        try {
            backGround = ImageIO.read(new File("background.jpg")); // wallpaper
        } catch (IOException e) {
            System.out.println("failed to load image");
        }

        pauseButton = new GameButton(330, 10, 80, 20, "Pause");
        pauseButton.addClickListener(new GameButton.MouseClickedListener() {
            @Override
            public void onClick() {
                System.out.println("pause");
                if (!paused) {
                    paused = true;
                    quitButton.setActive(true);
                    pauseButton.setText("UnPause");
                } else {
                    paused = false;
                    quitButton.setActive(false);
                    pauseButton.setText("Pause");
                }
            }
        });

        quitButton = new GameButton(330, 40, 80, 20, "Quit");
        quitButton.setActive(false);
        quitButton.addClickListener(new GameButton.MouseClickedListener() {
            @Override
            public void onClick() {
                game.setScreen(new MainMenuScreen(game));
                endSound.start();
            }
        });
    }

    private void resetBall() {
        Ball ball = new Ball((int) platform.position.x, (int) (platform.position.y + platformHight / 2 + ballDiameter / 2 - 0.1),
                ballDiameter / 2);
        world.add(ball);
        manager.recordBall(ball);
        platform.attatchBall(ball);
        ballsRemaining--;
        if (ballsRemaining < 0) {
            game.setScreen(new MainMenuScreen(game, score));
            endSound.start();
        }
    }

    @Override
    public void update() {
        if (!paused) {
            world.step();
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        ((Graphics) g2d).drawImage(backGround, -600, 20, null);
    }

    @Override
    public void renderInReverse(Graphics2D g2d) {
        world.render(g2d);
        if (paused) {
            g2d.setColor(new Color(0, 0, 0, 0.5f));
            g2d.fillRect(0, 0, game.getScreenX(), game.getScreenY());
        }
    }

    @Override
    public void renderAfterReversing(Graphics2D g2d) {
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Score: " + score, 145, 15);
        g2d.drawString("Balls Remaining: " + ballsRemaining, 15, 15);

        pauseButton.draw(g2d); // TODO: FIX THIS
        quitButton.draw(g2d);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!paused) {
            platform.applyMouseInput(e.getX());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!paused) {
            platform.applyMouseInput(e.getX());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!pauseButton.mouseDown(e.getX(), e.getY()) && !quitButton.mouseDown(e.getX(), e.getY())) {
            if (e.isControlDown()) {
                resetBall();
            } else {
                platform.launchBall();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pauseButton.mouseUp(e.getX(), e.getY());
        quitButton.mouseUp(e.getX(), e.getY());
    }

    @Override
    public void onCollision(Body A, Body B) {
        if ((A instanceof Ball || A instanceof Lazer) && B instanceof Brick) {
            ((Brick) B).reduceHP();
            score += 50;
            boingSound.start();
            if (((Brick) B).isDead()) {
                if (Math.random() < 0.5) {
                    PowerUp powerUp = new PowerUp((int) B.position.x, (int) B.position.y);
                    world.add(powerUp);
                }
                world.remove(B);
                if(levels.reduceBrick(world)<=0) {
                    levelUpSound.start();
                }
            }
            if (A instanceof Lazer) {
                world.remove(A);
            }
            return;
        } else if ((B instanceof Ball || B instanceof Lazer) && A instanceof Brick) {
            ((Brick) A).reduceHP();
            score += 50;
            boingSound.start();
            if (((Brick) A).isDead()) {
                if (Math.random() < 0.5) {
                    PowerUp powerUp = new PowerUp((int) B.position.x, (int) B.position.y);
                    world.add(powerUp);
                }
                world.remove(A);
                if(levels.reduceBrick(world)<=0) {
                    levelUpSound.start();
                }
            }
            if (B instanceof Lazer) {
                world.remove(B);
            }
            return;
        }
        if (A instanceof Platform && B instanceof Ball) {
            if (((Platform) A).wantsBall()) {
                ((Platform) A).attatchBall((Ball) B);
            } else {
                ((Platform) A).changeBallDirection((Ball) B);
            }
            return;
        } else if (B instanceof Platform && A instanceof Ball) {
            if (((Platform) B).wantsBall()) {
                ((Platform) B).attatchBall((Ball) A);
            } else {
                ((Platform) B).changeBallDirection((Ball) A);
            }
            return;
        }
        if (A instanceof Platform && B instanceof PowerUp) {
            score += 100;
            ballsRemaining += manager.enablePowerUp((PowerUp) B);
            powerUpSound.start();
            world.remove(B);
        } else if (B instanceof Platform && A instanceof PowerUp) {
            score += 100;
            ballsRemaining += manager.enablePowerUp((PowerUp) A);
            powerUpSound.start();
            world.remove(A);
        }
        if (A.equals(deathWall)) {
            world.remove(B);
            if (B instanceof Ball) {
                manager.removeBallRecord((Ball) B);
                if (!world.containsType(Ball.class)) {
                    deathSound.start();
                    resetBall();
                }
            }
            return;
        } else if (B.equals(deathWall)) {
            world.remove(A);
            if (A instanceof Ball) {
                manager.removeBallRecord((Ball) A);
                if (!world.containsType(Ball.class)) {
                    deathSound.start();
                    resetBall();
                }
            }
            return;
        }
        if (A instanceof Lazer && !(B instanceof Platform)) {
            world.remove(A);
            return;
        } else if (B instanceof Lazer && !(B instanceof Platform)) {
            world.remove(B);
            return;
        }

    }

    /////////somewhat unused
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void dispose() {
        game.removeMouseListener(this);
        game.removeMouseMotionListener(this);
        bgSound.stop();
    }

}

