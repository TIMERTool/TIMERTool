/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.model;

import java.util.Calendar;
import java.util.Objects;

/**
 *
 * @author Peter Hoek
 */
public class CalendarDate extends AbstractDate {

    protected final Calendar date;

    public CalendarDate(Calendar date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CalendarDate) {
            CalendarDate other = (CalendarDate) object;

            if (date.equals(other.date)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 19 * 7 + Objects.hashCode(this.date);
    }

    @Override
    public long getTimeInUnits() {
        return date.getTimeInMillis();
    }

    @Override
    public boolean greaterThan(AbstractDate other) {
        if (other instanceof CalendarDate) {
            return date.after(((CalendarDate) other).date);
        } else {
            throw new IllegalArgumentException("TimeDate supplied was not a CalendarDate.");
        }
    }

    @Override
    public boolean lessThan(AbstractDate other) {
        if (other instanceof CalendarDate) {
            return date.before(((CalendarDate) other).date);
        } else {
            throw new IllegalArgumentException("TimeDate supplied was not a CalendarDate.");
        }
    }

    @Override
    public AbstractDate add(int amount) {
        Calendar calendar = (Calendar) date.clone();
        calendar.add(Calendar.MILLISECOND, amount);

        return new CalendarDate(calendar);
    }

    @Override
    public AbstractDate add(AbstractDate other) {
        if (other instanceof CalendarDate) {
            Calendar calendar = (Calendar) date.clone();
            calendar.setTimeInMillis(getTimeInUnits() + other.getTimeInUnits());

            return new CalendarDate(calendar);
        } else {
            throw new IllegalArgumentException("TimeDate supplied was not a CalendarDate.");
        }
    }

    @Override
    public AbstractDate subtract(int amount) {
        Calendar calendar = (Calendar) date.clone();
        calendar.add(Calendar.MILLISECOND, -1 * amount);

        return new CalendarDate(calendar);
    }

    @Override
    public AbstractDate subtract(AbstractDate other) {
        if (other instanceof CalendarDate) {
            Calendar calendar = (Calendar) date.clone();
            calendar.setTimeInMillis(getTimeInUnits() - other.getTimeInUnits());

            return new CalendarDate(calendar);
        } else {
            throw new IllegalArgumentException("TimeDate supplied was not a CalendarDate.");
        }
    }

    public Calendar getCalendar() {
        return date;
    }
}
