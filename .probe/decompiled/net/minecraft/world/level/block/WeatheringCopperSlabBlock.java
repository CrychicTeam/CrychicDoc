package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringCopperSlabBlock extends SlabBlock implements WeatheringCopper {

    private final WeatheringCopper.WeatherState weatherState;

    public WeatheringCopperSlabBlock(WeatheringCopper.WeatherState weatheringCopperWeatherState0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.weatherState = weatheringCopperWeatherState0;
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        this.m_220947_(blockState0, serverLevel1, blockPos2, randomSource3);
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return WeatheringCopper.getNext(blockState0.m_60734_()).isPresent();
    }

    public WeatheringCopper.WeatherState getAge() {
        return this.weatherState;
    }
}