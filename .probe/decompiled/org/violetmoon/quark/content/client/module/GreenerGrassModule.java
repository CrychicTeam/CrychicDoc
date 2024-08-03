package org.violetmoon.quark.content.client.module;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.config.type.ConvulsionMatrixConfig;
import org.violetmoon.zeta.client.event.play.ZFirstClientTick;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BooleanSuppliers;

@ZetaLoadModule(category = "client")
public class GreenerGrassModule extends ZetaModule {

    private static final String[] GRASS_PRESET_NAMES = new String[] { "Dreary", "Vibrant" };

    private static final String GRASS_NAME = "Grass Colors";

    private static final String[] GRASS_BIOMES = new String[] { "plains", "forest", "mountains", "jungle", "savanna", "swamp" };

    private static final int[] GRASS_COLORS = new int[] { -7226023, -8798118, -7686519, -10892996, -4212907, -9801671 };

    private static final int[] FOLLIAGE_COLORS = new int[] { -8934609, -10899920, -9591957, -13583605, -5331926, -9801671 };

    private static final double[][] GRASS_PRESETS = new double[][] { { 1.24, 0.0, 0.0, 0.0, 0.84, 0.0, 0.0, 0.16, 0.36 }, { 1.0, 0.0, 0.0, 0.24, 1.0, 0.24, 0.0, 0.0, 0.6 } };

    private static final double[] GRASS_DEFAULT = new double[] { 0.89, 0.0, 0.0, 0.0, 1.11, 0.0, 0.0, 0.0, 0.89 };

    private static final String[] WATER_PRESET_NAMES = new String[] { "Muddy", "Colder" };

    private static final String WATER_NAME = "Water Colors";

    private static final String[] WATER_BIOMES = new String[] { "generic", "swamp", "meadow", "mangrove", "cold", "warm" };

    private static final int[] WATER_COLORS = new int[] { -12618012, -10388636, -15839537, -12944790, -12757034, -12331538 };

    private static final double[][] WATER_PRESETS = new double[][] { { 0.76, 0.0, 0.1, 0.0, 0.8, 0.0, 0.0, 0.0, 0.7 }, { 1.0, 0.0, 0.0, 0.24, 0.96, 0.24, 0.2, 0.52, 1.0 } };

    private static final double[] WATER_DEFAULT = new double[] { 0.86, 0.0, 0.0, 0.0, 1.0, 0.22, 0.0, 0.0, 1.22 };

    private static final ConvulsionMatrixConfig.Params GRASS_PARAMS = new ConvulsionMatrixConfig.Params("Grass Colors", GRASS_DEFAULT, GRASS_BIOMES, GRASS_COLORS, FOLLIAGE_COLORS, GRASS_PRESET_NAMES, GRASS_PRESETS);

    private static final ConvulsionMatrixConfig.Params WATER_PARAMS = new ConvulsionMatrixConfig.Params("Water Colors", WATER_DEFAULT, WATER_BIOMES, WATER_COLORS, null, WATER_PRESET_NAMES, WATER_PRESETS);

    @Config
    public static boolean affectLeaves = true;

    @Config
    public static boolean affectWater = false;

    @Config
    public static List<String> blockList = Lists.newArrayList(new String[] { "minecraft:large_fern", "minecraft:tall_grass", "minecraft:grass_block", "minecraft:fern", "minecraft:grass", "minecraft:potted_fern", "minecraft:sugar_cane", "environmental:giant_tall_grass", "valhelsia_structures:grass_block" });

    @Config
    public static List<String> leavesList = Lists.newArrayList(new String[] { "minecraft:spruce_leaves", "minecraft:birch_leaves", "minecraft:oak_leaves", "minecraft:jungle_leaves", "minecraft:acacia_leaves", "minecraft:dark_oak_leaves", "atmospheric:rosewood_leaves", "atmospheric:morado_leaves", "atmospheric:yucca_leaves", "autumnity:maple_leaves", "environmental:willow_leaves", "environmental:hanging_willow_leaves", "minecraft:vine" });

    @Config
    public static ConvulsionMatrixConfig colorMatrix = new ConvulsionMatrixConfig(GRASS_PARAMS);

    @Config
    public static ConvulsionMatrixConfig waterMatrix = new ConvulsionMatrixConfig(WATER_PARAMS);

    public int getWaterColor(int orig) {
        return orig;
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends GreenerGrassModule {

        @LoadEvent
        public void firstClientTick(ZFirstClientTick event) {
            this.registerGreenerColor(blockList, BooleanSuppliers.TRUE);
            this.registerGreenerColor(leavesList, () -> affectLeaves);
        }

        private void registerGreenerColor(Iterable<String> ids, BooleanSupplier condition) {
            BlockColors colors = Minecraft.getInstance().getBlockColors();
            for (String id : ids) {
                Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(id));
                if (block != Blocks.AIR) {
                    BlockColor original = QuarkClient.ZETA_CLIENT.getBlockColor(colors, block);
                    if (original != null) {
                        colors.register(this.getConvulsedColor(original, condition), block);
                    }
                }
            }
        }

        private BlockColor getConvulsedColor(BlockColor color, BooleanSupplier condition) {
            return (state, world, pos, tintIndex) -> {
                int originalColor = color.getColor(state, world, pos, tintIndex);
                return this.enabled && condition.getAsBoolean() ? colorMatrix.convolve(originalColor) : originalColor;
            };
        }

        @Override
        public int getWaterColor(int currColor) {
            return this.enabled && affectWater ? waterMatrix.convolve(currColor) : currColor;
        }
    }
}