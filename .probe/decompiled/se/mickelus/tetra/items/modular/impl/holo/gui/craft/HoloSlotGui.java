package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class HoloSlotGui extends GuiClickable {

    private final GuiTexture backdrop;

    private final GuiString slotString;

    public HoloSlotGui(int x, int y, GuiAttachment attachment, String slot, String label, Consumer<String> onSelect) {
        super(x, y, 11, 11, () -> onSelect.accept(slot));
        this.setAttachmentPoint(attachment);
        this.backdrop = new GuiTexture(-1, -1, 11, 11, 68, 0, GuiTextures.workbench);
        this.backdrop.setAttachmentPoint(attachment);
        this.backdrop.setAttachmentAnchor(attachment);
        this.addChild(this.backdrop);
        this.slotString = new GuiString(-15, 1, label);
        if (GuiAttachment.topLeft.equals(attachment)) {
            this.slotString.setX(14);
        }
        this.slotString.setAttachmentPoint(attachment);
        this.slotString.setAttachmentAnchor(attachment);
        this.addChild(this.slotString);
        this.width = this.slotString.getWidth() + 14;
    }

    @Override
    protected void onFocus() {
        super.onFocus();
        this.backdrop.setColor(16777164);
        this.slotString.setColor(16777164);
    }

    @Override
    protected void onBlur() {
        super.onBlur();
        this.backdrop.setColor(16777215);
        this.slotString.setColor(16777215);
    }
}