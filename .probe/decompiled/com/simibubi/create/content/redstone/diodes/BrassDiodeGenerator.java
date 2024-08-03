package com.simibubi.create.content.redstone.diodes;

import com.tterrag.registrate.providers.DataGenContext;
import java.util.Vector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;

public class BrassDiodeGenerator extends AbstractDiodeGenerator {

    @Override
    protected <T extends Block> Vector<ModelFile> createModels(DataGenContext<Block, T> ctx, BlockModelProvider prov) {
        Vector<ModelFile> models = this.makeVector(4);
        String name = ctx.getName();
        ResourceLocation template = this.existing(name);
        models.add(prov.getExistingFile(template));
        models.add(prov.withExistingParent(name + "_powered", template).texture("top", this.texture(ctx, "powered")));
        models.add(prov.withExistingParent(name + "_powering", template).texture("torch", this.poweredTorch()).texture("top", this.texture(ctx, "powering")));
        models.add(prov.withExistingParent(name + "_powered_powering", template).texture("torch", this.poweredTorch()).texture("top", this.texture(ctx, "powered_powering")));
        return models;
    }

    @Override
    protected int getModelIndex(BlockState state) {
        return (state.m_61143_(BrassDiodeBlock.POWERING) ^ state.m_61143_(BrassDiodeBlock.INVERTED) ? 2 : 0) + (state.m_61143_(BrassDiodeBlock.f_52496_) ? 1 : 0);
    }
}