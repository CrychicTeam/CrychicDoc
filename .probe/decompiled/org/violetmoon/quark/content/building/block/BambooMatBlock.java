package org.violetmoon.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class BambooMatBlock extends ZetaBlock {

    private static final EnumProperty<Direction> FACING = BlockStateProperties.FACING_HOPPER;

    public BambooMatBlock(String name, @Nullable ZetaModule module) {
        this(name, module, CreativeModeTabs.BUILDING_BLOCKS);
    }

    public BambooMatBlock(String name, @Nullable ZetaModule module, ResourceKey<CreativeModeTab> tab) {
        super(name, module, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).forceSolidOn().strength(0.5F).sound(SoundType.BAMBOO).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor((s, r, p) -> false));
        this.m_49959_((BlockState) this.m_49966_().m_61124_(FACING, Direction.NORTH));
        if (module != null) {
            this.setCreativeTab(tab);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction dir = ctx.m_8125_();
        if (ctx.m_43723_().m_146909_() > 70.0F) {
            dir = Direction.DOWN;
        }
        if (dir != Direction.DOWN) {
            Direction opposite = dir.getOpposite();
            BlockPos target = ctx.getClickedPos().relative(opposite);
            BlockState state = ctx.m_43725_().getBlockState(target);
            if (state.m_60734_() != this || state.m_61143_(FACING) != opposite) {
                target = ctx.getClickedPos().relative(dir);
                state = ctx.m_43725_().getBlockState(target);
                if (state.m_60734_() == this && state.m_61143_(FACING) == dir) {
                    dir = opposite;
                }
            }
        }
        return (BlockState) this.m_49966_().m_61124_(FACING, dir);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}