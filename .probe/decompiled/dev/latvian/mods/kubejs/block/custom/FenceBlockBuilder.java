package dev.latvian.mods.kubejs.block.custom;

import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;

public class FenceBlockBuilder extends MultipartShapedBlockBuilder {

    public FenceBlockBuilder(ResourceLocation i) {
        super(i, "_fence");
        this.tagBoth(BlockTags.FENCES.location());
        if (Platform.isForge()) {
            this.tagBoth(new ResourceLocation("forge:fences"));
        }
    }

    public Block createObject() {
        return new FenceBlock(this.createProperties());
    }

    @Override
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        String modPost = this.newID("block/", "_post").toString();
        String modSide = this.newID("block/", "_side").toString();
        bs.part("", modPost);
        bs.part("north=true", (Consumer<MultipartBlockStateGenerator.Part>) (p -> p.model(modSide).uvlock()));
        bs.part("east=true", (Consumer<MultipartBlockStateGenerator.Part>) (p -> p.model(modSide).uvlock().y(90)));
        bs.part("south=true", (Consumer<MultipartBlockStateGenerator.Part>) (p -> p.model(modSide).uvlock().y(180)));
        bs.part("west=true", (Consumer<MultipartBlockStateGenerator.Part>) (p -> p.model(modSide).uvlock().y(270)));
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        m.parent("minecraft:block/fence_inventory");
        m.texture("texture", this.textures.get("texture").getAsString());
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        String texture = this.textures.get("texture").getAsString();
        generator.blockModel(this.newID("", "_post"), m -> {
            m.parent("minecraft:block/fence_post");
            m.texture("texture", texture);
        });
        generator.blockModel(this.newID("", "_side"), m -> {
            m.parent("minecraft:block/fence_side");
            m.texture("texture", texture);
        });
    }
}