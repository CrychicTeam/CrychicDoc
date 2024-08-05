package dev.latvian.mods.kubejs.block.custom;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class ShapedBlockBuilder extends BlockBuilder {

    public ShapedBlockBuilder(ResourceLocation i, String... suffixes) {
        super(i);
        this.notSolid();
        this.property(BlockStateProperties.WATERLOGGED);
        this.texture("texture", "kubejs:block/detector");
        for (String s : suffixes) {
            if (this.id.getPath().endsWith(s)) {
                this.texture("texture", this.id.getNamespace() + ":block/" + this.id.getPath().substring(0, this.id.getPath().length() - s.length()));
                break;
            }
        }
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        super.textureAll(tex);
        return this.texture("texture", tex);
    }
}