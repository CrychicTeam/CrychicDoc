package journeymap.client.ui.component;

import journeymap.client.render.draw.DrawUtil;
import net.minecraft.client.gui.GuiGraphics;

public class DropDownItem extends Button {

    private Object id;

    private String label;

    private SelectableParent parent;

    private net.minecraft.client.gui.components.Button.OnPress onPress;

    private boolean autoClose = true;

    public DropDownItem(SelectableParent parent, Object id, String label, net.minecraft.client.gui.components.Button.OnPress onPress, String... toolTip) {
        this(parent, id, label, onPress);
        this.setTooltip(toolTip);
    }

    public DropDownItem(SelectableParent parent, Object id, String label, net.minecraft.client.gui.components.Button.OnPress onPress) {
        super(label);
        this.onPress = onPress;
        this.label = label;
        this.id = id;
        this.parent = parent;
        this.setTextOnly(this.fontRenderer);
    }

    public DropDownItem(SelectableParent parent, Object id, String label, String... toolTip) {
        this(parent, id, label);
        this.setTooltip(toolTip);
    }

    public DropDownItem(SelectableParent parent, Object id, String label) {
        super(label);
        this.label = label;
        this.id = id;
        this.parent = parent;
        this.setTextOnly(this.fontRenderer);
    }

    public DropDownItem(SelectableParent parent, Object id, boolean autoClose, String label, net.minecraft.client.gui.components.Button.OnPress onPress) {
        this(parent, id, label, onPress);
        this.autoClose = autoClose;
    }

    public void press() {
        this.onPress.onPress(this);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderSpecialDecoration(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height) {
        if (!this.autoClose) {
            graphics.pose().pushPose();
            DrawUtil.drawCenteredLabel(graphics, ">", (double) (x + width - 10), (double) this.getMiddleY(), null, 0.0F, this.varLabelColor, 1.0F, 1.0, this.drawLabelShadow);
            graphics.pose().popPose();
        }
    }

    public Object getId() {
        return this.id;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    public boolean isAutoClose() {
        return this.autoClose;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.f_93622_) {
            this.parent.setSelected(this);
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean isHovered() {
        return super.isHovered();
    }
}