package net.blay09.mods.balm.forge.client.rendering;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeBalmRenderers implements BalmRenderers {

    private final Map<String, ForgeBalmRenderers.Registrations> registrations = new ConcurrentHashMap();

    @Override
    public ModelLayerLocation registerModel(ResourceLocation location, Supplier<LayerDefinition> layerDefinition) {
        ModelLayerLocation modelLayerLocation = new ModelLayerLocation(location, "main");
        this.getActiveRegistrations().layerDefinitions.put(modelLayerLocation, layerDefinition);
        return modelLayerLocation;
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> type, EntityRendererProvider<? super T> provider) {
        this.getActiveRegistrations().entityRenderers.add(Pair.of(type::get, provider));
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<? super T> provider) {
        this.getActiveRegistrations().blockEntityRenderers.add(Pair.of(type::get, provider));
    }

    @Override
    public void registerBlockColorHandler(BlockColor color, Supplier<Block[]> blocks) {
        this.getActiveRegistrations().blockColors.add(new ForgeBalmRenderers.ColorRegistration<>(color, blocks));
    }

    @Override
    public void registerItemColorHandler(ItemColor color, Supplier<ItemLike[]> items) {
        this.getActiveRegistrations().itemColors.add(new ForgeBalmRenderers.ColorRegistration<>(color, items));
    }

    @Override
    public void setBlockRenderType(Supplier<Block> block, RenderType renderType) {
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this.getActiveRegistrations());
    }

    private ForgeBalmRenderers.Registrations getActiveRegistrations() {
        return (ForgeBalmRenderers.Registrations) this.registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new ForgeBalmRenderers.Registrations());
    }

    private static class ColorRegistration<THandler, TObject> {

        private final THandler color;

        private final Supplier<TObject[]> objects;

        public ColorRegistration(THandler color, Supplier<TObject[]> objects) {
            this.color = color;
            this.objects = objects;
        }

        public THandler getColor() {
            return this.color;
        }

        public Supplier<TObject[]> getObjects() {
            return this.objects;
        }
    }

    private static class Registrations {

        public final Map<ModelLayerLocation, Supplier<LayerDefinition>> layerDefinitions = new HashMap();

        public final List<Pair<Supplier<BlockEntityType<?>>, BlockEntityRendererProvider<BlockEntity>>> blockEntityRenderers = new ArrayList();

        public final List<Pair<Supplier<EntityType<?>>, EntityRendererProvider<Entity>>> entityRenderers = new ArrayList();

        public final List<ForgeBalmRenderers.ColorRegistration<BlockColor, Block>> blockColors = new ArrayList();

        public final List<ForgeBalmRenderers.ColorRegistration<ItemColor, ItemLike>> itemColors = new ArrayList();

        @SubscribeEvent
        public void setupClient(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public void initRenderers(EntityRenderersEvent.RegisterRenderers event) {
            for (Pair<Supplier<BlockEntityType<?>>, BlockEntityRendererProvider<BlockEntity>> entry : this.blockEntityRenderers) {
                event.registerBlockEntityRenderer((BlockEntityType) ((Supplier) entry.getFirst()).get(), (BlockEntityRendererProvider) entry.getSecond());
            }
            for (Pair<Supplier<EntityType<?>>, EntityRendererProvider<Entity>> entry : this.entityRenderers) {
                event.registerEntityRenderer((EntityType) ((Supplier) entry.getFirst()).get(), (EntityRendererProvider) entry.getSecond());
            }
        }

        @SubscribeEvent
        public void initLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            for (Entry<ModelLayerLocation, Supplier<LayerDefinition>> entry : this.layerDefinitions.entrySet()) {
                event.registerLayerDefinition((ModelLayerLocation) entry.getKey(), (Supplier<LayerDefinition>) entry.getValue());
            }
        }

        @SubscribeEvent
        public void initBlockColors(RegisterColorHandlersEvent.Block event) {
            for (ForgeBalmRenderers.ColorRegistration<BlockColor, Block> blockColor : this.blockColors) {
                event.register(blockColor.getColor(), (Block[]) blockColor.getObjects().get());
            }
        }

        @SubscribeEvent
        public void initItemColors(RegisterColorHandlersEvent.Item event) {
            for (ForgeBalmRenderers.ColorRegistration<ItemColor, ItemLike> itemColor : this.itemColors) {
                event.register(itemColor.getColor(), (ItemLike[]) itemColor.getObjects().get());
            }
        }
    }
}