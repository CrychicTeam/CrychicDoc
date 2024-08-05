package com.simibubi.create.content.kinetics.gearbox;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;

public class GearboxBlock extends RotatedPillarKineticBlock implements IBE<GearboxBlockEntity> {

    public GearboxBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.PUSH_ONLY;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return ((Direction.Axis) state.m_61143_(AXIS)).isVertical() ? super.m_49635_(state, builder) : Arrays.asList(new ItemStack((ItemLike) AllItems.VERTICAL_GEARBOX.get()));
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return ((Direction.Axis) state.m_61143_(AXIS)).isVertical() ? super.getCloneItemStack(state, target, world, pos, player) : new ItemStack((ItemLike) AllItems.VERTICAL_GEARBOX.get());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(AXIS, Direction.Axis.Y);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() != state.m_61143_(AXIS);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return (Direction.Axis) state.m_61143_(AXIS);
    }

    @Override
    public Class<GearboxBlockEntity> getBlockEntityClass() {
        return GearboxBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GearboxBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends GearboxBlockEntity>) AllBlockEntityTypes.GEARBOX.get();
    }
}