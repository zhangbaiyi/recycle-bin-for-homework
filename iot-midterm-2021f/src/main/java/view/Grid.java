package view;

import javax.swing.*;
import java.awt.*;


public class Grid extends JPanel {


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.translate(0, 0);
        g.drawLine(10, 10, 10, 220);
        g.drawLine(10, 10, 340, 10);
        for (int i = 1; i <= 11; i++) {
            g.drawLine(10 + i * 30, 10, 10 + i * 30, 220);
        }
        for (int i = 1; i <= 7; i++) {
            g.drawLine(10, 10 + i * 30, 340, 10 + i * 30);
        }

    }

    @Override
    public Graphics getGraphics() {
        return super.getGraphics();
    }
}
