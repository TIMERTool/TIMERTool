package org.timer.gui.multislider;


import org.timer.model.TimeLink;
import org.timer.model.TimeManager;
import org.timer.gui.multislider.ScrollPanel;
import org.timer.gui.multislider.LinkPanelBottom;
import org.timer.gui.multislider.LinkPanelTop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Iterator;
import org.timer.gui.graph.GraphPanel;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Peter Hoek
 */
public class TimePanel extends ScrollPanel {

    private final TimeManager manager;
    private final LinkPanelTop linkPanelTop;
    private final LinkPanelBottom linkPanelBottom;
    private final GraphPanel graphPanel;

    public TimePanel(TimeManager manager, LinkPanelTop linkPanelTop, LinkPanelBottom linkPanelBottom, GraphPanel graphPanel) {
        this.manager = manager;
        this.linkPanelTop = linkPanelTop;
        this.linkPanelBottom = linkPanelBottom;
        this.graphPanel = graphPanel;
        
        setOpaque(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;        

        Iterator<TimeLink> it = manager.getAllTimeLinksIterator();

        while (it.hasNext()) {
            TimeLink upTo = it.next();
            int pixel = manager.linkTimeToPixel(upTo.getTime());
            
            int thickness = manager.getDurationScalingFactor() > 0 ? upTo.getDuration() / 50 * manager.getDurationScalingFactor() : 1;
            
            if(thickness < 1) {
                thickness = 1;
            }

            g2.setColor(upTo.getColour());
        
            g2.draw(new Line2D.Double(pixel, 0, pixel - (((double)thickness) / 2f) + 1, 19));
            g2.draw(new Line2D.Double(pixel, 0, pixel + (((double)thickness) / 2f), 19));
            
            g2.fillRect((pixel - (thickness / 2)) - (thickness % 2 == 0 ? 1 : 2) + 2, 20, thickness, 60);
                 
            g2.draw(new Line2D.Double(pixel - (((double)thickness / 2f)) + 1, 80, pixel, 100));
            g2.draw(new Line2D.Double(pixel + (((double)thickness / 2f)), 80, pixel, 100));
        }
        
        linkPanelTop.repaint();
        linkPanelBottom.repaint();
    }

    @Override
    protected void didScroll(int amount) {
        manager.updateWindow(amount);
        
        graphPanel.update(manager.getVisibleTimeLinksIterator());
    }
}
