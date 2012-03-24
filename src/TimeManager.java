
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Peter Hoek
 */
public class TimeManager {

    private TimeLink first, last, start, end;
    private int timePanelSideOffset, timeWindowVisibleStart, timeWindowLength, totalTimePanelLength, totalLinkPanelLength, minTime, maxTime, timePanelScalingFactor = 1, timeLinks, durationScalingFactor = 0;
    private boolean colourByTo = false;
    private static final Color[] colours = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.BLACK, Color.WHITE, Color.LIGHT_GRAY};
    private ArrayList<Integer> top = new ArrayList<>();
    private ArrayList<Integer> bottom = new ArrayList<>();

    public TimeManager(File inputFile, int preferredTotalLinkPanelLength, int windowLength) throws IOException {
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

            top.add(end.getTopNode());
            bottom.add(end.getBottomNode());

            if (end.getPrev() == null) {
                start = end;
            } else {
                end.getPrev().setNext(end);
            }

            timeLinks++;
        }

        Collections.sort(top);
        Collections.sort(bottom);

        end.setNext(null);

        this.timeWindowLength = windowLength;
        this.timeWindowVisibleStart = 0;
        this.totalTimePanelLength = windowLength - (getTimePanelSidePadding() * 2);
        this.totalLinkPanelLength = preferredTotalLinkPanelLength;
        this.first = start;
        this.last = end;
        this.minTime = start.getTime();
        this.maxTime = end.getTime();

        setUpWindow();
    }

    public TimeLinkIterator getAllTimeLinksIterator() {
        return new TimeLinkIterator(first, last);
    }

    public TimeLinkIterator getVisibleTimeLinksIterator() {
        return new TimeLinkIterator(start, end);
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

    public int getLinkPanelTotalLength() {
        return totalLinkPanelLength;
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

    public int linkTimeToPixel(int time) {
        //System.out.println(timePanelSideOffset);

        return ((int) (((double) (time - minTime)) * (((double) totalTimePanelLength / (double) (maxTime - minTime)) * (double) timePanelScalingFactor))) + timePanelSideOffset;
    }

    public int topNodeToPixel(int node) {
        int upTo = 0;

        for (int theNode : top) {
            if (theNode == node) {
                return (int) ((double) upTo * ((double) totalLinkPanelLength / (double) top.size()));
            }

            upTo++;
        }

        return -1;
    }

    public int bottomNodeToPixel(int node) {
        int upTo = 0;

        for (int theNode : bottom) {
            if (theNode == node) {
                return (int) ((double) upTo * ((double) totalLinkPanelLength / (double) bottom.size()));
            }

            upTo++;
        }

        return -1;
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
        int thickness = getDurationScalingFactor() > 0 ? Math.max(first.getDuration(), last.getDuration()) / getDurationScalingFactor() : 1;

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
