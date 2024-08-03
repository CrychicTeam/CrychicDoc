package org.violetmoon.quark.api;

public interface IPistonCallback {

    void onPistonMovementStarted();

    default void onPistonMovementFinished() {
    }
}