package Jarkanoid;

import Core.Game;
import Core.Screen;
import Jarkanoid.UI.GameButton;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainMenuScreen extends Screen implements MouseListener {
    private int timePassed = 0, score = -1;
    private GameButton playButton, quitButton;
    private BufferedImage backGround = null;

    public MainMenuScreen(Game j, int score) {
        this(j);
        this.score = score;
    }
    public MainMenuScreen(Game j) {
        super(j);
        game.addMouseListener(this);
        ///sound
        
        playButton = new GameButton(140, 220, "Play");
        playButton.addClickListener(new GameButton.MouseClickedListener() {
            @Override
            public void onClick() {
                game.setScreen(new GameScreen(game));
            }
        });
        
        quitButton = new GameButton(140, 290, "Quit");
        quitButton.addClickListener(new GameButton.MouseClickedListener() {
            @Override
            public void onClick() {
                System.exit(0);
            }
        });
        
        try {
            backGround = ImageIO.read(new File("background.jpg"));
        } catch(IOException e) {
            System.out.println("failed to load image");
        }
        
    }

    @Override
    public void update() {
/*
        if (timePassed * 25 >= 3000)
            game.setScreen(new GameScreen(game));
        timePassed++;
        */
    }


    @Override
    public void render(Graphics2D g2d) {
        ((Graphics)g2d).drawImage(backGround, 0, -80, null);
 
         g2d.setFont(new Font("TimesRoman", Font.BOLD, 38)); 
        g2d.setColor(Color.WHITE);
        g2d.drawString("Jarkanoid!", 120, 150);
            g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
        if(score != -1)
        {
            g2d.drawString("Your Score is: "+score, 130, 190);
        }
        g2d.drawString("MADE BY: JIMMY, SONIKA, & AMY", 50, 690);

        playButton.draw(g2d);
        quitButton.draw(g2d);
    }

    @Override
    public void renderInReverse(Graphics2D g2d) {
        // for physics simulation, leave blank if there isn't any physics to draw
    }
    
    @Override
    public void renderAfterReversing(Graphics2D g2d) {
        // not needed if render in reverse is blank
    }
    
    @Override
    public void dispose() {
        game.removeMouseListener(this);
        //stop sound
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        playButton.mouseDown(e.getX(), e.getY());
        quitButton.mouseDown(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        playButton.mouseUp(e.getX(), e.getY());
        quitButton.mouseUp(e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
