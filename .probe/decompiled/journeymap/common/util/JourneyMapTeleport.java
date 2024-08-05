package journeymap.common.util;

import com.mojang.authlib.GameProfile;
import journeymap.client.Constants;
import journeymap.common.Journeymap;
import journeymap.common.LoaderHooks;
import journeymap.common.helper.DimensionHelper;
import journeymap.common.network.data.model.Location;
import journeymap.common.properties.DimensionProperties;
import journeymap.common.properties.PropertiesManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class JourneyMapTeleport {

    private static final JourneyMapTeleport INSTANCE = new JourneyMapTeleport();

    private JourneyMapTeleport() {
    }

    public static JourneyMapTeleport instance() {
        return INSTANCE;
    }

    public boolean attemptTeleport(Entity entity, Location location) {
        MinecraftServer mcServer = LoaderHooks.getServer();
        boolean creative = false;
        boolean cheatMode = false;
        if (entity == null) {
            Journeymap.getLogger().error("Attempted to teleport null entity.");
            return false;
        } else if (entity instanceof ServerPlayer) {
            creative = ((ServerPlayer) entity).m_150110_().instabuild;
            cheatMode = mcServer.getPlayerList().isOp(new GameProfile(entity.getUUID(), entity.getName().getString()));
            if (mcServer == null) {
                entity.sendSystemMessage(Constants.getStringTextComponent("Cannot Find World"));
                return false;
            } else if (!this.isTeleportAvailable(entity, location) && !creative && !cheatMode && !Journeymap.isOp((ServerPlayer) entity)) {
                entity.sendSystemMessage(Constants.getStringTextComponent("Server has disabled JourneyMap teleport usage for your current or destination dimension."));
                return false;
            } else if (!entity.isAlive()) {
                entity.sendSystemMessage(Constants.getStringTextComponent("Cannot teleport when dead."));
                return false;
            } else {
                Level destinationWorld = mcServer.getLevel(DimensionHelper.getWorldKeyForName(location.getDim()));
                if (destinationWorld == null) {
                    for (ServerLevel world : mcServer.getAllLevels()) {
                        if (location.getDim().equalsIgnoreCase(DimensionHelper.getDimName(world)) || location.getDim().equalsIgnoreCase(DimensionHelper.getDimKeyName(world.m_46472_()))) {
                            destinationWorld = world;
                            break;
                        }
                    }
                }
                if (destinationWorld == null) {
                    entity.sendSystemMessage(Constants.getStringTextComponent("Could not get world for Dimension " + location.getDim()));
                    return false;
                } else {
                    return this.teleportEntity((ServerLevel) destinationWorld, entity, location);
                }
            }
        } else {
            return false;
        }
    }

    private boolean isTeleportAvailable(Entity entity, Location location) {
        DimensionProperties destinationProperty = PropertiesManager.getInstance().getDimProperties(DimensionHelper.getWorldKeyForName(location.getDim()));
        DimensionProperties entityLocationProperty = PropertiesManager.getInstance().getDimProperties(DimensionHelper.getDimension(entity));
        return this.canDimTeleport(destinationProperty) && this.canDimTeleport(entityLocationProperty);
    }

    private boolean canDimTeleport(DimensionProperties properties) {
        return properties.enabled.get() ? properties.teleportEnabled.get() : PropertiesManager.getInstance().getGlobalProperties().teleportEnabled.get();
    }

    private boolean teleportEntity(ServerLevel destinationWorld, Entity player, Location location) {
        ServerLevel startWorld = (ServerLevel) player.level();
        boolean changedWorld = startWorld != destinationWorld;
        if (player instanceof ServerPlayer) {
            if (changedWorld) {
                LoaderHooks.doTeleport((ServerPlayer) player, destinationWorld, location);
            } else {
                player.teleportTo(location.getX(), location.getY(), location.getZ());
            }
        }
        return false;
    }
}