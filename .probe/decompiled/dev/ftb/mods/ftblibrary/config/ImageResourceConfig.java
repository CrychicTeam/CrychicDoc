package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.config.ui.SelectImageResourceScreen;
import dev.ftb.mods.ftblibrary.config.ui.SelectableResource;
import dev.ftb.mods.ftblibrary.icon.IResourceIcon;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.OptionalLong;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ImageResourceConfig extends ResourceConfigValue<ResourceLocation> {

    public static final ResourceLocation NONE = new ResourceLocation("ftblibrary", "none");

    private boolean allowEmpty = true;

    public ImageResourceConfig() {
        this.value = NONE;
    }

    @Override
    public void onClicked(Widget clicked, MouseButton button, ConfigCallback callback) {
        new SelectImageResourceScreen(this, callback).withGridSize(8, 12).openGui();
    }

    public void setAllowEmpty(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
    }

    @Override
    public boolean allowEmptyResource() {
        return this.allowEmpty;
    }

    @Override
    public boolean canHaveNBT() {
        return false;
    }

    @Override
    public OptionalLong fixedResourceSize() {
        return OptionalLong.of(1L);
    }

    @Override
    public boolean isEmpty() {
        return this.value == null || this.value.equals(NONE);
    }

    @Override
    public SelectableResource<ResourceLocation> getResource() {
        return new SelectableResource.ImageResource(this.getValue());
    }

    @Override
    public boolean setResource(SelectableResource<ResourceLocation> selectedStack) {
        return this.setCurrentValue(selectedStack.stack());
    }

    @Override
    public void addInfo(TooltipList list) {
        if (this.value != null && !this.value.equals(this.defaultValue)) {
            list.add(Component.translatable("config.group.value").append(": ").withStyle(ChatFormatting.AQUA).append(Component.literal(this.getValue().toString()).withStyle(ChatFormatting.WHITE)));
        }
        super.addInfo(list);
    }

    public static ResourceLocation getResourceLocation(Icon icon) {
        return icon instanceof IResourceIcon i ? i.getResourceLocation() : NONE;
    }
}