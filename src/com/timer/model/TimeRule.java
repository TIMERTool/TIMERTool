package com.timer.model;


import com.timer.model.TimeLink;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Peter Hoek
 */
public abstract class TimeRule {
    public abstract boolean evaluate(TimeLink link);
}