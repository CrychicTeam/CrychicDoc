package me.shedaniel.clothconfig2.impl;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModifierKeyCodeImpl implements ModifierKeyCode {

    private InputConstants.Key keyCode;

    private Modifier modifier;

    @Override
    public InputConstants.Key getKeyCode() {
        return this.keyCode;
    }

    @Override
    public Modifier getModifier() {
        return this.modifier;
    }

    @Override
    public ModifierKeyCode setKeyCode(InputConstants.Key keyCode) {
        this.keyCode = keyCode.getType().getOrCreate(keyCode.getValue());
        if (keyCode.equals(InputConstants.UNKNOWN)) {
            this.setModifier(Modifier.none());
        }
        return this;
    }

    @Override
    public ModifierKeyCode setModifier(Modifier modifier) {
        this.modifier = Modifier.of(modifier.getValue());
        return this;
    }

    @Override
    public String toString() {
        return this.getLocalizedName().getString();
    }

    @Override
    public Component getLocalizedName() {
        Component base = this.keyCode.getDisplayName();
        if (this.modifier.hasShift()) {
            base = Component.translatable("modifier.cloth-config.shift", base);
        }
        if (this.modifier.hasControl()) {
            base = Component.translatable("modifier.cloth-config.ctrl", base);
        }
        if (this.modifier.hasAlt()) {
            base = Component.translatable("modifier.cloth-config.alt", base);
        }
        return base;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof ModifierKeyCode that) ? false : this.keyCode.equals(that.getKeyCode()) && this.modifier.equals(that.getModifier());
        }
    }

    public int hashCode() {
        int result = this.keyCode != null ? this.keyCode.hashCode() : 0;
        return 31 * result + (this.modifier != null ? this.modifier.hashCode() : 0);
    }
}