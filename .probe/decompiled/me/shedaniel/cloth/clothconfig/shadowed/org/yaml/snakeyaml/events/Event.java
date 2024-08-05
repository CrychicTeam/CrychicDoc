package me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.events;

import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.error.Mark;

public abstract class Event {

    private final Mark startMark;

    private final Mark endMark;

    public Event(Mark startMark, Mark endMark) {
        this.startMark = startMark;
        this.endMark = endMark;
    }

    public String toString() {
        return "<" + this.getClass().getName() + "(" + this.getArguments() + ")>";
    }

    public Mark getStartMark() {
        return this.startMark;
    }

    public Mark getEndMark() {
        return this.endMark;
    }

    protected String getArguments() {
        return "";
    }

    public boolean is(Event.ID id) {
        return this.getEventId() == id;
    }

    public abstract Event.ID getEventId();

    public boolean equals(Object obj) {
        return obj instanceof Event ? this.toString().equals(obj.toString()) : false;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public static enum ID {

        Alias,
        DocumentEnd,
        DocumentStart,
        MappingEnd,
        MappingStart,
        Scalar,
        SequenceEnd,
        SequenceStart,
        StreamEnd,
        StreamStart
    }
}