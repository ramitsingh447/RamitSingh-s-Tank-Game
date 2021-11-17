package src.gameobjects;

import src.Collidable;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject implements Collidable {

    private final boolean breakable;
    private int wallHealth = 2;
    private BufferedImage explosionImg;

    public Wall(int x, int y, BufferedImage wallImg, BufferedImage explosionImg, boolean breakable) {
        super(x, y, wallImg);
        this.explosionImg = explosionImg;
        this.breakable = breakable;
    }

    public boolean isBreakable() {
        return this.breakable;
    }

    private void depleteHealth() {
        if (wallHealth > 0) {
            wallHealth--;
        } else {
            wallHealth = 0;
            this.alive = false;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void collision(Collidable obj) {
        if (obj instanceof Bullet) {
            if (this.breakable && this.alive) {
                depleteHealth();
            }
        }
    }

    @Override
    public void drawImage(Graphics2D g2d) {
        if (alive) {
            g2d.drawImage(this.img, x, y, null);
        } else {
            g2d.drawImage(this.explosionImg, x, y, null);
        }
    }

}