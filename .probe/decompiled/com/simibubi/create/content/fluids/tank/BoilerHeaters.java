package com.simibubi.create.content.fluids.tank;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.utility.AttachedRegistry;
import com.simibubi.create.foundation.utility.BlockHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class BoilerHeaters {

    private static final AttachedRegistry<Block, BoilerHeaters.Heater> BLOCK_HEATERS = new AttachedRegistry<>(ForgeRegistries.BLOCKS);

    private static final List<BoilerHeaters.HeaterProvider> GLOBAL_HEATERS = new ArrayList();

    public static void registerHeater(ResourceLocation block, BoilerHeaters.Heater heater) {
        BLOCK_HEATERS.register(block, heater);
    }

    public static void registerHeater(Block block, BoilerHeaters.Heater heater) {
        BLOCK_HEATERS.register(block, heater);
    }

    public static void registerHeaterProvider(BoilerHeaters.HeaterProvider provider) {
        GLOBAL_HEATERS.add(provider);
    }

    public static float getActiveHeat(Level level, BlockPos pos, BlockState state) {
        BoilerHeaters.Heater heater = BLOCK_HEATERS.get(state.m_60734_());
        if (heater != null) {
            return heater.getActiveHeat(level, pos, state);
        } else {
            for (BoilerHeaters.HeaterProvider provider : GLOBAL_HEATERS) {
                heater = provider.getHeater(level, pos, state);
                if (heater != null) {
                    return heater.getActiveHeat(level, pos, state);
                }
            }
            return -1.0F;
        }
    }

    public static void registerDefaults() {
        registerHeater((Block) AllBlocks.BLAZE_BURNER.get(), (level, pos, state) -> {
            BlazeBurnerBlock.HeatLevel value = (BlazeBurnerBlock.HeatLevel) state.m_61143_(BlazeBurnerBlock.HEAT_LEVEL);
            if (value == BlazeBurnerBlock.HeatLevel.NONE) {
                return -1.0F;
            } else if (value == BlazeBurnerBlock.HeatLevel.SEETHING) {
                return 2.0F;
            } else {
                return value.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING) ? 1.0F : 0.0F;
            }
        });
        registerHeaterProvider((level, pos, state) -> AllTags.AllBlockTags.PASSIVE_BOILER_HEATERS.matches(state) && BlockHelper.isNotUnheated(state) ? (level1, pos1, state1) -> 0.0F : null);
    }

    public interface Heater {

        float getActiveHeat(Level var1, BlockPos var2, BlockState var3);
    }

    public interface HeaterProvider {

        @Nullable
        BoilerHeaters.Heater getHeater(Level var1, BlockPos var2, BlockState var3);
    }
}