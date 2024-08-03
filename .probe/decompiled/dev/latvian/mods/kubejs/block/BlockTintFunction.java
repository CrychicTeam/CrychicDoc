package dev.latvian.mods.kubejs.block;

import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.Undefined;
import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.mod.util.color.SimpleColor;
import dev.latvian.mods.rhino.mod.util.color.SimpleColorWithAlpha;
import dev.latvian.mods.rhino.mod.wrapper.ColorWrapper;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.List;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface BlockTintFunction {

    BlockTintFunction GRASS = (s, l, p, i) -> new SimpleColor(l != null && p != null ? BiomeColors.getAverageGrassColor(l, p) : GrassColor.get(0.5, 1.0));

    Color DEFAULT_FOLIAGE_COLOR = new SimpleColor(FoliageColor.getDefaultColor());

    BlockTintFunction FOLIAGE = (s, l, p, i) -> (Color) (l != null && p != null ? new SimpleColor(BiomeColors.getAverageFoliageColor(l, p)) : DEFAULT_FOLIAGE_COLOR);

    BlockTintFunction.Fixed EVERGREEN_FOLIAGE = new BlockTintFunction.Fixed(new SimpleColor(FoliageColor.getEvergreenColor()));

    BlockTintFunction.Fixed BIRCH_FOLIAGE = new BlockTintFunction.Fixed(new SimpleColor(FoliageColor.getBirchColor()));

    BlockTintFunction.Fixed MANGROVE_FOLIAGE = new BlockTintFunction.Fixed(new SimpleColor(FoliageColor.getMangroveColor()));

    BlockTintFunction WATER = (s, l, p, i) -> l != null && p != null ? new SimpleColorWithAlpha(BiomeColors.getAverageWaterColor(l, p)) : null;

    Color[] REDSTONE_COLORS = new Color[16];

    BlockTintFunction REDSTONE = (state, level, pos, index) -> {
        if (REDSTONE_COLORS[0] == null) {
            for (int i = 0; i < REDSTONE_COLORS.length; i++) {
                REDSTONE_COLORS[i] = new SimpleColor(RedStoneWireBlock.getColorForPower(i));
            }
        }
        return REDSTONE_COLORS[state.m_61143_(BlockStateProperties.POWER)];
    };

    Color getColor(BlockState var1, @Nullable BlockAndTintGetter var2, @Nullable BlockPos var3, int var4);

    @Nullable
    static BlockTintFunction of(Context cx, Object o) {
        if (o == null || Undefined.isUndefined(o)) {
            return null;
        } else if (o instanceof BlockTintFunction) {
            return (BlockTintFunction) o;
        } else if (o instanceof List<?> list) {
            BlockTintFunction.Mapped map = new BlockTintFunction.Mapped();
            for (int i = 0; i < list.size(); i++) {
                BlockTintFunction f = of(cx, list.get(i));
                if (f != null) {
                    map.map.put(i, f);
                }
            }
            return map;
        } else {
            if (o instanceof CharSequence) {
                String i = o.toString();
                BlockTintFunction f = (BlockTintFunction) (switch(i) {
                    case "grass" ->
                        GRASS;
                    case "foliage" ->
                        FOLIAGE;
                    case "evergreen_foliage" ->
                        EVERGREEN_FOLIAGE;
                    case "birch_foliage" ->
                        BIRCH_FOLIAGE;
                    case "mangrove_foliage" ->
                        MANGROVE_FOLIAGE;
                    case "water" ->
                        WATER;
                    case "redstone" ->
                        REDSTONE;
                    default ->
                        null;
                });
                if (f != null) {
                    return f;
                }
            } else if (o instanceof BaseFunction function) {
                return (BlockTintFunction) NativeJavaObject.createInterfaceAdapter(cx, BlockTintFunction.class, function);
            }
            return new BlockTintFunction.Fixed(ColorWrapper.of(o));
        }
    }

    public static record Fixed(Color color) implements BlockTintFunction {

        @Override
        public Color getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int index) {
            return this.color;
        }
    }

    public static class Mapped implements BlockTintFunction {

        public final Int2ObjectMap<BlockTintFunction> map = new Int2ObjectArrayMap(1);

        @Override
        public Color getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int index) {
            BlockTintFunction f = (BlockTintFunction) this.map.get(index);
            return f == null ? null : f.getColor(state, level, pos, index);
        }
    }
}