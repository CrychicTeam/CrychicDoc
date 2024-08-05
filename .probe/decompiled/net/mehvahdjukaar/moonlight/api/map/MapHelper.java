package net.mehvahdjukaar.moonlight.api.map;

import java.util.Arrays;
import net.mehvahdjukaar.moonlight.api.integration.MapAtlasCompat;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.core.CompatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;

public class MapHelper {

    @Nullable
    public static MapItemSavedData getMapData(ItemStack stack, Level level, @Nullable Player player) {
        MapItemSavedData data = MapItem.getSavedData(stack, level);
        if (data == null && CompatHandler.MAP_ATLASES && player != null) {
            data = MapAtlasCompat.getSavedDataFromAtlas(stack, level, player);
        }
        return data;
    }

    @Deprecated(forRemoval = true)
    public static Integer getMapId(ItemStack stack, Player player, Object data) {
        Integer i = MapItem.getMapId(stack);
        if (i == null && CompatHandler.MAP_ATLASES) {
            i = MapAtlasCompat.getMapIdFromAtlas(stack, player.m_9236_(), data);
        }
        return i;
    }

    public static void addVanillaDecorations(ItemStack stack, BlockPos pos, MapDecoration.Type type, int mapColor) {
        MapItemSavedData.addTargetDecoration(stack, pos, "+", type);
        if (mapColor != 0) {
            CompoundTag com = stack.getOrCreateTagElement("display");
            com.putInt("MapColor", mapColor);
        }
    }

    public static void addDecorationToMap(ItemStack stack, BlockPos pos, MapDecorationType<?, ?> type, int mapColor) {
        ListTag tags;
        if (stack.hasTag() && stack.getTag().contains("CustomDecorations", 9)) {
            tags = stack.getTag().getList("CustomDecorations", 10);
        } else {
            tags = new ListTag();
            stack.addTagElement("CustomDecorations", tags);
        }
        CompoundTag tag = new CompoundTag();
        tag.putString("type", Utils.getID(type).toString());
        tag.putInt("x", pos.m_123341_());
        tag.putInt("z", pos.m_123343_());
        tags.add(tag);
        if (mapColor != 0) {
            CompoundTag com = stack.getOrCreateTagElement("display");
            com.putInt("MapColor", mapColor);
        }
    }

    public static void addDecorationToMap(ItemStack stack, BlockPos pos, ResourceLocation id, int mapColor) {
        if (id.getNamespace().equals("minecraft")) {
            MapDecoration.Type type = getVanillaType(id);
            if (type != null) {
                addVanillaDecorations(stack, pos, type, mapColor);
                return;
            }
        }
        MapDecorationType<?, ?> type = MapDataRegistry.get(id);
        if (type != null) {
            addDecorationToMap(stack, pos, type, mapColor);
        } else {
            addVanillaDecorations(stack, pos, MapDecoration.Type.TARGET_X, mapColor);
        }
    }

    @Nullable
    private static MapDecoration.Type getVanillaType(ResourceLocation id) {
        return (MapDecoration.Type) Arrays.stream(MapDecoration.Type.values()).filter(t -> t.toString().toLowerCase().equals(id.getPath())).findFirst().orElse(null);
    }

    public static boolean toggleMarkersAtPos(Level level, BlockPos pos, ItemStack stack, @Nullable Player player) {
        return getMapData(stack, level, player) instanceof ExpandedMapData expandedMapData ? expandedMapData.toggleCustomDecoration(level, pos) : false;
    }

    public static boolean removeAllCustomMarkers(Level level, ItemStack stack, @Nullable Player player) {
        if (getMapData(stack, level, player) instanceof ExpandedMapData expandedMapData && !level.isClientSide) {
            expandedMapData.resetCustomDecoration();
            return true;
        }
        return false;
    }

    public static boolean addSimpleDecorationToMap(MapItemSavedData data, Level level, ResourceLocation id, BlockPos pos, @Nullable Component name) {
        MapDecorationType<?, ?> type = MapDataRegistry.get(id);
        if (type != null) {
            MapBlockMarker<?> marker = type.createEmptyMarker();
            marker.setPersistent(true);
            marker.setPos(pos);
            marker.setName(name);
            ((ExpandedMapData) data).addCustomMarker(marker);
            return true;
        } else {
            return false;
        }
    }
}