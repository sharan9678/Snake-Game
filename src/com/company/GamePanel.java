package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNIT = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 100;
    static boolean gameOn = false;
    final int[] x = new int[GAME_UNIT];
    final int[] y = new int[GAME_UNIT];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void pause() {
        GamePanel.gameOn = true;
        timer.stop();
    }

    public void resume() {
        GamePanel.gameOn = false;
        timer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        if(running) {
//            for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) {
//                graphics.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
//                graphics.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
//            }

            graphics.setColor(Color.red);
            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i=0; i<bodyParts; i++) {
                if(i == 0){
                    graphics.setColor(Color.green);
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    //graphics.setColor(new Color(45, 180, 0));
                    graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            graphics.setColor(Color.red);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, graphics.getFont().getSize());

        } else {
            gameOver(graphics);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int)SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt((int)SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for(int i=bodyParts; i>0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollision() {
        //checks if head collides with body
        for(int i=bodyParts; i>0; i--) {
            if (x[0] == x[i] && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        //check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        //check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        //check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        //stop the timer if running is false
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics graphics) {
        //display score
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, graphics.getFont().getSize());

        //Game Over text
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("GameOver"))/2, SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(GamePanel.gameOn) {
                        resume();
                    } else {
                        pause();
                    }
                    break;
            }
        }
    }

}
