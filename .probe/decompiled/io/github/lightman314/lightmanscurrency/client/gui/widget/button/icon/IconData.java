package io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.rendering.Sprite;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;

public abstract class IconData {

    public static final IconData BLANK = of();

    public final void render(EasyGuiGraphics gui, ScreenPosition pos) {
        this.render(gui, pos.x, pos.y);
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void render(EasyGuiGraphics var1, int var2, int var3);

    public static IconData of(@Nonnull ItemLike item) {
        return of(new ItemStack(item));
    }

    public static IconData of(@Nonnull ItemLike item, @Nullable String countTextOverride) {
        return of(new ItemStack(item), countTextOverride);
    }

    public static IconData of(@Nonnull RegistryObject<? extends ItemLike> item) {
        return of(new ItemStack(item.get()));
    }

    public static IconData of(@Nonnull RegistryObject<? extends ItemLike> item, @Nullable String countTextOverride) {
        return of(new ItemStack(item.get()), countTextOverride);
    }

    public static IconData of(@Nonnull ItemStack iconStack) {
        return of(iconStack, null);
    }

    public static IconData of(@Nonnull ItemStack iconStack, @Nullable String countTextOverride) {
        return new IconData.ItemIcon(iconStack, countTextOverride);
    }

    public static IconData of(@Nonnull ResourceLocation iconImage, int u, int v) {
        return new IconData.ImageIcon(Sprite.SimpleSprite(iconImage, u, v, 16, 16));
    }

    public static IconData of(@Nonnull Sprite sprite) {
        return new IconData.ImageIcon(sprite);
    }

    public static IconData of(@Nonnull Component iconText) {
        return new IconData.TextIcon(iconText, 16777215);
    }

    public static IconData of(@Nonnull Component iconText, int textColor) {
        return new IconData.TextIcon(iconText, textColor);
    }

    public static IconData of(@Nonnull IconData... icons) {
        return new IconData.MultiIcon(Lists.newArrayList(icons));
    }

    private static class ImageIcon extends IconData {

        private final Sprite sprite;

        private ImageIcon(Sprite sprite) {
            this.sprite = sprite;
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void render(EasyGuiGraphics gui, int x, int y) {
            gui.blitSprite(this.sprite, x, y);
        }
    }

    private static class ItemIcon extends IconData {

        private final ItemStack iconStack;

        private final String countTextOverride;

        private ItemIcon(ItemStack iconStack, @Nullable String countTextOverride) {
            this.iconStack = iconStack;
            this.countTextOverride = countTextOverride;
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void render(EasyGuiGraphics gui, int x, int y) {
            gui.renderItem(this.iconStack, x, y, this.countTextOverride);
        }
    }

    private static class MultiIcon extends IconData {

        private final List<IconData> icons;

        private MultiIcon(List<IconData> icons) {
            this.icons = icons;
        }

        @Override
        public void render(EasyGuiGraphics gui, int x, int y) {
            for (IconData icon : this.icons) {
                icon.render(gui, x, y);
            }
        }
    }

    private static class TextIcon extends IconData {

        private final Component iconText;

        private final int textColor;

        private TextIcon(Component iconText, int textColor) {
            this.iconText = iconText;
            this.textColor = textColor;
        }

        @Override
        public void render(EasyGuiGraphics gui, int x, int y) {
            int xPos = x + 8 - gui.font.width(this.iconText) / 2;
            int yPos = y + (16 - 9) / 2;
            gui.drawShadowed(this.iconText, xPos, yPos, this.textColor);
        }
    }
}