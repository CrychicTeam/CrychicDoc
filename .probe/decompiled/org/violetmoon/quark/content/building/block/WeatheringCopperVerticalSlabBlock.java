package org.violetmoon.quark.content.building.block;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.block.ext.CustomWeatheringCopper;
import org.violetmoon.zeta.module.ZetaModule;

public class WeatheringCopperVerticalSlabBlock extends QuarkVerticalSlabBlock implements CustomWeatheringCopper {

    private final WeatheringCopper.WeatherState weatherState;

    public WeatheringCopperVerticalSlabBlock first;

    public WeatheringCopperVerticalSlabBlock prev;

    public WeatheringCopperVerticalSlabBlock next;

    public WeatheringCopperVerticalSlabBlock(Block parent, ZetaModule module) {
        super(parent, module);
        this.weatherState = (WeatheringCopper.WeatherState) ((WeatheringCopper) parent).m_142297_();
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        this.m_220947_(state, world, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return this.getNext(state).isPresent();
    }

    @NotNull
    @Override
    public Optional<BlockState> getNext(@NotNull BlockState state) {
        return this.next == null ? Optional.empty() : Optional.of(this.next.m_152465_(state));
    }

    @NotNull
    @Override
    public Optional<BlockState> getPrevious(@NotNull BlockState state) {
        return this.prev == null ? Optional.empty() : Optional.of(this.prev.m_152465_(state));
    }

    @NotNull
    @Override
    public BlockState getFirst(@NotNull BlockState state) {
        return this.first.m_152465_(state);
    }

    @NotNull
    public WeatheringCopper.WeatherState getAge() {
        return this.weatherState;
    }
}