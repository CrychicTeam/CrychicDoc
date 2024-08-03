package dev.xkmc.modulargolems.events;

import dev.xkmc.modulargolems.content.capability.GolemConfigCapability;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.item.card.ClickEntityFilterCard;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "modulargolems", bus = Bus.FORGE)
public class ModifierEventListeners {

    @SubscribeEvent
    public static void onGolemSpawn(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof AbstractGolemEntity<?, ?> entity) {
                entity.getModifiers().forEach((k, v) -> k.onGolemSpawn(entity, v));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onHurtPre(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof AbstractGolemEntity<?, ?> entity) {
            entity.getModifiers().forEach((k, v) -> k.onHurtTarget(entity, event, v));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onAttackPre(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof AbstractGolemEntity<?, ?> entity) {
            entity.getModifiers().forEach((k, v) -> k.onAttackTarget(entity, event, v));
        }
    }

    @SubscribeEvent
    public static void onAttacked(LivingAttackEvent event) {
        if (event.getEntity() instanceof AbstractGolemEntity<?, ?> entity) {
            entity.getModifiers().forEach((k, v) -> k.onAttacked(entity, event, v));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onHurtPost(LivingHurtEvent event) {
        if (event.getEntity() instanceof AbstractGolemEntity<?, ?> entity) {
            entity.getModifiers().forEach((k, v) -> k.onHurt(entity, event, v));
        }
    }

    @SubscribeEvent
    public static void onDamaged(LivingDamageEvent event) {
        if (event.getEntity() instanceof AbstractGolemEntity<?, ?> entity) {
            entity.getModifiers().forEach((k, v) -> k.onDamaged(entity, event, v));
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        for (Entity e : event.getAffectedEntities()) {
            if (e instanceof AbstractGolemEntity<?, ?> golem && (Integer) golem.getModifiers().getOrDefault(GolemModifiers.EXPLOSION_RES.get(), 0) > 0) {
                event.getAffectedBlocks().clear();
                return;
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (MGConfig.COMMON.doEnemyAggro.get()) {
            if (event.getEntity() instanceof Mob mob && !event.getLevel().isClientSide() && mob instanceof Enemy && !(mob instanceof Creeper)) {
                int priority = 0;
                TargetGoal ans = null;
                for (WrappedGoal goal : mob.targetSelector.getAvailableGoals()) {
                    if (goal.getGoal() instanceof NearestAttackableTargetGoal<?> target && target.targetType == IronGolem.class) {
                        priority = goal.getPriority();
                        ans = new NearestAttackableTargetGoal(mob, AbstractGolemEntity.class, target.randomInterval, target.f_26136_, target.f_26131_, null);
                        break;
                    }
                }
                if (ans != null) {
                    mob.targetSelector.addGoal(priority, ans);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDrop(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof AbstractGolemEntity<?, ?> e && e.hasFlag(GolemFlags.PICKUP)) {
            event.getDrops().forEach(x -> x.m_20219_(e.m_20182_()));
        }
    }

    @SubscribeEvent
    public static void onAttachLevelCapabilities(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject() instanceof ServerLevel level && level.m_46472_() == Level.OVERWORLD) {
            event.addCapability(new ResourceLocation("modulargolems", "command"), new GolemConfigCapability(level));
        }
    }

    @SubscribeEvent
    public static void onTargetCardClick(PlayerInteractEvent.EntityInteract event) {
        if (event.getItemStack().getItem() instanceof ClickEntityFilterCard && event.getTarget() instanceof LivingEntity le) {
            event.setCancellationResult(event.getItemStack().interactLivingEntity(event.getEntity(), le, event.getHand()));
            event.setCanceled(true);
        }
    }
}