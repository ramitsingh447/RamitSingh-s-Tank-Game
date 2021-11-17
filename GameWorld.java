package src;

import javax.swing.*;
import src.gameobjects.*;
import src.gameobjects.PowerUp;
import src.gameobjects.Tank;
import src.gameobjects.Wall;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class GameWorld extends JPanel {

    public static final int WORLD_WIDTH = 1920, WORLD_HEIGHT = 1440;
    private static final int SCREEN_WIDTH = 961, SCREEN_HEIGHT = 720;
    private BufferedImage world;
    private JFrame jf;
    private Tank ta1, tb2;
    private BufferedImage backgroundImg;
    private BufferedImage tankImg, lifeImg;
    private BufferedImage bulletImg, imagesmallexplosion, imagelargeexplosion;
    private BufferedImage wallImg, breakableWallImg;
    private boolean player1Won = false;
    private boolean player2Won = false;

    private final ArrayList < GameObject > gameObjects = new ArrayList < > ();

    public void addGameObject(GameObject obj) {
        this.gameObjects.add(obj);
    }

    public static void main(String[] args) {
        GameWorld game = new GameWorld();
        game.init();
        try {
            while (true) {
                game.gameUpdate();
                game.checkCollisions();
                game.repaint();
                if (game.player1Won || game.player2Won) {
                    break;
                }
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {

        }
    }

    private void init() {
        this.jf = new JFrame("Tank Wars");
        this.world = new BufferedImage(GameWorld.WORLD_WIDTH, GameWorld.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);

        try {
            backgroundImg = ImageIO.read(getClass().getResource("/resources/Background.bmp"));
            tankImg = ImageIO.read(getClass().getResource("/resources/tank1.png"));
            lifeImg = ImageIO.read(getClass().getResource("/resources/life.gif"));
            bulletImg = ImageIO.read(getClass().getResource("/resources/Weapon.gif"));
            imagesmallexplosion = ImageIO.read(getClass().getResource("/resources/Explosion_small.gif"));
            imagelargeexplosion = ImageIO.read(getClass().getResource("/resources/Explosion_large.gif"));
            wallImg = ImageIO.read(getClass().getResource("/resources/Wall2.gif"));
            breakableWallImg = ImageIO.read(getClass().getResource("/resources/Wall1.png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        createMap();

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);

        this.jf.setSize(GameWorld.SCREEN_WIDTH + 9, GameWorld.SCREEN_HEIGHT + 38);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);
    }

    private void createMap() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/Map.txt")));
            String line;
            int j = 0;



            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");
                for (int i = 0; i < tokens.length; i++) {
                    switch (tokens[i]) {
                        case "1":
                            gameObjects.add(new Wall(i * 32, j * 32, wallImg, null, false));
                            break;
                        case "2":
                            gameObjects.add(new Wall(i * 32, j * 32, breakableWallImg, imagesmallexplosion, true));
                            break;
                        case "3":
                            ta1 = new Tank(i * 32, j * 32, 0, 0, 0, tankImg, bulletImg, "tankerPlayer1", this);
                            ta1.setSmallExplosion(imagesmallexplosion);
                            ta1.setLargeExplosion(imagelargeexplosion);
                            gameObjects.add(ta1);
                            TankControls tc1 = new TankControls(ta1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_F);
                            this.jf.addKeyListener(tc1);
                            break;
                        case "4":
                            tb2 = new Tank(i * 32, j * 32, 0, 0, 0, tankImg, bulletImg, "tankerPlayer2", this);
                            tb2.setSmallExplosion(imagesmallexplosion);
                            tb2.setLargeExplosion(imagelargeexplosion);
                            gameObjects.add(tb2);
                            TankControls tc2 = new TankControls(tb2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);
                            this.jf.addKeyListener(tc2);
                            break;
                        case "5":
                            gameObjects.add(new PowerUp(i * 32, j * 32, lifeImg));
                            break;
                        default:
                            break;
                    }
                }
                j++;
            }

            br.close();

        } catch (IOException e) {
            System.out.println("File failed to open!");
        }
    }

    private void gameUpdate() {
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjects.get(i).isAlive()) {
                gameObjects.get(i).update();
            } else {
                if (gameObjects.get(i) instanceof Tank) {
                    if (((Tank) gameObjects.get(i)).getTankName().equals("Player2")) {
                        player1Won = true;
                    } else {
                        player2Won = true;
                    }
                }
                gameObjects.remove(i);
                i--;
            }
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < gameObjects.size(); i++) {
            for (int j = 0; j < gameObjects.size(); j++) {
                Collidable co1 = (Collidable) gameObjects.get(i);
                Collidable co2 = (Collidable) gameObjects.get(j);
                if (((GameObject) co1).getRec().getBounds().intersects(((GameObject) co2).getRec().getBounds())) {
                    co1.collision(co2);
                    co2.collision(co1);
                }
            }
        }
    }

    private int getScreenXCord( Tank tank) {
        int xCoordinate = tank.getX();
        if (xCoordinate < SCREEN_WIDTH / 4) {
            xCoordinate = SCREEN_WIDTH / 4;
        }
        if (xCoordinate > WORLD_WIDTH - SCREEN_WIDTH / 4) {
            xCoordinate = WORLD_WIDTH - SCREEN_WIDTH / 4;
        }
        return xCoordinate - SCREEN_WIDTH / 4;
    }

    private int getScreenYCoordinate(Tank tank) {
        int yCoordinate = tank.getY();
        if (yCoordinate < SCREEN_HEIGHT / 2) {
            yCoordinate = SCREEN_HEIGHT / 2;
        }
        if (yCoordinate > WORLD_HEIGHT - SCREEN_HEIGHT / 2) {
            yCoordinate = WORLD_HEIGHT - SCREEN_HEIGHT / 2;
        }
        return yCoordinate - SCREEN_HEIGHT / 2;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        super.paintComponent(g2);

        for (int i = 0; i < WORLD_WIDTH; i += 320) {
            for (int j = 0; j < WORLD_HEIGHT; j += 240)
                buffer.drawImage(backgroundImg, i, j, 320, 240, this);
        }

        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).drawImage(buffer);
        }

        BufferedImage leftScreen = world.getSubimage(getScreenXCord(ta1), getScreenYCoordinate(ta1), SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        BufferedImage rightScreen = world.getSubimage(getScreenXCord(tb2), getScreenYCoordinate(tb2), SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        g2.drawImage(leftScreen, 0, 0, null);
        g2.drawImage(rightScreen, SCREEN_WIDTH / 2 + 1, 0, null);

        g2.drawImage(world, SCREEN_WIDTH / 2 - WORLD_WIDTH / 8 / 2, SCREEN_HEIGHT - WORLD_HEIGHT / 5, WORLD_WIDTH / 5, WORLD_HEIGHT / 5, null);

        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("Player1: ", 10, 28);
        g2.drawString("Player2: ", SCREEN_WIDTH / 2 + 10, 28);

        int x = 110;
        for (int i = 0; i < ta1.getLives(); i++) {
            g2.drawImage(lifeImg, x, 10, null);
            x += lifeImg.getWidth();
        }

        int x1 = SCREEN_WIDTH / 2 + 110;
        for (int i = 0; i < tb2.getLives(); i++) {
            g2.drawImage(lifeImg, x1, 10, null);
            x1 += lifeImg.getWidth();
        }

        if (player1Won || player2Won) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 72));
            g2.setColor(Color.WHITE);
            g2.drawString("GAME OVER ", SCREEN_WIDTH / 4 + 50, SCREEN_HEIGHT / 4);
            if (player1Won) {
                g2.drawString("PLAYER1 WINS! ", SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2);
            }
            if (player2Won) {
                g2.drawString("PLAYER2 WINS! ", SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2);
            }
        }

    }
}