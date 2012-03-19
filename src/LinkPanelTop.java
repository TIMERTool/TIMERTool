
import java.awt.Graphics;
import java.awt.Graphics2D;

public class LinkPanelTop extends ScrollPanel {

    private final TimeManager manager;

    public LinkPanelTop(TimeManager manager) {
        this.manager = manager;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        TimeLinkIterator it = manager.getVisibleTimeLinksIterator();

        while (it.hasNext()) {
            TimeLink upTo = it.next();
            int pixel = manager.topNodeToPixel(upTo.getTopNode());

            if (pixel != -1) {
                g2.setColor(upTo.getColour());
                g2.drawLine(pixel, 0, manager.linkTimeToPixel(upTo.getTime()) + (getVisibleStart() - manager.getTimePanelVisibleStart()), 174);
            }
        }
    }
}
