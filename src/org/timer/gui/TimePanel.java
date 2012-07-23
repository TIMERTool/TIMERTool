/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.Iterator;
import javax.swing.JToolTip;
import javax.swing.JViewport;
import javax.swing.event.MouseInputAdapter;
import org.timer.model.Model;
import org.timer.model.TimeEdge;

/**
 *
 * @author Peter Hoek
 */
public class TimePanel extends ScrollPanel {

    public static final int INTERVALSIZE = 20;
    private final Model model;
    private final EdgeTopPanel edgeTopPanel;
    private final EdgeBottomPanel edgeBottomPanel;
    private final GraphPanel graphPanel;
    private final JToolTip toolTip = new JToolTip();
    private int rawZoom, mouseLoc = -1;

    public TimePanel(final Model model, EdgeTopPanel linkPanelTop, EdgeBottomPanel linkPanelBottom, final GraphPanel graphPanel) {
        super();

        this.model = model;
        this.edgeTopPanel = linkPanelTop;
        this.edgeBottomPanel = linkPanelBottom;
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
                if (e.getWheelRotation() != 0) {
                    rawZoom += e.getWheelRotation() * 16;

                    if (rawZoom < 0) {
                        rawZoom = 0;
                    }

                    double oldLen = model.getTimePanelTotalLength();

                    model.setTimePanelScalingFactor(Math.pow((double) rawZoom / 75D, 2) + 1);

                    setPreferredSize(new Dimension(model.getTimePanelTotalLength(), 100));
                    ((JViewport) getParent()).setViewPosition(new Point(model.getTimePanelTotalLength(), 100));

                    double changeRatio = ((double) model.getTimePanelTotalLength()) / oldLen;

                    scrollBy(1 + (int) (((int) (changeRatio * ((double) model.getTimePanelVisibleStart() + (((double) model.getTimeWindowLength()) / 2D)))) - ((double) model.getTimePanelVisibleStart() + (((double) model.getTimeWindowLength()) / 2D))));

                    model.updateWindow(getVisibleStart());

                    graphPanel.update(model.getVisibleTimeLinksIterator());
                }
            }
        };

        addMouseListener(adapter);
        addMouseMotionListener(adapter);
        addMouseWheelListener(adapter);
    }

    public void updateMouseLoc(int x, int y) {
        x -= 1;

        if (y >= 299) {
            mouseLoc = x;

            toolTip.setVisible(true);
            toolTip.setTipText(model.formatDate(model.pixelToLinkTime(x)));
            toolTip.setLocation(x - toolTip.getWidth(), y);
            toolTip.setVisible(true);
        } else {
            mouseLoc = -1;

            toolTip.setVisible(false);
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Iterator<TimeEdge> it = model.getAllTimeLinksIterator();

        while (it.hasNext()) {
            TimeEdge upTo = it.next();
            int pixel = model.linkTimeToPixel(upTo.getTime());

            int thickness = model.getDurationScalingFactor() > 0 ? (int) (upTo.getDuration() / 50 * model.getDurationScalingFactor()) : 1;

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

        g2.setColor(Color.BLACK);

        AffineTransform fontAT = new AffineTransform();

        Font theFont = g2.getFont();

        fontAT.rotate(Math.PI / 2);
        g2.setFont(theFont.deriveFont(fontAT));

        for (int i = 14; i < getWidth(); i += ((double) INTERVALSIZE)) {
            g2.drawLine(i, 300, i, 300 + 5);

            //model.getMinTime().add((int) ((i - 166) * (double) (model.getMaxTime().subtract(model.getMinTime()).getTimeInUnits()) / (double) getWidth()))

            g2.drawString(model.formatDate(model.pixelToLinkTime(i)), i - 4, 300 + 7);
        }

        if (mouseLoc > -1) {
            g2.drawLine(mouseLoc, 0, mouseLoc, 100);
        }

        edgeTopPanel.repaint();
        edgeBottomPanel.repaint();

        super.paint(g);
    }

    @Override
    protected void didScroll(int amount) {
        model.updateWindow(getVisibleStart());

        graphPanel.update(model.getVisibleTimeLinksIterator());
    }
}
