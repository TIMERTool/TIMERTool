
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main extends JFrame {

    public static final int INITALLINKPANELLENGTH = 2500;
    public static final int WINDOWLENGTH = 990;

    public static void main(String[] args) {
        new Main().setVisible(true);
    }

    public Main() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(500, 500);
            setExtendedState(MAXIMIZED_BOTH); 

            JLayeredPane jp = new JLayeredPane();

            final TimeManager manager = new TimeManager(new File("input.csv"), INITALLINKPANELLENGTH, WINDOWLENGTH);

            final LinkPanelTop linkPanelTop = new LinkPanelTop(manager);
            linkPanelTop.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 300));

            final LinkPanelBottom linkPanelBottom = new LinkPanelBottom(manager);
            linkPanelBottom.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 300));

            final TimePanel timePanel = new TimePanel(manager, linkPanelTop, linkPanelBottom);
            timePanel.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 100));

            final JSlider durationScalingFactorBar = new JSlider();
            durationScalingFactorBar.setMinimum(1);
            durationScalingFactorBar.setMaximum(250);
            durationScalingFactorBar.setValue(durationScalingFactorBar.getMaximum());
            durationScalingFactorBar.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    int val = durationScalingFactorBar.getValue();
                    
                    if(val == durationScalingFactorBar.getMaximum()) {
                        val = 0;
                    } else {
                        val = durationScalingFactorBar.getValue();
                    }
                    
                    manager.setDurationScalingFactor(val);
                    timePanel.repaint();
                }
            });
            durationScalingFactorBar.setBounds(5, 700, 200, 50);

            final JSlider timePanelScalingFactorBar = new JSlider();
            timePanelScalingFactorBar.setMinimum(1);
            timePanelScalingFactorBar.setMaximum(1000);
            timePanelScalingFactorBar.setValue(1);
            timePanelScalingFactorBar.addChangeListener(new ChangeListener() {
                
                @Override
                public void stateChanged(ChangeEvent e) {
                    manager.setTimePanelScalingFactor(timePanelScalingFactorBar.getValue());

                    linkPanelTop.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 300));
                    linkPanelBottom.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 300));
                    timePanel.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 100));

                    timePanel.scrollBy(0);
                    
                    timePanel.repaint();
                }
            });
            timePanelScalingFactorBar.setBounds(5, 600, 200, 50);

            JScrollPane linksTop = new JScrollPane(linkPanelTop);
            linksTop.setBounds(300, 500, 1000, 175);
            linksTop.setBorder(BorderFactory.createEmptyBorder());
            linksTop.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            linksTop.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

            JScrollPane linksBottom = new JScrollPane(linkPanelBottom);
            linksBottom.setBounds(300, 775, 1000, 175);
            linksBottom.setBorder(BorderFactory.createEmptyBorder());
            linksBottom.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            linksBottom.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

            JScrollPane times = new JScrollPane(timePanel);
            times.setBounds(300, 675, 1000, 100);
            times.setBorder(BorderFactory.createEmptyBorder());
            times.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            times.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

            TransparentPanel tp = new TransparentPanel(manager);
            tp.setBounds(300, 675, 1000, 100);
            tp.setOpaque(false);

            jp.add(linksTop, new Integer(0));
            jp.add(linksBottom, new Integer(0));
            jp.add(times, new Integer(1));
            jp.add(tp, new Integer(2));
            jp.add(durationScalingFactorBar, new Integer(3));
            jp.add(timePanelScalingFactorBar, new Integer(3));

            add(jp);
        } catch (IOException io) {
        }
    }
}
