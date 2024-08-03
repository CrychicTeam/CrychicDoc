package net.minecraftforge.event;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class BuildCreativeModeTabContentsEvent extends Event implements IModBusEvent, CreativeModeTab.Output {

    private final CreativeModeTab tab;

    private final CreativeModeTab.ItemDisplayParameters parameters;

    private final MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries;

    private final ResourceKey<CreativeModeTab> tabKey;

    @Internal
    public BuildCreativeModeTabContentsEvent(CreativeModeTab tab, ResourceKey<CreativeModeTab> tabKey, CreativeModeTab.ItemDisplayParameters parameters, MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries) {
        this.tab = tab;
        this.tabKey = tabKey;
        this.parameters = parameters;
        this.entries = entries;
    }

    public CreativeModeTab getTab() {
        return this.tab;
    }

    public ResourceKey<CreativeModeTab> getTabKey() {
        return this.tabKey;
    }

    public FeatureFlagSet getFlags() {
        return this.parameters.enabledFeatures();
    }

    public CreativeModeTab.ItemDisplayParameters getParameters() {
        return this.parameters;
    }

    public boolean hasPermissions() {
        return this.parameters.hasPermissions();
    }

    public MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> getEntries() {
        return this.entries;
    }

    @Override
    public void accept(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
        this.getEntries().put(stack, visibility);
    }

    public void accept(Supplier<? extends ItemLike> item, CreativeModeTab.TabVisibility visibility) {
        this.m_245282_((ItemLike) item.get(), visibility);
    }

    public void accept(Supplier<? extends ItemLike> item) {
        this.m_246326_((ItemLike) item.get());
    }
}