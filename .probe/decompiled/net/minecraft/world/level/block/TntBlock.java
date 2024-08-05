package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class TntBlock extends Block {

    public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;

    public TntBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(UNSTABLE, false));
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState3.m_60713_(blockState0.m_60734_())) {
            if (level1.m_276867_(blockPos2)) {
                explode(level1, blockPos2);
                level1.removeBlock(blockPos2, false);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (level1.m_276867_(blockPos2)) {
            explode(level1, blockPos2);
            level1.removeBlock(blockPos2, false);
        }
    }

    @Override
    public void playerWillDestroy(Level level0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        if (!level0.isClientSide() && !player3.isCreative() && (Boolean) blockState2.m_61143_(UNSTABLE)) {
            explode(level0, blockPos1);
        }
        super.playerWillDestroy(level0, blockPos1, blockState2, player3);
    }

    @Override
    public void wasExploded(Level level0, BlockPos blockPos1, Explosion explosion2) {
        if (!level0.isClientSide) {
            PrimedTnt $$3 = new PrimedTnt(level0, (double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_() + 0.5, explosion2.getIndirectSourceEntity());
            int $$4 = $$3.getFuse();
            $$3.setFuse((short) (level0.random.nextInt($$4 / 4) + $$4 / 8));
            level0.m_7967_($$3);
        }
    }

    public static void explode(Level level0, BlockPos blockPos1) {
        explode(level0, blockPos1, null);
    }

    private static void explode(Level level0, BlockPos blockPos1, @Nullable LivingEntity livingEntity2) {
        if (!level0.isClientSide) {
            PrimedTnt $$3 = new PrimedTnt(level0, (double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_() + 0.5, livingEntity2);
            level0.m_7967_($$3);
            level0.playSound(null, $$3.m_20185_(), $$3.m_20186_(), $$3.m_20189_(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level0.m_142346_(livingEntity2, GameEvent.PRIME_FUSE, blockPos1);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        ItemStack $$6 = player3.m_21120_(interactionHand4);
        if (!$$6.is(Items.FLINT_AND_STEEL) && !$$6.is(Items.FIRE_CHARGE)) {
            return super.m_6227_(blockState0, level1, blockPos2, player3, interactionHand4, blockHitResult5);
        } else {
            explode(level1, blockPos2, player3);
            level1.setBlock(blockPos2, Blocks.AIR.defaultBlockState(), 11);
            Item $$7 = $$6.getItem();
            if (!player3.isCreative()) {
                if ($$6.is(Items.FLINT_AND_STEEL)) {
                    $$6.hurtAndBreak(1, player3, p_57425_ -> p_57425_.m_21190_(interactionHand4));
                } else {
                    $$6.shrink(1);
                }
            }
            player3.awardStat(Stats.ITEM_USED.get($$7));
            return InteractionResult.sidedSuccess(level1.isClientSide);
        }
    }

    @Override
    public void onProjectileHit(Level level0, BlockState blockState1, BlockHitResult blockHitResult2, Projectile projectile3) {
        if (!level0.isClientSide) {
            BlockPos $$4 = blockHitResult2.getBlockPos();
            Entity $$5 = projectile3.getOwner();
            if (projectile3.m_6060_() && projectile3.mayInteract(level0, $$4)) {
                explode(level0, $$4, $$5 instanceof LivingEntity ? (LivingEntity) $$5 : null);
                level0.removeBlock($$4, false);
            }
        }
    }

    @Override
    public boolean dropFromExplosion(Explosion explosion0) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(UNSTABLE);
    }
}