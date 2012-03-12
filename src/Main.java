
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;

public class Main extends JFrame {

    public static final int INITALTIMEPANELLENGTH = 500;
    public static final int INITALLINKPANELLENGTH = 1000;
    public static final int INITALWINDOWTIME = 0;
    public static final int WINDOWLENGTH = 990;

    public static void main(String[] args) {
        new Main().setVisible(true);
    }

    public Main() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(1000, 1500);

            JLayeredPane jp = new JLayeredPane();
            add(jp);

            TimeManager manager = new TimeManager(new File("input.csv"), INITALTIMEPANELLENGTH, INITALLINKPANELLENGTH, INITALWINDOWTIME, WINDOWLENGTH);

            LinkPanelTop linkPanelTop = new LinkPanelTop(manager);
            linkPanelTop.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 300));
            
            LinkPanelBottom linkPanelBottom = new LinkPanelBottom(manager);
            linkPanelBottom.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 300));

            TimePanel timePanel = new TimePanel(manager, linkPanelTop, linkPanelBottom);
            timePanel.setPreferredSize(new Dimension(manager.getTimePanelTotalLength(), 100));

            JScrollPane linksTop = new JScrollPane(linkPanelTop);
            linksTop.setBounds(300, 650, 1000, 100);
            linksTop.setBorder(BorderFactory.createEmptyBorder());
            linksTop.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            linksTop.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
            
            JScrollPane linksBottom = new JScrollPane(linkPanelBottom);
            linksBottom.setBounds(300, 850, 1000, 100);
            linksBottom.setBorder(BorderFactory.createEmptyBorder());
            linksBottom.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            linksBottom.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

            JScrollPane times = new JScrollPane(timePanel);
            times.setBounds(300, 750, 1000, 100);
            times.setBorder(BorderFactory.createEmptyBorder());
            times.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            times.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);

            TransparentPanel tp = new TransparentPanel(manager);
            tp.setBounds(300, 750, 1000, 100);
            tp.setOpaque(false);

            jp.add(linksTop, new Integer(0));
            jp.add(linksBottom, new Integer(0));
            jp.add(times, new Integer(1));
            jp.add(tp, new Integer(2));
        } catch (IOException io) {
        }
    }
}
