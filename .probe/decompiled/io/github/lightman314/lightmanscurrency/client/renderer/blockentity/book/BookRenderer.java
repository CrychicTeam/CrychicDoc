package io.github.lightman314.lightmanscurrency.client.renderer.blockentity.book;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.lightman314.lightmanscurrency.client.renderer.blockentity.book.renderers.NormalBookRenderer;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.restrictions.BookRestriction;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public abstract class BookRenderer {

    protected final ItemStack book;

    private static final List<BookRendererGenerator> GENERATORS = new ArrayList();

    protected BookRenderer(ItemStack book) {
        this.book = book;
    }

    public static void register(@Nonnull BookRendererGenerator generator) {
        GENERATORS.add(generator);
    }

    public static BookRenderer GetRenderer(@Nonnull ItemStack bookStack) {
        for (BookRendererGenerator generator : GENERATORS) {
            BookRenderer renderer = generator.createRendererForItem(bookStack);
            if (renderer != null) {
                return renderer;
            }
        }
        return BookRestriction.CanSellAsBook(bookStack) ? NormalBookRenderer.INSTANCE : null;
    }

    public abstract void render(BlockEntity var1, float var2, @NotNull PoseStack var3, @NotNull MultiBufferSource var4, int var5, int var6);

    protected final void renderModel(ResourceLocation modelResource, PoseStack pose, MultiBufferSource buffer, int lightLevel) {
        Minecraft mc = Minecraft.getInstance();
        BakedModel model = mc.getModelManager().getModel(modelResource);
        ItemRenderer itemRenderer = mc.getItemRenderer();
        itemRenderer.render(this.book, ItemDisplayContext.FIXED, false, pose, buffer, lightLevel, OverlayTexture.NO_OVERLAY, model);
    }
}