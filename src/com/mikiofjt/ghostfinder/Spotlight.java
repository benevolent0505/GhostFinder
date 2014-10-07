package com.mikiofjt.ghostfinder;

import java.awt.geom.Ellipse2D;

/**
 * Created by Mikio on 2014/10/04.
 */
public class Spotlight {
    protected Ellipse2D.Double spot;

    private double count = 0.0;

    public Spotlight () {
        this(0, 0, 0);
    }

    public Spotlight (int x, int y, int radius) {
        this.spot = new Ellipse2D.Double(x - radius, y - radius, radius*2, radius*2);
    }

    public void setSpot (int x, int y, int radius) {
        count += 0.05;
        spot.x = x - (int)(radius - count);
        spot.y = y - (int)(radius - count);
        spot.width = (int)(radius - count) * 2;
        spot.height = (int)(radius - count) * 2;
    }

    public Ellipse2D getSpot () {
        return spot;
    }
}