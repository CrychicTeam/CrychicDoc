package com.simibubi.create.content.equipment.armor;

import com.simibubi.create.foundation.advancement.AllAdvancements;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DivingHelmetItem extends BaseArmorItem {

    public static final EquipmentSlot SLOT = EquipmentSlot.HEAD;

    public static final ArmorItem.Type TYPE = ArmorItem.Type.HELMET;

    public DivingHelmetItem(ArmorMaterial material, Item.Properties properties, ResourceLocation textureLoc) {
        super(material, TYPE, properties, textureLoc);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.AQUA_AFFINITY ? false : super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.AQUA_AFFINITY ? 1 : super.getEnchantmentLevel(stack, enchantment);
    }

    public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        Map<Enchantment, Integer> map = super.getAllEnchantments(stack);
        map.put(Enchantments.AQUA_AFFINITY, 1);
        return map;
    }

    public static boolean isWornBy(Entity entity) {
        return !getWornItem(entity).isEmpty();
    }

    public static ItemStack getWornItem(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack stack = livingEntity.getItemBySlot(SLOT);
            return !(stack.getItem() instanceof DivingHelmetItem) ? ItemStack.EMPTY : stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @SubscribeEvent
    public static void breatheUnderwater(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Level world = entity.m_9236_();
        boolean second = world.getGameTime() % 20L == 0L;
        boolean drowning = entity.m_20146_() == 0;
        if (world.isClientSide) {
            entity.getPersistentData().remove("VisualBacktankAir");
        }
        ItemStack helmet = getWornItem(entity);
        if (!helmet.isEmpty()) {
            boolean lavaDiving = entity.m_20077_();
            if (helmet.getItem().isFireResistant() || !lavaDiving) {
                if (entity.canDrownInFluidType(entity.getEyeInFluidType()) || lavaDiving) {
                    if (!(entity instanceof Player) || !((Player) entity).isCreative()) {
                        List<ItemStack> backtanks = BacktankUtil.getAllWithAir(entity);
                        if (!backtanks.isEmpty()) {
                            if (lavaDiving) {
                                if (entity instanceof ServerPlayer sp) {
                                    AllAdvancements.DIVING_SUIT_LAVA.awardTo(sp);
                                }
                                if (backtanks.stream().noneMatch(backtank -> backtank.getItem().isFireResistant())) {
                                    return;
                                }
                            }
                            if (drowning) {
                                entity.m_20301_(10);
                            }
                            if (world.isClientSide) {
                                entity.getPersistentData().putInt("VisualBacktankAir", Math.round((Float) backtanks.stream().map(BacktankUtil::getAir).reduce(0.0F, Float::sum)));
                            }
                            if (second) {
                                BacktankUtil.consumeAir(entity, (ItemStack) backtanks.get(0), 1.0F);
                                if (!lavaDiving) {
                                    if (entity instanceof ServerPlayer sp) {
                                        AllAdvancements.DIVING_SUIT.awardTo(sp);
                                    }
                                    entity.m_20301_(Math.min(entity.m_6062_(), entity.m_20146_() + 10));
                                    entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 30, 0, true, false, true));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}