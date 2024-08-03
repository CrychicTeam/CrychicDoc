package dev.xkmc.l2library.base.menu.stacked;

import net.minecraft.network.chat.Component;

public record TextEntry(Component text, int x, int y, int color, boolean shadow) {
}