package dev.latvian.mods.kubejs.block.custom;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class ButtonBlockBuilder extends ShapedBlockBuilder {

    public transient BlockSetType behaviour;

    public transient int ticksToStayPressed;

    public transient boolean arrowsCanPress;

    public ButtonBlockBuilder(ResourceLocation i) {
        super(i, "_button");
        this.noCollision();
        this.tagBoth(BlockTags.BUTTONS.location());
        this.behaviour = BlockSetType.OAK;
        this.ticksToStayPressed = 30;
        this.arrowsCanPress = true;
    }

    public ButtonBlockBuilder behaviour(BlockSetType wt) {
        this.behaviour = wt;
        return this;
    }

    public ButtonBlockBuilder behaviour(String wt) {
        for (BlockSetType type : BlockSetType.values().toList()) {
            if (type.name().equals(wt)) {
                this.behaviour = type;
                return this;
            }
        }
        return this;
    }

    public ButtonBlockBuilder ticksToStayPressed(int t) {
        this.ticksToStayPressed = t;
        return this;
    }

    public ButtonBlockBuilder arrowsCanPress(boolean b) {
        this.arrowsCanPress = b;
        return this;
    }

    public Block createObject() {
        return new ButtonBlock(this.createProperties(), BlockSetType.OAK, this.ticksToStayPressed, this.arrowsCanPress);
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        String mod0 = this.newID("block/", "").toString();
        String mod1 = this.newID("block/", "_pressed").toString();
        bs.variant("face=ceiling,facing=east,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0).x(180).y(270)));
        bs.variant("face=ceiling,facing=east,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1).x(180).y(270)));
        bs.variant("face=ceiling,facing=north,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0).x(180).y(180)));
        bs.variant("face=ceiling,facing=north,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1).x(180).y(180)));
        bs.variant("face=ceiling,facing=south,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0).x(180)));
        bs.variant("face=ceiling,facing=south,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1).x(180)));
        bs.variant("face=ceiling,facing=west,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0).x(180).y(90)));
        bs.variant("face=ceiling,facing=west,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1).x(180).y(90)));
        bs.variant("face=floor,facing=east,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0).y(90)));
        bs.variant("face=floor,facing=east,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1).y(90)));
        bs.variant("face=floor,facing=north,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0)));
        bs.variant("face=floor,facing=north,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1)));
        bs.variant("face=floor,facing=south,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0).y(180)));
        bs.variant("face=floor,facing=south,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1).y(180)));
        bs.variant("face=floor,facing=west,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0).y(270)));
        bs.variant("face=floor,facing=west,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1).y(270)));
        bs.variant("face=wall,facing=east,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0).x(90).y(90).uvlock()));
        bs.variant("face=wall,facing=east,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1).x(90).y(90).uvlock()));
        bs.variant("face=wall,facing=north,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0).x(90).uvlock()));
        bs.variant("face=wall,facing=north,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1).x(90).uvlock()));
        bs.variant("face=wall,facing=south,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0).x(90).y(180).uvlock()));
        bs.variant("face=wall,facing=south,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1).x(90).y(180).uvlock()));
        bs.variant("face=wall,facing=west,powered=false", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod0).x(90).y(270).uvlock()));
        bs.variant("face=wall,facing=west,powered=true", (Consumer<VariantBlockStateGenerator.Variant>) (v -> v.model(mod1).x(90).y(270).uvlock()));
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        String texture = this.textures.get("texture").getAsString();
        generator.blockModel(this.id, m -> {
            m.parent("minecraft:block/button");
            m.texture("texture", texture);
        });
        generator.blockModel(this.newID("", "_pressed"), m -> {
            m.parent("minecraft:block/button_pressed");
            m.texture("texture", texture);
        });
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        m.parent("minecraft:block/button_inventory");
        m.texture("texture", this.textures.get("texture").getAsString());
    }
}