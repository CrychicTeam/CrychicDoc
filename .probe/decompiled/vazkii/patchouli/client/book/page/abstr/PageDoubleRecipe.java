package vazkii.patchouli.client.book.page.abstr;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;

public abstract class PageDoubleRecipe<T> extends PageWithText {

    @SerializedName("recipe")
    ResourceLocation recipeId;

    @SerializedName("recipe2")
    ResourceLocation recipe2Id;

    String title;

    protected transient T recipe1;

    protected transient T recipe2;

    protected transient Component title1;

    protected transient Component title2;

    @Override
    public void build(Level level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
        super.build(level, entry, builder, pageNum);
        this.recipe1 = this.loadRecipe(level, builder, entry, this.recipeId);
        this.recipe2 = this.loadRecipe(level, builder, entry, this.recipe2Id);
        if (this.recipe1 == null && this.recipe2 != null) {
            this.recipe1 = this.recipe2;
            this.recipe2 = null;
        }
        boolean customTitle = this.title != null && !this.title.isEmpty();
        this.title1 = !customTitle ? this.getRecipeOutput(level, this.recipe1).getHoverName() : this.i18nText(this.title);
        this.title2 = Component.literal("-");
        if (this.recipe2 != null) {
            this.title2 = (Component) (!customTitle ? this.getRecipeOutput(level, this.recipe2).getHoverName() : Component.empty());
            if (this.title1.equals(this.title2)) {
                this.title2 = Component.empty();
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float pticks) {
        if (this.recipe1 != null) {
            int recipeX = this.getX();
            int recipeY = this.getY();
            this.drawRecipe(graphics, this.recipe1, recipeX, recipeY, mouseX, mouseY, false);
            if (this.recipe2 != null) {
                this.drawRecipe(graphics, this.recipe2, recipeX, recipeY + this.getRecipeHeight() - (this.title2.getString().isEmpty() ? 10 : 0), mouseX, mouseY, true);
            }
        }
        super.render(graphics, mouseX, mouseY, pticks);
    }

    @Override
    public int getTextHeight() {
        return this.getY() + this.getRecipeHeight() * (this.recipe2 == null ? 1 : 2) - (this.title2.getString().isEmpty() ? 23 : 13);
    }

    @Override
    public boolean shouldRenderText() {
        return this.getTextHeight() + 10 < 156;
    }

    protected abstract void drawRecipe(GuiGraphics var1, T var2, int var3, int var4, int var5, int var6, boolean var7);

    protected abstract T loadRecipe(Level var1, BookContentsBuilder var2, BookEntry var3, ResourceLocation var4);

    protected abstract ItemStack getRecipeOutput(Level var1, T var2);

    protected abstract int getRecipeHeight();

    protected int getX() {
        return 9;
    }

    protected int getY() {
        return 4;
    }

    protected Component getTitle(boolean second) {
        return second ? this.title2 : this.title1;
    }
}