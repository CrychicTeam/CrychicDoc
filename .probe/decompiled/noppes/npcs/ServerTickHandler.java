package noppes.npcs;

import java.util.ArrayList;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.controllers.SyncController;
import noppes.npcs.controllers.VisibilityController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.entity.data.DataScenes;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketSync;
import noppes.npcs.shared.client.util.AnalyticsTracking;

public class ServerTickHandler {

    public int ticks = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            PlayerData data = PlayerData.get(player);
            if (player.m_20193_().getDayTime() % 24000L == 1L || player.m_20193_().getDayTime() % 240000L == 12001L) {
                VisibilityController.instance.onUpdate((ServerPlayer) player);
            }
            if (data.updateClient) {
                Packets.send((ServerPlayer) player, new PacketSync(8, data.getSyncNBT(), true));
                VisibilityController.instance.onUpdate((ServerPlayer) player);
                data.updateClient = false;
            }
            if (data.prevHeldItem != player.m_21205_() && (data.prevHeldItem.getItem() == CustomItems.wand || player.m_21205_().getItem() == CustomItems.wand)) {
                VisibilityController.instance.onUpdate((ServerPlayer) player);
            }
            data.prevHeldItem = player.m_21205_();
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.LevelTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            NPCSpawning.findChunksForSpawning((ServerLevel) event.level);
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START && this.ticks++ >= 20) {
            SchematicController.Instance.updateBuilding();
            MassBlockController.Update();
            this.ticks = 0;
            for (DataScenes.SceneState state : DataScenes.StartedScenes.values()) {
                if (!state.paused) {
                    state.ticks++;
                }
            }
            for (DataScenes.SceneContainer entry : DataScenes.ScenesToRun) {
                entry.update();
            }
            DataScenes.ScenesToRun = new ArrayList();
        }
    }

    @SubscribeEvent
    public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        final ServerPlayer player = (ServerPlayer) event.getEntity();
        MinecraftServer server = event.getEntity().m_20194_();
        for (ServerLevel level : server.getAllLevels()) {
            ServerScoreboard board = level.getScoreboard();
            for (String objective : Availability.scores) {
                Objective so = board.m_83477_(objective);
                if (so != null) {
                    if (board.getObjectiveDisplaySlotCount(so) == 0) {
                        player.connection.send(new ClientboundSetObjectivePacket(so, 0));
                    }
                    Score sco = board.m_83471_(player.m_6302_(), so);
                    player.connection.send(new ClientboundSetScorePacket(ServerScoreboard.Method.CHANGE, so.getName(), sco.getOwner(), sco.getScore()));
                }
            }
        }
        player.f_36095_.m_38893_(new ContainerListener() {

            @Override
            public void slotChanged(AbstractContainerMenu container, int slotInd, ItemStack stack) {
                if (!player.m_9236_().isClientSide) {
                    PlayerQuestData playerdata = PlayerData.get(player).questData;
                    playerdata.checkQuestCompletion(player, 0);
                }
            }

            @Override
            public void dataChanged(AbstractContainerMenu container, int varToUpdate, int newValue) {
            }
        });
        PlayerData data = PlayerData.get(event.getEntity());
        String serverName = "local";
        if (server.isDedicatedServer()) {
            serverName = "server";
        } else if (server.isPublished()) {
            serverName = "lan";
        }
        AnalyticsTracking.sendData(data.iAmStealingYourDatas, "join", serverName);
        SyncController.syncPlayer((ServerPlayer) event.getEntity());
    }
}