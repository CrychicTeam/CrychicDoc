package io.github.lightman314.lightmanscurrency.client.renderer.blockentity.book;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;

public interface BookRendererGenerator {

    @Nullable
    BookRenderer createRendererForItem(@Nonnull ItemStack var1);
}