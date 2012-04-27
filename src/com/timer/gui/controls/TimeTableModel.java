package com.timer.gui.controls;


import com.timer.model.TimeManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.table.AbstractTableModel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Peter Hoek
 */
public class TimeTableModel extends AbstractTableModel {

    private TimeManager manager;
    private ArrayList<JCheckBox> checkBoxes = new ArrayList<>();

    public TimeTableModel(final TimeManager manager) {
        this.manager = manager;

        for (int i = 0; i < manager.getNodeList().size(); i++) {
            NumberedCheckBox checkBox = new NumberedCheckBox(i);
            checkBox.setText("");
            checkBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    manager.setNodeVisibility(((NumberedCheckBox)e.getSource()).getNumber(), checkBoxes.get(((NumberedCheckBox)e.getSource()).getNumber()).isSelected());
                }
            });

            checkBoxes.add(checkBox);
        }
    }

    @Override
    public int getRowCount() {
        return manager.getNodeList().size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return manager.getNodeName(manager.getNodeList().get(rowIndex));
        } else if (columnIndex == 1) {
            return checkBoxes.get(rowIndex);
        } else {
            throw new IllegalArgumentException("Illegal Column!");
        }
    }
}