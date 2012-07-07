/*
 * Copyright (c) 2012, Peter Hoek 
 * All rights reserved.
 */

import java.util.Calendar;
import org.timer.model.AbstractDate;
import org.timer.model.AbstractDateFormatter;
import org.timer.model.CalendarDate;

/**
 *
 * @author Peter Hoek
 */
public class VASTDateFormatter extends AbstractDateFormatter {

    @Override
    public String format(AbstractDate date) {
        if (date instanceof CalendarDate) {
            CalendarDate calendarDate = (CalendarDate) date;

            return intToTwoDigitString(calendarDate.getCalendar().get(Calendar.HOUR_OF_DAY))
                    + ":"
                    + intToTwoDigitString(calendarDate.getCalendar().get(Calendar.MINUTE))
                    + " "
                    + formatPlace(calendarDate.getCalendar().get(Calendar.DAY_OF_MONTH));
        } else {
            throw new IllegalArgumentException("TimeDate supplied was not a CalendarDate.");
        }
    }

    private String intToTwoDigitString(int i) {
        String str = new Integer(i).toString();

        if (str.length() == 1) {
            str = "0" + str;
        }

        return str;
    }

    private String formatPlace(int i) {
        switch (i % 10) {
            case 1:
                return i + "st";
            case 2:
                return i + "nd";
            case 3:
                return i + "rd";
            default:
                return i + "th";
        }
    }
}
