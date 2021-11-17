package src.gameobjects;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {

    int x;
    int y;
    boolean alive;
    BufferedImage img;
    private Rectangle rectangle;

    GameObject(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.alive = true;
        this.rectangle = new Rectangle(this.x, this.y, this.img.getWidth(), this.img.getHeight());
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public Rectangle getRec() {
        return this.rectangle;
    }

    public abstract void update();

    public abstract void drawImage(Graphics2D g2d);

}