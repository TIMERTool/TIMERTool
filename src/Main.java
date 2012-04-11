
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Peter Hoek
 */
public class Main extends JFrame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main().setVisible(true);
    }

    public Main() {
        add(new TIMERPanel());
    }
}
