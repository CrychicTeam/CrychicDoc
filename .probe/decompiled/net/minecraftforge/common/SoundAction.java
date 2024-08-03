package net.minecraftforge.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SoundAction {

    private static final Map<String, SoundAction> ACTIONS = new ConcurrentHashMap();

    private final String name;

    public static SoundAction get(String name) {
        return (SoundAction) ACTIONS.computeIfAbsent(name, SoundAction::new);
    }

    private SoundAction(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public String toString() {
        return "SoundAction[" + this.name + "]";
    }
}