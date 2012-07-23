/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.timer.model.Model;

/**
 *
 * @author Peter Hoek
 */
public class GUI extends JFrame {

    public static final int WINDOWLENGTH = 1010;
    public static final String[] columns = {"Node", "Top?", "Bottom?"};
    private final Model model;
    private EdgeTopPanel edgeTopPanel;
    private JScrollPane edgesTop;
    private EdgeBottomPanel edgeBottomPanel;
    private JScrollPane edgesBottom;
    private TimePanel timePanel;
    private JScrollPane times;
    private TransparentPanel transparentPanel;
    private JLabel graphPanelEdgeScalingFactorBarLabel;
    private JSlider graphPanelEdgeScalingFactorBar;
    private JLabel durationScalingFactorBarLabel;
    private JSlider durationScalingFactorBar;
    private JButton selectAllTop;
    private JButton deselectAllTop;
    private JTable visiblePanel;
    private DefaultTableModel tableModel;
    private JScrollPane visibleChooser;
    private GraphPanel graphPanel;
    private JLabel select;
    private JLabel deselect;
    private JButton selectAllBottom;
    private JButton deselectAllBottom;

    public GUI(Model model) {
        super();
        this.model = model;

        initComponents();
        
    }

    private void initComponents() {
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLayeredPane jp = new JLayeredPane();
      

        edgeTopPanel = new EdgeTopPanel(model);
        edgeTopPanel.setPreferredSize(new Dimension(model.getTopLinkPanelTotalLength(), 300));

        edgeBottomPanel = new EdgeBottomPanel(model);
        edgeBottomPanel.setPreferredSize(new Dimension(model.getBottomLinkPanelTotalLength(), 300));

        graphPanel = new GraphPanel(model, WINDOWLENGTH, 475);
        graphPanel.setBounds(220, 0, WINDOWLENGTH, 450);
        graphPanel.update(model.getVisibleTimeLinksIterator());

        timePanel = new TimePanel(model, edgeTopPanel, edgeBottomPanel, graphPanel);
        timePanel.setPreferredSize(new Dimension(model.getTimePanelTotalLength(), 100 + 400));

        edgesTop = new JScrollPane(edgeTopPanel);
        edgesTop.setBounds(220, 451, WINDOWLENGTH - 10, 200);
        edgesTop.setBorder(BorderFactory.createEmptyBorder());
        edgesTop.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        edgesTop.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

        edgesBottom = new JScrollPane(edgeBottomPanel);
        edgesBottom.setBounds(220, 750, WINDOWLENGTH - 10, 198);
        edgesBottom.setBorder(BorderFactory.createEmptyBorder());
        edgesBottom.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        edgesBottom.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

        times = new JScrollPane(timePanel);
        times.setBounds(215, 650, WINDOWLENGTH, 100 + 400);
        times.setBorder(BorderFactory.createEmptyBorder());
        times.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        times.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
        times.getViewport().setBackground(new Color (93,98,107));
        

        transparentPanel = new TransparentPanel(model);
        transparentPanel.setBounds(215, 650, WINDOWLENGTH, 100);
        transparentPanel.setOpaque(false);

        graphPanelEdgeScalingFactorBarLabel = new JLabel("Graph Edge Stroke Threshold:");
        graphPanelEdgeScalingFactorBarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        graphPanelEdgeScalingFactorBarLabel.setBounds(5, 550, 200, 50);

        graphPanelEdgeScalingFactorBar = new JSlider();
        graphPanelEdgeScalingFactorBar.setMinimum(1);
        graphPanelEdgeScalingFactorBar.setMaximum(10);
        graphPanelEdgeScalingFactorBar.setValue(1);
        graphPanelEdgeScalingFactorBar.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                model.setGraphPanelEdgeScalingFactor(graphPanelEdgeScalingFactorBar.getValue());

                graphPanel.update(model.getVisibleTimeLinksIterator());
            }
        });
        graphPanelEdgeScalingFactorBar.setBounds(5, 570, 200, 50);

        durationScalingFactorBarLabel = new JLabel("Duration Scaling Factor:");
        durationScalingFactorBarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        durationScalingFactorBarLabel.setBounds(5, 590, 200, 50);

        durationScalingFactorBar = new JSlider();
        durationScalingFactorBar.setMinimum(1);
        durationScalingFactorBar.setMaximum(1000);
        durationScalingFactorBar.setValue(1);
        durationScalingFactorBar.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                double val = Math.pow(((double) durationScalingFactorBar.getValue() / 75D), 2);

                if (durationScalingFactorBar.getValue() == 1) {
                    val = 0;
                }

                model.setDurationScalingFactor(val);

                timePanel.repaint();
            }
        });
        durationScalingFactorBar.setBounds(5, 610, 200, 50);

        tableModel = new DefaultTableModel();
        tableModel.setDataVector(model.getNodeLookup(), columns);

        tableModel.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == -1) {
                    return;
                }

                model.getNodeLookup()[e.getFirstRow()][e.getColumn()] = tableModel.getValueAt(e.getFirstRow(), e.getColumn());
                timePanel.repaint();
                graphPanel.update(model.getVisibleTimeLinksIterator());
            }
        });

        visiblePanel = new JTable(tableModel);

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
                for (int i = 0; i < model.getNodeLookup().length; i++) {
                    model.getNodeLookup()[i][1] = true;
                }

                reloadVisibleTable();
            }
        });

        selectAllBottom = new JButton("Bottom");
        selectAllBottom.setBounds(135, 500, 75, 30);

        selectAllBottom.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < model.getNodeLookup().length; i++) {
                    model.getNodeLookup()[i][2] = true;
                }

                reloadVisibleTable();
            }
        });

        deselectAllTop = new JButton("Top");
        deselectAllTop.setBounds(60, 530, 75, 30);

        deselectAllTop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < model.getNodeLookup().length; i++) {
                    model.getNodeLookup()[i][1] = false;
                }

                reloadVisibleTable();
            }
        });

        deselectAllBottom = new JButton("Bottom");
        deselectAllBottom.setBounds(135, 530, 75, 30);

        deselectAllBottom.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < model.getNodeLookup().length; i++) {
                    model.getNodeLookup()[i][2] = false;
                }

                reloadVisibleTable();
            }
        });

        jp.add(graphPanel, new Integer(0));
        jp.add(times, new Integer(1));
        jp.add(edgesTop, new Integer(2));
        jp.add(edgesBottom, new Integer(2));
        jp.add(transparentPanel, new Integer(3));
        jp.add(graphPanelEdgeScalingFactorBarLabel, new Integer(4));
        jp.add(graphPanelEdgeScalingFactorBar, new Integer(4));
        jp.add(durationScalingFactorBarLabel, new Integer(4));
        jp.add(durationScalingFactorBar, new Integer(4));
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
        tableModel.setDataVector(model.getNodeLookup(), columns);

        visiblePanel.getColumnModel().getColumn(1).setCellRenderer(visiblePanel.getDefaultRenderer(Boolean.class));
        visiblePanel.getColumnModel().getColumn(1).setCellEditor(visiblePanel.getDefaultEditor(Boolean.class));

        visiblePanel.getColumnModel().getColumn(2).setCellRenderer(visiblePanel.getDefaultRenderer(Boolean.class));
        visiblePanel.getColumnModel().getColumn(2).setCellEditor(visiblePanel.getDefaultEditor(Boolean.class));

        timePanel.scrollBy(0);

        timePanel.repaint();
    }
}
