package com.simibubi.create.content.contraptions.actors.psi;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PortableStorageInterfaceBlock extends WrenchableDirectionalBlock implements IBE<PortableStorageInterfaceBlockEntity> {

    boolean fluids;

    public static PortableStorageInterfaceBlock forItems(BlockBehaviour.Properties p_i48415_1_) {
        return new PortableStorageInterfaceBlock(p_i48415_1_, false);
    }

    public static PortableStorageInterfaceBlock forFluids(BlockBehaviour.Properties p_i48415_1_) {
        return new PortableStorageInterfaceBlock(p_i48415_1_, true);
    }

    private PortableStorageInterfaceBlock(BlockBehaviour.Properties p_i48415_1_, boolean fluids) {
        super(p_i48415_1_);
        this.fluids = fluids;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
        this.withBlockEntityDo(world, pos, PortableStorageInterfaceBlockEntity::neighbourChanged);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.m_6402_(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getNearestLookingDirection();
        if (context.m_43723_() != null && context.m_43723_().m_6144_()) {
            direction = direction.getOpposite();
        }
        return (BlockState) this.m_49966_().m_61124_(f_52588_, direction.getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.PORTABLE_STORAGE_INTERFACE.get((Direction) state.m_61143_(f_52588_));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return (Integer) this.getBlockEntityOptional(worldIn, pos).map(be -> be.isConnected() ? 15 : 0).orElse(0);
    }

    @Override
    public Class<PortableStorageInterfaceBlockEntity> getBlockEntityClass() {
        return PortableStorageInterfaceBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PortableStorageInterfaceBlockEntity> getBlockEntityType() {
        return this.fluids ? (BlockEntityType) AllBlockEntityTypes.PORTABLE_FLUID_INTERFACE.get() : (BlockEntityType) AllBlockEntityTypes.PORTABLE_STORAGE_INTERFACE.get();
    }
}