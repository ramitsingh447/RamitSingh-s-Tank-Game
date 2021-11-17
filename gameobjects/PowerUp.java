package src.gameobjects;

import src.Collidable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUp extends GameObject implements Collidable {

    public PowerUp(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }

    @Override
    public void collision(Collidable obj) {
        if (obj instanceof src.gameobjects.Tank && ((src.gameobjects.Tank) obj).getLives() < 4) {
            this.alive = false;
        }
    }

    @Override
    public void drawImage(Graphics2D g2d) {
        if (this.alive) {
            g2d.drawImage(this.img, x, y, null);
        }
    }
}