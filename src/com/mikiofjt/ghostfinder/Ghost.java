package com.mikiofjt.ghostfinder;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Created by Mikio on 2014/10/04.
 */
public class Ghost {

    public static final int SIZE = 90;

    private int x, y;

    private Image ghost;

    private Random random;

    protected static int coughtCounter = 0;

    public Ghost (Image ghost) {
        this.ghost = ghost;

        random = new Random();

        x = random.nextInt(MainPanel.WIDTH - SIZE);
        y = random.nextInt(MainPanel.HEIGHT - SIZE);
    }

    public void update () {
        x = random.nextInt(MainPanel.WIDTH - SIZE);
        y = random.nextInt(MainPanel.HEIGHT - SIZE);
    }

    public void countUp () {
        coughtCounter++;
    }

    public boolean isClicked (MouseEvent e) {
        double distance = Math.sqrt(Math.pow(x + SIZE/2 - e.getX(), 2.0) + Math.pow(y + SIZE/2 - e.getY(), 2.0));
        if ((int)distance <= SIZE/2) {
            return true;
        }
        return false;
    }

    public void draw (Graphics g) {
        g.drawImage(ghost, x, y, null);
    }
}