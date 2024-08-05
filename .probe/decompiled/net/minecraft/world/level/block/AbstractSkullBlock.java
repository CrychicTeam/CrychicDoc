package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public abstract class AbstractSkullBlock extends BaseEntityBlock implements Equipable {

    private final SkullBlock.Type type;

    public AbstractSkullBlock(SkullBlock.Type skullBlockType0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.type = skullBlockType0;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new SkullBlockEntity(blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        if (level0.isClientSide) {
            boolean $$3 = blockState1.m_60713_(Blocks.DRAGON_HEAD) || blockState1.m_60713_(Blocks.DRAGON_WALL_HEAD) || blockState1.m_60713_(Blocks.PIGLIN_HEAD) || blockState1.m_60713_(Blocks.PIGLIN_WALL_HEAD);
            if ($$3) {
                return m_152132_(blockEntityTypeT2, BlockEntityType.SKULL, SkullBlockEntity::m_261318_);
            }
        }
        return null;
    }

    public SkullBlock.Type getType() {
        return this.type;
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}