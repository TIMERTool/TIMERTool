/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Peter Hoek
 */
public final class Model {

    private final int timeWindowLength, totalTimePanelLength;
    private final AbstractDate minTime, maxTime;
    private final AbstractData data;
    private final AbstractDateFormatter dateFormatter;
    private int graphPanelEdgeScalingFactor = 1, timePanelSideOffset, timeWindowVisibleStart;
    private Object[][] nodeLookup;
    private double timePanelScalingFactor = 1, timePanelDurationScalingFactor = 0;
    private TimeEdge start, end;

    public Model(AbstractData data, AbstractDateFormatter dateFormatter, int windowLength) throws IOException {
        this.data = data;

        data.sort();

        nodeLookup = new Object[data.getNodes().size()][3];

        for (int i = 0; i < data.getNodes().size(); i++) {
            nodeLookup[i][0] = getNodeName(data.getNodes().get(i));
            nodeLookup[i][1] = true;
            nodeLookup[i][2] = true;
        }

        this.dateFormatter = dateFormatter;
        this.timeWindowLength = windowLength;
        this.timeWindowVisibleStart = 0;
        this.totalTimePanelLength = windowLength - (getTimePanelSidePadding() * 2);
        this.start = data.getFirst();
        this.end = data.getLast();
        this.minTime = data.getFirst().getTime();
        this.maxTime = data.getLast().getTime();

        setUpWindow();
    }

    public String formatDate(AbstractDate date) {
        return dateFormatter.format(date);
    }

    public Iterator<TimeEdge> makeIterator(TimeEdge start, TimeEdge end) {
        ArrayList<TimeEdge> list = new ArrayList<>();

        if (start == null || end == null) {
            return list.iterator();
        }

        while (start != end.getNext()) {
            if ((Boolean) nodeLookup[data.getNodes().indexOf(start.getTopNode())][1] && (Boolean) nodeLookup[data.getNodes().indexOf(start.getBottomNode())][2]) {
                list.add(start);
            }

            start = start.getNext();
        }

        return list.iterator();
    }

    public Iterator<TimeEdge> getAllTimeLinksIterator() {
        return makeIterator(data.getFirst(), data.getLast());
    }

    public Iterator<TimeEdge> getVisibleTimeLinksIterator() {
        return makeIterator(start, end);
    }

    public AbstractDate getMinTime() {
        return minTime;
    }

    public AbstractDate getMaxTime() {
        return maxTime;
    }

    public int getTimePanelVisibleStart() {
        return timeWindowVisibleStart;
    }

    public int getTimePanelTotalLength() {
        return linkTimeToPixel(maxTime) + timePanelSideOffset;
    }

    public double getTimePanelScalingFactor() {
        return timePanelScalingFactor;
    }

    public final int getTimePanelSidePadding() {
        return ((int) (getTimeWindowStart() * 1.25f));
    }

    public int getTopLinkPanelTotalLength() {
        return (data.getTopNodes().size() * (10 + 1)) + 5;
    }

    public int getBottomLinkPanelTotalLength() {
        return (data.getBottomNodes().size() * (10 + 1)) + 5;
    }

    public int getTimeWindowLength() {
        return timeWindowLength;
    }

    public final int getTimeWindowStart() {
        return Math.round((((float) timeWindowLength) / 100.0f) * 15.0f);
    }

    public final int getTimeWindowEnd() {
        return timeWindowLength - getTimeWindowStart();
    }

    public double getDurationScalingFactor() {
        return timePanelDurationScalingFactor;
    }

    public String getNodeName(int node) {
        return new Integer(node).toString();
    }

    public Object[][] getNodeLookup() {
        return nodeLookup;
    }

    public List<Integer> getNodes() {
        return data.getNodes();
    }

    public int linkTimeToPixel(AbstractDate date) {
        return ((int) ((double) totalTimePanelLength / (double) (maxTime.subtract(minTime).getTimeInUnits()) * (date.subtract(minTime).getTimeInUnits()) * timePanelScalingFactor)) + timePanelSideOffset;
    }

    public AbstractDate pixelToLinkTime(int pixel) {
        return data.makeDate((int) ((((pixel - timePanelSideOffset)) / ((double) totalTimePanelLength / (double) (maxTime.subtract(minTime).getTimeInUnits()))) / timePanelScalingFactor)).add(minTime);
    }

    public int topNodeToPixel(int node) {
        int index = data.getTopNodes().indexOf(node);

        if (index == -1) {
            throw new IllegalArgumentException("Invalid Node.");
        }

        return (index * (10 + 1)) + 5;
    }

    public int bottomNodeToPixel(int node) {
        int index = data.getBottomNodes().indexOf(node);

        if (index == -1) {
            throw new IllegalArgumentException("Invalid Node.");
        }

        return (index * (10 + 1)) + 5;
    }

    public void setTimePanelScalingFactor(double timePanelScalingFactor) {
        this.timePanelScalingFactor = timePanelScalingFactor;
    }

    public void setDurationScalingFactor(double durationScalingFactor) {
        this.timePanelDurationScalingFactor = durationScalingFactor;
    }

    public final void updateWindow(int offset) {
        timeWindowVisibleStart = offset;

        setUpWindow();
    }

    public final void setUpWindow() {
        int thickness = getDurationScalingFactor() > 0 ? (int) (Math.max(data.getFirst().getDuration(), data.getLast().getDuration()) / 50 * getDurationScalingFactor()) : 1;

        if (thickness < 1) {
            thickness = 1;
        }

        timePanelSideOffset = thickness + getTimePanelSidePadding();

        TimeEdge next = data.getFirst();

        while (true) {
            start = next;

            if (next == null || linkTimeToPixel(next.getTime()) >= timeWindowVisibleStart + getTimeWindowStart()) {
                break;
            }

            next = next.getNext();
        }

        next = data.getLast();

        while (true) {
            end = next;

            if (next == null || linkTimeToPixel(next.getTime()) <= timeWindowVisibleStart + getTimeWindowEnd()) {
                break;
            }

            next = next.getPrev();
        }
    }

    public int getGraphPanelEdgeScalingFactor() {
        return graphPanelEdgeScalingFactor;
    }

    public void setGraphPanelEdgeScalingFactor(int graphPanelEdgeScalingFactor) {
        this.graphPanelEdgeScalingFactor = graphPanelEdgeScalingFactor;
    }
}