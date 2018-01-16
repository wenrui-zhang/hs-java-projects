package Core;

import java.util.Timer;
import java.util.TimerTask;

public class World extends TimerTask {

    private Game game;
    private Timer timer;

    public World(Game g) {
        game = g;
    }

    public void start(int interval) {
        timer = new Timer();
        timer.schedule(this, 0, interval);
    }

    @Override
    public void run() {
        game.update();
        game.repaint();
    }
}
