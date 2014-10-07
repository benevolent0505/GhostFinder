package com.mikiofjt.ghostfinder;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;

/**
 * Created by Mikio on 2014/10/03.
 */
public class MainPanel extends JPanel implements Runnable, MouseListener, MouseMotionListener {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private static final int RADIUS = 100;

    private Image backgroundImage, ghostImage, itemImage;

    private JLabel start, explain, exit, explainLabel, next, point;

    private StageStatus status = StageStatus.START;

    private Ghost ghost;
    private Item item;

    private Font mpFont;

    private Spotlight spotlight;

    private Timer timer;

    private Thread thread;

    public MainPanel () {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        loadImage();

        ghost = new Ghost(ghostImage);
        item = new Item(itemImage);

        spotlight = new Spotlight(WIDTH/2, HEIGHT/2, RADIUS);

        mpFont  = createFont("./res/mplus-1c-black.ttf");
        mpFont = mpFont.deriveFont(30.0f);

        start = new JLabel("Start");
        add(start);
        start.setFont(mpFont);
        start.setForeground(Color.WHITE);
        start.addMouseListener(this);

        explain = new JLabel("Explain");
        add(explain);
        explain.setFont(mpFont);
        explain.setForeground(Color.WHITE);
        explain.addMouseListener(this);

        exit = new JLabel("Exit");
        add(exit);
        exit.setFont(mpFont);
        exit.setForeground(Color.WHITE);
        exit.addMouseListener(this);

        explainLabel = new JLabel("<html>察してください<br>画面をクリックしてゲームスタート<html>");
        add(explainLabel);
        explainLabel.setFont(mpFont);
        explainLabel.setForeground(Color.WHITE);
        explainLabel.addMouseListener(this);

        next = new JLabel("Next Stage");
        add(next);
        next.setFont(mpFont);
        next.setForeground(Color.WHITE);
        next.addMouseListener(this);

        point = new JLabel();
        add(point);
        point.setFont(mpFont);
        point.setForeground(Color.WHITE);

        addMouseListener(this);
        addMouseMotionListener(this);

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            repaint();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        // 共通する部分をはここで
        g.setFont(mpFont);
        g.setColor(Color.BLACK);
        g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, this);

        // labelの位置は変わらないので共通部分に
        start.setLocation(WIDTH/2 - start.getWidth()/2, HEIGHT/2 - start.getHeight()/2);
        explain.setLocation(WIDTH/2 - explain.getWidth()/2, HEIGHT/2 + 50);
        exit.setLocation(WIDTH/2 - exit.getWidth()/2, HEIGHT/2 + 100 + explain.getHeight()/2);
        explainLabel.setLocation(WIDTH/2 - explainLabel.getWidth()/2, HEIGHT/2);
        next.setLocation(WIDTH/2 - next.getWidth()/2, HEIGHT/2 + next.getHeight()/2);
        point.setLocation(WIDTH/2 - point.getWidth()/2, 100);

        // ゲームのstatusに応じた描画の処理
        switch (status) {
            case START:
                explainLabel.setVisible(false);
                next.setVisible(false);
                point.setVisible(false);

                fillTransparentRect(g, Color.BLACK);
                break;
            case EXPLAIN:
                start.setVisible(false);
                explain.setVisible(false);
                exit.setVisible(false);
                explainLabel.setVisible(true);

                fillTransparentRect(g, Color.BLACK);
                break;
            case GAME_LOOP:
                start.setVisible(false);
                explain.setVisible(false);
                exit.setVisible(false);
                explainLabel.setVisible(false);
                next.setVisible(false);
                point.setVisible(false);

                ghost.draw(g);
                if (!item.clicked()) {
                    item.draw(g);
                }

                drawSpotlight(g);
                break;
            case CATCH_GHOST:
                point.setVisible(true);
                point.setText(Ghost.coughtCounter + "体目のゴーストを捕まえました！");
                next.setVisible(true);
                exit.setVisible(true);

                ghost.draw(g);
                item.draw(g);

                fillTransparentRect(g, Color.BLACK);
                break;
            case GAME_OVER:
                point.setVisible(true);
                exit.setVisible(true);
                point.setText("残念、取り逃がしてしまいました");

                ghost.draw(g);
                item.draw(g);

                fillTransparentRect(g, Color.BLACK);
                break;
            case FINISH:
                point.setVisible(true);
                exit.setVisible(true);
                point.setText("全てのゴーストを捕まえました！");

                ghost.draw(g);
                item.draw(g);

                fillTransparentRect(g, Color.BLACK);
                break;
            default:
                g.drawString("Error", 100, 100);
                break;
        }
    }

    private void startTimer (int time) {
        timer = new Timer(time * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                status = StageStatus.GAME_OVER;
            }
        });
        timer.start();
    }

    private void delayTimer() {
        timer.stop();
        java.util.Timer delay = new java.util.Timer();
        delay.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.restart();
            }
        }, 3000);
    }

    private void loadImage () {
        ImageIcon icon = new ImageIcon(getClass().getResource("./res/image/background.gif"));
        backgroundImage = icon.getImage();
        icon = new ImageIcon(getClass().getResource("./res/image/ghost0.gif"));
        ghostImage = icon.getImage();
        icon = new ImageIcon(getClass().getResource("./res/image/item.gif"));
        itemImage = icon.getImage();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        // labelをクリックした時の処理
        if (e.getSource().equals(start)) {
            startTimer(12);
            status = StageStatus.GAME_LOOP;
        } else if (e.getSource().equals(explain)) {
            status = StageStatus.EXPLAIN;
        } else if (e.getSource().equals(exit)) {
            System.exit(0);
        } else if (e.getSource().equals(explainLabel)) {
            startTimer(12);
            status = StageStatus.GAME_LOOP;
        } else if (e.getSource().equals(next)) {
            ghost.update();
            item.update();
            startTimer(12 - Ghost.coughtCounter);
            status = StageStatus.GAME_LOOP;
        }

        // ゴーストをクリックした時の処理
        if (ghost.isClicked(e)) {
            if (status == StageStatus.GAME_LOOP) {
                ghost.countUp();
                if (Ghost.coughtCounter == 10) {
                    timer.stop();
                    status = StageStatus.FINISH;
                } else {
                    timer.stop();
                    status = StageStatus.CATCH_GHOST;
                }
            }
        }

        // アイテムをクリックした時の処理
        if (item.isClicked(e)) {
            if (status == StageStatus.GAME_LOOP) {
                delayTimer();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        spotlight.setSpot(e.getX(), e.getY(), RADIUS);
    }

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    private Font createFont (String filename) {
        Font font = null;
        InputStream is = null;

        try {
            is = getClass().getResourceAsStream(filename);
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            is.close();
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return font;
    }

    private void drawSpotlight (Graphics g) {
        Rectangle2D screen = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
        Area mask = new Area(screen);
        mask.subtract(new Area(spotlight.getSpot()));
        Graphics2D g2 = (Graphics2D) g;
        g2.fill(mask);
    }

    private void fillTransparentRect (Graphics g, Color color) {
        Graphics2D g2 = (Graphics2D) g;
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
        g2.setComposite(composite);
        g2.setColor(color);
        g2.fillRect(0, 0, WIDTH, HEIGHT);
    }
}