package net.minecraft.world.level.block;

import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PowderSnowCauldronBlock extends LayeredCauldronBlock {

    public PowderSnowCauldronBlock(BlockBehaviour.Properties blockBehaviourProperties0, Predicate<Biome.Precipitation> predicateBiomePrecipitation1, Map<Item, CauldronInteraction> mapItemCauldronInteraction2) {
        super(blockBehaviourProperties0, predicateBiomePrecipitation1, mapItemCauldronInteraction2);
    }

    @Override
    protected void handleEntityOnFireInside(BlockState blockState0, Level level1, BlockPos blockPos2) {
        m_153559_((BlockState) Blocks.WATER_CAULDRON.defaultBlockState().m_61124_(f_153514_, (Integer) blockState0.m_61143_(f_153514_)), level1, blockPos2);
    }
}