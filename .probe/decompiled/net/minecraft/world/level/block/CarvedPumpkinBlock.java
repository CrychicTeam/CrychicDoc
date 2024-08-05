package net.minecraft.world.level.block;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class CarvedPumpkinBlock extends HorizontalDirectionalBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    @Nullable
    private BlockPattern snowGolemBase;

    @Nullable
    private BlockPattern snowGolemFull;

    @Nullable
    private BlockPattern ironGolemBase;

    @Nullable
    private BlockPattern ironGolemFull;

    private static final Predicate<BlockState> PUMPKINS_PREDICATE = p_51396_ -> p_51396_ != null && (p_51396_.m_60713_(Blocks.CARVED_PUMPKIN) || p_51396_.m_60713_(Blocks.JACK_O_LANTERN));

    protected CarvedPumpkinBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState3.m_60713_(blockState0.m_60734_())) {
            this.trySpawnGolem(level1, blockPos2);
        }
    }

    public boolean canSpawnGolem(LevelReader levelReader0, BlockPos blockPos1) {
        return this.getOrCreateSnowGolemBase().find(levelReader0, blockPos1) != null || this.getOrCreateIronGolemBase().find(levelReader0, blockPos1) != null;
    }

    private void trySpawnGolem(Level level0, BlockPos blockPos1) {
        BlockPattern.BlockPatternMatch $$2 = this.getOrCreateSnowGolemFull().find(level0, blockPos1);
        if ($$2 != null) {
            SnowGolem $$3 = EntityType.SNOW_GOLEM.create(level0);
            if ($$3 != null) {
                spawnGolemInWorld(level0, $$2, $$3, $$2.getBlock(0, 2, 0).getPos());
            }
        } else {
            BlockPattern.BlockPatternMatch $$4 = this.getOrCreateIronGolemFull().find(level0, blockPos1);
            if ($$4 != null) {
                IronGolem $$5 = EntityType.IRON_GOLEM.create(level0);
                if ($$5 != null) {
                    $$5.setPlayerCreated(true);
                    spawnGolemInWorld(level0, $$4, $$5, $$4.getBlock(1, 2, 0).getPos());
                }
            }
        }
    }

    private static void spawnGolemInWorld(Level level0, BlockPattern.BlockPatternMatch blockPatternBlockPatternMatch1, Entity entity2, BlockPos blockPos3) {
        clearPatternBlocks(level0, blockPatternBlockPatternMatch1);
        entity2.moveTo((double) blockPos3.m_123341_() + 0.5, (double) blockPos3.m_123342_() + 0.05, (double) blockPos3.m_123343_() + 0.5, 0.0F, 0.0F);
        level0.m_7967_(entity2);
        for (ServerPlayer $$4 : level0.m_45976_(ServerPlayer.class, entity2.getBoundingBox().inflate(5.0))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger($$4, entity2);
        }
        updatePatternBlocks(level0, blockPatternBlockPatternMatch1);
    }

    public static void clearPatternBlocks(Level level0, BlockPattern.BlockPatternMatch blockPatternBlockPatternMatch1) {
        for (int $$2 = 0; $$2 < blockPatternBlockPatternMatch1.getWidth(); $$2++) {
            for (int $$3 = 0; $$3 < blockPatternBlockPatternMatch1.getHeight(); $$3++) {
                BlockInWorld $$4 = blockPatternBlockPatternMatch1.getBlock($$2, $$3, 0);
                level0.setBlock($$4.getPos(), Blocks.AIR.defaultBlockState(), 2);
                level0.m_46796_(2001, $$4.getPos(), Block.getId($$4.getState()));
            }
        }
    }

    public static void updatePatternBlocks(Level level0, BlockPattern.BlockPatternMatch blockPatternBlockPatternMatch1) {
        for (int $$2 = 0; $$2 < blockPatternBlockPatternMatch1.getWidth(); $$2++) {
            for (int $$3 = 0; $$3 < blockPatternBlockPatternMatch1.getHeight(); $$3++) {
                BlockInWorld $$4 = blockPatternBlockPatternMatch1.getBlock($$2, $$3, 0);
                level0.m_6289_($$4.getPos(), Blocks.AIR);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.m_8125_().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING);
    }

    private BlockPattern getOrCreateSnowGolemBase() {
        if (this.snowGolemBase == null) {
            this.snowGolemBase = BlockPatternBuilder.start().aisle(" ", "#", "#").where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }
        return this.snowGolemBase;
    }

    private BlockPattern getOrCreateSnowGolemFull() {
        if (this.snowGolemFull == null) {
            this.snowGolemFull = BlockPatternBuilder.start().aisle("^", "#", "#").where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }
        return this.snowGolemFull;
    }

    private BlockPattern getOrCreateIronGolemBase() {
        if (this.ironGolemBase == null) {
            this.ironGolemBase = BlockPatternBuilder.start().aisle("~ ~", "###", "~#~").where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', p_284869_ -> p_284869_.getState().m_60795_()).build();
        }
        return this.ironGolemBase;
    }

    private BlockPattern getOrCreateIronGolemFull() {
        if (this.ironGolemFull == null) {
            this.ironGolemFull = BlockPatternBuilder.start().aisle("~^~", "###", "~#~").where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', p_284868_ -> p_284868_.getState().m_60795_()).build();
        }
        return this.ironGolemFull;
    }
}