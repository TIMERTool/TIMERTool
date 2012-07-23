/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.swing.UIManager;
import org.timer.gui.GUI;
import org.timer.model.Model;

/**
 *
 * @author Peter Hoek
 */
public class Main {

    public static final int WINDOWLENGTH = 1010;

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
                    UIManager.getLookAndFeelDefaults().put("Panel.background", new Color (93,98,107));


                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        new GUI(new Model(new VASTData(new File("source.csv")), new VASTDateFormatter(), WINDOWLENGTH)).setVisible(true);
    }
}
