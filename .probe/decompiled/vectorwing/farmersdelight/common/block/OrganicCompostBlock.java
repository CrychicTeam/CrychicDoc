package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;

public class OrganicCompostBlock extends Block {

    public static IntegerProperty COMPOSTING = IntegerProperty.create("composting", 0, 7);

    public OrganicCompostBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) super.defaultBlockState().m_61124_(COMPOSTING, 0));
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COMPOSTING);
        super.createBlockStateDefinition(builder);
    }

    public int getMaxCompostingStage() {
        return 7;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.f_46443_) {
            float chance = 0.0F;
            boolean hasWater = false;
            int maxLight = 0;
            for (BlockPos neighborPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                BlockState neighborState = level.m_8055_(neighborPos);
                if (neighborState.m_204336_(ModTags.COMPOST_ACTIVATORS)) {
                    chance += 0.02F;
                }
                if (neighborState.m_60819_().is(FluidTags.WATER)) {
                    hasWater = true;
                }
                int light = level.m_45517_(LightLayer.SKY, neighborPos.above());
                if (light > maxLight) {
                    maxLight = light;
                }
            }
            chance += maxLight > 12 ? 0.1F : 0.05F;
            chance += hasWater ? 0.1F : 0.0F;
            if (level.m_213780_().nextFloat() <= chance) {
                if ((Integer) state.m_61143_(COMPOSTING) == this.getMaxCompostingStage()) {
                    level.m_7731_(pos, ModBlocks.RICH_SOIL.get().defaultBlockState(), 3);
                } else {
                    level.m_7731_(pos, (BlockState) state.m_61124_(COMPOSTING, (Integer) state.m_61143_(COMPOSTING) + 1), 3);
                }
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return this.getMaxCompostingStage() + 1 - (Integer) blockState.m_61143_(COMPOSTING);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextInt(10) == 0) {
            level.addParticle(ParticleTypes.MYCELIUM, (double) pos.m_123341_() + (double) random.nextFloat(), (double) pos.m_123342_() + 1.1, (double) pos.m_123343_() + (double) random.nextFloat(), 0.0, 0.0, 0.0);
        }
    }
}