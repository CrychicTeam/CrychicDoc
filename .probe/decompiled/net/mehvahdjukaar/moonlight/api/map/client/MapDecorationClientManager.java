package net.mehvahdjukaar.moonlight.api.map.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import java.util.function.Function;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.type.MapDecorationType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;

public class MapDecorationClientManager extends TextureAtlasHolder {

    public static final ResourceLocation LOCATION_MAP_MARKERS = Moonlight.res("textures/atlas/map_markers.png");

    public static final RenderType MAP_MARKERS_RENDER_TYPE = RenderType.text(LOCATION_MAP_MARKERS);

    private static MapDecorationClientManager instance;

    private static final Map<ResourceLocation, Function<ResourceLocation, DecorationRenderer<?>>> CUSTOM_RENDERERS_FACTORIES = Maps.newHashMap();

    private static final Map<MapDecorationType<?, ?>, DecorationRenderer<?>> RENDERERS = Maps.newHashMap();

    public MapDecorationClientManager() {
        super(Minecraft.getInstance().getTextureManager(), LOCATION_MAP_MARKERS, Moonlight.res("map_markers"));
        instance = this;
    }

    public static TextureAtlasSprite getAtlasSprite(ResourceLocation location) {
        return instance.m_118901_(location);
    }

    @Deprecated(forRemoval = true)
    public static <T extends CustomMapDecoration> void registerCustomRenderer(MapDecorationType<T, ?> type, DecorationRenderer<T> renderer) {
        registerCustomRenderer(type.getCustomFactoryID(), r -> renderer);
    }

    public static <T extends CustomMapDecoration> void registerCustomRenderer(ResourceLocation typeFactoryId, Function<ResourceLocation, DecorationRenderer<T>> renderer) {
        CUSTOM_RENDERERS_FACTORIES.put(typeFactoryId, renderer);
    }

    private static <T extends CustomMapDecoration> DecorationRenderer<T> createRenderer(MapDecorationType<T, ?> type) {
        ResourceLocation id = Utils.getID(type);
        ResourceLocation texture = new ResourceLocation(id.getNamespace(), "map_marker/" + id.getPath());
        Function<ResourceLocation, DecorationRenderer<?>> custom = (Function<ResourceLocation, DecorationRenderer<?>>) CUSTOM_RENDERERS_FACTORIES.get(type.getCustomFactoryID());
        return custom != null ? (DecorationRenderer) custom.apply(texture) : new DecorationRenderer<>(texture);
    }

    public static <E extends CustomMapDecoration> DecorationRenderer<E> getRenderer(E decoration) {
        return getRenderer(decoration.getType());
    }

    public static <E extends CustomMapDecoration, T extends MapDecorationType<E, ?>> DecorationRenderer<E> getRenderer(T type) {
        return (DecorationRenderer<E>) RENDERERS.computeIfAbsent(type, t -> createRenderer(type));
    }

    public static <T extends CustomMapDecoration> boolean render(T decoration, PoseStack matrixStack, VertexConsumer vertexBuilder, MultiBufferSource buffer, @Nullable MapItemSavedData mapData, boolean isOnFrame, int light, int index) {
        DecorationRenderer<T> renderer = getRenderer(decoration);
        return renderer != null ? renderer.render(decoration, matrixStack, vertexBuilder, buffer, mapData, isOnFrame, light, index) : false;
    }
}