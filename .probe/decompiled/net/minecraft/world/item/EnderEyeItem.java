package net.minecraft.world.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class EnderEyeItem extends Item {

    public EnderEyeItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        if (!$$3.m_60713_(Blocks.END_PORTAL_FRAME) || (Boolean) $$3.m_61143_(EndPortalFrameBlock.HAS_EYE)) {
            return InteractionResult.PASS;
        } else if ($$1.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockState $$4 = (BlockState) $$3.m_61124_(EndPortalFrameBlock.HAS_EYE, true);
            Block.pushEntitiesUp($$3, $$4, $$1, $$2);
            $$1.setBlock($$2, $$4, 2);
            $$1.updateNeighbourForOutputSignal($$2, Blocks.END_PORTAL_FRAME);
            useOnContext0.getItemInHand().shrink(1);
            $$1.m_46796_(1503, $$2, 0);
            BlockPattern.BlockPatternMatch $$5 = EndPortalFrameBlock.getOrCreatePortalShape().find($$1, $$2);
            if ($$5 != null) {
                BlockPos $$6 = $$5.getFrontTopLeft().offset(-3, 0, -3);
                for (int $$7 = 0; $$7 < 3; $$7++) {
                    for (int $$8 = 0; $$8 < 3; $$8++) {
                        $$1.setBlock($$6.offset($$7, 0, $$8), Blocks.END_PORTAL.defaultBlockState(), 2);
                    }
                }
                $$1.globalLevelEvent(1038, $$6.offset(1, 0, 1), 0);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        BlockHitResult $$4 = m_41435_(level0, player1, ClipContext.Fluid.NONE);
        if ($$4.getType() == HitResult.Type.BLOCK && level0.getBlockState($$4.getBlockPos()).m_60713_(Blocks.END_PORTAL_FRAME)) {
            return InteractionResultHolder.pass($$3);
        } else {
            player1.m_6672_(interactionHand2);
            if (level0 instanceof ServerLevel $$5) {
                BlockPos $$6 = $$5.findNearestMapStructure(StructureTags.EYE_OF_ENDER_LOCATED, player1.m_20183_(), 100, false);
                if ($$6 != null) {
                    EyeOfEnder $$7 = new EyeOfEnder(level0, player1.m_20185_(), player1.m_20227_(0.5), player1.m_20189_());
                    $$7.setItem($$3);
                    $$7.signalTo($$6);
                    level0.m_214171_(GameEvent.PROJECTILE_SHOOT, $$7.m_20182_(), GameEvent.Context.of(player1));
                    level0.m_7967_($$7);
                    if (player1 instanceof ServerPlayer) {
                        CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayer) player1, $$6);
                    }
                    level0.playSound(null, player1.m_20185_(), player1.m_20186_(), player1.m_20189_(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F, 0.4F / (level0.getRandom().nextFloat() * 0.4F + 0.8F));
                    level0.m_5898_(null, 1003, player1.m_20183_(), 0);
                    if (!player1.getAbilities().instabuild) {
                        $$3.shrink(1);
                    }
                    player1.awardStat(Stats.ITEM_USED.get(this));
                    player1.m_21011_(interactionHand2, true);
                    return InteractionResultHolder.success($$3);
                }
            }
            return InteractionResultHolder.consume($$3);
        }
    }
}