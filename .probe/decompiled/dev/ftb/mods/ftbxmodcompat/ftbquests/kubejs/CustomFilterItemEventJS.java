package dev.ftb.mods.ftbxmodcompat.ftbquests.kubejs;

import dev.ftb.mods.ftbquests.api.event.CustomFilterDisplayItemsEvent;
import dev.latvian.mods.kubejs.event.EventJS;
import java.util.Collection;
import net.minecraft.world.item.ItemStack;

public class CustomFilterItemEventJS extends EventJS {

    private final CustomFilterDisplayItemsEvent event;

    public CustomFilterItemEventJS(CustomFilterDisplayItemsEvent event) {
        this.event = event;
    }

    public void addStack(ItemStack stack) {
        this.event.add(stack);
    }

    public void addStacks(Collection<ItemStack> stacks) {
        this.event.add(stacks);
    }
}