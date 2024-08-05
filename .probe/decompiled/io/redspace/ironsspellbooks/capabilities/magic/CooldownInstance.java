package io.redspace.ironsspellbooks.capabilities.magic;

public class CooldownInstance {

    private int cooldownRemaining;

    private final int spellCooldown;

    public CooldownInstance(int spellCooldown) {
        this.spellCooldown = spellCooldown;
        this.cooldownRemaining = spellCooldown;
    }

    public CooldownInstance(int spellCooldown, int cooldownRemaining) {
        this.spellCooldown = spellCooldown;
        this.cooldownRemaining = cooldownRemaining;
    }

    public void decrement() {
        this.cooldownRemaining--;
    }

    public void decrementBy(int amount) {
        this.cooldownRemaining -= amount;
    }

    public int getCooldownRemaining() {
        return this.cooldownRemaining;
    }

    public int getSpellCooldown() {
        return this.spellCooldown;
    }

    public float getCooldownPercent() {
        return this.cooldownRemaining == 0 ? 0.0F : (float) this.cooldownRemaining / (float) this.spellCooldown;
    }
}