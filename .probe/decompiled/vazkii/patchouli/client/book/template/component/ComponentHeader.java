package vazkii.patchouli.client.book.template.component;

import com.google.gson.annotations.SerializedName;
import java.util.function.UnaryOperator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.template.TemplateComponent;

public class ComponentHeader extends TemplateComponent {

    public IVariable text;

    @SerializedName("color")
    public IVariable colorStr;

    boolean centered = true;

    float scale = 1.0F;

    transient Component actualText;

    transient int color;

    @Override
    public void build(BookContentsBuilder builder, BookPage page, BookEntry entry, int pageNum) {
        try {
            this.color = Integer.parseInt(this.colorStr.asString(""), 16);
        } catch (NumberFormatException var6) {
            this.color = page.book.headerColor;
        }
        if (this.x == -1) {
            this.x = 58;
        }
        if (this.y == -1) {
            this.y = 0;
        }
    }

    @Override
    public void render(GuiGraphics graphics, BookPage page, int mouseX, int mouseY, float pticks) {
        graphics.pose().pushPose();
        graphics.pose().translate((float) this.x, (float) this.y, 0.0F);
        graphics.pose().scale(this.scale, this.scale, this.scale);
        if (this.centered) {
            page.parent.drawCenteredStringNoShadow(graphics, page.i18n(this.actualText.getString()), 0, 0, this.color);
        } else {
            graphics.drawString(page.fontRenderer, page.i18n(this.actualText.getString()), 0, 0, this.color, false);
        }
        graphics.pose().popPose();
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        super.onVariablesAvailable(lookup);
        this.actualText = ((IVariable) lookup.apply(this.text)).as(Component.class);
        this.colorStr = (IVariable) lookup.apply(this.colorStr);
    }
}