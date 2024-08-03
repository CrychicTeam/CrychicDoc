package dev.xkmc.l2hostility.events;

import dev.xkmc.l2damagetracker.init.data.ArmorEffectConfig;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import dev.xkmc.l2hostility.init.network.LootDataToClient;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.mixin.ForgeInternalHandlerAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "l2hostility", bus = Bus.FORGE)
public class MobEvents {

    @SubscribeEvent
    public static void onMobAttack(LivingAttackEvent event) {
        boolean bypassInvul = event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY);
        boolean bypassMagic = event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS);
        boolean magic = event.getSource().is(L2DamageTypes.MAGIC);
        if (magic && !bypassInvul && !bypassMagic && CurioCompat.hasItemInCurio(event.getEntity(), (Item) LHItems.RING_DIVINITY.get())) {
            event.setCanceled(true);
        } else {
            if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
                ((MobTraitCap) MobTraitCap.HOLDER.get(event.getEntity())).traitEvent((k, v) -> k.onAttackedByOthers(v, event.getEntity(), event));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onMobHurt(LivingHurtEvent event) {
        if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
            ((MobTraitCap) MobTraitCap.HOLDER.get(event.getEntity())).traitEvent((k, v) -> k.onHurtByOthers(v, event.getEntity(), event));
        } else if (event.getEntity() instanceof Player player && !event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && CurioCompat.hasItemInCurio(player, (Item) LHItems.CURSE_PRIDE.get())) {
            int level = PlayerDifficulty.HOLDER.get(player).getLevel().getLevel();
            double rate = LHConfig.COMMON.prideHealthBonus.get();
            double factor = 1.0 + rate * (double) level;
            event.setAmount((float) ((double) event.getAmount() / factor));
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        for (ItemStack e : CurioCompat.getItems(event.getEntity(), ex -> ex.getItem() instanceof CurseCurioItem)) {
            if (e.getItem() instanceof CurseCurioItem curse) {
                curse.onDamage(e, event.getEntity(), event);
            }
        }
    }

    @SubscribeEvent
    public static void onMobDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            LivingEntity credit = mob.m_21232_();
            if (credit != null && CurioCompat.hasItemInCurio(credit, (Item) LHItems.CURSE_LUST.get())) {
                for (EquipmentSlot e : EquipmentSlot.values()) {
                    mob.setDropChance(e, 1.0F);
                }
            }
        }
        if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
            ((MobTraitCap) MobTraitCap.HOLDER.get(event.getEntity())).traitEvent((k, v) -> k.onDeath(v, event.getEntity(), event));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onMobDrop(LivingDropsEvent event) {
        if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(event.getEntity());
            if (cap.noDrop) {
                event.setCanceled(true);
                return;
            }
            LivingEntity killer = event.getEntity().getKillCredit();
            if (killer != null && CurioCompat.hasItemInCurio(killer, (Item) LHItems.NIDHOGGUR.get())) {
                double val = LHConfig.COMMON.nidhoggurDropFactor.get() * (double) cap.getLevel();
                int count = (int) val;
                if (event.getEntity().getRandom().nextDouble() < val - (double) count) {
                    count++;
                }
                count++;
                for (ItemEntity stack : event.getDrops()) {
                    stack.getItem().setCount(stack.getItem().getCount() * count);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onExpDrop(LivingExperienceDropEvent event) {
        if (MobTraitCap.HOLDER.isProper(event.getEntity())) {
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(event.getEntity());
            if (cap.noDrop) {
                event.setCanceled(true);
                return;
            }
            int exp = event.getDroppedExperience();
            int level = cap.getLevel();
            exp = (int) ((double) exp * (1.0 + LHConfig.COMMON.expDropFactor.get() * (double) level));
            event.setDroppedExperience(exp);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPotionTest(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        if (CurioCompat.hasItemInCurio(entity, (Item) LHItems.CURSE_WRATH.get())) {
            Set<MobEffect> config = ArmorEffectConfig.get().getImmunity(LHItems.CURSE_WRATH.getId().toString());
            if (config.contains(event.getEffectInstance().getEffect())) {
                event.setResult(Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        List<TraitLootModifier> list = new ArrayList();
        for (IGlobalLootModifier e : ForgeInternalHandlerAccessor.callGetLootModifierManager().getAllLootMods()) {
            if (e instanceof TraitLootModifier loot) {
                list.add(loot);
            }
        }
        LootDataToClient packet = new LootDataToClient(list);
        if (event.getPlayer() == null) {
            L2Hostility.HANDLER.toAllClient(packet);
        } else {
            L2Hostility.HANDLER.toClientPlayer(packet, event.getPlayer());
        }
    }
}