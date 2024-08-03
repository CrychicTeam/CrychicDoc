package dev.ftb.mods.ftblibrary.config;

import dev.architectury.fluid.FluidStack;
import dev.ftb.mods.ftblibrary.config.ui.SelectFluidScreen;
import dev.ftb.mods.ftblibrary.config.ui.SelectableResource;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import java.util.OptionalLong;
import net.minecraft.network.chat.Component;

public class FluidConfig extends ResourceConfigValue<FluidStack> {

    private final boolean allowEmpty;

    private final boolean isFixedSize;

    private final long fixedSize;

    private boolean showAmount = true;

    public FluidConfig(boolean allowEmpty) {
        this.isFixedSize = false;
        this.fixedSize = 0L;
        this.allowEmpty = allowEmpty;
        this.defaultValue = FluidStack.empty();
        this.value = FluidStack.empty();
    }

    public FluidConfig(long fixedSize) {
        this.isFixedSize = true;
        this.fixedSize = fixedSize;
        this.allowEmpty = false;
        this.defaultValue = FluidStack.empty();
        this.value = FluidStack.empty();
    }

    public FluidConfig showAmount(boolean show) {
        this.showAmount = show;
        return this;
    }

    public Component getStringForGUI(FluidStack v) {
        if (v != null && !v.isEmpty()) {
            return (Component) (this.showAmount ? Component.literal(v.getAmount() + "mB ").append(v.getName()) : v.getName());
        } else {
            return Component.translatable("gui.none");
        }
    }

    @Override
    public void onClicked(Widget clickedWidget, MouseButton button, ConfigCallback callback) {
        if (this.getCanEdit()) {
            new SelectFluidScreen(this, callback).openGui();
        }
    }

    @Override
    public boolean allowEmptyResource() {
        return this.allowEmpty;
    }

    @Override
    public OptionalLong fixedResourceSize() {
        return this.isFixedSize ? OptionalLong.of(this.fixedSize) : OptionalLong.empty();
    }

    @Override
    public boolean isEmpty() {
        return this.getValue().isEmpty();
    }

    @Override
    public SelectableResource<FluidStack> getResource() {
        return SelectableResource.fluid(this.getValue());
    }

    @Override
    public boolean setResource(SelectableResource<FluidStack> selectedStack) {
        return this.setCurrentValue(selectedStack.stack());
    }
}