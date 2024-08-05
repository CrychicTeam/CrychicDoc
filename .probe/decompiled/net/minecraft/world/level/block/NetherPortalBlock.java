package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NetherPortalBlock extends Block {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    protected static final int AABB_OFFSET = 2;

    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);

    protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    public NetherPortalBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AXIS, Direction.Axis.X));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        switch((Direction.Axis) blockState0.m_61143_(AXIS)) {
            case Z:
                return Z_AXIS_AABB;
            case X:
            default:
                return X_AXIS_AABB;
        }
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (serverLevel1.m_6042_().natural() && serverLevel1.m_46469_().getBoolean(GameRules.RULE_DOMOBSPAWNING) && randomSource3.nextInt(2000) < serverLevel1.m_46791_().getId()) {
            while (serverLevel1.m_8055_(blockPos2).m_60713_(this)) {
                blockPos2 = blockPos2.below();
            }
            if (serverLevel1.m_8055_(blockPos2).m_60643_(serverLevel1, blockPos2, EntityType.ZOMBIFIED_PIGLIN)) {
                Entity $$4 = EntityType.ZOMBIFIED_PIGLIN.spawn(serverLevel1, blockPos2.above(), MobSpawnType.STRUCTURE);
                if ($$4 != null) {
                    $$4.setPortalCooldown();
                }
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        Direction.Axis $$6 = direction1.getAxis();
        Direction.Axis $$7 = (Direction.Axis) blockState0.m_61143_(AXIS);
        boolean $$8 = $$7 != $$6 && $$6.isHorizontal();
        return !$$8 && !blockState2.m_60713_(this) && !new PortalShape(levelAccessor3, blockPos4, $$7).isComplete() ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (entity3.canChangeDimensions()) {
            entity3.handleInsidePortal(blockPos2);
        }
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if (randomSource3.nextInt(100) == 0) {
            level1.playLocalSound((double) blockPos2.m_123341_() + 0.5, (double) blockPos2.m_123342_() + 0.5, (double) blockPos2.m_123343_() + 0.5, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, randomSource3.nextFloat() * 0.4F + 0.8F, false);
        }
        for (int $$4 = 0; $$4 < 4; $$4++) {
            double $$5 = (double) blockPos2.m_123341_() + randomSource3.nextDouble();
            double $$6 = (double) blockPos2.m_123342_() + randomSource3.nextDouble();
            double $$7 = (double) blockPos2.m_123343_() + randomSource3.nextDouble();
            double $$8 = ((double) randomSource3.nextFloat() - 0.5) * 0.5;
            double $$9 = ((double) randomSource3.nextFloat() - 0.5) * 0.5;
            double $$10 = ((double) randomSource3.nextFloat() - 0.5) * 0.5;
            int $$11 = randomSource3.nextInt(2) * 2 - 1;
            if (!level1.getBlockState(blockPos2.west()).m_60713_(this) && !level1.getBlockState(blockPos2.east()).m_60713_(this)) {
                $$5 = (double) blockPos2.m_123341_() + 0.5 + 0.25 * (double) $$11;
                $$8 = (double) (randomSource3.nextFloat() * 2.0F * (float) $$11);
            } else {
                $$7 = (double) blockPos2.m_123343_() + 0.5 + 0.25 * (double) $$11;
                $$10 = (double) (randomSource3.nextFloat() * 2.0F * (float) $$11);
            }
            level1.addParticle(ParticleTypes.PORTAL, $$5, $$6, $$7, $$8, $$9, $$10);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return ItemStack.EMPTY;
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        switch(rotation1) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch((Direction.Axis) blockState0.m_61143_(AXIS)) {
                    case Z:
                        return (BlockState) blockState0.m_61124_(AXIS, Direction.Axis.X);
                    case X:
                        return (BlockState) blockState0.m_61124_(AXIS, Direction.Axis.Z);
                    default:
                        return blockState0;
                }
            default:
                return blockState0;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AXIS);
    }
}