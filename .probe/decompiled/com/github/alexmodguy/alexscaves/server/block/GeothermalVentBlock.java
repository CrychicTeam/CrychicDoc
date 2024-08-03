package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.blockentity.ACBlockEntityRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.GeothermalVentBlockEntity;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class GeothermalVentBlock extends BaseEntityBlock {

    public static final IntegerProperty SMOKE_TYPE = IntegerProperty.create("smoke_type", 0, 3);

    public static final BooleanProperty SPAWNING_PARTICLES = BooleanProperty.create("spawning_particles");

    public GeothermalVentBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F, 5.0F).sound(SoundType.TUFF));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(SMOKE_TYPE, 0)).m_61124_(SPAWNING_PARTICLES, true));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(SMOKE_TYPE, this.getSmokeType(levelaccessor, blockpos))).m_61124_(SPAWNING_PARTICLES, this.isSpawningParticles(blockpos, levelaccessor));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        return (BlockState) ((BlockState) state.m_61124_(SMOKE_TYPE, this.getSmokeType(levelAccessor, blockPos))).m_61124_(SPAWNING_PARTICLES, this.isSpawningParticles(blockPos, levelAccessor));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SMOKE_TYPE, SPAWNING_PARTICLES);
    }

    public int getSmokeType(LevelAccessor level, BlockPos blockpos) {
        BlockState state = level.m_8055_(blockpos.below());
        if (state.m_60734_() instanceof GeothermalVentBlock) {
            return (Integer) state.m_61143_(SMOKE_TYPE);
        } else if (state.m_60819_().getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get()) {
            return 3;
        } else if (state.m_60819_().is(FluidTags.WATER)) {
            return 1;
        } else {
            return state.m_60819_().is(FluidTags.LAVA) ? 2 : 0;
        }
    }

    public boolean isSpawningParticles(BlockPos pos, LevelAccessor level) {
        BlockState above = level.m_8055_(pos.above());
        return above.m_60795_() || !above.m_280555_();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack heldItem = player.m_21120_(hand);
        if (heldItem.is(Items.GLASS_BOTTLE) && (Integer) blockState.m_61143_(SMOKE_TYPE) == 3 && (Boolean) blockState.m_61143_(SPAWNING_PARTICLES)) {
            ItemStack bottle = new ItemStack(ACItemRegistry.RADON_BOTTLE.get());
            if (!player.addItem(bottle)) {
                player.drop(bottle, false);
            }
            if (!player.isCreative()) {
                heldItem.shrink(1);
            }
            player.m_216990_(SoundEvents.BOTTLE_FILL);
            return InteractionResult.SUCCESS;
        } else {
            return super.m_6227_(blockState, level, blockPos, player, hand, result);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        if (!level.isClientSide) {
            return null;
        } else {
            return state.m_61143_(SMOKE_TYPE) > 0 && state.m_61143_(SPAWNING_PARTICLES) ? m_152132_(entityType, ACBlockEntityRegistry.GEOTHERMAL_VENT.get(), GeothermalVentBlockEntity::particleTick) : null;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeothermalVentBlockEntity(pos, state);
    }
}