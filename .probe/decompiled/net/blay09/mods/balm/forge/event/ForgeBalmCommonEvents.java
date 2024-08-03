package net.blay09.mods.balm.forge.event;

import net.blay09.mods.balm.api.event.BreakBlockEvent;
import net.blay09.mods.balm.api.event.ChunkTrackingEvent;
import net.blay09.mods.balm.api.event.CropGrowEvent;
import net.blay09.mods.balm.api.event.DigSpeedEvent;
import net.blay09.mods.balm.api.event.EntityAddedEvent;
import net.blay09.mods.balm.api.event.ItemCraftedEvent;
import net.blay09.mods.balm.api.event.LivingDamageEvent;
import net.blay09.mods.balm.api.event.LivingDeathEvent;
import net.blay09.mods.balm.api.event.LivingFallEvent;
import net.blay09.mods.balm.api.event.LivingHealEvent;
import net.blay09.mods.balm.api.event.PlayerAttackEvent;
import net.blay09.mods.balm.api.event.PlayerChangedDimensionEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.balm.api.event.PlayerLogoutEvent;
import net.blay09.mods.balm.api.event.PlayerRespawnEvent;
import net.blay09.mods.balm.api.event.TickPhase;
import net.blay09.mods.balm.api.event.TickType;
import net.blay09.mods.balm.api.event.TossItemEvent;
import net.blay09.mods.balm.api.event.UseBlockEvent;
import net.blay09.mods.balm.api.event.UseItemEvent;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.balm.api.event.server.ServerStoppedEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ForgeBalmCommonEvents {

    public static void registerEvents(ForgeBalmEvents events) {
        events.registerTickEvent(TickType.Server, TickPhase.Start, handler -> MinecraftForge.EVENT_BUS.addListener(orig -> {
            if (orig.phase == TickEvent.Phase.START) {
                handler.handle(ServerLifecycleHooks.getCurrentServer());
            }
        }));
        events.registerTickEvent(TickType.Server, TickPhase.End, handler -> MinecraftForge.EVENT_BUS.addListener(orig -> {
            if (orig.phase == TickEvent.Phase.END) {
                handler.handle(ServerLifecycleHooks.getCurrentServer());
            }
        }));
        events.registerTickEvent(TickType.ServerLevel, TickPhase.Start, handler -> MinecraftForge.EVENT_BUS.addListener(orig -> {
            if (orig.phase == TickEvent.Phase.START && orig.side == LogicalSide.SERVER) {
                handler.handle(orig.level);
            }
        }));
        events.registerTickEvent(TickType.ServerLevel, TickPhase.End, handler -> MinecraftForge.EVENT_BUS.addListener(orig -> {
            if (orig.phase == TickEvent.Phase.END && orig.side == LogicalSide.SERVER) {
                handler.handle(orig.level);
            }
        }));
        events.registerTickEvent(TickType.ServerPlayer, TickPhase.Start, handler -> MinecraftForge.EVENT_BUS.addListener(orig -> {
            if (orig.phase == TickEvent.Phase.START && orig.side == LogicalSide.SERVER) {
                handler.handle((ServerPlayer) orig.player);
            }
        }));
        events.registerTickEvent(TickType.ServerPlayer, TickPhase.End, handler -> MinecraftForge.EVENT_BUS.addListener(orig -> {
            if (orig.phase == TickEvent.Phase.END && orig.side == LogicalSide.SERVER) {
                handler.handle((ServerPlayer) orig.player);
            }
        }));
        events.registerEvent(ServerStartedEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ServerStartedEvent event = new ServerStartedEvent(orig.getServer());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(ServerStoppedEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ServerStoppedEvent event = new ServerStoppedEvent(orig.getServer());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(UseBlockEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            UseBlockEvent event = new UseBlockEvent(orig.getEntity(), orig.getLevel(), orig.getHand(), orig.getHitVec());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCancellationResult(event.getInteractionResult());
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(UseItemEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            UseItemEvent event = new UseItemEvent(orig.getEntity(), orig.getLevel(), orig.getHand());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCancellationResult(event.getInteractionResult());
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(PlayerLoginEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            PlayerLoginEvent event = new PlayerLoginEvent((ServerPlayer) orig.getEntity());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(PlayerLogoutEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            PlayerLogoutEvent event = new PlayerLogoutEvent((ServerPlayer) orig.getEntity());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(BreakBlockEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            BlockEntity blockEntity = orig.getLevel().m_7702_(orig.getPos());
            BreakBlockEvent event = new BreakBlockEvent((Level) orig.getLevel(), orig.getPlayer(), orig.getPos(), orig.getState(), blockEntity);
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(PlayerRespawnEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            PlayerRespawnEvent event = new PlayerRespawnEvent((ServerPlayer) orig.getEntity(), (ServerPlayer) orig.getEntity());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(LivingFallEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            LivingFallEvent event = new LivingFallEvent(orig.getEntity());
            events.fireEventHandlers(priority, event);
            if (event.getFallDamageOverride() != null) {
                orig.setDamageMultiplier(0.0F);
                event.getEntity().hurt(event.getEntity().m_9236_().damageSources().fall(), event.getFallDamageOverride());
            }
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(LivingDamageEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            LivingDamageEvent event = new LivingDamageEvent(orig.getEntity(), orig.getSource(), orig.getAmount());
            events.fireEventHandlers(priority, event);
            orig.setAmount(event.getDamageAmount());
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(CropGrowEvent.Pre.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            if (orig.getLevel() instanceof Level level) {
                CropGrowEvent.Pre event = new CropGrowEvent.Pre(level, orig.getPos(), orig.getState());
                events.fireEventHandlers(priority, event);
                if (event.isCanceled()) {
                    orig.setResult(Result.DENY);
                }
            }
        }));
        events.registerEvent(CropGrowEvent.Post.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            if (orig.getLevel() instanceof Level level) {
                CropGrowEvent.Post event = new CropGrowEvent.Post(level, orig.getPos(), orig.getState());
                events.fireEventHandlers(priority, event);
            }
        }));
        events.registerEvent(ChunkTrackingEvent.Start.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ChunkTrackingEvent.Start event = new ChunkTrackingEvent.Start(orig.getLevel(), orig.getPlayer(), orig.getPos());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(ChunkTrackingEvent.Stop.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ChunkTrackingEvent.Stop event = new ChunkTrackingEvent.Stop(orig.getLevel(), orig.getPlayer(), orig.getPos());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(TossItemEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            TossItemEvent event = new TossItemEvent(orig.getPlayer(), orig.getEntity().getItem());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(PlayerAttackEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            PlayerAttackEvent event = new PlayerAttackEvent(orig.getEntity(), orig.getTarget());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(LivingHealEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            LivingHealEvent event = new LivingHealEvent(orig.getEntity(), orig.getAmount());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(DigSpeedEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            DigSpeedEvent event = new DigSpeedEvent(orig.getEntity(), orig.getState(), orig.getOriginalSpeed());
            events.fireEventHandlers(priority, event);
            if (event.getSpeedOverride() != null) {
                orig.setNewSpeed(event.getSpeedOverride());
            }
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(PlayerChangedDimensionEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            PlayerChangedDimensionEvent event = new PlayerChangedDimensionEvent((ServerPlayer) orig.getEntity(), orig.getFrom(), orig.getTo());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(ItemCraftedEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            ItemCraftedEvent event = new ItemCraftedEvent(orig.getEntity(), orig.getCrafting(), orig.getInventory());
            events.fireEventHandlers(priority, event);
        }));
        events.registerEvent(LivingDeathEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            LivingDeathEvent event = new LivingDeathEvent(orig.getEntity(), orig.getSource());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
        events.registerEvent(EntityAddedEvent.class, priority -> MinecraftForge.EVENT_BUS.addListener(ForgeBalmEvents.toForge(priority), orig -> {
            EntityAddedEvent event = new EntityAddedEvent(orig.getEntity(), orig.getLevel());
            events.fireEventHandlers(priority, event);
            if (event.isCanceled()) {
                orig.setCanceled(true);
            }
        }));
    }
}