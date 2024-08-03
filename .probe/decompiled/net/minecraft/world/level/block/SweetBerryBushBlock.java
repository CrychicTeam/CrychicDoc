package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SweetBerryBushBlock extends BushBlock implements BonemealableBlock {

    private static final float HURT_SPEED_THRESHOLD = 0.003F;

    public static final int MAX_AGE = 3;

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    private static final VoxelShape SAPLING_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);

    private static final VoxelShape MID_GROWTH_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public SweetBerryBushBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AGE, 0));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return new ItemStack(Items.SWEET_BERRIES);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        if ((Integer) blockState0.m_61143_(AGE) == 0) {
            return SAPLING_SHAPE;
        } else {
            return blockState0.m_61143_(AGE) < 3 ? MID_GROWTH_SHAPE : super.m_5940_(blockState0, blockGetter1, blockPos2, collisionContext3);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(AGE) < 3;
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        int $$4 = (Integer) blockState0.m_61143_(AGE);
        if ($$4 < 3 && randomSource3.nextInt(5) == 0 && serverLevel1.m_45524_(blockPos2.above(), 0) >= 9) {
            BlockState $$5 = (BlockState) blockState0.m_61124_(AGE, $$4 + 1);
            serverLevel1.m_7731_(blockPos2, $$5, 2);
            serverLevel1.m_220407_(GameEvent.BLOCK_CHANGE, blockPos2, GameEvent.Context.of($$5));
        }
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (entity3 instanceof LivingEntity && entity3.getType() != EntityType.FOX && entity3.getType() != EntityType.BEE) {
            entity3.makeStuckInBlock(blockState0, new Vec3(0.8F, 0.75, 0.8F));
            if (!level1.isClientSide && (Integer) blockState0.m_61143_(AGE) > 0 && (entity3.xOld != entity3.getX() || entity3.zOld != entity3.getZ())) {
                double $$4 = Math.abs(entity3.getX() - entity3.xOld);
                double $$5 = Math.abs(entity3.getZ() - entity3.zOld);
                if ($$4 >= 0.003F || $$5 >= 0.003F) {
                    entity3.hurt(level1.damageSources().sweetBerryBush(), 1.0F);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        int $$6 = (Integer) blockState0.m_61143_(AGE);
        boolean $$7 = $$6 == 3;
        if (!$$7 && player3.m_21120_(interactionHand4).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if ($$6 > 1) {
            int $$8 = 1 + level1.random.nextInt(2);
            m_49840_(level1, blockPos2, new ItemStack(Items.SWEET_BERRIES, $$8 + ($$7 ? 1 : 0)));
            level1.playSound(null, blockPos2, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level1.random.nextFloat() * 0.4F);
            BlockState $$9 = (BlockState) blockState0.m_61124_(AGE, 1);
            level1.setBlock(blockPos2, $$9, 2);
            level1.m_220407_(GameEvent.BLOCK_CHANGE, blockPos2, GameEvent.Context.of(player3, $$9));
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            return super.m_6227_(blockState0, level1, blockPos2, player3, interactionHand4, blockHitResult5);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return (Integer) blockState2.m_61143_(AGE) < 3;
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        int $$4 = Math.min(3, (Integer) blockState3.m_61143_(AGE) + 1);
        serverLevel0.m_7731_(blockPos2, (BlockState) blockState3.m_61124_(AGE, $$4), 2);
    }
}