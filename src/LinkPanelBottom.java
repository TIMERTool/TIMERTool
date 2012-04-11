
import java.awt.Graphics;
import java.awt.Graphics2D;

public class LinkPanelBottom extends ScrollPanel {

    private final TimeManager manager;

    public LinkPanelBottom(TimeManager manager) {
        this.manager = manager;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        TimeLinkIterator it = manager.getVisibleTimeLinksIterator();

        while (it.hasNext()) {
            TimeLink upTo = it.next();
            int pixel = manager.bottomNodeToPixel(upTo.getBottomNode());

            if (pixel != -1) {
                g2.setColor(upTo.getColour());
                g2.drawLine(pixel, 174, manager.linkTimeToPixel(upTo.getTime()) + (getVisibleStart() - manager.getTimePanelVisibleStart()), 0);
            }
        }
    }
}
