/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.timer.gui.graph.GraphPanel;
import org.timer.gui.multislider.link.LinkPanelBottom;
import org.timer.gui.multislider.link.LinkPanelTop;
import org.timer.gui.multislider.time.TimePanel;
import org.timer.gui.multislider.time.TransparentPanel;
import org.timer.model.TimeManager;

/**
 *
 * @author Peter Hoek
 */
public class Window extends JFrame {

    public static final int WINDOWLENGTH = 1010;
    public static final String[] columns = {"Node", "Top?", "Bottom?"};
    private final TimeManager manager;
    private LinkPanelTop linkPanelTop;
    private JScrollPane linksTop;
    private LinkPanelBottom linkPanelBottom;
    private JScrollPane linksBottom;
    private TimePanel timePanel;
    private JScrollPane times;
    private TransparentPanel tp;
    private JSlider durationScalingFactorBar;
    private JSlider timePanelScalingFactorBar;
    private JButton selectAllTop;
    private JButton deselectAllTop;
    private JTable visiblePanel;
    private DefaultTableModel dtm;
    private JScrollPane visibleChooser;
    private GraphPanel graphPanel;
    private JLabel select;
    private JLabel deselect;
    private JButton selectAllBottom;
    private JButton deselectAllBottom;

    public Window(TimeManager manager) {
        super();
        this.manager = manager;

        initComponents();
    }

    private void initComponents() {        
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLayeredPane jp = new JLayeredPane();

        linkPanelTop = new LinkPanelTop(manager);
        linkPanelTop.setPreferredSize(new Dimension(manager.getTopLinkPanelTotalLength(), 300));

        linkPanelBottom = new LinkPanelBottom(manager);
        linkPanelBottom.setPreferredSize(new Dimension(manager.getBottomLinkPanelTotalLength(), 300));

        graphPanel = new GraphPanel(manager);
        graphPanel.setBounds(350, 0, 1000, 450);
        graphPanel.update(manager.getVisibleTimeLinksIterator());
        
        timePanel = new TimePanel(manager, linkPanelTop, linkPanelBottom, graphPanel);
        timePanel.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 100 + 400));

        linksTop = new JScrollPane(linkPanelTop);
        linksTop.setBounds(350, 400, 1000, 250);
        linksTop.setBorder(BorderFactory.createEmptyBorder());
        linksTop.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        linksTop.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

        linksBottom = new JScrollPane(linkPanelBottom);
        linksBottom.setBounds(350, 750, 1000, 190);
        linksBottom.setBorder(BorderFactory.createEmptyBorder());
        linksBottom.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        linksBottom.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
        
        times = new JScrollPane(timePanel);
        times.setBounds(345, 650, 1010, 100 + 400);
        times.setBorder(BorderFactory.createEmptyBorder());
        times.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        times.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

        tp = new TransparentPanel(manager);
        tp.setBounds(345, 650, 1010, 100);
        tp.setOpaque(false);

        durationScalingFactorBar = new JSlider();
        durationScalingFactorBar.setMinimum(1);
        durationScalingFactorBar.setMaximum(100);
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

        timePanelScalingFactorBar = new JSlider();
        timePanelScalingFactorBar.setMinimum(1);
        timePanelScalingFactorBar.setMaximum(1000);
        timePanelScalingFactorBar.setValue(1);
        timePanelScalingFactorBar.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                manager.setTimePanelScalingFactor(Math.pow((double) timePanelScalingFactorBar.getValue() / 75D, 2) + 1);

                timePanel.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 100));
                times.getViewport().setViewPosition(new Point(manager.getTimePanelTotalLength(), 100));

                timePanel.scrollBy(0);

                timePanel.repaint();
                graphPanel.update(manager.getVisibleTimeLinksIterator());
            }
        });
        timePanelScalingFactorBar.setBounds(5, 600, 200, 50);

        dtm = new DefaultTableModel();
        dtm.setDataVector(manager.getNodeLookup(), columns);

        dtm.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == -1) {
                    return;
                }

                manager.getNodeLookup()[e.getFirstRow()][e.getColumn()] = dtm.getValueAt(e.getFirstRow(), e.getColumn());
                timePanel.repaint();
                graphPanel.update(manager.getVisibleTimeLinksIterator());
            }
        });

        visiblePanel = new JTable(dtm);

        visiblePanel.getColumnModel().getColumn(1).setCellRenderer(visiblePanel.getDefaultRenderer(Boolean.class));
        visiblePanel.getColumnModel().getColumn(1).setCellEditor(visiblePanel.getDefaultEditor(Boolean.class));

        visiblePanel.getColumnModel().getColumn(2).setCellRenderer(visiblePanel.getDefaultRenderer(Boolean.class));
        visiblePanel.getColumnModel().getColumn(2).setCellEditor(visiblePanel.getDefaultEditor(Boolean.class));

        visibleChooser = new JScrollPane(visiblePanel);
        visibleChooser.setBounds(0, 0, 210, 500);

        select = new JLabel("Select:");
        select.setBounds(5, 510, 60, 10);

        deselect = new JLabel("Deselect:");
        deselect.setBounds(5, 540, 60, 10);

        selectAllTop = new JButton("Top");
        selectAllTop.setBounds(60, 500, 75, 30);

        selectAllTop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < manager.getNodeLookup().length; i++) {
                    manager.getNodeLookup()[i][1] = true;
                }

                reloadVisibleTable();
            }
        });

        selectAllBottom = new JButton("Bottom");
        selectAllBottom.setBounds(135, 500, 75, 30);

        selectAllBottom.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < manager.getNodeLookup().length; i++) {
                    manager.getNodeLookup()[i][2] = true;
                }

                reloadVisibleTable();
            }
        });

        deselectAllTop = new JButton("Top");
        deselectAllTop.setBounds(60, 530, 75, 30);

        deselectAllTop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < manager.getNodeLookup().length; i++) {
                    manager.getNodeLookup()[i][1] = false;
                }

                reloadVisibleTable();
            }
        });

        deselectAllBottom = new JButton("Bottom");
        deselectAllBottom.setBounds(135, 530, 75, 30);

        deselectAllBottom.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < manager.getNodeLookup().length; i++) {
                    manager.getNodeLookup()[i][2] = false;
                }

                reloadVisibleTable();
            }
        });

        jp.add(graphPanel, new Integer(0));
        jp.add(times, new Integer(1));
        jp.add(linksTop, new Integer(2));
        jp.add(linksBottom, new Integer(2));
        jp.add(tp, new Integer(3));
        jp.add(durationScalingFactorBar, new Integer(4));
        jp.add(timePanelScalingFactorBar, new Integer(4));
        jp.add(visibleChooser, new Integer(4));
        jp.add(select, new Integer(4));
        jp.add(deselect, new Integer(4));
        jp.add(selectAllTop, new Integer(4));
        jp.add(selectAllBottom, new Integer(4));
        jp.add(deselectAllTop, new Integer(4));
        jp.add(deselectAllBottom, new Integer(4));

        add(jp);
    }

    public void reloadVisibleTable() {
        dtm.setDataVector(manager.getNodeLookup(), columns);

        visiblePanel.getColumnModel().getColumn(1).setCellRenderer(visiblePanel.getDefaultRenderer(Boolean.class));
        visiblePanel.getColumnModel().getColumn(1).setCellEditor(visiblePanel.getDefaultEditor(Boolean.class));

        visiblePanel.getColumnModel().getColumn(2).setCellRenderer(visiblePanel.getDefaultRenderer(Boolean.class));
        visiblePanel.getColumnModel().getColumn(2).setCellEditor(visiblePanel.getDefaultEditor(Boolean.class));

        timePanel.scrollBy(0);

        timePanel.repaint();
    }
}
