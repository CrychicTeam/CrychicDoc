package net.blay09.mods.balm.api.event.client;

public class KeyInputEvent {

    private final int key;

    private final int scanCode;

    private final int action;

    private final int modifiers;

    public KeyInputEvent(int key, int scanCode, int action, int modifiers) {
        this.key = key;
        this.scanCode = scanCode;
        this.action = action;
        this.modifiers = modifiers;
    }

    public int getKey() {
        return this.key;
    }

    public int getScanCode() {
        return this.scanCode;
    }

    public int getAction() {
        return this.action;
    }

    public int getModifiers() {
        return this.modifiers;
    }
}