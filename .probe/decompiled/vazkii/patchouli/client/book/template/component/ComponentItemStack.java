package vazkii.patchouli.client.book.template.component;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.UnaryOperator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.template.TemplateComponent;

public class ComponentItemStack extends TemplateComponent {

    public IVariable item;

    private boolean framed;

    @SerializedName("link_recipe")
    private boolean linkedRecipe;

    private transient ItemStack[] items;

    @Override
    public void build(BookContentsBuilder builder, BookPage page, BookEntry entry, int pageNum) {
        if (this.linkedRecipe) {
            for (ItemStack stack : this.items) {
                entry.addRelevantStack(builder, stack, pageNum);
            }
        }
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        super.onVariablesAvailable(lookup);
        this.items = ((IVariable) lookup.apply(this.item)).as(ItemStack[].class);
    }

    @Override
    public void render(GuiGraphics graphics, BookPage page, int mouseX, int mouseY, float pticks) {
        if (this.items.length != 0) {
            if (this.framed) {
                RenderSystem.enableBlend();
                graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                graphics.blit(page.book.craftingTexture, this.x - 5, this.y - 5, 20.0F, 102.0F, 26, 26, 128, 256);
            }
            page.parent.renderItemStack(graphics, this.x, this.y, mouseX, mouseY, this.items[page.parent.ticksInBook / 20 % this.items.length]);
        }
    }
}