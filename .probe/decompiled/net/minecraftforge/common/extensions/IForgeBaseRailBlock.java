package net.minecraftforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import org.jetbrains.annotations.Nullable;

public interface IForgeBaseRailBlock {

    boolean isFlexibleRail(BlockState var1, BlockGetter var2, BlockPos var3);

    default boolean canMakeSlopes(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    RailShape getRailDirection(BlockState var1, BlockGetter var2, BlockPos var3, @Nullable AbstractMinecart var4);

    default float getRailMaxSpeed(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        if (cart instanceof MinecartFurnace) {
            return cart.m_20069_() ? 0.15F : 0.2F;
        } else {
            return cart.m_20069_() ? 0.2F : 0.4F;
        }
    }

    default void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
    }

    default boolean isValidRailShape(RailShape shape) {
        return true;
    }
}