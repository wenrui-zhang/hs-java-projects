package Core;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {

    protected final int desiredFPS = 40;
    protected final double expectedDelta = 1 / (double) desiredFPS;
    private int screenX = 420,
            screenY = 700;
    private Screen screen = null;
    public Game() {
        setPreferredSize(new Dimension(screenX, screenY));
        setMinimumSize(this.getPreferredSize());
        setMaximumSize(this.getPreferredSize());

        setBackground(Color.BLACK);

        World world = new World(this);
        world.start(1000 / 60);// 60 fps!!!
    }

    public final void setScreen(Screen screen) {
        if(this.screen != null)
            this.screen.dispose();
        this.screen = screen;
    }

    public void update() {
        if (screen != null) {
            screen.update();
        }
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (screen != null) {
            Graphics2D g2d = (Graphics2D) g;
            screen.render(g2d);

            g2d.translate(0, screenY);
            g2d.scale(1.0, -1.0);

            screen.renderInReverse(g2d);

            g2d.scale(1.0, -1.0);
            g2d.translate(0, -screenY);

            screen.renderAfterReversing(g2d);
        }
    }
}
