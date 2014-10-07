package com.mikiofjt.ghostfinder;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Created by Mikio on 2014/10/04.
 */
public class Item {

    public static final int SIZE = 50;

    private int x, y;

    private boolean clicked;

    private Image item;

    private Random random;

    public Item (Image item) {
        this.item = item;

        random = new Random();

        clicked = false;

        x = random.nextInt(MainPanel.WIDTH - SIZE);
        y = random.nextInt(MainPanel.HEIGHT - SIZE);
    }

    public void update () {
        x = random.nextInt(MainPanel.WIDTH - SIZE);
        y = random.nextInt(MainPanel.HEIGHT - SIZE);
        clicked = false;
    }

    public boolean isClicked (MouseEvent e) {
        if (!clicked) {
            double distance = Math.sqrt(Math.pow(x + SIZE/2 - e.getX(), 2.0) + Math.pow(y + SIZE/2 - e.getY(), 2.0));
            if ((int)distance <= SIZE/2) {
                clicked = true;
                return true;
            }
        }
        return false;
    }

    public boolean clicked () {
        return clicked;
    }

    public void draw (Graphics g) {
        g.drawImage(item, x, y, null);
    }
}