package Jarkanoid.UI;

import java.awt.*;

public class GameButton {
    public interface MouseClickedListener {
        void onClick();
    }
    private int x, y, width, height, textX, textY;
    private String text;
    private MouseClickedListener clickListener = null;
    private Color color;
    private String font = "TimesRoman";
    private int size = 12;
    private boolean isActive = true;
    public GameButton(int x, int y, String text) {
        this(x, y, 150, 45, text);
    }

    public GameButton(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        color = Color.BLUE;
        
        textX = x+(width/2)-(text.length()*5/2);
        textY = y+(height/2);
    }

    public void addClickListener(MouseClickedListener listener) {
        clickListener = listener;
    }

    public void draw(Graphics2D g2d) {
        if(isActive) {
            g2d.setFont(new Font(font, Font.PLAIN, size));
            g2d.setColor(color);
            g2d.fillRect(x, y, width, height);
            g2d.setColor(Color.WHITE);
            g2d.drawString(text, textX, textY);
        }
    }

    private boolean withinBounds(int x, int y) {
        return (this.x<x && x<(this.x+width) && this.y<y && y<(this.y+height));
    }
    private boolean pressed = false;

    public boolean mouseDown(int x, int y) {
        if(withinBounds(x, y) && isActive) {
            pressed = true;
            color = Color.RED;
            return true;
        }
        return false;
    }

    public void mouseUp(int x, int y) {
        if(pressed && withinBounds(x, y) && isActive) {
            color = Color.BLUE;
            pressed = false;
            if(clickListener != null)
                clickListener.onClick();
        }
        else {
            pressed = false;
            color = Color.BLUE;
        }
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    public void setText(String text) {
        this.text = text;
    }

}
