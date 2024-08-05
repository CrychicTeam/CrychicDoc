package vazkii.patchouli.client.book.template.component;

import com.google.gson.annotations.SerializedName;
import java.util.function.UnaryOperator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.template.TemplateComponent;

public class ComponentText extends TemplateComponent {

    public IVariable text;

    @SerializedName("color")
    public IVariable colorStr;

    @SerializedName("max_width")
    int maxWidth = 116;

    @SerializedName("line_height")
    int lineHeight = 9;

    transient Component actualText;

    transient BookTextRenderer textRenderer;

    transient int color;

    @Override
    public void build(BookContentsBuilder builder, BookPage page, BookEntry entry, int pageNum) {
        try {
            this.color = Integer.parseInt(this.colorStr.asString(""), 16);
        } catch (NumberFormatException var6) {
            this.color = page.book.textColor;
        }
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        super.onVariablesAvailable(lookup);
        this.actualText = ((IVariable) lookup.apply(this.text)).as(Component.class);
        this.colorStr = (IVariable) lookup.apply(this.colorStr);
    }

    @Override
    public void onDisplayed(BookPage page, GuiBookEntry parent, int left, int top) {
        this.textRenderer = new BookTextRenderer(parent, this.actualText, this.x, this.y, this.maxWidth, this.lineHeight, this.color);
    }

    @Override
    public void render(GuiGraphics graphics, BookPage page, int mouseX, int mouseY, float pticks) {
        this.textRenderer.render(graphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(BookPage page, double mouseX, double mouseY, int mouseButton) {
        return this.textRenderer.click(mouseX, mouseY, mouseButton);
    }
}