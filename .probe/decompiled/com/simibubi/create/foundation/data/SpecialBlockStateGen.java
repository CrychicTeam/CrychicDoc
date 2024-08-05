package com.simibubi.create.foundation.data;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

public abstract class SpecialBlockStateGen {

    protected Property<?>[] getIgnoredProperties() {
        return new Property[0];
    }

    public final <T extends Block> void generate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov) {
        prov.getVariantBuilder((Block) ctx.getEntry()).forAllStatesExcept(state -> ConfiguredModel.builder().modelFile(this.getModel(ctx, prov, state)).rotationX((this.getXRotation(state) + 360) % 360).rotationY((this.getYRotation(state) + 360) % 360).build(), this.getIgnoredProperties());
    }

    protected int horizontalAngle(Direction direction) {
        return direction.getAxis().isVertical() ? 0 : (int) direction.toYRot();
    }

    protected abstract int getXRotation(BlockState var1);

    protected abstract int getYRotation(BlockState var1);

    public abstract <T extends Block> ModelFile getModel(DataGenContext<Block, T> var1, RegistrateBlockstateProvider var2, BlockState var3);
}