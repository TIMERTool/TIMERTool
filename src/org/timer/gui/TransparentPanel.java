/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import org.timer.model.Model;

/**
 *
 * @author Peter Hoek
 */
public class TransparentPanel extends JPanel {

    private final Model model;

    public TransparentPanel(Model model) {
        super();

        this.model = model;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(new Color(93,98,107, 220));//(214, 217, 223, 200));
        g.fillRect(0, 0, model.getTimeWindowStart() + 5, 100);
        g.fillRect(model.getTimeWindowEnd() + 1 + 5, 0, model.getTimeWindowLength() + model.getTimeWindowEnd() + 5, 100);

        g.setColor(new Color(0, 0, 0, 255));
        g.drawLine(model.getTimeWindowStart() + 5, 0, model.getTimeWindowStart() + 5, 99);
        g.drawLine(model.getTimeWindowEnd() + 5, 0, model.getTimeWindowEnd() + 5, 99);
        g.drawLine(model.getTimeWindowStart() + 5, 0, model.getTimeWindowEnd() + 5, 0);
        g.drawLine(model.getTimeWindowStart() + 5, 99, model.getTimeWindowEnd() + 5, 99);
    }
}
