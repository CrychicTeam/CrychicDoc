package vazkii.patchouli.client.book.template.component;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.template.TemplateComponent;

public class ComponentTooltip extends TemplateComponent {

    @SerializedName("tooltip")
    public IVariable[] tooltipRaw;

    int width;

    int height;

    transient List<Component> tooltip;

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        super.onVariablesAvailable(lookup);
        for (int i = 0; i < this.tooltipRaw.length; i++) {
            this.tooltipRaw[i] = (IVariable) lookup.apply(this.tooltipRaw[i]);
        }
    }

    @Override
    public void onDisplayed(BookPage page, GuiBookEntry parent, int left, int top) {
        this.tooltip = new ArrayList();
        for (IVariable s : this.tooltipRaw) {
            this.tooltip.add((Component) s.as(Component.class));
        }
    }

    @Override
    public void render(GuiGraphics graphics, BookPage page, int mouseX, int mouseY, float pticks) {
        if (page.parent.isMouseInRelativeRange((double) mouseX, (double) mouseY, this.x, this.y, this.width, this.height)) {
            page.parent.setTooltip(this.tooltip);
        }
    }
}