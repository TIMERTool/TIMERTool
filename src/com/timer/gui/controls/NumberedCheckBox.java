package com.timer.gui.controls;

import javax.swing.JCheckBox;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Peter Hoek
 */
public class NumberedCheckBox extends JCheckBox {
    private final int number;
    
    public NumberedCheckBox(int number) {
        this.number = number;
    }
    
    public int getNumber() {
        return number;
    }
}
