package net.blay09.mods.waystones.config;

public class InventoryButtonMode {

    private final String value;

    public InventoryButtonMode(String value) {
        this.value = value;
    }

    public boolean isEnabled() {
        return !this.value.isEmpty() && !"NONE".equals(this.value);
    }

    public boolean isReturnToNearest() {
        return "NEAREST".equals(this.value);
    }

    public boolean isReturnToAny() {
        return "ANY".equals(this.value);
    }

    public boolean hasNamedTarget() {
        return this.isEnabled() && !this.isReturnToNearest() && !this.isReturnToAny();
    }

    public String getNamedTarget() {
        return this.value;
    }
}