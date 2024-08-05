package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.server.entity.living.DinosaurEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.RelicheirusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class PrehistoricMixtureItem extends BowlFoodItem {

    public PrehistoricMixtureItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand hand) {
        FoodProperties foodProperties = itemStack.getFoodProperties(livingEntity);
        if (!livingEntity.m_9236_().isClientSide && livingEntity instanceof Mob && this.canFeedMob(player, (Mob) livingEntity) && foodProperties != null) {
            label56: {
                livingEntity.heal((float) foodProperties.getNutrition());
                if (livingEntity instanceof DinosaurEntity dinosaur && dinosaur.onFeedMixture(itemStack, player)) {
                    break label56;
                }
                if (!foodProperties.getEffects().isEmpty()) {
                    for (Pair<MobEffectInstance, Float> mobEffectInstance : foodProperties.getEffects()) {
                        livingEntity.addEffect((MobEffectInstance) mobEffectInstance.getFirst());
                    }
                }
                if (this == ACItemRegistry.SERENE_SALAD.get()) {
                    livingEntity.removeEffect(ACEffectRegistry.STUNNED.get());
                }
            }
            for (int i = 0; i < 4 + livingEntity.getRandom().nextInt(3); i++) {
                ((ServerLevel) livingEntity.m_9236_()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemStack), livingEntity.m_20208_(0.8F), livingEntity.m_20187_(), livingEntity.m_20262_(0.8F), 0, 0.0, 0.0, 0.0, 0.0);
            }
            if (!player.isCreative()) {
                itemStack.shrink(1);
                if (!player.addItem(new ItemStack(Items.BOWL))) {
                    player.drop(new ItemStack(Items.BOWL), true);
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.m_6880_(itemStack, player, livingEntity, hand);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (this == ACItemRegistry.SERENE_SALAD.get()) {
            livingEntity.removeEffect(ACEffectRegistry.STUNNED.get());
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }

    private boolean canFeedMob(Player player, Mob mob) {
        if (mob instanceof TremorsaurusEntity && mob.m_21023_(ACEffectRegistry.STUNNED.get()) && this == ACItemRegistry.SERENE_SALAD.get()) {
            return true;
        } else {
            if (mob instanceof RelicheirusEntity relicheirus && relicheirus.getPushingTreesFor() > 0 && this == ACItemRegistry.PRIMORDIAL_SOUP.get()) {
                return false;
            }
            LivingEntity target = mob.getTarget();
            return target == null || !target.m_7306_(player);
        }
    }
}