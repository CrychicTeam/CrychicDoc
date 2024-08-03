package journeymap.common;

import com.google.common.base.Joiner;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Function;
import journeymap.common.network.data.model.Location;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.minecraftforge.versions.mcp.MCPVersion;

public class LoaderHooks {

    public static boolean isDedicatedServer() {
        return getServer().isDedicatedServer();
    }

    public static MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    public static ArrayList<String> getMods() {
        ArrayList<String> list = new ArrayList();
        for (IModInfo mod : ModList.get().getMods()) {
            if (ModList.get().isLoaded(mod.getModId())) {
                list.add(String.format("%s:%s", mod.getDisplayName(), mod.getVersion()));
            }
        }
        return list;
    }

    public static String getModNames() {
        return Joiner.on(", ").join(getMods());
    }

    public static String getMCVersion() {
        return MCPVersion.getMCVersion();
    }

    public static String getLoaderVersion() {
        return ForgeVersion.getVersion();
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId) || ModList.get().isLoaded(modId.toLowerCase());
    }

    public static URL getModFileLocation(String domain) throws Exception {
        URL fileLocation = null;
        ModContainer mod = (ModContainer) ModList.get().getModContainerById(domain).orElse(null);
        if (mod == null) {
            for (IModInfo modEntry : ModList.get().getMods()) {
                if (modEntry.getModId().toLowerCase().equals(domain)) {
                    mod = (ModContainer) ModList.get().getModContainerById(domain).orElse(null);
                    break;
                }
            }
        }
        if (mod != null) {
            fileLocation = ModList.get().getModFileById(mod.getModId()).getFile().getFilePath().toUri().toURL();
        }
        return fileLocation;
    }

    public static boolean isClient() {
        return FMLLoader.getDist().isClient();
    }

    public static void doTeleport(ServerPlayer player, ServerLevel destinationWorld, final Location location) {
        player.changeDimension(destinationWorld, new ITeleporter() {

            @Override
            public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                Entity newPosEntity = (Entity) repositionEntity.apply(false);
                newPosEntity.teleportTo(location.getX(), location.getY() + 1.0, location.getZ());
                return newPosEntity;
            }
        });
    }
}