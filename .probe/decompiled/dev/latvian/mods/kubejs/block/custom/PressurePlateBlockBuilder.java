package dev.latvian.mods.kubejs.block.custom;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class PressurePlateBlockBuilder extends ShapedBlockBuilder {

    public transient BlockSetType behaviour;

    public PressurePlateBlockBuilder(ResourceLocation i) {
        super(i, "_pressure_plate");
        this.noCollision();
        this.tagBoth(BlockTags.PRESSURE_PLATES.location());
        this.behaviour = BlockSetType.OAK;
    }

    public PressurePlateBlockBuilder behaviour(BlockSetType wt) {
        this.behaviour = wt;
        return this;
    }

    public PressurePlateBlockBuilder behaviour(String wt) {
        for (BlockSetType type : BlockSetType.values().toList()) {
            if (type.name().equals(wt)) {
                this.behaviour = type;
                return this;
            }
        }
        return this;
    }

    public Block createObject() {
        return new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, this.createProperties(), this.behaviour);
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        bs.variant("powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(this.newID("block/", "_down").toString())));
        bs.variant("powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(this.newID("block/", "_up").toString())));
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        String texture = this.textures.get("texture").getAsString();
        generator.blockModel(this.newID("", "_down"), m -> {
            m.parent("minecraft:block/pressure_plate_down");
            m.texture("texture", texture);
        });
        generator.blockModel(this.newID("", "_up"), m -> {
            m.parent("minecraft:block/pressure_plate_up");
            m.texture("texture", texture);
        });
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        m.parent(this.newID("block/", "_up").toString());
    }
}