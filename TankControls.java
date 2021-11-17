package src;

import src.gameobjects.Tank;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class TankControls implements KeyListener {

    private final Tank player;
    private final int up, down, right, left, shoot;

    TankControls(Tank tank, int up, int down, int left, int right, int shoot) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.shoot = shoot;
        this.player = tank;
    }

    @Override
    public void keyTyped(KeyEvent e) {}



    public void keyPressed(KeyEvent e) {
        int ArrowKey = e.getKeyCode();

        if (ArrowKey == up) {
            this.player.toggleUpArrow();
        }
        if (ArrowKey == down) {
            this.player.toggleDownArrow();
        }
        if (ArrowKey == left) {
            this.player.toggleLeftArrow();
        }
        if (ArrowKey == right) {
            this.player.toggleRightArrow();
        }
        if (ArrowKey == shoot) {
            this.player.toggleShoot();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int releasedKey = e.getKeyCode();

        if (releasedKey == up) {
            this.player.unToggleUpArrow();
        }
        if (releasedKey == down) {
            this.player.unToggleDownArrow();
        }
        if (releasedKey == left) {
            this.player.unToggleLeftArrow();
        }
        if (releasedKey == right) {
            this.player.unToggleRightArrow();
        }
        if (releasedKey == shoot) {
            this.player.unToggleshoot();
        }
    }
}