/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.model;

/**
 *
 * @author Peter Hoek
 */
public abstract class TimeRule {

    public abstract boolean evaluate(TimeLink link);
}
