/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.timer.model.AbstractData;
import org.timer.model.AbstractDate;
import org.timer.model.CalendarDate;

/**
 *
 * @author Peter Hoek
 */
public class VASTData extends AbstractData {

    public static final Color[] colors = {
        Color.RED,
        Color.ORANGE,
        Color.YELLOW,
        Color.GREEN,
        Color.BLUE,
        Color.CYAN,
        Color.MAGENTA,
        Color.BLACK,
        Color.WHITE,
        new Color (0,50,50),
        new Color (50,0,50),
        new Color (0,100,0),
        new Color (50,50,0),
        new Color (50,0,0)
           
    };

    public VASTData(File input) {
        super(new NodeColorDefinition(colors));

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(input));
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                addLink(Integer.parseInt(values[0]), Integer.parseInt(values[1]), new VASTDate(values[2]), Integer.parseInt(values[3]) + 1);
            }
        } catch (IOException ex) {
            Logger.getLogger(VASTData.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(VASTData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public AbstractDate makeDate(long timeInUnits) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timeInUnits);

        return new CalendarDate(calendar);
    }
}
