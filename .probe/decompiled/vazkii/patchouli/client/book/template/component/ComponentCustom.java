package vazkii.patchouli.client.book.template.component;

import com.google.gson.annotations.SerializedName;
import java.util.function.UnaryOperator;
import net.minecraft.client.gui.GuiGraphics;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.template.TemplateComponent;
import vazkii.patchouli.common.util.SerializationUtil;

public class ComponentCustom extends TemplateComponent {

    @SerializedName("class")
    String clazz;

    private transient ICustomComponent callbacks;

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        super.onVariablesAvailable(lookup);
        try {
            Class<?> classObj = Class.forName(this.clazz);
            this.callbacks = (ICustomComponent) SerializationUtil.RAW_GSON.fromJson(this.sourceObject, classObj);
            this.callbacks.onVariablesAvailable(lookup);
        } catch (Exception var3) {
            throw new RuntimeException("Failed to create custom component " + this.clazz, var3);
        }
    }

    @Override
    public void build(BookContentsBuilder builder, BookPage page, BookEntry entry, int pageNum) {
        this.callbacks.build(this.x, this.y, pageNum);
    }

    @Override
    public void render(GuiGraphics graphics, BookPage page, int mouseX, int mouseY, float pticks) {
        this.callbacks.render(graphics, page.parent, pticks, mouseX, mouseY);
    }

    @Override
    public void onDisplayed(BookPage page, GuiBookEntry parent, int left, int top) {
        this.callbacks.onDisplayed(parent);
    }

    @Override
    public boolean mouseClicked(BookPage page, double mouseX, double mouseY, int mouseButton) {
        return this.callbacks.mouseClicked(page.parent, mouseX, mouseY, mouseButton);
    }
}