package com.almostreliable.morejs.features.structure;

import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface StructureBlockInfoModification {

    String getId();

    Block getBlock();

    void setBlock(ResourceLocation var1);

    void setBlock(ResourceLocation var1, Map<String, Object> var2);

    Map<String, Object> getProperties();

    boolean hasNbt();

    @Nullable
    CompoundTag getNbt();

    void setNbt(@Nullable CompoundTag var1);

    BlockPos getPosition();

    void setVanillaBlockState(BlockState var1);
}