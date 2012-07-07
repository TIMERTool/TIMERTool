/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Peter Hoek
 */
public abstract class AbstractData {

    private final AbstractColorDefinition colorDefinition;
    private final List<Integer> nodes = new ArrayList<>();
    private final List<Integer> topNodes = new ArrayList<>();
    private final List<Integer> bottomNodes = new ArrayList<>();
    private TimeEdge first, last;

    public AbstractData(AbstractColorDefinition colorDefinition) {
        this.colorDefinition = colorDefinition;
    }

    public void addLink(int topNode, int bottomNode, AbstractDate time, int thickness) {
        TimeEdge link = new TimeEdge(getLast(), null, topNode, time, bottomNode, thickness, getColorDefinition().getColor(bottomNode));
        last = link;

        if (!nodes.contains(last.getTopNode())) {
            getNodes().add(getLast().getTopNode());
        }

        if (!nodes.contains(last.getBottomNode())) {
            getNodes().add(getLast().getBottomNode());
        }

        if (!topNodes.contains(last.getTopNode())) {
            getTopNodes().add(getLast().getTopNode());
        }

        if (!bottomNodes.contains(last.getBottomNode())) {
            getBottomNodes().add(getLast().getBottomNode());
        }

        if (getLast().getPrev() == null) {
            first = getLast();
        } else {
            getLast().getPrev().setNext(getLast());
        }
    }

    public void sort() {
        Collections.sort(nodes);
        Collections.sort(topNodes);
        Collections.sort(bottomNodes);
    }

    public abstract AbstractDate makeDate(long timeInUnits);

    public AbstractColorDefinition getColorDefinition() {
        return colorDefinition;
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    public List<Integer> getTopNodes() {
        return topNodes;
    }

    public List<Integer> getBottomNodes() {
        return bottomNodes;
    }

    public TimeEdge getFirst() {
        return first;
    }

    public TimeEdge getLast() {
        return last;
    }
}
