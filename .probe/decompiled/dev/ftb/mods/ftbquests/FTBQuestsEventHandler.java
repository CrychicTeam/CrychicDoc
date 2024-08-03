package dev.ftb.mods.ftbquests;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.hooks.level.entity.PlayerHooks;
import dev.ftb.mods.ftbquests.block.FTBQuestsBlocks;
import dev.ftb.mods.ftbquests.block.entity.FTBQuestsBlockEntities;
import dev.ftb.mods.ftbquests.command.FTBQuestsCommands;
import dev.ftb.mods.ftbquests.events.ClearFileCacheEvent;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.DimensionTask;
import dev.ftb.mods.ftbquests.quest.task.KillTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.util.DeferredInventoryDetection;
import dev.ftb.mods.ftbquests.util.FTBQuestsInventoryListener;
import dev.ftb.mods.ftbteams.api.event.PlayerChangedTeamEvent;
import dev.ftb.mods.ftbteams.api.event.PlayerLoggedInAfterTeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamCreatedEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import java.util.List;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public enum FTBQuestsEventHandler {

    INSTANCE;

    private List<KillTask> killTasks = null;

    private List<Task> autoSubmitTasks = null;

    void init() {
        LifecycleEvent.SERVER_BEFORE_START.register(this::serverAboutToStart);
        CommandRegistrationEvent.EVENT.register(this::registerCommands);
        LifecycleEvent.SERVER_STARTED.register(this::serverStarted);
        LifecycleEvent.SERVER_STOPPING.register(this::serverStopped);
        LifecycleEvent.SERVER_LEVEL_SAVE.register(this::worldSaved);
        FTBQuestsBlocks.register();
        FTBQuestsItems.register();
        FTBQuestsBlockEntities.register();
        ClearFileCacheEvent.EVENT.register(this::fileCacheClear);
        TeamEvent.PLAYER_LOGGED_IN.register(this::playerLoggedIn);
        TeamEvent.CREATED.register(this::teamCreated);
        TeamEvent.PLAYER_CHANGED.register(this::playerChangedTeam);
        EntityEvent.LIVING_DEATH.register(this::playerKill);
        TickEvent.PLAYER_POST.register(this::playerTick);
        PlayerEvent.CRAFT_ITEM.register(this::itemCrafted);
        PlayerEvent.SMELT_ITEM.register(this::itemSmelted);
        PlayerEvent.PLAYER_CLONE.register(this::cloned);
        PlayerEvent.CHANGE_DIMENSION.register(this::changedDimension);
        PlayerEvent.OPEN_MENU.register(this::containerOpened);
        TickEvent.SERVER_POST.register(DeferredInventoryDetection::tick);
    }

    private void serverAboutToStart(MinecraftServer server) {
        ServerQuestFile.INSTANCE = new ServerQuestFile(server);
    }

    private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx, Commands.CommandSelection selection) {
        FTBQuestsCommands.register(dispatcher);
    }

    private void serverStarted(MinecraftServer server) {
        ServerQuestFile.INSTANCE.load();
    }

    private void serverStopped(MinecraftServer server) {
        ServerQuestFile.INSTANCE.saveNow();
        ServerQuestFile.INSTANCE.unload();
        ServerQuestFile.INSTANCE = null;
    }

    private void worldSaved(ServerLevel level) {
        if (ServerQuestFile.INSTANCE != null) {
            ServerQuestFile.INSTANCE.saveNow();
        }
    }

    private void fileCacheClear(BaseQuestFile file) {
        if (file.isServerSide()) {
            this.killTasks = null;
            this.autoSubmitTasks = null;
        }
    }

    private void playerLoggedIn(PlayerLoggedInAfterTeamEvent event) {
        ServerQuestFile.INSTANCE.playerLoggedIn(event);
    }

    private void teamCreated(TeamCreatedEvent event) {
        ServerQuestFile.INSTANCE.teamCreated(event);
    }

    private void playerChangedTeam(PlayerChangedTeamEvent event) {
        ServerQuestFile.INSTANCE.playerChangedTeam(event);
    }

    private EventResult playerKill(LivingEntity entity, DamageSource source) {
        if (source.getEntity() instanceof ServerPlayer player && !PlayerHooks.isFake(player)) {
            if (this.killTasks == null) {
                this.killTasks = ServerQuestFile.INSTANCE.collect(KillTask.class);
            }
            if (this.killTasks.isEmpty()) {
                return EventResult.pass();
            }
            TeamData data = ServerQuestFile.INSTANCE.getOrCreateTeamData(player);
            for (KillTask task : this.killTasks) {
                if (data.getProgress(task) < task.getMaxProgress() && data.canStartTasks(task.getQuest())) {
                    task.kill(data, entity);
                }
            }
        }
        return EventResult.pass();
    }

    private void playerTick(Player player) {
        ServerQuestFile file = ServerQuestFile.INSTANCE;
        if (player instanceof ServerPlayer && file != null && !PlayerHooks.isFake(player)) {
            if (this.autoSubmitTasks == null) {
                this.autoSubmitTasks = file.collect(o -> o instanceof Task && ((Task) o).autoSubmitOnPlayerTick() > 0);
            }
            if (this.autoSubmitTasks == null || this.autoSubmitTasks.isEmpty()) {
                return;
            }
            TeamData data = file.getOrCreateTeamData(player);
            if (data.isLocked()) {
                return;
            }
            long t = player.m_9236_().getGameTime();
            file.withPlayerContext((ServerPlayer) player, () -> {
                for (Task task : this.autoSubmitTasks) {
                    long d = (long) task.autoSubmitOnPlayerTick();
                    if (d > 0L && t % d == 0L && !data.isCompleted(task) && data.canStartTasks(task.getQuest())) {
                        task.submitTask(data, (ServerPlayer) player);
                    }
                }
            });
        }
    }

    private void itemCrafted(Player player, ItemStack crafted, Container inventory) {
        if (player instanceof ServerPlayer && !crafted.isEmpty()) {
            FTBQuestsInventoryListener.detect((ServerPlayer) player, crafted, 0L);
        }
    }

    private void itemSmelted(Player player, ItemStack smelted) {
        if (player instanceof ServerPlayer && !smelted.isEmpty()) {
            FTBQuestsInventoryListener.detect((ServerPlayer) player, smelted, 0L);
        }
    }

    private void cloned(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean wonGame) {
        newPlayer.f_36095_.m_38893_(new FTBQuestsInventoryListener(newPlayer));
        if (!wonGame) {
            if (!PlayerHooks.isFake(newPlayer) && !newPlayer.m_9236_().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                for (int i = 0; i < oldPlayer.m_150109_().items.size(); i++) {
                    ItemStack stack = oldPlayer.m_150109_().items.get(i);
                    if (stack.getItem() == FTBQuestsItems.BOOK.get() && newPlayer.m_36356_(stack)) {
                        oldPlayer.m_150109_().items.set(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    private void changedDimension(ServerPlayer player, ResourceKey<Level> oldLevel, ResourceKey<Level> newLevel) {
        if (!PlayerHooks.isFake(player)) {
            ServerQuestFile file = ServerQuestFile.INSTANCE;
            TeamData data = file.getOrCreateTeamData(player);
            if (data.isLocked()) {
                return;
            }
            file.withPlayerContext(player, () -> {
                for (DimensionTask task : file.collect(DimensionTask.class)) {
                    if (data.canStartTasks(task.getQuest())) {
                        task.submitTask(data, player);
                    }
                }
            });
        }
    }

    private void containerOpened(Player player, AbstractContainerMenu menu) {
        if (player instanceof ServerPlayer && !PlayerHooks.isFake(player) && !(menu instanceof InventoryMenu)) {
            menu.addSlotListener(new FTBQuestsInventoryListener((ServerPlayer) player));
        }
    }
}