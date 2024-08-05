package net.minecraft.client.color.block;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.IdMapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;

public class BlockColors {

    private static final int DEFAULT = -1;

    private final IdMapper<BlockColor> blockColors = new IdMapper<>(32);

    private final Map<Block, Set<Property<?>>> coloringStates = Maps.newHashMap();

    public static BlockColors createDefault() {
        BlockColors $$0 = new BlockColors();
        $$0.register((p_276233_, p_276234_, p_276235_, p_276236_) -> p_276234_ != null && p_276235_ != null ? BiomeColors.getAverageGrassColor(p_276234_, p_276233_.m_61143_(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER ? p_276235_.below() : p_276235_) : GrassColor.getDefaultColor(), Blocks.LARGE_FERN, Blocks.TALL_GRASS);
        $$0.addColoringState(DoublePlantBlock.HALF, Blocks.LARGE_FERN, Blocks.TALL_GRASS);
        $$0.register((p_276237_, p_276238_, p_276239_, p_276240_) -> p_276238_ != null && p_276239_ != null ? BiomeColors.getAverageGrassColor(p_276238_, p_276239_) : GrassColor.getDefaultColor(), Blocks.GRASS_BLOCK, Blocks.FERN, Blocks.GRASS, Blocks.POTTED_FERN);
        $$0.register((p_276241_, p_276242_, p_276243_, p_276244_) -> {
            if (p_276244_ != 0) {
                return p_276242_ != null && p_276243_ != null ? BiomeColors.getAverageGrassColor(p_276242_, p_276243_) : GrassColor.getDefaultColor();
            } else {
                return -1;
            }
        }, Blocks.PINK_PETALS);
        $$0.register((p_92636_, p_92637_, p_92638_, p_92639_) -> FoliageColor.getEvergreenColor(), Blocks.SPRUCE_LEAVES);
        $$0.register((p_92631_, p_92632_, p_92633_, p_92634_) -> FoliageColor.getBirchColor(), Blocks.BIRCH_LEAVES);
        $$0.register((p_92626_, p_92627_, p_92628_, p_92629_) -> p_92627_ != null && p_92628_ != null ? BiomeColors.getAverageFoliageColor(p_92627_, p_92628_) : FoliageColor.getDefaultColor(), Blocks.OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.VINE, Blocks.MANGROVE_LEAVES);
        $$0.register((p_92621_, p_92622_, p_92623_, p_92624_) -> p_92622_ != null && p_92623_ != null ? BiomeColors.getAverageWaterColor(p_92622_, p_92623_) : -1, Blocks.WATER, Blocks.BUBBLE_COLUMN, Blocks.WATER_CAULDRON);
        $$0.register((p_92616_, p_92617_, p_92618_, p_92619_) -> RedStoneWireBlock.getColorForPower((Integer) p_92616_.m_61143_(RedStoneWireBlock.POWER)), Blocks.REDSTONE_WIRE);
        $$0.addColoringState(RedStoneWireBlock.POWER, Blocks.REDSTONE_WIRE);
        $$0.register((p_92611_, p_92612_, p_92613_, p_92614_) -> p_92612_ != null && p_92613_ != null ? BiomeColors.getAverageGrassColor(p_92612_, p_92613_) : -1, Blocks.SUGAR_CANE);
        $$0.register((p_92606_, p_92607_, p_92608_, p_92609_) -> 14731036, Blocks.ATTACHED_MELON_STEM, Blocks.ATTACHED_PUMPKIN_STEM);
        $$0.register((p_92601_, p_92602_, p_92603_, p_92604_) -> {
            int $$4 = (Integer) p_92601_.m_61143_(StemBlock.AGE);
            int $$5 = $$4 * 32;
            int $$6 = 255 - $$4 * 8;
            int $$7 = $$4 * 4;
            return $$5 << 16 | $$6 << 8 | $$7;
        }, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
        $$0.addColoringState(StemBlock.AGE, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM);
        $$0.register((p_92596_, p_92597_, p_92598_, p_92599_) -> p_92597_ != null && p_92598_ != null ? 2129968 : 7455580, Blocks.LILY_PAD);
        return $$0;
    }

    public int getColor(BlockState blockState0, Level level1, BlockPos blockPos2) {
        BlockColor $$3 = this.blockColors.byId(BuiltInRegistries.BLOCK.m_7447_(blockState0.m_60734_()));
        if ($$3 != null) {
            return $$3.getColor(blockState0, null, null, 0);
        } else {
            MapColor $$4 = blockState0.m_284242_(level1, blockPos2);
            return $$4 != null ? $$4.col : -1;
        }
    }

    public int getColor(BlockState blockState0, @Nullable BlockAndTintGetter blockAndTintGetter1, @Nullable BlockPos blockPos2, int int3) {
        BlockColor $$4 = this.blockColors.byId(BuiltInRegistries.BLOCK.m_7447_(blockState0.m_60734_()));
        return $$4 == null ? -1 : $$4.getColor(blockState0, blockAndTintGetter1, blockPos2, int3);
    }

    public void register(BlockColor blockColor0, Block... block1) {
        for (Block $$2 : block1) {
            this.blockColors.addMapping(blockColor0, BuiltInRegistries.BLOCK.m_7447_($$2));
        }
    }

    private void addColoringStates(Set<Property<?>> setProperty0, Block... block1) {
        for (Block $$2 : block1) {
            this.coloringStates.put($$2, setProperty0);
        }
    }

    private void addColoringState(Property<?> property0, Block... block1) {
        this.addColoringStates(ImmutableSet.of(property0), block1);
    }

    public Set<Property<?>> getColoringProperties(Block block0) {
        return (Set<Property<?>>) this.coloringStates.getOrDefault(block0, ImmutableSet.of());
    }
}