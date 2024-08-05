package fr.frinn.custommachinery.client.screen.popup;

import fr.frinn.custommachinery.client.screen.BaseScreen;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class InfoPopup extends PopupScreen {

    private final List<Component> text = new ArrayList();

    public InfoPopup(BaseScreen parent, int xSize, int ySize) {
        super(parent, xSize, ySize);
    }

    public InfoPopup text(Component... text) {
        this.text.addAll(Arrays.asList(text));
        return this;
    }

    @Override
    protected void init() {
        super.init();
        this.m_142416_(Button.builder(Component.translatable("custommachinery.gui.popup.confirm"), button -> this.parent.closePopup(this)).bounds(this.x + 10, this.y + this.ySize - 30, this.xSize - 20, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        List<FormattedCharSequence> list = this.text.stream().flatMap(component -> this.f_96547_.split(component, this.xSize - 20).stream()).toList();
        for (int i = 0; i < list.size(); i++) {
            FormattedCharSequence text = (FormattedCharSequence) list.get(i);
            int width = this.f_96547_.width(text);
            int x = (this.xSize - width) / 2 + this.x;
            graphics.drawString(this.f_96547_, text, x, this.y + i * 9 + 5, 0, false);
        }
    }
}