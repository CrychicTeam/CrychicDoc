package net.mehvahdjukaar.moonlight.api.block;

import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public interface IColored {

    @Nullable
    DyeColor getColor();

    @Deprecated(forRemoval = true)
    @Nullable
    default Item changeItemColor(@Nullable DyeColor color) {
        return BlocksColorAPI.changeColor(((ItemLike) this).asItem(), color);
    }

    default boolean supportsBlankColor() {
        return false;
    }
}