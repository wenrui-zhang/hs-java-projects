import Core.Game;
import Jarkanoid.MainMenuScreen;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        game.setScreen(new MainMenuScreen(game));

        Box box = new Box(BoxLayout.X_AXIS);
        box.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(Box.createHorizontalGlue());
        box.add(game);
        box.add(Box.createHorizontalGlue());

        JFrame frame = new JFrame("Jarkanoid!!");
        frame.add(box);

        frame.pack();

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
