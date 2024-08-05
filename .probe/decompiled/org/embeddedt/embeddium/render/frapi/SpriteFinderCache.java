package org.embeddedt.embeddium.render.frapi;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = "embeddium", value = { Dist.CLIENT }, bus = Bus.MOD)
public class SpriteFinderCache {

    private static final SpriteFinderCache.Finder NULL_FINDER = (u, v) -> null;

    private static final MethodHandle SPRITE_FINDER_HANDLE;

    private static SpriteFinderCache.Finder blockAtlasSpriteFinder = NULL_FINDER;

    @SubscribeEvent
    public static void onReload(RegisterClientReloadListenersEvent event) {
        if (SPRITE_FINDER_HANDLE != null) {
            var listener = new SimplePreparableReloadListener<Object>() {

                @Override
                protected Object prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
                    return null;
                }

                @Override
                protected void apply(Object pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
                    ModelManager modelManager = Minecraft.getInstance().getModelManager();
                    SpriteFinderCache.Finder finder = SpriteFinderCache.NULL_FINDER;
                    SpriteFinder fabricFinder = SpriteFinder.get(modelManager.getAtlas(InventoryMenu.BLOCK_ATLAS));
                    try {
                        MethodType bootstrapType = MethodType.methodType(SpriteFinderCache.Finder.class, SpriteFinder.class);
                        MethodType invocationType = MethodType.methodType(TextureAtlasSprite.class, float.class, float.class);
                        finder = (SpriteFinderCache.Finder) LambdaMetafactory.metafactory(MethodHandles.lookup(), "findNearestSprite", bootstrapType, invocationType, SpriteFinderCache.SPRITE_FINDER_HANDLE, invocationType).getTarget().invokeExact(fabricFinder);
                    } catch (Throwable var9) {
                        var9.printStackTrace();
                    }
                    SpriteFinderCache.blockAtlasSpriteFinder = finder;
                }
            };
            event.registerReloadListener(listener);
        }
    }

    public static SpriteFinderCache.Finder forBlockAtlas() {
        return blockAtlasSpriteFinder;
    }

    static {
        MethodHandle mh;
        try {
            mh = MethodHandles.lookup().findVirtual(Class.forName("net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder"), "find", MethodType.methodType(TextureAtlasSprite.class, float.class, float.class));
        } catch (Throwable var2) {
            mh = null;
        }
        SPRITE_FINDER_HANDLE = mh;
    }

    public interface Finder {

        @Nullable
        TextureAtlasSprite findNearestSprite(float var1, float var2);
    }
}