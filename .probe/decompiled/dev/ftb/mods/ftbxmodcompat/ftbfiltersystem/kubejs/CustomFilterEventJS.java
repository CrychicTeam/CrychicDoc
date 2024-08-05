package dev.ftb.mods.ftbxmodcompat.ftbfiltersystem.kubejs;

import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.world.item.ItemStack;

public class CustomFilterEventJS extends EventJS {

    private final ItemStack stack;

    private final String data;

    public CustomFilterEventJS(ItemStack stack, String data) {
        this.stack = stack;
        this.data = data;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public String getData() {
        return this.data;
    }
}