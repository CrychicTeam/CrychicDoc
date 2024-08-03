package com.mna.api.timing;

public interface IDelayedEvent {

    boolean tick();

    String getID();
}