package net.minecraftforge.common;

import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.loot.LootModifierManager;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.command.ConfigCommand;
import net.minecraftforge.server.command.ForgeCommand;

public class ForgeInternalHandler {

    private static LootModifierManager INSTANCE;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity.getClass().equals(ItemEntity.class)) {
            ItemStack stack = ((ItemEntity) entity).getItem();
            Item item = stack.getItem();
            if (item.hasCustomEntity(stack)) {
                Entity newEntity = item.createEntity(event.getLevel(), entity, stack);
                if (newEntity != null) {
                    entity.discard();
                    event.setCanceled(true);
                    BlockableEventLoop<? super TickTask> executor = LogicalSidedProvider.WORKQUEUE.get(event.getLevel().isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
                    executor.tell(new TickTask(0, () -> event.getLevel().m_7967_(newEntity)));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDimensionUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel) {
            FakePlayerFactory.unloadLevel((ServerLevel) event.getLevel());
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        WorldWorkerManager.tick(event.phase == TickEvent.Phase.START);
    }

    @SubscribeEvent
    public void checkSettings(TickEvent.ClientTickEvent event) {
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (!event.getLevel().m_5776_()) {
            FarmlandWaterManager.removeTickets(event.getChunk());
        }
    }

    @SubscribeEvent
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        UsernameCache.setUsername(event.getEntity().m_20148_(), event.getEntity().getGameProfile().getName());
    }

    @SubscribeEvent
    public void tagsUpdated(TagsUpdatedEvent event) {
        if (event.shouldUpdateStaticData()) {
            ForgeHooks.updateBurns();
        }
    }

    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event) {
        new ForgeCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onResourceReload(AddReloadListenerEvent event) {
        INSTANCE = new LootModifierManager();
        event.addListener(INSTANCE);
    }

    static LootModifierManager getLootModifierManager() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Can not retrieve LootModifierManager until resources have loaded once.");
        } else {
            return INSTANCE;
        }
    }

    @SubscribeEvent
    public void resourceReloadListeners(AddReloadListenerEvent event) {
        event.addListener(TierSortingRegistry.getReloadListener());
        event.addListener(CreativeModeTabRegistry.getReloadListener());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void builtinMobSpawnBlocker(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Mob mob && mob.isSpawnCancelled()) {
            event.setCanceled(true);
        }
    }
}