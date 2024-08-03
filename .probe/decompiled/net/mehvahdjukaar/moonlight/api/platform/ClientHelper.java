package net.mehvahdjukaar.moonlight.api.platform;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonElement;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.client.model.CustomBakedModel;
import net.mehvahdjukaar.moonlight.api.client.model.CustomModelLoader;
import net.mehvahdjukaar.moonlight.api.item.IItemDecoratorRenderer;
import net.mehvahdjukaar.moonlight.api.platform.forge.ClientHelperImpl;
import net.mehvahdjukaar.moonlight.api.resources.assets.LangBuilder;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class ClientHelper {

    private static final Cache<ResourceLocation, Material> CACHED_MATERIALS = CacheBuilder.newBuilder().expireAfterAccess(2L, TimeUnit.MINUTES).build();

    @ExpectPlatform
    @Transformed
    public static void addClientSetup(Runnable clientSetup) {
        ClientHelperImpl.addClientSetup(clientSetup);
    }

    @ExpectPlatform
    @Transformed
    public static void addClientSetupAsync(Runnable clientSetup) {
        ClientHelperImpl.addClientSetupAsync(clientSetup);
    }

    @ExpectPlatform
    @Transformed
    public static void registerRenderType(Block block, RenderType... types) {
        ClientHelperImpl.registerRenderType(block, types);
    }

    public static void registerRenderType(Block block, RenderType type) {
        registerRenderType(block, type);
    }

    @ExpectPlatform
    @Transformed
    public static void registerFluidRenderType(Fluid fluid, RenderType type) {
        ClientHelperImpl.registerFluidRenderType(fluid, type);
    }

    @ExpectPlatform
    @Transformed
    public static void addClientReloadListener(Supplier<PreparableReloadListener> listener, ResourceLocation location) {
        ClientHelperImpl.addClientReloadListener(listener, location);
    }

    @ExpectPlatform
    @Transformed
    public static void addParticleRegistration(Consumer<ClientHelper.ParticleEvent> eventListener) {
        ClientHelperImpl.addParticleRegistration(eventListener);
    }

    @ExpectPlatform
    @Transformed
    public static void addItemDecoratorsRegistration(Consumer<ClientHelper.ItemDecoratorEvent> eventListener) {
        ClientHelperImpl.addItemDecoratorsRegistration(eventListener);
    }

    @ExpectPlatform
    @Transformed
    public static void addEntityRenderersRegistration(Consumer<ClientHelper.EntityRendererEvent> eventListener) {
        ClientHelperImpl.addEntityRenderersRegistration(eventListener);
    }

    @ExpectPlatform
    @Transformed
    public static void addBlockEntityRenderersRegistration(Consumer<ClientHelper.BlockEntityRendererEvent> eventListener) {
        ClientHelperImpl.addBlockEntityRenderersRegistration(eventListener);
    }

    @ExpectPlatform
    @Transformed
    public static void addBlockColorsRegistration(Consumer<ClientHelper.BlockColorEvent> eventListener) {
        ClientHelperImpl.addBlockColorsRegistration(eventListener);
    }

    @ExpectPlatform
    @Transformed
    public static void addItemColorsRegistration(Consumer<ClientHelper.ItemColorEvent> eventListener) {
        ClientHelperImpl.addItemColorsRegistration(eventListener);
    }

    @ExpectPlatform
    @Transformed
    public static void addModelLayerRegistration(Consumer<ClientHelper.ModelLayerEvent> eventListener) {
        ClientHelperImpl.addModelLayerRegistration(eventListener);
    }

    @ExpectPlatform
    @Transformed
    public static void addSpecialModelRegistration(Consumer<ClientHelper.SpecialModelEvent> eventListener) {
        ClientHelperImpl.addSpecialModelRegistration(eventListener);
    }

    @ExpectPlatform
    @Transformed
    public static void addModelLoaderRegistration(Consumer<ClientHelper.ModelLoaderEvent> eventListener) {
        ClientHelperImpl.addModelLoaderRegistration(eventListener);
    }

    @ExpectPlatform
    @Transformed
    public static BakedModel getModel(ModelManager modelManager, ResourceLocation modelLocation) {
        return ClientHelperImpl.getModel(modelManager, modelLocation);
    }

    @ExpectPlatform
    @Transformed
    public static UnbakedModel getUnbakedModel(ModelManager modelManager, ResourceLocation modelLocation) {
        return ClientHelperImpl.getUnbakedModel(modelManager, modelLocation);
    }

    @ExpectPlatform
    @Transformed
    public static void addTooltipComponentRegistration(Consumer<ClientHelper.TooltipComponentEvent> eventListener) {
        ClientHelperImpl.addTooltipComponentRegistration(eventListener);
    }

    @ExpectPlatform
    @Transformed
    public static void addKeyBindRegistration(Consumer<ClientHelper.KeyBindEvent> eventListener) {
        ClientHelperImpl.addKeyBindRegistration(eventListener);
    }

    @ExpectPlatform
    @Transformed
    public static int getPixelRGBA(TextureAtlasSprite sprite, int frameIndex, int x, int y) {
        return ClientHelperImpl.getPixelRGBA(sprite, frameIndex, x, y);
    }

    @ExpectPlatform
    @Transformed
    public static BlockModel parseBlockModel(JsonElement json) {
        return ClientHelperImpl.parseBlockModel(json);
    }

    @ExpectPlatform
    @Transformed
    public static Path getModIcon(String modId) {
        return ClientHelperImpl.getModIcon(modId);
    }

    @ExpectPlatform
    @Transformed
    public static void registerOptionalTexturePack(ResourceLocation folderName, Component displayName, boolean defaultEnabled) {
        ClientHelperImpl.registerOptionalTexturePack(folderName, displayName, defaultEnabled);
    }

    public static void registerOptionalTexturePack(ResourceLocation folderName) {
        registerOptionalTexturePack(folderName, Component.literal(LangBuilder.getReadableName(folderName.getPath())), false);
    }

    public static Material getBlockMaterial(ResourceLocation bockTexture) {
        try {
            return (Material) CACHED_MATERIALS.get(bockTexture, () -> new Material(TextureAtlas.LOCATION_BLOCKS, bockTexture));
        } catch (ExecutionException var2) {
            throw new RuntimeException(var2);
        }
    }

    public interface BlockColorEvent {

        void register(BlockColor var1, Block... var2);

        int getColor(BlockState var1, BlockAndTintGetter var2, BlockPos var3, int var4);
    }

    @FunctionalInterface
    public interface BlockEntityRendererEvent {

        <E extends BlockEntity> void register(BlockEntityType<? extends E> var1, BlockEntityRendererProvider<E> var2);
    }

    @FunctionalInterface
    public interface EntityRendererEvent {

        <E extends Entity> void register(EntityType<? extends E> var1, EntityRendererProvider<E> var2);
    }

    public interface ItemColorEvent {

        void register(ItemColor var1, ItemLike... var2);

        int getColor(ItemStack var1, int var2);
    }

    @FunctionalInterface
    public interface ItemDecoratorEvent {

        void register(ItemLike var1, IItemDecoratorRenderer var2);
    }

    @FunctionalInterface
    public interface KeyBindEvent {

        void register(KeyMapping var1);
    }

    @FunctionalInterface
    public interface ModelLayerEvent {

        void register(ModelLayerLocation var1, Supplier<LayerDefinition> var2);
    }

    @FunctionalInterface
    public interface ModelLoaderEvent {

        void register(ResourceLocation var1, CustomModelLoader var2);

        default void register(ResourceLocation id, Supplier<CustomBakedModel> bakedModelFactory) {
            this.register(id, (CustomModelLoader) ((json, context) -> (modelBaker, spriteGetter, transform, location) -> (CustomBakedModel) bakedModelFactory.get()));
        }

        default void register(ResourceLocation id, BiFunction<ModelState, Function<Material, TextureAtlasSprite>, CustomBakedModel> bakedModelFactory) {
            this.register(id, (CustomModelLoader) ((json, context) -> (modelBaker, spriteGetter, transform, location) -> (CustomBakedModel) bakedModelFactory.apply(transform, spriteGetter)));
        }
    }

    @FunctionalInterface
    public interface ParticleEvent {

        <P extends ParticleType<T>, T extends ParticleOptions> void register(P var1, ClientHelper.ParticleFactory<T> var2);
    }

    @FunctionalInterface
    @OnlyIn(Dist.CLIENT)
    public interface ParticleFactory<T extends ParticleOptions> {

        @NotNull
        ParticleProvider<T> create(SpriteSet var1);
    }

    @FunctionalInterface
    public interface SpecialModelEvent {

        void register(ResourceLocation var1);
    }

    @FunctionalInterface
    public interface TooltipComponentEvent {

        <T extends TooltipComponent> void register(Class<T> var1, Function<? super T, ? extends ClientTooltipComponent> var2);
    }
}