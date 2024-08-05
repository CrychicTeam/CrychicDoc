package com.mna.api.spells;

public enum ComponentApplicationResult {

    SUCCESS(true), DELAYED(true), FAIL(false), TARGET_ENTITY_SPAWNED(true), NOT_PRESENT(false);

    public final boolean is_success;

    private ComponentApplicationResult(boolean success) {
        this.is_success = success;
    }

    public static ComponentApplicationResult fromBoolean(boolean b) {
        return b ? SUCCESS : FAIL;
    }

    public ComponentApplicationResult mergeWith(ComponentApplicationResult componentApplicationResult) {
        switch(componentApplicationResult) {
            case FAIL:
                return this.is_success ? this : componentApplicationResult;
            case NOT_PRESENT:
                return this;
            case SUCCESS:
            case DELAYED:
            case TARGET_ENTITY_SPAWNED:
                return componentApplicationResult;
            default:
                return this;
        }
    }
}