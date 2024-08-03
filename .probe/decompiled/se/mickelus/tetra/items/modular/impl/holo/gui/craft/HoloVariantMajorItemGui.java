package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleGlyph;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.schematic.OutcomePreview;

@ParametersAreNonnullByDefault
public class HoloVariantMajorItemGui extends HoloVariantItemGui {

    public HoloVariantMajorItemGui(int x, int y, OutcomePreview outcome, @Nullable String label, Consumer<OutcomePreview> onHover, Consumer<OutcomePreview> onBlur, Consumer<OutcomePreview> onSelect) {
        super(x, y, 16, 16, outcome, onHover, onBlur, onSelect);
        this.backdrop = new GuiTexture(1, 0, 15, 15, 52, 0, GuiTextures.workbench);
        this.addChild(this.backdrop);
        if (label != null) {
            GuiString labelElement = new GuiStringOutline(label.startsWith("-") ? -2 : 1, 0, label);
            labelElement.setColor(outcome.glyph.tint);
            labelElement.setAttachment(GuiAttachment.middleCenter);
            this.addChild(labelElement);
        } else {
            this.addChild(new GuiModuleGlyph(0, 0, 16, 16, outcome.glyph).setShift(false));
        }
        this.material.setX(0);
        this.material.setY(0);
    }

    @Override
    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        mouseX -= refX + this.x;
        mouseY -= refY + this.y;
        boolean gainFocus = mouseX + mouseY >= 8;
        if (mouseX + mouseY > 24) {
            gainFocus = false;
        }
        if (mouseX - mouseY > 8) {
            gainFocus = false;
        }
        if (mouseY - mouseX > 8) {
            gainFocus = false;
        }
        if (gainFocus != this.hasFocus) {
            this.hasFocus = gainFocus;
            if (this.hasFocus) {
                this.onFocus();
            } else {
                this.onBlur();
            }
        }
    }
}