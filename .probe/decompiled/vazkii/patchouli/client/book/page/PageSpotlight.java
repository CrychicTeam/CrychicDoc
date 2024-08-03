package vazkii.patchouli.client.book.page;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

public class PageSpotlight extends PageWithText {

    IVariable item;

    String title;

    @SerializedName("link_recipe")
    boolean linkRecipe;

    transient Ingredient ingredient;

    @Override
    public void build(Level level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(level, entry, builder, pageNum);
        this.ingredient = this.item.as(Ingredient.class);
        if (this.linkRecipe) {
            for (ItemStack stack : this.ingredient.getItems()) {
                entry.addRelevantStack(builder, stack, pageNum);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pticks) {
        int w = 66;
        int h = 26;
        RenderSystem.enableBlend();
        graphics.blit(this.book.craftingTexture, 58 - w / 2, 10, 0.0F, (float) (128 - h), w, h, 128, 256);
        Component toDraw;
        if (this.title != null && !this.title.isEmpty()) {
            toDraw = this.i18nText(this.title);
        } else {
            toDraw = this.ingredient.getItems()[0].getHoverName();
        }
        this.parent.drawCenteredStringNoShadow(graphics, toDraw.getVisualOrderText(), 58, 0, this.book.headerColor);
        this.parent.renderIngredient(graphics, 50, 15, mouseX, mouseY, this.ingredient);
        super.render(graphics, mouseX, mouseY, pticks);
    }

    @Override
    public int getTextHeight() {
        return 40;
    }
}