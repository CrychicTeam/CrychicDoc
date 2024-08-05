package net.mehvahdjukaar.amendments.common.block;

import java.util.function.Supplier;
import net.mehvahdjukaar.amendments.common.tile.CandleSkullBlockTile;
import net.mehvahdjukaar.moonlight.api.block.IRecolorable;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class FloorCandleSkullBlock extends AbstractCandleSkullBlock implements IRecolorable {

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    public FloorCandleSkullBlock(BlockBehaviour.Properties properties) {
        this(properties, () -> ParticleTypes.SMALL_FLAME);
    }

    public FloorCandleSkullBlock(BlockBehaviour.Properties properties, Supplier<ParticleType<? extends ParticleOptions>> particle) {
        super(properties, particle);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(ROTATION, 0)).m_61124_(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(ROTATION);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(ROTATION, rotation.rotate((Integer) state.m_61143_(ROTATION), 16));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return (BlockState) state.m_61124_(ROTATION, mirror.mirror((Integer) state.m_61143_(ROTATION), 16));
    }

    @Override
    public boolean tryRecolor(Level level, BlockPos blockPos, BlockState blockState, @Nullable DyeColor dyeColor) {
        if (level.getBlockEntity(blockPos) instanceof CandleSkullBlockTile tile) {
            BlockState c = tile.getCandle();
            if (!c.m_60795_()) {
                Block otherCandle = BlocksColorAPI.changeColor(c.m_60734_(), dyeColor);
                if (otherCandle != null && !c.m_60713_(otherCandle)) {
                    tile.setCandle(otherCandle.withPropertiesOf(c));
                    tile.m_6596_();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isDefaultColor(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.getBlockEntity(blockPos) instanceof CandleSkullBlockTile tile) {
            BlockState c = tile.getCandle();
            return BlocksColorAPI.isDefaultColor(c.m_60734_());
        } else {
            return false;
        }
    }
}