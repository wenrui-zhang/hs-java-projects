package Core;

import java.awt.*;

public abstract class Screen {

    protected Game game;

    public Screen(Game j) {
        game = j;
    }

    public abstract void update();

    public abstract void render(Graphics2D g2d);

    public abstract void renderInReverse(Graphics2D g2d);

    public abstract void renderAfterReversing(Graphics2D g2d);
    
    public abstract void dispose();
}
