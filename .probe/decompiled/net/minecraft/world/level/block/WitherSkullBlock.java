package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

public class WitherSkullBlock extends SkullBlock {

    @Nullable
    private static BlockPattern witherPatternFull;

    @Nullable
    private static BlockPattern witherPatternBase;

    protected WitherSkullBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(SkullBlock.Types.WITHER_SKELETON, blockBehaviourProperties0);
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, @Nullable LivingEntity livingEntity3, ItemStack itemStack4) {
        super.m_6402_(level0, blockPos1, blockState2, livingEntity3, itemStack4);
        BlockEntity $$5 = level0.getBlockEntity(blockPos1);
        if ($$5 instanceof SkullBlockEntity) {
            checkSpawn(level0, blockPos1, (SkullBlockEntity) $$5);
        }
    }

    public static void checkSpawn(Level level0, BlockPos blockPos1, SkullBlockEntity skullBlockEntity2) {
        if (!level0.isClientSide) {
            BlockState $$3 = skullBlockEntity2.m_58900_();
            boolean $$4 = $$3.m_60713_(Blocks.WITHER_SKELETON_SKULL) || $$3.m_60713_(Blocks.WITHER_SKELETON_WALL_SKULL);
            if ($$4 && blockPos1.m_123342_() >= level0.m_141937_() && level0.m_46791_() != Difficulty.PEACEFUL) {
                BlockPattern.BlockPatternMatch $$5 = getOrCreateWitherFull().find(level0, blockPos1);
                if ($$5 != null) {
                    WitherBoss $$6 = EntityType.WITHER.create(level0);
                    if ($$6 != null) {
                        CarvedPumpkinBlock.clearPatternBlocks(level0, $$5);
                        BlockPos $$7 = $$5.getBlock(1, 2, 0).getPos();
                        $$6.m_7678_((double) $$7.m_123341_() + 0.5, (double) $$7.m_123342_() + 0.55, (double) $$7.m_123343_() + 0.5, $$5.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F, 0.0F);
                        $$6.f_20883_ = $$5.getForwards().getAxis() == Direction.Axis.X ? 0.0F : 90.0F;
                        $$6.makeInvulnerable();
                        for (ServerPlayer $$8 : level0.m_45976_(ServerPlayer.class, $$6.m_20191_().inflate(50.0))) {
                            CriteriaTriggers.SUMMONED_ENTITY.trigger($$8, $$6);
                        }
                        level0.m_7967_($$6);
                        CarvedPumpkinBlock.updatePatternBlocks(level0, $$5);
                    }
                }
            }
        }
    }

    public static boolean canSpawnMob(Level level0, BlockPos blockPos1, ItemStack itemStack2) {
        return itemStack2.is(Items.WITHER_SKELETON_SKULL) && blockPos1.m_123342_() >= level0.m_141937_() + 2 && level0.m_46791_() != Difficulty.PEACEFUL && !level0.isClientSide ? getOrCreateWitherBase().find(level0, blockPos1) != null : false;
    }

    private static BlockPattern getOrCreateWitherFull() {
        if (witherPatternFull == null) {
            witherPatternFull = BlockPatternBuilder.start().aisle("^^^", "###", "~#~").where('#', p_58272_ -> p_58272_.getState().m_204336_(BlockTags.WITHER_SUMMON_BASE_BLOCKS)).where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_SKULL).or(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_WALL_SKULL)))).where('~', p_284877_ -> p_284877_.getState().m_60795_()).build();
        }
        return witherPatternFull;
    }

    private static BlockPattern getOrCreateWitherBase() {
        if (witherPatternBase == null) {
            witherPatternBase = BlockPatternBuilder.start().aisle("   ", "###", "~#~").where('#', p_58266_ -> p_58266_.getState().m_204336_(BlockTags.WITHER_SUMMON_BASE_BLOCKS)).where('~', p_284878_ -> p_284878_.getState().m_60795_()).build();
        }
        return witherPatternBase;
    }
}