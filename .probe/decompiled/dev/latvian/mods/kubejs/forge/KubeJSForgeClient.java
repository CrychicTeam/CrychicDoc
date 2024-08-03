package dev.latvian.mods.kubejs.forge;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.BlockTintFunctionWrapper;
import dev.latvian.mods.kubejs.client.ItemTintFunctionWrapper;
import dev.latvian.mods.kubejs.fluid.FluidBucketItemBuilder;
import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class KubeJSForgeClient {

    public KubeJSForgeClient() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOW, this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::blockColors);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::itemColors);
    }

    private void setupClient(FMLClientSetupEvent event) {
        KubeJS.PROXY.clientSetup();
        for (BuilderBase<? extends Block> builder : RegistryInfo.BLOCK) {
            if (builder instanceof BlockBuilder b) {
                String var5 = b.renderType;
                switch(var5) {
                    case "cutout":
                        ItemBlockRenderTypes.setRenderLayer(b.get(), RenderType.cutout());
                        break;
                    case "cutout_mipped":
                        ItemBlockRenderTypes.setRenderLayer(b.get(), RenderType.cutoutMipped());
                        break;
                    case "translucent":
                        ItemBlockRenderTypes.setRenderLayer(b.get(), RenderType.translucent());
                }
            }
        }
        for (BuilderBase<? extends Fluid> builderx : RegistryInfo.FLUID) {
            if (builderx instanceof FluidBuilder b) {
                String var10 = b.renderType;
                switch(var10) {
                    case "cutout":
                        ItemBlockRenderTypes.setRenderLayer(b.get().getSource(), RenderType.cutout());
                        ItemBlockRenderTypes.setRenderLayer(b.get().getFlowing(), RenderType.cutout());
                        break;
                    case "cutout_mipped":
                        ItemBlockRenderTypes.setRenderLayer(b.get().getSource(), RenderType.cutoutMipped());
                        ItemBlockRenderTypes.setRenderLayer(b.get().getFlowing(), RenderType.cutoutMipped());
                        break;
                    case "translucent":
                        ItemBlockRenderTypes.setRenderLayer(b.get().getSource(), RenderType.translucent());
                        ItemBlockRenderTypes.setRenderLayer(b.get().getFlowing(), RenderType.translucent());
                }
            }
        }
    }

    private void blockColors(RegisterColorHandlersEvent.Block event) {
        for (BuilderBase<? extends Block> builder : RegistryInfo.BLOCK) {
            if (builder instanceof BlockBuilder) {
                BlockBuilder b = (BlockBuilder) builder;
                if (b.tint != null) {
                    event.register(new BlockTintFunctionWrapper(b.tint), b.get());
                }
            }
        }
    }

    private void itemColors(RegisterColorHandlersEvent.Item event) {
        for (BuilderBase<? extends Item> builder : RegistryInfo.ITEM) {
            if (builder instanceof ItemBuilder b && b.tint != null) {
                event.register(new ItemTintFunctionWrapper(b.tint), b.get());
            }
            if (builder instanceof FluidBucketItemBuilder b && b.fluidBuilder.bucketColor != -1) {
                event.register((stack, index) -> index == 1 ? b.fluidBuilder.bucketColor : -1, b.get());
            }
        }
    }
}