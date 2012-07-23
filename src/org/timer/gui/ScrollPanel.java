/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author Peter Hoek
 */
public class ScrollPanel extends JPanel {

    private int xDiff;
    private int visibleStart;

    public ScrollPanel() {
        super();
       
        
      
        MouseInputAdapter mia = new MouseInputAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (getParent() instanceof JViewport) {
                    JViewport jv = (JViewport) getParent();
                    Point p = jv.getViewPosition();
                    int oldVisibleStart = visibleStart;
                    visibleStart = p.x - (e.getX() - xDiff);

                    int maxX = getWidth() - jv.getWidth();

                    if (visibleStart < 0) {
                        visibleStart = 0;

                        xDiff = e.getX();
                    } else if (visibleStart > maxX) {
                        visibleStart = maxX;

                        xDiff = e.getX();
                    }

                    jv.setViewPosition(new Point(visibleStart, 0));

                    didScroll(visibleStart - oldVisibleStart);

                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                xDiff = e.getX();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        };

        addMouseMotionListener(mia);
        addMouseListener(mia);
    }

    public void scrollBy(int amount) {
        if (getParent() instanceof JViewport) {
            JViewport jv = (JViewport) getParent();

            visibleStart += amount;

            if (visibleStart < 0) {
                visibleStart = 0;
            } else if (visibleStart > getWidth()) {
                visibleStart = getWidth();
            }

            jv.setViewPosition(new Point(visibleStart, 0));

            didScroll(amount);

            repaint();
        }
    }

    protected void didScroll(int amount) {
    }

    public int getVisibleStart() {
        return visibleStart;
    }
}
