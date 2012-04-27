package com.timer.model;


import java.awt.Color;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Peter Hoek
 */
public class TimeLink {

    private TimeLink prev;
    private TimeLink next;
    private int topNode;
    private int time;
    private int bottomNode;
    private int duration;
    private Color colour;

    public TimeLink(TimeLink prev, TimeLink next, int topNode, int time, int bottomNode, int thickness, Color colour) {
        this.prev = prev;
        this.next = next;
        this.topNode = topNode;
        this.time = time;
        this.bottomNode = bottomNode;
        this.duration = thickness;
        this.colour = colour;
    }

    /**
     * @return the next
     */
    public TimeLink getNext() {
        return next;
    }

    /**
     * @return the prev
     */
    public TimeLink getPrev() {
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
    public int getTime() {
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
    public void setNext(TimeLink next) {
        this.next = next;
    }

    /**
     * @param prev the prev to set
     */
    public void setPrev(TimeLink prev) {
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
    public void setTime(int time) {
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
