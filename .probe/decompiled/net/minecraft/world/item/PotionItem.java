package net.minecraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class PotionItem extends Item {

    private static final int DRINK_DURATION = 32;

    public PotionItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.WATER);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack0, Level level1, LivingEntity livingEntity2) {
        Player $$3 = livingEntity2 instanceof Player ? (Player) livingEntity2 : null;
        if ($$3 instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) $$3, itemStack0);
        }
        if (!level1.isClientSide) {
            for (MobEffectInstance $$5 : PotionUtils.getMobEffects(itemStack0)) {
                if ($$5.getEffect().isInstantenous()) {
                    $$5.getEffect().applyInstantenousEffect($$3, $$3, livingEntity2, $$5.getAmplifier(), 1.0);
                } else {
                    livingEntity2.addEffect(new MobEffectInstance($$5));
                }
            }
        }
        if ($$3 != null) {
            $$3.awardStat(Stats.ITEM_USED.get(this));
            if (!$$3.getAbilities().instabuild) {
                itemStack0.shrink(1);
            }
        }
        if ($$3 == null || !$$3.getAbilities().instabuild) {
            if (itemStack0.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            if ($$3 != null) {
                $$3.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        livingEntity2.m_146850_(GameEvent.DRINK);
        return itemStack0;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        Player $$3 = useOnContext0.getPlayer();
        ItemStack $$4 = useOnContext0.getItemInHand();
        BlockState $$5 = $$1.getBlockState($$2);
        if (useOnContext0.getClickedFace() != Direction.DOWN && $$5.m_204336_(BlockTags.CONVERTABLE_TO_MUD) && PotionUtils.getPotion($$4) == Potions.WATER) {
            $$1.playSound(null, $$2, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
            $$3.m_21008_(useOnContext0.getHand(), ItemUtils.createFilledResult($$4, $$3, new ItemStack(Items.GLASS_BOTTLE)));
            $$3.awardStat(Stats.ITEM_USED.get($$4.getItem()));
            if (!$$1.isClientSide) {
                ServerLevel $$6 = (ServerLevel) $$1;
                for (int $$7 = 0; $$7 < 5; $$7++) {
                    $$6.sendParticles(ParticleTypes.SPLASH, (double) $$2.m_123341_() + $$1.random.nextDouble(), (double) ($$2.m_123342_() + 1), (double) $$2.m_123343_() + $$1.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                }
            }
            $$1.playSound(null, $$2, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            $$1.m_142346_(null, GameEvent.FLUID_PLACE, $$2);
            $$1.setBlockAndUpdate($$2, Blocks.MUD.defaultBlockState());
            return InteractionResult.sidedSuccess($$1.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        return ItemUtils.startUsingInstantly(level0, player1, interactionHand2);
    }

    @Override
    public String getDescriptionId(ItemStack itemStack0) {
        return PotionUtils.getPotion(itemStack0).getName(this.m_5524_() + ".effect.");
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        PotionUtils.addPotionTooltip(itemStack0, listComponent2, 1.0F);
    }
}