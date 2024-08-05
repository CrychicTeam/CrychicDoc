package se.mickelus.tetra.gui;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;

@ParametersAreNonnullByDefault
public class GuiSliderSegmented extends GuiClickable {

    private final int valueSteps;

    private final Consumer<Integer> onChange;

    private final GuiElement currentIndicator;

    private final GuiElement hoverIndicator;

    private boolean isDragging = false;

    private int value = 0;

    public GuiSliderSegmented(int x, int y, int width, int valueSteps, Consumer<Integer> onChange) {
        super(x, y, width, 12, () -> {
        });
        this.addChild(new GuiRect(5, 7, width - 9, 1, 8355711).setOpacity(0.7F));
        this.addChild(new GuiRect(0, 7, 4, 1, 8355711));
        this.addChild(new GuiRect(1, 7, 4, 1, 8355711).setAttachment(GuiAttachment.topRight));
        this.addChild(new GuiRect(0, 4, 1, 3, 8355711));
        this.addChild(new GuiRect(1, 4, 1, 3, 8355711).setAttachment(GuiAttachment.topRight));
        for (int i = 0; i < valueSteps; i++) {
            if (((valueSteps - 1) / 2 - i) % 3 == 0) {
                this.addChild(new GuiRect(i * width / (valueSteps - 1) - 1, 7, 3, 1, 0));
                this.addChild(new GuiRect(i * width / (valueSteps - 1), 4, 1, 4, 8355711));
            } else {
                this.addChild(new GuiRect(i * width / (valueSteps - 1), 5, 1, 2, 8355711).setOpacity(0.7F));
            }
        }
        this.hoverIndicator = new GuiElement(0, 4, 1, 4);
        this.hoverIndicator.addChild(new GuiRect(-1, 0, 3, 5, 0));
        this.hoverIndicator.addChild(new GuiRect(0, -1, 1, 3, 8355711));
        this.hoverIndicator.addChild(new GuiRect(0, 3, 1, 1, 8355711));
        this.hoverIndicator.addChild(new GuiRect(0, 5, 1, 1, 8355711));
        this.hoverIndicator.setVisible(false);
        this.addChild(this.hoverIndicator);
        this.currentIndicator = new GuiElement(0, 4, 1, 4);
        this.currentIndicator.addChild(new GuiRect(-1, 0, 3, 5, 0));
        this.currentIndicator.addChild(new GuiRect(0, -1, 1, 3, 16777215));
        this.currentIndicator.addChild(new GuiRect(0, 3, 1, 1, 16776960));
        this.currentIndicator.addChild(new GuiRect(0, 5, 1, 1, 16777215));
        this.addChild(this.currentIndicator);
        this.valueSteps = valueSteps;
        this.onChange = onChange;
    }

    public void setValue(int value) {
        this.value = value;
        this.currentIndicator.setX(value * this.width / (this.valueSteps - 1));
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        if (super.onMouseClick(x, y, button)) {
            this.isDragging = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onMouseRelease(int x, int y, int button) {
        this.isDragging = false;
    }

    protected int calculateSegment(int refX, int mouseX) {
        return Math.round((float) (this.valueSteps - 1) * Math.min(Math.max((float) (mouseX - refX - this.x) / (1.0F * (float) this.width), 0.0F), 1.0F));
    }

    @Override
    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        this.elements.stream().filter(GuiElement::isVisible).forEach(element -> element.updateFocusState(refX + this.x + getXOffset(this, element.getAttachmentAnchor()) - getXOffset(element, element.getAttachmentPoint()), refY + this.y + getYOffset(this, element.getAttachmentAnchor()) - getYOffset(element, element.getAttachmentPoint()), mouseX, mouseY));
        boolean gainFocus = mouseX >= this.getX() + refX - 5 && mouseX < this.getX() + refX + this.getWidth() + 10 && mouseY >= this.getY() + refY && mouseY < this.getY() + refY + this.getHeight();
        if (gainFocus != this.hasFocus) {
            this.hasFocus = gainFocus;
            if (this.hasFocus) {
                this.onFocus();
            } else {
                this.onBlur();
            }
        }
    }

    @Override
    protected void onFocus() {
        this.hoverIndicator.setVisible(true);
    }

    @Override
    protected void onBlur() {
        this.hoverIndicator.setVisible(false);
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (this.isDragging) {
            int newSegment = this.calculateSegment(refX, mouseX);
            if (newSegment != this.value) {
                this.value = newSegment;
                this.onChange.accept(this.value);
                this.currentIndicator.setX(this.value * this.width / (this.valueSteps - 1));
                this.hoverIndicator.setX(this.value * this.width / (this.valueSteps - 1));
            }
        } else if (this.hoverIndicator.isVisible()) {
            this.hoverIndicator.setX(this.calculateSegment(refX, mouseX) * this.width / (this.valueSteps - 1));
        }
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }
}