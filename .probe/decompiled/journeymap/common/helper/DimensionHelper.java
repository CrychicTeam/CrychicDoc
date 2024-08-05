package journeymap.common.helper;

import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import journeymap.common.LoaderHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionHelper {

    public static String getDimName(Entity entity) {
        return getDimName(entity.level());
    }

    public static String getDimName(@Nullable Level world) {
        return world != null ? getDimName(world.dimension()) : "overworld";
    }

    public static String getDimName(ResourceKey<Level> dimensionKey) {
        return dimensionKey.location().getPath();
    }

    public static String getDimName(String key) {
        return key.split(":")[1];
    }

    public static String getDimKeyName(Entity entity) {
        return getDimKeyName(entity.level());
    }

    public static String getDimKeyName(@Nullable Level world) {
        return world != null ? getDimKeyName(world.dimension()) : "minecraft:overworld";
    }

    public static String getDimKeyName(ResourceKey<Level> dimensionKey) {
        return dimensionKey.location().toString();
    }

    public static ResourceKey<Level> getDimension(Entity entity) {
        return getDimension(entity.level());
    }

    public static ResourceKey<Level> getDimension(Level world) {
        return world.dimension();
    }

    public static DimensionType getDimTypeForName(String dimName) {
        return (DimensionType) getDimTypeMap().get(dimName);
    }

    public static DimensionType getDimTypeForKey(ResourceKey<Level> dimKey) {
        return (DimensionType) getDimTypeMap().get(getDimName(dimKey));
    }

    public static String getSafeDimName(ResourceKey<Level> dim) {
        return dim.location().toString().replaceAll(":", "~");
    }

    public static ResourceLocation getDimResource(String dimName) {
        return new ResourceLocation(dimName);
    }

    public static boolean isNetherWorld(Level world) {
        return world.dimension().equals(Level.NETHER);
    }

    public static boolean isOverworldWorld(Level world) {
        return world.dimension().equals(Level.OVERWORLD);
    }

    public static boolean isEndWorld(Level world) {
        return world.dimension().equals(Level.END);
    }

    public static ResourceKey<Level> getWorldKeyForName(String dimName) {
        return ResourceKey.create(Registries.DIMENSION, getDimResource(dimName));
    }

    public static Map<String, DimensionType> getDimTypeMap() {
        return (Map<String, DimensionType>) StreamSupport.stream(Spliterators.spliteratorUnknownSize(LoaderHooks.getServer().getAllLevels().iterator(), 16), false).collect(Collectors.toMap(DimensionHelper::getDimKeyName, Level::m_6042_));
    }

    public static List<ResourceKey<Level>> getServerDimNameList() {
        return (List<ResourceKey<Level>>) StreamSupport.stream(Spliterators.spliteratorUnknownSize(LoaderHooks.getServer().getAllLevels().iterator(), 16), false).map(Level::m_46472_).collect(Collectors.toList());
    }

    public static Set<ResourceKey<Level>> getClientDimList() {
        return Minecraft.getInstance().player.connection.levels();
    }
}