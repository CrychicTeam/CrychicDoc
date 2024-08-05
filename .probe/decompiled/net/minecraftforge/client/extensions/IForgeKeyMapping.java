package net.minecraftforge.client.extensions;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.jetbrains.annotations.NotNull;

public interface IForgeKeyMapping {

    private KeyMapping self() {
        return (KeyMapping) this;
    }

    @NotNull
    InputConstants.Key getKey();

    default boolean isActiveAndMatches(InputConstants.Key keyCode) {
        return keyCode != InputConstants.UNKNOWN && keyCode.equals(this.getKey()) && this.getKeyConflictContext().isActive() && this.getKeyModifier().isActive(this.getKeyConflictContext());
    }

    default void setToDefault() {
        this.setKeyModifierAndCode(this.getDefaultKeyModifier(), this.self().getDefaultKey());
    }

    void setKeyConflictContext(IKeyConflictContext var1);

    IKeyConflictContext getKeyConflictContext();

    KeyModifier getDefaultKeyModifier();

    KeyModifier getKeyModifier();

    void setKeyModifierAndCode(KeyModifier var1, InputConstants.Key var2);

    default boolean isConflictContextAndModifierActive() {
        return this.getKeyConflictContext().isActive() && this.getKeyModifier().isActive(this.getKeyConflictContext());
    }

    default boolean hasKeyModifierConflict(KeyMapping other) {
        return (this.getKeyConflictContext().conflicts(other.getKeyConflictContext()) || other.getKeyConflictContext().conflicts(this.getKeyConflictContext())) && (this.getKeyModifier().matches(other.getKey()) || other.getKeyModifier().matches(this.getKey()));
    }
}