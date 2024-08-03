package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BlockMyrmexResin extends Block {

    private final boolean sticky;

    public BlockMyrmexResin(boolean sticky) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).strength(2.5F).sound(sticky ? SoundType.SLIME_BLOCK : SoundType.GRAVEL));
        this.sticky = sticky;
    }

    static String name(boolean sticky, String suffix) {
        return sticky ? "myrmex_resin_sticky_%s".formatted(suffix) : "myrmex_resin_%s".formatted(suffix);
    }

    @Deprecated
    public boolean canEntitySpawn(BlockState state, BlockGetter worldIn, BlockPos pos, EntityType<?> type) {
        return false;
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (this.sticky && !(entity instanceof EntityMyrmexBase)) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.4, 0.4, 0.4));
        }
    }
}