package org.timer.gui.multislider;


import org.timer.model.TimeManager;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Peter Hoek
 */
public class TransparentPanel extends JPanel {
    private final TimeManager manager;

    public TransparentPanel(TimeManager manager) {
        this.manager = manager;
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        g.setColor(new Color(214, 217, 223, 200));
        g.fillRect(0, 0, manager.getTimeWindowStart(), 100);
        g.fillRect(manager.getTimeWindowEnd() + 1, 0, manager.getTimeWindowLength()+ manager.getTimeWindowEnd(), 100);
        
        g.setColor(new Color(0, 0, 0, 255));
        g.drawLine(manager.getTimeWindowStart(), 0, manager.getTimeWindowStart(), 99);
        g.drawLine(manager.getTimeWindowEnd(), 0, manager.getTimeWindowEnd(), 99);
        g.drawLine(manager.getTimeWindowStart(), 0, manager.getTimeWindowEnd(), 0);
        g.drawLine(manager.getTimeWindowStart(), 99, manager.getTimeWindowEnd(), 99);
    }
}
