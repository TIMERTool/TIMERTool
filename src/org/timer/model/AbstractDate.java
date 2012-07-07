/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.model;

/**
 *
 * @author Peter Hoek
 */
public abstract class AbstractDate {

    @Override
    public abstract boolean equals(Object object);

    @Override
    public abstract int hashCode();

    public abstract long getTimeInUnits();

    public abstract boolean greaterThan(AbstractDate other);

    public abstract boolean lessThan(AbstractDate other);

    public abstract AbstractDate add(int other);

    public abstract AbstractDate add(AbstractDate other);

    public abstract AbstractDate subtract(int other);

    public abstract AbstractDate subtract(AbstractDate other);
}
