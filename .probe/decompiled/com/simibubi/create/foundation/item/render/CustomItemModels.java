package com.simibubi.create.foundation.item.render;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class CustomItemModels {

    private final Multimap<ResourceLocation, NonNullFunction<BakedModel, ? extends BakedModel>> modelFuncs = MultimapBuilder.hashKeys().arrayListValues().build();

    private final Map<Item, NonNullFunction<BakedModel, ? extends BakedModel>> finalModelFuncs = new IdentityHashMap();

    private boolean funcsLoaded = false;

    public void register(ResourceLocation item, NonNullFunction<BakedModel, ? extends BakedModel> func) {
        this.modelFuncs.put(item, func);
    }

    public void forEach(NonNullBiConsumer<Item, NonNullFunction<BakedModel, ? extends BakedModel>> consumer) {
        this.loadEntriesIfMissing();
        this.finalModelFuncs.forEach(consumer);
    }

    private void loadEntriesIfMissing() {
        if (!this.funcsLoaded) {
            this.loadEntries();
            this.funcsLoaded = true;
        }
    }

    private void loadEntries() {
        this.finalModelFuncs.clear();
        this.modelFuncs.asMap().forEach((location, funcList) -> {
            Item item = ForgeRegistries.ITEMS.getValue(location);
            if (item != null) {
                NonNullFunction<BakedModel, ? extends BakedModel> finalFunc = null;
                for (NonNullFunction<BakedModel, ? extends BakedModel> func : funcList) {
                    if (finalFunc == null) {
                        finalFunc = func;
                    } else {
                        finalFunc = finalFunc.andThen(func);
                    }
                }
                this.finalModelFuncs.put(item, finalFunc);
            }
        });
    }
}