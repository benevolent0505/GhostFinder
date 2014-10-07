package com.mikiofjt.ghostfinder;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mikio on 2014/10/03.
 */
public class GhostFinder extends JFrame {

    public GhostFinder () {
        setTitle("Ghost Finder");

        MainPanel panel = new MainPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);

        pack();
    }

    public static void main(String[] args) {
        GhostFinder frame = new GhostFinder();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
