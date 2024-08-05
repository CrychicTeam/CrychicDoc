package org.violetmoon.quark.api.event;

import java.util.Set;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class GatherToolClassesEvent extends Event {

    public final ItemStack stack;

    public final Set<String> classes;

    public GatherToolClassesEvent(ItemStack stack, Set<String> classes) {
        this.stack = stack;
        this.classes = classes;
    }
}