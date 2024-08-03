package me.shedaniel.clothconfig2.impl.builders;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import me.shedaniel.clothconfig2.gui.entries.KeyCodeEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class KeyCodeBuilder extends FieldBuilder<ModifierKeyCode, KeyCodeEntry, KeyCodeBuilder> {

    @Nullable
    private Consumer<ModifierKeyCode> saveConsumer = null;

    @NotNull
    private Function<ModifierKeyCode, Optional<Component[]>> tooltipSupplier = bool -> Optional.empty();

    private final ModifierKeyCode value;

    private boolean allowKey = true;

    private boolean allowMouse = true;

    private boolean allowModifiers = true;

    public KeyCodeBuilder(Component resetButtonKey, Component fieldNameKey, ModifierKeyCode value) {
        super(resetButtonKey, fieldNameKey);
        this.value = ModifierKeyCode.copyOf(value);
    }

    public KeyCodeBuilder setAllowModifiers(boolean allowModifiers) {
        this.allowModifiers = allowModifiers;
        if (!allowModifiers) {
            this.value.setModifier(Modifier.none());
        }
        return this;
    }

    public KeyCodeBuilder setAllowKey(boolean allowKey) {
        if (!this.allowMouse && !allowKey) {
            throw new IllegalArgumentException();
        } else {
            this.allowKey = allowKey;
            return this;
        }
    }

    public KeyCodeBuilder setAllowMouse(boolean allowMouse) {
        if (!this.allowKey && !allowMouse) {
            throw new IllegalArgumentException();
        } else {
            this.allowMouse = allowMouse;
            return this;
        }
    }

    public KeyCodeBuilder setErrorSupplier(@Nullable Function<InputConstants.Key, Optional<Component>> errorSupplier) {
        return this.setModifierErrorSupplier(keyCode -> (Optional) errorSupplier.apply(keyCode.getKeyCode()));
    }

    public KeyCodeBuilder setModifierErrorSupplier(@Nullable Function<ModifierKeyCode, Optional<Component>> errorSupplier) {
        this.errorSupplier = errorSupplier;
        return this;
    }

    public KeyCodeBuilder requireRestart() {
        this.requireRestart(true);
        return this;
    }

    public KeyCodeBuilder setKeySaveConsumer(Consumer<InputConstants.Key> saveConsumer) {
        return this.setModifierSaveConsumer(keyCode -> saveConsumer.accept(keyCode.getKeyCode()));
    }

    public KeyCodeBuilder setDefaultValue(Supplier<InputConstants.Key> defaultValue) {
        return this.setModifierDefaultValue(() -> ModifierKeyCode.of((InputConstants.Key) defaultValue.get(), Modifier.none()));
    }

    public KeyCodeBuilder setModifierSaveConsumer(Consumer<ModifierKeyCode> saveConsumer) {
        this.saveConsumer = saveConsumer;
        return this;
    }

    public KeyCodeBuilder setModifierDefaultValue(Supplier<ModifierKeyCode> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public KeyCodeBuilder setDefaultValue(InputConstants.Key defaultValue) {
        return this.setDefaultValue(ModifierKeyCode.of(defaultValue, Modifier.none()));
    }

    public KeyCodeBuilder setDefaultValue(ModifierKeyCode defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public KeyCodeBuilder setKeyTooltipSupplier(@NotNull Function<InputConstants.Key, Optional<Component[]>> tooltipSupplier) {
        return this.setModifierTooltipSupplier(keyCode -> (Optional) tooltipSupplier.apply(keyCode.getKeyCode()));
    }

    public KeyCodeBuilder setModifierTooltipSupplier(@NotNull Function<ModifierKeyCode, Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }

    public KeyCodeBuilder setTooltipSupplier(@NotNull Supplier<Optional<Component[]>> tooltipSupplier) {
        this.tooltipSupplier = bool -> (Optional) tooltipSupplier.get();
        return this;
    }

    public KeyCodeBuilder setTooltip(Optional<Component[]> tooltip) {
        this.tooltipSupplier = bool -> tooltip;
        return this;
    }

    public KeyCodeBuilder setTooltip(@Nullable Component... tooltip) {
        this.tooltipSupplier = bool -> Optional.ofNullable(tooltip);
        return this;
    }

    @NotNull
    public KeyCodeEntry build() {
        KeyCodeEntry entry = new KeyCodeEntry(this.getFieldNameKey(), this.value, this.getResetButtonKey(), this.defaultValue, this.saveConsumer, null, this.isRequireRestart());
        entry.setTooltipSupplier(() -> (Optional) this.tooltipSupplier.apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> (Optional) this.errorSupplier.apply(entry.getValue()));
        }
        entry.setAllowKey(this.allowKey);
        entry.setAllowMouse(this.allowMouse);
        entry.setAllowModifiers(this.allowModifiers);
        return this.finishBuilding(entry);
    }
}