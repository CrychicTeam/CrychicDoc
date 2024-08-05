package journeymap.client.command;

import com.mojang.authlib.GameProfile;
import java.util.TreeSet;
import journeymap.client.JourneymapClient;
import journeymap.client.waypoint.Waypoint;
import journeymap.common.Journeymap;
import journeymap.common.helper.DimensionHelper;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;

public class CmdTeleportWaypoint {

    final Minecraft mc = Minecraft.getInstance();

    final Waypoint waypoint;

    public CmdTeleportWaypoint(Waypoint waypoint) {
        this.waypoint = waypoint;
    }

    public static boolean isPermitted(Minecraft mc) {
        if (mc.getSingleplayerServer() != null) {
            IntegratedServer mcServer = mc.getSingleplayerServer();
            PlayerList configurationManager = null;
            GameProfile profile = null;
            try {
                profile = new GameProfile(mc.player.m_20148_(), mc.player.m_7755_().getString());
                configurationManager = mcServer.m_6846_();
                Journeymap.getLogger().debug("integrated server not null, can send commands: " + configurationManager.isOp(profile) + " is tp enabled: " + JourneymapClient.getInstance().getStateHandler().isTeleportEnabled());
                return configurationManager.isOp(profile) || JourneymapClient.getInstance().getStateHandler().isTeleportEnabled();
            } catch (Exception var7) {
                Exception e = var7;
                var7.printStackTrace();
                try {
                    if (profile != null && configurationManager != null) {
                        Journeymap.getLogger().debug("Some Error happened: " + mcServer.m_129792_() + " : " + ((ServerLevelData) mcServer.m_129880_(mc.player.m_9236_().dimension()).m_6106_()).getAllowCommands() + " : " + mcServer.m_236731_().getName().equalsIgnoreCase(profile.getName()));
                        return mcServer.m_129792_() && ((ServerLevelData) mcServer.m_129880_(mc.player.m_9236_().dimension()).m_6106_()).getAllowCommands() && mcServer.m_236731_().getName().equalsIgnoreCase(profile.getName());
                    }
                    Journeymap.getLogger().warn("Failed to check teleport permission both ways: " + LogFormatter.toString(e) + ", and profile or configManager were null.");
                } catch (Exception var6) {
                    Journeymap.getLogger().warn("Failed to check teleport permission. Both ways failed: " + LogFormatter.toString(var7) + ", and " + LogFormatter.toString(var6));
                }
            }
        }
        if (JourneymapClient.getInstance().getStateHandler().isJourneyMapServerConnection()) {
            Journeymap.getLogger().debug("On a server with JM returning: " + JourneymapClient.getInstance().getStateHandler().isTeleportEnabled());
            return JourneymapClient.getInstance().getStateHandler().isTeleportEnabled();
        } else {
            Journeymap.getLogger().debug("On a server without JM returning true by default");
            return true;
        }
    }

    public void run() {
        double x = this.waypoint.getRawCenterX();
        double z = this.waypoint.getRawCenterZ();
        TreeSet<String> dims = (TreeSet<String>) this.waypoint.getDimensions();
        String dim = this.mc.player.m_9236_().dimension().location().toString();
        if (!dims.isEmpty()) {
            dim = (String) dims.stream().filter(d -> this.mc.player.m_9236_().dimension().equals(DimensionHelper.getWorldKeyForName(d))).findFirst().orElse((String) dims.first());
        }
        if (Level.NETHER.equals(DimensionHelper.getWorldKeyForName(dim)) && Level.NETHER.equals(this.mc.player.m_9236_().dimension())) {
            x = this.waypoint.getBlockCenteredX();
            z = this.waypoint.getBlockCenteredZ();
        }
        if (Level.NETHER.equals(DimensionHelper.getWorldKeyForName(dim)) && !Level.NETHER.equals(this.mc.player.m_9236_().dimension())) {
            x = (double) ((int) x >> 3) + (x > 0.0 ? 0.5 : -0.5);
            z = (double) ((int) z >> 3) + (z > 0.0 ? 0.5 : -0.5);
        }
        teleport(x, this.waypoint.getY(), z, dim);
    }

    public static void teleport(BlockPos pos, ResourceKey<Level> dim) {
        teleport((double) pos.m_123341_(), pos.m_123342_(), (double) pos.m_123343_(), dim != null ? dim.location().toString() : null);
    }

    public static void teleport(double x, int y, double z, String dim) {
        Minecraft mc = Minecraft.getInstance();
        if (!JourneymapClient.getInstance().getStateHandler().isJourneyMapServerConnection() && !Minecraft.getInstance().hasSingleplayerServer()) {
            String teleportCommand = JourneymapClient.getInstance().getWaypointProperties().teleportCommand.getAsString();
            teleportCommand = teleportCommand.replace("{name}", mc.player.m_7755_().getString()).replace("{x}", String.valueOf(x)).replace("{y}", String.valueOf(y)).replace("{z}", String.valueOf(z)).replace("{dim}", dim);
            if (teleportCommand.startsWith("/")) {
                Journeymap.getLogger().debug("Sending slash tp command: {}", teleportCommand);
                mc.player.connection.sendCommand(teleportCommand.substring(1));
            } else {
                Journeymap.getLogger().debug("Sending non-slash tp command: {}", teleportCommand);
                mc.player.connection.sendChat(teleportCommand);
            }
        } else {
            JourneymapClient.getInstance().getDispatcher().sendTeleportPacket(x, y, z, dim);
        }
    }
}