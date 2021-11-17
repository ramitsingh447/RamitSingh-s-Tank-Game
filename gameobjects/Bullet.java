package src.gameobjects;

import src.Collidable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import src.GameWorld;

public class Bullet extends GameObject implements Collidable {

    private int vx, vy, angle;
    private String owner;
    private BufferedImage explosion;

    Bullet(int x, int y, int angle, BufferedImage bulletImg, BufferedImage explosion, String owner) {
        super(x, y, bulletImg);
        this.vx = (int) Math.round(3 * Math.cos(Math.toRadians(angle)));
        this.vy = (int) Math.round(3 * Math.sin(Math.toRadians(angle)));
        this.angle = angle;
        this.explosion = explosion;
        this.owner = owner;
    }

    public String getOwner() {
        return this.owner;
    }

    @Override
    public void update() {
        if (this.alive) {
            x += vx;
            y += vy;
        }
        this.getRec().setLocation(x, y);
    }

    @Override
    public void collision(Collidable obj) {
        if (obj instanceof src.gameobjects.Wall || obj instanceof src.gameobjects.Tank && ((src.gameobjects.Tank) obj).getTankName() != this.owner || obj instanceof Bullet && ((Bullet) obj).getOwner() != this.owner) {
            this.alive = false;
        }
    }

    @Override
    public void drawImage(Graphics2D g2d) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        if (this.alive) {
            g2d.drawImage(this.img, rotation, null);
        } else {
            g2d.drawImage(this.explosion, x, y, null);
        }
    }

}