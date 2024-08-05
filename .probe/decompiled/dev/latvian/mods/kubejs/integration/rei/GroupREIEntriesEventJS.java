package dev.latvian.mods.kubejs.integration.rei;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.util.Tags;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.List;
import java.util.function.Predicate;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class GroupREIEntriesEventJS extends EventJS {

    private final REIEntryWrappers entryWrappers;

    public final CollapsibleEntryRegistry registry;

    public GroupREIEntriesEventJS(REIEntryWrappers entryWrappers, CollapsibleEntryRegistry registry) {
        this.entryWrappers = entryWrappers;
        this.registry = registry;
    }

    public void groupItems(ResourceLocation groupId, Component description, Ingredient entries) {
        this.group(groupId, description, EntryIngredients.ofIngredient(entries));
    }

    public void groupFluids(ResourceLocation groupId, Component description, FluidStackJS... entries) {
        this.group(groupId, description, EntryIngredients.of(VanillaEntryTypes.FLUID, CollectionUtils.map(entries, FluidStackJS::getFluidStack)));
    }

    public void groupEntries(ResourceLocation groupId, Component description, ResourceLocation entryTypeId, Object entries) {
        EntryType<?> entryType = KubeJSREIPlugin.getTypeOrThrow(entryTypeId);
        EntryWrapper<?, ?> wrapper = this.entryWrappers.getWrapper(entryType);
        List<? extends EntryStack<?>> list = wrapper.entryList(entries);
        this.group(groupId, description, UtilsJS.cast(list));
    }

    public void groupSameItem(ResourceLocation group, Component description, ItemStack item) {
        this.groupItemsIf(group, description, item.getItem().kjs$asIngredient());
    }

    public void groupSameFluid(ResourceLocation group, Component description, FluidStackJS fluid) {
        this.groupFluidsIf(group, description, stack -> stack.getFluid().equals(fluid.getFluid()));
    }

    public void groupItemsByTag(ResourceLocation groupId, Component description, ResourceLocation tags) {
        this.group(groupId, description, EntryIngredients.ofItemTag(Tags.item(tags)));
    }

    public void groupFluidsByTag(ResourceLocation groupId, Component description, ResourceLocation tags) {
        this.group(groupId, description, EntryIngredients.ofFluidTag(Tags.fluid(tags)));
    }

    public void groupItemsIf(ResourceLocation groupId, Component description, Predicate<ItemStack> predicate) {
        this.registry.group(groupId, description, VanillaEntryTypes.ITEM, item -> predicate.test((ItemStack) item.getValue()));
    }

    public void groupFluidsIf(ResourceLocation groupId, Component description, Predicate<FluidStackJS> predicate) {
        this.registry.group(groupId, description, VanillaEntryTypes.FLUID, fluid -> predicate.test(FluidStackJS.of(fluid.getValue())));
    }

    public void groupEntriesIf(ResourceLocation groupId, Component description, ResourceLocation entryTypeId, Predicate predicate) {
        EntryType<?> entryType = KubeJSREIPlugin.getTypeOrThrow(entryTypeId);
        this.registry.group(groupId, description, entryType, entry -> predicate.test(entry.getValue()));
    }

    public void groupAnyIf(ResourceLocation groupId, Component description, Predicate<EntryStack<?>> predicate) {
        this.registry.group(groupId, description, predicate);
    }

    private void group(ResourceLocation groupId, Component description, List<EntryStack<?>> entries) {
        this.registry.group(groupId, description, entries);
    }
}