package com.github.alexmodguy.alexscaves.server.level.structure.processor;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.BaleenBoneBlock;
import com.github.alexmodguy.alexscaves.server.block.ThinBoneBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class WhalefallProcessor extends StructureProcessor {

    public static final Codec<WhalefallProcessor> CODEC = Codec.unit(() -> WhalefallProcessor.INSTANCE_GRAVITY);

    public static final Codec<WhalefallProcessor> CODEC_SKULL = Codec.unit(() -> WhalefallProcessor.INSTANCE_NO_GRAVITY);

    public static final WhalefallProcessor INSTANCE_GRAVITY = new WhalefallProcessor(true);

    public static final WhalefallProcessor INSTANCE_NO_GRAVITY = new WhalefallProcessor(false);

    private final boolean gravity;

    public WhalefallProcessor(boolean gravity) {
        this.gravity = gravity;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPosUnused, BlockPos pos, StructureTemplate.StructureBlockInfo relativeInfo, StructureTemplate.StructureBlockInfo info, StructurePlaceSettings settings) {
        RandomSource randomsource = settings.getRandom(info.pos());
        BlockPos fallTo = info.pos();
        if (this.gravity) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(info.pos().m_123341_(), info.pos().m_123342_(), info.pos().m_123343_());
            while (this.gravity && this.sinkThrough(levelReader.m_8055_(mutableBlockPos)) && mutableBlockPos.m_123342_() > levelReader.getMinBuildHeight()) {
                mutableBlockPos.move(0, -1, 0);
            }
            int i = mutableBlockPos.m_123342_();
            int j = relativeInfo.pos().m_123342_() + 1;
            fallTo = new BlockPos(info.pos().m_123341_(), i + j, info.pos().m_123343_());
        }
        BlockState in = info.state();
        if (in.m_60713_(ACBlockRegistry.BALEEN_BONE.get()) && (double) randomsource.nextFloat() < 0.2) {
            Direction.Axis axis = in.m_61143_(BaleenBoneBlock.X) ? Direction.Axis.X : Direction.Axis.Z;
            in = (BlockState) ((BlockState) ACBlockRegistry.THIN_BONE.get().defaultBlockState().m_61124_(ThinBoneBlock.f_55923_, axis)).m_61124_(ThinBoneBlock.OFFSET, 0);
        }
        return new StructureTemplate.StructureBlockInfo(fallTo, in, info.nbt());
    }

    private boolean sinkThrough(BlockState blockState) {
        return !blockState.m_60819_().isEmpty() || blockState.m_204336_(ACTagRegistry.WHALEFALL_IGNORES) || blockState.m_60795_();
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return this.gravity ? ACStructureProcessorRegistry.WHALEFALL.get() : ACStructureProcessorRegistry.WHALEFALL_SKULL.get();
    }
}