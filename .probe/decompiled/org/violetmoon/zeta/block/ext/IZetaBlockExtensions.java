package org.violetmoon.zeta.block.ext;

import com.google.common.collect.BiMap;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public interface IZetaBlockExtensions {

    IZetaBlockExtensions DEFAULT = new IZetaBlockExtensions() {
    };

    default int getLightEmissionZeta(BlockState state, BlockGetter level, BlockPos pos) {
        return state.m_60791_();
    }

    default boolean isLadderZeta(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return state.m_204336_(BlockTags.CLIMBABLE);
    }

    default boolean makesOpenTrapdoorAboveClimbableZeta(BlockState state, LevelReader level, BlockPos pos, BlockState trapdoorState) {
        return state.m_60734_() instanceof LadderBlock && state.m_61143_(LadderBlock.FACING) == trapdoorState.m_61143_(TrapDoorBlock.f_54117_);
    }

    default boolean canSustainPlantZeta(BlockState state, BlockGetter level, BlockPos pos, Direction facing, String plantabletype) {
        return false;
    }

    default boolean isConduitFrameZeta(BlockState state, LevelReader level, BlockPos pos, BlockPos conduit) {
        return state.m_60734_() == Blocks.PRISMARINE || state.m_60734_() == Blocks.PRISMARINE_BRICKS || state.m_60734_() == Blocks.SEA_LANTERN || state.m_60734_() == Blocks.DARK_PRISMARINE;
    }

    default float getEnchantPowerBonusZeta(BlockState state, LevelReader level, BlockPos pos) {
        return state.m_60713_(Blocks.BOOKSHELF) ? 1.0F : 0.0F;
    }

    default SoundType getSoundTypeZeta(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return state.m_60827_();
    }

    default float[] getBeaconColorMultiplierZeta(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
        return state.m_60734_() instanceof BeaconBeamBlock bbeam ? bbeam.getColor().getTextureDiffuseColors() : null;
    }

    default boolean isStickyBlockZeta(BlockState state) {
        return state.m_60734_() == Blocks.SLIME_BLOCK || state.m_60734_() == Blocks.HONEY_BLOCK;
    }

    default boolean canStickToZeta(BlockState state, BlockState other) {
        if (state.m_60734_() == Blocks.HONEY_BLOCK && other.m_60734_() == Blocks.SLIME_BLOCK) {
            return false;
        } else {
            return state.m_60734_() == Blocks.SLIME_BLOCK && other.m_60734_() == Blocks.HONEY_BLOCK ? false : state.isStickyBlock() || other.isStickyBlock();
        }
    }

    default int getFlammabilityZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return ((FireBlock) Blocks.FIRE).getBurnOdds(state);
    }

    default boolean isFlammableZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return this.getFlammabilityZeta(state, world, pos, face) > 0;
    }

    default int getFireSpreadSpeedZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return ((FireBlock) Blocks.FIRE).getIgniteOdds(state);
    }

    default boolean collisionExtendsVerticallyZeta(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return state.m_204336_(BlockTags.FENCES) || state.m_204336_(BlockTags.WALLS) || state.m_60734_() instanceof FenceGateBlock;
    }

    default boolean shouldDisplayFluidOverlayZeta(BlockState state, BlockAndTintGetter level, BlockPos pos, FluidState fluidState) {
        return state.m_60734_() instanceof HalfTransparentBlock || state.m_60734_() instanceof LeavesBlock;
    }

    @Nullable
    default BlockState getToolModifiedStateZeta(BlockState state, UseOnContext context, String toolActionType, boolean simulate) {
        return switch(toolActionType) {
            case "axe_strip" ->
                AxeItem.getAxeStrippingState(state);
            case "axe_scrape" ->
                (BlockState) WeatheringCopper.getPrevious(state).orElse(null);
            case "axe_wax_off" ->
                (BlockState) Optional.ofNullable((Block) ((BiMap) HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(state.m_60734_())).map(blockx -> blockx.withPropertiesOf(state)).orElse(null);
            case "shovel_flatten" ->
                ShovelItem.getShovelPathingState(state);
            case "hoe_till" ->
                {
                    Block block = state.m_60734_();
                    if (block == Blocks.ROOTED_DIRT) {
                        if (!simulate && !context.getLevel().isClientSide) {
                            Block.popResourceFromFace(context.getLevel(), context.getClickedPos(), context.getClickedFace(), new ItemStack(Items.HANGING_ROOTS));
                        }
                        ???;
                    } else {
                        ???;
                    }
                }
            default ->
                null;
        };
    }

    default boolean isScaffoldingZeta(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return state.m_60713_(Blocks.SCAFFOLDING);
    }
}