package dev.xkmc.modulargolems.content.menu.ghost;

import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;

public record ItemTarget(int x, int y, int w, int h, Consumer<ItemStack> con) {
}