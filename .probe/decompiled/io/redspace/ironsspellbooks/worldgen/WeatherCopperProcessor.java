package io.redspace.ironsspellbooks.worldgen;

import com.mojang.serialization.Codec;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.StructureProcessorRegistry;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class WeatherCopperProcessor extends StructureProcessor {

    public static final Codec<WeatherCopperProcessor> CODEC = Codec.FLOAT.fieldOf("bias").xmap(WeatherCopperProcessor::new, obj -> obj.bias).codec();

    float bias;

    public WeatherCopperProcessor(float bias) {
        this.bias = bias;
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo process(@Nonnull LevelReader level, @Nonnull BlockPos jigsawPiecePos, @Nonnull BlockPos jigsawPieceBottomCenterPos, @Nonnull StructureTemplate.StructureBlockInfo blockInfoLocal, @Nonnull StructureTemplate.StructureBlockInfo blockInfoGlobal, @Nonnull StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (blockInfoGlobal.state().m_60734_() instanceof WeatheringCopper copperBlock) {
            float f = Mth.lerp(Utils.random.nextFloat(), this.bias, 1.0F);
            int weatherStage = (int) (f * 4.0F);
            BlockState state = blockInfoGlobal.state();
            for (int i = 0; i < weatherStage; i++) {
                Optional<BlockState> nextState = copperBlock.getNext(state);
                if (nextState.isPresent()) {
                    state = ((BlockState) nextState.get()).m_60734_().withPropertiesOf(blockInfoGlobal.state());
                }
            }
            return new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), state, blockInfoGlobal.nbt());
        } else {
            return blockInfoGlobal;
        }
    }

    @Nonnull
    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorRegistry.WEATHER_COPPER.get();
    }
}