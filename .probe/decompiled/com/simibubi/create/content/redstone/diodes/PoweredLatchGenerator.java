package com.simibubi.create.content.redstone.diodes;

import com.tterrag.registrate.providers.DataGenContext;
import java.util.Vector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;

public class PoweredLatchGenerator extends AbstractDiodeGenerator {

    @Override
    protected <T extends Block> Vector<ModelFile> createModels(DataGenContext<Block, T> ctx, BlockModelProvider prov) {
        Vector<ModelFile> models = this.makeVector(2);
        String name = ctx.getName();
        ResourceLocation off = this.existing("latch_off");
        ResourceLocation on = this.existing("latch_on");
        models.add(prov.withExistingParent(name, off).texture("top", this.texture(ctx, "idle")));
        models.add(prov.withExistingParent(name + "_powered", on).texture("top", this.texture(ctx, "powering")));
        return models;
    }

    @Override
    protected int getModelIndex(BlockState state) {
        return state.m_61143_(PoweredLatchBlock.POWERING) ? 1 : 0;
    }
}