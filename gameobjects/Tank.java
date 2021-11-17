package src.gameobjects;

import src.Collidable;
import src.GameWorld;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Tank extends GameObject implements Collidable {

    private int vx, vy, angle, health = 10, lives = 3;
    private int initX, initY, prevX, prevY;
    private final int R = 2, ROTATIONAL = 2;
    private boolean UpArrow, DownArrow, RightArrow, LeftArrow, shoot;
    private long firingTimer = System.currentTimeMillis();
    private BufferedImage bulletImg, smallExplosionImg, largeExplosionImg;
    private GameWorld gameworld;
    private String tankName;
    private int count = 0;

    public Tank(int x, int y, int vx, int vy, int angle, BufferedImage tankImg, BufferedImage bulletImg, String tankName, GameWorld gameworld) {
        super(x, y, tankImg);
        this.vx = vx;
        this.vy = vy;
        this.initX = x;
        this.initY = y;
        this.angle = angle;
        this.bulletImg = bulletImg;
        this.tankName = tankName;
        this.gameworld = gameworld;
    }

    private int getHealth() {
        return this.health;
    }

    public int getLives() {
        return this.lives;
    }

    public String getTankName() {
        return this.tankName;
    }

    public void setSmallExplosion(BufferedImage explosion) {
        this.smallExplosionImg = explosion;
    }

    public void setLargeExplosion(BufferedImage explosion) {
        this.largeExplosionImg = explosion;
    }

    public void toggleUpArrow() {
        this.UpArrow = true;
    }

    public void toggleDownArrow() {
        this.DownArrow = true;
    }

    public void toggleRightArrow() {
        this.RightArrow = true;
    }

    public void toggleLeftArrow() {
        this.LeftArrow = true;
    }

    public void toggleShoot() {
        this.shoot = true;
    }

    public void unToggleUpArrow() {
        this.UpArrow = false;
    }

    public void unToggleDownArrow() {
        this.DownArrow = false;
    }

    public void unToggleRightArrow() {
        this.RightArrow = false;
    }

    public void unToggleLeftArrow() {
        this.LeftArrow = false;
    }

    public void unToggleshoot() {
        this.shoot = false;
    }

    @Override
    public void update() {
        this.prevX = x;
        this.prevY = y;

        if (this.UpArrow) {
            this.moveForwards();
        }
        if (this.DownArrow) {
            this.moveBackwards();
        }
        if (this.LeftArrow) {
            this.rotateLeft();
        }
        if (this.RightArrow) {
            this.rotateRight();
        }
        if (this.shoot) {
            this.Shoot();
        }
        this.getRec().setLocation(x, y);
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONAL;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONAL;
    }

    private void Shoot() {
        if (System.currentTimeMillis() - firingTimer > 1000) {
            Bullet blt = new Bullet(x + 17, y + 17, angle, bulletImg, smallExplosionImg, tankName);
            gameworld.addGameObject(blt);
            firingTimer = System.currentTimeMillis();
        }
    }

    private void checkBorder() {
        if (x < 40) {
            x = 40;
        }
        if (x >= GameWorld.WORLD_WIDTH - 88) {
            x = GameWorld.WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameWorld.WORLD_HEIGHT - 88) {
            y = GameWorld.WORLD_HEIGHT - 88;
        }
    }

    private void depleteHealth() {
        if (health > 0) {
            health--;
        } else {
            if (lives > 0 && health == 0) {
                lives--;
                x = initX;
                y = initY;
                health = 10;
            }
            if (lives == 0) {
                alive = false;
            }
        }
    }

    @Override
    public void collision(Collidable obj) {
        if (obj instanceof src.gameobjects.Wall || (obj instanceof Tank && !((Tank) obj).getTankName().equals(this.tankName))) {
            this.x = prevX;
            this.y = prevY;
        }
        if (obj instanceof Bullet && !Objects.equals(((Bullet) obj).getOwner(), this.tankName)) {
            this.depleteHealth();
        }
        if (obj instanceof PowerUp && lives < 4) {
            count++;

            if (count == 2) {
                lives++;
                count = 0;
            }
        }
    }

    @Override
    public void drawImage(Graphics2D g2d) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        if (this.alive) {
            g2d.drawImage(this.img, rotation, null);
            g2d.setColor(Color.green);
            g2d.fillRect(x, y + (int) getRec().getHeight() + 10, getHealth() * 5, 10);
        } else {
            g2d.drawImage(this.largeExplosionImg, x, y, null);
        }
    }

}