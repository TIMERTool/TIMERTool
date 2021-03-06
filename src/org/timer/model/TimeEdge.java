/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.model;

import java.awt.Color;
import java.util.Objects;

/**
 *
 * @author Peter Hoek
 */
public class TimeEdge {

    private TimeEdge prev;
    private TimeEdge next;
    private int topNode;
    private int bottomNode;
    private AbstractDate time;
    private int duration;
    private Color colour;

    public TimeEdge(TimeEdge prev, TimeEdge next, int topNode, AbstractDate time, int bottomNode, int thickness, Color colour) {
        this.prev = prev;
        this.next = next;
        this.topNode = topNode;
        this.bottomNode = bottomNode;
        this.time = time;
        this.duration = thickness;
        this.colour = colour;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof TimeEdge) {
            TimeEdge otherTimeLink = (TimeEdge) other;

            if (prev == otherTimeLink.prev
                    && next == otherTimeLink.next
                    && topNode == otherTimeLink.topNode
                    && time == otherTimeLink.time
                    && bottomNode == otherTimeLink.bottomNode
                    && duration == otherTimeLink.duration
                    && colour == otherTimeLink.colour) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.topNode;
        hash = 89 * hash + Objects.hashCode(this.time);
        hash = 89 * hash + this.bottomNode;
        hash = 89 * hash + this.duration;
        hash = 89 * hash + Objects.hashCode(this.colour);
        return hash;
    }

    /**
     * @return the next
     */
    public TimeEdge getNext() {
        return next;
    }

    /**
     * @return the prev
     */
    public TimeEdge getPrev() {
        return prev;
    }

    /**
     * @return the topNode
     */
    public int getTopNode() {
        return topNode;
    }

    /**
     * @return the time
     */
    public AbstractDate getTime() {
        return time;
    }

    /**
     * @return the bottomNode
     */
    public int getBottomNode() {
        return bottomNode;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @return the colour
     */
    public Color getColour() {
        return colour;
    }

    /**
     * @param next the next to set
     */
    public void setNext(TimeEdge next) {
        this.next = next;
    }

    /**
     * @param prev the prev to set
     */
    public void setPrev(TimeEdge prev) {
        this.prev = prev;
    }

    /**
     * @param topNode the topNode to set
     */
    public void setTopNode(int topNode) {
        this.topNode = topNode;
    }

    /**
     * @param time the time to set
     */
    public void setTime(AbstractDate time) {
        this.time = time;
    }

    /**
     * @param bottomNode the bottomNode to set
     */
    public void setBottomNode(int bottomNode) {
        this.bottomNode = bottomNode;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @param colour the colour to set
     */
    public void setColour(Color colour) {
        this.colour = colour;
    }
}
