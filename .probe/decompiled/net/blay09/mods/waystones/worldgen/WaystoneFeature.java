package net.blay09.mods.waystones.worldgen;

import com.mojang.serialization.Codec;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.block.WaystoneBlockBase;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WaystoneFeature extends Feature<NoneFeatureConfiguration> {

    private final BlockState waystoneState;

    public WaystoneFeature(Codec<NoneFeatureConfiguration> codec, BlockState waystoneState) {
        super(codec);
        this.waystoneState = waystoneState;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        Direction facing = Direction.values()[2 + random.nextInt(4)];
        BlockState state = world.m_8055_(pos);
        BlockPos posAbove = pos.above();
        BlockState stateAbove = world.m_8055_(posAbove);
        if (state.m_60795_() && stateAbove.m_60795_()) {
            world.m_7731_(pos, (BlockState) ((BlockState) ((BlockState) this.waystoneState.m_61124_(WaystoneBlock.HALF, DoubleBlockHalf.LOWER)).m_61124_(WaystoneBlockBase.ORIGIN, WaystoneOrigin.WILDERNESS)).m_61124_(WaystoneBlock.FACING, facing), 2);
            world.m_7731_(posAbove, (BlockState) ((BlockState) ((BlockState) this.waystoneState.m_61124_(WaystoneBlock.HALF, DoubleBlockHalf.UPPER)).m_61124_(WaystoneBlockBase.ORIGIN, WaystoneOrigin.WILDERNESS)).m_61124_(WaystoneBlock.FACING, facing), 2);
            WaystoneBlockEntity tileEntity = (WaystoneBlockEntity) world.m_7702_(pos);
            if (tileEntity != null) {
                tileEntity.initializeWaystone(world, null, WaystoneOrigin.WILDERNESS);
                BlockEntity tileEntityAbove = world.m_7702_(pos.above());
                if (tileEntityAbove instanceof WaystoneBlockEntity) {
                    ((WaystoneBlockEntity) tileEntityAbove).initializeFromBase(tileEntity);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}