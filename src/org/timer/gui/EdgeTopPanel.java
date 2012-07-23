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
     AffineTransform fontAT ;
     Font theFont;
     Font theDerivedFont;

    public EdgeTopPanel(Model model) {
        super();

        this.model = model;
        fontAT = new AffineTransform();
        fontAT.rotate(Math.PI /2 );
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        Iterator<TimeEdge> it = model.getVisibleTimeLinksIterator();
        
        if(theDerivedFont==null)
        {
            theFont = g2.getFont();
            theDerivedFont = theFont.deriveFont(fontAT);
        }
        g2.setFont(theDerivedFont);

        while (it.hasNext()) {
            TimeEdge upTo = it.next();
            int pixel = model.topNodeToPixel(upTo.getTopNode());

            if (pixel != -1) {
                g2.setColor(upTo.getColour());

                
                g2.drawString(model.getNodeName(upTo.getTopNode()), pixel -2 , 1);
                

                g2.drawLine(pixel, 22, model.linkTimeToPixel(upTo.getTime()) + (getVisibleStart() - model.getTimePanelVisibleStart()), 198);
            }
        }
        g2.setFont(theFont);
    }
}