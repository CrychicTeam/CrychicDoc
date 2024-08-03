package com.rekindled.embers.api.filter;

public abstract class ComparatorNormal implements IFilterComparator {

    private String name;

    private int priority;

    public ComparatorNormal(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public String getName() {
        return this.name;
    }
}