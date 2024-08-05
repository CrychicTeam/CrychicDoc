package se.mickelus.tetra.items.modular.impl.holo.gui.system;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.tetra.items.modular.impl.holo.gui.HoloRootBaseGui;

@ParametersAreNonnullByDefault
public class HoloSystemRootGui extends HoloRootBaseGui {

    public HoloSystemRootGui(int x, int y) {
        super(x, y);
        GuiString test = new GuiString(0, 0, ChatFormatting.OBFUSCATED + "system");
        test.setAttachment(GuiAttachment.middleCenter);
        this.addChild(test);
    }
}