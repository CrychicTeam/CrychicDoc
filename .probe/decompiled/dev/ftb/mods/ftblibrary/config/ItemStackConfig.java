package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.config.ui.SelectItemStackScreen;
import dev.ftb.mods.ftblibrary.config.ui.SelectableResource;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import java.util.OptionalLong;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

public class ItemStackConfig extends ResourceConfigValue<ItemStack> {

    private final boolean allowEmpty;

    private final boolean isFixedSize;

    private final long fixedSize;

    public ItemStackConfig(boolean single, boolean empty) {
        this.isFixedSize = single && !empty;
        this.fixedSize = 0L;
        this.allowEmpty = empty;
        this.defaultValue = ItemStack.EMPTY;
        this.value = ItemStack.EMPTY;
    }

    public ItemStackConfig(long fixedSize) {
        Validate.isTrue(fixedSize >= 1L);
        this.isFixedSize = true;
        this.fixedSize = fixedSize;
        this.allowEmpty = false;
        this.defaultValue = ItemStack.EMPTY;
        this.value = ItemStack.EMPTY;
    }

    public ItemStack copy(ItemStack value) {
        return value.isEmpty() ? ItemStack.EMPTY : value.copy();
    }

    public Component getStringForGUI(@Nullable ItemStack v) {
        if (v != null && !v.isEmpty()) {
            return (Component) (v.getCount() <= 1 ? v.getHoverName() : Component.literal(v.getCount() + "x ").append(v.getHoverName()));
        } else {
            return Component.translatable("gui.none");
        }
    }

    @Override
    public void onClicked(Widget clickedWidget, MouseButton button, ConfigCallback callback) {
        if (this.getCanEdit()) {
            new SelectItemStackScreen(this, callback).openGui();
        }
    }

    public ItemStack getValue() {
        ItemStack val = (ItemStack) super.getValue();
        return val.isEmpty() ? ItemStack.EMPTY : val;
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
    public SelectableResource<ItemStack> getResource() {
        return SelectableResource.item(this.getValue());
    }

    @Override
    public boolean setResource(SelectableResource<ItemStack> selectedStack) {
        return this.setCurrentValue(selectedStack.stack());
    }
}