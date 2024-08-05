package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class PrimitiveClubItem extends Item {

    private final Multimap<Attribute, AttributeModifier>[] defaultModifiers = new ImmutableMultimap[4];

    public PrimitiveClubItem(Item.Properties properties) {
        super(properties);
        for (int i = 0; i <= 3; i++) {
            this.defaultModifiers[i] = this.getStatsForEnchantmentLevel(i);
        }
    }

    private ImmutableMultimap getStatsForEnchantmentLevel(int swiftwoodLevel) {
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Tool modifier", 8.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Tool modifier", (double) Math.min(0.0F, -3.75F + 0.15F * (float) swiftwoodLevel), AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity hurtEntity, LivingEntity player) {
        stack.hurtAndBreak(1, player, entityx -> entityx.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        if (!hurtEntity.m_9236_().isClientSide) {
            SoundEvent soundEvent = ACSoundRegistry.PRIMITIVE_CLUB_MISS.get();
            if (hurtEntity.getRandom().nextFloat() < 0.8F) {
                MobEffectInstance instance = new MobEffectInstance(ACEffectRegistry.STUNNED.get(), 150 + hurtEntity.getRandom().nextInt(150), 0, false, false);
                if (hurtEntity.addEffect(instance)) {
                    AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(hurtEntity.m_19879_(), player.m_19879_(), 3, instance.getDuration()));
                    soundEvent = ACSoundRegistry.PRIMITIVE_CLUB_HIT.get();
                    int dazingEdgeLevel = stack.getEnchantmentLevel(ACEnchantmentRegistry.DAZING_SWEEP.get());
                    if (dazingEdgeLevel > 0) {
                        float f = (float) dazingEdgeLevel + 1.2F;
                        AABB aabb = AABB.ofSize(hurtEntity.m_20182_(), (double) f, (double) f, (double) f);
                        for (Entity entity : hurtEntity.m_9236_().getEntities(player, aabb, Entity::m_271807_)) {
                            if (!entity.is(hurtEntity) && !entity.isAlliedTo(player) && entity.distanceTo(hurtEntity) <= f && entity instanceof LivingEntity) {
                                LivingEntity inflict = (LivingEntity) entity;
                                MobEffectInstance instance2 = new MobEffectInstance(ACEffectRegistry.STUNNED.get(), 80 + hurtEntity.getRandom().nextInt(80), 0, false, false);
                                inflict.hurt(inflict.m_9236_().damageSources().mobAttack(player), 1.0F);
                                if (inflict.addEffect(instance2)) {
                                    AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(inflict.m_19879_(), player.m_19879_(), 3, instance2.getDuration()));
                                }
                            }
                        }
                    }
                }
            }
            player.m_9236_().playSound((Player) null, player.m_20185_(), player.m_20186_(), player.m_20189_(), soundEvent, player.m_5720_(), 1.0F, 1.0F);
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

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers[0] : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public boolean isValidRepairItem(ItemStack item, ItemStack repairItem) {
        return repairItem.is(ACItemRegistry.HEAVY_BONE.get()) || super.isValidRepairItem(item, repairItem);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        int swift = stack.getEnchantmentLevel(ACEnchantmentRegistry.SWIFTWOOD.get());
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers[Mth.clamp(swift, 0, 3)] : super.getAttributeModifiers(slot, stack);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsCaves.PROXY.getISTERProperties());
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return (double) player.getAttackStrengthScale(0.0F) < 0.95 || player.f_20921_ != 0.0F;
    }

    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.getAttackStrengthScale(0.0F) < 1.0F && player.f_20921_ > 0.0F) {
                return true;
            }
            player.f_20913_ = -1;
        }
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean held) {
        if (entity instanceof Player player && held && (double) player.getAttackStrengthScale(0.0F) < 0.95 && player.f_20921_ > 0.0F) {
            player.f_20913_--;
        }
    }
}