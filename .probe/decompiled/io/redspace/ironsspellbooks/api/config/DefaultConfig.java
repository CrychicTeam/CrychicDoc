package io.redspace.ironsspellbooks.api.config;

import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;

public class DefaultConfig {

    public SpellRarity minRarity;

    public ResourceLocation schoolResource;

    public int maxLevel = -1;

    public boolean enabled = true;

    public double cooldownInSeconds = -1.0;

    public boolean allowCrafting = true;

    public DefaultConfig(Consumer<DefaultConfig> intialize) throws RuntimeException {
        intialize.accept(this);
        this.build();
    }

    public DefaultConfig() {
    }

    public DefaultConfig setMaxLevel(int i) {
        this.maxLevel = i;
        return this;
    }

    public DefaultConfig setDeprecated(boolean deprecated) {
        this.enabled = !deprecated;
        return this;
    }

    public DefaultConfig setMinRarity(SpellRarity i) {
        this.minRarity = i;
        return this;
    }

    public DefaultConfig setCooldownSeconds(double i) {
        this.cooldownInSeconds = i;
        return this;
    }

    public DefaultConfig setSchoolResource(ResourceLocation schoolResource) {
        this.schoolResource = schoolResource;
        return this;
    }

    public DefaultConfig setAllowCrafting(boolean allowCrafting) {
        this.allowCrafting = allowCrafting;
        return this;
    }

    public DefaultConfig build() throws RuntimeException {
        if (!this.validate()) {
            throw new RuntimeException("You didn't define all config attributes!");
        } else {
            return this;
        }
    }

    private boolean validate() {
        return this.minRarity != null && this.maxLevel >= 0 && this.schoolResource != null && this.cooldownInSeconds >= 0.0;
    }
}