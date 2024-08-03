package net.mehvahdjukaar.supplementaries.common.entities.trades;

import net.mehvahdjukaar.moonlight.api.map.MapDataRegistry;
import net.mehvahdjukaar.moonlight.api.map.MapHelper;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.worldgen.StructureLocator;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdventurerMapsHandler {

    public static final int SEARCH_RADIUS = 150;

    public static ItemStack createMapOrQuill(ServerLevel serverLevel, BlockPos pos, @Nullable HolderSet<Structure> targets, int radius, boolean skipKnown, int zoom, @Nullable ResourceLocation mapMarker, @Nullable String name, int color) {
        if (!serverLevel.getServer().getWorldData().worldGenOptions().generateStructures()) {
            return ItemStack.EMPTY;
        } else {
            if (targets == null) {
                targets = (HolderSet<Structure>) serverLevel.m_9598_().registryOrThrow(Registries.STRUCTURE).getTag(ModTags.ADVENTURE_MAP_DESTINATIONS).orElse(null);
            }
            if (targets == null || targets.size() < 1) {
                return ItemStack.EMPTY;
            } else if (CompatHandler.QUARK && (Boolean) CommonConfigs.Tweaks.QUARK_QUILL.get()) {
                ItemStack item = QuarkCompat.makeAdventurerQuill(serverLevel, targets, radius, skipKnown, zoom, null, name, color);
                item.setHoverName(Component.translatable(name));
                return item;
            } else {
                StructureLocator.LocatedStruct found = StructureLocator.findNearestRandomMapFeature(serverLevel, targets, pos, radius, skipKnown);
                if (found != null) {
                    BlockPos toPos = found.pos();
                    return createStructureMap(serverLevel, toPos, found.structure(), zoom, mapMarker, name, color);
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }
    }

    @NotNull
    public static ItemStack createStructureMap(ServerLevel level, BlockPos pos, Holder<Structure> structure, int zoom, @Nullable ResourceLocation decoration, @Nullable String name, int color) {
        ItemStack stack = MapItem.create(level, pos.m_123341_(), pos.m_123343_(), (byte) zoom, true, true);
        MapItem.renderBiomePreviewMap(level, stack);
        if (decoration == null) {
            MapDecorationType<?, ? extends MapBlockMarker<?>> type = (MapDecorationType<?, ? extends MapBlockMarker<?>>) MapDataRegistry.getAssociatedType(structure);
            decoration = Utils.getID(type);
            if (color == 0) {
                color = type.getDefaultMapColor();
            }
        }
        MapHelper.addDecorationToMap(stack, pos, decoration, color);
        if (name != null) {
            stack.setHoverName(Component.translatable(name));
        }
        return stack;
    }

    public static ItemStack createCustomMapForTrade(Level level, BlockPos pos, HolderSet<Structure> destinations, @Nullable String mapName, int mapColor, @Nullable ResourceLocation mapMarker) {
        return level instanceof ServerLevel serverLevel ? createMapOrQuill(serverLevel, pos, destinations, 150, true, 2, mapMarker, mapName, mapColor) : ItemStack.EMPTY;
    }
}