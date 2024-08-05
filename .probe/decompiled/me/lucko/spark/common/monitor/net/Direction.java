package me.lucko.spark.common.monitor.net;

public enum Direction {

    RECEIVE("rx"), TRANSMIT("tx");

    private final String abbreviation;

    private Direction(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String abbrev() {
        return this.abbreviation;
    }
}