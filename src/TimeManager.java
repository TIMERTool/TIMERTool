
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private int timePanelStartOffset, timePanelEndOffset, timeWindowVisibleStart, timeWindowLength, totalTimePanelLength, totalLinkPanelLength, minTime, maxTime, timePanelScalingFactor = 1, timeLinks;
    private ArrayList<Integer> top = new ArrayList<>();
    private ArrayList<Integer> bottom = new ArrayList<>();

    public TimeManager(File inputFile, int preferredTotalTimePanelLength, int preferredTotalLinkPanelLength, int windowStartTime, int windowLength) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputFile));

        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");

            //TODO Calculate line thickness
            //TODO Calculate line colour

            end = new TimeLink(end, null, Integer.parseInt(values[0]), Integer.parseInt(values[2]), Integer.parseInt(values[1]), 1, Color.RED);

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

        this.timeWindowVisibleStart = 0;
        this.totalTimePanelLength = preferredTotalTimePanelLength;
        this.totalLinkPanelLength = preferredTotalLinkPanelLength;
        this.timeWindowLength = windowLength;
        this.first = start;
        this.last = end;
        this.minTime = start.getTime();
        this.maxTime = end.getTime();
        this.timePanelStartOffset = (Math.max(first.getThickness(), last.getThickness()) / 2) + getTimePanelSidePadding();
        this.timePanelEndOffset = (timePanelStartOffset * 2) + linkTimeToPixel(minTime + 1) * 2;
        
        System.out.println(timeWindowVisibleStart + getTimeWindowEnd());

        updateWindow(windowStartTime);
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
        return totalTimePanelLength + timePanelEndOffset;
    }

    public int getTimePanelSidePadding() {
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

    public int linkTimeToPixel(int time) {
        return ((int) (((double) (time - minTime)) * (((double) totalTimePanelLength / (double) (maxTime - minTime)) * (double) timePanelScalingFactor))) + (timePanelStartOffset * timePanelScalingFactor);
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

    int bottomNodeToPixel(int node) {
        int upTo = 0;

        for (int theNode : bottom) {
            if (theNode == node) {
                return (int) ((double) upTo * ((double) totalLinkPanelLength / (double) bottom.size()));
            }

            upTo++;
        }

        return -1;
    }

    public void setScalingFactor(int scalingFactor) {
        this.timePanelScalingFactor = scalingFactor;
    }

    public final void updateWindow(int offset) {
        timeWindowVisibleStart += offset;

        TimeLink next = start;

        if (offset == 0) {
            while (true) {
                start = next;

                if (next == null || linkTimeToPixel(next.getTime()) >= timeWindowVisibleStart + getTimeWindowStart()) {
                    break;
                }

                next = next.getNext();
            }

            next = end;

            while (true) {
                end = next;

                System.out.print(linkTimeToPixel(next.getTime())+" <= " + (timeWindowVisibleStart + getTimeWindowEnd()));
                
                if (next == null || linkTimeToPixel(next.getTime()) <= timeWindowVisibleStart + getTimeWindowEnd()) {
                    System.out.println(" yep " + next.getTime());
                    
                    break;
                } else {
                    System.out.println();
                }

                next = next.getPrev();
            }
        } else if (offset > 0) {    
            while (true) {
                start = next;

                if (next == null || linkTimeToPixel(next.getTime()) >= timeWindowVisibleStart + getTimeWindowStart()) {
                    break;
                }

                next = next.getNext();
            }

            next = end;

            while (true) {
                if (next == null || linkTimeToPixel(next.getTime()) > timeWindowVisibleStart + getTimeWindowEnd()) {
                    break;
                }

                end = next;
                next = next.getNext();
            }
        } else if (offset < 0) {
            if(start == null || end == null) {
                start = end = last;
            }
            
            while (true) {
                if (next == null || linkTimeToPixel(next.getTime()) < timeWindowVisibleStart + getTimeWindowStart()) {
                    break;
                }

                start = next;
                next = next.getPrev();
            }

            next = end;

            while (true) {
                end = next;
                
                if (next == null || linkTimeToPixel(next.getTime()) <= timeWindowVisibleStart + getTimeWindowEnd()) {
                    break;
                }
                
                next = next.getPrev();
            }
        }

   
       // System.out.println(start.getTime() + " " + end.getTime());
              //System.out.println(linkTimeToPixel(start.getTime()) + " " + linkTimeToPixel(end.getTime()));
    }
}
