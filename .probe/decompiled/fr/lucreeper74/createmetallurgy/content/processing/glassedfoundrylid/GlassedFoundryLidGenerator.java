package fr.lucreeper74.createmetallurgy.content.processing.glassedfoundrylid;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class GlassedFoundryLidGenerator extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return this.horizontalAngle((Direction) state.m_61143_(GlassedFoundryLidBlock.FACING));
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        if ((Boolean) state.m_61143_(GlassedFoundryLidBlock.UNDER_FOUNDRY_MIXER)) {
            return state.m_61143_(GlassedFoundryLidBlock.OPEN) ? AssetLookup.partialBaseModel(ctx, prov, "under_mixer_open") : AssetLookup.partialBaseModel(ctx, prov, "under_mixer");
        } else {
            return state.m_61143_(GlassedFoundryLidBlock.OPEN) ? AssetLookup.partialBaseModel(ctx, prov, "open") : AssetLookup.partialBaseModel(ctx, prov);
        }
    }
}