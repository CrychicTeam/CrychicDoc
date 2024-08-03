package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class HoloSlotMajorGui extends GuiClickable {

    private final GuiTexture backdrop;

    private final GuiString slotString;

    public HoloSlotMajorGui(int x, int y, GuiAttachment attachment, String slot, String label, Consumer<String> onSelect) {
        super(x, y, 64, 17, () -> onSelect.accept(slot));
        this.setAttachmentPoint(attachment);
        this.backdrop = new GuiTexture(0, 0, 15, 15, 52, 0, GuiTextures.workbench);
        if (GuiAttachment.topRight.equals(attachment)) {
            this.backdrop.setX(-1);
        }
        this.backdrop.setAttachment(attachment);
        this.addChild(this.backdrop);
        this.slotString = new GuiString(18, 3, label);
        if (GuiAttachment.topRight.equals(attachment)) {
            this.slotString.setX(-18);
        }
        this.slotString.setAttachment(attachment);
        this.addChild(this.slotString);
        if ("".equals(label)) {
            this.slotString.setString(I18n.get("tetra.holo.craft.slot"));
        }
        this.width = this.slotString.getWidth() + 18;
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