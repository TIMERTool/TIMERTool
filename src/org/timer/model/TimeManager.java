package org.timer.model;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Peter Hoek
 */
public final class TimeManager {

    private TimeLink first, last, start, end;
    private int timePanelSideOffset, timeWindowVisibleStart, timeWindowLength, totalTimePanelLength, minTime, maxTime, timePanelScalingFactor = 1, timeLinks, durationScalingFactor = 0;
    private boolean colourByTo = false;
    private static final Color[] colours = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.BLACK, Color.LIGHT_GRAY};
    private ArrayList<Integer> nodes = new ArrayList<>();
    private ArrayList<Integer> topNodes = new ArrayList<>();
    private ArrayList<Integer> bottomNodes = new ArrayList<>();
    private Object[][] nodeLookup;
    private HashMap<Integer, String> labels = new HashMap<>();

    public TimeManager(File inputFile, int windowLength) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");

            int colourIndex;

            if (colourByTo) {
                colourIndex = Integer.parseInt(values[1]) % colours.length;
            } else {
                colourIndex = Integer.parseInt(values[0]) % colours.length;
            }

            end = new TimeLink(end, null, Integer.parseInt(values[0]), Integer.parseInt(values[2]), Integer.parseInt(values[1]), (Integer.parseInt(values[3])) + 1, colours[colourIndex]);

            if (!nodes.contains(end.getTopNode())) {
                nodes.add(end.getTopNode());
            }

            if (!nodes.contains(end.getBottomNode())) {
                nodes.add(end.getBottomNode());
            }

            if (!topNodes.contains(end.getTopNode())) {
                topNodes.add(end.getTopNode());
            }

            if (!bottomNodes.contains(end.getBottomNode())) {
                bottomNodes.add(end.getBottomNode());
            }

            if (end.getPrev() == null) {
                start = end;
            } else {
                end.getPrev().setNext(end);
            }

            timeLinks++;
        }
        
        Collections.sort(nodes);

        nodeLookup = new Object[nodes.size()][3];
        
        for(int i = 0; i < nodes.size(); i++) {
            nodeLookup[i][0] = getNodeName(nodes.get(i));
            nodeLookup[i][1] = true;
            nodeLookup[i][2] = true;
        }
        
        Collections.sort(topNodes);
        Collections.sort(bottomNodes);

        end.setNext(null);

        this.timeWindowLength = windowLength;
        this.timeWindowVisibleStart = 0;
        this.totalTimePanelLength = windowLength - (getTimePanelSidePadding() * 2);
        this.first = start;
        this.last = end;
        this.minTime = start.getTime();
        this.maxTime = end.getTime();

        setUpWindow();
    }

    public Iterator<TimeLink> makeIterator(TimeLink start, TimeLink end, TimeRule rule) {
        ArrayList<TimeLink> list = new ArrayList<>();

        if (start == null || end == null) {
            System.out.println("Null start or end timelink node.\n\nStart:"+start+"\nEnd:"+end.toString());
            
            System.exit(1);
        }

        while (start != end.getNext()) {
            if (rule.evaluate(start)) {
                list.add(start);
            }

            start = start.getNext();
        }

        list.add(end);

        return list.iterator();
    }

    public Iterator<TimeLink> getAllTimeLinksIterator() {
        return makeIterator(first, last, new TimeRule() {

            @Override
            public boolean evaluate(TimeLink link) {
                if((Boolean) nodeLookup[nodes.indexOf(link.getTopNode())][1] && (Boolean) nodeLookup[nodes.indexOf(link.getBottomNode())][2]) {
                    return true;
                }
                
                return false;
            }
        });
    }

    public Iterator<TimeLink> getVisibleTimeLinksIterator() {
        return makeIterator(start, end, new TimeRule() {

            @Override
            public boolean evaluate(TimeLink link) {
                if((Boolean) nodeLookup[nodes.indexOf(link.getTopNode())][1] && (Boolean) nodeLookup[nodes.indexOf(link.getBottomNode())][2]) {
                    return true;
                }

                return false;
            }
        });
    }

    public int getMinTime() {
        return minTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public int getTimePanelScalingFactor() {
        return timePanelScalingFactor;
    }

    public int getTimePanelVisibleStart() {
        return timeWindowVisibleStart;
    }

    public int getTimePanelTotalLength() {
        return (timePanelSideOffset + totalTimePanelLength + timePanelSideOffset) * timePanelScalingFactor;
    }

    public final int getTimePanelSidePadding() {
        return ((int) (getTimeWindowStart() * 1.25f));
    }

    public int getTopLinkPanelTotalLength() {
        return (topNodes.size() * (10 + 1)) + 5;
    }

    public int getBottomLinkPanelTotalLength() {
        return (bottomNodes.size() * (10 + 1)) + 5;
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

    public int getDurationScalingFactor() {
        return durationScalingFactor;
    }

    public String getNodeName(int node) {
        return new Integer(node).toString();
    }
    
    public Object[][] getNodeLookup() {
        return nodeLookup;
    }

    public ArrayList<Integer> getNodeList() {
        return nodes;
    }

    public int linkTimeToPixel(int time) {
        return ((int) (((double) (time - minTime)) * (((double) totalTimePanelLength / (double) (maxTime - minTime)) * (double) timePanelScalingFactor))) + timePanelSideOffset;
    }

    public int topNodeToPixel(int node) {
        int index = topNodes.indexOf(node);

        if (index == -1) {
            throw new IllegalArgumentException("Invalid Node.");
        }

        return (index * (10 + 1)) + 5;
    }

    public int bottomNodeToPixel(int node) {
        int index = bottomNodes.indexOf(node);

        if (index == -1) {
            throw new IllegalArgumentException("Invalid Node.");
        }

        return (index * (10 + 1)) + 5;
    }

    public void setTimePanelScalingFactor(int timePanelScalingFactor) {
        this.timePanelScalingFactor = timePanelScalingFactor;
    }

    public void setDurationScalingFactor(int durationScalingFactor) {
        this.durationScalingFactor = durationScalingFactor;
    }

    public final void updateWindow(int offset) {
        timeWindowVisibleStart += offset;

        setUpWindow();
    }

    public final void setUpWindow() {
        int thickness = getDurationScalingFactor() > 0 ? Math.max(first.getDuration(), last.getDuration()) / 50 * getDurationScalingFactor() : 1;

        if (thickness < 1) {
            thickness = 1;
        }

        this.timePanelSideOffset = thickness + getTimePanelSidePadding();

        TimeLink next = first;

        while (true) {
            start = next;

            if (next == null || linkTimeToPixel(next.getTime()) >= timeWindowVisibleStart + getTimeWindowStart()) {
                break;
            }

            next = next.getNext();
        }

        next = last;

        while (true) {
            end = next;

            if (next == null || linkTimeToPixel(next.getTime()) <= timeWindowVisibleStart + getTimeWindowEnd()) {
                break;
            }

            next = next.getPrev();
        }
    }
}