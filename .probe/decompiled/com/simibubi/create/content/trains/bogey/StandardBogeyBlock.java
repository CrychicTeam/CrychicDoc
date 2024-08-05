package com.simibubi.create.content.trains.bogey;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.trains.track.TrackMaterial;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StandardBogeyBlock extends AbstractBogeyBlock<StandardBogeyBlockEntity> implements IBE<StandardBogeyBlockEntity>, ProperWaterloggedBlock, ISpecialBlockItemRequirement {

    public StandardBogeyBlock(BlockBehaviour.Properties props, BogeySizes.BogeySize size) {
        super(props, size);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false));
    }

    @Override
    public TrackMaterial.TrackType getTrackType(BogeyStyle style) {
        return TrackMaterial.TrackType.STANDARD;
    }

    @Override
    public double getWheelPointSpacing() {
        return 2.0;
    }

    @Override
    public double getWheelRadius() {
        return (this.size == BogeySizes.LARGE ? 12.5 : 6.5) / 16.0;
    }

    @Override
    public Vec3 getConnectorAnchorOffset() {
        return new Vec3(0.0, 0.21875, 1.0);
    }

    @Override
    public BogeyStyle getDefaultStyle() {
        return AllBogeyStyles.STANDARD;
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return AllBlocks.RAILWAY_CASING.asStack();
    }

    @Override
    public Class<StandardBogeyBlockEntity> getBlockEntityClass() {
        return StandardBogeyBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StandardBogeyBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends StandardBogeyBlockEntity>) AllBlockEntityTypes.BOGEY.get();
    }
}