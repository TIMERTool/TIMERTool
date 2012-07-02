package org.timer.gui.multislider.link;

import org.timer.model.TimeLink;
import org.timer.model.TimeManager;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import org.timer.gui.multislider.ScrollPanel;

public class LinkPanelTop extends ScrollPanel {

    private final TimeManager manager;

    public LinkPanelTop(TimeManager manager) {
        this.manager = manager;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        Iterator<TimeLink> it = manager.getVisibleTimeLinksIterator();

        while (it.hasNext()) {
            TimeLink upTo = it.next();
            int pixel = manager.topNodeToPixel(upTo.getTopNode());

            if (pixel != -1) {
                g2.setColor(upTo.getColour());
                
                AffineTransform fontAT = new AffineTransform();
                Font theFont = g2.getFont();

                fontAT.rotate(-(Math.PI / 2));
                Font theDerivedFont = theFont.deriveFont(fontAT);

                g2.setFont(theDerivedFont);
                g2.drawString(manager.getNodeName(upTo.getTopNode()), pixel + 4, 75);
                g2.setFont(theFont);                
                
                g2.drawLine(pixel, 75, manager.linkTimeToPixel(upTo.getTime()) + (getVisibleStart() - manager.getTimePanelVisibleStart()), 249);
            }
        }
    }
}