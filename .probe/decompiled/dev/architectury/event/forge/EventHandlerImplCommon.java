package dev.architectury.event.forge;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.ChunkEvent;
import dev.architectury.event.events.common.CommandPerformEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.utils.value.IntValue;
import java.lang.ref.WeakReference;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class EventHandlerImplCommon {

    public static WeakReference<LootDataManager> lootDataManagerRef = null;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            dev.architectury.event.events.common.TickEvent.SERVER_PRE.invoker().tick(ServerLifecycleHooks.getCurrentServer());
        } else if (event.phase == TickEvent.Phase.END) {
            dev.architectury.event.events.common.TickEvent.SERVER_POST.invoker().tick(ServerLifecycleHooks.getCurrentServer());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(TickEvent.LevelTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            if (event.phase == TickEvent.Phase.START) {
                dev.architectury.event.events.common.TickEvent.SERVER_LEVEL_PRE.invoker().tick((ServerLevel) event.level);
            } else if (event.phase == TickEvent.Phase.END) {
                dev.architectury.event.events.common.TickEvent.SERVER_LEVEL_POST.invoker().tick((ServerLevel) event.level);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerStartingEvent event) {
        LifecycleEvent.SERVER_STARTING.invoker().stateChanged(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerStartedEvent event) {
        LifecycleEvent.SERVER_STARTED.invoker().stateChanged(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerStoppingEvent event) {
        LifecycleEvent.SERVER_STOPPING.invoker().stateChanged(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerStoppedEvent event) {
        LifecycleEvent.SERVER_STOPPED.invoker().stateChanged(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(RegisterCommandsEvent event) {
        CommandRegistrationEvent.EVENT.invoker().register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerEvent.PlayerLoggedInEvent event) {
        dev.architectury.event.events.common.PlayerEvent.PLAYER_JOIN.invoker().join((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerEvent.PlayerLoggedOutEvent event) {
        dev.architectury.event.events.common.PlayerEvent.PLAYER_QUIT.invoker().quit((ServerPlayer) event.getEntity());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerEvent.PlayerRespawnEvent event) {
        dev.architectury.event.events.common.PlayerEvent.PLAYER_RESPAWN.invoker().respawn((ServerPlayer) event.getEntity(), event.isEndConquered());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(CommandEvent event) {
        CommandPerformEvent performEvent = new CommandPerformEvent(event.getParseResults(), event.getException());
        if (CommandPerformEvent.EVENT.invoker().act(performEvent).isFalse()) {
            event.setCanceled(true);
        }
        event.setParseResults(performEvent.getResults());
        event.setException(performEvent.getThrowable());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            dev.architectury.event.events.common.TickEvent.PLAYER_PRE.invoker().tick(event.player);
        } else if (event.phase == TickEvent.Phase.END) {
            dev.architectury.event.events.common.TickEvent.PLAYER_POST.invoker().tick(event.player);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerChatEvent event) {
        class ChatComponentImpl implements ChatEvent.ChatComponent {

            @Override
            public Component get() {
                return event.getMessage();
            }

            @Override
            public void set(Component component) {
                event.setMessage(component);
            }
        }
        ChatEvent.DECORATE.invoker().decorate(event.getPlayer(), new ChatComponentImpl());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void eventAfter(ServerChatEvent event) {
        EventResult process = ChatEvent.RECEIVED.invoker().received(event.getPlayer(), event.getMessage());
        if (process.isFalse()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventWorldEvent(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getLevel();
            LifecycleEvent.SERVER_LEVEL_LOAD.invoker().act(world);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventWorldEvent(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getLevel();
            LifecycleEvent.SERVER_LEVEL_UNLOAD.invoker().act(world);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventWorldEvent(LevelEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) event.getLevel();
            LifecycleEvent.SERVER_LEVEL_SAVE.invoker().act(world);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(LivingDeathEvent event) {
        if (EntityEvent.LIVING_DEATH.invoker().die(event.getEntity(), event.getSource()).isFalse()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(AdvancementEvent.AdvancementEarnEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            dev.architectury.event.events.common.PlayerEvent.PLAYER_ADVANCEMENT.invoker().award((ServerPlayer) event.getEntity(), event.getAdvancement());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerEvent(PlayerEvent.Clone event) {
        if (event.getOriginal() instanceof ServerPlayer && event.getEntity() instanceof ServerPlayer) {
            dev.architectury.event.events.common.PlayerEvent.PLAYER_CLONE.invoker().clone((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getEntity(), !event.isWasDeath());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventExplosionEvent(ExplosionEvent.Start event) {
        if (dev.architectury.event.events.common.ExplosionEvent.PRE.invoker().explode(event.getLevel(), event.getExplosion()).isFalse()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventExplosionEvent(ExplosionEvent.Detonate event) {
        dev.architectury.event.events.common.ExplosionEvent.DETONATE.invoker().explode(event.getLevel(), event.getExplosion(), event.getAffectedEntities());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(LivingAttackEvent event) {
        if (EntityEvent.LIVING_HURT.invoker().hurt(event.getEntity(), event.getSource(), event.getAmount()).isFalse()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(EntityJoinLevelEvent event) {
        if (EntityEvent.ADD.invoker().add(event.getEntity(), event.getLevel()).isFalse()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(BlockEvent.FarmlandTrampleEvent event) {
        if (event.getLevel() instanceof Level && InteractionEvent.FARMLAND_TRAMPLE.invoker().trample((Level) event.getLevel(), event.getPos(), event.getState(), event.getFallDistance(), event.getEntity()).value() != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(FillBucketEvent event) {
        ItemStack oldItem = event.getEmptyBucket();
        CompoundEventResult<ItemStack> result = dev.architectury.event.events.common.PlayerEvent.FILL_BUCKET.invoker().fill(event.getEntity(), event.getLevel(), oldItem, event.getTarget());
        if (result.interruptsFurtherEvaluation()) {
            event.setCanceled(true);
            event.setFilledBucket(result.object());
            if (result.value() != null) {
                event.setResult(result.value() ? Result.ALLOW : Result.DENY);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventLivingSpawnEvent(MobSpawnEvent.FinalizeSpawn event) {
        EventResult result = EntityEvent.LIVING_CHECK_SPAWN.invoker().canSpawn(event.getEntity(), event.getLevel(), event.getX(), event.getY(), event.getZ(), event.getSpawnType(), event.getSpawner());
        if (result.interruptsFurtherEvaluation() && !result.isEmpty()) {
            event.setSpawnCancelled(result.value());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(AnimalTameEvent event) {
        EventResult result = EntityEvent.ANIMAL_TAME.invoker().tame(event.getAnimal(), event.getTamer());
        event.setCanceled(result.isFalse());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerEvent.ItemCraftedEvent event) {
        dev.architectury.event.events.common.PlayerEvent.CRAFT_ITEM.invoker().craft(event.getEntity(), event.getCrafting(), event.getInventory());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerEvent.ItemSmeltedEvent event) {
        dev.architectury.event.events.common.PlayerEvent.SMELT_ITEM.invoker().smelt(event.getEntity(), event.getSmelting());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(EntityItemPickupEvent event) {
        EventResult result = dev.architectury.event.events.common.PlayerEvent.PICKUP_ITEM_PRE.invoker().canPickup(event.getEntity(), event.getItem(), event.getItem().getItem());
        if (result.isFalse()) {
            event.setCanceled(true);
        } else if (result.isTrue()) {
            event.setResult(Result.ALLOW);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerEvent.ItemPickupEvent event) {
        dev.architectury.event.events.common.PlayerEvent.PICKUP_ITEM_POST.invoker().pickup(event.getEntity(), event.getOriginalEntity(), event.getStack());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ItemTossEvent event) {
        dev.architectury.event.events.common.PlayerEvent.DROP_ITEM.invoker().drop(event.getPlayer(), event.getEntity());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerContainerEvent(PlayerContainerEvent.Open event) {
        dev.architectury.event.events.common.PlayerEvent.OPEN_MENU.invoker().open(event.getEntity(), event.getContainer());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerContainerEvent(PlayerContainerEvent.Close event) {
        dev.architectury.event.events.common.PlayerEvent.CLOSE_MENU.invoker().close(event.getEntity(), event.getContainer());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.RightClickItem event) {
        CompoundEventResult<ItemStack> result = InteractionEvent.RIGHT_CLICK_ITEM.invoker().click(event.getEntity(), event.getHand());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.result().asMinecraft());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.RightClickBlock event) {
        EventResult result = InteractionEvent.RIGHT_CLICK_BLOCK.invoker().click(event.getEntity(), event.getHand(), event.getPos(), event.getFace());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.asMinecraft());
            event.setUseBlock(Result.DENY);
            event.setUseItem(Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.EntityInteract event) {
        EventResult result = InteractionEvent.INTERACT_ENTITY.invoker().interact(event.getEntity(), event.getTarget(), event.getHand());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.asMinecraft());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventPlayerInteractEvent(PlayerInteractEvent.LeftClickBlock event) {
        EventResult result = InteractionEvent.LEFT_CLICK_BLOCK.invoker().click(event.getEntity(), event.getHand(), event.getPos(), event.getFace());
        if (result.isPresent()) {
            event.setCanceled(true);
            event.setCancellationResult(result.asMinecraft());
            event.setUseBlock(Result.DENY);
            event.setUseItem(Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer && event.getLevel() instanceof Level) {
            EventResult result = dev.architectury.event.events.common.BlockEvent.BREAK.invoker().breakBlock((Level) event.getLevel(), event.getPos(), event.getState(), (ServerPlayer) event.getPlayer(), new IntValue() {

                public int getAsInt() {
                    return event.getExpToDrop();
                }

                public void accept(int value) {
                    event.setExpToDrop(value);
                }
            });
            if (result.isFalse()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel() instanceof Level) {
            EventResult result = dev.architectury.event.events.common.BlockEvent.PLACE.invoker().placeBlock((Level) event.getLevel(), event.getPos(), event.getState(), event.getEntity());
            if (result.isFalse()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(ServerAboutToStartEvent event) {
        LifecycleEvent.SERVER_BEFORE_START.invoker().stateChanged(event.getServer());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            dev.architectury.event.events.common.PlayerEvent.CHANGE_DIMENSION.invoker().change((ServerPlayer) event.getEntity(), event.getFrom(), event.getTo());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventChunkDataEvent(ChunkDataEvent.Save event) {
        if (event.getLevel() instanceof ServerLevel) {
            ChunkEvent.SAVE_DATA.invoker().save(event.getChunk(), (ServerLevel) event.getLevel(), event.getData());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void eventChunkDataEvent(ChunkDataEvent.Load event) {
        LevelAccessor level = event.getChunk().getWorldForge();
        if (!(level instanceof ServerLevel) && event instanceof EventHandlerImplCommon.LevelEventAttachment) {
            level = ((EventHandlerImplCommon.LevelEventAttachment) event).architectury$getAttachedLevel();
        }
        ChunkEvent.LOAD_DATA.invoker().load(event.getChunk(), level instanceof ServerLevel ? (ServerLevel) level : null, event.getData());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(LootTableLoadEvent event) {
        LootEvent.MODIFY_LOOT_TABLE.invoker().modifyLootTable(lootDataManagerRef == null ? null : (LootDataManager) lootDataManagerRef.get(), event.getName(), new LootTableModificationContextImpl(event.getTable()), true);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(AttackEntityEvent event) {
        EventResult result = dev.architectury.event.events.common.PlayerEvent.ATTACK_ENTITY.invoker().attack(event.getEntity(), event.getEntity().m_9236_(), event.getTarget(), event.getEntity().m_7655_(), null);
        if (result.isFalse()) {
            event.setCanceled(true);
        }
    }

    public interface LevelEventAttachment {

        LevelAccessor architectury$getAttachedLevel();

        void architectury$attachLevel(LevelAccessor var1);
    }

    public static class ModBasedEventHandler {

        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void event(FMLCommonSetupEvent event) {
            LifecycleEvent.SETUP.invoker().run();
        }
    }
}