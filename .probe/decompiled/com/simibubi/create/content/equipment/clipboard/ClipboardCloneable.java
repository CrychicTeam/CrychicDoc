package com.simibubi.create.content.equipment.clipboard;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public interface ClipboardCloneable {

    String getClipboardKey();

    boolean writeToClipboard(CompoundTag var1, Direction var2);

    boolean readFromClipboard(CompoundTag var1, Player var2, Direction var3, boolean var4);
}