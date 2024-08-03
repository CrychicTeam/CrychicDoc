package me.shedaniel.clothconfig2.impl.builders;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.Requirement;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;

@OnlyIn(Dist.CLIENT)
public abstract class FieldBuilder<T, A extends AbstractConfigListEntry, SELF extends FieldBuilder<T, A, SELF>> {

    @NotNull
    private final Component fieldNameKey;

    @NotNull
    private final Component resetButtonKey;

    protected boolean requireRestart = false;

    @Nullable
    protected Supplier<T> defaultValue = null;

    @Nullable
    protected Function<T, Optional<Component>> errorSupplier;

    @Nullable
    protected Requirement enableRequirement = null;

    @Nullable
    protected Requirement displayRequirement = null;

    protected FieldBuilder(Component resetButtonKey, Component fieldNameKey) {
        this.resetButtonKey = (Component) Objects.requireNonNull(resetButtonKey);
        this.fieldNameKey = (Component) Objects.requireNonNull(fieldNameKey);
    }

    @Nullable
    public final Supplier<T> getDefaultValue() {
        return this.defaultValue;
    }

    @Deprecated
    public final AbstractConfigListEntry buildEntry() {
        return this.build();
    }

    @NotNull
    public abstract A build();

    @Contract(value = "_ -> param1", mutates = "param1")
    protected A finishBuilding(A gui) {
        if (gui == null) {
            return null;
        } else {
            if (this.enableRequirement != null) {
                gui.setRequirement(this.enableRequirement);
            }
            if (this.displayRequirement != null) {
                gui.setDisplayRequirement(this.displayRequirement);
            }
            return gui;
        }
    }

    @NotNull
    public final Component getFieldNameKey() {
        return this.fieldNameKey;
    }

    @NotNull
    public final Component getResetButtonKey() {
        return this.resetButtonKey;
    }

    public boolean isRequireRestart() {
        return this.requireRestart;
    }

    public void requireRestart(boolean requireRestart) {
        this.requireRestart = requireRestart;
    }

    @Contract(mutates = "this")
    @Experimental
    public final SELF setRequirement(Requirement requirement) {
        this.enableRequirement = requirement;
        return (SELF) this;
    }

    @Contract(mutates = "this")
    @Experimental
    public final SELF setDisplayRequirement(Requirement requirement) {
        this.displayRequirement = requirement;
        return (SELF) this;
    }
}