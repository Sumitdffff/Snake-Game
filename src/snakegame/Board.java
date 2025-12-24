package snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {

    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RANDOM_POSITION = 29;
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = false;        // game not started
    private boolean startScreen = true;    // show start screen

    private Timer timer;
    private final int DELAY = 140;

    private Image ball;
    private Image apple;
    private Image head;

    // Score variables
    private int score = 0;
    private int highScore = 0;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        setBackground(new Color(200, 255, 200));
        setFocusable(true);
        setPreferredSize(new Dimension(653, 388));
        loadImages();
        addKeyListener(new TAdapter());
    }

    private void loadImages() {
        ImageIcon iid = new ImageIcon(ClassLoader.getSystemResource("icons/dot.png"));
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon(ClassLoader.getSystemResource("icons/apple.png"));
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon(ClassLoader.getSystemResource("icons/head.png"));
        head = iih.getImage();
    }

    private void initGame() {
        dots = 3;
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * DOT_SIZE;
            y[z] = 50;
        }
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (startScreen) {
            drawStartScreen(g);
        } else if (inGame) {
            drawGame(g);
        } else {
            drawGameOver(g);
        }
    }

    private void drawStartScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String msg = "Press ENTER to Start";
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(msg, (getWidth() - metrics.stringWidth(msg)) / 2, getHeight() / 2);
    }

    private void drawGame(Graphics g) {
        g.drawImage(apple, apple_x, apple_y, this);

        for (int z = 0; z < dots; z++) {
            if (z == 0) {
                g.drawImage(head, x[z], y[z], this);
            } else {
                g.drawImage(ball, x[z], y[z], this);
            }
        }

        // Draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 25);
        g.drawString("High Score: " + highScore, 10, 50);
    }

    private void drawGameOver(Graphics g) {
        String msg = "Game Over!";
        Font font = new Font("Times New Roman", Font.BOLD, 50);
        g.setColor(Color.RED);
        g.setFont(font);
        FontMetrics metrics = getFontMetrics(font);
        g.drawString(msg, (getWidth() - metrics.stringWidth(msg)) / 2, getHeight() / 2 - 50);

        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Your Score: " + score, (getWidth() - metrics.stringWidth("Your Score: " + score)) / 2, getHeight() / 2);
        g.drawString("High Score: " + highScore, (getWidth() - metrics.stringWidth("High Score: " + highScore)) / 2, getHeight() / 2 + 30);
    }

    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score += 10;
            if (score > highScore) {
                highScore = score;
            }
            locateApple();
        }
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[z - 1];
            y[z] = y[z - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 3) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= getHeight() || y[0] < 0 || x[0] >= getWidth() || x[0] < 0) {
            inGame = false;
        }

        if (!inGame && timer != null) {
            timer.stop();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE;
        r = (int) (Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            // START SCREEN
            if (startScreen && key == KeyEvent.VK_ENTER) {
                startScreen = false;
                inGame = true;
                score = 0;

                leftDirection = false;
                rightDirection = true;
                upDirection = false;
                downDirection = false;

                initGame();
            }

            // RESTART AFTER GAME OVER
            if (!startScreen && !inGame && key == KeyEvent.VK_ENTER) {
                inGame = true;
                score = 0;

                leftDirection = false;
                rightDirection = true;
                upDirection = false;
                downDirection = false;

                initGame();
            }

            // CONTROLS
            if (inGame) {
                if (key == KeyEvent.VK_LEFT && !rightDirection) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
                if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
                if (key == KeyEvent.VK_UP && !downDirection) {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
                if (key == KeyEvent.VK_DOWN && !upDirection) {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            }
        }
    }
}
