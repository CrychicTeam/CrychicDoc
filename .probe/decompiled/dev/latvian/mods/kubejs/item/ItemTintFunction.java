package dev.latvian.mods.kubejs.item;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.Undefined;
import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.mod.util.color.SimpleColor;
import dev.latvian.mods.rhino.mod.wrapper.ColorWrapper;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ItemTintFunction {

    ItemTintFunction BLOCK = (stack, index) -> {
        if (stack.getItem() instanceof BlockItem block) {
            BlockState s = block.getBlock().defaultBlockState();
            BlockBuilder internal = s.m_60734_().kjs$getBlockBuilder();
            if (internal != null && internal.tint != null) {
                return internal.tint.getColor(s, null, null, index);
            }
        }
        return null;
    };

    ItemTintFunction POTION = (stack, index) -> new SimpleColor(PotionUtils.getColor(stack));

    ItemTintFunction MAP = (stack, index) -> new SimpleColor(MapItem.getColor(stack));

    ItemTintFunction DISPLAY_COLOR_NBT = (stack, index) -> {
        CompoundTag tag = stack.getTagElement("display");
        return tag != null && tag.contains("color", 99) ? new SimpleColor(tag.getInt("color")) : null;
    };

    Color getColor(ItemStack var1, int var2);

    @Nullable
    static ItemTintFunction of(Context cx, Object o) {
        if (o == null || Undefined.isUndefined(o)) {
            return null;
        } else if (o instanceof ItemTintFunction) {
            return (ItemTintFunction) o;
        } else if (o instanceof List<?> list) {
            ItemTintFunction.Mapped map = new ItemTintFunction.Mapped();
            for (int i = 0; i < list.size(); i++) {
                ItemTintFunction f = of(cx, list.get(i));
                if (f != null) {
                    map.map.put(i, f);
                }
            }
            return map;
        } else {
            if (o instanceof CharSequence) {
                String i = o.toString();
                ItemTintFunction f = switch(i) {
                    case "block" ->
                        BLOCK;
                    case "potion" ->
                        POTION;
                    case "map" ->
                        MAP;
                    case "display_color_nbt" ->
                        DISPLAY_COLOR_NBT;
                    default ->
                        null;
                };
                if (f != null) {
                    return f;
                }
            } else if (o instanceof BaseFunction function) {
                return (ItemTintFunction) NativeJavaObject.createInterfaceAdapter(cx, ItemTintFunction.class, function);
            }
            return new ItemTintFunction.Fixed(ColorWrapper.of(o));
        }
    }

    public static record Fixed(Color color) implements ItemTintFunction {

        @Override
        public Color getColor(ItemStack stack, int index) {
            return this.color;
        }
    }

    public static class Mapped implements ItemTintFunction {

        public final Int2ObjectMap<ItemTintFunction> map = new Int2ObjectArrayMap(1);

        @Override
        public Color getColor(ItemStack stack, int index) {
            ItemTintFunction f = (ItemTintFunction) this.map.get(index);
            return f == null ? null : f.getColor(stack, index);
        }
    }
}