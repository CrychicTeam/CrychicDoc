package dev.latvian.mods.kubejs.block;

import dev.latvian.mods.kubejs.bindings.event.BlockEvents;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DetectorBlock extends Block {

    private final DetectorBlock.Builder builder;

    public DetectorBlock(DetectorBlock.Builder b) {
        super(BlockBehaviour.Properties.copy(Blocks.BEDROCK));
        this.builder = b;
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(BlockStateProperties.POWERED, false));
    }

    @Deprecated
    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        boolean p = !(Boolean) blockState.m_61143_(BlockStateProperties.POWERED);
        if (p == level.m_276867_(blockPos)) {
            level.setBlock(blockPos, (BlockState) blockState.m_61124_(BlockStateProperties.POWERED, p), 2);
            if (BlockEvents.DETECTOR_CHANGED.hasListeners(this.builder.detectorId) || (p ? BlockEvents.DETECTOR_POWERED : BlockEvents.DETECTOR_UNPOWERED).hasListeners(this.builder.detectorId)) {
                DetectorBlockEventJS e = new DetectorBlockEventJS(this.builder.detectorId, level, blockPos, p);
                BlockEvents.DETECTOR_CHANGED.post(level, this.builder.detectorId, e);
                if (p) {
                    BlockEvents.DETECTOR_POWERED.post(level, this.builder.detectorId, e);
                } else {
                    BlockEvents.DETECTOR_UNPOWERED.post(level, this.builder.detectorId, e);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.POWERED);
    }

    public static class Builder extends BlockBuilder {

        public transient String detectorId = (this.id.getNamespace().equals("kubejs") ? "" : this.id.getNamespace() + ".") + this.id.getPath().replace('/', '.');

        public Builder(ResourceLocation i) {
            super(i);
            if (this.detectorId.endsWith("_detector")) {
                this.detectorId = this.detectorId.substring(0, this.detectorId.length() - 9);
            }
            if (this.detectorId.startsWith("detector_")) {
                this.detectorId = this.detectorId.substring(9);
            }
            this.displayName(Component.literal("KubeJS Detector [" + this.detectorId + "]"));
        }

        public DetectorBlock.Builder detectorId(String id) {
            this.detectorId = id;
            this.displayName(Component.literal("KubeJS Detector [" + this.detectorId + "]"));
            return this;
        }

        public Block createObject() {
            return new DetectorBlock(this);
        }

        @Override
        public void generateAssetJsons(AssetJsonGenerator generator) {
            generator.blockState(this.id, bs -> {
                bs.simpleVariant("powered=false", "kubejs:block/detector");
                bs.simpleVariant("powered=true", "kubejs:block/detector_on");
            });
            generator.itemModel(this.id, m -> m.parent("kubejs:block/detector"));
        }
    }
}