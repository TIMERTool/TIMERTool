/*
 * Copyright (c) 2012, Peter Hoek 
 * All rights reserved.
 */

import java.util.GregorianCalendar;
import org.timer.model.CalendarDate;

/**
 *
 * @author Peter Hoek
 */
public class VASTDate extends CalendarDate {

    public VASTDate(String date) {
        super(new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
                Integer.parseInt(date.substring(4, 6)),
                Integer.parseInt(date.substring(6, 8)),
                Integer.parseInt(date.substring(9, 11)),
                Integer.parseInt(date.substring(11, 13))));
    }
}
