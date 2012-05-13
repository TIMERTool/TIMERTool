package com.timer.core;

import com.timer.gui.LinkPanelBottom;
import com.timer.gui.LinkPanelTop;
import com.timer.gui.TimePanel;
import com.timer.gui.TransparentPanel;
import com.timer.model.TimeManager;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Peter Hoek
 */
public class Main extends JFrame {

    public static final int WINDOWLENGTH = 990;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main().setVisible(true);
    }

    public Main() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            JLayeredPane jp = new JLayeredPane();

            final TimeManager manager = new TimeManager(new File("input.csv"), WINDOWLENGTH);

            final LinkPanelTop linkPanelTop = new LinkPanelTop(manager);
            linkPanelTop.setPreferredSize(new Dimension(manager.getTopLinkPanelTotalLength(), 300));

            final LinkPanelBottom linkPanelBottom = new LinkPanelBottom(manager);
            linkPanelBottom.setPreferredSize(new Dimension(manager.getBottomLinkPanelTotalLength(), 300));

            final TimePanel timePanel = new TimePanel(manager, linkPanelTop, linkPanelBottom);
            timePanel.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 100));

            final JScrollPane linksTop = new JScrollPane(linkPanelTop);
            linksTop.setBounds(350, 400, 1000, 250);
            linksTop.setBorder(BorderFactory.createEmptyBorder());
            linksTop.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            linksTop.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

            final JScrollPane linksBottom = new JScrollPane(linkPanelBottom);
            linksBottom.setBounds(350, 750, 1000, 300);
            linksBottom.setBorder(BorderFactory.createEmptyBorder());
            linksBottom.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            linksBottom.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

            final JScrollPane times = new JScrollPane(timePanel);
            times.setBounds(350, 650, 1000, 100);
            times.setBorder(BorderFactory.createEmptyBorder());
            times.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            times.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

            TransparentPanel tp = new TransparentPanel(manager);
            tp.setBounds(350, 650, 1000, 100);
            tp.setOpaque(false);

            final JSlider durationScalingFactorBar = new JSlider();
            durationScalingFactorBar.setMinimum(1);
            durationScalingFactorBar.setMaximum(250);
            durationScalingFactorBar.setValue(durationScalingFactorBar.getMaximum());
            durationScalingFactorBar.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    int val = durationScalingFactorBar.getValue();

                    if (val == durationScalingFactorBar.getMaximum()) {
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

                    timePanel.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 100));
                    times.getViewport().setViewPosition(new Point(manager.getTimePanelTotalLength(), 100));

                    timePanel.scrollBy(0);

                    timePanel.repaint();
                }
            });
            timePanelScalingFactorBar.setBounds(5, 600, 200, 50);

            String[] columns = {"Node", "?"};
            
            DefaultTableModel dtm = new DefaultTableModel();
            dtm.setDataVector(manager.getNodeLookup(), columns);

            final JTable visiblePanel = new JTable(dtm);

            visiblePanel.getColumnModel().getColumn(0).setPreferredWidth(300);

            TableColumn checkBoxColumn = visiblePanel.getColumnModel().getColumn(1);
            checkBoxColumn.setCellRenderer(visiblePanel.getDefaultRenderer(Boolean.class));
            checkBoxColumn.setCellEditor(visiblePanel.getDefaultEditor(Boolean.class));

            JScrollPane visibleChooser = new JScrollPane(visiblePanel);
            visibleChooser.setBounds(0, 0, 200, 500);

            jp.add(linksTop, new Integer(0));
            jp.add(linksBottom, new Integer(0));
            jp.add(times, new Integer(1));
            jp.add(tp, new Integer(2));
            jp.add(durationScalingFactorBar, new Integer(3));
            jp.add(timePanelScalingFactorBar, new Integer(3));
            jp.add(visibleChooser, new Integer(3));

            add(jp);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
