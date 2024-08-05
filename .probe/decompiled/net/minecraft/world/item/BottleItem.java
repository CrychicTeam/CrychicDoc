package net.minecraft.world.item;

import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class BottleItem extends Item {

    public BottleItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        List<AreaEffectCloud> $$3 = level0.m_6443_(AreaEffectCloud.class, player1.m_20191_().inflate(2.0), p_289499_ -> p_289499_ != null && p_289499_.m_6084_() && p_289499_.getOwner() instanceof EnderDragon);
        ItemStack $$4 = player1.m_21120_(interactionHand2);
        if (!$$3.isEmpty()) {
            AreaEffectCloud $$5 = (AreaEffectCloud) $$3.get(0);
            $$5.setRadius($$5.getRadius() - 0.5F);
            level0.playSound(null, player1.m_20185_(), player1.m_20186_(), player1.m_20189_(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
            level0.m_220400_(player1, GameEvent.FLUID_PICKUP, player1.m_20182_());
            if (player1 instanceof ServerPlayer $$6) {
                CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger($$6, $$4, $$5);
            }
            return InteractionResultHolder.sidedSuccess(this.turnBottleIntoItem($$4, player1, new ItemStack(Items.DRAGON_BREATH)), level0.isClientSide());
        } else {
            BlockHitResult $$7 = m_41435_(level0, player1, ClipContext.Fluid.SOURCE_ONLY);
            if ($$7.getType() == HitResult.Type.MISS) {
                return InteractionResultHolder.pass($$4);
            } else {
                if ($$7.getType() == HitResult.Type.BLOCK) {
                    BlockPos $$8 = $$7.getBlockPos();
                    if (!level0.mayInteract(player1, $$8)) {
                        return InteractionResultHolder.pass($$4);
                    }
                    if (level0.getFluidState($$8).is(FluidTags.WATER)) {
                        level0.playSound(player1, player1.m_20185_(), player1.m_20186_(), player1.m_20189_(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                        level0.m_142346_(player1, GameEvent.FLUID_PICKUP, $$8);
                        return InteractionResultHolder.sidedSuccess(this.turnBottleIntoItem($$4, player1, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)), level0.isClientSide());
                    }
                }
                return InteractionResultHolder.pass($$4);
            }
        }
    }

    protected ItemStack turnBottleIntoItem(ItemStack itemStack0, Player player1, ItemStack itemStack2) {
        player1.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.createFilledResult(itemStack0, player1, itemStack2);
    }
}