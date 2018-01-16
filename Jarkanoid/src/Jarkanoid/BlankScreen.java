package Jarkanoid;

import Core.Game;
import Core.Screen;
import Jarkanoid.UI.GameButton;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BlankScreen extends Screen implements MouseListener {
    private int timePassed = 0, score = -1;
    private GameButton playButton;

    public BlankScreen(Game j, int score) {
        super(j);
        this.score = score;
    }
    public BlankScreen(Game j) {
        super(j);
        game.addMouseListener(this);
        playButton = new GameButton(100, 200, "Play");

        playButton.addClickListener(new GameButton.MouseClickedListener() {
            @Override
            public void onClick() {
                game.setScreen(new GameScreen(game));
            }
        });
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
    public void renderInReverse(Graphics2D g2d) {
        // for physics simulation, leave blank if there isn't any physics to draw
    }

    @Override
    public void renderAfterReversing(Graphics2D g2d) {
       // not needed because render in reverse
    }

    @Override
    public void render(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        if(score == -1)
            g2d.drawString("THIS IS A MAIN MENU SCREEN SCREEN", 100, 100);
        else
            g2d.drawString("Score = "+score, 100, 100);

        playButton.draw(g2d);
    }

    @Override
    public void dispose() {
        game.removeMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        playButton.mouseDown(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        playButton.mouseUp(e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
