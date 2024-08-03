package journeymap.common.events.forge;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import journeymap.common.LoaderHooks;
import journeymap.common.command.CreateWaypoint;
import journeymap.common.events.ServerEventHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.ServerOpList;
import net.minecraft.server.players.ServerOpListEntry;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.PermissionsChangedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeServerEvents {

    private final ServerEventHandler handler = new ServerEventHandler();

    @SubscribeEvent
    public void onPermissionChangedEvent(PermissionsChangedEvent event) {
        if (event.getEntity() instanceof ServerPlayer && event.getNewLevel() != event.getOldLevel()) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            MinecraftServer server = player.server;
            GameProfile profile = player.m_36316_();
            ServerOpList ops = server.getPlayerList().getOps();
            if (event.getNewLevel() >= server.getOperatorUserPermissionLevel()) {
                ops.m_11381_(new ServerOpListEntry(profile, server.getOperatorUserPermissionLevel(), ops.canBypassPlayerLimit(profile)));
            } else if (event.getNewLevel() == 0) {
                ops.m_11393_(profile);
            }
            this.handler.sendConfigsToPlayer(player);
        }
    }

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || LoaderHooks.getServer() == null || LoaderHooks.getServer().getPlayerList().getPlayers().size() >= 1) {
            Level world = LoaderHooks.getServer().getLevel(Level.OVERWORLD);
            this.handler.onServerTickEvent(world);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            this.handler.onEntityJoinWorldEvent(event.getEntity());
        }
    }

    @SubscribeEvent
    public void onEntityLeaveWorldEvent(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getLevel() instanceof ServerLevel level) {
            this.handler.unloadPlayer(player, level);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            this.handler.onPlayerLoggedInEvent(event.getEntity());
        }
    }

    @SubscribeEvent
    public void registerCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CreateWaypoint.register(dispatcher);
    }
}