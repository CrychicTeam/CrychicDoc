package io.github.edwinmindcraft.origins.data.generator;

import io.github.apace100.origins.registry.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class OriginsBlockStateProvider extends BlockStateProvider {

    public OriginsBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen.getPackOutput(), "origins", exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        BlockModelBuilder builder = this.models().withExistingParent("temporary_cobweb", "cobweb").renderType("cutout");
        this.simpleBlock(ModBlocks.TEMPORARY_COBWEB.get(), builder);
    }
}