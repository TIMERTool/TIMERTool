/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */

import java.awt.Color;
import org.timer.model.AbstractColorDefinition;

/**
 *
 * @author Peter Hoek
 */
public class NodeColorDefinition extends AbstractColorDefinition {

    private final Color[] colors;

    public NodeColorDefinition(Color[] colors) {
        this.colors = colors;
    }

    @Override
    public Color getColor(int node) {
        return colors[node % colors.length];
    }
}
