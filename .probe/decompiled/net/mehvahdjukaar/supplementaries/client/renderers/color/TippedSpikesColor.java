package net.mehvahdjukaar.supplementaries.client.renderers.color;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.mehvahdjukaar.moonlight.api.util.math.colors.HSLColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BambooSpikesBlockTile;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TippedSpikesColor implements BlockColor, ItemColor {

    private static final ThreadLocal<Int2IntMap> CACHED_COLORS_0 = ThreadLocal.withInitial(Int2IntOpenHashMap::new);

    private static final ThreadLocal<Int2IntMap> CACHED_COLORS_1 = ThreadLocal.withInitial(Int2IntOpenHashMap::new);

    private static int getCachedColor(int base, int tint) {
        return switch(tint) {
            case 1 ->
                ((Int2IntMap) CACHED_COLORS_0.get()).computeIfAbsent(base, b -> getProcessedColor(base, 0));
            case 2 ->
                ((Int2IntMap) CACHED_COLORS_1.get()).computeIfAbsent(base, b -> getProcessedColor(base, 1));
            default ->
                -1;
        };
    }

    @Override
    public int getColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tint) {
        if (world != null && pos != null) {
            if (world.m_7702_(pos) instanceof BambooSpikesBlockTile tile) {
                int color = tile.getColor();
                return getCachedColor(color, tint);
            }
            if (CompatHandler.QUARK && world instanceof Level level && QuarkCompat.getMovingBlockEntity(pos, state, level) instanceof BambooSpikesBlockTile tile) {
                int color = tile.getColor();
                return getCachedColor(color, tint);
            }
        }
        return 16777215;
    }

    @Override
    public int getColor(ItemStack stack, int tint) {
        return tint == 0 ? 16777215 : getCachedColor(PotionUtils.getColor(stack), tint);
    }

    private static int getProcessedColor(int rgb, int tint) {
        HSLColor hsl = new RGBColor(rgb).asHSL();
        float h = hsl.hue();
        if (tint == 1) {
            boolean b = h > 0.16667F && h < 0.6667F;
            float i = b ? -0.04F : 0.04F;
            h = (h + i) % 1.0F;
        }
        hsl = ColorHelper.prettyfyColor(hsl.withHue(h));
        float s = hsl.saturation();
        s = tint == 0 ? s * 0.81F : s * 0.74F;
        return hsl.withSaturation(s).asRGB().toInt();
    }
}