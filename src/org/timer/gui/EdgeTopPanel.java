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
public class EdgeTopPanel extends ScrollPanel {

    private final Model model;

    public EdgeTopPanel(Model model) {
        super();

        this.model = model;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        Iterator<TimeEdge> it = model.getVisibleTimeLinksIterator();

        while (it.hasNext()) {
            TimeEdge upTo = it.next();
            int pixel = model.topNodeToPixel(upTo.getTopNode());

            if (pixel != -1) {
                g2.setColor(upTo.getColour());

                AffineTransform fontAT = new AffineTransform();
                Font theFont = g2.getFont();

                fontAT.rotate(-(Math.PI / 2));
                Font theDerivedFont = theFont.deriveFont(fontAT);

                g2.setFont(theDerivedFont);
                g2.drawString(model.getNodeName(upTo.getTopNode()), pixel + 4, 22);
                g2.setFont(theFont);

                g2.drawLine(pixel, 22, model.linkTimeToPixel(upTo.getTime()) + (getVisibleStart() - model.getTimePanelVisibleStart()), 198);
            }
        }
    }
}