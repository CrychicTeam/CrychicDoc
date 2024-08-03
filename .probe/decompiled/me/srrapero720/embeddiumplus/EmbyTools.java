package me.srrapero720.embeddiumplus;

import it.unimi.dsi.fastutil.Pair;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class EmbyTools {

    private static final Marker IT = MarkerManager.getMarker("Tools");

    public static Pair<String, String> resourceLocationPair(String res) {
        String[] r = res.split(":");
        Pair var2;
        try {
            var2 = Pair.of(r[0], r[1]);
        } finally {
            r[0] = null;
            r[1] = null;
        }
        return var2;
    }

    public static ChatFormatting colorByLow(int usage) {
        return usage < 9 ? ChatFormatting.DARK_RED : (usage < 16 ? ChatFormatting.RED : (usage < 30 ? ChatFormatting.GOLD : ChatFormatting.RESET));
    }

    public static ChatFormatting colorByPercent(int usage) {
        return usage >= 100 ? ChatFormatting.DARK_RED : (usage >= 90 ? ChatFormatting.RED : (usage >= 75 ? ChatFormatting.GOLD : ChatFormatting.RESET));
    }

    public static String tintByLower(int usage) {
        return (usage < 9 ? ChatFormatting.DARK_RED : (usage < 16 ? ChatFormatting.RED : (usage < 30 ? ChatFormatting.GOLD : ChatFormatting.RESET))).toString() + usage;
    }

    public static String tintByPercent(long usage) {
        return (usage >= 100L ? ChatFormatting.DARK_RED : (usage >= 90L ? ChatFormatting.RED : (usage >= 75L ? ChatFormatting.GOLD : ChatFormatting.RESET))).toString() + usage;
    }

    public static boolean isWhitelisted(ResourceLocation entityOrTile, ForgeConfigSpec.ConfigValue<List<? extends String>> configValue) {
        for (String item : configValue.get()) {
            Pair<String, String> resLoc = resourceLocationPair(item);
            if (((String) resLoc.key()).equals(entityOrTile.getNamespace()) && (((String) resLoc.value()).equals("*") || ((String) resLoc.value()).equals(entityOrTile.getPath()))) {
                return true;
            }
        }
        return false;
    }

    public static int getColorARGB(int a, int r, int g, int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static long ramUsed() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public static long bytesToMB(long input) {
        return input / 1024L / 1024L;
    }

    public static boolean isModInstalled(String modid) {
        return FMLLoader.getLoadingModList().getModFileById(modid) != null;
    }

    public static boolean isEntityInRange(BlockPos pos, Vec3 cam, int maxHeight, int maxDistanceSqr) {
        return isEntityInRange(pos.getCenter(), cam, maxHeight, maxDistanceSqr);
    }

    public static boolean isEntityInRange(Entity entity, double camX, double camY, double camZ, int maxHeight, int maxDistanceSqr) {
        return isEntityInRange(entity.position(), new Vec3(camX, camY, camZ), maxHeight, maxDistanceSqr);
    }

    public static boolean isEntityInRange(Vec3 position, Vec3 camera, int maxHeight, int maxDistanceSqr) {
        if (Math.abs(position.y - camera.y - 4.0) < (double) maxHeight) {
            double x = position.x - camera.x;
            double z = position.z - camera.z;
            return x * x + z * z < (double) maxDistanceSqr;
        } else {
            return false;
        }
    }
}