package dev.kosmx.playerAnim.api.firstPerson;

import org.jetbrains.annotations.ApiStatus.Internal;

public enum FirstPersonMode {

    NONE(false), VANILLA(true), THIRD_PERSON_MODEL(true), DISABLED(false);

    private final boolean enabled;

    private static final ThreadLocal<Boolean> firstPersonPass = ThreadLocal.withInitial(() -> false);

    private FirstPersonMode(boolean enabled) {
        this.enabled = enabled;
    }

    public static boolean isFirstPersonPass() {
        return (Boolean) firstPersonPass.get();
    }

    @Internal
    public static void setFirstPersonPass(boolean newValue) {
        firstPersonPass.set(newValue);
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}