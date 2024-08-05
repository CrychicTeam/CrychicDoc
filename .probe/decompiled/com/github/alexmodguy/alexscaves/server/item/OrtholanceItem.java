package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.WaveEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class OrtholanceItem extends Item implements Vanishable {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public OrtholanceItem(Item.Properties properties) {
        super(properties);
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Tool modifier", 5.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Tool modifier", -2.4F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.BOW;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int useTime) {
        int i = Mth.clamp(this.getUseDuration(stack) - useTime, 0, 60);
        int flinging = stack.getEnchantmentLevel(ACEnchantmentRegistry.FLINGING.get());
        boolean tsunami = stack.getEnchantmentLevel(ACEnchantmentRegistry.TSUNAMI.get()) > 0;
        if (i > 0) {
            float f = 0.1F * (float) i + (float) flinging * 0.1F;
            Vec3 vec3 = livingEntity.m_20184_().add(livingEntity.m_20252_(1.0F).normalize().multiply((double) f, (double) (f * 0.15F), (double) f));
            if (i >= 10 && !level.isClientSide) {
                level.playSound(null, livingEntity, ACSoundRegistry.ORTHOLANCE_WAVE.get(), SoundSource.NEUTRAL, 4.0F, 1.0F);
                stack.hurtAndBreak(1, livingEntity, player1 -> player1.broadcastBreakEvent(player1.getUsedItemHand()));
                int maxWaves = i / 5;
                if (tsunami) {
                    maxWaves = 5;
                    Vec3 waveCenterPos = livingEntity.m_20182_().add(vec3);
                    WaveEntity tsunamiWaveEntity = new WaveEntity(level, livingEntity);
                    tsunamiWaveEntity.m_6034_(waveCenterPos.x, livingEntity.m_20186_(), waveCenterPos.z);
                    tsunamiWaveEntity.setLifespan(20);
                    tsunamiWaveEntity.setWaveScale(5.0F);
                    tsunamiWaveEntity.setWaitingTicks(2);
                    tsunamiWaveEntity.setYRot(-((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI)));
                    level.m_7967_(tsunamiWaveEntity);
                } else {
                    for (int wave = 0; wave < maxWaves; wave++) {
                        float f1 = (float) wave / (float) maxWaves;
                        int lifespan = 3 + (int) ((1.0F - f1) * 3.0F);
                        Vec3 waveCenterPos = livingEntity.m_20182_().add(vec3.scale((double) (f1 * 2.0F)));
                        WaveEntity leftWaveEntity = new WaveEntity(level, livingEntity);
                        leftWaveEntity.m_6034_(waveCenterPos.x, livingEntity.m_20186_(), waveCenterPos.z);
                        leftWaveEntity.setLifespan(lifespan);
                        leftWaveEntity.setYRot(-((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI)) + 60.0F - (float) (15 * wave));
                        level.m_7967_(leftWaveEntity);
                        WaveEntity rightWaveEntity = new WaveEntity(level, livingEntity);
                        rightWaveEntity.m_6034_(waveCenterPos.x, livingEntity.m_20186_(), waveCenterPos.z);
                        rightWaveEntity.setLifespan(lifespan);
                        rightWaveEntity.setYRot(-((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI)) - 60.0F + (float) (15 * wave));
                        level.m_7967_(rightWaveEntity);
                    }
                    if (stack.getEnchantmentLevel(ACEnchantmentRegistry.SECOND_WAVE.get()) > 0) {
                        int maxSecondWaves = Math.max(1, maxWaves - 1);
                        for (int wave = 0; wave < maxSecondWaves; wave++) {
                            float f1 = (float) wave / (float) maxSecondWaves;
                            int lifespan = 3 + (int) ((1.0F - f1) * 3.0F);
                            Vec3 waveCenterPos = livingEntity.m_20182_().add(vec3.scale((double) (f1 * 2.0F)));
                            WaveEntity leftWaveEntity = new WaveEntity(level, livingEntity);
                            leftWaveEntity.m_6034_(waveCenterPos.x, livingEntity.m_20186_(), waveCenterPos.z);
                            leftWaveEntity.setLifespan(lifespan);
                            leftWaveEntity.setYRot(-((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI)) + 60.0F - (float) (15 * wave));
                            leftWaveEntity.setWaitingTicks(8);
                            level.m_7967_(leftWaveEntity);
                            WaveEntity rightWaveEntity = new WaveEntity(level, livingEntity);
                            rightWaveEntity.m_6034_(waveCenterPos.x, livingEntity.m_20186_(), waveCenterPos.z);
                            rightWaveEntity.setLifespan(lifespan);
                            rightWaveEntity.setYRot(-((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI)) - 60.0F + (float) (15 * wave));
                            rightWaveEntity.setWaitingTicks(8);
                            level.m_7967_(rightWaveEntity);
                        }
                    }
                }
                AABB aabb = new AABB(livingEntity.m_20182_(), livingEntity.m_20182_().add(vec3.scale((double) maxWaves))).inflate(1.0);
                DamageSource source = livingEntity.m_269291_().mobAttack(livingEntity);
                double d = 0.0;
                for (AttributeModifier modifier : stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE)) {
                    d += modifier.getAmount();
                }
                for (LivingEntity entity : level.m_45976_(LivingEntity.class, aabb)) {
                    if (!livingEntity.m_7307_(entity) && !livingEntity.equals(entity) && livingEntity.hasLineOfSight(entity)) {
                        entity.hurt(source, (float) d);
                        entity.stopRiding();
                    }
                }
            }
            livingEntity.m_20256_(vec3.add(0.0, (double) ((livingEntity.m_20096_() ? 0.2F : 0.0F) + (float) flinging * 0.1F), 0.0));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.m_21120_(interactionHand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.m_6672_(interactionHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity hurt, LivingEntity player) {
        stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        Vec3 vec3 = player.m_20252_(1.0F);
        if (stack.getEnchantmentLevel(ACEnchantmentRegistry.SEA_SWING.get()) > 0) {
            WaveEntity waveEntity = new WaveEntity(hurt.m_9236_(), player);
            waveEntity.m_6034_(player.m_20185_(), hurt.m_20186_(), player.m_20189_());
            waveEntity.setLifespan(5);
            waveEntity.setYRot(-((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI)));
            player.m_9236_().m_7967_(waveEntity);
        }
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState state, BlockPos blockPos, LivingEntity livingEntity) {
        if ((double) state.m_60800_(level, blockPos) != 0.0) {
            itemStack.hurtAndBreak(2, livingEntity, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsCaves.PROXY.getISTERProperties());
    }
}