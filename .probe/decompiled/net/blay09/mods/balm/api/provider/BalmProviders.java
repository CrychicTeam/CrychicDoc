package net.blay09.mods.balm.api.provider;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface BalmProviders {

    <T> T getProvider(BlockEntity var1, Class<T> var2);

    <T> T getProvider(BlockEntity var1, Direction var2, Class<T> var3);

    <T> T getProvider(Entity var1, Class<T> var2);
}