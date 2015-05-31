package org.tokalac.logging.core;

/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 */

public class AbstractLevel {

    protected String name;
    protected int value;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
}
