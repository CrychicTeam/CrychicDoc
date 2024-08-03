package dev.latvian.mods.kubejs.block.custom;

import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.minecraft.resources.ResourceLocation;

public abstract class MultipartShapedBlockBuilder extends ShapedBlockBuilder {

    public MultipartShapedBlockBuilder(ResourceLocation i, String... suffixes) {
        super(i, suffixes);
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (this.blockstateJson != null) {
            generator.json(this.newID("blockstates/", ""), this.blockstateJson);
        } else {
            generator.multipartState(this.id, this::generateMultipartBlockStateJson);
        }
        if (this.modelJson != null) {
            generator.json(this.newID("models/", ""), this.modelJson);
        } else {
            this.generateBlockModelJsons(generator);
        }
        if (this.itemBuilder != null) {
            if (this.itemBuilder.modelJson != null) {
                generator.json(this.newID("models/item/", ""), this.itemBuilder.modelJson);
            } else {
                generator.itemModel(this.itemBuilder.id, x$0 -> this.generateItemModelJson(x$0));
            }
        }
    }

    protected abstract void generateMultipartBlockStateJson(MultipartBlockStateGenerator var1);
}