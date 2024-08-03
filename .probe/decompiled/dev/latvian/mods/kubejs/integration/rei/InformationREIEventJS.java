package dev.latvian.mods.kubejs.integration.rei;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.Arrays;
import java.util.List;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.plugin.client.BuiltinClientPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class InformationREIEventJS extends EventJS {

    private final REIEntryWrappers entryWrappers;

    public InformationREIEventJS(REIEntryWrappers entryWrappers) {
        this.entryWrappers = entryWrappers;
    }

    public void addItem(Ingredient stacks, Component title, Component[] description) {
        this.add(VanillaEntryTypes.ITEM, stacks, title, description);
    }

    public void addFluid(FluidStackJS stacks, Component title, Component[] description) {
        this.add(VanillaEntryTypes.FLUID, stacks, title, description);
    }

    public void add(ResourceLocation typeId, Object stacks, Component title, Component[] description) {
        this.add(KubeJSREIPlugin.getTypeOrThrow(typeId), stacks, title, description);
    }

    @HideFromJS
    public <T> void add(EntryType<T> type, Object stacks, Component title, Component[] description) {
        EntryWrapper<T, ?> w = this.entryWrappers.getWrapper(type);
        List<EntryStack<T>> list = w.entryList(stacks);
        BuiltinClientPlugin.getInstance().registerInformation(EntryIngredient.of(list), title, components -> {
            components.addAll(Arrays.asList(description));
            return components;
        });
    }
}