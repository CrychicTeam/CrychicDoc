package com.simibubi.create.content.redstone.diodes;

import com.tterrag.registrate.providers.DataGenContext;
import java.util.Vector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;

public class ToggleLatchGenerator extends AbstractDiodeGenerator {

    @Override
    protected <T extends Block> Vector<ModelFile> createModels(DataGenContext<Block, T> ctx, BlockModelProvider prov) {
        String name = ctx.getName();
        Vector<ModelFile> models = this.makeVector(4);
        ResourceLocation off = this.existing("latch_off");
        ResourceLocation on = this.existing("latch_on");
        models.add(prov.getExistingFile(off));
        models.add(prov.withExistingParent(name + "_off_powered", off).texture("top", this.texture(ctx, "powered")));
        models.add(prov.getExistingFile(on));
        models.add(prov.withExistingParent(name + "_on_powered", on).texture("top", this.texture(ctx, "powered_powering")));
        return models;
    }

    @Override
    protected int getModelIndex(BlockState state) {
        return (state.m_61143_(ToggleLatchBlock.POWERING) ? 2 : 0) + (state.m_61143_(ToggleLatchBlock.f_52496_) ? 1 : 0);
    }
}