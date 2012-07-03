/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.core;

import java.io.File;
import java.io.IOException;
import org.timer.gui.Window;
import org.timer.model.TimeManager;

/**
 *
 * @author Peter Hoek
 */
public class Main {

    public static final int WINDOWLENGTH = 990;

    public static void main(String args[]) throws IOException {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        new Window(new TimeManager(new File("input.csv"), WINDOWLENGTH)).setVisible(true);
    }
}
