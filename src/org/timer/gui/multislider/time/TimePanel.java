/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.gui.multislider.time;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.Iterator;
import javax.swing.JToolTip;
import javax.swing.event.MouseInputAdapter;
import org.timer.gui.ScrollPanel;
import org.timer.gui.graph.GraphPanel;
import org.timer.gui.multislider.link.LinkPanelBottom;
import org.timer.gui.multislider.link.LinkPanelTop;
import org.timer.model.TimeLink;
import org.timer.model.TimeManager;

/**
 *
 * @author Peter Hoek
 */
public class TimePanel extends ScrollPanel {

    public static final int INTERVALSIZE = 20;
    private final TimeManager manager;
    private final LinkPanelTop linkPanelTop;
    private final LinkPanelBottom linkPanelBottom;
    private final GraphPanel graphPanel;
    private final JToolTip toolTip = new JToolTip();
    private int mouseLoc = -1;
    private boolean renderTimeIndicator = false;
    private boolean canScrollZoom;

    public TimePanel(TimeManager manager, LinkPanelTop linkPanelTop, LinkPanelBottom linkPanelBottom, GraphPanel graphPanel) {
        super();

        this.manager = manager;
        this.linkPanelTop = linkPanelTop;
        this.linkPanelBottom = linkPanelBottom;
        this.graphPanel = graphPanel;

        setOpaque(false);

        add(toolTip);
        toolTip.setVisible(false);

        MouseInputAdapter adapter = new MouseInputAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                updateMouseLoc(e.getPoint().x, e.getPoint().y);

                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updateMouseLoc(e.getPoint().x, e.getPoint().y);

                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                updateMouseLoc(e.getPoint().x, e.getPoint().y);

                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseLoc = -1;

                toolTip.setVisible(false);

                repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                //if(mouseLoc ) {
                //}
            }
        };

        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    public void updateMouseLoc(int x, int y) {
        mouseLoc = x;

        if (y >= 299) {
            renderTimeIndicator = true;

            toolTip.setTipText(new Integer(((int) ((x - 166) * (double) (manager.getMaxTime() - manager.getMinTime()) / (double) getWidth())) + manager.getMinTime()).toString());

            toolTip.setLocation(x - toolTip.getWidth(), y);
            toolTip.setVisible(true);
        } else {
            renderTimeIndicator = false;
            toolTip.setVisible(false);

            if (y <= 100) {
                canScrollZoom = true;
            } else {
                canScrollZoom = false;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Iterator<TimeLink> it = manager.getAllTimeLinksIterator();

        while (it.hasNext()) {
            TimeLink upTo = it.next();
            int pixel = manager.linkTimeToPixel(upTo.getTime());

            int thickness = manager.getDurationScalingFactor() > 0 ? upTo.getDuration() / 50 * manager.getDurationScalingFactor() : 1;

            if (thickness < 1) {
                thickness = 1;
            }

            g2.setColor(upTo.getColour());

            g2.draw(new Line2D.Double(pixel + 5, 0, pixel - (((double) thickness) / 2f) + 1 + 5, 19));
            g2.draw(new Line2D.Double(pixel + 5, 0, pixel + (((double) thickness) / 2f) + 5, 19));

            g2.fillRect(((pixel - (thickness / 2)) - (thickness % 2 == 0 ? 1 : 2) + 2) + 5, 20, thickness, 60);

            g2.draw(new Line2D.Double(pixel - (((double) thickness / 2f)) + 1 + 5, 80, pixel + 5, 100));
            g2.draw(new Line2D.Double(pixel + (((double) thickness / 2f)) + 5, 80, pixel + 5, 100));
        }

        //g2.setColor(Color.RED);
        //g2.fillRect(0, 300, getWidth(), 60);
        g2.setColor(Color.BLACK);

        AffineTransform fontAT = new AffineTransform();

        Font theFont = g2.getFont();

        fontAT.rotate(Math.PI / 2);
        g2.setFont(theFont.deriveFont(fontAT));

        for (int i = 14; i < getWidth(); i += ((double) INTERVALSIZE)) {
            g2.drawLine(i, 300, i, 300 + 5);
            g2.drawString(new Integer(((int) ((i - 166) * (double) (manager.getMaxTime() - manager.getMinTime()) / (double) getWidth())) + manager.getMinTime()).toString(), i - 4, 300 + 7);
        }

        if (renderTimeIndicator) {
            g2.drawLine(mouseLoc, 0, mouseLoc, 100);
        }

        linkPanelTop.repaint();
        linkPanelBottom.repaint();

        super.paint(g);
    }

    @Override
    protected void didScroll(int amount) {
        manager.updateWindow(amount);

        graphPanel.update(manager.getVisibleTimeLinksIterator());
    }
}
