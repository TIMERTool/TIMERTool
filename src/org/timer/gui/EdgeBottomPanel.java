/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.gui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import org.timer.model.Model;
import org.timer.model.TimeEdge;

/**
 *
 * @author Peter Hoek
 */
public class EdgeBottomPanel extends ScrollPanel {

    private final Model model;
    private final AffineTransform fontAT;

    public EdgeBottomPanel(Model model) {
        super();

        this.model = model;

        fontAT = new AffineTransform();
        fontAT.rotate(Math.PI / 2);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        Iterator<TimeEdge> it = model.getVisibleTimeLinksIterator();

        Font font = g2.getFont();

        g2.setFont(font.deriveFont(fontAT));

        while (it.hasNext()) {
            TimeEdge upTo = it.next();
            int pixel = model.bottomNodeToPixel(upTo.getBottomNode());

            if (pixel != -1) {
                g2.setColor(upTo.getColour());

                g2.drawString(model.getNodeName(upTo.getBottomNode()), pixel - 4, 178);

                g2.drawLine(pixel, 174, model.linkTimeToPixel(upTo.getTime()) + (getVisibleStart() - model.getTimePanelVisibleStart()), 0);
            }
        }

        g2.setFont(font);
    }
}
