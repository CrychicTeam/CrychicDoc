package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class RedStoneOreBlock extends Block {

    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public RedStoneOreBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(LIT, false));
    }

    @Override
    public void attack(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3) {
        interact(blockState0, level1, blockPos2);
        super.m_6256_(blockState0, level1, blockPos2, player3);
    }

    @Override
    public void stepOn(Level level0, BlockPos blockPos1, BlockState blockState2, Entity entity3) {
        if (!entity3.isSteppingCarefully()) {
            interact(blockState2, level0, blockPos1);
        }
        super.stepOn(level0, blockPos1, blockState2, entity3);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.isClientSide) {
            spawnParticles(level1, blockPos2);
        } else {
            interact(blockState0, level1, blockPos2);
        }
        ItemStack $$6 = player3.m_21120_(interactionHand4);
        return $$6.getItem() instanceof BlockItem && new BlockPlaceContext(player3, interactionHand4, $$6, blockHitResult5).canPlace() ? InteractionResult.PASS : InteractionResult.SUCCESS;
    }

    private static void interact(BlockState blockState0, Level level1, BlockPos blockPos2) {
        spawnParticles(level1, blockPos2);
        if (!(Boolean) blockState0.m_61143_(LIT)) {
            level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(LIT, true), 3);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return (Boolean) blockState0.m_61143_(LIT);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(LIT)) {
            serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(LIT, false), 3);
        }
    }

    @Override
    public void spawnAfterBreak(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, ItemStack itemStack3, boolean boolean4) {
        super.m_213646_(blockState0, serverLevel1, blockPos2, itemStack3, boolean4);
        if (boolean4 && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack3) == 0) {
            int $$5 = 1 + serverLevel1.f_46441_.nextInt(5);
            this.m_49805_(serverLevel1, blockPos2, $$5);
        }
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(LIT)) {
            spawnParticles(level1, blockPos2);
        }
    }

    private static void spawnParticles(Level level0, BlockPos blockPos1) {
        double $$2 = 0.5625;
        RandomSource $$3 = level0.random;
        for (Direction $$4 : Direction.values()) {
            BlockPos $$5 = blockPos1.relative($$4);
            if (!level0.getBlockState($$5).m_60804_(level0, $$5)) {
                Direction.Axis $$6 = $$4.getAxis();
                double $$7 = $$6 == Direction.Axis.X ? 0.5 + 0.5625 * (double) $$4.getStepX() : (double) $$3.nextFloat();
                double $$8 = $$6 == Direction.Axis.Y ? 0.5 + 0.5625 * (double) $$4.getStepY() : (double) $$3.nextFloat();
                double $$9 = $$6 == Direction.Axis.Z ? 0.5 + 0.5625 * (double) $$4.getStepZ() : (double) $$3.nextFloat();
                level0.addParticle(DustParticleOptions.REDSTONE, (double) blockPos1.m_123341_() + $$7, (double) blockPos1.m_123342_() + $$8, (double) blockPos1.m_123343_() + $$9, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(LIT);
    }
}