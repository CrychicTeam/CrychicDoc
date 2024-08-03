package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TurtleEggBlock extends Block {

    public static final int MAX_HATCH_LEVEL = 2;

    public static final int MIN_EGGS = 1;

    public static final int MAX_EGGS = 4;

    private static final VoxelShape ONE_EGG_AABB = Block.box(3.0, 0.0, 3.0, 12.0, 7.0, 12.0);

    private static final VoxelShape MULTIPLE_EGGS_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0);

    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;

    public static final IntegerProperty EGGS = BlockStateProperties.EGGS;

    public TurtleEggBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HATCH, 0)).m_61124_(EGGS, 1));
    }

    @Override
    public void stepOn(Level level0, BlockPos blockPos1, BlockState blockState2, Entity entity3) {
        if (!entity3.isSteppingCarefully()) {
            this.destroyEgg(level0, blockState2, blockPos1, entity3, 100);
        }
        super.stepOn(level0, blockPos1, blockState2, entity3);
    }

    @Override
    public void fallOn(Level level0, BlockState blockState1, BlockPos blockPos2, Entity entity3, float float4) {
        if (!(entity3 instanceof Zombie)) {
            this.destroyEgg(level0, blockState1, blockPos2, entity3, 3);
        }
        super.fallOn(level0, blockState1, blockPos2, entity3, float4);
    }

    private void destroyEgg(Level level0, BlockState blockState1, BlockPos blockPos2, Entity entity3, int int4) {
        if (this.canDestroyEgg(level0, entity3)) {
            if (!level0.isClientSide && level0.random.nextInt(int4) == 0 && blockState1.m_60713_(Blocks.TURTLE_EGG)) {
                this.decreaseEggs(level0, blockPos2, blockState1);
            }
        }
    }

    private void decreaseEggs(Level level0, BlockPos blockPos1, BlockState blockState2) {
        level0.playSound(null, blockPos1, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level0.random.nextFloat() * 0.2F);
        int $$3 = (Integer) blockState2.m_61143_(EGGS);
        if ($$3 <= 1) {
            level0.m_46961_(blockPos1, false);
        } else {
            level0.setBlock(blockPos1, (BlockState) blockState2.m_61124_(EGGS, $$3 - 1), 2);
            level0.m_220407_(GameEvent.BLOCK_DESTROY, blockPos1, GameEvent.Context.of(blockState2));
            level0.m_46796_(2001, blockPos1, Block.getId(blockState2));
        }
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (this.shouldUpdateHatchLevel(serverLevel1) && onSand(serverLevel1, blockPos2)) {
            int $$4 = (Integer) blockState0.m_61143_(HATCH);
            if ($$4 < 2) {
                serverLevel1.m_5594_(null, blockPos2, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + randomSource3.nextFloat() * 0.2F);
                serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(HATCH, $$4 + 1), 2);
            } else {
                serverLevel1.m_5594_(null, blockPos2, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + randomSource3.nextFloat() * 0.2F);
                serverLevel1.m_7471_(blockPos2, false);
                for (int $$5 = 0; $$5 < blockState0.m_61143_(EGGS); $$5++) {
                    serverLevel1.m_46796_(2001, blockPos2, Block.getId(blockState0));
                    Turtle $$6 = EntityType.TURTLE.create(serverLevel1);
                    if ($$6 != null) {
                        $$6.m_146762_(-24000);
                        $$6.setHomePos(blockPos2);
                        $$6.m_7678_((double) blockPos2.m_123341_() + 0.3 + (double) $$5 * 0.2, (double) blockPos2.m_123342_(), (double) blockPos2.m_123343_() + 0.3, 0.0F, 0.0F);
                        serverLevel1.addFreshEntity($$6);
                    }
                }
            }
        }
    }

    public static boolean onSand(BlockGetter blockGetter0, BlockPos blockPos1) {
        return isSand(blockGetter0, blockPos1.below());
    }

    public static boolean isSand(BlockGetter blockGetter0, BlockPos blockPos1) {
        return blockGetter0.getBlockState(blockPos1).m_204336_(BlockTags.SAND);
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (onSand(level1, blockPos2) && !level1.isClientSide) {
            level1.m_46796_(2005, blockPos2, 0);
        }
    }

    private boolean shouldUpdateHatchLevel(Level level0) {
        float $$1 = level0.m_46942_(1.0F);
        return (double) $$1 < 0.69 && (double) $$1 > 0.65 ? true : level0.random.nextInt(500) == 0;
    }

    @Override
    public void playerDestroy(Level level0, Player player1, BlockPos blockPos2, BlockState blockState3, @Nullable BlockEntity blockEntity4, ItemStack itemStack5) {
        super.playerDestroy(level0, player1, blockPos2, blockState3, blockEntity4, itemStack5);
        this.decreaseEggs(level0, blockPos2, blockState3);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState0, BlockPlaceContext blockPlaceContext1) {
        return !blockPlaceContext1.m_7078_() && blockPlaceContext1.m_43722_().is(this.m_5456_()) && blockState0.m_61143_(EGGS) < 4 ? true : super.m_6864_(blockState0, blockPlaceContext1);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = blockPlaceContext0.m_43725_().getBlockState(blockPlaceContext0.getClickedPos());
        return $$1.m_60713_(this) ? (BlockState) $$1.m_61124_(EGGS, Math.min(4, (Integer) $$1.m_61143_(EGGS) + 1)) : super.getStateForPlacement(blockPlaceContext0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return blockState0.m_61143_(EGGS) > 1 ? MULTIPLE_EGGS_AABB : ONE_EGG_AABB;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(HATCH, EGGS);
    }

    private boolean canDestroyEgg(Level level0, Entity entity1) {
        if (entity1 instanceof Turtle || entity1 instanceof Bat) {
            return false;
        } else {
            return !(entity1 instanceof LivingEntity) ? false : entity1 instanceof Player || level0.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        }
    }
}