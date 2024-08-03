package dev.ftb.mods.ftblibrary.config.ui;

import dev.architectury.fluid.FluidStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface SelectableResource<T> {

    T stack();

    long getCount();

    default boolean isEmpty() {
        return this.getCount() == 0L;
    }

    Component getName();

    Icon getIcon();

    SelectableResource<T> copyWithCount(long var1);

    static SelectableResource<ItemStack> item(ItemStack stack) {
        return new SelectableResource.ItemStackResource(stack);
    }

    static SelectableResource<FluidStack> fluid(FluidStack stack) {
        return new SelectableResource.FluidStackResource(stack);
    }

    CompoundTag getTag();

    void setTag(CompoundTag var1);

    void setCount(int var1);

    public static record FluidStackResource(FluidStack stack) implements SelectableResource<FluidStack> {

        @Override
        public long getCount() {
            return this.stack().getAmount();
        }

        @Override
        public Component getName() {
            return this.stack.getName();
        }

        @Override
        public Icon getIcon() {
            return Icon.getIcon(ClientUtils.getStillTexture(this.stack)).withTint(Color4I.rgb(ClientUtils.getFluidColor(this.stack)));
        }

        @Override
        public SelectableResource<FluidStack> copyWithCount(long count) {
            return SelectableResource.fluid(this.stack.copyWithAmount(count));
        }

        @Override
        public CompoundTag getTag() {
            return this.stack.getTag();
        }

        @Override
        public void setTag(CompoundTag tag) {
            this.stack.setTag(tag);
        }

        @Override
        public void setCount(int count) {
            this.stack.setAmount((long) count);
        }
    }

    public static class ImageResource implements SelectableResource<ResourceLocation> {

        private final ResourceLocation location;

        private final Component name;

        private final Icon icon;

        public ImageResource(ResourceLocation location) {
            this.location = location;
            this.name = location == null ? Component.translatable("gui.none").withStyle(ChatFormatting.GRAY) : Component.literal(location.getNamespace()).withStyle(ChatFormatting.GOLD).append(":").append(Component.literal(location.getPath()).withStyle(ChatFormatting.YELLOW));
            this.icon = Icon.getIcon(location);
        }

        public ResourceLocation stack() {
            return this.location;
        }

        @Override
        public long getCount() {
            return 1L;
        }

        @Override
        public Component getName() {
            return this.name;
        }

        @Override
        public Icon getIcon() {
            return this.icon;
        }

        @Override
        public SelectableResource<ResourceLocation> copyWithCount(long count) {
            return this;
        }

        @Override
        public CompoundTag getTag() {
            return null;
        }

        @Override
        public void setTag(CompoundTag tag) {
        }

        @Override
        public void setCount(int count) {
        }
    }

    public static record ItemStackResource(ItemStack stack) implements SelectableResource<ItemStack> {

        @Override
        public long getCount() {
            return (long) this.stack.getCount();
        }

        @Override
        public Component getName() {
            return this.stack.getHoverName();
        }

        @Override
        public Icon getIcon() {
            return ItemIcon.getItemIcon(this.stack);
        }

        @Override
        public SelectableResource<ItemStack> copyWithCount(long count) {
            return SelectableResource.item(this.stack.copyWithCount((int) count));
        }

        @Override
        public CompoundTag getTag() {
            return this.stack.getTag();
        }

        @Override
        public void setTag(CompoundTag tag) {
            this.stack.setTag(tag);
        }

        @Override
        public void setCount(int count) {
            this.stack.setCount(count);
        }
    }
}