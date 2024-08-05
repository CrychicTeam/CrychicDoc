package com.mna.api.blocks;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IRequirePlayerReference<T extends BlockEntity> {

    void setPlayerReference(Player var1);
}