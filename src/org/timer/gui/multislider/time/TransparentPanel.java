/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.gui.multislider.time;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import org.timer.model.TimeManager;

/**
 *
 * @author Peter Hoek
 */
public class TransparentPanel extends JPanel {

    private final TimeManager manager;

    public TransparentPanel(TimeManager manager) {
        super();
        
        this.manager = manager;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(new Color(214, 217, 223, 200));
        g.fillRect(0, 0, manager.getTimeWindowStart() + 5, 100);
        g.fillRect(manager.getTimeWindowEnd() + 1 + 5, 0, manager.getTimeWindowLength() + manager.getTimeWindowEnd() + 5, 100);

        g.setColor(new Color(0, 0, 0, 255));
        g.drawLine(manager.getTimeWindowStart() + 5, 0, manager.getTimeWindowStart() + 5, 99);
        g.drawLine(manager.getTimeWindowEnd() + 5, 0, manager.getTimeWindowEnd() + 5, 99);
        g.drawLine(manager.getTimeWindowStart() + 5, 0, manager.getTimeWindowEnd() + 5, 0);
        g.drawLine(manager.getTimeWindowStart() + 5, 99, manager.getTimeWindowEnd() + 5, 99);
    }
}
